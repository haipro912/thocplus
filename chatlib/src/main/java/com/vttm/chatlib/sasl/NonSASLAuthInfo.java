package com.vttm.chatlib.sasl;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by thaodv on 5/10/2015.
 */
public class NonSASLAuthInfo extends IQ {
    public static final String ELEMENT = "nonAuth";
    public static final String NAMESPACE = "http://jabber.org/protocol/nonAuth";

    private String token;
    private String domainFile;
    private String domainMessage;
    private String domainOnMedia;
    private String publicRSAKey;
    private String domainServiceKeeng;
    private String domainMedia2Keeng;
    private String domainImageKeeng;
    private int vipInfo = -1;
    private int cbnv = -1;
    private int call = -1;
    private int SSL = -1;
    private int smsIn = -1;

    public NonSASLAuthInfo()
    {
        super(ELEMENT, NAMESPACE);
    }

    public NonSASLAuthInfo(String childElementName, String childElementNamespace) {
        super(childElementName, childElementNamespace);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDomainFile() {
        return domainFile;
    }

    public void setDomainFile(String domainFile) {
        this.domainFile = domainFile;
    }

    public String getDomainMessage() {
        return domainMessage;
    }

    public void setDomainMessage(String domainMessage) {
        this.domainMessage = domainMessage;
    }

    public String getDomainOnMedia() {
        return domainOnMedia;
    }

    public void setDomainOnMedia(String domainOnMedia) {
        this.domainOnMedia = domainOnMedia;
    }

    public String getPublicRSAKey() {
        return publicRSAKey;
    }

    public void setPublicRSAKey(String publicRSAKey) {
        this.publicRSAKey = publicRSAKey;
    }

    public String getDomainServiceKeeng() {
        return domainServiceKeeng;
    }

    public void setDomainServiceKeeng(String domainServiceKeeng) {
        this.domainServiceKeeng = domainServiceKeeng;
    }

    public String getDomainMedia2Keeng() {
        return domainMedia2Keeng;
    }

    public void setDomainMedia2Keeng(String domainMedia2Keeng) {
        this.domainMedia2Keeng = domainMedia2Keeng;
    }

    public String getDomainImageKeeng() {
        return domainImageKeeng;
    }

    public void setDomainImageKeeng(String domainImageKeeng) {
        this.domainImageKeeng = domainImageKeeng;
    }

    public int getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(int vipInfo) {
        this.vipInfo = vipInfo;
    }

    public int getCBNV() {
        return cbnv;
    }

    public void setCBNV(int cbnv) {
        this.cbnv = cbnv;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getSSL() {
        return SSL;
    }

    public void setSSL(int ssl) {
        this.SSL = ssl;
    }

    public int getSmsIn() {
        return smsIn;
    }

    public void setSmsIn(int smsIn) {
        this.smsIn = smsIn;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder buf) {
        buf.append("<auth_info>");
        if (token != null) {
            buf.append("<token>").append(StringUtils.escapeForXml(token)).append("</token>");
        }
        if (domainFile != null) {
            buf.append("<domain_file>").append(StringUtils.escapeForXml(domainFile)).append("</domain_file>");
        }
        if (domainMessage != null) {
            buf.append("<domain_msg>").append(StringUtils.escapeForXml(domainMessage)).append("</domain_msg>");
        }
        if (domainOnMedia != null) {
            buf.append("<domain_on_media>").append(StringUtils.escapeForXml(domainOnMedia)).append("</domain_on_media>");
        }
        if (domainServiceKeeng != null) {
            buf.append("<kservice>").append(StringUtils.escapeForXml(domainServiceKeeng)).append("</kservice>");
        }
        if (domainMedia2Keeng != null) {
            buf.append("<kmedia>").append(StringUtils.escapeForXml(domainMedia2Keeng)).append("</kmedia>");
        }
        if (domainImageKeeng != null) {
            buf.append("<kimage>").append(StringUtils.escapeForXml(domainImageKeeng)).append("</kimage>");
        }
        buf.append("<vip>").append(vipInfo + "").append("</vip>");
        buf.append("</auth_info>");
        return buf;
    }
}
