package com.vttm.chatlib.packet;

import android.text.TextUtils;

import com.vttm.chatlib.utils.Log;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

public class Authentication extends IQ {
    private static final String TAG = Authentication.class.getSimpleName();

    public static final String ELEMENT = "auth";
    public static final String NAMESPACE = "http://jabber.org/protocol/auth";

    private String username = null;
    private String password = null;
    private String digest = null;
    private String resource = null;
    private String status = null;
    private String myAvatarNewChangeTime = null;
    private String revision;
    private String countryCode;
    /**
     * co che xac thuc
     */
//    private String mechanismMethod;

    /**
     * Create a new authentication packet. By default, the packet will be in
     * "set" mode in order to perform an actual authentication with the server.
     * In order to send a "get" request to get the available authentication
     * modes back from the server, change the type of the IQ packet to "get":
     * <p>
     * <p>
     * <tt>setType(IQ.Type.GET);</tt>
     */
    public Authentication() {
        super(ELEMENT, NAMESPACE);
        setType(IQ.Type.set);
    }

    /**
     * Returns the username, or <tt>null</tt> if the username hasn't been sent.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the plain text password or <tt>null</tt> if the password hasn't
     * been set.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the plain text password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password digest or <tt>null</tt> if the digest hasn't been
     * set. Password digests offer a more secure alternative for authentication
     * compared to plain text. The digest is the hex-encoded SHA-1 hash of the
     * connection ID plus the user's password. If the digest and password are
     * set, digest authentication will be used. If only one value is set, the
     * respective authentication mode will be used.
     *
     * @return the digest of the user's password.
     */
    public String getDigest() {
        return digest;
    }

    /**
     * Sets the digest value using a connection ID and password. Password
     * digests offer a more secure alternative for authentication compared to
     * plain text. The digest is the hex-encoded SHA-1 hash of the connection ID
     * plus the user's password. If the digest and password are set, digest
     * authentication will be used. If only one value is set, the respective
     * authentication mode will be used.
     *
     * @param connectionID the connection ID.
     * @param password     the password.
     * @see org.jivesoftware.smack.Connection#getConnectionID()
     */
    public void setDigest(String connectionID, String password) {
        this.digest = StringUtils.hash(connectionID + password);
    }

    /**
     * Sets the digest value directly. Password digests offer a more secure
     * alternative for authentication compared to plain text. The digest is the
     * hex-encoded SHA-1 hash of the connection ID plus the user's password. If
     * the digest and password are set, digest authentication will be used. If
     * only one value is set, the respective authentication mode will be used.
     *
     * @param digest the digest, which is the SHA-1 hash of the connection ID the
     *               user's password, encoded as hex.
     * @see org.jivesoftware.smack.Connection#getConnectionID()
     */
    public void setDigest(String digest) {
        Log.i(TAG, "setDigest = " + digest);
        this.digest = digest;
    }

    /**
     * Returns the resource or <tt>null</tt> if the resource hasn't been set.
     *
     * @return the resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource.
     *
     * @param resource the resource.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMyAvatarNewChangeTime() {
        return myAvatarNewChangeTime;
    }

    public void setMyAvatarNewChangeTime(String myAvatarNewChangeTime) {
        this.myAvatarNewChangeTime = myAvatarNewChangeTime;
    }

    //    public String getMechanismMethod() {
//        return mechanismMethod;
//    }
//
//    public void setMechanismMethod(String mechanismMethod) {
//        this.mechanismMethod = mechanismMethod;
//    }
    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:auth\">");
        String mechanismMethod = getMechanismMethod();
        if (!TextUtils.isEmpty(mechanismMethod)) {
            buf.append("<mechanism>").append(mechanismMethod).append("</mechanism>");
        }
        if (username != null) {
            if (username.equals("")) {
                buf.append("<username/>");
            } else {
                buf.append("<username>").append(username).append("</username>");
            }
        }
        if (digest != null) {
            if (digest.equals("")) {
                buf.append("<digest/>");
            } else {
                buf.append("<digest>").append(digest).append("</digest>");
            }
        }
        if (password != null && digest == null) {
            if (password.equals("")) {
                buf.append("<password/>");
            } else {
                buf.append("<password>")
                        .append(StringUtils.escapeForXml(password))
                        .append("</password>");
            }
        }
        if (resource != null) {
            if (resource.equals("")) {
                buf.append("<resource/>");
            } else {
                buf.append("<resource>").append(resource).append("</resource>");
            }
        }
        buf.append("<client_type>").append("Android").append("</client_type>");
        if (!TextUtils.isEmpty(revision)) {
            buf.append("<revision>").append(revision).append("</revision>");
        }
        if (!TextUtils.isEmpty(countryCode)) {
            buf.append("<country>").append(countryCode).append("</country>");
        }
        buf.append("</query>");
        return buf.toString();
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
//        xml.rightAngleBracket();
        xml.append(toXML());
        return xml;
    }
}
