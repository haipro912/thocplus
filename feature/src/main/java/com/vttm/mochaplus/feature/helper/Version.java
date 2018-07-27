/*
Version.java
Copyright (C) 2010  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.vttm.mochaplus.feature.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.vttm.mochaplus.feature.BuildConfig;

/**
 * Centralize version access and allow simulation of lower versions.
 *
 * @author Guillaume Beraudo
 */
public class Version {

    private static final String TAG = Version.class.getSimpleName();

   /* private static native boolean nativeHasZrtp();

    private static native boolean nativeHasNeon();*/

    private static Boolean hasNeon;


    public static boolean isXLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isArmv7() {
        try {
            return sdkAboveOrEqual(4)
                    && Build.class.getField("CPU_ABI").get(null).toString()
                    .startsWith("armeabi-v7");
        } catch (Throwable e) {
            Log.e(TAG, "Throwable", e);
        }
        return false;
    }

    public static boolean isX86() {
        try {
            return sdkAboveOrEqual(4)
                    && Build.class.getField("CPU_ABI").get(null).toString()
                    .startsWith("x86");
        } catch (Throwable e) {
            Log.e(TAG, "Throwable", e);
        }
        return false;
    }

   /* public static boolean hasNeon() {
        if (hasNeon == null)
            hasNeon = nativeHasNeon();
        return hasNeon;
    }*/

    public static boolean hasFastCpu() {
        return isArmv7() || isX86();
    }

    public static boolean isVideoCapable() {
        //        return !Version.sdkStrictlyBelow(5) &&
        //                isArmv7() && Hacks.hasCamera();
        // do not call video
        return false;
    }

    private static Boolean sCacheHasZrtp;

    /*public static boolean hasZrtp() {
        if (sCacheHasZrtp == null) {
            sCacheHasZrtp = nativeHasZrtp();
        }
        return sCacheHasZrtp;
    }

    public static void dumpCapabilities() {
        StringBuilder sb = new StringBuilder(" ==== Capabilities dump ====\n");
        sb.append("Has neon: ").append(Boolean.toString(hasNeon()))
                .append("\n");
        sb.append("Has ZRTP: ").append(Boolean.toString(hasZrtp()))
                .append("\n");
        Log.i("TAG", sb.toString());
    }*/

    private static boolean hasCamera() {
        int numCameras = Camera.getNumberOfCameras();
        return numCameras >= 1;
    }

    private static boolean hasRearCamera() {
        int numCameras = Camera.getNumberOfCameras();
        return numCameras > 1;
    }

    /**
     * check api version
     */
    //StrictMode
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    /*.detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() */  // or .detectAll() for all detectable problems
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    //
    public static boolean sdkAboveOrEqual(int value) {
        return Build.VERSION.SDK_INT >= value;
    }

    public static boolean sdkStrictlyBelow(int value) {
        return Build.VERSION.SDK_INT < value;
    }
}