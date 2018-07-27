/**
 * $RCSfile$
 * $Revision: 11616 $
 * $Date: 2010-02-09 13:40:11 +0100 (mar. 09 févr. 2010) $
 * <p/>
 * Copyright 2003-2007 Jive Software.
 * <p/>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack;

import com.viettel.util.Log;

import org.jivesoftware.smack.Connection.ListenerWrapper;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.sasl.SASLMechanism.Challenge;
import org.jivesoftware.smack.sasl.SASLMechanism.Failure;
import org.jivesoftware.smack.sasl.SASLMechanism.Success;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Listens for XML traffic from the XMPP server and parses it into packet
 * objects. The packet reader also invokes all packet listeners and collectors.
 * <p/>
 *
 * @author Matt Tucker
 * @see Connection#createPacketCollector
 * @see Connection#addPacketListener
 */
class PacketReader {

    private static final String TAG = PacketReader.class.getSimpleName();
    private Thread readerThread;
    private ExecutorService listenerExecutor;

    private XMPPConnection connection;
    private XmlPullParser parser;
    private boolean done;

    private String connectionID = null;
    private Semaphore connectionSemaphore;

    protected PacketReader(final XMPPConnection connection) {
        this.connection = connection;
        this.init();
    }

    /**
     * Initializes the reader in order to be used. The reader is initialized
     * during the first connection and when reconnecting due to an abruptly
     * disconnection.
     */
    protected void init() {
        done = false;
        connectionID = null;

        readerThread = new Thread() {
            public void run() {
                parsePackets(this);
            }
        };
        readerThread.setName("Smack Packet Reader ("
                + connection.connectionCounterValue + ")");
        readerThread.setDaemon(true);

        // Create an executor to deliver incoming packets to listeners. We'll
        // use a single
        // thread with an unbounded queue.
        listenerExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable,
                        "Smack Listener Processor ("
                                + connection.connectionCounterValue
                                + ")"
                );
                thread.setDaemon(true);
                return thread;
            }
        });

        resetParser();
    }

    /**
     * Starts the packet reader thread and returns once a connection to the
     * server has been established. A connection will be attempted for a maximum
     * of five seconds. An XMPPException will be thrown if the connection fails.
     *
     * @throws XMPPException if the server fails to send an opening stream back for more
     *                       than five seconds.
     */
    public void startup() throws XMPPException {
        connectionSemaphore = new Semaphore(1);
        if (readerThread != null && readerThread.isAlive()) return; //addded by thaodv
        readerThread.start();
        // Wait for stream tag before returing. We'll wait a couple of seconds
        // before
        // giving up and throwing an error.
        try {
            connectionSemaphore.acquire();

            // A waiting thread may be woken up before the wait time or a notify
            // (although this is a rare thing). Therefore, we continue waiting
            // until either a connectionID has been set (and hence a notify was
            // made) or the total wait time has elapsed.
            int waitTime = SmackConfiguration.getPacketReplyTimeout();
//            Log.i(TAG, "waitTime = "+ 3*waitTime + "ms");
            connectionSemaphore.tryAcquire(1 * waitTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            // Ignore.
            Log.e(TAG, "Exception", ie);
        }
        if (connectionID == null) {
            throw new XMPPException(
                    "java.io.IOException");
        } else {
            connection.connectionID = connectionID;
        }
    }

    /**
     * Shuts the packet reader down.
     */
    public void shutdown() {
        // Notify connection listeners of the connection closing if done hasn't
        // already been set.
//        Log.i(TAG, "[VIETTEL] shutdown");
        if (!done) {
            for (ConnectionListener listener : connection
                    .getConnectionListeners()) {
                try {
                    listener.connectionClosed();
                } catch (Exception e) {
                    // Cath and print any exception so we can recover
                    // from a faulty listener and finish the shutdown process
                    Log.e(TAG, "Exception", e);
                }
            }
        }
        done = true;

        // == THAODV begin
        readerThread.interrupt();
        parser = null;
        // == THAODV end

        // Shut down the listener executor.
        listenerExecutor.shutdown();

    }

    /**
     * Cleans up all resources used by the packet reader.
     */
    void cleanup() {
        connection.recvListeners.clear();
        connection.collectors.clear();
    }

    void notifyConnectionClose() {
        done = true;
        // Closes the connection temporary. A reconnection is possible
        connection.shutdown(new Presence(Presence.Type.unavailable));
        // Wait 100 ms for socket clean up and shutdown
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            // Ignore.
        }
        // Print the stack trace to help catch the problem
        // Notify connection listeners of the error.
        for (ConnectionListener listener : connection.getConnectionListeners()) {
            try {
                listener.connectionClosed();
            } catch (Exception e2) {
                // Catch and print any exception so we can recover
                // from a faulty listener
                e2.printStackTrace();
            }
        }
    }


    /**
     * Sends a notification indicating that the connection was reconnected
     * successfully.
     */
    protected void notifyReconnection() {
        // Notify connection listeners of the reconnection.
        for (ConnectionListener listener : connection.getConnectionListeners()) {
            try {
                listener.reconnectionSuccessful();
            } catch (Exception e) {
                // Catch and print any exception so we can recover
                // from a faulty listener
                Log.e(TAG, "Exception", e);
            }
        }
    }

    /**
     * Resets the parser using the latest connection's reader. Reseting the
     * parser is necessary when the plain connection has been secured or when a
     * new opening stream element is going to be sent by the server.
     */
    private void resetParser() {
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(connection.reader);
        } catch (XmlPullParserException xppe) {
            Log.f(TAG, "[VIETTEL] resetParser has an exception ", xppe);
        }
    }

    /**
     * Parse top-level packets in order to process them further.
     *
     * @param thread the thread that is being used by the reader to parse incoming
     *               packets.
     */
    private void parsePackets(Thread thread) {
        try {
            int eventType = parser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("message")) {
                        processPacket(PacketParserUtils.parseMessage(parser));
                    } else if (parser.getName().equals("iq")) {
                        processPacket(PacketParserUtils.parseIQ(parser, connection));
                    } else if (parser.getName().equals("presence")) {
                        processPacket(PacketParserUtils.parsePresence(parser));
                    }
                    // We found an opening stream. Record information about it,
                    // then notify
                    // the connectionID lock so that the packet reader startup
                    // can finish.
                    else if (parser.getName().equals("stream")) {
                        // Ensure the correct jabber:client namespace is being
                        // used.
                        if ("jabber:client".equals(parser.getNamespace(null))) {
                            // Get the connection id.
                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                if (parser.getAttributeName(i).equals("id")) {
                                    // Save the connectionID
                                    connectionID = parser.getAttributeValue(i);
                                    if (!"1.0".equals(parser.getAttributeValue(
                                            "", "version"))) {
                                        // Notify that a stream has been opened
                                        // if the server is not XMPP 1.0 compliant
                                        // otherwise make the notification after TLS has been
                                        // negotiated or if TLS is not supported
                                        releaseConnectionIDLock();
                                    }
                                } else if (parser.getAttributeName(i).equals(
                                        "from")) {
                                    // Use the server name that the server says
                                    // that it is.
                                    connection.config.setServiceName(parser
                                            .getAttributeValue(i));
                                }
                            }
                        }
                    } else if (parser.getName().equals("error")) {
                        throw new XMPPException(
                                PacketParserUtils.parseStreamError(parser));
                    } else if (parser.getName().equals("features")) {
                        parseFeatures(parser);
                    } else if (parser.getName().equals("proceed")) {
                        // Secure the connection by negotiating TLS
                        connection.proceedTLSReceived();
                        // Reset the state of the parser since a new stream
                        // element is going
                        // to be sent by the server
                        resetParser();
                    } else if (parser.getName().equals("failure")) {
                        String namespace = parser.getNamespace(null);
                        if ("urn:ietf:params:xml:ns:xmpp-tls".equals(namespace)) {
                            // TLS negotiation has failed. The server will close
                            // the connection
                            throw new Exception("TLS negotiation has failed");
                        } else if ("http://jabber.org/protocol/compress"
                                .equals(namespace)) {
                            // Stream compression has been denied. This is a
                            // recoverable
                            // situation. It is still possible to authenticate
                            // and
                            // use the connection but using an uncompressed
                            // connection
                            connection.streamCompressionDenied();
                        } else {
                            // SASL authentication has failed. The server may
                            // close the connection
                            // depending on the number of retries
                            final Failure failure = PacketParserUtils
                                    .parseSASLFailure(parser);
                            processPacket(failure);
                            if (failure.isLocked()) {
                                connection.getSASLAuthentication()
                                        .authenticationFailed(failure);
                            } else {
                                connection.getSASLAuthentication()
                                        .authenticationFailed(failure);
                            }
                        }
                    } else if (parser.getName().equals("challenge")) {
                        // The server is challenging the SASL authentication
                        // made by the client
                        String challengeData = parser.nextText();
                        connection.getSASLAuthentication().challengeReceived(
                                challengeData);
                        processPacket(new Challenge(challengeData));
                    } else if (parser.getName().equals("success")) {
                        String token = parser.getAttributeValue("", "token");
                        String domainFile = parser.getAttributeValue("", "domain_file");
                        String domainMsg = parser.getAttributeValue("", "domain_msg");
                        String domainOnMedia = parser.getAttributeValue("", "domain_on_media");
//                        if (parser.getAttributeCount() > 0) {
//                            token = parser.getAttributeValue(0);
//                        }
                        Success success = new Success(parser.nextText(), token, domainFile, domainMsg, domainOnMedia);
                        processPacket(success);
                        // We now need to bind a resource for the connection
                        // Open a new stream and wait for the response
                        connection.packetWriter.openStream();
                        //ko chay qua ham nay nua
                        // Reset the state of the parser since a new stream
                        // element is going
                        // to be sent by the server
                        resetParser();
                        // The SASL authentication with the server was
                        // successful. The next step
                        // will be to bind the resource
                        connection.getSASLAuthentication().authenticated();
                    } else if (parser.getName().equals("compressed")) {
                        // Server confirmed that it's possible to use stream
                        // compression. Start
                        // stream compression
                        connection.startStreamCompression();
                        // Reset the state of the parser since a new stream
                        // element is going
                        // to be sent by the server
                        resetParser();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("stream")) {
                        // Disconnect the connection
                        Log.f(TAG, "received </stream> --> close connection");
                        notifyConnectionClose();
                    }
                }
                eventType = parser.next();
            } while (!done && eventType != XmlPullParser.END_DOCUMENT
                    && thread == readerThread);
        } catch (Exception e) {
            if (!done && connection != null) {
                Log.e(TAG, "Exception", e);
                Log.f(TAG, "notifyConnectionError", e);
                done = true;
                releaseConnectionIDLock();
                connection.notifyConnectionError(e);
            }
        }
    }

    /**
     * Releases the connection ID lock so that the thread that was waiting can
     * resume. The lock will be released when one of the following three
     * conditions is met:
     * <p/>
     * <p/>
     * 1) An opening stream was sent from a non XMPP 1.0 compliant server 2)
     * Stream features were received from an XMPP 1.0 compliant server that does
     * not support TLS 3) TLS negotiation was successful
     */
    private void releaseConnectionIDLock() {
        Log.i(TAG, "releaseConnectionIDLock");
        if (connectionSemaphore == null) return; //avoid crash
        connectionSemaphore.release();
    }

    /**
     * Processes a packet after it's been fully parsed by looping through the
     * installed packet collectors and listeners and letting them examine the
     * packet to see if they are a match with the filter.
     *
     * @param packet the packet to process.
     */
    private void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }
