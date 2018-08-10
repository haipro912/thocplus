package com.vttm.chatlib.packet;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.util.StringUtils;

public class EventReceivedMessagePacket extends Stanza {

    public static final String NAMESPACE = "vt:message:event";
    private String messageID = null;

    public EventReceivedMessagePacket() {
    }

    public EventReceivedMessagePacket(String to) {
        setTo(to);
    }


    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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
//
//        if (NAMESPACE != null) {
//            buf.append(" ns=\"").append(NAMESPACE).append("\"");
//        }
//
//        buf.append(">");
//
//        buf.append("<received><received/>");
//
//        // Add message id
//        if (messageID != null) {
//            buf.append("<id>").append(messageID).append("</id>");
//        }
//        buf.append("</message>");
        return buf.toString();
    }

    @Override
    public String toString() {
        return "EventReceivedMessagePacket{" +
                "messageID='" + messageID + '\'' +
                '}';
    }
}