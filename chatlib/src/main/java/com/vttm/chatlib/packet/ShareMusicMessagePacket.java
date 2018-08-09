package com.vttm.chatlib.packet;

import com.vttm.chatlib.utils.Log;

import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.util.StringUtils;

public class ShareMusicMessagePacket extends Stanza {
    private String typeString = null;
    private ReengMessagePacket.Type type = ReengMessagePacket.Type.chat;
    private String sender;
    private long songId = -1;
    private int state = 1;
    private SubType subType = SubType.nc_create;
    private String receiver;
    private String songName;
    private String errorCode;
    private String songUrl;
    private String singerName;
    private String songThumb;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public String getTypeString() {
        return typeString;
    }

    public ReengMessagePacket.Type getType() {
        return type;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
        try {
            type = ReengMessagePacket.Type.valueOf(typeString);
        } catch (Exception e) {
            Log.e("ShareMusicPacket","Exception",e);
            type = ReengMessagePacket.Type.normal;
        }
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongThumb() {
        return songThumb;
    }

    public void setSongThumb(String songThumb) {
        this.songThumb = songThumb;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXml(getTo()))
                    .append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXml(getFrom()))
                    .append("\"");
        }

        if (type != ReengMessagePacket.Type.normal) {
            buf.append(" type=\"").append(type).append("\"");
        }
        buf.append(" subtype=\"").append(subType).append("\"");
        if (subType == SubType.nc_join) {
            if (getReceiver() != null) {
                buf.append(" member=\"").append(getReceiver()).append("\"");
            }
        }

        buf.append(">");
        if (subType == SubType.nc_leave) {
            buf.append("<response>").append("i'm busy").append("</response>");
        }
        if (getReceiver() != null) {
            buf.append("<member>").append(getReceiver()).append("</member>");
        }
        if (songId != -1) {
            buf.append("<songid>").append(songId)
                    .append("</songid>");
        }
        if (songName != null) {
            buf.append("<songname>").append(StringUtils.escapeForXml(songName))
                    .append("</songname>");
        }
        if (singerName != null) {
            buf.append("<singername>").append(StringUtils.escapeForXml(singerName))
                    .append("</singername>");
        }
        if (songUrl != null) {
            buf.append("<songurl>").append(StringUtils.escapeForXml(songUrl))
                    .append("</songurl>");
        }
        if (songThumb != null) {
            buf.append("<songthumb>").append(StringUtils.escapeForXml(songThumb))
                    .append("</songthumb>");
        }
        buf.append("<packetid>").append(getPacketID()).append("</packetid>");
        if (state != 0) {
            buf.append("<state>").append(getState())
                    .append("</state>");
        }
        buf.append("</message>");
        return buf.toString();
    }

    @Override
    public String toString() {
        return "ShareMusicMessagePacket{" +
                "typeString='" + typeString + '\'' +
                ", type=" + type +
                ", sender='" + sender + '\'' +
                ", songId=" + songId +
                ", state=" + state +
                ", subType=" + subType +
                ", receiver='" + receiver + '\'' +
                ", songName='" + songName + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", singerName='" + singerName + '\'' +
                ", songThumb='" + songThumb + '\'' +
                '}';
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Represents the type of a message.
     */
    public enum SubType {

        /**
         * moi cung nghe
         */
        nc_create,
        /**
         * invite B
         */
        nc_invite,
        /**
         * tao room khong thanh cong do internal error hoac timeout
         */
        nc_error,
        /**
         * tao room thanh cong
         */
        nc_success,
        /**
         * dong y join vao room
         */
        nc_join,
        /**
         * qua thoi gian timeout
         */
        nc_expired,
        /**
         * message share contact
         */
        nc_lastactivity,
        /**
         * khong dong y tham gia cung nghe
         */
        nc_reject,
        /**
         * mat ket noi
         */
        nc_unavaiable,
        /**
         * chu dong roi phong
         */
        nc_leave;

        public static SubType fromString(String name) {
            try {
                return SubType.valueOf(name);
            } catch (Exception e) {
                Log.e("ShareMusicPacket","Exception",e);
                return nc_lastactivity;
            }
        }

        public static boolean contains(String name) {
            for (SubType c : SubType.values()) {
                if (c.name().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }
}
