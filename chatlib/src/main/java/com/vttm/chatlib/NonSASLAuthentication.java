package com.vttm.chatlib;

import android.text.TextUtils;

import com.vttm.chatlib.packet.Authentication;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaCollector;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements;

import java.io.IOException;

public class NonSASLAuthentication {
    private static final String TAG = NonSASLAuthentication.class.getSimpleName();
    private final AbstractXMPPConnection connection;
    private final ConnectionConfiguration configuration;
    private String mechanismMethod;
    private boolean authenticationSuccessful;
    private Exception saslException;

    public NonSASLAuthentication(AbstractXMPPConnection connection, ConnectionConfiguration configuration) {
        super();
        this.configuration = configuration;
        this.connection = connection;
        this.init();
    }

    public void setMechanismMethod(String mechanismMethod) {
        this.mechanismMethod = mechanismMethod;
    }

    void init() {
        authenticationSuccessful = false;
        saslException = null;
    }

    public boolean authenticationSuccessful() {
        return authenticationSuccessful;
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
                           String revision, String countryCode) throws XMPPException, SASLErrorException, IOException,
            SmackException, InterruptedException {
        // If we send an authentication packet in "get" mode with just the
        // username,
        // the server will return the list of authentication protocols it
        // supports.
        StanzaCollector collector;
        IQ response = null;
        // Now, create the authentication packet we'll send to the server.
        Authentication authentication = new Authentication();
        authentication.setUsername(username);

        if (!TextUtils.isEmpty(mechanismMethod)) {
            authentication.setMechanismMethod(mechanismMethod);
        }
        // Figure out if we should use digest or plain text authentication.
        authentication.setDigest(connection.getStreamId(), password);
        authentication.setResource(resource);
        authentication.setRevision(revision);
        authentication.setCountryCode(countryCode);
        collector = connection.createStanzaCollector(new StanzaIdFilter(authentication.getStanzaId()));
        // Send the packet.
        //<iq id="KSa2X-1" authentype="code" type="set"><query
        // xmlns="jabber:iq:auth"><username>0986420088</username><digest>ae97eb75921009e7bfbca0c9ec264e44345e5f05
        // </digest><resource>reeng</resource></query></iq>

        connection.sendStanza(authentication);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration.getDefaultReplyTimeout());

        //có thể làm cách khác là gọi hàm processPacket(success) tương tự SASL
        //thử cách này xem có ổn định hơn ko - lúc timeout
//        if (response == null) {
//            XMPPError error = new XMPPError((XMPPError.Condition.request_timeout), "timeout");
//            throw new XMPPException(error);
//        } else if (response.getType() == IQ.Type.error) {
//            throw new XMPPException(response.getError());
//        }


        if (saslException != null) {
            if (saslException instanceof SmackException) {
                throw (SmackException) saslException;
            } else if (saslException instanceof SASLErrorException) {
                throw (SASLErrorException) saslException;
            } else {
                throw new IllegalStateException("Unexpected exception type" , saslException);
            }
        }

        if (!authenticationSuccessful) {
            throw SmackException.NoResponseException.newWith(connection, "successful SASL authentication");
        }

        // We're done with the collector, so explicitly cancel it.
        collector.cancel();
        return response;
    }

    public String authenticate(String username, String password, String resource,
                               String revision, String countryCode) throws XMPPException, SASLErrorException, IOException, SmackException, InterruptedException {
        // If we send an authentication packet in "get" mode with just the
        // username,
        // the server will return the list of authentication protocols it
        // supports.
        Authentication discoveryAuth = new Authentication();
        discoveryAuth.setType(IQ.Type.get);
        discoveryAuth.setUsername(username);

        StanzaCollector collector = connection.createStanzaCollector(new StanzaIdFilter(discoveryAuth.getStanzaId()));
        IQ response = null;
        // Send the packet

        connection.sendStanza(discoveryAuth);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration.getDefaultReplyTimeout());


        if (response == null) {
            throw new MochaXMPPException("No response from the server.");
        }
        // If the server replied with an error, throw an exception.
        else if (response.getType() == IQ.Type.error) {
            throw new MochaXMPPException(response.getError().toString());
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
            authentication.setDigest(connection.getStreamId(), password);
        } else if (authTypes.getPassword() != null) {
            authentication.setPassword(password);
        } else {
            throw new MochaXMPPException("Server does not support compatible authentication mechanism.");
        }
        authentication.setResource(resource);
        authentication.setRevision(revision);
        collector = connection.createStanzaCollector(new StanzaIdFilter(authentication.getStanzaId()));

        // Send the packet.
        //<iq id="KSa2X-1" authentype="code" type="set"><query
        // xmlns="jabber:iq:auth"><username>0986420088</username><digest>ae97eb75921009e7bfbca0c9ec264e44345e5f05
        // </digest><resource>reeng</resource></query></iq>
        connection.sendStanza(authentication);
        // Wait up to a certain number of seconds for a response from the
        // server.
        response = (IQ) collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());

        if (response == null) {
            throw new MochaXMPPException("Authentication failed.");
        } else if (response.getType() == IQ.Type.error) {
            throw new MochaXMPPException(response.getError().toString());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();
        return response.getTo().toString();
    }

    public String authenticateAnonymously() throws XMPPException {
        // Create the authentication packet we'll send to the server.
        Authentication auth = new Authentication();

        StanzaCollector collector = connection.createStanzaCollector(new StanzaIdFilter(auth.getStanzaId()));
        IQ response = null;
        // Send the packet.
        try {
            connection.sendStanza(auth);
            // Wait up to a certain number of seconds for a response from the
            // server.
            response = (IQ) collector.nextResult(SmackConfiguration.getDefaultReplyTimeout());
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response == null) {
            throw new MochaXMPPException("Anonymous login failed.");
        } else if (response.getType() == IQ.Type.error) {
            throw new MochaXMPPException(response.getError().toString());
        }

        // We're done with the collector, so explicitly cancel it.
        collector.cancel();

        if (response.getTo() != null) {
            return response.getTo().toString();
        } else {
            return configuration.getXMPPServiceDomain() + "/" + ((Authentication) response).getResource();
        }
    }

    public void authenticated(SaslStreamElements.Success success) throws SmackException, InterruptedException {
        authenticationSuccessful = true;
        // Wake up the thread that is waiting in the #authenticate method
        synchronized (this) {
            notify();
        }
    }

    /**
     * Notification message saying that SASL authentication has failed. The server may have
     * closed the connection depending on the number of possible retries.
     *
     * @param saslFailure the SASL failure as reported by the server
     * @see <a href="https://tools.ietf.org/html/rfc6120#section-6.5">RFC6120 6.5</a>
     */
    public void authenticationFailed(SaslStreamElements.SASLFailure saslFailure) {
        authenticationFailed(new SASLErrorException(mechanismMethod, saslFailure));
    }

    public void authenticationFailed(Exception exception) {
        saslException = exception;
        // Wake up the thread that is waiting in the #authenticate method
        synchronized (this) {
            notify();
        }
    }
}
