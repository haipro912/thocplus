/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.vttm.mochaplus.feature.utils;

/**
 * Created by amitshekhar on 08/01/17.
 */

public final class AppConstants {

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String PACKET = "com.vttm.tinnganradio";
    public static final int TYPE_NEWS = 1;

    public static final String DB_NAME = "mochaplus.db";
    public static final String PREF_NAME = "mochaplus.pref";

    public static final int NUM_SIZE = 20;

    public static final class VIDEO_API {
        public static final String VIP = "VIP";
        public static final String NOVIP = "NOVIP";
    }

    public static final class ThreadMessageConstant {
        public static final int TYPE_THREAD_PERSON_CHAT = 0;
        public static final int TYPE_THREAD_GROUP_CHAT = 1;
        public static final int TYPE_THREAD_OFFICER_CHAT = 2;
        public static final int TYPE_THREAD_ROOM_CHAT = 3;
        public static final int TYPE_THREAD_BROADCAST_CHAT = 4;
    }

    public static final class XMPP {
        public static final String XMPP_RESOUCE = "@reeng/reeng";
        public static final String XMPP_DOMAIN = "reeng";
        public static final String XMPP_SMS = "sms";
        public static final String XMPP_GROUP_RESOUCE = "@muc.reeng/reeng";
        public static final String XMPP_OFFICAL_RESOUCE = "@offical.reeng/reeng";
        public static final String XMPP_ROOM_RESOUCE = "@room.reeng/reeng";
        public static final String XMPP_BROADCAST_RESOUCE = "@broadcast.reeng/reeng";
    }

    public static final class CONNECTION_STATE {
        public static final int NOT_CONNECT = -1;
        public static final int CONNECTING = 1;
        public static final int CONNECTED = 2;
    }

    public static final class FILE {
        public static final String[] FILE_DOC_TYPES = {"application/msword", "application/vnd" +
                ".openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml" +
                ".presentation",
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain",
                "application/pdf"};

        public static final int VOICE_MIN_DURATION = 1;
        public static final String JPEG_FILE_PREFIX = "IMG_";
        public static final String VIDEO_FILE_PREFIX = "VID_";

        public static final String JPEG_FILE_SUFFIX = ".jpg";
        public static final String PNG_FILE_SUFFIX = ".png";
        public static final String ZIP_FILE_SUFFIX = ".zip";
        public static final String MP4_FILE_SUFFIX = ".mp4";
        public static final String GP_FILE_SUFFIX = ".3gp";
        public static final String VIDEO_PREFIX_NAME = "V_MOCHA_";
        public static final String NO_MEDIA_FILE_NAME = ".nomedia";
        // avatar
        public static final int AVATAR_MAX_SIZE = 800; // pixel
        public static final int BUFFER_SIZE_DEFAULT = 4096; // 4 Kb
        // max video recorder 30mb
        public static final long VIDEO_MAX_SIZE = 25 * 1024 * 1024;
        public static final long DOCUMENT_MAX_SIZE = 10 * 1024 * 1024;

        public static final long ONE_MEGABYTE = 1024 * 1024;
        public static final long ONE_KILOBYTE = 1024;

        public static final short VIDEO_RECORD_MAX_DURATION = 15; //Giay
    }

    public static final int TAB_MAIN = 1;
    public static final int TAB_VIDEO_DETAIL = 2;

    public static final class LOGIN {
        public static final int TAB_LOGIN = 0;
        public static final int TAB_REGISTER = 1;
        public static final int TAB_VERIFY = 2;
    }

    public static final class PERMISSION {
        public static final int PERMISSION_REQUEST_ALL = 1;
        public static final int PERMISSION_REQUEST_LOCATION = 2;
        public static final int PERMISSION_REQUEST_RECORD_AUDIO = 3;
        public static final int PERMISSION_REQUEST_TAKE_PHOTO = 4;
        public static final int PERMISSION_REQUEST_TAKE_VIDEO = 5;
        public static final int PERMISSION_REQUEST_EDIT_CONTACT = 6;
        public static final int PERMISSION_REQUEST_SAVE_CONTACT = 7;
        public static final int PERMISSION_REQUEST_DELETE_CONTACT = 8;
        public static final int PERMISSION_REQUEST_WRITE_STORAGE = 9;
        public static final int PERMISSION_REQUEST_WRITE_CONTACT = 10;
        public static final int PERMISSION_REQUEST_TAKE_PHOTO_GROUP_AVATAR = 14;
        public static final int PERMISSION_REQUEST_GALLERY = 15;
        public static final int PERMISSION_REQUEST_FILE = 16;
        public static final int PERMISSION_REQUEST_FILE_DOWNLOAD = 17;
    }

    public static final class KEY_BUNDLE {
        public static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
        public static final String KEY_VIDEO_SELECT = "KEY_VIDEO_SELECT";
    }

    public static final class FONT_SIZE {
        public static final float SMALL_RATIO = 0.85f;
        public static final float MEDIUM_RATIO = 1.0f;
        public static final float LARGE_RATIO = 1.2f;
        public static final int SMALL = 1;
        public static final int MEDIUM = 2;
        public static final int LARGE = 3;

        public static final int LEVEL_0 = 1;
        public static final int LEVEL_1 = 2;
        public static final int LEVEL_1_5 = 3;
        public static final int LEVEL_2 = 4;
        public static final int LEVEL_2_5 = 5;
        public static final int LEVEL_3 = 6;
        public static final int LEVEL_4 = 7;

        public static final int LEVEL_6 = 9;
    }

