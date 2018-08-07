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

    public static final class FILE {
        public static final int BUFFER_SIZE_DEFAULT = 4096; // 4 Kb
        public static final long ONE_MEGABYTE = 1024 * 1024;
        public static final long ONE_KILOBYTE = 1024;
    }

    public static final int TAB_MAIN = 1;
    public static final int TAB_VIDEO_DETAIL = 2;

    public static final class LOGIN {
        public static final int TAB_LOGIN = 0;
        public static final int TAB_REGISTER = 1;
        public static final int TAB_VERIFY = 2;
    }

    public static final class KEY_BUNDLE {
        public static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
        public static final String KEY_VIDEO_SELECT = "KEY_VIDEO_SELECT";
    }

    public static final class PREFERENCE {
        public static final String PREF_DIR_NAME = "com.viettel.reeng.app";
        public static final String PREF_PUBLIC_RSA_KEY = "PREF_PUBLIC_RSA_KEY";
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
