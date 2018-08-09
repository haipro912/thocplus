package com.vttm.chatlib.packet;

import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.vttm.chatlib.utils.ConvertHelper;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.StanzaError;
import org.jivesoftware.smack.util.StringUtils;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class ReengMessagePacket extends Stanza{

    private static final String TAG = ReengMessagePacket.class.getSimpleName();

    private static final int PIN_TYPE_DEEPLINK = 0;
    private static final int PIN_TYPE_MSGID = 1;

    private String typeString = null;
    private String subTypeString = null;
    private Type type = Type.normal;
    private SubType subType = SubType.normal;
    private String subject = null;
    private String thread = null;
    private String language;
    // content
    private boolean noStore = false;
    private String body = null;
    private String tel;
    private String videoUrl;
    private String videoThumb;
    private String avatarUrl;
    //file voice,image
    private FileType fileType = FileType.normal;
    private String fileId = null;
    private String name = null;
    private String officalName = null;
    private int size = 0;
    private int duration = 0;
    // voice sticker
    private long stickerId = -1;
    private String stickerPacket = null;
    // greeting sticker, image
    private String imageUrl;
    private String voiceUrl;
    private String jsonListSticker;
    private int cState = -1;
    // notify group
    private int groupPrivate = -1;
    private String groupAvatar;
    private int groupClass = -1;
    private String groupId;
    private String groupName;
    private String fromJid;
    private ArrayList<Member> members;
    private String sender;
    private long timeSend = -1L;
    private long timeReceive = -1L;
    private long expired = -1L;
    // attribute phuc vu ban tin lam quen
    private String external;
    private String toName;
    private String fromName;
    private String nick;
    private String appId;
    private String toAvatar;
    private String fromAvatar;
    // location
    private String lat;
    private String lng;
    // transfer money
    private String amountMoney;
    private String unitMoney;
    private String timeTransferMoney;
    private String link;
    //
    private ArrayList<String> keyConfig;
    // event room chat
    private String eventRoomId;
    private String eventRoomName;
    private String eventRoomAvatar;

    private String senderName;
    private int stickyState = -1;
    private ArrayList<String> phoneNumbers;
    private String mediaLink;
    // deep link
    private String dlLeftLabel, dlLeftAction;
    private String dlRightLabel, dlRightAction;
    private String serviceType;
    // gif
    private String gifImg;
    //fake mo
    private String label;
    private String confirm;
    // image link
    private String imageLinkUrl;
    private String imageLinkTo;

    private String reply;
    private String largeEmo = null;
    private ArrayList<AdvertiseItem> listAdvertise;
    // poll
    private String pollType;
    private String pollDetail;

    //ratio for image
    private String ratio;
    // bankPlus
    private String bPlusAmount;
    private String bPlusDesc;
    private String bPlusId;
    private String bPlusType;
    private String lixiSender;
    private String lixiReceiver;
    private String textTag;

    //lixi
    private String amountLixi;
    private String orderId;
    private int splitRandom = -1;
    private String requestIdLixi;
    private String listMemberLixiStr;

    //pin_message
    private String pinMsgTitle;
    private String pinMsgImg;
    private String pinMsgTarget;
    private int pinMsgAction = -1;
    private long pinType = -1;
    private int pinThreadType = -1;
    private long pinExpired;


    //add lastseen
    private long lastSeen = -1;

    //add virtual number
    private String avnoNumber;

    //config cho tung user
    private KeyValueConfig keyValueConfig;

    //sticker trong ban tin suggest_voice_sticker
    private String stickerData;
    private String imgCover;
    private String imgAvatar;

    protected long timeConnect;
    protected String languageCode, countryCode;

    //add lastavatar
    private String lastAvatar;

    /**
     * Creates a new, "normal" message.
     */
    public ReengMessagePacket() {
    }

    public ReengMessagePacket(String to) {
        setTo(to);
    }

    public ReengMessagePacket(String to, Type type) {
        setTo(to);
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
        this.setType(Type.fromString(typeString));
    }

    public Type getType() {
        if (type == null) {
            return Type.normal;
        }
        return type;
    }

    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    public SubType getSubType() {
        if (subType == null) {
            return SubType.normal;
        }
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public void setSubTypeString(String subTypeStr) {
        this.subTypeString = subTypeStr;
    }

    public String getSubTypeString() {
        return subTypeString;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * get, set thread id
     *
     * @return
     */
    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    /**
     * language
     *
     * @since 3.0.2
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * sender
     *
     * @return
     */
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * body message
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        //        this.body = convertUtf8ToUnicode(body);
        this.body = body;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * get set contact name and number (share contact)
     *
     * @return
     */

    public String getTel() {
        return tel;
    }

    public void setTel(String contactNumber) {
        this.tel = contactNumber;
    }

    // sharevideov2
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    // file
    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficalName() {
        return officalName;
    }

    public void setOfficalName(String officalName) {
        this.officalName = officalName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setNoStore(boolean noStore) {
        this.noStore = noStore;
    }

    public boolean isNoStore() {
        return noStore;
    }

    public long getStickerId() {
        return stickerId;
    }

    public void setStickerId(long stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerPacket() {
        return stickerPacket;
    }

    public void setStickerPacket(String stickerPacket) {
        this.stickerPacket = stickerPacket;
    }

    /**
     * notify group
     *
     * @return
     */

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromJid() {
        return fromJid;
    }

    public void setFromJid(String fromJid) {
        this.fromJid = fromJid;
    }

    public long getTimeSend() {
        return timeSend;// thoi gian send tin nhan
    }

    public void setTimeSend(String timeString) {
        if (TextUtils.isEmpty(timeString)) {
            this.timeSend = new Date().getTime();
        } else {
            try {
                this.timeSend = Long.parseLong(timeString);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Exception", e);
                this.timeSend = new Date().getTime();
            }
        }
    }

    public long getTimeReceive() {
        return timeReceive;// thoi gian sv gui tin nhan
    }

    public void setTimeReceive(String timeString) {
        if (!TextUtils.isEmpty(timeString)) {
            try {
                this.timeReceive = Long.parseLong(timeString);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Exception", e);
                this.timeReceive = -1;
            }
        }
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public int getCState() {
        return cState;
    }

    public void setCState(int cState) {
        this.cState = cState;
    }

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<Member>();
        }
        members.add(member);
    }

    public ArrayList<Member> getMembers() {
        if (members == null || members.isEmpty()) {
            return null;
        }
        HashSet<Member> memberHashSet = new HashSet<Member>(members);
        return new ArrayList<Member>(memberHashSet);
    }

    public int getGroupPrivate() {
        return groupPrivate;
    }

    public void setGroupPrivate(int groupPrivate) {
        this.groupPrivate = groupPrivate;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public int getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(int groupClass) {
        this.groupClass = groupClass;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getExternal() {
        if (!TextUtils.isEmpty(external)) {
            return external.toLowerCase();
        }
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    // location
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getJsonListSticker() {
        return jsonListSticker;
    }

    public void setJsonListSticker(String jsonListSticker) {
        this.jsonListSticker = jsonListSticker;
    }

    public String getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(String amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(String unitMoney) {
        this.unitMoney = unitMoney;
    }

    public String getTimeTransferMoney() {
        return timeTransferMoney;
    }

    public void setTimeTransferMoney(String timeTransferMoney) {
        this.timeTransferMoney = timeTransferMoney;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<String> getKeyConfig() {
        return keyConfig;
    }

    public void addKeyConfig(String key) {
        if (keyConfig == null) {
            keyConfig = new ArrayList<String>();
        }
        keyConfig.add(key);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getStickyState() {
        return stickyState;
    }

    public void setStickyState(String stickyStateStr) {
        this.stickyState = ConvertHelper.parserIntFromString(stickyStateStr, -1);
    }

    /**
     * event follow room
     *
     * @return
     */
    public String getEventRoomId() {
        return eventRoomId;
    }

    public void setEventRoomId(String eventRoomId) {
        this.eventRoomId = eventRoomId;
    }

    public String getEventRoomName() {
        return eventRoomName;
    }

    public void setEventRoomName(String eventRoomName) {
        this.eventRoomName = eventRoomName;
    }

    public String getEventRoomAvatar() {
        return eventRoomAvatar;
    }

    public void setEventRoomAvatar(String eventRoomAvatar) {
        this.eventRoomAvatar = eventRoomAvatar;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getPhoneNumbersToXML() {
        String str = "";
        if (phoneNumbers != null && phoneNumbers.size() > 0) {
            StringBuilder buf = new StringBuilder();
            buf.append("<members>");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                buf.append("<member>").append(StringUtils.escapeForXml(phoneNumbers.get(i)))
                        .append("</member>");
            }
            buf.append(getExtensionsXML());
            buf.append("</members>");
            return buf.toString();
        }
        return str;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getDlLeftLabel() {
        return dlLeftLabel;
    }

    public void setDlLeftLabel(String dlLeftLabel) {
        this.dlLeftLabel = dlLeftLabel;
    }

    public String getDlLeftAction() {
        return dlLeftAction;
    }

    public void setDlLeftAction(String dlLeftAction) {
        this.dlLeftAction = dlLeftAction;
    }

    public String getDlRightLabel() {
        return dlRightLabel;
    }

    public void setDlRightLabel(String dlRightLabel) {
        this.dlRightLabel = dlRightLabel;
    }

    public String getDlRightAction() {
        return dlRightAction;
    }

    public void setDlRightAction(String dlRightAction) {
        this.dlRightAction = dlRightAction;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getGifImg() {
        return gifImg;
    }

    public void setGifImg(String gifImg) {
        this.gifImg = gifImg;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getImageLinkUrl() {
        return imageLinkUrl;
    }

    public void setImageLinkUrl(String imageLinkUrl) {
        this.imageLinkUrl = imageLinkUrl;
    }

    public String getImageLinkTo() {
        return imageLinkTo;
    }

    public void setImageLinkTo(String imageLinkTo) {
        this.imageLinkTo = imageLinkTo;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getLargeEmo() {
        return largeEmo;
    }

    public void setLargeEmo(String largeEmo) {
        this.largeEmo = largeEmo;
    }

    public ArrayList<AdvertiseItem> getListAdvertise() {
        return listAdvertise;
    }

    public void addAdvertiseItem(AdvertiseItem advertiseItem) {
        if (listAdvertise == null) listAdvertise = new ArrayList<>();
        listAdvertise.add(advertiseItem);
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getPollDetail() {
        return pollDetail;
    }

    public void setPollDetail(String pollDetail) {
        this.pollDetail = pollDetail;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getBPlusAmount() {
        return bPlusAmount;
    }

    public void setBPlusAmount(String bPlusAmount) {
        this.bPlusAmount = bPlusAmount;
    }

    public String getBPlusDesc() {
        return bPlusDesc;
    }

    public void setBPlusDesc(String bPlusDesc) {
        this.bPlusDesc = bPlusDesc;
    }

    public String getBPlusId() {
        return bPlusId;
    }

    public void setBPlusId(String bPlusId) {
        this.bPlusId = bPlusId;
    }

    public String getBPlusType() {
        return bPlusType;
    }

    public void setBPlusType(String bPlusType) {
        this.bPlusType = bPlusType;
    }

    public String getLixiSender() {
        return lixiSender;
    }

    public void setLixiSender(String lixiSender) {
        this.lixiSender = lixiSender;
    }

    public String getLixiReceiver() {
        return lixiReceiver;
    }

    public void setLixiReceiver(String lixiReceiver) {
        this.lixiReceiver = lixiReceiver;
    }

    public String getAmountLixi() {
        return amountLixi;
    }

    public void setAmountLixi(String amountLixi) {
        this.amountLixi = amountLixi;
    }

    public String getRequestIdLixi() {
        return requestIdLixi;
    }

    public void setRequestIdLixi(String requestIdLixi) {
        this.requestIdLixi = requestIdLixi;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getSplitRandom() {
        return splitRandom;
    }

    public void setSplitRandom(int splitRandom) {
        this.splitRandom = splitRandom;
    }

    public String getListMemberLixiStr() {
        return listMemberLixiStr;
    }

    public void setListMemberLixiStr(String listMemberLixiStr) {
        this.listMemberLixiStr = listMemberLixiStr;
    }

    public String getTextTag() {
        return textTag;
    }

    public void setTextTag(String textTag) {
        this.textTag = textTag;
    }


    public void setPinMsgTitle(String pinMsgTitle) {
        this.pinMsgTitle = pinMsgTitle;
    }

    public void setPinType(long pinType) {
        this.pinType = pinType;
    }

    public void setPinMsgTarget(String pinMsgTarget) {
        this.pinMsgTarget = pinMsgTarget;
    }

    public void setPinMsgAction(int pinMsgAction) {
        this.pinMsgAction = pinMsgAction;
    }

    public void setPinMsgImg(String pinMsgImg) {
        this.pinMsgImg = pinMsgImg;
    }


    public String getPinMsgTitle() {
        return pinMsgTitle;
    }

    public String getPinMsgImg() {
        return pinMsgImg;
    }

    public int getPinMsgAction() {
        return pinMsgAction;
    }

    public String getPinMsgTarget() {
        return pinMsgTarget;
    }

    public long getPinType() {
        return pinType;
    }

    public int getPinThreadType() {
        return pinThreadType;
    }

    public void setPinThreadType(int pinThreadType) {
        this.pinThreadType = pinThreadType;
    }

    public long getPinExpired() {
        return pinExpired;
    }

    public void setPinExpired(long pinExpired) {
        this.pinExpired = pinExpired;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public KeyValueConfig getKeyValueConfig() {
        return keyValueConfig;
    }

    public void setKeyValueConfig(KeyValueConfig keyValueConfig) {
        this.keyValueConfig = keyValueConfig;
    }

    public void setAvnoNumber(String avnoNumber) {
        this.avnoNumber = avnoNumber;
    }

    public String getAvnoNumber() {
        return avnoNumber;
    }

    public String getStickerData() {
        return stickerData;
    }

    public void setStickerData(String stickerData) {
        this.stickerData = stickerData;
    }

    public String getImgCover() {
        return imgCover;
    }

    public void setImgCover(String imgCover) {
        this.imgCover = imgCover;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public void setTimeConnect(long timeConnect) {
        this.timeConnect = timeConnect;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLastAvatar() {
        return lastAvatar;
    }

    public void setLastAvatar(String lastAvatar) {
        this.lastAvatar = lastAvatar;
    }

    //==================object to xml string=======================================

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
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
            buf.append(" to=\"").append(StringUtils.escapeForXml(getTo()))
                    .append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXml(getFrom()))
                    .append("\"");
        }
        if (type != Type.normal) {
            buf.append(" type=\"").append(type).append("\"");
        } else if (typeString != null) {
            buf.append(" type=\"").append(typeString).append("\"");
        }
        if (stickyState != -1) {
            buf.append(" is_sticky=\"").append(stickyState).append("\"");
        }
        if (subType != SubType.normal) {
            buf.append(" subtype=\"").append(subType).append("\"");
        } else if (subTypeString != null) {
            buf.append(" subtype=\"").append(subTypeString).append("\"");
        }
        if (!TextUtils.isEmpty(external)) {
            buf.append(" external=\"").append(StringUtils.escapeForXml(external)).append("\"");
        }
        if (getSender() != null) {
            buf.append(" member=\"").append(getSender()).append("\"");
        }
        if (senderName != null) {
            buf.append(" name=\"").append(StringUtils.escapeForXml(senderName)).append("\"");
        }
        if(!TextUtils.isEmpty(lastAvatar)){
            buf.append(" lastavatar=\"").append(StringUtils.escapeForXml(lastAvatar)).append("\"");
        }
        if (timeSend != -1L) {
            buf.append(" timesend=\"").append(timeSend).append("\"");
        }
        //send lastseen
        if(lastSeen !=-1){
            buf.append(" lastseen=\"").append(lastSeen).append("\"");
        }
        if (!TextUtils.isEmpty(getAvnoNumber())) {
            buf.append(" virtual=\"").append(getAvnoNumber()).append("\"");
        }
        // buf.append(" expired=\"").append(System.currentTimeMillis() + 10000).append("\"");    //TODO test expired
        buf.append(">");
        if (subject != null) {
            buf.append("<subject>").append(StringUtils.escapeForXml(subject)).append("</subject>");
        }
        //add thread id
        if (thread != null) {
            buf.append("<thread>").append(thread).append("</thread>");
        }
        // Add the body in the default language
        if (noStore) {
            buf.append("<no_store/>");
        }
        /*if (groupClass != -1) {
            buf.append("<gtype>").append(groupClass).append("</gtype>");
        }*/
        if (cState != -1) {
            buf.append("<cstate>").append(cState).append("</cstate>");
        }
        if (body != null) {
            buf.append("<body");
            if (!TextUtils.isEmpty(largeEmo)) {
                buf.append(" emoticon=\"").append(largeEmo).append("\"");
            }
            buf.append(">").append(StringUtils.escapeForXml(getBody()))
                    .append("</body>");
        }
        if (!TextUtils.isEmpty(getPhoneNumbersToXML())) {
            buf.append(getPhoneNumbersToXML());
        }
        if (avatarUrl != null) {
            buf.append("<avatar>").append(StringUtils.escapeForXml(avatarUrl))
                    .append("</avatar>");
        }
        //add contact element
        if (tel != null && tel.length() > 0) {
            buf.append("<tel>").append(StringUtils.escapeForXml(tel)).append("</tel>");
        }
        if (!TextUtils.isEmpty(name)) {
            buf.append("<name>").append(StringUtils.escapeForXml(name)).append("</name>");
        }
        if (!TextUtils.isEmpty(officalName)) {
            buf.append("<officalname>").append(StringUtils.escapeForXml(officalName)).append("</officalname>");
        }
        if (!TextUtils.isEmpty(fromName)) {
            buf.append("<from_name>").append(StringUtils.escapeForXml(fromName)).append("</from_name>");
        }
        if (!TextUtils.isEmpty(toName)) {
            buf.append("<to_name>").append(StringUtils.escapeForXml(toName)).append("</to_name>");
        }
        if (!TextUtils.isEmpty(nick)) {
            buf.append("<nick>").append(StringUtils.escapeForXml(nick)).append("</nick>");
        }
        if (!TextUtils.isEmpty(toAvatar)) {
            buf.append("<to_avatar>").append(StringUtils.escapeForXml(toAvatar)).append("</to_avatar>");
        }
        if (!TextUtils.isEmpty(fromAvatar)) {
            buf.append("<from_avatar>").append(StringUtils.escapeForXml(fromAvatar)).append("</from_avatar>");
        }
        if (!TextUtils.isEmpty(appId)) {
            buf.append("<app_id>").append(StringUtils.escapeForXml(appId)).append("</app_id>");
        }
        // add sharevideov2 message
        if (videoUrl != null) {
            buf.append("<sharevideov2>").append(StringUtils.escapeForXml(videoUrl)).append("/>");
        }
        if (videoThumb != null) {
            buf.append("<thumb>").append(StringUtils.escapeForXml(videoThumb)).append("</thumb>");
        }
        if (mediaLink != null) {
            buf.append("<media_link>").append(StringUtils.escapeForXml(mediaLink)).append("</media_link>");
        }
        // Add file message
        if (fileType != FileType.normal) {
            buf.append("<type>").append(fileType).append("</type");
        }
        if (fileId != null) {
            buf.append("<id>").append(fileId).append("</id>");
        }
        // Add duration
        if (duration > 0) {
            buf.append("<duration>").append(String.valueOf(duration)).append("</duration>");
        }
        // Add size
        if (size > 0) {
            buf.append("<size>").append(String.valueOf(size)).append("</size>");
        }
        // add sticker voice
        if (stickerId != -1) {
            buf.append("<itemid>").append(String.valueOf(stickerId)).append("</itemid>");
        }
        if (stickerPacket != null) {
            buf.append("<packageid>").append(stickerPacket).append("</packageid>");
        }
        // add share location
        if (!TextUtils.isEmpty(lat)) {
            buf.append("<lat>").append(lat).append("</lat>");
        }
        if (!TextUtils.isEmpty(lng)) {
            buf.append("<lng>").append(lng).append("</lng>");
        }
        if (!TextUtils.isEmpty(amountMoney)) {
            buf.append("<amount_money>").append(StringUtils.escapeForXml(amountMoney)).append("</amount_money>");
        }
        if (!TextUtils.isEmpty(unitMoney)) {
            buf.append("<unit_money>").append(StringUtils.escapeForXml(unitMoney)).append("</unit_money>");
        }
        if (!TextUtils.isEmpty(timeTransferMoney)) {
            buf.append("<time_transfer>").append(timeTransferMoney).append("</time_transfer>");
        }
        // event room
        if (!TextUtils.isEmpty(eventRoomId)) {
            buf.append("<star_id>").append(eventRoomId).append("</star_id>");
        }
        if (!TextUtils.isEmpty(eventRoomName)) {
            buf.append("<star_name>").append(eventRoomName).append("</star_name>");
        }
        if (!TextUtils.isEmpty(eventRoomAvatar)) {
            buf.append("<star_avatar>").append(eventRoomAvatar).append("</star_avatar>");
        }
        if (!TextUtils.isEmpty(serviceType)) {
            buf.append("<servicetype>").append(eventRoomAvatar).append("</servicetype>");
        }
        if (!TextUtils.isEmpty(reply)) {
            buf.append("<reply>").append(StringUtils.escapeForXml(reply)).append("</reply>");
        }
        // Add room
        if (groupName != null) {
            buf.append("<room").append(" name=\"").append(StringUtils.escapeForXml(groupName)).append("\"");
            if (groupId != null && groupId.length() > 0) {
                buf.append(" jid=\"").append(StringUtils.escapeForXml(groupId)).append("\"");
            }
            if (fromJid != null && fromJid.length() > 0) {
                buf.append(" fromJid=\"").append(fromJid).append("\"");
            }
            buf.append("/>");
        }
        // Add members
        if (members != null) {
            for (int i = 0; i < members.size(); i++) {
                buf.append(members.get(i));
            }
        }
        // group private
        if (groupPrivate != -1) {
            buf.append("<keep_private value=\'").append(groupPrivate).append("\'/>");
        }

        if (!TextUtils.isEmpty(ratio)) {
            buf.append("<ratio>").append(ratio).append("</ratio>");
        }
        // bankplus
        if (!TextUtils.isEmpty(bPlusAmount)) {
            buf.append("<bplus_amount>").append(StringUtils.escapeForXml(bPlusAmount)).append("</bplus_amount>");
        }
        if (!TextUtils.isEmpty(bPlusDesc)) {
            buf.append("<bplus_desc>").append(StringUtils.escapeForXml(bPlusDesc)).append("</bplus_desc>");
        }
        if (!TextUtils.isEmpty(bPlusId)) {
            buf.append("<bplus_id>").append(StringUtils.escapeForXml(bPlusId)).append("</bplus_id>");
        }
        if (!TextUtils.isEmpty(bPlusType)) {
            buf.append("<bplus_type>").append(StringUtils.escapeForXml(bPlusType)).append("</bplus_type>");
        }

        //lixi
        if (!TextUtils.isEmpty(amountLixi)) {
            buf.append("<amount>").append(StringUtils.escapeForXml(amountLixi)).append("</amount>");
        }
        if (splitRandom != -1) {
            buf.append("<randommoney>").append(splitRandom).append("</randommoney>");
        }
        if (!TextUtils.isEmpty(orderId)) {
            buf.append("<orderid>").append(StringUtils.escapeForXml(orderId)).append("</orderid>");
        }
        if (!TextUtils.isEmpty(requestIdLixi)) {
            buf.append("<requestid>").append(StringUtils.escapeForXml(requestIdLixi)).append("</requestid>");
        }
        if (!TextUtils.isEmpty(listMemberLixiStr)) {
            buf.append("<members>").append(StringUtils.escapeForXml(listMemberLixiStr)).append("</members>");
        }

        if (!TextUtils.isEmpty(textTag)) {
            buf.append("<tag><![CDATA[").append(textTag).append("]]></tag>");
        }
        if (keyConfig != null && !keyConfig.isEmpty()) {
            for (String mKey : keyConfig) {
                buf.append("<key>").append(mKey).append("</key>");
            }
        }
        if (listAdvertise != null && !listAdvertise.isEmpty()) {
            for (AdvertiseItem advertiseItem : listAdvertise) {
                buf.append(advertiseItem.toXML());
            }
        }

        if (pinMsgAction != -1) {
            buf.append("<pin").append(" action=\"").append(pinMsgAction).append("\"");
            if (pinType != -1) {
                buf.append(" type=\"").append(pinType).append("\"");
                buf.append(" target=\"").append(pinMsgTarget).append("\"");
            }
            if (!TextUtils.isEmpty(pinMsgTitle)) {
                buf.append(" title=\"").append(StringUtils.escapeForXml(pinMsgTitle)).append("\"");
            }
            if (!TextUtils.isEmpty(pinMsgImg)) {
                buf.append(" img=\"").append(StringUtils.escapeForXml(pinMsgImg)).append("\"");
            }
            buf.append("/>");
        }

        // Append the error subpacket if the message type is an error.
        if (type == Type.error) {
            StanzaError error = getError();
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    @Override
    public String toString() {
        return "ReengMessagePacket{" +
                "typeString='" + typeString + '\'' +
                ", subTypeString='" + subTypeString + '\'' +
                ", type=" + type +
                ", subType=" + subType +
                ", subject='" + subject + '\'' +
                ", thread='" + thread + '\'' +
                ", language='" + language + '\'' +
                ", noStore=" + noStore +
                ", body='" + body + '\'' +
                ", tel='" + tel + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoThumb='" + videoThumb + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", fileType=" + fileType +
                ", fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                ", officalName='" + officalName + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", stickerId=" + stickerId +
                ", stickerPacket='" + stickerPacket + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", jsonListSticker='" + jsonListSticker + '\'' +
                ", cState=" + cState +
                ", groupPrivate=" + groupPrivate +
                ", groupAvatar='" + groupAvatar + '\'' +
                ", groupClass=" + groupClass +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", fromJid='" + fromJid + '\'' +
                ", members=" + members +
                ", sender='" + sender + '\'' +
                ", timeSend=" + timeSend +
                ", timeReceive=" + timeReceive +
                ", expired=" + expired +
                ", external='" + external + '\'' +
                ", toName='" + toName + '\'' +
                ", fromName='" + fromName + '\'' +
                ", nick='" + nick + '\'' +
                ", appId='" + appId + '\'' +
                ", toAvatar='" + toAvatar + '\'' +
                ", fromAvatar='" + fromAvatar + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", amountMoney='" + amountMoney + '\'' +
                ", unitMoney='" + unitMoney + '\'' +
                ", timeTransferMoney='" + timeTransferMoney + '\'' +
                ", link='" + link + '\'' +
                ", keyConfig=" + keyConfig +
                ", eventRoomId='" + eventRoomId + '\'' +
                ", eventRoomName='" + eventRoomName + '\'' +
                ", eventRoomAvatar='" + eventRoomAvatar + '\'' +
                ", senderName='" + senderName + '\'' +
                ", stickyState=" + stickyState +
                ", phoneNumbers=" + phoneNumbers +
                ", mediaLink='" + mediaLink + '\'' +
                ", dlLeftLabel='" + dlLeftLabel + '\'' +
                ", dlLeftAction='" + dlLeftAction + '\'' +
                ", dlRightLabel='" + dlRightLabel + '\'' +
                ", dlRightAction='" + dlRightAction + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", gifImg='" + gifImg + '\'' +
                ", label='" + label + '\'' +
                ", confirm='" + confirm + '\'' +
                ", imageLinkUrl='" + imageLinkUrl + '\'' +
                ", imageLinkTo='" + imageLinkTo + '\'' +
                ", reply='" + reply + '\'' +
                ", largeEmo='" + largeEmo + '\'' +
                ", listAdvertise=" + listAdvertise +
                ", pollType='" + pollType + '\'' +
                ", pollDetail='" + pollDetail + '\'' +
                ", ratio='" + ratio + '\'' +
                ", bPlusAmount='" + bPlusAmount + '\'' +
                ", bPlusDesc='" + bPlusDesc + '\'' +
                ", bPlusId='" + bPlusId + '\'' +
                ", bPlusType='" + bPlusType + '\'' +
                ", lixiSender='" + lixiSender + '\'' +
                ", lixiReceiver='" + lixiReceiver + '\'' +
                ", textTag='" + textTag + '\'' +
                ", amountLixi='" + amountLixi + '\'' +
                ", orderId='" + orderId + '\'' +
                ", splitRandom=" + splitRandom +
                ", requestIdLixi='" + requestIdLixi + '\'' +
                ", listMemberLixiStr='" + listMemberLixiStr + '\'' +
                ", pinMsgTitle='" + pinMsgTitle + '\'' +
                ", pinMsgImg='" + pinMsgImg + '\'' +
                ", pinMsgTarget='" + pinMsgTarget + '\'' +
                ", pinMsgAction=" + pinMsgAction +
                ", pinType=" + pinType +
                ", pinThreadType=" + pinThreadType +
                ", pinExpired=" + pinExpired +
                ", lastSeen=" + lastSeen +
                ", avnoNumber='" + avnoNumber + '\'' +
                ", keyValueConfig=" + keyValueConfig +
                ", stickerData='" + stickerData + '\'' +
                ", imgCover='" + imgCover + '\'' +
                ", imgAvatar='" + imgAvatar + '\'' +
                ", timeConnect=" + timeConnect +
                ", languageCode='" + languageCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", lastAvatar='" + lastAvatar + '\'' +
                '}';
    }

    //====================end object to xml string==============================

    /**
     * Represents the type of a message.
     */
    public enum Type {

        /**
         * (Default) a normal text message used in email like interface.
         */
        normal,

        /**
         * Typically short text message used in line-by-line chat interfaces.
         */
        chat,

        /**
         * Chat message sent to a groupchat server for group chats.
         */
        groupchat,
        /**
         * offical message
         */
        offical,
        /**
         * room chat
         */
        roomchat,
        /**
         * Text message to be displayed in scrolling marquee displays.
         */
        headline,

        /**
         * indicates a messaging error.
         */
        error,
        /**
         * event message
         */
        event;

        private static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                Log.e("MessagePacket", "Exception", e);
                return normal;
            }
        }
    }

    /**
     * Represents the type of a message.
     */
    public enum SubType {
        empty,// khong co subtype
        /**
         * (Default) a normal (subtype chua duoc dinh nghia)
         */
        normal,
        /**
         * message text
         */
        text,
        /**
         * message image
         */
        image,
        /**
         * message file
         */
        file_2,
        /**
         * message voice
         */
        voicemail,
        /**
         * message share contact
         */
        contact,
        /**
         * message share sharevideov2
         */
        sharevideov2,
        // voice sticker
        voicesticker,
        //thiep sinh nhat
        greeting_voicesticker,
        //thu hoi tin nhan
        restore,
        watch_video,
        /**
         * message notification group chat
         */
        create,
        invite,
        join,
        rename,
        leave,
        kick,
        makeAdmin,
        groupPrivate,
        groupAvatar,
        /**
         * subtype dung trong cac ban tin bao hieu
         */
        event, sms_out, sms_in, no_route, typing, update, upgrade,
        config, event_expired, star_unfollow, notification_image,
        inbox_banner, sticky_banner,

        /**
         * subtype dung trong cac ban tin cung nghe v2
         *
         * @param name
         * @return //music_stranger_accept//music_stranger_reinvite
         */
        music_invite, music_action, music_leave, music_action_response, music_ping, music_pong, music_request_change,
        // voi nguoi la
        music_stranger_accept, music_stranger_reinvite,
        //accept room
        music_accept,
        // tang nhac cho
        crbt_gift,
        /**
         * moi ket ban
         */
        invite_friend,
        /**
         * moi ket ban thanh cong
         */
        invite_success,
        /**
         * chia se vi tri
         */
        location,
        /**
         * chuyen tien
         */
        transfer_money,
        /**
         * event room chat
         */
        event_sticky,
        // thong bao tu sv
        toast,
        notification,
        // deep link
        deeplink,
        // gif play full
        gift,
        // fake dang ky mo
        fake_mo,
        // fake mo dang notification
        notification_fake_mo,
        // xem ảnh full
        image_link,
        // quang cao nhu zalo
        advertise,
        // poll
        vote,
        //luckywheel_help
        luckywheel_help,
        //call
        call,
        //call rtc
        call_rtc,
        //call rtc (v2)
        call_rtc_2,
        // call out
        call_out,
        //bank_plus
        bank_plus,
        //
        lixi_notification,
        //li xi
        lixi,
        // call in
        call_in,
        //pin message
        pin_msg,
        //delconfig
        delconfig,
        //setconfig
        setconfig,
        //
        suggest_voicesticker;

        public static SubType fromString(String name) {
            if (name == null || name.length() <= 0) {
                return empty;
            }
            try {
                return SubType.valueOf(name);
            } catch (Exception e) {
                Log.e("MessagePacket", "Exception", e);
                return normal;
            }
        }

        public static boolean containsEvent(SubType subType) {
            return subType == event || subType == sms_out ||
                    subType == sms_in || subType == no_route ||
                    subType == typing || subType == update ||
                    subType == upgrade || subType == event_expired ||
                    subType == star_unfollow || subType == notification_image ||
                    subType == inbox_banner;
        }

        public static boolean containsMusic(SubType subType) {
            return subType == music_invite || subType == music_action ||
                    subType == music_leave || subType == music_action_response ||
                    subType == music_ping || subType == music_pong ||
                    subType == music_stranger_accept || subType == music_stranger_reinvite ||
                    subType == music_accept || subType == crbt_gift ||
                    subType == music_request_change;
        }

        public static boolean containsConfigGroup(SubType subType) {
            return subType == create || subType == invite ||
                    subType == join || subType == rename ||
                    subType == leave || subType == kick ||
                    subType == makeAdmin || subType == groupPrivate ||
                    subType == groupAvatar;
        }

        public static boolean containsCall(SubType subType) {
            return subType == call || subType == call_rtc || subType == call_rtc_2;
        }

        public static boolean containsCallOut(SubType subType) {
            return subType == call_out;
        }

        public static boolean containsCallIn(SubType subType) {
            return subType == call_in;
        }
    }

    public enum FileType {
        voicemail, image, sharevideo, normal, gif;

        public static FileType fromString(String name) {
            try {
                return FileType.valueOf(name);
            } catch (Exception e) {
                Log.e("MessagePacket", "Exception", e);
                return normal;
            }
        }
    }
}