    public static final class SETTINGS {
        public static final int TRANSLATE = 2;
        public static final int CHANGE_STATUS = 6;
        public static final int LOGOUT = 8;
        public static final int LIST_ROOM_MUSIC = 9;
        public static final int ABOUT = 12;
        public static final int PACKAGE_DETAIL = 13;
        public static final int NEAR_YOU = 14;

        public static final int SETTING_NOTIFICATION = 14;
        public static final int SETTING_PRIVATE = 15;
        public static final int SETTING_CALL_MESSAGE = 16;
        public static final int SETTING_IMAGE_SOUND = 17;
        public static final int NOTE_MESSAGE = 18;

        public static final String DATA_FRAGMENT = "fragment";
        public static final int ALPHA_SHOW_ACTIONBAR = 200;
    }

    public static final class LOCK_APP {
        public static final int LOCK_APP_SETTING = 1;
        public static final int LOCK_APP_LOCKED = 2;
        public static final int LOCK_APP_CHANGE_OLD_PASS = 3;
        public static final int LOCK_APP_CHANGE_PASS = 4;
        public static final int LOCK_APP_CHANGE_PASS_RE = 5;
        public static final int LOCK_APP_OPEN_SETTING = 6;
        public static final int LOCK_APP_LOCKED_SETTING = 7;

        public static final int LOCK_APP_TIME_NOW = 100;//now
        public static final int LOCK_APP_TIME_5_S = 5000;//10s
        public static final int LOCK_APP_TIME_10_S = 10000;
        public static final int LOCK_APP_TIME_30_S = 30000;
        public static final int LOCK_APP_TIME_1_M = 60000;//60s
    }

    public static final class PREFERENCE {
        public static final String PREF_DIR_NAME = "com.viettel.reeng.app";
        public static final String PREF_SPEAKER = "pref_speaker";
        public static final String PREF_APP_VERSION = "pref_app_version";// luu version code cu
        public static final String PREF_CLIENT_INFO_CODE_VERSION = "PREF_CLIENT_INFO_CODE_VERSION";
        public static final String PREF_CLIENT_INFO_DEVICE_LANGUAGE = "PREF_CLIENT_INFO_DEVICE_LANGUAGE";
        public static final String PREF_NO_NOTIF_LIST_ID = "pref_no_notif_list_id";
        public static final String PREF_CONTACT_LIST_REMOVE_FAIL = "pref_contact_list_remove_fail";
        public static final String PREF_AVATAR_FILE_CAPTURE = "avatar_capture";
        public static final String PREF_IMAGE_FILE_CAPTURE = "image_capture";
        public static final String PREF_AVATAR_FILE_CROP = "avatar_crop";
        public static final String PREF_AVATAR_GROUP_FILE_CROP = "avatar_group_crop";
        public static final String PREF_IMAGE_FILE_CROP = "image_crop";
        public static final String PREF_AVATAR_FILE_FACEBOOK = "avatar_facebook";
        public static final String PREF_AVATAR_FILE_GROUP = "avatar_group_";
        public static final String PREF_CHAT_IMAGE_PATH = "pref_chat_image_path";
        public static final String PREF_AVATAR_GROUP_IMAGE_PATH = "pref_avatar_group_image_path";
        public static final String PREF_STRANGER_SHOW_ALERTS = "pref_stranger_show_alerts";
        public static final String PREF_GET_LIST_BLOCK = "pref_get_list_block";
        //for module setting
        public static final String PREF_SETTING_VIBRATE = "pref_vibrate";//true
        public static final String PREF_SETTING_RINGTONE = "pref_ringtone";//true
        public static final String PREF_SETTING_MSG_IN_POPUP = "pref_msg_in_popup";//hien thi msg tren popup; true
        public static final String PREF_SETTING_ENABLE_SEEN = "pref_enable_seen";//true
        public static final String PREF_SETTING_ENABLE_UNLOCK = "pref_enable_unlock";//true
        public static final String PREF_SETTING_NOTIFY_NEW_FRIEND = "pref_setting_notify_new_friend"; //true
        public static final String PREF_SETTING_IMAGE_HD = "pref_setting_image_hd"; //false
        public static final String PREF_SETTING_OFF_MUSIC_IN_ROOM = "pref_setting_off_music"; //false
        public static final String PREF_SETTING_SYSTEM_SOUND = "pref_setting_system_sound";
        public static final String PREF_SETTING_NOTIFICATION_SOUND = "pref_setting_notification_sound";
        public static final String PREF_SETTING_NOTIFICATION_SOUND_DEFAULT = "pref_setting_notification_sound_default";
        public static final String PREF_SETTING_AUTO_SMSOUT = "pref_setting_auto_smsout";
        public static final String PREF_SETTING_RECEIVE_SMSOUT = "pref_setting_receive_smsout";
        public static final String PREF_SETTING_BIRTHDAY_REMINDER = "pref_birthday_reminder";//true
        public static final String PREF_SETTING_FONT_SIZE = "pref_setting_font_size";
        public static final String PREF_SETTING_FLOATING_BUTTON = "pref_setting_floating_button";

