package com.vttm.chatlib.packet;

import com.vttm.chatlib.utils.Log;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.util.StringUtils;

public class GSMMessagePacket extends Stanza {
    private Type type = Type.error;
    private boolean sended = false;
    private int errorCode = 0;
    private String errorType = null;
    public final static String NAME_SPACE = "vt:message:event";
    public final static int ERROR_OVER_TOTAL_MESSAGES = 2;
    public final static int ERROR_OVER_24H_MESSAGES = 1;

    public GSMMessagePacket() {
    }

    public GSMMessagePacket(String to) {
        setTo(to);
    }

    public GSMMessagePacket(String to, Type type) {
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

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
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

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        StringBuilder buf = new StringBuilder();
//        buf.append("<message");
//        if (getXmlns() != null) {
//            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
//        }
//        if (getPacketID() != null) {
//            buf.append(" id=\"").append(getPacketID()).append("\"");
//        }
//        if (getTo() != null) {
//            buf.append(" to=\"").append(StringUtils.escapeForXml(getTo()))
//                    .append("\"");
//        }
//        if (getFrom() != null) {
//            buf.append(" from=\"").append(StringUtils.escapeForXml(getFrom()))
//                    .append("\"");
//        }
//        if (type != null) {
//            buf.append(" type=\"").append(type).append("\"");
//        }
//        buf.append(">");
//        if (sended) {
//            buf.append("<x xmlns=\"").append(NAME_SPACE)
//                    .append("\"><sended/></x>");
//        } else {
//            buf.append("<error");
//            if (errorCode > 0) {
//                buf.append(" code=\"").append(errorCode).append("\"");
//            }
//            if (errorType != null) {
//                buf.append(" type=\"").append(errorType).append("\"");
//            }
//            buf.append("/>");
//        }
//        // Add packet extensions, if any are defined.
//        buf.append(getExtensionsXML());
//        buf.append("</message>");
        return buf.toString();
    }

    @Override
    public String toString() {
        return "GSMMessagePacket{" +
                "type=" + type +
                ", sended=" + sended +
                ", errorCode=" + errorCode +
                ", errorType='" + errorType + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GSMMessagePacket message = (GSMMessagePacket) o;
        return type == message.type;

    }

    public int hashCode() {
        int result;
        result = (type != null ? type.hashCode() : 0);
        return result;
    }

    public enum Type {
        error;

        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                Log.e("GSMMessage", "Exception", e);
                return error;
            }
        }
    }
}