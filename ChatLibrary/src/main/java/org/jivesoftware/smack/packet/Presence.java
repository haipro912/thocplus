/**
 * $RCSfile$
 * $Revision: 10907 $
 * $Date: 2008-11-19 21:54:05 +0100 (mer. 19 nov. 2008) $
 * <p/>
 * Copyright 2003-2007 Jive Software.
 * <p/>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack.packet;

import android.text.TextUtils;

import com.viettel.util.ConvertHelper;
import com.viettel.util.Log;

import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

/**
 * Represents XMPP presence packets. Every presence packet has a type, which is
 * one of the following values:
 * <ul>
 * <li>{@link Presence.Type#available available} -- (Default) indicates the user
 * is available to receive messages.
 * <li>{@link Presence.Type#unavailable unavailable} -- the user is unavailable
 * to receive messages.
 * <li>{@link Presence.Type#subscribe subscribe} -- request subscription to
 * sender's presence.
 * <li>{@link Presence.Type#unsubscribe unsubscribe} -- request removal of
 * sender's presence.
 * <li>{@link Presence.Type#error error} -- the presence packet contains an
 * error message.
 * </ul>
 * <p/>
 * <p/>
 * A number of attributes are optional:
 * <ul>
 * <li>Status -- free-form text describing a user's presence (i.e., gone to
 * lunch).
 * <li>Priority -- non-negative numerical priority of a sender's resource. The
 * highest resource priority is the default recipient of packets not addressed
 * to a particular resource.
 * <li>Mode -- one of five presence modes: {@link Mode#available available} (the
 * default), {@link Mode#chat chat}, {@link Mode#away away}, {@link Mode#xa xa}
 * (extended away), and {@link Mode#dnd dnd} (do not disturb).
 * </ul>
 * <p/>
 * <p/>
 * Presence packets are used for two purposes. First, to notify the server of
 * our the clients current presence status. Second, they are used to subscribe
 * and unsubscribe users from the roster.
 *
 * @author Matt Tucker
 * @see RosterPacket
 */
public class Presence extends Packet {
    private static final String TAG = Presence.class.getSimpleName();
    private Type type = Type.available;
    private SubType subType = SubType.normal;
    private String showType = null;
    // private String avatarRV=null;
    private String status = null;
    private String state = null;
    private String lastAvatar = null;
    private long timeStamp = -1;
    private long nowSv = -1;
    private int priority = Integer.MIN_VALUE;
    private Mode mode = null;
    private String language;
    // change domain
    private String domainFile = null;
    private String domainMsg = null;
    private String domainOnMedia = null;
    private String domainImage = null;
    private String domainServiceKeeng = null;
    private String domainMedia2Keeng = null;
    private String domainImageKeeng = null;
    private int vip = -1;
    private int cbnv = -1;
    private int call = -1;
    private int callOut = -1;
    private int ssl = -1;
    private int smsIn = -1;
    private String avnoNumber = null;
    private String mochaApi = null;
    private int avnoEnable = -1;
    private int tabCallEnable = -1;
    //
    private String contactInfo;
    private String musicInfo;
    private ArrayList<String> subscribeMusicRooms;
    //state room chat
    private int joinState = -1;
    private int follow = -1;// so nguoi follow sao
    private String backgroundRoom = null;
    private String userName;
    private String userBirthdayStr;
    private String feed;
    private int permission = -1;
    private String lastStickyPacket;
    private int vipInfo = 0;
    private String locationId;

    /**
     * Creates a new presence update. Status, priority, and mode are left
     * un-set.
     *
     * @param type the type.
     */
    public Presence(Type type) {
        setType(type);
    }

    /**
     * Creates a new presence update with a specified status, priority, and
     * mode.
     *
     * @param type     the type.
     * @param status   a text message describing the presence update.
     * @param priority the priority of this presence update.
     * @param mode     the mode type for this presence update.
     */
    public Presence(Type type, String state, String status, int priority, Mode mode) {
        setType(type);
        setState(state);
        setStatus(status);
        setPriority(priority);
        setMode(mode);
    }

    /**
     * Returns true if the {@link Type presence type} is available (online) and
     * false if the user is unavailable (offline), or if this is a presence
     * packet involved in a subscription operation. This is a convenience method
     * equivalent to <tt>getType() == Presence.Type.available</tt>. Note that
     * even when the user is available, their presence mode may be
     * {@link Mode#away away}, {@link Mode#xa extended away} or {@link Mode#dnd
     * do not disturb}. Use {@link #isAway()} to determine if the user is away.
     *
     * @return true if the presence type is available.
     */
    public boolean isAvailable() {
        return type == Type.available;
    }