        // for prevate setting
        public static final String PREF_SETTING_PREVIEW_MSG = "pref_preview_msg";//xem trc noi dung tin nhan: true
        //
        public static final String PREF_DOMAIN_FILE = "pref_domain_file";
        public static final String PREF_DOMAIN_MSG = "pref_domain_message";
        public static final String PREF_DOMAIN_ON_MEDIA = "pref_domain_on_media";
        public static final String PREF_DOMAIN_IMAGE = "pref_domain_image";
        //
        public static final String PREF_AUTO_PLAY_STICKER = "PREF_AUTO_PLAY_STICKER";
        public static final String PREF_SHOW_MEDIA = "PREF_SHOW_MEDIA";
        public static final String PREF_REPLY_SMS = "PREF_REPLY_SMS";
        // keybroad
        public static final String PREF_KEYBOARD_HEIGHT = "pref_keyboard_height";
        public static final String PREF_KEYBOARD_OFFSET_HEIGHT = "pref_keyboard_offset_height";
        public static final String PREF_TAPLET_LANGSCAPE_KEYBOARD_OFFSET_HEIGHT =
                "pref_taplet_langscape_keyboard_offset_height";
        // music business
        public static final String PREF_RECEIVE_MUSIC_DIFFERENCE_TIME = "pref_receive_music_difference_time";
        public static final String PREF_SEND_MUSIC_LAST_THREAD_ID = "pref_send_music_last_thread_id";
        //niem vui lan toa
        public static final String PREF_SHOW_BUZZ_COUNT = "pref_show_buzz_count";
        //sticker store
        public static final String PREF_SENT_DEVICE_ID = "PREF_SENT_DEVICE_ID";
        public static final String PREF_PUBLIC_RSA_KEY = "PREF_PUBLIC_RSA_KEY";
        public static final String PREF_STICKER_NEW_FROM_SERVER = "new_stickers_from_server_v2";
        //More apps response
        public static final String PREF_MOREAPP_RESPONSE = "PREF_RESPONSE_MOREAPPS";
        //game list response
        public static final String PREF_LISTGAME_RESPONSE = "PREF_LISTGAME_RESPONSE";
        //default back ground
        public static final String PREF_DEFAULT_BACKGROUND_PATH = "PREF_DEFAULT_BG_PATH";
        public static final String PREF_APPLY_BACKGROUND_ALL = "PREF_APPLY_BACKGROUND_ALL";
        //language translate
        public static final String PREF_LANGUAGE_TRANSLATE_SELECTED = "PREF_LANGUAGE_TRANSLATE_SELECTED";
        public static final String PREF_CONFIG_BANNER_CONTENT = "PREF_CONFIG_BANNER_CONTENT_V2";
        public static final String PREF_CONFIG_BANNER_CALL = "PREF_CONFIG_BANNER_CALL";
        //pref onmedia
        public static final String PREF_ONMEDIA_CHAT_WITH_STRANGER = "PREF_ONMEDIA_CHAT_WITH_STRANGER";
        public static final String PREF_ONMEDIA_NOTIFY_NEW_FEED = "PREF_ONMEDIA_NOTIFY_NEW_FEED";
        public static final String PREF_ONMEDIA_SECURE_WEB = "PREF_ONMEDIA_SECURE_WEB";
        public static final String PREF_ONMEDIA_SHARE_FACEBOOK = "PREF_ONMEDIA_SHARE_FACEBOOK";
        public static final String PREF_ONMEDIA_LAST_TIME_SHOW_HOROSCOPE = "PREF_ONMEDIA_LAST_TIME_SHOW_HOROSCOPE";
        //last tab
        public static final String PREF_HOME_LAST_TAB = "PREF_HOME_LAST_TAB";
        public static final String PREF_LOCK_SPAM_ROOM_CHAT = "PREF_LOCK_SPAM_ROOM_CHAT";
        //
        public static final String PREF_DOMAIN_DOMAIN_FILE_TEST = "PREF_DOMAIN_DOMAIN_FILE";
        public static final String PREF_DOMAIN_DOMAIN_MSG_TEST = "PREF_DOMAIN_DOMAIN_MSG";
        public static final String PREF_DOMAIN_DOMAIN_ONMEDIA_TEST = "PREF_DOMAIN_DOMAIN_ONMEDIA";
        public static final String PREF_SETTING_SETUP_KEYBOARD_SEND = "PREF_SETTING_SETUP_KEYBOARD_SEND";//default true
        public static final String PREF_SETTING_LANGUAGE_MOCHA = "PREF_SETTING_LANGUAGE_MOCHA";//default true
        public static final String PREF_MOCHA_USER_VIP_INFO = "PREF_MOCHA_USER_VIP_INFO";//default 0
        public static final String PREF_MOCHA_USER_CBNV = "PREF_MOCHA_USER_CBNV";//default false
        public static final String PREF_MOCHA_ENABLE_CALL = "PREF_MOCHA_ENABLE_CALL";//default -1
        public static final String PREF_MOCHA_ENABLE_SMS_IN = "PREF_MOCHA_ENABLE_SMS_IN";//default -1
        public static final String PREF_MOCHA_ENABLE_SSL = "PREF_MOCHA_ENABLE_SSL";//default -1
        public static final String PREF_KEENG_USER_ID = "PREF_KEENG_USER_ID";
        public static final String PREF_MOCHA_ENABLE_AVNO = "PREF_MOCHA_ENABLE_AVNO";
        public static final String PREF_MOCHA_ENABLE_TAB_CALL = "PREF_MOCHA_ENABLE_TAB_CALL";

        public static final String PREF_LAST_SHOW_ALERT_TAB_HOT = "PREF_LAST_SHOW_ALERT_TAB_HOT";
        public static final String PREF_HAD_SHOW_ALERT_TAB_HOT_TODAY = "PREF_HAD_SHOW_ALERT_TAB_HOT_TODAY";
        public static final String PREF_HAD_NOTIFY_TAB_HOT = "PREF_HAD_NOTIFY_TAB_HOT";

        public static final String PREF_LAST_SHOW_ALERT_TAB_VIDEO = "PREF_LAST_SHOW_ALERT_TAB_VIDEO";
//        public static final String PREF_LAST_SHOW_ALERT_POPUP_REGISTER_VIP = "PREF_LAST_SHOW_ALERT_POPUP_REGISTER_VIP";

