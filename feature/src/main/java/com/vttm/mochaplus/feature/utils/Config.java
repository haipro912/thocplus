package com.vttm.mochaplus.feature.utils;

import android.os.Environment;

public class Config {

    public static final String REVISION = "1";

    public static final String DOMAIN_VIDEO = "http://hl2.mocha.com.vn:8080";
    public static final String CLIENT_TYPE = "Android";
    public static final String COUNTRY_CODE = "VN";

    public static final String KEY = "CQPkng4R1wL@CZT29YDE94A$*";
    public static final String RSA_KEY = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgF3g82nB1ImzAwSN7JXeOC7wChDA4Nbzun" +
            "/2B60sB04LCxBt88yRQTK734ugqAJ9cnYYNjwYfzcoTmubiMygsdtoNf1HTmezAL+ppsJxZ" +
            "/TlfomXz6zUS2HxNUdNcgX0NdHpq5OR9713p6tiq5Z4TdYjja9P7FEG8p4xf8snDEjhAgMBAAE=";

    public static final boolean PREF_DEF_SETUP_KEYBOARD_SEND = false;
    public static final boolean PREF_DEF_SHOW_MEDIA = true;
    public static final boolean PREF_DEF_REPLY_SMS = true;

    public static final class Server {
        public static final boolean SERVER_TEST = false;
        public static final boolean FREE_15_DAYS = false;
        public static final boolean FACEBOOK_TEST = false;
        public static final String USER_FACEBOOK_TEST = "01685269957";
        public static final boolean SEND_VIDEO_ENABLE = false;
    }

    public static final class Smack {
        public static final int PACKET_REPLY_TIMEOUT = 40000;       //change from 300000 -> 30000 -> 20000
        public static final int KEEP_ALIVE_INTERVAL = 40000;
        public static final int PING_INTERVAL = 1 * 60 * 1000;      // 2 phut

        public static final String DOMAIN_MSG = "171.255.193.155";
        public static final int PORT_MSG = 5225;
        public static final String RESOURCE = "reeng";

//        public static final String DOMAIN_MSG = "125.235.13.148";
//        public static final int PORT_MSG = 5222;
//        public static final String RESOURCE = "localhost";
    }

    public static final class Extras {
        public static final String DEFAULT_ENCODING = "UTF-8";
        public static final String ENCODE_MD5 = "vt13579";
        public static final boolean SMOOTH_SCROLL = false;

        public static final String B_PLUS_ID = "mocha_viettel";
        public static final String B_PLUS_MERCHANT =
                "e9715126c9ed69b14bdf6abc790f571b8beeb98c7563ee5e25fe16be20e6ed990bfe94f8c1e82a87d0e13b521276c599b61d9ac86c3f5cc61cc1e3f16c8f75db";
        public static final String B_PLUS_ACCESS_CODE =
                "nIoC4FDBW83LscgCWmC573RbkLLb8168xFImsnLPXW6NnUxODDSTjFuz5fgS4vNBczuj34WpKBiUT6oVMJ6MfA8si2jFewWw17fMJVVW8fJtHoKnNTJu9jVF36Sw1CwZ";
        //sha512//com.viettel.mocha.app,DE:5F:AF:09:44:04:F5:C9:48:49:62:47:7B:B9:B0:00:CE:9A:72:FA

        /*public static final String B_PLUS_ID = "test2";
        public static final String B_PLUS_MERCHANT = "66b688e12af644ab883052df3a30a939";
        public static final String B_PLUS_ACCESS_CODE = "66b688e12af644ab883052df3a30a940";*/
    }

    public static final class Pattern {
        public static final String PHONE = "^0?[9][0-9][1-9][0-9]{6}$|^0?16[0-9]{8}$|^0?12[0-9]{8}$|^0?1[8-9][0-9]{8" +
                "}$|^[+]?849[0-9][1-9][0-9]{6}$|^[+]?8416[0-9]{8}$|^[+]?8412[0-9]{8}$|^[+]?841[8-9][0-9]{8}$";
        public static final String VIETTEL = "^09[6-8][1-9][0-9]{6,6}$|^016[1-9][1-9][0-9]{6,6}$|^086[1-9][0-9]{6," +
                "6}$|^[+]?849[6-8][1-9][0-9]{6,6}$|^[+]?8416[1-9][1-9][0-9]{6,6}$|^[+]?8486[1-9][0-9]{6,6}$";
        public static final String MORE_PHONE = "^[+]?[0-9]{5}[0-9]*$";
        public static final String LINK_YOUTUBE = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\" +
                ".com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";


        public static final String REGEX_MOCHA_VIDEO = "(https|https?)?(://)?(www.)?(m.)?(video.mocha.com.vn)?(.*?)(-v\\d{5,}.html)$";
        public static final String REGEX_GET_ID_MOCHA_VIDEO = "-v(.*?).html";
        public static final String REGEX_MOCHA_CHANNEL = "(https|https?)?(://)?(www.)?(m.)?(video.mocha.com.vn)?(.*?)(_cn\\d{3,}.html)$";
        public static final String REGEX_GET_ID_MOCHA_CHANNEL = "_cn(.*?).html";
    }

    public static final class Storage {
        public static final String REENG_STORAGE_FOLDER = Environment.getExternalStorageDirectory().getPath() +
                "/Mocha";
        public static final String GALLERY_MOCHA = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/Mocha";
        public static final String FILE_DOCUMENT_MOCHA = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath() + "/Mocha";
        public static final String IMAGE_FOLDER = "/Mocha Images";
        public static final String DOWNLOAD_FOLDER = "/Downloads";
        public static final String IMAGE_COMPRESSED_FOLDER = "/.cpthumbs";
        public static final String VOICEMAIL_FOLDER = "/.Voicemails";
        public static final String PROFILE_PATH = "/.Profile";
        public static final String LIST_AVATAR_PATH = "/.Avatar/";
        public static final String BACKGROUND_FOLDER = "/.Background/";
        public static final String STICKER_FOLDER = "/.Sticker/";
        public static final String CACHE_FOLDER = "/.Cache";
        public static final String VIDEO_FOLDER = "/Mocha Videos";
        public static final String GIF_FOLDER = "/.Gif";
    }

}
