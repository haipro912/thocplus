package com.vttm.mochaplus.feature.helper;

import android.text.TextUtils;

import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.mochaplus.feature.utils.AppConstants;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class PacketMessageId {
    private static PacketMessageId mInstance;
    private Pattern pattern = Pattern.compile("-");
    private HashMap<String, String> mHashMapSubTypeKeys = new HashMap<>();
    private HashMap<String, String> mHashMapMessageTypeKeys = new HashMap<>();
    private HashMap<String, String> mHashMapThreadTypeKeys = new HashMap<>();

    public static synchronized PacketMessageId getInstance() {
        if (mInstance == null) {
            mInstance = new PacketMessageId();
        }
        return mInstance;
    }

    public PacketMessageId() {
        initKeys();
    }

    // chuan hoa msgId
    private void initKeys() {
        mHashMapSubTypeKeys = new HashMap<>();
        for (String subtype : SUBTYPE_KEYS) {
            String[] data = subtype.split(";");
            mHashMapSubTypeKeys.put(data[0], data[1]);
        }
        //message type
        mHashMapMessageTypeKeys = new HashMap<>();
        for (String subtype : MESSAGE_TYPE_KEYS) {
            String[] data = subtype.split(";");
            mHashMapMessageTypeKeys.put(data[0], data[1]);
        }
        // key type message
        mHashMapThreadTypeKeys = new HashMap<>();
        for (String type : TYPE_KEYS) {
            String[] data = type.split(";");
            mHashMapThreadTypeKeys.put(data[0], data[1]);
        }
    }

    private String genUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String content = uuid.toString();
        content = pattern.matcher(content).replaceAll("");
        return content.toUpperCase();
    }

    public String genPacketId(int threadType, String subType) {
        // get prefix
        StringBuilder sb = new StringBuilder("01");// android
        //type
        switch (threadType) {
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT:
                sb.append("01");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT:
                sb.append("02");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT:
                sb.append("03");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT:
                sb.append("04");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_BROADCAST_CHAT:
                sb.append("05");
                break;
            default:
                sb.append("00");// unknown
                break;
        }
        // subtype
        if (!TextUtils.isEmpty(subType) && mHashMapSubTypeKeys.containsKey(subType)) {
            sb.append(mHashMapSubTypeKeys.get(subType));
        } else {
            sb.append("000");// unknown
        }
        // append UUID
        sb.append("_");
        sb.append(genUUIDCode());
        return sb.toString();
    }

    public String genPacketId(String type, String subType) {
        // get prefix
        StringBuilder sb = new StringBuilder("01");// android
        //type
        if (!TextUtils.isEmpty(type) && mHashMapThreadTypeKeys.containsKey(type)) {
            sb.append(mHashMapThreadTypeKeys.get(type));
        } else {
            sb.append("00");// unknown
        }
        // subtype
        if (!TextUtils.isEmpty(subType) && mHashMapSubTypeKeys.containsKey(subType)) {
            sb.append(mHashMapSubTypeKeys.get(subType));
        } else {
            sb.append("000");// unknown
        }
        // append UUID
        sb.append("_");
        sb.append(genUUIDCode());
        return sb.toString();
    }

    public String genShareMusicPacketId(int threadType, ReengMessagePacket.SubType subType) {
        // get prefix
        StringBuilder sb = new StringBuilder("01");// android
        //type
        switch (threadType) {
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT:
                sb.append("01");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT:
                sb.append("02");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT:
                sb.append("03");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT:
                sb.append("04");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_BROADCAST_CHAT:
                sb.append("05");
                break;
            default:
                sb.append("00");// unknown
                break;
        }
        if (subType == null) {
            sb.append("000");// unknown
        } else if (mHashMapSubTypeKeys.containsKey(subType.toString())) {
            sb.append(mHashMapSubTypeKeys.get(subType.toString()));
        } else {
            sb.append("000");// unknown
        }
        // append UUID
        sb.append("_");
        sb.append(genUUIDCode());
        return sb.toString();
    }

    public String genMessagePacketId(int threadType, String messageType) {
        // get prefix
        StringBuilder sb = new StringBuilder("01");// android
        //type
        switch (threadType) {
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT:
                sb.append("01");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT:
                sb.append("02");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT:
                sb.append("03");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT:
                sb.append("04");
                break;
            case AppConstants.ThreadMessageConstant.TYPE_THREAD_BROADCAST_CHAT:
                sb.append("05");
                break;
            default:
                sb.append("00");// unknown
                break;
        }
        // subtype
        if (!TextUtils.isEmpty(messageType) && mHashMapMessageTypeKeys.containsKey(messageType)) {
            sb.append(mHashMapMessageTypeKeys.get(messageType));
        } else {
            sb.append("000");// unknown
        }
        // append UUID
        sb.append("_");
        sb.append(genUUIDCode());
        return sb.toString();
    }

    public String genMessagePacketIdDefault() {
        return genPacketId(null, null);
    }

    private static final String[] SUBTYPE_KEYS = {
            "text;001",
            "image;002",
            "file_2;003",
            "voicemail;004",
            "contact;005",
            "sharevideov2;006",
            "voicesticker;007",
            "location;008",
            "greeting_voicesticker;009",
            "restore;010",
            "no_route;011",
            "typing;012",
            "event;013",
            "music_pong;014",
            "music_action;015",
            "available;016",
            "music_sub;017",
            "foreground;018",
            "background;019",
            "star_unsub;020",
            "create;021",
            "invite;022",
            "join;023",
            "rename;024",
            "leave;025",
            "kick;026",
            "makeAdmin;027",
            "sms_out;028",
            "sms_in;029",
            "update;030",
            "upgrade;031",
            "config;032",
            "music_stranger_accept;032",
            "music_stranger_reinvite;034",
            "room music_accept;036",
            "invite_friend;037",
            "invite_success;038",
            "transfer_money;039",
            "event_sticky;040",
            "diemthi_chiase;042",
            "music_invite;043",
            "music_leave;044",
            "music_ping;045",
            "music_action_response;046",
            "crbt_gift;047",
            "music_request_change;048",
            "call;050",
            "call_rtc;051",
            "call_rtc_2;052",
            "watch_video;053",
            "call_out;054",
            "bank_plus;055",
            "lixi;056",
            "call_in;057"
    };

    private static final String[] MESSAGE_TYPE_KEYS = {
            "text;001",
            "image;002",
            "file;003",
            "voicemail;004",
            "shareContact;005",
            "shareVideo;006",
            "voiceSticker;007",
            "shareLocation;008",
            "greeting_voicesticker;009",
            "restore;010",
            "transferMoney;039",
            "event_follow_room;040",
            "diemthi_chiase;042",
            // cung nghe
            "suggestShareMusic;043",
            "inviteShareMusic;043",     // 2 cai cung subtype
            "call;052",
            "watch_video;053",
            "bank_plus;055",
            "lixi;056",
            "notification;000",
    };

    private static final String[] TYPE_KEYS = {
            "chat;01",
            "groupchat;02",
            "offical;03",
            "roomchat;04",
            "available;06",
            "unavailable;07",
            "subscribe;08",
            "unsubscribe;09"
    };
}