        public static final String PREF_LAST_SHOW_ALERT_AB_DISCOVER = "PREF_LAST_SHOW_ALERT_AB_DISCOVER";
        //app lock
        public static final String PREF_SETTING_LOCK_APP_ENABLE = "PREF_SETTING_LOCK_APP_ENABLE";
        public static final String PREF_SETTING_LOCK_APP_TIME = "PREF_SETTING_LOCK_APP_TIME";
        public static final String PREF_SETTING_LOCK_APP_PASS_ENCRYPTED = "PREF_SETTING_LOCK_APP_PASS_ENCRYPTED";
        public static final String PREF_SETTING_LOCK_APP_STATE_LOCKED = "PREF_SETTING_LOCK_APP_STATE_LOCKED";
        public static final String PREF_AROUND_LOCATION = "PREF_AROUND_LOCATION";

        public static final String PREF_INAPP_LAST_SHOW_POPUP = "PREF_INAPP_LAST_SHOW_POPUP";
        public static final String PREF_INAPP_LAST_FAKE_OA = "PREF_INAPP_LAST_FAKE_OA";

        public static final String PREF_SAVE_SEARCH_USER = "PREF_SAVE_SEARCH_USER";
        public static final String PREF_LUCKEY_WHEEL_LAST_SPIN = "PREF_LUCKEY_WHEEL_LAST_SPIN";
        public static final String PREF_LUCKEY_WHEEL_LAST_TIME_CLICK = "PREF_LUCKEY_WHEEL_LAST_TIME_CLICK";
        public static final String PREF_LUCKEY_WHEEL_LAST_LOTTPOINT = "PREF_LUCKEY_WHEEL_LAST_LOTTPOINT";
        public static final String PREF_ITEM_DEEPLINK_LAST_TIME_CLICK = "PREF_ITEM_DEEPLINK_LAST_TIME_CLICK";
        public static final String PREF_LAST_TIME_SHOW_DIALOG_WEB = "PREF_LAST_TIME_SHOW_DIALOG_WEB";
        public static final String PREF_COUNT_SHOW_DIALOG_WEB = "PREF_COUNT_SHOW_DIALOG_WEB";
        public static final String PREF_LIST_GAME_LAST_TIME_CLICK = "PREF_LIST_GAME_LAST_TIME_CLICK";

        public static final String PREF_CALL_OUT_ENABLE_STATE = "PREF_CALL_OUT_ENABLE_STATE";
        public static final String PREF_MY_STRANGER_LOCATION = "PREF_MY_STRANGER_LOCATION";
        public static final String PREF_LAST_UPDATE_STRANGER_LOCATION = "PREF_LAST_UPDATE_STRANGER_LOCATION";

        public static final String PREF_LIST_TOP_VIDEO = "PREF_LIST_TOP_VIDEO";
        public static final String PREF_CALL_HISTORY_BANNER_LAST_TIME_CLICK =
                "PREF_CALL_HISTORY_BANNER_LAST_TIME_CLICK";
        public static final String PREF_CONFIDE_LAST_TIME_CLICK = "PREF_CONFIDE_LAST_TIME_CLICK";
        public static final String PREF_QRCODE_URL = "QRCODE_URL";

        public static final String PREF_LAST_TIME_UPLOAD_LOG = "PREF_LAST_TIME_UPLOAD_LOG";
        public static final String PREF_ENABLE_UPLOAD_LOG = "PREF_ENABLE_UPLOAD_LOG";

        public static final String PREF_LIST_STICKY_BANNER = "LIST_STICKY_BANNER";
        public static final String PREF_LAST_TIME_UPLOAD_LOCATION = "PREF_LAST_TIME_UPLOAD_LOCATION";
        public static final String PREF_LAST_TIME_GET_LIST_GROUP = "PREF_LAST_TIME_GET_LIST_GROUP";
        public static final String PREF_LIST_ADS_VIDEO_CATEGORY = "PREF_LIST_ADS_VIDEO_CATEGORY";

        /**
         * config content
         */
        public static final class CONFIG {
            public static final String TIMESTAMP_GET_CONFIG = "pref_timestamp_get_config_4";
            public static final String SMS2_NOTE_AVAILABLE = "smsout2.note.available"; //key o ban moi
            public static final String SMSOUT_PREFIX_AVAILABLE = "smsout.to.prefix";
            // domain keeng --> ko dung domain lay tu api config nua
            public static final String PREF_DOMAIN_SERVICE_KEENG = "domain.service.keeng";
            public static final String PREF_DOMAIN_MEDIA2_KEENG = "domain.media2.keeng";
            public static final String PREF_DOMAIN_IMAGE_KEENG = "domain.image.keeng";
            // domain keeng tu ban tin success xmpp
            public static final String PREF_DOMAIN_SERVICE_KEENG_V2 = "domain.service.keeng_v2";
            public static final String PREF_DOMAIN_MEDIA2_KEENG_V2 = "domain.media2.keeng_v2";
            public static final String PREF_DOMAIN_IMAGE_KEENG_V2 = "domain.image.keeng_v2";
            // config game
            public static final String TIMESTAMP_GET_LIST_STICKER = "TIMESTAMP_GET_LIST_STICKER";
            public static final String DEFAULT_STATUS_NOT_MOCHA = "default.status.notmocha";
            public static final String NVLT_ENABLE = "nvlt.enable";
            // version app
            public static final String VERSION_CODE_APP = "version.code.app";
            public static final String VERSION_NAME_APP = "version.name.app";
            public static final String PING_INTERVAL = "ping.interval";

            //value default
            public static final String VERSION_CODE_DEFAULT = "0";

            public static final String PREF_FORCE_GET_CONFIG_NOT_DONE = "PREF_FORCE_GET_CONFIG_NOT_DONE";
            public static final String PREF_FORCE_GET_STICKER_NOT_DONE = "PREF_FORCE_GET_STICKER_NOT_DONE";

