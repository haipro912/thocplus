package com.vttm.mochaplus.feature.business;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.data.db.utils.DbUtils;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.interfaces.VipInfoChangeListener;
import com.vttm.mochaplus.feature.helper.PermissionHelper;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.helper.encrypt.RSAEncrypt;
import com.vttm.mochaplus.feature.model.ReengAccount;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;

import org.jivesoftware.smack.packet.Presence;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ThaoDV on 6/6/14.
 */
public class ReengAccountBusiness {
    private static final String TAG = ReengAccountBusiness.class.getSimpleName();
    private CopyOnWriteArrayList<VipInfoChangeListener> vipInfoChangeListeners = new CopyOnWriteArrayList<>();
    private ReengAccount mCurrentAccount;
    private ApplicationController mApplication;
    private DataManager dataManager;
    private SharedPreferences mPref;
    private boolean isShowForceDialog = false;
    private boolean isValidRevision = true;
    private boolean isReady = false;
    private String msgForceUpdate;
    private String urlForceUpdate;
    private String countryCode;
    private String currentLanguage;
    private String settingLanguage;
    private int vipInfo;
    private boolean isVip = false;
    private boolean isCBNV = false;
    private int callEnable = -1;
    private int smsIn = -1;
    private int callOutState = 0;
    private boolean avnoEnable = false;
    private boolean tabCallEnable = false;
    private boolean isEnableCallOut = false;
    private String mochaApi = "";
    // location stranger
    private String locationId;

    private boolean isEnableUploadLog;

    public ReengAccountBusiness(ApplicationController app) {
        mApplication = app;
        this.mPref = mApplication.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
        isReady = false;
        this.settingLanguage = mPref.getString(AppConstants.PREFERENCE.PREF_SETTING_LANGUAGE_MOCHA, getDeviceLanguage());
    }

    public void init() {
        dataManager = mApplication.getDataManager();
        // get account from database
        initAccountFromDatabase();
        initCountryCode();
        setCurrentLanguage();// set current khi khoi tao app
        this.vipInfo = mPref.getInt(AppConstants.PREFERENCE.PREF_MOCHA_USER_VIP_INFO, 0);
        this.isVip = vipInfo == 1;
        this.isCBNV = mPref.getBoolean(AppConstants.PREFERENCE.PREF_MOCHA_USER_CBNV, false);
        this.callEnable = mPref.getInt(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_CALL, -1);
        this.smsIn = mPref.getInt(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_SMS_IN, -1);
        this.callOutState = mPref.getInt(AppConstants.PREFERENCE.PREF_CALL_OUT_ENABLE_STATE, 0);
        this.locationId = mPref.getString(AppConstants.PREFERENCE.PREF_MY_STRANGER_LOCATION, "-1");
        this.avnoEnable = mPref.getBoolean(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_AVNO, false);
        this.tabCallEnable = mPref.getBoolean(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_TAB_CALL, false);
        isReady = true;
        //them state callout =-2 enable
        this.isEnableCallOut = callOutState == 1 || callOutState == -1 || callOutState == -2; // TODO sv chưa chạy
        // tron luong goi cuoc call
        // out
        //this.isEnableCallOut = callOutState == 1;
        String rsaEncrypt = RSAEncrypt.getInStance(mApplication).encrypt(mApplication, getJidNumber());
        AppLogger.d(TAG, "rsaEncrypt: " + rsaEncrypt);
        this.isEnableUploadLog = mPref.getBoolean(AppConstants.PREFERENCE.PREF_ENABLE_UPLOAD_LOG, false);
    }

//    public void setEnableUploadLog(boolean enable) {
//        AppLogger.i(TAG, "setEnableUploadLog: " + enable);
//        isEnableUploadLog = enable;
//        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_ENABLE_UPLOAD_LOG, enable).apply();
//        if (enable) {
//            SignalStrengthHelper.getInstant(mApplication).startListener();
//        } else {
//            SignalStrengthHelper.getInstant(mApplication).stopListener();
//        }
//    }

