package com.vttm.mochaplus.feature.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by thaodv on 5/14/2015.
 */
public class DeviceHelper {
    private static final String TAG = DeviceHelper.class.getSimpleName();

    public static String getDeviceId(Context ctx) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return "";
    }

    public static boolean isEmulator() {
        return Build.BRAND.contains("generic")
                || Build.DEVICE.contains("generic")
                || Build.PRODUCT.contains("sdk")
                || Build.HARDWARE.contains("goldfish")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("vbox86p")
                || Build.DEVICE.contains("vbox86p")
                || Build.HARDWARE.contains("vbox86");
    }

    public static boolean checkDontKeepActivity(Context context) {
        int state;
        if (Version.hasJellyBeanMR1()) {
            state = Settings.System.getInt(context.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
        } else {
            state = Settings.System.getInt(context.getContentResolver(), Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        }
        return state != 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isBackCameraAvailable(Context context) {
        if (Version.hasLollipop()) {
            String backCameraId = null;
            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                for (String cameraId : manager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
                    Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                    if (facing == CameraMetadata.LENS_FACING_BACK) {
                        backCameraId = cameraId;
                        break;
                    }
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, "Exception", e);
            }
            return !TextUtils.isEmpty(backCameraId);
        } else {
            int backCameraId = -1;
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                CameraInfo cameraInfo = new CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    backCameraId = i;
                    break;
                }
            }
            return backCameraId != -1;
        }
    }
}