            //url api keeng service
            public static final String PREF_URL_SERVICE_GET_SONG = "SERVICE_GET_SONG";
            public static final String PREF_URL_SERVICE_GET_TOP_SONG = "SERVICE_GET_TOP_SONG";
            public static final String PREF_URL_SERVICE_GET_FEEDS_KEENG = "SERVICE_GET_FEEDS_KEENG";
            public static final String PREF_URL_SERVICE_SEARCH_SONG = "SERVICE_SEARCH_SONG";
            public static final String PREF_URL_SERVICE_GET_ALBUM = "SERVICE_GET_ALBUM";
            public static final String PREF_URL_SERVICE_GET_SONG_UPLOAD = "SERVICE_GET_SONG_UPLOAD";
            public static final String PREF_URL_MEDIA2_SEARCH_SUGGESTION = "MEDIA2_SEARCH_SUGGESTION";
            public static final String PREF_URL_MEDIA_UPLOAD_SONG = "MEDIA_UPLOAD_SONG";
            //
            public static final String IMAGE_PROFILE_MAX_SIZE = "imageprofile.max.size";
            // crbt
            public static final String PREF_CRBT_ENABLE = "crbt.enable";
            public static final String PREF_CRBT_PRICE = "crbt.price";
            public static final String PREF_KEENG_PACKAGE = "android.keeng.package.name";
            //onmedia
            public static final String CONFIG_ONMEDIA_ON = "onmedia.on";

            public static final String REGISTER_VIP_BANNER = "register.vip.banner.v2";
            public static final String REGISTER_VIP_BUTTON = "register.vip.button.v2";
            public static final String REGISTER_VIP_CONFIRM = "register.vip.confirm.v2";
            public static final String REGISTER_VIP_CMD = "register.vip.cmd";
            public static final String REGISTER_VIP_CMD_CANCEL = "register.vip.cmdcancel";
            public static final String UNREGISTER_VIP_CONFIRM = "unregister.vip.confirm";
//            public static final String REGISTER_VIP_RECONFIRM = "register.vip.reconfirm";
//            public static final String REGISTER_VIP_WC_CONFIRM = "register.vip.wc.confirm";

            public static final String INAPP_ENABLE = "inapp.enable";
            public static final String LUCKY_WHEEL_ENABLE = "lucky.wheel.enable";
            public static final String QR_SCAN_ENABLE = "qr.scan.enable";
            public static final String GUEST_BOOK_ENABLE = "mocha.memory.on";
            public static final String CALL_OUT_LABEL = "call.out.label";
            public static final String LISTGAME_ENABLE = "listgame.enable";
            public static final String BANNER_CALL_HISTORY = "banner.callhistory";
            public static final String DISCOVERY_ENABLE = "discovery.enable";
            public static final String STRANGER_LOCATION_TIMEOUT = "stranger.location.update";
            public static final String BACKGROND_DEFAULT = "background.default";
            public static final String MORE_ITEM_DEEPLINK = "more.item.deeplink";
            public static final String SHAKE_GAME_ON = "shake.game.on";
            public static final String LIXI_ENABLE = "lixi.enable";
            public static final String SONTUNG83_ENABLE = "sontung83.enable";
            public static final String AVNO_PAYMENT_WAPSITE = "avno.payment.wapsite";
            public static final String VIDEO_UPLOAD_USER = "video.upload.user";
            public static final String VIDEO_UPLOAD_PASS = "video.upload.pass";
            public static final String TAB_VIDEO_ENABLE = "mocha.video.enable";
            public static final String SPOINT_ENABLE = "spoint.enable";
            public static final String BANKPLUS_ENABLE = "bankplus.enable";
            public static final String WATCH_VIDEO_TOGETHER_ENABLE = "mocha.watchtogether.enable";
            public static final String TAB_STRANGER_ENABLE = "home.stranger.enable";
            public static final String SUGGEST_VIDEO_ENABLE = "suggest.video.enable";
            public static final String FIREBASE_WAKEUP_ENABLE = "firebase.wakeup.enable";
            public static final String GET_LINK_LOCATION = "getlink.location";
            public static final String WHITELIST_DEVICE = "whitelist.device";
            public static final String PREFIX_CHANGENUMBER = "changenum.prefix.v2";
            public static final String PREFIX_CHANGENUMBER_TEST = "changenum.prefix.v2.test";
        }
    }

    public static final class PREF_DEFAULT {
        public static final String URL_SERVICE_GET_SONG_DEFAULT = "/KeengWSRestful/ws/common/getSong";
        public static final String URL_SERVICE_GET_TOP_SONG_DEFAULT = "/KeengWSRestful/ws/internal/mocha/getSongMocha";
        public static final String URL_SERVICE_GET_FEEDS_KEENG_DEFAULT = "/KeengWSRestful/ws/social/user/feed";
        public static final String URL_SERVICE_SEARCH_SONG_DEFAULT = "/KeengWSRestful/ws/common/search";
        public static final String URL_SERVICE_GET_ALBUM_DEFAULT = "/KeengWSRestful/ws/common/getAlbum";
        public static final String URL_SERVICE_GET_SONG_UPLOAD_DEFAULT = "/KeengWSRestful/ws/internal/mocha/getSongUpload";
        public static final String URL_MEDIA2_SEARCH_SUGGESTION_DEFAULT = "/solr/mbartists/select/";
        public static final String URL_MEDIA_UPLOAD_SONG_DEFAULT = "http://vip.medias4.cdn.keeng.vn:8089/uploadv4.php";
        public static final String URL_BANNER_CALL_HISROTY = "assets://banner/banner_call_history.jpg";
        //for module setting
        public static final boolean PREF_DEF_VIBRATE = true;
        public static final boolean PREF_DEF_RINGTONE = true;
        public static final boolean PREF_DEF_MSG_IN_POPUP = false;// default disable quick reply
        public static final boolean PREF_DEF_ENABLE_SEEN = true;
        public static final boolean PREF_DEF_ENABLE_UNLOCK = true;
        public static final boolean PREF_DEF_NOTIFY_NEW_FRIEND = true;
        public static final boolean PREF_DEF_IMAGE_HD = false;
        public static final boolean PREF_DEF_PLAYMUSIC_IN_ROOM = false;
        public static final boolean PREF_DEF_BIRTHDAY_REMINDER = true;

