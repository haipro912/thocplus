package org.jivesoftware.smack.packet;

import com.viettel.util.Log;

import org.jivesoftware.smack.util.StringUtils;

public class VoicemailGSMResponseMessage extends Packet {
    private Type type = Type.chat;
    private boolean sent = false;
    private int errorCode = 0;
    private String errorType = null;
    private int length = 0;
    private String voicemailId;
    public final static String NAMESPACE = "vt:message:event";

    public VoicemailGSMResponseMessage() {
    }

    public VoicemailGSMResponseMessage(String to) {
        setTo(to);
    }

    public VoicemailGSMResponseMessage(String to, Type type) {
        setTo(to);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getVoicemailId() {
        return voicemailId;
    }

    public void setVoicemailId(String voicemailId) {
        this.voicemailId = voicemailId;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
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
        if (type != null) {
            buf.append(" type=\"").append(type).append("\"");
        }
        buf.append(">");
        if (sent) {
            buf.append("<x xmlns=\"").append(NAMESPACE)
                    .append("\"><sent/></x>");
        } else {
            buf.append("<error");
            if (errorCode > 0) {
                buf.append(" code=\"").append(errorCode).append("\"");
            }
            if (errorType != null) {
                buf.append(" type=\"").append(errorType).append("\"");
            }
            buf.append("/>");
        }

        if (voicemailId != null) {
            buf.append("<id>").append(voicemailId).append("</id>");
        }
        if (length >= 0) {
            buf.append("<length>").append(length).append("</length>");
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        VoicemailGSMResponseMessage message = (VoicemailGSMResponseMessage) o;
        return type == message.type;

    }

    public int hashCode() {
        int result;
        result = (type != null ? type.hashCode() : 0);
        return result;
    }

    public enum Type {
        error, chat, groupchat;

        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                Log.e("VoiceMailGsm", "Exception", e);
                return chat;
            }
        }
    }
}