package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import org.jivesoftware.smack.model.IceServer;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 11/14/2016.
 */

public class IQCall extends IQ {
    public static final String nameSpaceFree = "mocha:iq:initcall";
    public static final String nameSpaceCallOut = "mocha:iq:initcallout";
    private String nameSpace;
    private String callSession = "";
    private String caller;
    private String callee;
    private String errorCode;
    private ArrayList<IceServer> iceServers;
    private String codecPrefs;
    private String virtual;
    private boolean isVideo = false;

    public IQCall(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getCallSession() {
        return callSession;
    }

    public void setCallSession(String callSession) {
        this.callSession = callSession;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ArrayList<IceServer> getIceServers() {
        return iceServers;
    }

    public void addIceServer(String user, String credential, String domain) {
        if (iceServers == null) {
            iceServers = new ArrayList<>();
        }
        IceServer iceServer = new IceServer(user, credential, domain);
        iceServers.add(iceServer);
    }

    public String getCodecPrefs() {
        return codecPrefs;
    }

    public void setCodecPrefs(String codecPrefs) {
        this.codecPrefs = codecPrefs;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    @Override
    public String getChildElementXML() {
        return null;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<iq");
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo()))
                    .append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom()))
                    .append("\"");
        }
        if (getType() != null) {
            buf.append(" type=\"").append(getType()).append("\"");
        }
        buf.append(">");
        // add event namespace
        buf.append("<query xmlns=\"").append(nameSpace).append("\">");
        buf.append("<session>").append(callSession).append("</session>");
        if(!TextUtils.isEmpty(virtual)){
            buf.append("<virtual>").append(virtual).append("</virtual>");
        }
        if (!TextUtils.isEmpty(caller)) {
            buf.append("<caller>").append(caller).append("</caller>");
        }
        if (!TextUtils.isEmpty(callee)) {
            buf.append("<callee>").append(callee).append("</callee>");
        }
        if (isVideo) {
            buf.append("<video_call/>");
        }
        if (iceServers != null && !iceServers.isEmpty()) {
            buf.append("<iceservers>");
            for (IceServer iceServer : iceServers) {
                buf.append(iceServer.toXml());
            }
            buf.append("</iceservers>");
        }
        if (!TextUtils.isEmpty(errorCode)) {
            buf.append("<error>").append(errorCode).append("</error>");
        }
        if (!TextUtils.isEmpty(codecPrefs)) {
            buf.append("<codecPrefs>").append(codecPrefs).append("</codecPrefs>");
        }
        // end add query
        buf.append("</query>");
        buf.append("</iq>");
        return buf.toString();
    }

    public static boolean containsIQCall(String nameSpace) {
        return nameSpaceFree.equals(nameSpace) || nameSpaceCallOut.equals(nameSpace);
    }
}