        // for prevate setting
        public static final boolean PREF_DEF_PREVIEW_MSG = true;
        // keyboard
        public static final int KEYBOARD_HEIGHT_DEFAULT = 233; // dp
        public static final int CHAT_BAR_HEIGHT_DEFAULT = 44; // dp
        public static final int SLIDE_MENU_MARGIN = 66;//dp

        //onmedia
        public static final boolean PREF_DEF_ONMEDIA_CHAT_WITH_STRANGER = true;
        public static final boolean PREF_DEF_ONMEDIA_NOTIFY_NEW_FEED = true;
        public static final boolean PREF_DEF_ONMEDIA_SECURE_WEB = true;
        //max images can upload
        public static final String PREF_IMAGE_PROFILE_MAX_SIZE = "20";
        //crbt
        public static final String PREF_DEF_CRBT_ENABLE = "0";
        public static final String PREF_DEF_CRBT_PRICE = "1000 VND";
        public static final String PREF_DEF_KEENG_PACKAGE = "com.vttm.keeng";
        //upload video
        public static final String PREF_DEF_VIDEO_UPLOAD_USER = "974802c5178ab63eba02d8035ad3f6ab";
        public static final String PREF_DEF_VIDEO_UPLOAD_PASS = "974802c5178ab63eba02d8035ad3f6ab";


    }

    public static final class ALARM_MANAGER {
        public static final long PING_TIMER = 60 * 1000L; //90s
        public static final int PING_ID = 13579;
        // ping pong music
        public static final long MUSIC_PING_TIMER = 30000L;
        public static final int MUSIC_PING_ID = 11112;
        public static final int MUSIC_PONG_ID = 11113;
        // timout timer
        public static final int TIME_OUT_RESPONSE_ACTION_MUSIC = 30 * 1000;     // 30s
        public static final int TIME_OUT_RECEIVE_INVITE_MUSIC = 20 * 1000;      // 20s
        public static final long ENTER_BACKGROUND_MAX_TIME = 30 * 60 * 1000;    // 30 mins
        //public static final long ENTER_BACKGROUND_MAX_TIME = 120 * 60 * 1000;    // 30 mins
    }

    public static final class CONTACT {
        public static final int STATUS_LIMIT = 100;
        public static final int USER_NAME_MAX_LENGTH = 40;
        public static final int NONE = 0;
        public static final int ACTIVE = 1;
        public static final int DEACTIVE = 2;
        public static final int SYSTEM_BLOCK = 4;

        public static final int GROUP_NONE = 0;
        public static final int GROUP_FAVORITE = 1;
        public static final int GENDER_MALE = 1;    // nam
        public static final int GENDER_FEMALE = 0;  // nu
        public static final int CONTACT_VIEW_NOMAL = 1;
        public static final int CONTACT_VIEW_CHECKBOX = 2;
        public static final int CONTACT_VIEW_ICON_BLOCK = 3;
        public static final int CONTACT_VIEW_SHARE = 4;
        public static final int CONTACT_VIEW_THREAD_MESSAGE = 5;
        public static final int CONTACT_VIEW_FORWARD_MESSAGE = 6;
        public static final int CONTACT_VIEW_MEMBER_GROUP = 7;
        public static final int CONTACT_VIEW_AVATAR_AND_NAME = 8;
        //public static final int COLOR_AVATAR_SIZE = 20;
        public static final int NUM_COLORS_AVATAR_DEFAULT = 10;
        public static final int MIN_LENGTH_NUMBER = 1;
        // group,stranger,officer list
        public static final String TYPE_LIST = "display_type_list";
        public static final int SHOW_LIST_GROUP = 1;
        public static final int FRAG_LIST_UTIL = 3;
        public static final int FRAG_LIST_SOCIAL_REQUEST = 4;
        public static final int FRAG_LIST_CONTACT_WITH_ACTION = 5;
        public static final int FRAG_LIST_FRIEND_MOCHA = 6;
        public static final String DATA_FRAGMENT = "data_frag";
        //stranger
        public static final String STRANGER_MUSIC_ID = "music_stranger";
        public static final String STRANGER_CONFIDE_ID = "talk_stranger";
        public static final String STRANGER_MOCHA_ID = "mocha_stranger";
        public static final int MAX_MEMBER_GROUP_SHOW = 5;
        //follow
        public static final int FOLLOW_STATE_UNKNOW = -2;//chua lay dc state
        public static final int FOLLOW_STATE_NONE = 0;  // khong co quan he
        public static final int FOLLOW_STATE_FOLLOWED = 1;//dang follow B
        public static final int FOLLOW_STATE_BE_FOLLOWED = 2;// B follow minh
        public static final int FOLLOW_STATE_FRIEND = 3;// friend

        public static final int SOCIAL_SOURCE_MOCHA = 0;
        public static final int SOCIAL_SOURCE_NON = 1;

        public static final int DEFAULT_ALPHA_ACTIONBAR = 0;
    }

