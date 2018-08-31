package com.vttm.mochaplus.feature.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.chatlib.utils.Log;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.BuildConfig;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.helper.HttpHelper;
import com.vttm.mochaplus.feature.helper.NetworkHelper;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.helper.PrefixChangeNumberHelper;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppConstants.PREFERENCE;
import com.vttm.mochaplus.feature.utils.AppConstants.PREFERENCE.CONFIG;
import com.vttm.mochaplus.feature.utils.AppConstants.PREF_DEFAULT;
import com.vttm.mochaplus.feature.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by toanvk2 on 1/16/15.
 */
public class ContentConfigBusiness {
    private static final String TAG = ContentConfigBusiness.class.getSimpleName();
    private ApplicationController mApplication;
    private SharedPreferences mPref;
    private Resources mRes;
    private HashMap<String, String> hashMapContents;
    private HashMap<String, String> hashMapConfigUser;
    // key = key of config, value = default value
    private Map<String, String> mFilterConfig = new HashMap<>();
    private long lastTimeGetConfig = -1L;
    private ContactBusiness mContactBusiness;
    // banner config
//    private ArrayList<BannerMocha> mListBanners;
    //    private ArrayList<BannerMocha> mListBannersCall;
    //private String apiKeyTranslate = null;
    private boolean isEnableCrbt = false;
    // location music
    private boolean isReady = false;
    private boolean isEnableOnmedia = false;
    private boolean isEnableInApp = false;
    private boolean isEnableLuckyWheel = false;
    private boolean isEnableQR = false;
    private boolean isEnableGuestBook = false;
    private boolean isEnableListGame = false;
    private boolean isEnableDiscovery = false;
    private String backgroundDefault;
    private boolean isUpdate = false;
//    private MoreItemDeepLink moreItemDeepLink = null;
    private boolean isShakeGame = false;
    private boolean isEnableLixi = false;
    private boolean isEnableSonTung83 = false;
//    private ArrayList<PinMessage> listStickyBanner;

    private boolean isEnableTabVideo = false;
    private boolean isEnableSpoint = false;
    private boolean isEnableBankplus = false;
    private boolean isEnableWatchVideoTogether = false;
    private boolean isEnableTabStranger = false;
    private boolean isEnableSuggestVideo = false;
    private boolean isEnableWakeUpFirebase = false;

    private HashMap<String, String> hashChangeNumberOld2New = new HashMap<>();
    private HashMap<String, String> hashChangeNumberNew2Old = new HashMap<>();
    private Pattern mPatternPrefixChangeNumb;
    private String regexPrefixChangeNumb;

    private DataManager dataManager;

    public ContentConfigBusiness(ApplicationController appication) {
        mApplication = appication;
        //TODO khoi tao pref len dau
        mRes = mApplication.getResources();
        mPref = mApplication.getSharedPreferences(PREFERENCE.PREF_DIR_NAME,
                Context.MODE_PRIVATE);
        dataManager = appication.getDataManager();
    }

    public void init() {
        mContactBusiness = mApplication.getContactBusiness();
//        initConfigBanner();
        initFilterConfig();
        // get config tu pref lan dau khoi tao busuness
        getAllConfigFromPref();
        PhoneNumberHelper.getInstant().setSmsOutPrefixes(getContentConfigByKey(CONFIG.SMSOUT_PREFIX_AVAILABLE));
//        initListStickyBanner();
        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }

