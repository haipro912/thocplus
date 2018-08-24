package com.vttm.mochaplus.feature.business;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.interfaces.AppLockStateListener;
import com.vttm.mochaplus.feature.interfaces.ScreenStateListener;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.utils.ToastUtils;

import java.util.List;

/**
 * Created by toanvk2 on 11/19/14.
 */
public class ApplicationStateManager implements ScreenStateListener {
    private static final String TAG = ApplicationStateManager.class.getSimpleName();
    public static final long TIME_CHECK_BACKGROUND = 500;
    //    public static final long TIME_CHECK_SLEEP = 1 * 6 * 1000; // 6 s
    public static final long TIME_CHECK_SLEEP = 10 * 60 * 1000; // 10 phut
    private ApplicationController mApplication;
    //    private XMPPManager mXmppManager;
    private boolean isAppWentToBg = false;
    private boolean isAppWentToFg = false;
    private boolean isWindowFocused = true;
    private boolean isBackPressed = false;
    private boolean isActivityForResult = false;
    private boolean isTakePhotoAndCrop = false;
    //    private boolean isGotoWebView = false;
    private CountDownTimer mCountDownTimer = null;
    private CountDownTimer mTimerSleep = null;
    //screen state
//    private ScreenStateReceiver mScreenStateReceiver;
    private long lastTimeAppEnterBackground = 0; //lan cuoi cung app chuyen tu fore -> background
    private boolean isShowQuickReply = false;
    private boolean isSendForeground = false;
    // app lock
    private AppLockStateListener lockStateListener;

    public ApplicationStateManager(ApplicationController app) {
        mApplication = app;
    }

    public void setAppWentToBg(boolean isAppWentToBg) {
        this.isAppWentToBg = isAppWentToBg;
    }

    public boolean isAppWentToBg() {
        return isAppWentToBg;
    }

    public boolean isAppWentToFg() {
        return isAppWentToFg;
    }

    public void setWindowFocused(boolean isWindowFocused) {
        Log.d(TAG, "setWindowFocused: " + isWindowFocused);
        this.isWindowFocused = isWindowFocused;
    }

    public void setShowQuickReply(boolean isShowQuickReply) {
        this.isShowQuickReply = isShowQuickReply;
    }

    public boolean isShowQuickReply() {
        return isShowQuickReply;
    }

    public void setSendForeground(boolean isSendForeground) {
        this.isSendForeground = isSendForeground;
    }

    public boolean isWindowFocused() {
        return isWindowFocused;
    }

    public boolean isBackPressed() {
        return isBackPressed;
    }

    public void setBackPressed(boolean isBackPressed) {
        this.isBackPressed = isBackPressed;
    }

    public boolean isActivityForResult() {
        return isActivityForResult;
    }

    public void setActivityForResult(boolean isActivityForResult) {
        this.isActivityForResult = isActivityForResult;
    }

    public boolean isTakePhotoAndCrop() {
        return isTakePhotoAndCrop;
    }

    public void setTakePhotoAndCrop(boolean isTakePhotoAndCrop) {
        this.isTakePhotoAndCrop = isTakePhotoAndCrop;
    }

    public void checkStateAfterLoginSuccess() {
        if (isAppForeground()) {// dang o bg
            isAppWentToFg = true;
            isAppWentToBg = false;
        } else {
            isAppWentToFg = false;
            isAppWentToBg = true;
        }
    }

    /**
     * @param activity
     * @param isTouchQuickReply nguoi dung dang reply trong quick reply ?
     */
    public void applicationEnterForeground(BaseActivity activity, boolean isTouchQuickReply) {
        Log.d(TAG, "applicationEnterForeground: " + activity.getClass().getSimpleName() + " isTouchQuickReply: " + isTouchQuickReply);
        lastTimeAppEnterBackground = 0;
//        if ((activity instanceof QuickReplyActivity || activity instanceof QuickMissCallActivity || activity instanceof MochaCallActivity || isShowQuickReply) && !isTouchQuickReply) {
//            Log.d(TAG, "Enter Foreground in quick reply ");
//            return;
//        }
        if (!isWindowFocused) {
            Log.d(TAG, "not focus screen window");
            return;
        }
        if (isAppWentToBg || !isAppWentToFg) {// dang o background hoac chua vao foreground
            XMPPManager xmppManager = mApplication.getXmppManager();
            isAppWentToBg = false;
            isAppWentToFg = true;
            // check ket noi den server
            notifyAppWentToForeground(activity);
            if (xmppManager != null && xmppManager.isAuthenticated()) {
                xmppManager.sendPresenceBackgroundOrForeground(false);
            }
            isSendForeground = true;
            if (mTimerSleep != null) {
                mTimerSleep.cancel();
                mTimerSleep = null;
            }
            //clear fake notify msg
//            ReengNotificationManager.clearMessagesNotification(mApplication, AppConstants.NOTIFICATION.NOTIFY_FAKE_NEW_MSG);
        }
    }

