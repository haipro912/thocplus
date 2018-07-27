package org.jivesoftware.smack.model;

/**
 * Created by toanvk2 on 10/6/2016.
 */

public class IceServer {
    private String user;
    private String credential;
    private String domain;

    public IceServer() {

    }

    public IceServer(String user, String credential, String domain) {
        this.user = user;
        this.credential = credential;
        this.domain = domain;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String toXml() {
        StringBuilder buf = new StringBuilder();
        buf.append("<server");
        buf.append(" user=\"").append(user).append("\"").append(" credential=\"").append(credential).append("\">");
        buf.append("<![CDATA[").append(domain).append("]]>");
        buf.append("</server>");
        return buf.toString();
    }
}
