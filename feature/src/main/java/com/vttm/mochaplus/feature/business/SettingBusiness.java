package com.vttm.mochaplus.feature.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;
import com.vttm.mochaplus.feature.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 7/15/14.
 */
public class SettingBusiness {
    private static SettingBusiness mInstance;
    private final String TAG = SettingBusiness.class.getSimpleName();
    private ArrayList<String> mListSilentThreadId;
    private ArrayList<String> mListSmartNotifyThreadId;
    private Context mContext;
    private SharedPreferences mPref;
    private boolean isPreviewMessage = true;
    private boolean isKeyboardSend;
    private boolean isSystemSound = false;
    private boolean isSystemSoundDefault = true;// dùng âm mặc định của máy hay ng dung da chon am #
    private boolean enableAutoSmsOut = true;
    private boolean enableReceiveSmsOut = true;
    private boolean enableFloatingButton = true;
    private Uri notificationSoundUri = null;
    private Uri notificationDefaultUri;
    private int fontSize;

    public SettingBusiness(Context context) {
        this.mContext = context;
        this.mPref = mContext.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
        setListSilentThreadId(getListThreadIdFromPref());
        setListSmartNotifyThreadId(getListThreadIdFromPref());
        isPreviewMessage = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_PREVIEW_MSG,
                AppConstants.PREF_DEFAULT.PREF_DEF_PREVIEW_MSG);
        isKeyboardSend = getPrefSetupKeyboardSend();
        enableAutoSmsOut = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_AUTO_SMSOUT, true);
        enableReceiveSmsOut = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_RECEIVE_SMSOUT, true);
        fontSize = mPref.getInt(AppConstants.PREFERENCE.PREF_SETTING_FONT_SIZE, AppConstants.FONT_SIZE.MEDIUM);
        enableFloatingButton = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_FLOATING_BUTTON, true);
        initNotificationSound();
    }

    public static synchronized SettingBusiness getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SettingBusiness(context);
        }
        return mInstance;
    }

    private ArrayList<String> getListSilentThreadId() {
        return mListSilentThreadId;
    }

    private void setListSilentThreadId(ArrayList<String> threadIds) {
        this.mListSilentThreadId = threadIds;
    }

    private ArrayList<String> getListSmartNotifyThreadId() {
        return mListSmartNotifyThreadId;
    }

    private void setListSmartNotifyThreadId(ArrayList<String> threadIds) {
        this.mListSmartNotifyThreadId = threadIds;
    }

    public boolean checkExistInSmartNotifyThread(int threadId) {
        if (threadId < 0) {
            return false;
        }
        if (getListSmartNotifyThreadId() == null || getListSmartNotifyThreadId().isEmpty()) {
            return false;
        }
        for (String id : getListSmartNotifyThreadId()) {
            if (id.equals(String.valueOf(threadId))) {
                return true;
            }
        }
        return false;
    }

    public void updateSmartNotifyThread(int threadId, boolean isAdd) {
        if (isAdd) {
            addToListSmartNotifyThread(threadId);
            //            getListSmartNotifyThreadId().add(String.valueOf(threadId));
        } else {
            removeFromListSmartNotifyThread(threadId);
        }
        AppLogger.d(TAG, "" + convertListThreadIdToString(mListSmartNotifyThreadId));
        setListThreadIdToPref(getListSilentThreadId());
    }

    private void removeFromListSmartNotifyThread(int threadId) {
        mListSmartNotifyThreadId.remove(String.valueOf(threadId));
    }

    private void addToListSmartNotifyThread(int threadId) {
        mListSmartNotifyThreadId.add(String.valueOf(threadId));
    }

    private String convertListThreadIdToString(ArrayList<String> threadIds) {
        JSONArray jsonArray = new JSONArray();
        for (String item : threadIds) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }

    private ArrayList<String> convertStringToListThreadId(String content) {
        if (content == null || content.length() <= 0) {
            return null;
        }
        try {
            ArrayList<String> listThread = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(content);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                listThread.add(jsonArray.get(i).toString());
            }
            return listThread;
        } catch (JSONException e) {
            AppLogger.e(TAG, "convertStringToListThreadId", e);
            return null;
        }
    }

    private void setListThreadIdToPref(ArrayList<String> listThreadId) {
        String listString = convertListThreadIdToString(listThreadId);
        mPref.edit().putString(AppConstants.PREFERENCE.PREF_NO_NOTIF_LIST_ID, listString).apply();
    }

    private ArrayList<String> getListThreadIdFromPref() {
        String temp = mPref.getString(AppConstants.PREFERENCE.PREF_NO_NOTIF_LIST_ID, "");
        ArrayList<String> listThreadId = convertStringToListThreadId(temp);
        if (listThreadId == null) {
            return new ArrayList<>();
        }
        return listThreadId;
    }

    public boolean checkExistInSilentThread(int threadId) {
        if (threadId < 0) {
            return false;
        }
        if (getListSilentThreadId() == null || getListSilentThreadId().isEmpty()) {
            return false;
        }
        for (String id : getListSilentThreadId()) {
            if (id.equals(String.valueOf(threadId))) {
                return true;
            }
        }
        return false;
    }

    public void updateSilentThread(int threadId, boolean isAdd) {
        if (isAdd) {
            addToListSilentThread(threadId);
        } else {
            removeFromListSilentThread(threadId);
        }
        AppLogger.d(TAG, "" + convertListThreadIdToString(mListSilentThreadId));
        setListThreadIdToPref(getListSilentThreadId());
    }

    private void removeFromListSilentThread(int threadId) {
        mListSilentThreadId.remove(String.valueOf(threadId));
    }

    private void addToListSilentThread(int threadId) {
        mListSilentThreadId.add(String.valueOf(threadId));
    }

    //cac ham get set pref setting
    public void setSettingPref(String key, boolean value) {
        mPref.edit().putBoolean(key, value).apply();
    }

    public boolean getPrefVibrate() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_VIBRATE, AppConstants.PREF_DEFAULT.PREF_DEF_VIBRATE);
    }

    public void setPrefVibrate(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_VIBRATE, value).apply();
    }

    public boolean getPrefRingtone() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_RINGTONE, AppConstants.PREF_DEFAULT.PREF_DEF_RINGTONE);
    }

    public void setPrefRingtone(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_RINGTONE, value).apply();
    }

    public boolean getPrefMsgInPopup() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_MSG_IN_POPUP, AppConstants.PREF_DEFAULT
                .PREF_DEF_MSG_IN_POPUP);
    }

    public void setPrefMsgInPopup(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_MSG_IN_POPUP, value).apply();
    }

    public boolean getPrefEnableUnlock() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_ENABLE_UNLOCK, AppConstants.PREF_DEFAULT
                .PREF_DEF_ENABLE_UNLOCK);
    }

    public void setPrefEnableUnlock(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_ENABLE_UNLOCK, value).apply();
    }

    public boolean getPrefAutoPlaySticker() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_AUTO_PLAY_STICKER, true);
    }

    public void setPrefAutoPlaySticker(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_AUTO_PLAY_STICKER, value).apply();
    }

    public void setPrefShowMedia(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SHOW_MEDIA, value).apply();
        if (value) {
            FileUtils.deleteNoMediaFile(mContext);
        } else {
            FileUtils.createNoMediaFile(mContext);
        }
    }

    public void setPrefReplySms(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_REPLY_SMS, value).apply();
    }

    public boolean getPrefEnableSeen() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_ENABLE_SEEN, AppConstants.PREF_DEFAULT
                .PREF_DEF_ENABLE_SEEN);
    }

    public boolean getPrefBirthdayReminder() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_BIRTHDAY_REMINDER, AppConstants.PREF_DEFAULT
                .PREF_DEF_BIRTHDAY_REMINDER);
    }

    public boolean getPrefShowMedia() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SHOW_MEDIA, Config.PREF_DEF_SHOW_MEDIA);
    }

    public boolean getPrefReplySms() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_REPLY_SMS, Config.PREF_DEF_REPLY_SMS);
    }

    public void setPrefEnableSeen(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_ENABLE_SEEN, value).apply();
    }

    public void setPrefBirthdayReminder(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_BIRTHDAY_REMINDER, value).apply();
    }

    //image quality
    public void setPrefEnableHDImage(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_IMAGE_HD, value).apply();
    }

    public boolean getPrefEnableHDImage() {
        boolean isVip = ((ApplicationController) mContext).getReengAccountBusiness().isVip();
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_IMAGE_HD, isVip || AppConstants.PREF_DEFAULT
                .PREF_DEF_IMAGE_HD);
    }

    //auto play music in room
    public void setPrefOffMusicInRoom(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_OFF_MUSIC_IN_ROOM, value).apply();
    }

    public boolean getPrefOffMusicInRoom() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_OFF_MUSIC_IN_ROOM, AppConstants.PREF_DEFAULT
                .PREF_DEF_PLAYMUSIC_IN_ROOM);
    }

    /**
     * Setting Notify New User
     *
     * @return
     */
    public boolean getPrefSettingNotifyNewUser() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_NOTIFY_NEW_FRIEND, AppConstants.PREF_DEFAULT
                .PREF_DEF_NOTIFY_NEW_FRIEND);
    }

    public void setPrefSettingNotifyNewUser(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_NOTIFY_NEW_FRIEND, value).apply();
    }

    public boolean getPrefPreviewMsg() {
        /*return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_PREVIEW_MSG, AppConstants.PREF_DEFAULT
                .PREF_DEF_PREVIEW_MSG);*/
        return isPreviewMessage;
    }

    public void setPrefPreviewMsg(boolean value) {
        isPreviewMessage = value;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_PREVIEW_MSG, value).apply();
    }

    public boolean getPrefChatWithStranger() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_CHAT_WITH_STRANGER,
                AppConstants.PREF_DEFAULT.PREF_DEF_ONMEDIA_CHAT_WITH_STRANGER);
    }

    public void setPrefChatWithStranger(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_CHAT_WITH_STRANGER, value).apply();
    }

    public boolean getPrefNotifyNewFeed() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_NOTIFY_NEW_FEED,
                AppConstants.PREF_DEFAULT.PREF_DEF_ONMEDIA_NOTIFY_NEW_FEED);
    }

    public void setPrefNotifyNewFeed(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_NOTIFY_NEW_FEED, value).apply();
    }

    public boolean getPrefSecureWeb() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_SECURE_WEB,
                AppConstants.PREF_DEFAULT.PREF_DEF_ONMEDIA_SECURE_WEB);
    }

    public void setPrefSecureWeb(boolean value) {
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_ONMEDIA_SECURE_WEB, value).apply();
    }

    /**
     * on/off keyboard send
     *
     * @return
     */
    private boolean getPrefSetupKeyboardSend() {
        return mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_SETUP_KEYBOARD_SEND, Config.PREF_DEF_SETUP_KEYBOARD_SEND);
    }

    public void setPrefSetupKeyboardSend(boolean value) {
        isKeyboardSend = value;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_SETUP_KEYBOARD_SEND, value).apply();
    }

    public boolean isSetupKeyboardSend() {
        return isKeyboardSend;
    }

    public void setPrefNotificationSound(Uri soundUri) {
        isSystemSoundDefault = false;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_NOTIFICATION_SOUND_DEFAULT, isSystemSoundDefault)
                .apply();
        notificationSoundUri = soundUri;
        if (soundUri == null) {
            mPref.edit().putString(AppConstants.PREFERENCE.PREF_SETTING_NOTIFICATION_SOUND, null).apply();
        } else {
            mPref.edit().putString(AppConstants.PREFERENCE.PREF_SETTING_NOTIFICATION_SOUND, soundUri.toString()).apply();
        }
    }

    public void setPrefSystemSound(boolean isSystem) {
        isSystemSound = isSystem;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_SYSTEM_SOUND, isSystemSound).apply();
    }

    public boolean isPrefSystemSound() {
        return isSystemSound;
    }

    public boolean isEnableAutoSmsOut() {
        return enableAutoSmsOut;
    }

    public void setPrefEnableAutoSmsOut(boolean enableAutoSmsOut) {
        this.enableAutoSmsOut = enableAutoSmsOut;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_AUTO_SMSOUT, enableAutoSmsOut).apply();
    }

    public boolean isEnableReceiveSmsOut() {
        return enableReceiveSmsOut;
    }

    public void setPrefEnableReceiveSmsOut(boolean enableReceiveSmsOut) {
        this.enableReceiveSmsOut = enableReceiveSmsOut;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_RECEIVE_SMSOUT, enableReceiveSmsOut).apply();
    }

    private void initNotificationSound() {
        //notificationDefaultUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + com.viettel
        // .mocha.app.R.raw.receive_message);
        notificationDefaultUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.receive_message);
        isSystemSound = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_SYSTEM_SOUND, false);
        isSystemSoundDefault = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_NOTIFICATION_SOUND_DEFAULT, true);
        String sound = mPref.getString(AppConstants.PREFERENCE.PREF_SETTING_NOTIFICATION_SOUND, null);//RingtoneManager
        // .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        if (isSystemSoundDefault) {
            notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            if (TextUtils.isEmpty(sound)) {
                notificationSoundUri = null;
            } else {
                notificationSoundUri = Uri.parse(sound);
            }
        }
    }

    public Uri getCurrentSoundUri() {
        if (isSystemSound) {
            return notificationSoundUri;
        } else {
            return notificationDefaultUri;
        }
    }

    public Uri getNotificationSoundUri() {
        return notificationSoundUri;
    }

    public String getNotificationSoundName() {
        if (notificationSoundUri == null) {
            return mContext.getResources().getString(R.string.setting_select_sound_silent);
        } else {
            return RingtoneManager.getRingtone(mContext, notificationSoundUri).getTitle(mContext);
        }
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        mPref.edit().putInt(AppConstants.PREFERENCE.PREF_SETTING_FONT_SIZE, fontSize).apply();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void updateStateFloatingButton(boolean enable) {
        this.enableFloatingButton = enable;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_FLOATING_BUTTON, enable).apply();
    }

    public boolean isEnableFloatingButton(){
        return enableFloatingButton;
    }
}