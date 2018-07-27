/**
 * $RCSfile$
 * $Revision: 11613 $
 * $Date: 2010-02-09 12:55:56 +0100 (mar. 09 févr. 2010) $
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

import android.content.Context;

import com.viettel.util.Log;
import com.viettel.util.LogDebugHelper;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.util.Collection;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 * Creates a socket connection to a XMPP server. This is the default connection
 * to a Jabber server and is specified in the XMPP Core (RFC 3920).
 *
 * @author Matt Tucker
 * @see Connection
 */
public class XMPPConnection extends Connection {
    private static final String TAG = "XMPPConnection";
    /**
     * The socket which is used for this connection.
     */
    protected Socket socket;
    String connectionID = null;
    PacketWriter packetWriter;
    PacketReader packetReader;
    Roster roster = null;
    private String user = null;
    private boolean connected = false;
    private Context context;
    private boolean authenticated = false;

    public PacketReader getPacketReader() {
        return packetReader;
    }
    //    private final Object myLock = new Object();
//    private int connectionStatus = 0; /* 0: others, 1: logining.*/

    /**
     * Flag that indicates if the user was authenticated with the server when
     * the connection to the server was closed (abruptly or not).
     */
//    private boolean wasAuthenticated = false;
    private boolean anonymous = false;
    private boolean usingTLS = false;
    /**
     * Collection of available stream compression methods offered by the server.
     */
    private Collection<String> compressionMethods;
    /**
     * Flag that indicates if stream compression is actually in use.
     */
    private boolean usingCompression;

    public XMPPConnection(ConnectionConfiguration config, Context context) {
        super(config);
//        this.config.setSASLAuthenticationEnabled(true);
        this.context = context;
    }

    public String getConnectionID() {
        if (!isConnected()) {
            return null;
        }
        return connectionID;
    }

    public String getUser() {
        if (!isAuthenticated()) {
            return null;
        }
        return user;
    }

