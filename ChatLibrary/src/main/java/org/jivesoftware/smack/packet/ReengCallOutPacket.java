package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import org.jivesoftware.smack.model.IceServer;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by thanhnt72 on 7/25/2016.
 */
public class ReengCallOutPacket extends ReengMessagePacket {
    private String caller;
    private String callee;
    private CallOutType callOutType = CallOutType.non;
    private CallStatus callStatus;
    private String callStatusStr;
    private String callOutData;
    private ArrayList<IceServer> iceServers;
    private String callSession;
    private String attrStatus;
    private boolean isCallConfide = false;

    public ReengCallOutPacket() {
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


    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallStatusStr() {
        return callStatusStr;
    }

    public void setCallStatusStr(String callStatusStr) {
        this.callStatusStr = callStatusStr;
    }

    public String getCallOutData() {
        return callOutData;
    }

    public void setCallOutData(String callOutData) {
        this.callOutData = callOutData;
    }

    public CallOutType getCallOutType() {
        return callOutType;
    }

    public void setCallOutType(CallOutType callOutType) {
        this.callOutType = callOutType;
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

    public boolean isCallConfide() {
        return isCallConfide;
    }

    public void setCallConfide(boolean callConfide) {
        isCallConfide = callConfide;
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
        if (getType() != Type.normal) {
            buf.append(" type=\"").append(getType()).append("\"");
        } else if (getTypeString() != null) {
            buf.append(" type=\"").append(getTypeString()).append("\"");
        }
        if (getSubType() != SubType.normal) {
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
        if (getBody() != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        }
        if (isNoStore()) {
            buf.append("<no_store/>");
        }
        if (isCallConfide) {
            buf.append("<talk_stranger/>");
        }
        if (!TextUtils.isEmpty(getOfficalName())) {
            buf.append("<officalname>").append(StringUtils.escapeForXML(getOfficalName())).append("</officalname>");
        }
        if (!TextUtils.isEmpty(getNick())) {
            buf.append("<nick>").append(StringUtils.escapeForXML(getNick())).append("</nick>");
        }
        if (!TextUtils.isEmpty(getAppId())) {
            buf.append("<app_id>").append(StringUtils.escapeForXML(getAppId())).append("</app_id>");
        }
        //call out type
        if (callOutType != null) {
            buf.append("<type>").append(callOutType.getValue()).append("</type>");
        }
        //call info
        buf.append("<callinfo>");
        if (!TextUtils.isEmpty(caller)) {
            buf.append("<caller>").append(caller).append("</caller>");
        }
        if (!TextUtils.isEmpty(callee)) {
            buf.append("<callee>").append(callee).append("</callee>");
        }
        /*if (!TextUtils.isEmpty(avnoNumber)) {
            buf.append("<virtualNum>").append(avnoNumber).append("</virtualNum>");
        }*/
        //TODO phiên bản ban đầu đặt tên là thẻ status, sau đối tác stringee sửa thành error nên sửa thành thẻ error, tên trường giữ nguyên
        if (callStatus != null && callStatus != CallStatus.non) {
            buf.append("<error>").append(callStatus.getValue()).append("</error>");
        }
        if (!TextUtils.isEmpty(callOutData)) {
            buf.append("<data>").append("<![CDATA[").append(callOutData).append("]]>").append("</data>");
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
        if (getType() == Type.error) {
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

    public enum CallOutType {
        invite("1"),
        data_sdp("2"),
        data_canditate("3"),
        receive_status("4"),
        stop_call("5"),
        idle_rtp("8"),
        non("0");
        private String value;

        CallOutType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CallOutType fromString(String name) {
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            for (CallOutType b : CallOutType.values()) {
                if (name.equals(b.value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public enum CallStatus {
        session_fail("-1"),
        session_ok("50"),
        trying("100"),
        ringing("180"),
        ringing_183("183"),
        connected("200"),
        timeOut("480"),
        busyCall("486"),
        endCall("603"),
        endCall_700("700"),
        non("non"),
        timeConnect("204");

        private String value;

        CallStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CallStatus fromString(String name) {
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            for (CallStatus b : CallStatus.values()) {
                if (name.equals(b.value)) {
                    return b;
                }
            }
            return null;
        }

        public static boolean containsEndCall(CallStatus status) {
            return status == timeOut || status == endCall || status == endCall_700;
        }

    }
}