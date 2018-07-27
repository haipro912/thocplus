package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by toanvk2 on 12/1/14.
 */
public class Ping extends IQ {
    //    private String nameSpace = "reeng:iq:ping";
    private String nameSpace = "urn:xmpp:ping";

    public Ping() {
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Ping(Type type) {
        setType(type);
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
        if (nameSpace != null)
            buf.append("<query xmlns=\"").append(nameSpace).append("\">").append("</query>");
        buf.append("</iq>");
        return buf.toString();
    }
}