    public void applicationEnterBackground(BaseActivity activity, boolean isReplying) {
        Log.d(TAG, "applicationEnterBackground: " + activity.getClass().getSimpleName());
//        if ((activity instanceof QuickReplyActivity || activity instanceof QuickMissCallActivity) && !isReplying && isAppWentToBg) {
//            return;
//        }
        startCountDown(activity);
    }

    private void startCountDown(final BaseActivity activity) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        // dang o fg hoac khong o bg moi start timer
        Log.d(TAG, "applicationEnterBackground: startcountdown");
        mCountDownTimer = new CountDownTimer(TIME_CHECK_BACKGROUND, TIME_CHECK_BACKGROUND) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (!isAppForeground()) {
                    notifyAppWentToBackground();
                    XMPPManager xmppManager = mApplication.getXmppManager();
                    isAppWentToBg = true;
                    isAppWentToFg = false;
                    if (xmppManager != null && xmppManager.isAuthenticated() && isSendForeground) {
                        xmppManager.sendPresenceBackgroundOrForeground(true);
                    }
                    isSendForeground = false;
                    mCountDownTimer = null;
                    Log.d(TAG, "applicationEnterBackground: finish countdown");
                    lastTimeAppEnterBackground = System.currentTimeMillis();
                    //                    startTimerSleep(activity);
                    //                    activity.showToast("App is Going to Background", Toast.LENGTH_SHORT);
                }
            }
        }.start();
    }

    public boolean isAppForeground() {
        if (!isScreenOn()) {
            return false;
        }
        ActivityManager mActivityManager = (ActivityManager) mApplication.
                getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        if (l == null) return false;
        for (RunningAppProcessInfo info : l) {
            if (info.uid == mApplication.getApplicationInfo().uid &&
                    info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentActivity() {
        ActivityManager am = (ActivityManager) mApplication
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        //String packageName = componentInfo.getPackageName();
        return componentInfo.getClassName();
    }

    public boolean isScreenOn() {
        PowerManager pm = (PowerManager)
                mApplication.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public boolean isScreenLocker() {
        KeyguardManager km = (KeyguardManager) mApplication.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    private void startTimerSleep(final BaseActivity activity) {
        if (mTimerSleep != null) {
            mTimerSleep.cancel();
            mTimerSleep = null;
        }
        Log.d(TAG, "startTimerSleep---->");
        mTimerSleep = new CountDownTimer(TIME_CHECK_SLEEP, TIME_CHECK_SLEEP) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "startTimerSleep: finish countdown");
                Log.d(TAG, "startTimerSleep: state isAppWentToBg: " + isAppWentToBg + " state isAppWentToFg: " + isAppWentToFg);
                ToastUtils.makeText(activity,"App is Going to Sleep after: " + TIME_CHECK_SLEEP + "ms", Toast.LENGTH_SHORT);
            }
        }.start();
    }

    @Override
    public void onScreenOn() {
        Log.d(TAG, "onScreenOn");
    }

    @Override
    public void onScreenOff() {
        Log.d(TAG, "onScreenOff");
    }

    public long getLastTimeAppEnterBackground() {
        return lastTimeAppEnterBackground;
    }

    public void setLastTimeAppEnterBackground(long lastTimeAppEnterBackground) {
        this.lastTimeAppEnterBackground = lastTimeAppEnterBackground;
    }

    private void initScreenStateReceiver() {
//        if (mScreenStateReceiver == null) {
//            mScreenStateReceiver = new ScreenStateReceiver();
//            mScreenStateReceiver.setStateListener(this);
//        }
    }

    //   screen on/off
    protected void registerScreenReceiver() {
//        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        mApplication.registerReceiver(mScreenStateReceiver, filter);
    }

    /**
     * applock
     */

    public void setAppLockStateListener(AppLockStateListener listener) {
        this.lockStateListener = listener;
    }

    private void notifyAppWentToBackground() {
        if (lockStateListener != null) {
            lockStateListener.onWentToBackground();
        }
    }

    private void notifyAppWentToForeground(BaseActivity activity) {
        if (lockStateListener != null) {
            lockStateListener.onWentToForeground(activity);
        }
    }

    public void notifyActivityStarted(BaseActivity activity) {
        if (lockStateListener != null) {
            lockStateListener.onActivityStarted(activity);
        }
    }
}