    /**
     * can lay config gi tu server thi khai bao o day
     */
    private void initFilterConfig() {
        // key = key of config, value = default value
        mFilterConfig.put(CONFIG.SMS2_NOTE_AVAILABLE, "");
        mFilterConfig.put(CONFIG.SMSOUT_PREFIX_AVAILABLE, "");
        // localpush music
        mFilterConfig.put(CONFIG.NVLT_ENABLE, "0");
        mFilterConfig.put(CONFIG.DEFAULT_STATUS_NOT_MOCHA, mRes.getString(R.string.status_non_mocha));
        // keeng
        mFilterConfig.put(CONFIG.PREF_DOMAIN_SERVICE_KEENG, "");
        mFilterConfig.put(CONFIG.PREF_DOMAIN_MEDIA2_KEENG, "");
        mFilterConfig.put(CONFIG.PREF_DOMAIN_IMAGE_KEENG, "");
        //version app
        mFilterConfig.put(CONFIG.VERSION_CODE_APP, String.valueOf(BuildConfig.VERSION_CODE));
        mFilterConfig.put(CONFIG.VERSION_NAME_APP, String.valueOf(BuildConfig.VERSION_NAME));
        mFilterConfig.put(CONFIG.PING_INTERVAL, String.valueOf(AppConstants.ALARM_MANAGER.PING_TIMER));
        //url service keeng
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_GET_SONG, PREF_DEFAULT.URL_SERVICE_GET_SONG_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_GET_TOP_SONG, PREF_DEFAULT.URL_SERVICE_GET_TOP_SONG_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_GET_FEEDS_KEENG, PREF_DEFAULT.URL_SERVICE_GET_FEEDS_KEENG_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_SEARCH_SONG, PREF_DEFAULT.URL_SERVICE_SEARCH_SONG_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_GET_ALBUM, PREF_DEFAULT.URL_SERVICE_GET_ALBUM_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_SERVICE_GET_SONG_UPLOAD, PREF_DEFAULT.URL_SERVICE_GET_SONG_UPLOAD_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_MEDIA2_SEARCH_SUGGESTION, PREF_DEFAULT.URL_MEDIA2_SEARCH_SUGGESTION_DEFAULT);
        mFilterConfig.put(CONFIG.PREF_URL_MEDIA_UPLOAD_SONG, PREF_DEFAULT.URL_MEDIA_UPLOAD_SONG_DEFAULT);
        //upload image profile
        mFilterConfig.put(CONFIG.IMAGE_PROFILE_MAX_SIZE, PREF_DEFAULT.PREF_IMAGE_PROFILE_MAX_SIZE);
        mFilterConfig.put(CONFIG.PREF_CRBT_ENABLE, PREF_DEFAULT.PREF_DEF_CRBT_ENABLE);
        mFilterConfig.put(CONFIG.PREF_CRBT_PRICE, PREF_DEFAULT.PREF_DEF_CRBT_PRICE);
        mFilterConfig.put(CONFIG.PREF_KEENG_PACKAGE, PREF_DEFAULT.PREF_DEF_KEENG_PACKAGE);
        //onmedia
        mFilterConfig.put(CONFIG.CONFIG_ONMEDIA_ON, "0");
        mFilterConfig.put(CONFIG.REGISTER_VIP_BUTTON, mRes.getString(R.string.register_vip_button));
        mFilterConfig.put(CONFIG.REGISTER_VIP_BANNER, mRes.getString(R.string.register_vip_banner));
        mFilterConfig.put(CONFIG.REGISTER_VIP_CONFIRM, mRes.getString(R.string.register_vip_confirm));
        mFilterConfig.put(CONFIG.UNREGISTER_VIP_CONFIRM, mRes.getString(R.string.msg_confirm_un_vip));
//        mFilterConfig.put(CONFIG.REGISTER_VIP_RECONFIRM, mRes.getString(R.string.msg_reconfirm_vip));
//        mFilterConfig.put(CONFIG.REGISTER_VIP_WC_CONFIRM, mRes.getString(R.string.register_vip_banner));
        //in app
        mFilterConfig.put(CONFIG.INAPP_ENABLE, "0");// mac dinh la off
        mFilterConfig.put(CONFIG.LUCKY_WHEEL_ENABLE, "0"); // mac dinh la off
        mFilterConfig.put(CONFIG.QR_SCAN_ENABLE, "0");
        mFilterConfig.put(CONFIG.GUEST_BOOK_ENABLE, "0");
        mFilterConfig.put(CONFIG.CALL_OUT_LABEL, mRes.getString(R.string.call_out_label));
        mFilterConfig.put(CONFIG.LISTGAME_ENABLE, "0");
        mFilterConfig.put(CONFIG.BANNER_CALL_HISTORY, "");
        mFilterConfig.put(CONFIG.DISCOVERY_ENABLE, "0");
        mFilterConfig.put(CONFIG.STRANGER_LOCATION_TIMEOUT, "24");
        mFilterConfig.put(CONFIG.BACKGROND_DEFAULT, "-");
        mFilterConfig.put(CONFIG.MORE_ITEM_DEEPLINK, "-");
        mFilterConfig.put(CONFIG.SHAKE_GAME_ON, "0");
        mFilterConfig.put(CONFIG.LIXI_ENABLE, "0");
        mFilterConfig.put(CONFIG.SONTUNG83_ENABLE, "0");
        mFilterConfig.put(CONFIG.AVNO_PAYMENT_WAPSITE, "");
        mFilterConfig.put(CONFIG.VIDEO_UPLOAD_USER, PREF_DEFAULT.PREF_DEF_VIDEO_UPLOAD_USER);
        mFilterConfig.put(CONFIG.VIDEO_UPLOAD_PASS, PREF_DEFAULT.PREF_DEF_VIDEO_UPLOAD_PASS);

