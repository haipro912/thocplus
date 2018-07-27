package com.vttm.mochaplus.feature.data.db.model;

import android.util.Log;

import com.vttm.mochaplus.feature.R;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "message", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ReengMessageConstant extends RealmObject {
    private static final String TAG = ReengMessageConstant.class.getSimpleName();

    @PrimaryKey
    public int id;
    public String content;
    @Index
    public int thread_id;
    public int is_read;
    public String sender;
    public String receiver;
    public String time;
    public int is_send;
    public String file_type;
    public String packet_id;
    public int status;
    public int duration;
    public String file_path;
    public int server_file_id;
    public String type;
    public String file_name;
    public int chat_mode;
    public int file_size;
    public String delivered_members;
    public String MESSAGE_DIRECTION;
    public int MESSAGE_MUSIC_STATE;
    public String IMAGE_URL;
    public String DIRECT_LINK_MEDIA;
    public String ROOM_INFO;
    public String MESSAGE_FILE_ID_NEW;
    public String MESSAGE_REPLY_DETAIL;
    public int MESSAGE_EXPIRED;
    public String TAG_CONTENT;

    /**
     * Represents the type of a message.
     */
    public enum MessageType {
        text, file, image, voicemail, notification,
        shareContact, shareVideo, voiceSticker,
        greeting_voicesticker,
        shareLocation, restore,
        watch_video,

        /**
         * goi y cung nghe
         */
        suggestShareMusic,
        /**
         * moi tham gia nghe chung
         */
        inviteShareMusic,
        /**
         * loai message chuyen bai, doi nbai, them bai ...
         */
        actionShareMusic,
        /**
         * chuyen tien
         */
        transferMoney,
        /**
         * event star room
         */
        event_follow_room,
        crbt_gift,
        deep_link,
        gift,
        fake_mo,
        notification_fake_mo,
        image_link,
        advertise,
        poll_create,
        poll_action,
        call,
        talk_stranger,
        luckywheel_help,
        bank_plus,
        lixi,
        message_banner,
        pin_message,
        suggest_voice_sticker,
        update_app;

        public static MessageType fromString(String name) {
            try {
                return MessageType.valueOf(name);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
                return text;
            }
        }

        public static boolean containsNormal(MessageType type) {// tin nhan thuong
            return type == text || type == shareContact || type == transferMoney ||
                    type == voiceSticker || type == shareLocation || type == gift ||
                    type == image_link;
        }

        public static boolean containsFile(MessageType type) {// tin nhan file
            return type == file || type == image ||
                    type == voicemail || type == shareVideo;
        }

        public static boolean containsNotification(MessageType type) {
            return type == notification || type == notification_fake_mo ||
                    type == poll_action || type == pin_message;
        }

        public static int toStringResource(MessageType messageType) {
            int stringResourceId;
            switch (messageType) {
                case text:
                    stringResourceId = R.string.message_text;
                    break;
                case image:
                    stringResourceId = R.string.message_image;
                    break;
                case voicemail:
                    stringResourceId = R.string.message_voice_mail;
                    break;
                case voiceSticker:
                    stringResourceId = R.string.message_voice_sticker;
                    break;
                case shareContact:
                    stringResourceId = R.string.message_share_contact;
                    break;
                case shareVideo:
                    stringResourceId = R.string.message_share_video;
                    break;
                case shareLocation:
                    stringResourceId = R.string.message_share_location;
                    break;
                case transferMoney:
                    stringResourceId = R.string.message_transfer_money;
                    break;
                case event_follow_room:
                    stringResourceId = R.string.message_event_follow_room;
                    break;
                default:
                    stringResourceId = R.string.message_text;
                    break;
            }
            return stringResourceId;
        }
    }

    public enum Direction {
        send, received;

        public static Direction fromString(String name) {
            return Direction.valueOf(name);
        }
    }

}