    public synchronized IQ customLogin(String username, String password,
                                       String resource, String mechanismMethod,
                                       String revision, String countryCode) throws
            XMPPException, IllegalStateException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        }
        if (authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        }
        // Do partial version of name-prep on the username.
        username = username.toLowerCase().trim();
        NonSASLAuthentication nonSASLAuthentication = new NonSASLAuthentication(this);
        nonSASLAuthentication.setMechanismMethod(mechanismMethod);
        IQ response = nonSASLAuthentication.authenticate(username, password, resource,
                mechanismMethod, revision, countryCode);
        // Authenticate using Non-SASL
        // Set the user.
        if (response != null) {
            this.user = username;
            // Update the serviceName with the one returned by the server
            // config.setServiceName(StringUtils.parseServer(response));
        } else {
            this.user = username + "@" + getServiceName();
            if (resource != null) {
                this.user += "/" + resource;
            }
        }
        // Indicate that we're now authenticated.
        authenticated = true;
        anonymous = false;
        Log.f(TAG, "XMPP authenticated");
        logDebugContent("XMPP authenticated");
        // If compression is enabled then request the server to use stream
        // compression
        if (config.isCompressionEnabled()) {
            useCompression();
        }
        // Set presence to online.
        if (config.isSendPresence()) {
            packetWriter.sendPacket(new Presence(Presence.Type.available));
        }
        // Stores the authentication for future reconnection
        config.setLoginInfo(username, password, resource, revision, countryCode);

        // If debugging is enabled, change the the debug window title to include
        // the name we are now logged-in as.
        // If DEBUG_ENABLED was set to true AFTER the connection was created the
        // debugger will be null
        if (config.isDebuggerEnabled() && debugger != null) {
            debugger.userHasLogged(user);
        }
        return response;
    }

    public Roster getRoster() {
        if (roster == null) {
            return null;
        }

        // DungNH8 comment load roster after login
        /*
         * if (!config.isRosterLoadedAtLogin()) { roster.reload(); }
         */

        // If this is the first time the user has asked for the roster after
        // calling
        // login, we want to wait for the server to send back the user's roster.
        // This
        // behavior shields API users from having to worry about the fact that
        // roster
        // operations are asynchronous, although they'll still have to listen
        // for
        // changes to the roster. Note: because of this waiting logic, internal
        // Smack code should be wary about calling the getRoster method, and may
        // need to
        // access the roster object directly.
        if (!roster.rosterInitialized) {
            try {
                synchronized (roster) {
                    long waitTime = SmackConfiguration.getPacketReplyTimeout();
                    long start = System.currentTimeMillis();
                    while (!roster.rosterInitialized) {
                        if (waitTime <= 0) {
                            break;
                        }
                        roster.wait(waitTime);
                        long now = System.currentTimeMillis();
                        waitTime -= now - start;
                        start = now;
                    }
                }
            } catch (InterruptedException ie) {
                // Ignore.
            }
        }
        return roster;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
        //        return connected;
    }

    public boolean isSecureConnection() {
        return isUsingTLS();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Closes the connection by setting presence to unavailable then closing the
     * stream to the XMPP server. The shutdown logic will be used during a
     * planned disconnection or when dealing with an unexpected disconnection.
     * Unlike {@link #disconnect()} the connection's packet reader, packet
     * writer, and {@link Roster} will not be removed; thus connection's state
     * is kept.
     *
     * @param unavailablePresence the presence packet to send during shutdown.
     */
    protected void shutdown(Presence unavailablePresence) {
        // Set presence to offline.
        //      synchronized (myLock) {
        //      connectionStatus = 1;
        Log.f(TAG, "[VIETTEL] XMPPConnection shutdown");
        logDebugContent("[VIETTEL] XMPPConnection shutdown");
        try {
            if (packetWriter != null) {
                packetWriter.sendPacket(unavailablePresence);
            }
            //      ko gui presence khi shutdown nua
            //      this.setWasAuthenticated(authenticated);
            authenticated = false;
            connected = false;
            if (packetReader != null) {
                packetReader.shutdown();
            }
            if (packetWriter != null) {
                packetWriter.shutdown();
            }
            // Wait 150 ms for processes to clean-up, then shutdown.
            //change from 150 to 50
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (writer != null) {
                try {
                    writer.close();
                    Log.f(TAG, "[VIETTEL] writer is closed 2");
                    logDebugContent("[VIETTEL] writer is closed 2");
                } catch (Throwable ignore) { /* ignore */
                }
                writer = null;
            }
            // Close down the readers and writers.
            try {
                Log.f(TAG, "[VIETTEL] close socket because connection is closed");
                logDebugContent("[VIETTEL] close socket because connection is " +
                        "closed");
                socket.close();
            } catch (Exception e) {
                // Ignore.
            }
            if (reader != null) {
                try {
                    reader.close();
                    Log.f(TAG, "[VIETTEL] reader is closed 2");
                    logDebugContent("[VIETTEL] reader is closed 2");
                } catch (Exception ignore) { /* ignore */
                    Log.e(TAG, "Exception", ignore);
                    Log.f(TAG, "[VIETTEL] has an exception when closing reader");
                    logDebugContent("[VIETTEL] has an exception when closing reader: " + ignore.toString());
                }
                reader = null;
            }
            //-----------------------------added by thaodv-------------------------------
            final Collection<ConnectionListener> connectionListeners = getConnectionListeners();
            if (connectionListeners != null) {
                for (ConnectionListener listener : connectionListeners) {
                    listener.connectionClosedCompleted();
                }
            }
            //-----------------------------------------------
            saslAuthentication.init();
        } catch (Exception e) {
            Log.f(TAG, "shutdown", e);
            logDebugContent("shutdown" + e);
        }
    }

    public void disconnect(Presence unavailablePresence) {
        // If not connected, ignore this request.
        Log.f(TAG, "disconnect()");
        logDebugContent("disconnect");
        shutdown(unavailablePresence);
        if (roster != null) {
            roster.cleanup();
            roster = null;
        }
        connected = false;
        if (packetWriter != null) {
            packetWriter.cleanup();
            packetWriter = null;
        }
        if (packetReader != null) {
            packetReader.cleanup();
            packetReader = null;
        }
    }

    public void sendPacket(Packet packet) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        }
        if (packet == null) {
            throw new NullPointerException("Packet is null.");
        }
        packetWriter.sendPacket(packet);
    }

    /**
     * Registers a packet interceptor with this connection. The interceptor will
     * be invoked every time a packet is about to be sent by this connection.
     * Interceptors may modify the packet to be sent. A packet filter determines
     * which packets will be delivered to the interceptor.
     *
     * @param packetInterceptor the packet interceptor to notify of packets about to be sent.
     * @param packetFilter      the packet filter to use.
     * @deprecated replaced by
     * {@link Connection#addPacketInterceptor(PacketInterceptor, PacketFilter)}
     * .
     */
    public void addPacketWriterInterceptor(PacketInterceptor packetInterceptor,
                                           PacketFilter packetFilter) {
        addPacketInterceptor(packetInterceptor, packetFilter);
    }

    /**
     * Removes a packet interceptor.
     *
     * @param packetInterceptor the packet interceptor to remove.
     * @deprecated replaced by
     * {@link Connection#removePacketInterceptor(PacketInterceptor)}
     * .
     */
    public void removePacketWriterInterceptor(
            PacketInterceptor packetInterceptor) {
        removePacketInterceptor(packetInterceptor);
    }

    /**
     * Registers a packet listener with this connection. The listener will be
     * notified of every packet that this connection sends. A packet filter
     * determines which packets will be delivered to the listener. Note that the
     * thread that writes packets will be used to invoke the listeners.
     * Therefore, each packet listener should complete all operations quickly or
     * use a different thread for processing.
     *
     * @param packetListener the packet listener to notify of sent packets.
     * @param packetFilter   the packet filter to use.
     * @deprecated replaced by
     * {@link #addPacketSendingListener(PacketListener, PacketFilter)}
     * .
     */
    public void addPacketWriterListener(PacketListener packetListener,
                                        PacketFilter packetFilter) {
        addPacketSendingListener(packetListener, packetFilter);
    }

    /**
     * Removes a packet listener for sending packets from this connection.
     *
     * @param packetListener the packet listener to remove.
     * @deprecated replaced by
     * {@link #removePacketSendingListener(PacketListener)}.
     */
    public void removePacketWriterListener(PacketListener packetListener) {
        removePacketSendingListener(packetListener);
    }

    /**
     * create socket connection
     *
     * @param config
     * @throws XMPPException
     */
    private void connectUsingConfiguration(ConnectionConfiguration config)
            throws XMPPException {
        String host = config.getHost();
        int port = config.getPort();
        try {
            if (config.getSocketFactory() == null) {
                this.socket = new Socket(host, port);
            } else {
                this.socket = config.getSocketFactory().createSocket(host, port);
            }
            socket.setKeepAlive(true);
        } catch (UnknownHostException uhe) {
            String errorMessage = "connectUsingConfiguration UnknownHostException Could not connect to " + host + ":"
                    + port
                    + ".";
            Log.f("SMACK", "UnknownHostException: " + errorMessage, uhe);
            logDebugContent("connectUsingConfiguration UnknownHostException: " +
                    errorMessage);
            throw new XMPPException(errorMessage, new XMPPError(
                    XMPPError.Condition.remote_server_timeout, errorMessage),
                    uhe
            );
        } catch (IOException ioe) {
            Log.e(TAG, "Exception", ioe);
            String errorMessage = "connectUsingConfiguration IOException XMPPError connecting to " + host + ":"
                    + port + ".";
            Log.f("SMACK", "IOException: " + errorMessage, ioe);
            logDebugContent("connectUsingConfiguration IOException: " +
                    errorMessage);
            throw new XMPPException(errorMessage, new XMPPError(
                    XMPPError.Condition.remote_server_error, errorMessage), ioe);
        }
        initConnection();
    }

    /**
     * Initializes the connection by creating a packet reader and writer and
     * opening a XMPP stream to the server.
     *
     * @throws XMPPException if establishing a connection to the server fails.
     */
    private void initConnection() throws XMPPException {
        long beginTime = System.currentTimeMillis();
        boolean isFirstInitialization = packetReader == null
                || packetWriter == null;
        if (!isFirstInitialization) {
            usingCompression = false;
        }
        // Set the reader and writer instance variables
        initReaderAndWriter();
        try {
            if (isFirstInitialization) {
                packetWriter = new PacketWriter(this);
                packetReader = new PacketReader(this);
                // If debugging is enabled, we should start the thread that will
                // listen for all packets and then log them.
                if (config.isDebuggerEnabled()) {
                    addPacketListener(debugger.getReaderListener(), null);
                    if (debugger.getWriterListener() != null) {
                        addPacketSendingListener(debugger.getWriterListener(),
                                null);
                    }
                }
            } else {
                packetWriter.init();
                packetReader.init();
            }

            // Start the packet writer. This will open a XMPP stream to the
            // server
            packetWriter.startup();
            // Start the packet reader. The startup() method will block until we
            // get an opening stream packet back from server.
            packetReader.startup();

            // Make note of the fact that we're now connected.
            connected = true;

            // Start keep alive process (after TLS was negotiated - if
            // available)
            if (isFirstInitialization) {
                // Notify listeners that a new connection has been established
                for (ConnectionCreationListener listener : getConnectionCreationListeners()) {
                    listener.connectionCreated(this);
                }
            } else {
                packetReader.notifyReconnection();
            }
            logDebugContent("initConnection success take: " + (System.currentTimeMillis() - beginTime));
        } catch (XMPPException ex) {
            // An exception occurred in setting up the connection. Make sure we
            // shut down the
            // readers and writers and close the socket.
            logDebugContent("initConnection fail XMPPException: " + ex.toString());
            if (packetWriter != null) {
                try {
                    packetWriter.shutdown();
                } catch (Throwable ignore) { /* ignore */
                }
                packetWriter = null;
            }
            if (packetReader != null) {
                try {
                    packetReader.shutdown();
                } catch (Throwable ignore) { /* ignore */
                }
                packetReader = null;
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable ignore) { /* ignore */
                }
                reader = null;
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable ignore) { /* ignore */
                }
                writer = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) { /* ignore */
                }
                socket = null;
            }
            authenticated = false;
            connected = false;
            throw ex; // Everything stoppped. Now throw the exception.
        }
    }

    private void initReaderAndWriter() throws XMPPException {
        Log.i(TAG, "initReaderAndWriter reader = " + reader);
        long time = System.currentTimeMillis();
        try {
            if (!usingCompression) {
                reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), "UTF-8"));
                writer = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), "UTF-8"));
            } else {
                try {
                    Class<?> zoClass = Class
                            .forName("com.jcraft.jzlib.ZOutputStream");
                    Constructor<?> constructor = zoClass.getConstructor(
                            OutputStream.class, Integer.TYPE);
                    Object out = constructor.newInstance(
                            socket.getOutputStream(), 9);
                    Method method = zoClass.getMethod("setFlushMode",
                            Integer.TYPE);
                    method.invoke(out, 2);
                    writer = new BufferedWriter(new OutputStreamWriter(
                            (OutputStream) out, "UTF-8"));

                    Class<?> ziClass = Class
                            .forName("com.jcraft.jzlib.ZInputStream");
                    constructor = ziClass.getConstructor(InputStream.class);
                    Object in = constructor
                            .newInstance(socket.getInputStream());
                    method = ziClass.getMethod("setFlushMode", Integer.TYPE);
                    method.invoke(in, 2);
                    reader = new BufferedReader(new InputStreamReader(
                            (InputStream) in, "UTF-8"));
                } catch (Exception e) {
                    Log.e(TAG, "Exception", e);
                    reader = new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), "UTF-8"));
                    writer = new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream(), "UTF-8"));
                }
            }
        } catch (IOException ioe) {
            logDebugContent("initReaderAndWriter IOException: " + ioe.toString());
            throw new XMPPException(
                    "IOException establishing connection with server.",
                    new XMPPError(XMPPError.Condition.remote_server_error,
                            "XMPPError establishing connection with server."),
                    ioe
            );
        }
        logDebugContent("initReaderAndWriter take: " + (System.currentTimeMillis() - time));
        // If debugging is enabled, we open a window and write out all network
        // traffic.
        initDebugger();
    }

    /***********************************************
     * TLS code below
     **********************************************/

    /**
     * Returns true if the connection to the server has successfully negotiated
     * TLS. Once TLS has been negotiatied the connection has been secured.
     *
     * @return true if the connection to the server has successfully negotiated
     * TLS.
     */
    public boolean isUsingTLS() {
        return usingTLS;
    }

    /**
     * Notification message saying that the server supports TLS so confirm the
     * server that we want to secure the connection.
     *
     * @param required true when the server indicates that TLS is required.
     */
    void startTLSReceived(boolean required) {
        if (required
                && config.getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled) {
            notifyConnectionError(new IllegalStateException("TLS required by server but not allowed by connection " +
                    "configuration"));
            return;
        }

        if (config.getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled) {
            // Do not secure the connection using TLS since TLS was disabled
            return;
        }
        try {
            writer.write("<starttls xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\"/>");
            writer.flush();
        } catch (IOException e) {
            notifyConnectionError(e);
        }
    }

    void notifyConnectionError(Exception e) {
        for (PacketCollector collector : getPacketCollectors()) {
            collector.selfNotify();
        }
        // Closes the connection temporary. A reconnection is possible
        shutdown(new Presence(Presence.Type.unavailable));
        // Print the stack trace to help catch the problem
        String msg = "";
        try {
            if (e != null) msg = e.getMessage();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.f(TAG, "notifyConnectionError: " + msg);
        logDebugContent("notifyConnectionError: " + msg);
        // Wait 100 ms for socket clean up and shutdown
        try {
            Thread.sleep(100);
        } catch (Exception ec) {
            // Ignore.
        }
        // Notify connection listeners of the error.
        for (ConnectionListener listener : getConnectionListeners()) {
            try {
                Log.i(TAG, "listener " + listener.getClass().getSimpleName());
                listener.connectionClosedOnError(e);
            } catch (Exception e2) {
                // Catch and print any exception so we can recover
                // from a faulty listener
                e2.printStackTrace();
            }
        }
    }

    /**
     * The server has indicated that TLS negotiation can start. We now need to
     * secure the existing plain connection and perform a handshake. This method
     * won't return until the connection has finished the handshake or an error
     * occured while securing the connection.
     *
     * @throws Exception if an exception occurs.
     */
    void proceedTLSReceived() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore ks;
        KeyManager[] kms = null;
        PasswordCallback pcb = null;

        if (config.getCallbackHandler() == null) {
            ks = null;
        } else {
            if (config.getKeystoreType().equals("NONE")) {
                ks = null;
                pcb = null;
            } else if (config.getKeystoreType().equals("PKCS11")) {
                try {
                    Constructor c = Class.forName(
                            "sun.security.pkcs11.SunPKCS11").getConstructor(
                            InputStream.class);
                    String pkcs11Config = "name = SmartCard\nlibrary = "
                            + config.getPKCS11Library();
                    ByteArrayInputStream config = new ByteArrayInputStream(
                            pkcs11Config.getBytes());
                    Provider p = (Provider) c.newInstance(config);
                    Security.addProvider(p);
                    ks = KeyStore.getInstance("PKCS11", p);
                    pcb = new PasswordCallback("PKCS11 Password: ", false);
                    this.config.getCallbackHandler().handle(
                            new Callback[]{pcb});
                    ks.load(null, pcb.getPassword());
                } catch (Exception e) {
                    ks = null;
                    pcb = null;
                }
            } else if (config.getKeystoreType().equals("Apple")) {
                ks = KeyStore.getInstance("KeychainStore", "Apple");
                ks.load(null, null);
                // pcb = new PasswordCallback("Apple Keychain",false);
                // pcb.setPassword(null);
            } else {
                ks = KeyStore.getInstance(config.getKeystoreType());
                try {
                    pcb = new PasswordCallback("Keystore Password: ", false);
                    config.getCallbackHandler().handle(new Callback[]{pcb});
                    ks.load(new FileInputStream(config.getKeystorePath()),
                            pcb.getPassword());
                } catch (Exception e) {
                    ks = null;
                    pcb = null;
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            try {
                if (pcb == null) {
                    kmf.init(ks, null);
                } else {
                    kmf.init(ks, pcb.getPassword());
                    pcb.clearPassword();
                }
                kms = kmf.getKeyManagers();
            } catch (NullPointerException npe) {
                kms = null;
            }
        }

        // Verify certificate presented by the server
        context.init(kms,
                new javax.net.ssl.TrustManager[]{new ServerTrustManager(
                        getServiceName(), config)},
                // new javax.net.ssl.TrustManager[]{new OpenTrustManager()},
                new java.security.SecureRandom()
        );
        Socket plain = socket;
        // Secure the plain connection
        socket = context.getSocketFactory().createSocket(plain,
                plain.getInetAddress().getHostName(), plain.getPort(), true);
        socket.setSoTimeout(0);
        socket.setKeepAlive(true);
        // Initialize the reader and writer with the new secured version
        initReaderAndWriter();
        // Proceed to do the handshake
        ((SSLSocket) socket).startHandshake();
        // if (((SSLSocket) socket).getWantClientAuth()) {
        // System.err.println("Connection wants client auth");
        // }
        // else if (((SSLSocket) socket).getNeedClientAuth()) {
        // System.err.println("Connection needs client auth");
        // }
        // else {
        // System.err.println("Connection does not require client auth");
        // }
        // Set that TLS was successful
        usingTLS = true;

        // Set the new writer to use
        packetWriter.setWriter(writer);
        // Send a new opening stream to the server
        packetWriter.openStream();
    }

    /**
     * Sets the available stream compression methods offered by the server.
     *
     * @param methods compression methods offered by the server.
     */
    void setAvailableCompressionMethods(Collection<String> methods) {
        compressionMethods = methods;
    }

    /**
     * Returns true if the specified compression method was offered by the
     * server.
     *
     * @param method the method to check.
     * @return true if the specified compression method was offered by the
     * server.
     */
    private boolean hasAvailableCompressionMethod(String method) {
        return compressionMethods != null
                && compressionMethods.contains(method);
    }

    public boolean isUsingCompression() {
        return usingCompression;
    }

    protected XMPPConnection(ConnectionConfiguration configuration) {
        super(configuration);
    }

    /**
     * Starts using stream compression that will compress network traffic.
     * Traffic can be reduced up to 90%. Therefore, stream compression is ideal
     * when using a slow speed network connection. However, the server and the
     * client will need to use more CPU time in order to un/compress network
     * data so under high load the server performance might be affected.
     * <p/>
     * <p/>
     * Stream compression has to have been previously offered by the server.
     * Currently only the zlib method is supported by the client. Stream
     * compression negotiation has to be done before authentication took place.
     * <p/>
     * <p/>
     * Note: to use stream compression the smackx.jar file has to be present in
     * the classpath.
     *
     * @return true if stream compression negotiation was successful.
     */
    private boolean useCompression() {
        // If stream compression was offered by the server and we want to use
        // compression then send compression request to the server
        if (authenticated) {
            throw new IllegalStateException(
                    "Compression should be negotiated before authentication.");
        }
        try {
            Class.forName("com.jcraft.jzlib.ZOutputStream");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Cannot use compression. Add smackx.jar to the classpath");
        }
        if (hasAvailableCompressionMethod("zlib")) {
            requestStreamCompression();
            // Wait until compression is being used or a timeout happened
            synchronized (this) {
                try {
                    this.wait(SmackConfiguration.getPacketReplyTimeout() * 5);
                } catch (InterruptedException e) {
                    // Ignore.
                }
            }
            return usingCompression;
        }
        return false;
    }

    /**
     * Request the server that we want to start using stream compression. When
     * using TLS then negotiation of stream compression can only happen after
     * TLS was negotiated. If TLS compression is being used the stream
     * compression should not be used.
     */
    private void requestStreamCompression() {
        try {
            writer.write("<compress xmlns='http://jabber.org/protocol/compress'>");
            writer.write("<method>zlib</method></compress>");
            writer.flush();
        } catch (IOException e) {
            notifyConnectionError(e);
        }
    }

    /**
     * Start using stream compression since the server has acknowledged stream
     * compression.
     *
     * @throws Exception if there is an exception starting stream compression.
     */
    void startStreamCompression() throws Exception {
        // Secure the plain connection
        usingCompression = true;
        // Initialize the reader and writer with the new secured version
        initReaderAndWriter();

        // Set the new writer to use
        packetWriter.setWriter(writer);
        // Send a new opening stream to the server
        packetWriter.openStream();
        // Notify that compression is being used
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Notifies the XMPP connection that stream compression was denied so that
     * the connection process can proceed.
     */
    void streamCompressionDenied() {
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Establishes a connection to the XMPP server and performs an automatic
     * login only if the previous connection state was logged (authenticated).
     * It basically creates and maintains a socket connection to the server.
     * <p/>
     * <p/>
     * Listeners will be preserved from a previous connection if the
     * reconnection occurs after an abrupt termination.
     *
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *                       Two possible errors can occur which will be wrapped by an
     *                       XMPPException -- UnknownHostException (XMPP error code 504),
     *                       and IOException (XMPP error code 502). The error codes and
     *                       wrapped exceptions can be used to present more appropiate
     *                       error messages to end-users.
     */
    public synchronized void connect() throws XMPPException, IllegalStateException {
        if (socket != null && !socket.isClosed() && !authenticated) {
            shutdown(new Presence(Presence.Type.unavailable));
            Log.i(TAG, "shutdown");
        } else if (socket != null && socket.isConnected() && authenticated) {
            Log.i(TAG, "call connect but return because authenticated = " + authenticated);
            return;
        }
        // Stablishes the connection, readers and writers
        Log.f(TAG, "begin connect xmpp");
        logDebugContentNetWorkType("begin connect xmpp");
        long time = System.currentTimeMillis();
        connectUsingConfiguration(config);
        // Automatically makes the login if the user was previouslly connected
        // successfully
        Log.f(TAG, "open socket success");
        logDebugContent("open socket success take: " + (System.currentTimeMillis() - time));
        // to the server and the connection was terminated abruptly
        if (isConnected() && !authenticated) { //change
            // Make the login
            if (config.getUsername() != null && config.getUsername().length() > 0) {
                customLogin(config.getUsername(), config.getToken(),
                        config.getResource(), Connection.TOKEN_AUTH_NON_SASL,
                        config.getRevision(), config.getCountryCode());
            }
        } else {
            Log.f(TAG, "not login because connected = " + isConnected() + " authenticated = " + authenticated);
            logDebugContent("not login because connected = " + isConnected() +
                    " authenticated = " + authenticated);
        }
        Log.d(TAG, "login success take: " + (System.currentTimeMillis() - time));
    }

    public Socket getSocket() {
        return socket;
    }

    public void logDebugContent(String content) {
        LogDebugHelper.getInstance(context).logDebugContent(content);
    }

    public void logDebugContentNetWorkType(String content) {
        LogDebugHelper.getInstance(context).logDebugContentNetworkType(content);
    }
}
