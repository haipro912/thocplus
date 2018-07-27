package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by toanvk2 on 12/1/14.
 */
public class IQGetInfo extends IQ {
    private String nameSpace = "reeng:iq:contacts";
    private String jid = null;
    private int state = -1;
    private long lastOn = 0;
    private long lastSeen = -1;

    public IQGetInfo() {

    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastOn() {
        return lastOn;
    }

    public void setLastOn(long lastOn) {
        this.lastOn = lastOn;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
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
            buf.append("<query xmlns=\"").append(nameSpace).append("\">");
        // add name of group
        if (jid != null) {
            buf.append("<item jid=\"").append(StringUtils.escapeForXML(jid)).append("\"/>");
        }
        if (state >= 0) {
            buf.append("<item state=\"").append(state).
                    append("\" lastOnline=\"").append(lastOn).
                    append("\" lastSeen=\"").append(lastSeen).append("\"/>");
        }
        buf.append("</query>");
        buf.append("</iq>");
        return buf.toString();
    }
}