        mFilterConfig.put(CONFIG.TAB_VIDEO_ENABLE, "0");
        mFilterConfig.put(CONFIG.SPOINT_ENABLE, "0");
        mFilterConfig.put(CONFIG.BANKPLUS_ENABLE, "0");
        mFilterConfig.put(CONFIG.WATCH_VIDEO_TOGETHER_ENABLE, "0");
        mFilterConfig.put(CONFIG.TAB_STRANGER_ENABLE, "0");
        mFilterConfig.put(CONFIG.REGISTER_VIP_CMD, "");
        mFilterConfig.put(CONFIG.REGISTER_VIP_CMD_CANCEL, "");
        mFilterConfig.put(CONFIG.SUGGEST_VIDEO_ENABLE, "");
        mFilterConfig.put(CONFIG.FIREBASE_WAKEUP_ENABLE, "");
        mFilterConfig.put(CONFIG.GET_LINK_LOCATION, "");
        mFilterConfig.put(CONFIG.WHITELIST_DEVICE, AppConstants.WHITE_LIST);
        if (Config.Server.SERVER_TEST)
            mFilterConfig.put(CONFIG.PREFIX_CHANGENUMBER_TEST, "-");
        else
            mFilterConfig.put(CONFIG.PREFIX_CHANGENUMBER, "-");
    }

    /**
     * lay content config tu sharepref, neu chua luu thi lay trong resource
     */
    private void getAllConfigFromPref() {
        lastTimeGetConfig = mPref.getLong(CONFIG.TIMESTAMP_GET_CONFIG, -1L);
        hashMapContents = new HashMap<>();
        // get config from sharePref
        Set<String> keys = mFilterConfig.keySet();
        for (String key : keys) {
            String upgradeFeatures = mPref.getString(key, mFilterConfig.get(key));
            hashMapContents.put(key, upgradeFeatures);
        }

        //get Config from db uu tien
        hashMapConfigUser = new HashMap<>();
        ArrayList<KeyValueModel> listKeyValue = configUserDataSource.getAllConfigUser();
        if (listKeyValue != null && listKeyValue.size() > 0) {
            for (int i = 0; i < listKeyValue.size(); i++) {
                KeyValueModel keyValueModel = listKeyValue.get(i);
                hashMapConfigUser.put(keyValueModel.getKey(), keyValueModel.getValue());
            }
        }

        initState();
    }

    /**
     * lay content config tu api
     */
    public void getConfigFromServer(boolean isForceToGet) {
        if (isForceToGet) {     //bat phai update
            mPref.edit().putBoolean(CONFIG.PREF_FORCE_GET_CONFIG_NOT_DONE, true).apply();
        } else {        //neu chua update thanh cong thi bat update
            if (mPref.getBoolean(CONFIG.PREF_FORCE_GET_CONFIG_NOT_DONE, false)) {
                isForceToGet = true;
            }
        }
        // neu thoi gian get config lan cuoi khong phai trong ngay hom nay thi moi get
        if (TimeHelper.checkTimeInDay(lastTimeGetConfig) && !isForceToGet) {
            return;
        }
        ReengAccountBusiness accountBusiness = mApplication.getReengAccountBusiness();
        if (accountBusiness.isValidAccount()
                && NetworkHelper.isConnectInternet(mApplication)) {
            // goi api get config
            String numberEncode = HttpHelper.EncoderUrl(accountBusiness.getJidNumber());
            long currentTime = System.currentTimeMillis();
            String language = accountBusiness.getDeviceLanguage();
            StringBuilder sb = new StringBuilder().
                    append(accountBusiness.getJidNumber()).
                    append(AppConstants.HTTP.CLIENT_TYPE_ANDROID).
                    append(language).
                    append(accountBusiness.getRegionCode()).
                    append(accountBusiness.getToken()).
                    append(currentTime);
            String dataEncrypt = HttpHelper.EncoderUrl(HttpHelper.encryptDataV2(mApplication, sb.toString(),
                    accountBusiness.getToken()));
            String url = String.format(
                    UrlConfigHelper.getInstance(mApplication).getUrlConfigOfFile(Config.UrlEnum.GET_CONTENT_CONFIG),
                    numberEncode,
                    AppConstants.HTTP.CLIENT_TYPE_ANDROID,
                    language,
                    accountBusiness.getRegionCode(),
                    String.valueOf(currentTime),
                    dataEncrypt);
            Log.f(TAG, "url: " + url);
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String decryptResponse = HttpHelper.decryptResponse(response,
                                    mApplication.getReengAccountBusiness().getToken());
                            Log.f(TAG, "onResponse: decrypt: " + decryptResponse);
                            int errorCode = -1;
                            try {
                                JSONObject responseObject = new JSONObject(decryptResponse);
                                errorCode = responseObject.optInt(AppConstants.HTTP.REST_CODE, -1);
                                if (errorCode == HTTPCode.E200_OK) {
                                    mPref.edit().putBoolean(CONFIG.PREF_FORCE_GET_CONFIG_NOT_DONE, false).apply();
                                    parserJsonConfig(responseObject);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Exception", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "VolleyError", error);
                }
            }
            );
            VolleyHelper.getInstance(mApplication).addRequestToQueue(request, TAG, false);
        }
    }

    private void parserJsonConfig(JSONObject responseObject) throws JSONException {
        // thoi gian get config tu sv
        boolean enableOnMediaLastConfig = isEnableOnmedia;
        boolean enableTabVideoLastConfig = isEnableTabVideo;
        boolean enableTabStrangerLastConfig = isEnableTabStranger;

        lastTimeGetConfig = TimeHelper.getCurrentTime();
        mPref.edit().putLong(CONFIG.TIMESTAMP_GET_CONFIG, lastTimeGetConfig).apply();
        if (responseObject.has("config")) {
            JSONArray arrayConfig = responseObject.getJSONArray("config");
            if (arrayConfig != null && arrayConfig.length() > 0) {
                int lengthArray = arrayConfig.length();
                for (int i = 0; i < lengthArray; i++) {
                    JSONObject object = arrayConfig.getJSONObject(i);
                    // get keys trong object, sau do lay values theo keys
                    // luu vao pref
                    Iterator iter = object.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        String value = null;
                        try {
                            value = object.getString(key);
                        } catch (Exception jex) {
                            Log.e(TAG, "Exception", jex);
                        } finally {
                            Log.i(TAG, key + " - " + value);
                            setStringConfigToPref(key, value);
                        }
                    }
                }
                initState();
            }
        }
        //set lai SMS OUT Prefix khi load thanh cong config
        PhoneNumberHelper.getInstant().setSmsOutPrefixes(getContentConfigByKey(CONFIG.SMSOUT_PREFIX_AVAILABLE));
        if (mContactBusiness.isContactReady() && mContactBusiness.isInitArrayListReady()) {
            mContactBusiness.initArrayListPhoneNumber();
        }
        boolean enableOnMediaNewConfig = isEnableOnMedia();
        boolean enableTabVideoNewConfig = isEnableTabVideo();
        boolean enableTabStrangerNewConfig = isEnableTabStranger();
        if (enableOnMediaLastConfig != enableOnMediaNewConfig
                || enableTabVideoLastConfig != enableTabVideoNewConfig
                || enableTabStrangerLastConfig != enableTabStrangerNewConfig) {
            Log.i(TAG, "onGetConfigTabChanged");
            ListenerHelper.getInstance().onConfigTabChanged();
        }
    }

    private void setStringConfigToPref(String key, String content) {
        if (!TextUtils.isEmpty(content)) {
            mPref.edit().putString(key, content).apply();
            hashMapContents.put(key, content);
        }
        //set maximageupload
        String maxSize = getContentConfigByKey(AppConstants.PREFERENCE.CONFIG.IMAGE_PROFILE_MAX_SIZE);
        if (!TextUtils.isEmpty(maxSize)) {
            int maxAlbumImages = Integer.valueOf(maxSize);
            mApplication.getImageProfileBusiness().setMaxAlbumImages(maxAlbumImages);
        }
    }

    public String getContentConfigByKey(String key) {
        if (hashMapContents == null) {// truong hop loi goi  config lai
            getAllConfigFromPref();
        }

        //Check config cho tung user truoc
        if (hashMapConfigUser != null) {
            if (hashMapConfigUser.containsKey(key))
                return hashMapConfigUser.get(key);
        }

        return hashMapContents.get(key);
    }

    //////////////////////////
    //// config banner////////
    //////////////////////////
