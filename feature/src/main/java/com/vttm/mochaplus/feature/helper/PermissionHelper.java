package com.vttm.mochaplus.feature.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.vttm.mochaplus.feature.mvp.base.BaseActivity;

import java.util.ArrayList;

public class PermissionHelper {
    private static RequestPermissionsResult mCallBack;

    public static boolean hasPermissionBluetooth(Context context) {
        return context.checkPermission(android.Manifest.permission.BLUETOOTH,
                Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean allowedPermission(Context context, String permission) {
        if (Version.hasM()) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            return permissionCheck == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static boolean declinedPermission(Context context, String permission) {
        if (Version.hasM()) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            return permissionCheck != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestPermission(BaseActivity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        // app-defined int constant. The callback method gets the
        // result of the request.
    }

    public static void requestPermissions(BaseActivity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
        // app-defined int constant. The callback method gets the
        // result of the request.
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isSystemAlertGranted(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public ArrayList<String> declinedPermissions(Context context, String[] permissions) {
        ArrayList<String> permissionsNeededs = new ArrayList<>();
        for (String permission : permissions) {
            if (declinedPermission(context, permission)) {
                permissionsNeededs.add(permission);
            }
        }
        return permissionsNeededs;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mCallBack != null) {
            mCallBack.onPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static synchronized void setCallBack(RequestPermissionsResult callBack) {
        mCallBack = callBack;
    }

    public interface RequestPermissionsResult {
        void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }
}