    public boolean isEnableUploadLog() {
        return isEnableUploadLog;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isShowForceDialog() {
        return isShowForceDialog;
    }

    public ReengAccount getCurrentAccount() {
        if (!isReady) {
            initAccountFromDatabase();
            initCountryCode();
            isReady = true;
        }
        return mCurrentAccount;
    }

    public Phonenumber.PhoneNumber getPhoneNumberProtocol() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return PhoneNumberHelper.getInstant().getPhoneNumberProtocol(mApplication.getPhoneUtil(),
                    account.getJidNumber(), account.getRegionCode());
        } else {
            return null;
        }
    }

    public void setShowForceDialog(boolean isShowForceDialog) {
        this.isShowForceDialog = isShowForceDialog;
    }

    public boolean isValidRevision() {
        return isValidRevision;
    }

    public void setValidRevision(boolean isValidRevision) {
        this.isValidRevision = isValidRevision;
    }

    public String getMsgForceUpdate() {
        return msgForceUpdate;
    }

    public void setMsgForceUpdate(String msgForceUpdate) {
        this.msgForceUpdate = msgForceUpdate;
    }

    public String getUrlForceUpdate() {
        return urlForceUpdate;
    }

    public void setUrlForceUpdate(String urlForceUpdate) {
        this.urlForceUpdate = urlForceUpdate;
    }

    public boolean isValidAccount() {
        getCurrentAccount();
        if (mCurrentAccount == null || !mCurrentAccount.isActive()) {
            return false;
        }
        return !TextUtils.isEmpty(mCurrentAccount.getToken());
    }

    public void createReengAccountBeforeLogin(Context context, String numberJid, String regionCode) {
        if (TextUtils.isEmpty(regionCode)) {
            regionCode = "VN";// mac dinh la vn
        }
        mCurrentAccount = new ReengAccount();
        mCurrentAccount.setNumberJid(numberJid);
        mCurrentAccount.setRegionCode(regionCode);
        mCurrentAccount.setActive(false);
        mCurrentAccount.setPermission(1);//00000001//show historyStranger, show birthday,
        initCountryCode();
        isReady = true;
        try {
            dataManager.insertAccount(DbUtils.clone(mCurrentAccount));
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void updateReengAccount(ReengAccount account) {
        mCurrentAccount = account;

        dataManager.updateAccount(DbUtils.clone(account));
        initCountryCode();
    }

    public void initAccountFromDatabase() {
//        mCurrentAccount = DbUtils.clone(dataManager.getAccount());
//        // co tai khoan ma truong region code null
//        if (mCurrentAccount != null && TextUtils.isEmpty(mCurrentAccount.getRegionCode())) {
//            mCurrentAccount.setRegionCode("VN");// mac dinh vn
//            updateReengAccount(mCurrentAccount);// cap nhat db
//        }
    }

    //get user number lay so de dang nhap
    public String getJidNumber() {
        if (mCurrentAccount != null) {
            return mCurrentAccount.getJidNumber();
        } else {
            return null;
        }
    }

    public String getUserName() {
        if (mCurrentAccount != null) {
            return mCurrentAccount.getName();
        } else {
            return null;
        }
    }

    public String getRegionCode() {
        if (mCurrentAccount != null) {
            if (!TextUtils.isEmpty(mCurrentAccount.getRegionCode())) {
                return mCurrentAccount.getRegionCode();
            } else {
                return "VN";// mac dinh la vn
            }
        } else {
            return "VN";// mac dinh la vn
        }
    }

    //get token
    public String getToken() {
        if (mCurrentAccount != null) {
            return mCurrentAccount.getToken();
        } else {
            return null;
        }
    }

    //get last changeAvatar
    public String getLastChangeAvatar() {
        if (mCurrentAccount != null) {
            return mCurrentAccount.getLastChangeAvatar();
        } else {
            return null;
        }
    }

    public void removeFileFromProfileDir() {
        File profileDir = new File(Config.Storage.REENG_STORAGE_FOLDER + Config.Storage.IMAGE_COMPRESSED_FOLDER);
        File list[] = profileDir.listFiles();
        if (list == null)
            return;
        for (File fileName : list) {
            if (fileName.exists()) {
                fileName.delete();
            }
        }
    }

//    public void deactivateAccount(Context context) throws XMPPException {
//        //send packet deactive
//        XMPPManager mXMPPManager = mApplication.getXmppManager();
//        Presence presence = new Presence(Presence.Type.unavailable);
//        presence.setStatus("unregister");
//        presence.setSubType(Presence.SubType.deactive);
//        mXMPPManager.sendPacketNoWaitResponse(presence);
//        clearDataWhenDeactive(mApplication);
//        clearPreference(context);
//        lockAccount(mApplication);
//    }

    /**
     * khoa account do sai token
     *
     * @param context
     */
    public void lockAccount(ApplicationController context) {
        if (mCurrentAccount == null) return;
        mCurrentAccount.setActive(false);
        mCurrentAccount.setToken("");
        dataManager.updateAccount(DbUtils.clone(mCurrentAccount));
        clearDataWhenDeactive(context);
//        clearContactData(mApplication);
        clearPreference(context);
    }

    private void clearDataWhenDeactive(ApplicationController application) {
        //        ko xoa bang account nua --> tranh truong hop deactive --> vao nhan dien
        //        ReengAccountDataSource raDataSource = ReengAccountDataSource.getInstance(context);
        //        raDataSource.deleteAllTable();

//        application.getApplicationComponent().providesUtils().saveChannelInfo(new Channel());// clear cache channel
//        ReengMessageDataSource rmDataSource = ReengMessageDataSource.getInstance(application);
//        rmDataSource.deleteAllTable();
//        ThreadMessageDataSource tmDataSource = ThreadMessageDataSource.getInstance(application);
//        tmDataSource.deleteAllTable();
//        EventMessageDataSource eventMessageDataSource = EventMessageDataSource.getInstance(application);
//        eventMessageDataSource.deleteAllTable();
//        //del contact
//        clearContactData(application);
//        // del block
//        BlockDataSource blockDataSource = BlockDataSource.getInstance(application);
//        blockDataSource.deleteAllTable();
//        // del stranger contact
//        StrangerDataSource strangerDataSource = StrangerDataSource.getInstance(application);
//        strangerDataSource.deleteAllTable();
//        // del noncontact
//        NonContactDataSource nonContactDataSource = NonContactDataSource.getInstance(application);
//        nonContactDataSource.deleteAllTable();
//        //sticker recent
//        StickerDataSource stickerDataSource = StickerDataSource.getInstance(application);
//        stickerDataSource.deleteAllStickerRecent();
//        stickerDataSource.deleteAllTable();
//        //officer
//        OfficerDataSource officerDataSource = OfficerDataSource.getInstance(application);
//        officerDataSource.deleteAllTable();
//        //top song
//        TopSongDataSource topSongDataSource = TopSongDataSource.getInstance(application);
//        topSongDataSource.deleteAllTable();
//        // call history
//        CallHistoryDataSource callHistoryDataSource = CallHistoryDataSource.getInstance(application);
//        callHistoryDataSource.deleteAllTable();
//        callHistoryDataSource.deleteAllDetailTable();
//        //clear image profile
//        mApplication.getImageProfileBusiness().deleteAllImageProfile();
//        //clear config user
//        ConfigUserDataSource.getInstance(mApplication).deleteAllTable();
    }

//    private void clearContactData(ApplicationController application) {
//        ContactDataSource contactDataSource = ContactDataSource.getInstance(application);
//        contactDataSource.deleteAllTable();
//        NumberDataSource numberDataSource = NumberDataSource.getInstance(application);
//        numberDataSource.deleteAllTable();
//    }

    private void clearPreference(Context context) {
        try {
            SharedPreferences mPref = context.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.clear().apply();
        } catch (Exception e) {
            AppLogger.d(TAG, "clear preference" + e);
        }
    }

    public String getDeviceLanguage() {
        try {
            Locale localeDefault = Locale.getDefault();
            if (!TextUtils.isEmpty(localeDefault.getLanguage())) {
                return localeDefault.getLanguage();
            } else {
                return "vi";
            }
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
            return "vi";
        }
    }

    /**
     * get countryCode code user login
     *
     * @return
     */
    public String getCountryCode() {
        if (TextUtils.isEmpty(countryCode)) {
            initCountryCode();
        }
        return countryCode;
    }

    private void initCountryCode() {
        String regionCode = getRegionCode();
        if (TextUtils.isEmpty(regionCode)) {
            this.countryCode = "0";
        } else if ("VN".equals(regionCode)) {
            this.countryCode = "0";
        } else {
            int code = mApplication.getPhoneUtil().getCountryCodeForRegion(regionCode);
            this.countryCode = "+" + String.valueOf(code);
        }
    }

    public boolean isViettel() {
        return PhoneNumberHelper.getInstant().isViettelUser(getCurrentAccount());
    }

    public boolean isHaiti() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return "HT".equals(account.getRegionCode());
        } else {
            return false;
        }
    }

    public boolean isLaos() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return "LA".equals(account.getRegionCode());
        } else {
            return false;
        }
    }

    public boolean isVietnam() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return "VN".equals(account.getRegionCode());
        } else {
            return false;
        }
    }

    public boolean isTimorLeste() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return "TL".equals(account.getRegionCode());
        } else {
            return false;
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage() {
        this.currentLanguage = getDeviceLanguage();
    }

    public void updatePermissionBirthday(boolean isShow) {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            int permission = account.getPermission();
            if (isShow) {
                permission = permission | ContactConstant.PERMISSION_BIRTHDAY_ON;//00000001
            } else {
                permission = permission & ~ContactConstant.PERMISSION_BIRTHDAY_ON;//11111110
            }
            account.setPermission(permission);
            updateReengAccount(account);
        }
    }

    public void updatePermissionHideStrangerHistory(boolean isHide) {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            int permission = account.getPermission();
            if (isHide) {
                permission = permission | ContactConstant.PERMISSION_HIDE_STRANGER_HISTORY;//00000010
            } else {
                permission = permission & ~ContactConstant.PERMISSION_HIDE_STRANGER_HISTORY;//11111101
            }
            account.setPermission(permission);
            updateReengAccount(account);
        }
    }

    public boolean isPermissionShowBirthday() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            if (account.getPermission() == -1) {
                return true;
            } else if ((account.getPermission() & ContactConstant.PERMISSION_BIRTHDAY_ON) == ContactConstant.PERMISSION_BIRTHDAY_ON) {
                return true;
            }
        }
        return false;
    }

    public boolean isHideStrangerHistory() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            if (account.getPermission() == -1) {
                return false;
            } else if ((account.getPermission() & ContactConstant.PERMISSION_HIDE_STRANGER_HISTORY) == ContactConstant.PERMISSION_HIDE_STRANGER_HISTORY) {
                return true;
            }
        }
        return false;
    }

    public void updateUserInfoFromPresence(Presence presence) {
        String from = presence.getFrom().toString();
        from = from.split("@")[0].trim();
        if (TextUtils.isEmpty(from)) {
            return;
        }
//        ReengAccount account = getCurrentAccount();
//        if (account != null && from.equals(account.getJidNumber())) {// dung so cua minh
//            if (presence.getUserName() != null) {
//                account.setName(presence.getUserName());
//            }
//            if (presence.getUserBirthdayStr() != null) {
//                account.setBirthdayString(presence.getUserBirthdayStr());
//            }
//            if (presence.getStatus() != null) {
//                account.setStatus(presence.getStatus());
//            }
//            updateReengAccount(account);
//        }
    }

    public void updateUserPackageInfo(Presence presence) {
        String from = presence.getFrom().toString();
        from = from.split("@")[0].trim();
        if (TextUtils.isEmpty(from)) {
            return;
        }
//        ReengAccount account = getCurrentAccount();
//        if (account != null && from.equals(account.getJidNumber())) {// dung so cua minh
//            setVipInfo(presence.getVipInfo());
//        }
    }

    public boolean isDifferentCountry(String friendNumber) {
        if (TextUtils.isEmpty(friendNumber)) {
            return false;
        }
        String myCode = getCountryCode();
        return !friendNumber.startsWith(myCode);
    }

    public String getSettingLanguage() {
        return settingLanguage;
    }

    public String getSettingLanguageFromPref() {
        return mPref.getString(AppConstants.PREFERENCE.PREF_SETTING_LANGUAGE_MOCHA, getDeviceLanguage());
    }

    public void setSettingLanguage(String settingLanguage) {
        this.settingLanguage = settingLanguage;
        mPref.edit().putString(AppConstants.PREFERENCE.PREF_SETTING_LANGUAGE_MOCHA, settingLanguage).apply();
    }

    public boolean isVip() {
        return isVip;
    }

    public boolean isAvnoEnable() {
        return avnoEnable;
    }

    public boolean isTabCallEnable() {
        return tabCallEnable;
    }

    public void setVipInfo(int vipInfo) {
        AppLogger.d(TAG, "setVipInfo----: " + vipInfo);
        this.vipInfo = vipInfo;
        this.isVip = vipInfo == 1;
        mPref.edit().putInt(AppConstants.PREFERENCE.PREF_MOCHA_USER_VIP_INFO, vipInfo).apply();
        onVipInfoChanged();
    }

    public boolean isCBNV() {
        return isCBNV;
    }

    public boolean isCallEnable() {
        return callEnable == 1;
    }

    public boolean isSmsInEnable() {// mac dinh ==-1 sẽ là on
        return smsIn == 1;
    }

    public void setSSL(int ssl) {
        AppLogger.d(TAG, "ssl: " + ssl);
        if (!Config.Server.SERVER_TEST)
            mPref.edit().putInt(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_SSL, ssl).apply();
    }

    public int getCallOutState() {
        return callOutState;
        //return -1;
    }

    public boolean isEnableCallOut() {
        return isEnableCallOut;
        //return true;
    }

    public String getMochaApi() {
        return mochaApi;
    }

    public void setConfigFromServer(int vipInfo, int cbnv, int call, int ssl, int smsIn, int callOut, String
            avnoNumber, String mochaApi, int avnoEnable, int tabCallEnable) {
        AppLogger.d(TAG, "setConfigFromServer: " + vipInfo + " cbnv: " + cbnv
                + " call: " + call + " ssl: " + ssl + " " + "smsIn: " + smsIn + " avno: " + avnoNumber + " mochaapi: " +
                "" + mochaApi + " tabCallEnable: " + tabCallEnable);
        // co thông tin (!= -1) thì mới update
        SharedPreferences.Editor editor = mPref.edit();
        if (vipInfo != -1) {
            this.vipInfo = vipInfo;
            this.isVip = vipInfo == 1;
            editor.putInt(AppConstants.PREFERENCE.PREF_MOCHA_USER_VIP_INFO, vipInfo);
        }
        if (cbnv != -1) {
            this.isCBNV = cbnv == 1;
            editor.putBoolean(AppConstants.PREFERENCE.PREF_MOCHA_USER_CBNV, isCBNV);
        }
        if (call != -1) {
            this.callEnable = call;
            editor.putInt(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_CALL, callEnable);
        }
        if (smsIn != -1) {
            this.smsIn = smsIn;
            editor.putInt(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_SMS_IN, smsIn);
        }
        if (ssl != -1) {
            setSSL(ssl);
        }
        this.callOutState = callOut;
        //them state callout =-2 enable
        this.isEnableCallOut = callOutState == 1 || callOutState == -1 || callOutState == -2; // TODO sv chưa chạy
        // tron luong goi cuoc call
        // out
        //this.isEnableCallOut = callOutState == 1;
        editor.putInt(AppConstants.PREFERENCE.PREF_CALL_OUT_ENABLE_STATE, callOut);

        if (avnoNumber == null || "-1".equals(avnoNumber)) avnoNumber = "";
        if (getCurrentAccount() != null &&
                !avnoNumber.equals(getAVNONumber())) {
            updateAVNONumber(avnoNumber);
        }
        if (!TextUtils.isEmpty(mochaApi)) {
            this.mochaApi = mochaApi;
        }
        if (avnoEnable != -1) {
            this.avnoEnable = avnoEnable == 1;
            editor.putBoolean(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_AVNO, this.avnoEnable);
            onAVNOChanged();
        }

        if (tabCallEnable != -1) {
            if ((tabCallEnable == 1) != this.tabCallEnable) {
                this.tabCallEnable = tabCallEnable == 1;
                editor.putBoolean(AppConstants.PREFERENCE.PREF_MOCHA_ENABLE_TAB_CALL, this.tabCallEnable);
//                ListenerHelper.getInstance().onConfigTabChanged();
            }
        }


        editor.apply();
        if (callOut != -1) {
            onVipInfoChanged();
        }


    }

    public void addVipInfoChangeListener(VipInfoChangeListener listener) {
        if (!vipInfoChangeListeners.contains(listener)) {
            vipInfoChangeListeners.add(listener);
        }
    }

    public void removeVipInfoChangeListener(VipInfoChangeListener listener) {
        if (vipInfoChangeListeners.contains(listener)) {
            vipInfoChangeListeners.remove(listener);
        }
    }

    private void onVipInfoChanged() {
        if (vipInfoChangeListeners != null && !vipInfoChangeListeners.isEmpty()) {
            for (VipInfoChangeListener listener : vipInfoChangeListeners) {
                listener.onChange();
            }
        }
    }

    public void onMoreItemChanged() {
        if (vipInfoChangeListeners != null && !vipInfoChangeListeners.isEmpty()) {
            for (VipInfoChangeListener listener : vipInfoChangeListeners) {
                listener.onMoreConfigChange();
            }
        }
    }

    public void onAVNOChanged() {
        if (vipInfoChangeListeners != null && !vipInfoChangeListeners.isEmpty()) {
            for (VipInfoChangeListener listener : vipInfoChangeListeners) {
                listener.onAVNOChange();
            }
        }
    }