//    public ArrayList<BannerMocha> getListBanners() {
//        return mListBanners;
//    }
//
//    /*public ArrayList<BannerMocha> getListBannersCall() {
//        return mListBannersCall;
//    }*/
//
//    public void processIncomingBannerMessage(ReengEventPacket receivedPacket) {
//        boolean needNotifyBannerCall = false;
//        String action = receivedPacket.getBannerAction();
//        ArrayList<JSONObject> jsonBanners = receivedPacket.getBannerJson();
//        ArrayList<BannerMocha> banners = new ArrayList<>();
////        ArrayList<BannerMocha> bannersCall = new ArrayList<>();
//        if (TextUtils.isEmpty(action)) {//add or remove all
//            if (jsonBanners != null && !jsonBanners.isEmpty()) {
//                banners = new ArrayList<>();
//                for (JSONObject item : jsonBanners) {
//                    BannerMocha banner = new BannerMocha();
//                    banner.parserFromJSON(item);
//                    if (banner.isValid() && !banner.isExpired()) {
//                        /*if (BannerMocha.TARGET_TAB_CALL.equals(banner.getTarget())) {
//                            bannersCall.add(banner);
//                            needNotifyBannerCall = true;
//                        } else*/
//                        banners.add(banner);
//                    } else {
//                        Log.d(TAG, "banner not valid or expired: " + banner.toString());
//                    }
//                }
//            }
//
//        } else if ("banner_off".equals(action) && jsonBanners != null) {// off list banner theo id
//            banners = new ArrayList<>(mListBanners);
//            for (JSONObject item : jsonBanners) {
//                BannerMocha banner = findBannerByBannerId(item.optString("id", null), banners);
//                if (banner != null) {
//                    banners.remove(banner);
//                }
//            }
//
//            /*bannersCall = new ArrayList<>(mListBannersCall);
//            for (JSONObject item : jsonBanners) {
//                BannerMocha banner = findBannerByBannerId(item.optString("id", null), bannersCall);
//                if (banner != null) {
//                    bannersCall.remove(banner);
//                    needNotifyBannerCall = true;
//                }
//            }*/
//        } else {
//            Log.d(TAG, "ngoại lệ không xử lý");
//        }
//
////        if (!needNotifyBannerCall) {
//        mListBanners = banners;
//        ListenerHelper.getInstance().onConfigBannerChanged(mListBanners);
//        saveConfigBannerToPref(mListBanners, PREFERENCE.PREF_CONFIG_BANNER_CONTENT);
//        /*} else {
//            mListBannersCall = bannersCall;
//            ListenerHelper.getInstance().onConfigBannerCallChanged(mListBannersCall);
//            saveConfigBannerToPref(mListBannersCall, PREFERENCE.PREF_CONFIG_BANNER_CALL);
//        }*/
//
//
//    }
//
//    private BannerMocha findBannerByBannerId(String bannerId, ArrayList<BannerMocha> list) {
//        if (TextUtils.isEmpty(bannerId) || list == null || list.isEmpty()) {
//            return null;
//        }
//        for (BannerMocha item : list) {
//            if (bannerId.equals(item.getId())) {
//                return item;
//            }
//        }
//        return null;
//    }
//
//    public void saveConfigBannerToPref(ArrayList<BannerMocha> listBanners, String key) {
//        if (listBanners == null || listBanners.isEmpty()) {
//            mPref.edit().putString(key, "").apply();
//        } else {
//            try {
//                Gson gson = new Gson();
//                JsonArray jsonArray = new JsonArray();
//                for (BannerMocha item : listBanners) {
//                    jsonArray.add(new JsonParser().parse(gson.toJson(item)));
//                }
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.add("listBanner", jsonArray);
//                mPref.edit().putString(key, jsonObject.toString()).apply();
//            } catch (Exception e) {
//                mPref.edit().putString(key, "").apply();
//                Log.e(TAG, "Exception", e);
//            }
//        }
//    }
//
//    public void initConfigBanner() {
//        String contentPref = mPref.getString(PREFERENCE.PREF_CONFIG_BANNER_CONTENT, "");
//        if (TextUtils.isEmpty(contentPref)) {
//            mListBanners = null;
//        } else {
//            try {
//                JsonElement jsonElement = new JsonParser().parse(contentPref);
//                JsonObject object = jsonElement.getAsJsonObject();
//                mListBanners = parserBannerMocha(object, PREFERENCE.PREF_CONFIG_BANNER_CONTENT);
//                ListenerHelper.getInstance().onConfigBannerChanged(mListBanners);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception", e);
//                mListBanners = null;
//            }
//        }
//
//        /*String contentCallPref = mPref.getString(PREFERENCE.PREF_CONFIG_BANNER_CALL, "");
//        if (TextUtils.isEmpty(contentCallPref)) {
//            mListBannersCall = null;
//        } else {
//            try {
//                JsonElement jsonElement = new JsonParser().parse(contentPref);
//                JsonObject object = jsonElement.getAsJsonObject();
//                mListBannersCall = parserBannerMocha(object, PREFERENCE.PREF_CONFIG_BANNER_CALL);
//                ListenerHelper.getInstance().onConfigBannerCallChanged(mListBannersCall);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception", e);
//                mListBannersCall = null;
//            }
//        }*/
//        /*mListBanners = new ArrayList<>();
//        BannerMocha bannerMocha = new BannerMocha("1", "tesst", "desc test", "icon", "link", 1, true, -1);
//        mListBanners.add(bannerMocha);*/
//    }

