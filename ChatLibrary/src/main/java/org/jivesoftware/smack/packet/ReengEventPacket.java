package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import com.viettel.util.Log;

import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 11/13/14.
 */
public class ReengEventPacket extends ReengMessagePacket {
    // event
    private final int DEFAULT_SEEN_STATE = -2;
    private ArrayList<String> listEventId = null;
    private EventType eventType = EventType.error;
    private boolean force = false;
    private String sms_remain = null;
    private String sms_state = null;
    private String sms_desc = null;
    private int seenState = DEFAULT_SEEN_STATE;
    private String bannerAction;
    private ArrayList<JSONObject> bannerJson;
    private String offline;

    /**
     * Creates a new, "normal" message.
     */
    public ReengEventPacket() {
        setSubType(SubType.event);
        listEventId = new ArrayList<String>();
    }

    /**
     * packet id
     *
     * @return
     */

    public void setOffline(String offline){
        this.offline=offline;
    }
    public ArrayList<String> getListIdOfEvent() {
        return listEventId;
    }

    public void addToListIdOfEvent(String eventId) {
        listEventId.add(eventId);
    }

    public void setSeenState(int seen) {
        seenState = seen;
    }

    public int getSeenState() {
        return seenState;
    }

    /**
     * event type (delivered, sent...)
     *
     * @return
     */
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(String forceString) {
        this.force = "1".equals(forceString);
    }

    public String getSmsRemain() {
        return sms_remain;
    }

    public void setSmsRemain(String sms_remain) {
        this.sms_remain = sms_remain;
    }

    public String getSmsState() {
        return sms_state;
    }

    public void setSmsState(String sms_state) {
        this.sms_state = sms_state;
    }

    public String getSmsDesc() {
        return sms_desc;
    }

    public void setSmsDesc(String smsDesc) {
        this.sms_desc = smsDesc;
    }

    public String getBannerAction() {
        return bannerAction;
    }

    public void setBannerAction(String bannerAction) {
        this.bannerAction = bannerAction;
    }

    public ArrayList<JSONObject> getBannerJson() {
        return bannerJson;
    }

    public void addBannerJson(JSONObject banner) {
        if (bannerJson == null) bannerJson = new ArrayList<>();
        bannerJson.add(banner);
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
        if (getSender() != null) {
            buf.append(" member=\"").append(getSender()).append("\"");
        }
        if (getTimeSend() != -1L) {
            buf.append(" timesend=\"").append(getTimeSend()).append("\"");
        }
        if (!TextUtils.isEmpty(getAvnoNumber())) {
            buf.append(" virtual=\"").append(getAvnoNumber()).append("\"");
        }
        buf.append(">");
        /**
         * <delivered seen = "1"/>
         * eventType la delivered thi them seen
         */
        if (eventType != EventType.error) {
            buf.append("<").append(eventType);
            if (eventType == EventType.delivered && seenState != DEFAULT_SEEN_STATE) {
                buf.append(" seen=\"").append(seenState).append("\"");
            }
            buf.append("/>");
        }
        if (listEventId != null && !listEventId.isEmpty()) {
            for (String packetId : listEventId) {
                buf.append("<id>").append(packetId).append("</id>");
            }
        }
        if (getSubType() == SubType.update) {
            if (getLink() != null) {
                buf.append("<link>").append(getLink()).append("</link>");
            }
            buf.append("<force>").append(force).append("</force>");
        } else if (getSubType() == SubType.sms_out) {
            buf.append("<smsout").append(" remain=\"").append(sms_remain).append("\"").
                    append(" type=\"").append(sms_state).append("\"").append("/>");
        }
        if (getBody() != null)
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        if (isNoStore())
            buf.append("<no_store/>");
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

    public enum EventType {
        delivered, displayed, body, error;

        public static EventType fromString(String name) {
            try {
                return EventType.valueOf(name);
            } catch (Exception e) {
                Log.e("ReengEvent", "Exception", e);
                return error;
            }
        }
    }
}