//    public void processUploadAvatarTask(String filePath) {
//        if (!TextUtils.isEmpty(filePath)) {
//            boolean isHD = SettingBusiness.getInstance(mApplication).getPrefEnableHDImage();
//            AppLogger.i(TAG, "upload anh IMAGE_AVATAR sau khi crop");
//            String fileCompressed = MessageHelper.getPathOfCompressedFile(filePath,
//                    Config.Storage.PROFILE_PATH, isHD);
//            //FileHelper.deleteFile(filePath);
//            ReengAccount account = getCurrentAccount();
//            account.setNeedUpload(true);
//            account.setAvatarPath(fileCompressed);
//            updateReengAccount(account);
//            UploadListener uploadAvatarListener = new UploadListener() {
//                @Override
//                public void onUploadStarted(UploadRequest uploadRequest) {
//
//                }
//
//                @Override
//                public void onUploadComplete(UploadRequest uploadRequest, String response) {
//                    AppLogger.i(TAG, "response: " + response);
//                    String time = "";
//                    try {
//                        JSONObject object = new JSONObject(response);
//                        int errorCode = -1;
//                        if (object.has(AppConstants.HTTP.REST_CODE)) {
//                            errorCode = object.getInt(AppConstants.HTTP.REST_CODE);
//                        }
//                        if (errorCode == AppConstants.HTTP.CODE_SUCCESS) {
//                            if (object.has(AppConstants.HTTP.USER_INFOR.LAST_AVATAR)) {
//                                time = object.getString(AppConstants.HTTP.USER_INFOR.LAST_AVATAR);
//                            }
//                        }
//                    } catch (Exception e) {
//                        AppLogger.e(TAG, "JSONException", e);
//                    }
//                    if (!TextUtils.isEmpty(time)) {   // upload ok
//                        ReengAccount account = mApplication.getReengAccountBusiness().getCurrentAccount();
//                        account.setLastChangeAvatar(time);
//                        account.setNeedUpload(false);
//                        mApplication.getReengAccountBusiness().updateReengAccount(account);
//                        ListenerHelper.getInstance().onAvatarChange(uploadRequest.getFilePath());
//                    }
//                }
//
//                @Override
//                public void onUploadFailed(UploadRequest uploadRequest, int errorCode, String errorMessage) {
//
//                }
//
//                @Override
//                public void onProgress(UploadRequest uploadRequest, long totalBytes, long uploadedBytes, int
//                        progress, long speed) {
//
//                }
//            };
//            mApplication.getTransferFileBusiness().uploadAvatar(fileCompressed, uploadAvatarListener);
//        }
//    }

    /**
     * get email from device
     *
     * @param context
     * @return
     */
    public String getEmail(Context context) {
        try {
            Account account = getAccount(context);
            if (account == null) {
                return null;
            } else {
                return account.name;
            }
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
            return null;
        }
    }

    //http://developer.android.com/intl/vi/guide/topics/security/permissions.html#normal-dangerous
    @SuppressWarnings({"MissingPermission"})
    private static Account getAccount(Context context) {
        if (PermissionHelper.declinedPermission(context, Manifest.permission.GET_ACCOUNTS)) {
            return null;
        }
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts != null && accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public void updateDomainTask() {
//        mApplication.getXmppManager().changeConfigXmpp();
//        // reconnect
//        if (IMService.isReady()) {
//            IMService.getInstance().connectByToken();
//        } else {
//            AppLogger.i(TAG, "IMService not ready -> start service");
//            mApplication.startIMService();
//        }
    }

    public String getLocationId() {
        return locationId;
    }

    public void updateLocationId(String id) {
        if (id != null && !id.equals(locationId)) {
            this.locationId = id;
            mPref.edit().putString(AppConstants.PREFERENCE.PREF_MY_STRANGER_LOCATION, id).apply();
        }
        updateLastTimeUpdateLocation();
    }

    public void updateLastTimeUpdateLocation() {
        mPref.edit().putLong(AppConstants.PREFERENCE.PREF_LAST_UPDATE_STRANGER_LOCATION, TimeHelper.getCurrentTime())
                .apply();
    }

//    public void checkAndSendIqGetLocation() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                if (checkTimeOutGetMyLocation()) {
//                    IQInfo iqRegId = new IQInfo(IQInfo.NAME_SPACE_LOCATION);
//                    iqRegId.setType(IQ.Type.GET);
//                    try {
//                        IQInfo result = (IQInfo) mApplication.getXmppManager().sendPacketThenWaitingResponse(iqRegId,
//                                false);
//                        if (result != null && result.getType() != null && result.getType() == IQ.Type.RESULT) {
//                            HashMap<String, String> elements = result.getElements();
//                            if (elements != null && "200".equals(elements.get("error"))) {
//                                updateLocationId(elements.get("locationId"));
//                            } /*else if (elements != null && "404".equals(elements.get("error"))) {
//                                updateLastTimeUpdateLocation();
//                            }*/
//                        }
//                    } catch (Exception e) {
//                        AppLogger.e(TAG, "Exception", e);
//                    }
//                }
//            }
//        }).start();
//    }

//    private boolean checkTimeOutGetMyLocation() {
//        if (TextUtils.isEmpty(locationId)) {
//            return true;
//        } else {
//            long lastUpdate = mPref.getLong(AppConstants.PREFERENCE.PREF_LAST_UPDATE_STRANGER_LOCATION, -1);
//            int configTimeOut = ConvertHelper.parserIntFromString(mApplication.getConfigBusiness().
//                    getContentConfigByKey(AppConstants.PREFERENCE.CONFIG.STRANGER_LOCATION_TIMEOUT), 24);
//            int duration = (int) ((TimeHelper.getCurrentTime() - lastUpdate) / TimeHelper.ONE_HOUR_IN_MILISECOND);
//            if (duration >= configTimeOut) {
//                return true;
//            }
//        }
//        return false;
//    }

    //TODO update avno number vào database trong th đăng ký, đổi sim ảo
    public void updateAVNONumber(String avnoNumber) {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            account.setAvnoNumber(avnoNumber);
            updateReengAccount(account);
        }
    }

    public String getAVNONumber() {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            return account.getAvnoNumber();
        }
        return null;
    }

    public void updateAVNOIdenfityCardFront(String urlFront) {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            account.setAvnoICFront(urlFront);
            updateReengAccount(account);
        }
    }

    public void updateAVNOIdenfityCardBack(String urlBack) {
        ReengAccount account = getCurrentAccount();
        if (account != null) {
            account.setAvnoICBack(urlBack);
            updateReengAccount(account);
        }
    }

//    public void uploadDataIfNeeded() {
//        if (mCurrentAccount.getNeedUpload()
//                && !TextUtils.isEmpty(mCurrentAccount.getAvatarPath())) {
//            mApplication.getReengAccountBusiness().processUploadAvatarTask(mCurrentAccount.getAvatarPath());
//        }
//        ImageProfile imageCover = mApplication.getImageProfileBusiness().getImageCover();
//        if (imageCover != null && !imageCover.isUploaded()
//                && !TextUtils.isEmpty(imageCover.getImagePathLocal())) {
//            mApplication.getTransferFileBusiness().uploadCoverWhenConnected(imageCover.getImagePathLocal());
//        }
//
//        if (LogDebugHelper.getInstance(mApplication).isEnableLog()) {
//            long lastTime = mPref.getLong(LogDebugHelper.PREF_UPLOAD_LOG_DEBUG_LAST_TIME, 0l);
//            if (TimeHelper.checkTimeInDay(lastTime)) {
//                AppLogger.i(TAG, "uploaded file logdebug");
//            } else {
//                mApplication.getTransferFileBusiness().startUploadLog();
//            }
//        }
//    }
}