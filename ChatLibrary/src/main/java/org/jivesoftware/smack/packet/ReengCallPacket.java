package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import org.jivesoftware.smack.model.CallData;
import org.jivesoftware.smack.model.IceServer;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by thanhnt72 on 7/25/2016.
 */
public class ReengCallPacket extends ReengMessagePacket {
    private String caller;
    private String callee;
    private CallError callError;
    private CallData callData;
    private ArrayList<IceServer> iceServers;
    private String callSession;
    private String attrStatus;
    private boolean isCallConfide = false;
    private String strangerAvatar;      // last change avatar cua ban
    private String strangerPosterName;          // ten cua minh dung khi tao loi moi cung nghe voi nguoi la
    private boolean isVideoCall = false;
    private boolean isOnlyAudio = false;


    public ReengCallPacket() {
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

    public CallError getCallError() {
        return callError;
    }

    public void setCallError(CallError callError) {
        this.callError = callError;
    }

    public CallData getCallData() {
        return callData;
    }

    public void setCallData(CallData callData) {
        this.callData = callData;
    }

    public void setCallData(String type, String value) {
        this.callData = new CallData(type, value);
    }

    public ArrayList<IceServer> getIceServers() {
        return iceServers;
    }

    public void setIceServers(ArrayList<IceServer> iceServers) {
        this.iceServers = iceServers;
    }

    public void addIceServer(String user, String credential, String domain) {
        if (iceServers == null) {
            iceServers = new ArrayList<>();
        }
        IceServer iceServer = new IceServer(user, credential, domain);
        iceServers.add(iceServer);
    }

    public String getCallSession() {
        return callSession;
    }

    public void setCallSession(String callSession) {
        this.callSession = callSession;
    }

    public String getAttrStatus() {
        return attrStatus;
    }

    public void setAttrStatus(String attrStatus) {
        this.attrStatus = attrStatus;
    }

    public boolean isSdp() {
        return callError == null && callData != null && "sdp".equals(callData.getType());
    }

    public boolean isCallConfide() {
        return isCallConfide;
    }

    public void setCallConfide(boolean callConfide) {
        isCallConfide = callConfide;
    }

    public String getStrangerAvatar() {
        return strangerAvatar;
    }

    public void setStrangerAvatar(String strangerAvatar) {
        this.strangerAvatar = strangerAvatar;
    }

    public String getStrangerPosterName() {
        return strangerPosterName;
    }

    public void setStrangerPosterName(String strangerPosterName) {
        this.strangerPosterName = strangerPosterName;
    }

    public boolean isVideoCall() {
        return isVideoCall;
    }

    public void setVideoCall(boolean videoCall) {
        isVideoCall = videoCall;
    }

    public boolean isOnlyAudio() {
        return isOnlyAudio;
    }

    public void setOnlyAudio(boolean onlyAudio) {
        isOnlyAudio = onlyAudio;
    }

    @Override
    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (getLanguage() != null) {
            buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
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
        if (getType() != ReengMessagePacket.Type.normal) {
            buf.append(" type=\"").append(getType()).append("\"");
        } else if (getTypeString() != null) {
            buf.append(" type=\"").append(getTypeString()).append("\"");
        }
        if (getSubType() != ReengMessagePacket.SubType.normal) {
            buf.append(" subtype=\"").append(getSubType()).append("\"");
        } else if (getSubTypeString() != null) {
            buf.append(" subtype=\"").append(getSubTypeString()).append("\"");
        }
        // attribute external
        if (!TextUtils.isEmpty(getExternal())) {
            buf.append(" external=\"").append(StringUtils.escapeForXML(getExternal())).append("\"");
        }
        if (getSender() != null) {
            buf.append(" member=\"").append(getSender()).append("\"");
        }
        if (getSenderName() != null) {
            buf.append(" name=\"").append(StringUtils.escapeForXML(getSenderName())).append("\"");
        }
        if (getTimeSend() != -1L) {
            buf.append(" timesend=\"").append(getTimeSend()).append("\"");
        }
        if (getTimeReceive() != -1L) {
            buf.append(" timereceive=\"").append(getTimeReceive()).append("\"");
        }
        if (!TextUtils.isEmpty(attrStatus)) {
            buf.append(" status=\"").append(attrStatus).append("\"");
        }
        if (!TextUtils.isEmpty(getAvnoNumber())) {
            buf.append(" virtual=\"").append(getAvnoNumber()).append("\"");
        }
        buf.append(">");
        // element
        if (getGroupClass() != -1) {
            buf.append("<gtype>").append(getGroupClass()).append("</gtype>");
        }
        if (getCState() != -1) {
            buf.append("<cstate>").append(getCState()).append("</cstate>");
        }
        if (getBody() != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        }
        if (isNoStore()) {
            buf.append("<no_store/>");
        }
        if (isCallConfide) {
            buf.append("<talk_stranger/>");
        }
        if (isVideoCall) {
            buf.append("<video_call/>");
        }
        if (isOnlyAudio) {
            buf.append("<only_audio/>");
        }
        if (!TextUtils.isEmpty(getOfficalName())) {
            buf.append("<officalname>").append(StringUtils.escapeForXML(getOfficalName())).append("</officalname>");
        }
        if (!TextUtils.isEmpty(getNick())) {
            buf.append("<nick>").append(StringUtils.escapeForXML(getNick())).append("</nick>");
        }
        if (!TextUtils.isEmpty(strangerAvatar)) {
            buf.append("<lastchangeavatar>").append(StringUtils.escapeForXML(strangerAvatar)).append("</lastchangeavatar>");
        }
        if (!TextUtils.isEmpty(strangerPosterName)) {
            buf.append("<postername>").append(StringUtils.escapeForXML(strangerPosterName)).append("</postername>");
        }
        if (!TextUtils.isEmpty(getAppId())) {
            buf.append("<app_id>").append(StringUtils.escapeForXML(getAppId())).append("</app_id>");
        }
        //call info
        buf.append("<callinfo>");
        if (!TextUtils.isEmpty(caller)) {
            buf.append("<caller>").append(caller).append("</caller>");
        }
        if (!TextUtils.isEmpty(callee)) {
            buf.append("<callee>").append(callee).append("</callee>");
        }
        if (callError != null && callError != CallError.non) {
            buf.append("<error>").append(callError.getValue()).append("</error>");
        }
        if (callData != null) {
            buf.append(callData.toXml());
        }
        if (iceServers != null && !iceServers.isEmpty()) {
            buf.append("<iceservers>");
            for (IceServer iceServer : iceServers) {
                buf.append(iceServer.toXml());
            }
            buf.append("</iceservers>");
        }
        if (callSession != null) {
            buf.append("<session>").append(callSession).append("</session>");
        }
        if(timeConnect!=0){
            buf.append("<cst>").append(timeConnect).append("</cst>");
        }
        if(!TextUtils.isEmpty(languageCode)){
            buf.append("<language>").append(languageCode).append("</language>");
        }
        if(!TextUtils.isEmpty(countryCode)){
            buf.append("<country>").append(countryCode).append("</country>");
        }
        buf.append("<CALL_KPI/>");
        buf.append("</callinfo>");
        // Append the error subpacket if the message type is an error.
        if (getType() == ReengMessagePacket.Type.error) {
            XMPPError error = getError();
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    public enum CallError {
        invite("invite"),
        trying("100"),
        ringing("180"),
        connected("200"),
        reconnect("202"),
        timeOut("480"),
        busyCall("486"),
        endCall("603"),
        lastSeen102("102"),
        notSupport("404"),
        videoEnable("1000"),
        videoDisable("1001"),
        non("non"),
        timeConnect("204");

        private String value;

        CallError(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CallError fromString(String name) {
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            for (CallError b : CallError.values()) {
                if (name.equals(b.value)) {
                    return b;
                }
            }
            return null;
        }
    }
}