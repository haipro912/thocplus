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

import com.viettel.util.Log;

import org.jivesoftware.smack.packet.Packet;

import java.io.IOException;
import java.io.Writer;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Writes packets to a XMPP server. Packets are sent using a dedicated thread.
 * Packet interceptors can be registered to dynamically modify packets before
 * they're actually sent. Packet listeners can be registered to listen for all
 * outgoing packets.
 *
 * @author Matt Tucker
 * @see Connection#addPacketInterceptor
 * @see Connection#addPacketSendingListener
 */
class PacketWriter {
    private static final String TAG = PacketWriter.class.getSimpleName();
    private final BlockingQueue<Packet> queue;
    private Thread writerThread;
    private Writer writer;
    private XMPPConnection connection;
    private boolean done;
    private Timer mTimer;
    /**
     * Timestamp when the last stanza was sent to the server. This information
     * is used by the keep alive process to only send heartbeats wh
     * connection has been idle.
     */

    /**
     * Creates a new packet writer with the specified connection.
     *
     * @param connection the connection.
     */
    protected PacketWriter(XMPPConnection connection) {
        this.queue = new ArrayBlockingQueue<Packet>(500, true);
        this.connection = connection;
        init();
    }

    /**
     * Initializes the writer in order to be used. It is called at the first
     * connection and also is invoked if the connection is disconnected by an
     * error.
     */
    protected void init() {
        this.writer = connection.writer;
        done = false;
        writerThread = new Thread() {
            public void run() {
                writePackets(this);
            }
        };
        writerThread.setName("Smack Packet Writer ("
                + connection.connectionCounterValue + ")");
        writerThread.setDaemon(true);
    }

    /**
     * Sends the specified packet to the server.
     *
     * @param packet the packet to send.
     */
    public void sendPacket(Packet packet) {
        try {
            if (!done) {
                try {
                    // Invoke interceptors for the new packet that is about to be sent.
                    // Interceptors
                    // may modify the content of the packet.
                    connection.firePacketInterceptors(packet);
                    queue.put(packet);
                } catch (Exception ie) {
                    Log.e(TAG, "Exception", ie);
                    return;
                }
                synchronized (queue) {
                    queue.notifyAll();
                }
                // Process packet writer listeners. Note that we're using the
                // sending
                // thread so it's expected that listeners are fast.
                connection.firePacketSendingListeners(packet);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            if (!done && connection != null) {
                done = true;
                Log.f(TAG, "notifyConnectionError", e);
                connection.logDebugContentNetWorkType("sendPacket notifyConnectionError: " + e.toString());
                connection.notifyConnectionError(e);
            }
        }
    }

    /**
     * Starts the packet writer thread and opens a connection to the server. The
     * packet writer will continue writing packets until {@link #shutdown} or an
     * error occurs.
     */
    public void startup() {
        if (writerThread != null && writerThread.isAlive()) {
            Log.i(TAG, "Return by writeThread existed");
            return; //added by thaodv
        }
        writerThread.start();
    }


    void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Shuts down the packet writer. Once this method has been called, no
     * further packets will be written to the server.
     */
    public void shutdown() {
        done = true;
        synchronized (queue) {
            queue.notifyAll();
        }
        //------------added by thaodv---------------------
        // cancel keep alive task
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        // cancel ping task
    }

    /**
     * Cleans up all resources used by the packet writer.
     */
    void cleanup() {
        connection.interceptors.clear();
        connection.sendListeners.clear();
    }

    /**
     * Returns the next available packet from the queue for writing.
     *
     * @return the next packet for writing.
     */
    private Packet nextPacket() {
        Packet packet = null;
        // Wait until there's a packet or we're done.
        while (!done && (packet = queue.poll()) == null) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            } catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return packet;
    }

    private void writePackets(Thread thisThread) {
        try {
            // Open the stream.
            openStream();
            // Write out packets from the queue.
            while (!done && (writerThread != null && writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    synchronized (writer) {
                        String value = packet.toXML();
                        writer.write(value);
                        writer.flush();
                    }
                }
            }
            Log.i(TAG, "[VIETTEL] Write thread is died");
            // Flush out the rest of the queue. If the queue is extremely large,
            // it's possible
            // we won't have time to entirely flush it before the socket is
            // forced closed
            // by the shutdown process.
            try {
                synchronized (writer) {
                    while (!queue.isEmpty()) {
                        Packet packet = queue.remove();
                        writer.write(packet.toXML());
                    }
                    writer.flush();
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
            // Delete the queue contents (hopefully nothing is left).
            queue.clear();
            // Close the stream.
            try {
                writer.write("</stream:stream>");
                writer.flush();
            } catch (Exception e) {
                // Do nothing
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                    // Do nothing
                }
            }
        } catch (Exception ioe) {
            //exception nay xay ra khi socket khong con mo nua ma client ko biet
            Log.e(TAG, "Exception", ioe);
            if (!done && connection != null) {
                done = true;
                Log.f(TAG, "notifyConnectionError", ioe);
                connection.logDebugContentNetWorkType("writePackets notifyConnectionError: " + ioe.toString());
                connection.notifyConnectionError(ioe);
            }
        }
    }

    /**
     * Sends to the server a new stream element. This operation may be requested
     * several times so we need to encapsulate the logic in one place. This
     * message will be sent while doing TLS, SASL and resource binding.
     *
     * @throws IOException If an error occurs while sending the stanza to the server.
     */
    void openStream() throws IOException {
        StringBuilder stream = new StringBuilder();
        stream.append("<stream:stream");
        stream.append(" to=\"").append(connection.getServiceName()).append("\"");
        stream.append(" xmlns=\"jabber:client\"");
        stream.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
        //        stream.append(" version=\"1.0\">");
        stream.append(" version=\"1.1\">");
        writer.write(stream.toString());
        writer.flush();
    }
}