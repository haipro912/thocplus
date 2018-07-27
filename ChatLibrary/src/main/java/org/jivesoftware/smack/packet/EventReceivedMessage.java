package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by vtsoft on 10/29/2014.
 */
public class EventReceivedMessage extends Packet {

    public static final String NAMESPACE = "vt:message:event";
    private String messageID = null;

    public EventReceivedMessage() {
    }

    public EventReceivedMessage(String to) {
        setTo(to);
    }


    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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

        if (NAMESPACE != null) {
            buf.append(" ns=\"").append(NAMESPACE).append("\"");
        }

        buf.append(">");

        buf.append("<received><received/>");

        // Add message id
        if (messageID != null) {
            buf.append("<id>").append(messageID).append("</id>");
        }
        buf.append("</message>");
        return buf.toString();
    }
}