//        Log.i(TAG, "[VIETTEL] Received: " + packet.toXML());
        // Loop through all collectors and notify the appropriate ones.
        for (PacketCollector collector : connection.getPacketCollectors()) {
//            Log.i(TAG, "[VIETTEL] collector =  " + collector.toString());
            collector.processPacket(packet);
        }
        // Deliver the incoming packet to listeners.
//        Log.i(TAG, "[VIETTEL] listenerExecutor submit the packet to listener ");
        listenerExecutor.submit(new ListenerNotification(packet));
    }

    private void parseFeatures(XmlPullParser parser) throws Exception {
        boolean startTLSReceived = false;
        boolean startTLSRequired = false;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("starttls")) {
                    startTLSReceived = true;
                } else if (parser.getName().equals("mechanisms")) {
                    // The server is reporting available SASL mechanisms. Store
                    // this information
                    // which will be used later while logging (i.e.
                    // authenticating) into
                    // the server
                    connection.getSASLAuthentication().setAvailableSASLMethods(
                            PacketParserUtils.parseMechanisms(parser));
                } else if (parser.getName().equals("bind")) {
                    // The server requires the client to bind a resource to the
                    // stream
                    connection.getSASLAuthentication().bindingRequired();
                } else if (parser.getName().equals("session")) {
                    // The server supports sessions
                    connection.getSASLAuthentication().sessionsSupported();
                } else if (parser.getName().equals("compression")) {
                    // The server supports stream compression
                    connection.setAvailableCompressionMethods(PacketParserUtils
                            .parseCompressionMethods(parser));
                } else if (parser.getName().equals("register")) {
                    connection.getAccountManager().setSupportsAccountCreation(
                            true);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("starttls")) {
                    // Confirm the server that we want to use TLS
                    connection.startTLSReceived(startTLSRequired);
                } else if (parser.getName().equals("required")
                        && startTLSReceived) {
                    startTLSRequired = true;
                } else if (parser.getName().equals("features")) {
                    done = true;
                }
            }
        }
        // If TLS is required but the server doesn't offer it, disconnect
        // from the server and throw an error. First check if we've already
        // negotiated TLS
        // and are secure, however (features get parsed a second time after TLS
        // is established).
        if (!connection.isSecureConnection()) {
            if (!startTLSReceived
                    && connection.getConfiguration().getSecurityMode() == ConnectionConfiguration.SecurityMode
                    .required) {
                throw new XMPPException(
                        "Server does not support security (TLS), "
                                + "but security required by connection configuration.",
                        new XMPPError(XMPPError.Condition.forbidden)
                );
            }
        }
        // Release the lock after TLS has been negotiated or we are not
        // insterested in TLS
        if (!startTLSReceived
                || connection.getConfiguration().getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled) {
            releaseConnectionIDLock();
        }
    }

    /**
     * A runnable to notify all listeners of a packet.
     */
    private class ListenerNotification implements Runnable {
        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
//            Log.i(TAG, "[VIETTEL] ListenerNotification notify to " + connection.recvListeners.values().size() + "
// ListenerWrappers");
            for (ListenerWrapper listenerWrapper : connection.recvListeners.values()) {
                listenerWrapper.notifyListener(packet);
            }
        }
    }
}