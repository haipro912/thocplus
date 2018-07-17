package com.vttm.mochaplus.feature.utils;

public class Config {

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
}