    /**
     * Returns true if the presence type is {@link Type#available available} and
     * the presence mode is {@link Mode#away away}, {@link Mode#xa extended
     * away}, or {@link Mode#dnd do not disturb}. False will be returned when
     * the type or mode is any other value, including when the presence type is
     * unavailable (offline). This is a convenience method equivalent to
     * <tt>type == Type.available && (mode == Mode.away || mode == Mode.xa || mode == Mode.dnd)</tt>
     * .
     *
     * @return true if the presence type is available and the presence mode is
     * away, xa, or dnd.
     */
    public boolean isAway() {
        return type == Type.available
                && (mode == Mode.away || mode == Mode.xa || mode == Mode.dnd);
    }

    /**
     * Returns the type of this presence packet.
     *
     * @return the type of the presence packet.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the presence packet.
     *
     * @param type the type of the presence packet.
     */
    public void setType(Type type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be null");
        }
        this.type = type;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        if (subType == null) {
            throw new NullPointerException("Type cannot be null");
        }
        this.subType = subType;
    }

    /**
     * Returns the status message of the presence update, or <tt>null</tt> if
     * there is not a status. The status is free-form text describing a user's
     * presence (i.e., "gone to lunch").
     *
     * @return the status message.
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the status message of the presence update. The status is free-form
     * text describing a user's presence (i.e., "gone to lunch").
     *
     * @param
     */
    public String getState() {
        if (state == null || state.length() <= 0)
            return null;
        else
            return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getNowSv() {
        return nowSv;
    }

    public void setNowSv(long nowSv) {
        this.nowSv = nowSv;
    }

    // get and set avatar
    public String getLastAvatar() {
        return lastAvatar;
    }

    // set
    public void setLastAvatar(String lastAvatar) {
        this.lastAvatar = lastAvatar;
    }


    public String getDomainFile() {
        return domainFile;
    }

    public void setDomainFile(String domainFile) {
        this.domainFile = domainFile;
    }

    public String getDomainMsg() {
        return domainMsg;
    }

    public void setDomainMsg(String domainMsg) {
        this.domainMsg = domainMsg;
    }

    public String getDomainOnMedia() {
        return domainOnMedia;
    }

    public void setDomainOnMedia(String domainOnMedia) {
        this.domainOnMedia = domainOnMedia;
    }

    public String getDomainImage() {
        return domainImage;
    }

    public void setDomainImage(String domainImage) {
        this.domainImage = domainImage;
    }

    public String getDomainServiceKeeng() {
        return domainServiceKeeng;
    }

    public void setDomainServiceKeeng(String domainServiceKeeng) {
        this.domainServiceKeeng = domainServiceKeeng;
    }

    public String getDomainMedia2Keeng() {
        return domainMedia2Keeng;
    }

    public void setDomainMedia2Keeng(String domainMedia2Keeng) {
        this.domainMedia2Keeng = domainMedia2Keeng;
    }

    public String getDomainImageKeeng() {
        return domainImageKeeng;
    }

    public void setDomainImageKeeng(String domainImageKeeng) {
        this.domainImageKeeng = domainImageKeeng;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getCBNV() {
        return cbnv;
    }

    public void setCBNV(int cbnv) {
        this.cbnv = cbnv;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getSSL() {
        return ssl;
    }

    public void setSSL(int ssl) {
        this.ssl = ssl;
    }

    public int getSmsIn() {
        return smsIn;
    }

    public void setSmsIn(int smsIn) {
        this.smsIn = smsIn;
    }

    public int getCallOut() {
        return callOut;
    }

    public void setCallOut(int callOut) {
        this.callOut = callOut;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(String musicInfo) {
        this.musicInfo = musicInfo;
    }

    public void setSubscribeMusicRooms(ArrayList<String> subscribeMusicRooms) {
        this.subscribeMusicRooms = subscribeMusicRooms;
    }

    public String getBackgroundRoom() {
        return backgroundRoom;
    }

    public void setBackgroundRoom(String backgroundRoom) {
        this.backgroundRoom = backgroundRoom;
    }

    public int getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(int vipInfo) {
        this.vipInfo = vipInfo;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Returns the priority of the presence, or Integer.MIN_VALUE if no priority
     * has been set.
     *
     * @return the priority.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the presence. The valid range is -128 through 128.
     *
     * @param priority the priority of the presence.
     * @throws IllegalArgumentException if the priority is outside the valid range.
     */
    public void setPriority(int priority) {
        if (priority < -128 || priority > 128) {
            throw new IllegalArgumentException("Priority value " + priority
                    + " is not valid. Valid range is -128 through 128.");
        }
        this.priority = priority;
    }

    /**
     * Returns the mode of the presence update, or <tt>null</tt> if the mode is
     * not set. A null presence mode value is interpreted to be the same thing
     * as {@link Presence.Mode#available}.
     *
     * @return the mode.
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets the mode of the presence update. A null presence mode value is
     * interpreted to be the same thing as {@link Presence.Mode#available}.
     *
     * @param mode the mode.
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Returns the xml:lang of this Presence, or null if one has not been set.
     *
     * @return the xml:lang of this Presence, or null if one has not been set.
     * @since 3.0.2
     */
    private String getLanguage() {
        return language;
    }

    /**
     * Sets the xml:lang of this Presence.
     *
     * @param language the xml:lang of this Presence.
     * @since 3.0.2
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    public void setJoinState(int joinState) {
        this.joinState = joinState;
    }

    public void setJoinStateStr(String stateStr) {
        this.joinState = ConvertHelper.parserIntFromString(stateStr, -1);
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBirthdayStr() {
        return userBirthdayStr;
    }

    public void setUserBirthdayStr(String userBirthdayStr) {
        this.userBirthdayStr = userBirthdayStr;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public void setLastStickyPacket(String lastStickyPacket) {
        this.lastStickyPacket = lastStickyPacket;
    }

    public String getAvnoNumber() {
        return avnoNumber;
    }

    public void setAvnoNumber(String avnoNumber) {
        this.avnoNumber = avnoNumber;
    }

    public String getMochaApi() {
        return mochaApi;
    }

    public void setMochaApi(String mochaApi) {
        this.mochaApi = mochaApi;
    }

    public int getAvnoEnable() {
        return avnoEnable;
    }

    public void setAvnoEnable(int avnoEnable) {
        this.avnoEnable = avnoEnable;
    }

    public int getTabCallEnable() {
        return tabCallEnable;
    }

    public void setTabCallEnable(int tabCallEnable) {
        this.tabCallEnable = tabCallEnable;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<presence");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (language != null) {
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
        if (type != Type.available)
            buf.append(" type=\"").append(type).append("\"");
        if (subType != null && subType != SubType.normal)
            buf.append(" subtype=\"").append(subType).append("\"");
        // change domain
        if (domainFile != null)
            buf.append(" domain_file=\"").append(domainFile).append("\"");
        if (domainMsg != null)
            buf.append(" domain_msg=\"").append(domainMsg).append("\"");
        //state join room
        if (joinState >= 0) {
            buf.append(" joinstate=\"").append(joinState).append("\"");
        }
        buf.append(">");
        if (state != null) {
            buf.append("<show>").append(StringUtils.escapeForXML(state))
                    .append("</show>");
        }
        if (status != null) {
            buf.append("<status>").append(StringUtils.escapeForXML(status))
                    .append("</status>");
        }
        if (!TextUtils.isEmpty(lastStickyPacket)) {
            buf.append("<lst_sticky>").append(StringUtils.escapeForXML(lastStickyPacket)).append("</lst_sticky>");
        }
        if (permission != -1) {
            buf.append("<permission>").append(permission).append("</permission>");
        }
        if (userName != null) {
            buf.append("<name>").append(StringUtils.escapeForXML(userName)).append("</name>");
        }
        if (userBirthdayStr != null) {
            buf.append("<birthdayStr>").append(StringUtils.escapeForXML(userBirthdayStr)).append("</birthdayStr>");
        }
        if (timeStamp != -1) {
            buf.append("<timestamp>").append(timeStamp).append("</timestamp>");
        }
        if (nowSv != -1) {
            buf.append("<now>").append(nowSv).append("</now>");
        }
        if (priority != Integer.MIN_VALUE) {
            buf.append("<priority>").append(priority).append("</priority>");
        }
        if (mode != null && mode != Mode.available) {
            buf.append("<show>").append(mode).append("</show>");
        }
        if (follow > 0) {
            buf.append("<total>").append(follow).append("</total>");
        }
        if (!TextUtils.isEmpty(backgroundRoom)) {
            buf.append("<image_url>").append(StringUtils.escapeForXML(backgroundRoom)).append("</image_url>");
        }
        if (subType == SubType.change_avatar) {
            try {
                buf.append("<avatar>").append(Long.parseLong(lastAvatar)).append("</avatar>");
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        } else if (subType == SubType.remove_avatar) {
            buf.append("<avatar>").append("remove").append("</avatar>");
        } else if (subType == SubType.client_info) {        // add client info
            //TODO ko dùng presence này nữa
            /*buf.append("<param");
            buf.append("/>");*/
        } else if (subType == SubType.contactInfo) {
            buf.append("<contactInfo>").append(StringUtils.escapeForXML(contactInfo)).append("/>");

        } else if (subType == SubType.music_info) {
            buf.append("<room>").append(StringUtils.escapeForXML(musicInfo)).append("/>");
        } else if (subType == SubType.music_sub || subType == SubType.music_unsub) {
            buf.append("<rooms>");
            if (subscribeMusicRooms != null) {
                for (String sessionId : subscribeMusicRooms) {
                    buf.append("<room id='").append(StringUtils.escapeForXML(sessionId)).append("'/>");
                }
            }
            buf.append("</rooms>");
        }
        if (!TextUtils.isEmpty(feed)) {
            buf.append("<feed>").append(StringUtils.escapeForXML(feed)).append("</feed>");
        }
        buf.append(this.getExtensionsXML());

        // Add the error sub-packet, if there is one.
        XMPPError error = getError();
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.append("</presence>");
        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(type);
        if (mode != null) {
            buf.append(": ").append(mode);
        }
        if (getStatus() != null) {
            buf.append("status (").append(getStatus()).append(")");
        }
        buf.append("last avatar (").append(getLastAvatar()).append(")");
        if (getTimeStamp() != -1) {
            buf.append("timestamp (").append(getTimeStamp()).append(")");
        }
        return buf.toString();
    }

    public String getShow() {
        return showType;
    }

    public void setShow(String show) {
        this.showType = show;
    }

    public enum SubType {
        /**
         * subtype aviable
         */
        available,
        /**
         * subtype nguoi dung moi
         */
        new_user,
        /**
         * subtype client info
         */
        client_info,
        /**
         * subtype change status
         */
        change_status,

        /**
         * subtype change avatar
         */
        change_avatar,

        /**
         * subtype remove avatar
         */
        remove_avatar,

        /**
         * subtype offline
         */
        offline,

        /**
         * subtype deactive
         */
        deactive,

        /**
         * invisible (de an)
         */
        invisible,
        // presence thay doi domain
        change_domain,
        normal,

        /**
         * subtype last seen
         */
        background,
        foreground,
        // contact info (active,deactive,avatar,status,birthday,gender)
        contactInfo,
        //stranger music sub
        music_sub,
        music_unsub,
        music_info,
        // subscribe room chat
        star_sub,
        star_unsub,
        // so nguoi follow sao
        count_users,
        // doi hinh nen sao
        change_background,
        //onmedia
        feedInfo,
        // thay doi quyen
        change_permission,
        // cap nhat info
        updateInfo,
        //cap nhat thong tin goi sub
        package_info,
        // cap nhat tam su ng lạ
        confide_accepted,
        // cap nhat vi tri cua minh
        strangerLocation
    }

    /**
     * A enum to represent the presecence type. Not that presence type is often
     * confused with presence mode. Generally, if a user is signed into a
     * server, they have a presence type of {@link #available available}, even
     * if the mode is {@link Mode#away away}, {@link Mode#dnd dnd}, etc. The
     * presence type is only {@link #unavailable unavailable} when the user is
     * signing out of the server.
     */
    public enum Type {

        /**
         * The user is available to receive messages (default).
         */
        available,

        /**
         * The user is unavailable to receive messages.
         */
        unavailable,

        /**
         * Request subscription to recipient's presence.
         */
        subscribe,

        /**
         * Grant subscription to sender's presence.
         */
        //subscribed,

        /**
         * Request removal of subscription to sender's presence.
         */
        unsubscribe,

        /**
         * Grant removal of subscription to sender's presence.
         */
        //unsubscribed,

        /**
         * The presence packet contains an error message.
         */
        error
    }

    /**
     * An enum to represent the presence mode.
     */
    public enum Mode {

        /**
         * Free to chat.
         */
        chat,

        /**
         * Available (the default).
         */
        available,

        /**
         * Away.
         */
        away,

        /**
         * Away for an extended period of time.
         */
        xa,

        /**
         * Do not disturb.
         */
        dnd
    }
}