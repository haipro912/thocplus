package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toanvk2 on 27/11/2015.
 */
public class IQInfo extends IQ {
    public static final String NAME_SPACE_LOCATION = "mocha:iq:strangerLocation";
    public static final String NAME_SPACE_CONTACT = "contactInfo";
    public static final String NAME_SPACE_UNKNOWN_MSG = "mocha:iq:unknowmsg";
    public static final String NAME_SPACE_GCM = "reeng:iq:gcm";
    public static final String NAME_SPACE_CLIENT_INFO = "client_info";
    public static final String NAME_SPACE_STAR_UN_FOLLOW = "star_unfollow";

    private String nameSpace = null;
    private HashMap<String, String> elements = null;

    public IQInfo() {
        elements = new HashMap<String, String>();
    }

    public IQInfo(String nameSpace) {
        this.nameSpace = nameSpace;
        elements = new HashMap<String, String>();
    }

    public IQInfo(String nameSpace, HashMap<String, String> elements) {
        this.nameSpace = nameSpace;
        this.elements = elements;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public HashMap<String, String> getElements() {
        return elements;
    }

    public void setElements(HashMap<String, String> elements) {
        this.elements = elements;
    }

    public void addElements(String key, String value) {
        if (elements == null) {
            elements = new HashMap<String, String>();
        }
        elements.put(key, value);
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
        if (nameSpace != null) {
            buf.append("<query xmlns=\"").append(nameSpace).append("\">");
            if (elements != null && !elements.isEmpty()) {
                for (Map.Entry<String, String> entry : elements.entrySet()) {
                    buf.append("<").append(entry.getKey()).append(">").
                            append(StringUtils.escapeForXML(entry.getValue())).
                            append("</").append(entry.getKey()).append(">");
                }
            }
            // end add query
            buf.append("</query>");
        }
        buf.append("</iq>");
        return buf.toString();
    }

    public static boolean containsIQInfo(String nameSpace) {
        return NAME_SPACE_CONTACT.equals(nameSpace) ||
                NAME_SPACE_UNKNOWN_MSG.equals(nameSpace) ||
                NAME_SPACE_GCM.equals(nameSpace) ||
                NAME_SPACE_CLIENT_INFO.equals(nameSpace) ||
                NAME_SPACE_STAR_UN_FOLLOW.equals(nameSpace) ||
                NAME_SPACE_LOCATION.equals(nameSpace);
    }
}