    public static final class IMAGE_TYPE {
        public static final int IMAGE_AVATAR = 0;
        public static final int IMAGE_COVER = 1;
        public static final int IMAGE_NORMAL = 2;
        public static final int IMAGE_IC_FRONT = 3;
        public static final int IMAGE_IC_BACK = 4;
    }

    public static final class HTTP {
        public static final String LINK_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";
        public static final String LINK_MARKET = "market://details?id=";
        public static final String BOUNDARY = "reeng";
        public static final int CONNECTION_TIMEOUT = 30 * 1000; //30s

        public static final String REST_CLIENT_TYPE = "clientType";
        public static final String REST_REVISION = "revision";
        public static final String REST_GROUP_ID = "groupid";
        public static final String REST_MSISDN = "msisdn";
        public static final String REST_OTHER_MSISDN = "otherMsisdn";
        public static final String REST_TOKEN = "token";
        public static final String REST_ERROR_CODE = "errorCode";// 1 so api dung code, 1 so dung errorCode :(((((((((
        public static final String REST_CODE = "code";
        public static final String INVALID_USER = "invalidUser";
        public static final String INVALID_USER_DESC = "invalidUserDesc";
        public static final String REST_DESC = "desc";
        public static final String REST_THUMB = "thumb";
        public static final String REST_LINK = "link";
        public static final int CODE_SUCCESS = 0;
        public static final String RECEIVERS = "receivers";
        public static final String REST_NAME = "name";
        public static final String REST_USER_NAME = "username";
        public static final String REST_COUNTRY_CODE = "countryCode";
        public static final int CLIENT_TYPE_ANDROID = 1;
        public static final String CLIENT_TYPE_STRING = "Android";
        public static final String LUCKY_CODE = "luckyCode"; //key lucky_code khi invite SMS tra ve
        public static final String REST_NOT_SUPPORT = "notsupport";
        public static final String USER_FACEBOOK_ID = "facebook_id";
        public static final String REQ_TIME = "reqTime";
        public static final String DATA_ENCRYPT = "dataEncrypt";
        public static final String BLOCK_LIST = "blocklist";
        public static final String ROOM_ID = "roomId";
        public static final String FRIENDS = "friends";
        public static final String REST_CONTENT = "content";
        public static final String REST_PAGE = "page";

        public static final String KEENG_USER_ID = "userid";
        public static final String KEENG_MEDIA_NAME = "media_name";
        public static final String KEENG_SINGER_NAME = "singer_name";
        public static final String KEENG_SESSION_TOKEN_UPLOAD = "sessiontoken";

        public static final String REST_LANGUAGE_CODE = "languageCode";
        public static final int CODE_UPLOAD_FAILED_EXCEEDED = 402;

        public static final String UPLOAD_TYPE_IMAGE = "image";
        //new params
        public static final String TIME_STAMP = "timestamp";
        public static final String DATA_SECURITY = "security";
        public static final String REST_MD5 = "md5";
        public static final String REST_RATIO = "ratio";

        public static final String SERVICE_TYPE = "serviceType";
        public static final String LINK_SHARE = "linkShare";
        public static final String BIRTHDAY_REMINDER = "birthdayReminder";

        public static final class CRBT {
            public static final String FROM = "from";
            public static final String TO = "to";
            public static final String SESSION = "session";
            public static final String CRBT_SONG_INFO = "songinfo";
            public static final String FROM_NICK = "fromnick";
            public static final String TO_NICK = "tonick";
            public static final String EXTERNAL = "external";
        }

        public static final class MESSGE {
            public static final String FROM = "from";
            public static final String TO = "to";
            public static final String CONTENT = "content";
            public static final String TYPE = "type";
            public static final String MSG_ID = "msgid";
            public static final String MD5_STR = "md5Str";
        }

        public static final class FILE {
            public static final String REST_UP_FILE = "uploadfile";
            public static final String REST_FILE_TYPE = "type";
            public static final String REST_UP_MUSIC = "media";
        }

        public static final class CONTACT {
            public static final String TIME_STAMP = "timestamps";
            public static final String DATA = "contacts";
            public static final String MSISDN = "cmsisdn";
            public static final String STATUS = "status";
            public static final String STATE = "state";
            public static final String LAST_AVATAR = "lavatar";
            public static final String GENDER = "gender";
            public static final String NAME = "cname";
            public static final String BIRTHDAY_STRING = "birthdayStr";
            public static final String COVER_IMAGE = "cover";
            public static final String COVER_ID = "coverSId";
            public static final String ALBUMS = "albums";
            public static final String PERMISSION = "permission";
            public static final String FOLLOWER = "follower";
            public static final String STATE_FOLLOWER = "sFollow";
            public static final String CHAT_DETAIL = "chatdetail";
            public static final String IS_COVER_DEFAULT = "isCover";
        }

        public static final class USER_INFOR {
            public static final String INFOR = "info";
            public static final String NAME = "name";
            public static final String GENDER = "gender";
            public static final String BIRTHDAY = "birthday";
            public static final String STATUS = "status";
            public static final String LAST_AVATAR = "lavatar";
            public static final String BIRTHDAY_STRING = "birthdayStr";
            public static final String AVATAR_URL = "avatar_url";
            public static final String COVER_IMAGE = "cover";
            public static final String ALBUMS = "albums";
            public static final String PERMISSION = "permission";
            public static final String HIDE_STRANGLE_HISTORY = "hideStrangleHistory";
            public static final String EMAILS = "emails";
            public static final String BIRTHDAY_REMINDER = "birthday_reminder";
        }

