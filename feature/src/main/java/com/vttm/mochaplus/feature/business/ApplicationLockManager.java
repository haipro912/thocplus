package com.vttm.mochaplus.feature.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.interfaces.AppLockStateListener;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.EncryptUtil;

/**
 * Created by toanvk2 on 3/9/2016.
 */
public class ApplicationLockManager implements AppLockStateListener {
    private static final String TAG = ApplicationLockManager.class.getSimpleName();
    private ApplicationController mApplication;
    private ApplicationStateManager mApplicationStateManager;
    private Resources mRes;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private long lastTimeWentToBackground = 1;
    private boolean isEnableSettingLock = false;
    private long currentTimeCheckLock;
    private String currentPassEncrypted;
    private boolean isLockedApp = false;
    private boolean isShowLockAppActivity = false;

    //public static final long TIME_CHECK_BACKGROUND = 5000;

    public ApplicationLockManager(ApplicationController application) {
        this.mApplication = application;
        this.mApplicationStateManager = mApplication.getAppStateManager();
        this.mRes = mApplication.getResources();
        mPref = mApplication.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
        initData();
        mApplicationStateManager.setAppLockStateListener(this);
    }

    @Override
    public void onWentToBackground() {
        if (lastTimeWentToBackground == -1 || !isAppLocked()) {
            this.lastTimeWentToBackground = TimeHelper.getCurrentTime();
        }
        Log.d(TAG, "onWentToBackground");
    }

    @Override
    public void onWentToForeground(BaseActivity activity) {
        Log.d(TAG, "onWentToForeground");
        showScreenLockApp(activity);
    }

    @Override
    public void onActivityStarted(BaseActivity activity) {
//        Log.d(TAG, "onActivityStarted : " + activity);
//        if (isShowLockAppActivity && !(activity instanceof LockAppActivity)) {
//            Log.d(TAG, "onActivityStarted : set isShowLockAppActivity = false");
//            //LockAppActivity.this.finish();
//            if (isAppLocked()) {
//                //isShowLockAppActivity = true;
//                Intent lockIntent = new Intent(mApplication, LockAppActivity.class);
//                //lockIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                //lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                lockIntent.putExtra(AppConstants.SETTINGS.DATA_FRAGMENT,  AppConstants.LOCK_APP.LOCK_APP_LOCKED);
//                activity.startActivity(lockIntent);
//            } else {
//                isShowLockAppActivity = false;
//            }
//        } else if (!mApplicationStateManager.isAppWentToBg() || activity instanceof QuickReplyActivity ||
//                activity instanceof QuickMissCallActivity|| activity instanceof LockAppActivity) {
//            Log.d(TAG, "onActivityStarted ->  quick or lock app showing");
//        } else {
//            Log.d(TAG, "onActivityStarted ->  showScreenLockApp");
//            showScreenLockApp(activity);
//        }
    }

    private void showScreenLockApp(BaseActivity activity) {
//        Log.d(TAG, "isShowLockAppActivity: " + isShowLockAppActivity);
//        if (isShowLockAppActivity) {
//            return;
//        }
//        if (isAppLocked()) {
//            //isShowLockAppActivity = true;
//            Intent lockIntent = new Intent(mApplication, LockAppActivity.class);
//            //lockIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            lockIntent.putExtra(AppConstants.SETTINGS.DATA_FRAGMENT, AppConstants.LOCK_APP.LOCK_APP_LOCKED);
//            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            activity.startActivity(lockIntent);
//        }
    }

