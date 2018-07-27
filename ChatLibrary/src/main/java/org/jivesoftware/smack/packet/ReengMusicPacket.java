package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import com.viettel.util.ConvertHelper;
import com.viettel.util.Log;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by toanvk2 on 02/04/2015.
 */
public class ReengMusicPacket extends ReengMessagePacket {
    private String sessionId;
    private String songId;
    private int roomStateOnline = -1;
    private int roomStateMusic = -1;// 0 là room không có nhạc, 1 là có nhạc
    private String songName;
    private String singer;
    private String songUrl;// link chia se bai hat
    private String mediaUrl;    // link play bai hat
    private String songThumb;
    private int songType = -1;

    private String responsePacketId;
    private String strangerAvatar;      // last change avatar cua ban
    private String strangerPosterName;          // ten cua minh dung khi tao loi moi cung nghe voi nguoi la
    private MusicStatus musicStatus = MusicStatus.error;
    private MusicAction musicAction = MusicAction.error;
    private String crbtCode;
    private String crbtPrice;
    private String session;
    private int autoPlayVideo = 0;

    /**
     * Creates a new, "normal" message.
     */
    public ReengMusicPacket() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getSongThumb() {
        return songThumb;
    }

    public void setSongThumb(String songThumb) {
        this.songThumb = songThumb;
    }

    public int getSongType() {
        return songType;
    }

    public void setSongType(int songType) {
        this.songType = songType;
    }

    public MusicStatus getMusicStatus() {
        return musicStatus;
    }

    public void setMusicStatus(MusicStatus musicStatus) {
        this.musicStatus = musicStatus;
    }

    public MusicAction getMusicAction() {
        return musicAction;
    }

    public void setMusicAction(MusicAction musicAction) {
        this.musicAction = musicAction;
    }

    public String getResponsePacketId() {
        return responsePacketId;
    }

    public void setResponsePacketId(String responsePacketId) {
        this.responsePacketId = responsePacketId;
    }

    public String getStrangerAvatar() {
        return strangerAvatar;
    }

    public void setStrangerAvatar(String strangerAvatar) {
        this.strangerAvatar = strangerAvatar;
    }

    public String getStrangerPosterName() {
        return strangerPosterName;
    }

    public void setStrangerPosterName(String strangerPosterName) {
        this.strangerPosterName = strangerPosterName;
    }

    public int getRoomStateOnline() {
        return roomStateOnline;
    }

    public void setRoomStateOnline(String stateStr) {
        this.roomStateOnline = ConvertHelper.parserIntFromString(stateStr, -1);
    }

    public int getRoomStateMusic() {
        return roomStateMusic;
    }

    public void setRoomStateMusic(String stateStr) {
        this.roomStateMusic = ConvertHelper.parserIntFromString(stateStr, -1);
    }

    public String getCrbtCode() {
        return crbtCode;
    }

    public void setCrbtCode(String crbtCode) {
        this.crbtCode = crbtCode;
    }

    public String getCrbtPrice() {
        return crbtPrice;
    }

    public void setCrbtPrice(String crbtPrice) {
        this.crbtPrice = crbtPrice;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getAutoPlayVideo() {
        return autoPlayVideo;
    }

    public void setAutoPlayVideo(int autoPlayVideo) {
        this.autoPlayVideo = autoPlayVideo;
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
        if (!TextUtils.isEmpty(getAvnoNumber())) {
            buf.append(" virtual=\"").append(getAvnoNumber()).append("\"");
        }
        buf.append(">");
        // element
        if (getGroupClass() != -1) {
            buf.append("<gtype>").append(getGroupClass()).append("</gtype>");
        }
        if (getCState() != -1) {
            buf.append("<cstate>").append(getCState()).append("</cstate>");
        }
        if (getBody() != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        }
        if (isNoStore()) {
            buf.append("<no_store/>");
        }
        if (roomStateOnline >= 0) {
            buf.append("<room_online>").append(roomStateOnline).append("</room_online>");
        }
        if (!TextUtils.isEmpty(strangerAvatar)) {
            buf.append("<lastchangeavatar>").append(StringUtils.escapeForXML(strangerAvatar)).append("</lastchangeavatar>");
        }
        if (!TextUtils.isEmpty(strangerPosterName)) {
            buf.append("<postername>").append(StringUtils.escapeForXML(strangerPosterName)).append("</postername>");
        }
        //   media detail
        if (sessionId != null) {
            buf.append("<sessionid>").append(sessionId).append("</sessionid>");
        }
        if (!TextUtils.isEmpty(songId)) {
            buf.append("<songid>").append(songId).append("</songid>");
        }
        if (songName != null) {
            buf.append("<songname>").append(StringUtils.escapeForXML(songName)).append("</songname>");
        }
        if (singer != null) {
            buf.append("<singername>").append(StringUtils.escapeForXML(singer)).append("</singername>");
        }
        if (songUrl != null) {
            buf.append("<songurl>").append(StringUtils.escapeForXML(songUrl)).append("</songurl>");
        }
        if (mediaUrl != null) {
            buf.append("<mediaurl>").append(StringUtils.escapeForXML(mediaUrl)).append("</mediaurl>");
        }
        if (songThumb != null) {
            buf.append("<songthumb>").append(StringUtils.escapeForXML(songThumb)).append("</songthumb>");
        }
        if (songType != -1) {
            buf.append("<songtype>").append(songType).append("</songtype>");
        }
        if (musicStatus != null && musicStatus != MusicStatus.error) {
            buf.append("<status>").append(musicStatus).append("</status>");
        }
        if (musicAction != null && musicAction != MusicAction.error) {
            buf.append("<action>").append(musicAction).append("</action>");
        }
        if (responsePacketId != null) {
            buf.append("<id>").append(responsePacketId).append("</id>");
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
        if (!TextUtils.isEmpty(crbtCode)) {
            buf.append("<crbt_code>").append(crbtCode).append("</crbt_code>");
        }
        if (!TextUtils.isEmpty(crbtPrice)) {
            buf.append("<crbt_price>").append(crbtPrice).append("</crbt_price>");
        }
        if (!TextUtils.isEmpty(session)) {
            buf.append("<session>").append(session).append("</session>");
        }
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

    public enum MusicStatus {
        available, busy, error;

        public static MusicStatus fromString(String name) {
            try {
                return MusicStatus.valueOf(name);
            } catch (Exception e) {
                Log.e("MusicPacket", "Exception", e);
                return error;
            }
        }
    }

    public enum MusicAction {
        change, add, remove, invite, error;

        public static MusicAction fromString(String name) {
            try {
                return MusicAction.valueOf(name);
            } catch (Exception e) {
                Log.e("MusicPacket","Exception",e);
                return error;
            }
        }
    }
}