        public static final class STICKER {
            public static final String STICKER_COLLECTION_DATA = "stickerCollection";
            public static final String STICKER_COLLECTION_ID = "collectionId";
            public static final String STICKER_COLLECTION_NAME = "collectionName";
            public static final String STICKER_COLLECTION_NUMBER = "numberSticker";
            public static final String STICKER_COLLECTION_AVATAR = "collectionAvatar";
            public static final String STICKER_COLLECTION_TYPE = "type";
            public static final String STICKER_COLLECTION_PREVIEW = "collectionPreview";
            // sticker item
            public static final String STICKER_ITEM_DATA = "stickers";
            public static final String STICKER_ITEM_ID = "stickerId";
            public static final String STICKER_ITEM_TYPE = "type";
            public static final String STICKER_ITEM_IMAGE = "img";
            public static final String STICKER_ITEM_VOICE = "voice";
        }

        public static final class STRANGER_MUSIC {
            public static final String NAME = "name";
            public static final String LAST_AVATAR = "lastChangeAvatar";
            public static final String BIRTHDAY = "birthdayStr";
            public static final String GENDER = "gender";
            public static final String MSISDN = "msisdn";
            public static final String STATUS = "status";
            public static final String IS_STAR = "isStar";
            public static final String HIDE_STRANGER_HISTORY = "hideStrangleHistory";
            public static final String LOCATION_ID = "locationId";
            public static final String MESSAGE_STICKY = "messageSticky";
            public static final String GROUP_ID = "groupId";
            public static final String POSTER_INFO = "posterInfo";
            public static final String SONG_INFO = "songInfo";
            public static final String ACCEPTOR_INFO = "acceptorInfo";
            public static final String ROOM_ID = "roomId";
            public static final String SESSION_ID = "sessionId";
            public static final String CREATE_DATE = "createdDate";
            public static final String STATE = "state";
            public static final String SONG_ID = "id";
            public static final String SONG_NAME = "name";
            public static final String SONG_SINGER = "singer";
            public static final String SONG_IMAGE = "image";
            public static final String SONG_MEDIA_URL = "mediaurl";
            public static final String SONG_URL = "url";
            public static final String SONG_CRBT_CODE = "crbt_code";
            public static final String SONG_CRBT_PRICE = "crbt_price";
            // banner quang cao
            public static final String STRANGER_BANNER = "banner";
            //around
            public static final String STRANGER_DISTANCE_STR = "strDistance";
            public static final String STRANGER_GEO_HASH = "geoHash";
            public static final String STRANGER_AROUND_INFO = "info";
            public static final String STRANGER_AGE = "age";
            // cofide
            public static final String CONFIDE_STATUS = "status";
        }

        public static final class STRANGER_STICKY {
            public static final int TYPE_DEEP_LINK = 1;
            public static final int TYPE_FAKE_MO = 2;
            public static final int TYPE_WEB_VIEW = 3;
            public static final String STICKY_TITLE = "imageTitle";
            public static final String STICKY_IMAGE_URL = "imageUrl";
            public static final String STICKY_TYPE = "type";
            public static final String STICKY_CONFIRM = "confirm";
            public static final String STICKY_ACTION = "action";
        }

        public static final class OFFICER {
            public static final String OFFICER_LIST = "listOffical";
            public static final String SERVER_ID = "serviceId";
            public static final String ALIAS = "alias";
            public static final String AVATAR_URL = "avatar";
            public static final String DESCRIPTION = "description";
            public static final String OFFICIAL_TYPE = "offical_type";
            //room
            public static final String MEMBER_NAME = "name";
            public static final String ROOM_ID = "roomId";
            public static final String JOIN_STATE = "joinState";
            public static final String ROOM_PLAY_LIST = "playList";
            public static final String ROOM_STATE_ONLINE = "roomOnline";
            public static final String ROOM_DESC_SONG = "msgPoster";
            public static final String ROOM_FOLLOW = "follow";
            public static final String ROOM_BACKGROUND = "background";
            //
            public static final String ROOM_FOLLOWS = "follows";
            public static final String ROOM_ORDER = "iOrder";
            public static final String ROOM_GROUP_ID = "groupId";
        }

        public static final class ANONYMOUS {
            public static final String FROM_NAME = "from_nickname";
            public static final String TO_AVATAR = "to_lavatar";// nguoi can thong tin
            public static final String TO_NAME = "to_nickname";
        }

        public static final class LOG_LISTEN {
            public static final String ACTION_TYPE = "actionType"; // = 1
            public static final String ITEM_TYPE = "itemType"; //  1 : song  , 2 : album , 3 : video
            public static final String ITEM_ID = "itemId";
            public static final String CHANNEL = "channel";     //mocha
            public static final String USER_ID = "userId";
            public static final String PASSWORD = "password";
        }

        public static final class ROOM_CHAT {
            public static final String REPORTER = "reporter";
            public static final String REPORTED = "reported";
            public static final String REPORTTYPE = "reportType";
            public static final String REPORTCONTENT = "content";
        }

        public static final class POLL {
            public static final String ID = "id";
            public static final String POLL_DATA = "data";
            public static final String POLL_TITLE = "title";
            public static final String POLL_CHOICE = "choice";
            public static final String POLL_GROUP_ID = "groupId";
            public static final String POLL_OPTIONS = "options";
            public static final String POLL_ID = "pollId";
            public static final String POLL_OPTION_ID = "optionId";
            public static final String POLL_NEW_IDS = "optionNewIds";
            public static final String POLL_OLD_IDS = "optionOldIds";
            public static final String RESULT = "result";
            public static final String VOTERS = "voters";
            public static final String SELECTED = "selected";
        }

        public static final class BDSV {
            public static final String MATCH_ID = "match_id";
            public static final String TEAM_ID = "team_id";
            public static final String NUMBER = "number";
            public static final String VIDEO_ID = "id";
            public static final String VIDEO_TYPE = "type";
        }
    }
}