    private void initData() {
        this.isEnableSettingLock = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_ENABLE, false);
        this.currentTimeCheckLock = mPref.getLong(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_TIME,  AppConstants.LOCK_APP.LOCK_APP_TIME_5_S);
        this.currentPassEncrypted = mPref.getString(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_PASS_ENCRYPTED, null);
        this.isLockedApp = mPref.getBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_STATE_LOCKED, false);
    }

    public boolean isShowLockAppActivity() {
        return isShowLockAppActivity;
    }

    public void setShowLockAppActivity(boolean isShowLockAppActivity) {
        this.isShowLockAppActivity = isShowLockAppActivity;
    }

    public boolean isAppLocked() {
        if (isEnableSettingLock) {
            if (isLockedApp) {
                return true;
            }
            if (lastTimeWentToBackground == -1) {
                return false;
            }
            if (TimeHelper.checkTimeOutForDurationTime(lastTimeWentToBackground, currentTimeCheckLock)) {
                isLockedApp = true;
                mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_STATE_LOCKED, isLockedApp).apply();
            }
            return isLockedApp;
        } else {
            return false;
        }
    }

    public void passedPassword() {
        isLockedApp = false;
        lastTimeWentToBackground = -1;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_STATE_LOCKED, isLockedApp).apply();
    }

    public boolean isEnableSettingLockApp() {
        return isEnableSettingLock;
    }

    public void setEnableSettingLockApp(boolean isEnableSettingLock) {
        this.isEnableSettingLock = isEnableSettingLock;
        mPref.edit().putBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_ENABLE, isEnableSettingLock).apply();
    }

    public long getTimeCheckLock() {
        return currentTimeCheckLock;
    }

    public void setTimeCheckLock(long timeCheckLock) {
        this.currentTimeCheckLock = timeCheckLock;
        mPref.edit().putLong(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_TIME, currentTimeCheckLock).apply();
    }

    public boolean isValidPassword(String enterPassword) {
        if (TextUtils.isEmpty(enterPassword) || TextUtils.isEmpty(currentPassEncrypted)) {
            return false;
        }
        String enterPasswordEncrypt = EncryptUtil.encryptSHA256(enterPassword);
        return currentPassEncrypted.equals(enterPasswordEncrypt);
    }

    public void setChangePassword(String enterPassword) {
        String enterPasswordEncrypt = EncryptUtil.encryptSHA256(enterPassword);
        if (!TextUtils.isEmpty(enterPasswordEncrypt)) {
            mEditor = mPref.edit();
            this.currentPassEncrypted = enterPasswordEncrypt;
            mEditor.putString(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_PASS_ENCRYPTED, currentPassEncrypted);
            if (!isEnableSettingLock) {
                this.isEnableSettingLock = true;
                mEditor.putBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_ENABLE, true);
                this.currentTimeCheckLock = AppConstants.LOCK_APP.LOCK_APP_TIME_5_S;//reset time về 5s nếu đặt mới passworrd
                mEditor.putLong(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_TIME, currentTimeCheckLock);
                //reset ve trang thai ko bi khoa
                lastTimeWentToBackground = -1;
                isLockedApp = false;
                mEditor.putBoolean(AppConstants.PREFERENCE.PREF_SETTING_LOCK_APP_STATE_LOCKED, isLockedApp).apply();
            }
            mEditor.apply();
        }
    }
//
//    public String getTimeLockString() {
//        if (currentTimeCheckLock <= AppConstants.LOCK_APP.LOCK_APP_TIME_NOW) {
//            return mRes.getString(R.string.lock_app_option_now);
//        } else if (currentTimeCheckLock <= AppConstants.LOCK_APP.LOCK_APP_TIME_5_S) {
//            return mRes.getString(R.string.lock_app_option_5_seconds);
//        } else if (currentTimeCheckLock <= AppConstants.LOCK_APP.LOCK_APP_TIME_10_S) {
//            return mRes.getString(R.string.lock_app_option_10_seconds);
//        } else if (currentTimeCheckLock <= AppConstants.LOCK_APP.LOCK_APP_TIME_30_S) {
//            return mRes.getString(R.string.lock_app_option_30_seconds);
//        } else {
//            return mRes.getString(R.string.lock_app_option_minute);
//        }
//    }
//
//    public ArrayList<ItemContextMenu> getListItemChooseTime() {
//        ArrayList<ItemContextMenu> listChooseTime = new ArrayList<>();
//        ItemContextMenu itemNow = new ItemContextMenu(mApplication, mRes.getString(R.string.lock_app_option_now), -1, null, LOCK_APP.LOCK_APP_TIME_NOW);
//        ItemContextMenu item5s = new ItemContextMenu(mApplication, mRes.getString(R.string.lock_app_option_5_seconds), -1, null, LOCK_APP.LOCK_APP_TIME_5_S);
//        ItemContextMenu item10s = new ItemContextMenu(mApplication, mRes.getString(R.string.lock_app_option_10_seconds), -1, null, LOCK_APP.LOCK_APP_TIME_10_S);
//        ItemContextMenu item30s = new ItemContextMenu(mApplication, mRes.getString(R.string.lock_app_option_30_seconds), -1, null, LOCK_APP.LOCK_APP_TIME_30_S);
//        ItemContextMenu item1m = new ItemContextMenu(mApplication, mRes.getString(R.string.lock_app_option_minute), -1, null, LOCK_APP.LOCK_APP_TIME_1_M);
//        listChooseTime.add(itemNow);
//        listChooseTime.add(item5s);
//        listChooseTime.add(item10s);
//        listChooseTime.add(item30s);
//        listChooseTime.add(item1m);
//        if (currentTimeCheckLock <= LOCK_APP.LOCK_APP_TIME_NOW) {
//            itemNow.setChecked(true);
//        } else if (currentTimeCheckLock <= LOCK_APP.LOCK_APP_TIME_5_S) {
//            item5s.setChecked(true);
//        } else if (currentTimeCheckLock <= LOCK_APP.LOCK_APP_TIME_10_S) {
//            item10s.setChecked(true);
//        } else if (currentTimeCheckLock <= LOCK_APP.LOCK_APP_TIME_30_S) {
//            item30s.setChecked(true);
//        } else {
//            item1m.setChecked(true);
//        }
//        return listChooseTime;
//    }

    public boolean isShowFullNotification() {
        if (isEnableSettingLock) {
            if (isLockedApp || mApplicationStateManager.isAppWentToBg()) {
                return false;
            }
        }
        return true;
    }
}