/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
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

import android.text.TextUtils;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;

/**
 * Implementation of JEP-0078: Non-SASL Authentication. Follow the following <a
 * href=http://www.jabber.org/jeps/jep-0078.html>link</a> to obtain more
 * information about the JEP.
 *
 * @author Gaston Dombiak
 */
class NonSASLAuthentication implements UserAuthentication {

    private static final String TAG = NonSASLAuthentication.class.getSimpleName();
    private Connection connection;
    private String mechanismMethod;

    public NonSASLAuthentication(Connection connection) {
        super();
        this.connection = connection;
    }

    public void setMechanismMethod(String mechanismMethod) {
        this.mechanismMethod = mechanismMethod;
    }


    /**
     * dung ham nay cho custom login mocha
     *
     * @param username
     * @param password
     * @param resource
     * @param mechanismMethod
     * @return
     * @throws XMPPException
     */
    public IQ authenticate(String username, String password, String resource, String mechanismMethod,
                           String revision, String countryCode) throws XMPPException {
        // If we send an authentication packet in "get" mode with just the
        // username,
        // the server will return the list of authentication protocols it
        // supports.
        PacketCollector collector;
        IQ response;
        // Now, create the authentication packet we'll send to the server.
        Authentication authentication = new Authentication();
        authentication.setUsername(username);

        if (!TextUtils.isEmpty(mechanismMethod)) {
            authentication.setMechanismMethod(mechanismMethod);
        }
        // Figure out if we should use digest or plain text authentication.
        authentication.setDigest(connection.getConnectionID(), password);
        authentication.setResource(resource);
        authentication.setRevision(revision);
        authentication.setCountryCode(countryCode);
//        collector = connection.createPacketCollector(new PacketIDFilter("test"));
        collector = connection.createPacketCollector(new PacketIDFilter(authentication.getPacketID()));
        // Send the packet.
        //<iq id="KSa2X-1" authentype="code" type="set"><query
        // xmlns="jabber:iq:auth"><username>0986420088</username><digest>ae97eb75921009e7bfbca0c9ec264e44345e5f05
        // </digest><resource>reeng</resource></query></iq>
        connection.sendPacket(authentication);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        //có thể làm cách khác là gọi hàm processPacket(success) tương tự SASL
        //thử cách này xem có ổn định hơn ko - lúc timeout
        if (response == null) {
            XMPPError error = new XMPPError((XMPPError.Condition.request_timeout), "timeout");
            throw new XMPPException(error);
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();
        return response;
    }

    public String authenticate(String username, String password, String resource,
                               String revision, String countryCode) throws XMPPException {
        // If we send an authentication packet in "get" mode with just the
        // username,
        // the server will return the list of authentication protocols it
        // supports.
        Authentication discoveryAuth = new Authentication();
        discoveryAuth.setType(IQ.Type.GET);
        discoveryAuth.setUsername(username);

        PacketCollector collector = connection
                .createPacketCollector(new PacketIDFilter(discoveryAuth
                        .getPacketID()));
        // Send the packet
        connection.sendPacket(discoveryAuth);
        // Wait up to a certain number of seconds for a response from the
        // server.
        IQ response = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("No response from the server.");
        }
        // If the server replied with an error, throw an exception.
        else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // Otherwise, no error so continue processing.
        Authentication authTypes = (Authentication) response;
        collector.cancel();

        // Now, create the authentication packet we'll send to the server.
        Authentication authentication = new Authentication();
        authentication.setUsername(username);

        if (!TextUtils.isEmpty(mechanismMethod)) {
            authentication.setMechanismMethod(mechanismMethod);
        }
        // Figure out if we should use digest or plain text authentication.
        if (authTypes.getDigest() != null) {
            authentication.setDigest(connection.getConnectionID(), password);
        } else if (authTypes.getPassword() != null) {
            authentication.setPassword(password);
        } else {
            throw new XMPPException(
                    "Server does not support compatible authentication mechanism.");
        }
        authentication.setResource(resource);
        authentication.setRevision(revision);
        collector = connection.createPacketCollector(new PacketIDFilter(authentication
                .getPacketID()));
        // Send the packet.
        //<iq id="KSa2X-1" authentype="code" type="set"><query
        // xmlns="jabber:iq:auth"><username>0986420088</username><digest>ae97eb75921009e7bfbca0c9ec264e44345e5f05
        // </digest><resource>reeng</resource></query></iq>
        connection.sendPacket(authentication);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Authentication failed.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();
        return response.getTo();
    }

    public Bind authenticateReturnPacket(String username, String password,
                                         String resource, String mechanismMethod, String revision)
            throws XMPPException {
        // If we send an authentication packet in "get" mode with just the
        // username,
        // the server will return the list of authentication protocols it
        // supports.
        Bind discoveryAuth = new Bind();
        discoveryAuth.setType(IQ.Type.GET);
        discoveryAuth.setResource(resource);

        PacketCollector collector = connection
                .createPacketCollector(new PacketIDFilter(discoveryAuth
                        .getPacketID()));
        // Send the packet
        connection.sendPacket(discoveryAuth);
        // Wait up to a certain number of seconds for a response from the
        // server.
        IQ response = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("No response from the server.");
        }
        // If the server replied with an error, throw an exception.
        else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // Otherwise, no error so continue processing.
        Authentication authTypes = (Authentication) response;
        collector.cancel();

        // Now, create the authentication packet we'll send to the server.
        Authentication auth = new Authentication();
        auth.setUsername(username);

        // Figure out if we should use digest or plain text authentication.
        if (authTypes.getDigest() != null) {
            auth.setDigest(connection.getConnectionID(), password);
        } else if (authTypes.getPassword() != null) {
            auth.setPassword(password);
        } else {
            throw new XMPPException(
                    "Server does not support compatible authentication mechanism.");
        }

        auth.setResource(resource);

        collector = connection.createPacketCollector(new PacketIDFilter(auth
                .getPacketID()));
        // Send the packet.
        connection.sendPacket(auth);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Authentication failed.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();
        return (Bind) response;
    }

    public String authenticateAnonymously() throws XMPPException {
        // Create the authentication packet we'll send to the server.
        Authentication auth = new Authentication();

        PacketCollector collector = connection
                .createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        // Send the packet.
        connection.sendPacket(auth);
        // Wait up to a certain number of seconds for a response from the
        // server.
        IQ response = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Anonymous login failed.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();

        if (response.getTo() != null) {
            return response.getTo();
        } else {
            return connection.getServiceName() + "/"
                    + ((Authentication) response).getResource();
        }
    }
}