//    private ArrayList<BannerMocha> parserBannerMocha(JsonObject object, String key) throws JsonSyntaxException {
//        ArrayList<BannerMocha> listBanners = new ArrayList<>();
//        boolean isChanged = false;
//        if (object.has("listBanner")) {
//            JsonArray jsonArray = object.getAsJsonArray("listBanner");
//            if (jsonArray != null && jsonArray.size() > 0) {
//                int lengthArray = jsonArray.size();
//                Gson gson = new Gson();
//                for (int i = 0; i < lengthArray; i++) {
//                    BannerMocha item = gson.fromJson(jsonArray.get(i), BannerMocha.class);
//                    if (item.isExpired() || !item.isValid()) {
//                        isChanged = true;
//                    } else {
//                        listBanners.add(item);
//                    }
//                }
//            }
//        }
//        if (isChanged)// co banner expired thi save lai ds moi
//            saveConfigBannerToPref(listBanners, key);
//        return listBanners;
//    }


    private void initState() {
        // crbtState
        isEnableCrbt = "1".equals(getContentConfigByKey(CONFIG.PREF_CRBT_ENABLE));
        // inAppEnable
        isEnableInApp = "1".equals(getContentConfigByKey(CONFIG.INAPP_ENABLE));
        isEnableLuckyWheel = "1".equals(getContentConfigByKey(CONFIG.LUCKY_WHEEL_ENABLE));
        isEnableQR = "1".equals(getContentConfigByKey(CONFIG.QR_SCAN_ENABLE));
        isEnableGuestBook = "1".equals(getContentConfigByKey(CONFIG.GUEST_BOOK_ENABLE));
        isEnableListGame = "1".equals(getContentConfigByKey(CONFIG.LISTGAME_ENABLE));
        isEnableDiscovery = "1".equals(getContentConfigByKey(CONFIG.DISCOVERY_ENABLE));
        isEnableOnmedia = "1".equals(getContentConfigByKey(CONFIG.CONFIG_ONMEDIA_ON));
        backgroundDefault = getContentConfigByKey(CONFIG.BACKGROND_DEFAULT);
        int version_code = TextHelper.parserIntFromString(getContentConfigByKey(CONFIG.VERSION_CODE_APP), BuildConfig.VERSION_CODE);
        isUpdate = BuildConfig.VERSION_CODE < version_code;
        parserMoreItemDeepLink(getContentConfigByKey(CONFIG.MORE_ITEM_DEEPLINK));
        isShakeGame = "1".equals(getContentConfigByKey(CONFIG.SHAKE_GAME_ON));
        isEnableLixi = "1".equals(getContentConfigByKey(CONFIG.LIXI_ENABLE));
        isEnableSonTung83 = "1".equals(getContentConfigByKey(CONFIG.SONTUNG83_ENABLE));
        isEnableTabVideo = "1".equals(getContentConfigByKey(CONFIG.TAB_VIDEO_ENABLE));
        isEnableSpoint = "1".equals(getContentConfigByKey(CONFIG.SPOINT_ENABLE));
        isEnableBankplus = "1".equals(getContentConfigByKey(CONFIG.BANKPLUS_ENABLE));
        isEnableWatchVideoTogether = "1".equals(getContentConfigByKey(CONFIG.WATCH_VIDEO_TOGETHER_ENABLE));
        isEnableTabStranger = "1".equals(getContentConfigByKey(CONFIG.TAB_STRANGER_ENABLE));
        isEnableSuggestVideo = "1".equals(getContentConfigByKey(CONFIG.SUGGEST_VIDEO_ENABLE));
        isEnableWakeUpFirebase = "1".equals(getContentConfigByKey(CONFIG.FIREBASE_WAKEUP_ENABLE));
        if (Config.Server.SERVER_TEST)
            regexPrefixChangeNumb = getContentConfigByKey(CONFIG.PREFIX_CHANGENUMBER_TEST);
        else
            regexPrefixChangeNumb = getContentConfigByKey(CONFIG.PREFIX_CHANGENUMBER);
        mPatternPrefixChangeNumb = null;
        int lenght = PrefixChangeNumberHelper.PREFIX_NEW.length;
        for (int i = 0; i < lenght; i++) {
            hashChangeNumberOld2New.put(PrefixChangeNumberHelper.PREFIX_OLD[i], PrefixChangeNumberHelper.PREFIX_NEW[i]);
            hashChangeNumberNew2Old.put(PrefixChangeNumberHelper.PREFIX_NEW[i], PrefixChangeNumberHelper.PREFIX_OLD[i]);
        }
        mApplication.getReengAccountBusiness().processChangeNumber();
    }

    public boolean isEnableCrbt() {
        return isEnableCrbt;
    }

    public boolean isEnableInApp() {
        return isEnableInApp;
    }

    public boolean isEnableLuckyWheel() {
        return isEnableLuckyWheel;
    }

    public boolean isEnableQR() {
        return isEnableQR;
    }

    public boolean isEnableGuestBook() {
        return isEnableGuestBook;
    }

    public boolean isEnableListGame() {
        return isEnableListGame;
    }

    public boolean isEnableDiscovery() {
        return isEnableDiscovery;
    }

    public boolean isEnableOnMedia() {
        return isEnableOnmedia;
    }

    public String getBackgroundDefault() {
        //return "http://hlvip2.mocha.com.vn/hlmocha92/media3/banner/bg_default_newyear.jpg";
        //return "-";
        return backgroundDefault;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

//    private void parserMoreItemDeepLink(String input) {
//        //input="{\"icon\":\"http://hlvip2.mocha.com.vn/hlmocha92/media3/banner/ic_home_shake.png\",\"title\":\"Lắc để nhận quà\",\"deeplink\":\"mocha://shake\",\"isNew\":false}";
//        //input = "{\"icon\":\"http://hlvip2.mocha.com.vn/hlmocha92/media3/banner/ic_home_sontung.png\",\"title\":\"Săn quà cùng Sơn Tùng\",\"deeplink\":\"mocha://sontung83\",\"isNew\":false}";
//        Log.d(TAG, "[check config] " + input);
//        if (TextUtils.isEmpty(input) || "-".equals(input)) {
//            moreItemDeepLink = null;
//        } else {
//            try {
//                JSONObject jsonObject = new JSONObject(input);
//                moreItemDeepLink = new MoreItemDeepLink(jsonObject.optString("icon"),
//                        jsonObject.optString("title"),
//                        jsonObject.optString("deeplink"),
//                        jsonObject.optBoolean("isNew"));
//            } catch (Exception e) {
//                Log.e(TAG, "Exception", e);
//                moreItemDeepLink = null;
//            }
//        }
//        mApplication.getReengAccountBusiness().onMoreItemChanged();
//    }
//
//    public MoreItemDeepLink getMoreItemDeepLink() {
//        return moreItemDeepLink;
//    }

    public boolean isShakeGame() {
        return isShakeGame;
    }

    public boolean isEnableLixi() {
        return isEnableLixi;
    }

    public boolean isEnableSonTung83() {
        return isEnableSonTung83;
    }

    public boolean isEnableTabVideo() {
        return isEnableTabVideo;
    }

    public boolean isEnableTabCall() {
//        return mApplication.getReengAccountBusiness().isTabCallEnable();
        return true;
    }

    public boolean isEnableTabStranger() {
        return isEnableTabStranger;
    }

    public boolean isEnableSpoint() {
        return isEnableSpoint;
    }

    public boolean isEnableBankplus() {
        return isEnableBankplus;
    }

    public boolean isEnableWatchVideoTogether() {
        return isEnableWatchVideoTogether;
    }

    public boolean isEnableSuggestVideo() {
        return isEnableSuggestVideo;
    }

    public boolean isEnableWakeUpFirebase() {
        return isEnableWakeUpFirebase;
    }

    //Xu ly config theo tung user
    public void processSetConfig(ReengMessagePacket packet) {
        String key = packet.getKeyValueConfig().getKey();
        String value = packet.getKeyValueConfig().getValue();

        boolean enableOnMediaLastConfig = false;
        boolean enableTabVideoLastConfig = false;
        boolean enableTabStrangerLastConfig = false;

        //Check enable onmedia truoc day
        if (CONFIG.CONFIG_ONMEDIA_ON.equals(key)) {
            enableOnMediaLastConfig = isEnableOnMedia();
        } else if (CONFIG.TAB_VIDEO_ENABLE.equals(key)) {
            enableTabVideoLastConfig = isEnableTabVideo();
        } else if (CONFIG.TAB_STRANGER_ENABLE.equals(key)) {
            enableTabStrangerLastConfig = isEnableTabStranger();
        }

        if (configUserDataSource != null) {
            if (!TextUtils.isEmpty(value)) {
                if (hashMapConfigUser != null && hashMapConfigUser.containsKey(key))//Neu co trong db roi thi update
                {
                    configUserDataSource.updateConfigUser(new KeyValueModel(key, value));
                } else//Chua co thi insert config
                {
                    configUserDataSource.insertConfigUser(new KeyValueModel(key, value));
                }
                hashMapConfigUser.put(key, value);
            }
            initState();


            if ((CONFIG.CONFIG_ONMEDIA_ON.equals(key) && enableOnMediaLastConfig != isEnableOnMedia())
                    || (CONFIG.TAB_VIDEO_ENABLE.equals(key) && enableTabVideoLastConfig != isEnableTabVideo())
                    || (CONFIG.TAB_STRANGER_ENABLE.equals(key) && enableTabStrangerLastConfig != isEnableTabStranger())) {
                Log.i(TAG, "onConfigTabChanged");
                ListenerHelper.getInstance().onConfigTabChanged();
            }
        }
    }

    public void processDelConfig(ReengMessagePacket packet) {
        String key = packet.getKeyValueConfig().getKey();
        String value = packet.getKeyValueConfig().getValue();

        boolean enableOnMediaLastConfig = false;
        boolean enableTabVideoLastConfig = false;
        boolean enableTabStrangerLastConfig = false;

        //Check enable onmedia truoc day
        if (CONFIG.CONFIG_ONMEDIA_ON.equals(key)) {
            enableOnMediaLastConfig = isEnableOnMedia();
        } else if (CONFIG.TAB_VIDEO_ENABLE.equals(key)) {
            enableTabVideoLastConfig = isEnableTabVideo();
        } else if (CONFIG.TAB_STRANGER_ENABLE.equals(key)) {
            enableTabStrangerLastConfig = isEnableTabStranger();
        }

        if (configUserDataSource != null) {
            configUserDataSource.deleteConfigUserByKey(key);
            hashMapConfigUser.remove(key);
            initState();

            if ((key.equals(CONFIG.CONFIG_ONMEDIA_ON) && enableOnMediaLastConfig != isEnableOnMedia())
                    || (CONFIG.TAB_VIDEO_ENABLE.equals(key) && enableTabVideoLastConfig != isEnableTabVideo())
                    || (CONFIG.TAB_STRANGER_ENABLE.equals(key) && enableTabStrangerLastConfig != isEnableTabStranger())) {
                Log.i(TAG, "onConfigTabChanged");
                ListenerHelper.getInstance().onConfigTabChanged();
            }
        }
    }

//    private void initListStickyBanner() {
//        String contentPref = mPref.getString(PREFERENCE.PREF_LIST_STICKY_BANNER, "");
//        if (TextUtils.isEmpty(contentPref)) {
//            listStickyBanner = null;
//        } else {
//            listStickyBanner = new Gson().fromJson(contentPref,
//                    new TypeToken<List<PinMessage>>() {
//                    }.getType());
//
//        }
//    }
//
//    public void processConfigStickyBanner(ReengMessagePacket packet) {
//        PinMessage pinMessage = getPinMessageFromPacket(packet);
//        if (listStickyBanner == null)
//            listStickyBanner = new ArrayList<>();
//        if (!listStickyBanner.isEmpty()) {
//            for (PinMessage sticky : listStickyBanner) {
//                if (sticky.getThreadType() == pinMessage.getThreadType()) {
//                    listStickyBanner.remove(sticky);
//                    break;
//                }
//            }
//        }
//        listStickyBanner.add(pinMessage);
//        mPref.edit().putString(PREFERENCE.PREF_LIST_STICKY_BANNER, new Gson().toJson(listStickyBanner)).apply();
//        ListenerHelper.getInstance().onConfigStickyBannerChanged();
//    }
//
//    public void removeConfigStickyBanner(PinMessage pinMessage) {
//        listStickyBanner.remove(pinMessage);
//        mPref.edit().putString(PREFERENCE.PREF_LIST_STICKY_BANNER, new Gson().toJson(listStickyBanner)).apply();
//        ListenerHelper.getInstance().onConfigStickyBannerChanged();
//    }
//
//    private PinMessage getPinMessageFromPacket(ReengMessagePacket packet) {
//        PinMessage pinMessage = new PinMessage();
//        pinMessage.setContent(packet.getBody());
//        pinMessage.setImage(packet.getPinMsgImg());
//        pinMessage.setType(PinMessage.TypePin.TYPE_SPECIAL_THREAD.VALUE);
//        pinMessage.setTarget(packet.getPinMsgTarget());
//        pinMessage.setThreadType(packet.getPinThreadType());
//        pinMessage.setExpired(packet.getPinExpired());
//        pinMessage.setTitle(packet.getPinMsgTitle());
//        return pinMessage;
//    }
//
//    public PinMessage getStickyBannerFromThreadType(ThreadMessage threadMessage) {
//        if (threadMessage == null)
//            return null;
//        if (!threadMessage.isStranger()
//                && threadMessage.getThreadType() != ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT
//                && threadMessage.getThreadType() != ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT)
//            return null;
//        if (listStickyBanner == null || listStickyBanner.isEmpty()) return null;
//        for (PinMessage pinMessage : listStickyBanner) {
//            int type = pinMessage.getThreadType();
//            if (type == PinMessage.SpecialThread.THREAD_STRANGER.VALUE) {
//                if (threadMessage.isStranger())
//                    return pinMessage;
//            } else if (type == PinMessage.SpecialThread.THREAD_CONTACT.VALUE) {
//                if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT)
//                    return pinMessage;
//            } else if (type == PinMessage.SpecialThread.THREAD_GROUP.VALUE) {
//                if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT)
//                    return pinMessage;
//            }
//        }
//        return null;
//    }

    public int countTabHome() {
        int countTab = 2;
        if (isEnableTabVideo())
            countTab++;
        if (isEnableTabStranger())
            countTab++;
        if (isEnableOnMedia())
            countTab++;
        return countTab;
    }

    public HashMap<String, String> getHashChangeNumberOld2New() {
        return hashChangeNumberOld2New;
    }

    public boolean needChangePrefix(String jid) {
        if (TextUtils.isEmpty(jid)) return false;
        if (jid.length() != 11) return false;
        if (isDisableChangePrefix()) return false;
        if (mPatternPrefixChangeNumb == null)
            mPatternPrefixChangeNumb = Pattern.compile(regexPrefixChangeNumb);

        return mPatternPrefixChangeNumb.matcher(jid).find();
    }

    public boolean isDisableChangePrefix() {
        if (TextUtils.isEmpty(regexPrefixChangeNumb) || "-".equals(regexPrefixChangeNumb))
            return true;
        return false;
    }

    public HashMap<String, String> getHashChangeNumberNew2Old() {
        return hashChangeNumberNew2Old;
    }

//    public interface OnConfigBannerChangeListener {
//        void onConfigBannerChanged(ArrayList<BannerMocha> listBanners);
//
//        void onConfigBannerCallChanged(ArrayList<BannerMocha> listBannersCall);
//    }

    public interface OnConfigChangeListener {
        void onConfigStickyBannerChanged();

        void onConfigTabChange();
    }
}