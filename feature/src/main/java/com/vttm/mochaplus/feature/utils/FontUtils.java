package com.vttm.mochaplus.feature.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.BOLD_ITALIC;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.SANS_SERIF;

/**
 * Created by HaiKE on 11/3/16.
 */

public class FontUtils {
    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("LOG_FONT", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }

    public static Typeface getTypeface(int fontType, Context context) {
        // here you can load the Typeface from asset or use default ones
        switch (fontType) {
            case BOLD:
                return Typeface.create(SANS_SERIF, BOLD);
            case ITALIC:
                return Typeface.create(SANS_SERIF, ITALIC);
            case BOLD_ITALIC:
                return Typeface.create(SANS_SERIF, BOLD_ITALIC);
            default:
                return Typeface.create(SANS_SERIF, Typeface.NORMAL);
        }
    }
}
