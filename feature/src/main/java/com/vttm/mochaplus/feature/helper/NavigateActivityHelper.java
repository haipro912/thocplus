package com.vttm.mochaplus.feature.helper;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.BuildConfig;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.mvp.login.LoginActivity;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;
import com.vttm.mochaplus.feature.utils.ToastUtils;

public class NavigateActivityHelper {
    private static final String TAG = NavigateActivityHelper.class.getSimpleName();

    public static void navigateToActionView(BaseActivity activity, Uri uri) {
        try {
            if (uri != null) {
                Intent webViewIntent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(webViewIntent);
            } else {
                ToastUtils.makeText(activity, R.string.e601_error_but_undefined);
            }
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
            ToastUtils.makeText(activity, R.string.e667_device_not_support_function);
        }
    }

    public static void navigateToPlayStore(BaseActivity activity, String packageName) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.HTTP.LINK_MARKET + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            AppLogger.e(TAG, "ActivityNotFoundException", anfe);
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(AppConstants.HTTP.LINK_GOOGLE_PLAY + packageName)));
            } catch (Exception e) {
                AppLogger.e(TAG, "Exception", e);
                ToastUtils.makeText(activity, R.string.e666_not_support_function);
            }
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
            ToastUtils.makeText(activity, R.string.e666_not_support_function);
        }
    }

    public static void navigateToLoginActivity(BaseActivity activity, boolean fromHome) {
        Intent intent = new Intent(activity, LoginActivity.class);
        if (fromHome) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public static void navigateToSendEmail(ApplicationController application, final BaseActivity activity) {
        String myNumber = "0969113369";//application.getReengAccountBusiness().getJidNumber();
        if (TextUtils.isEmpty(myNumber)) {
            myNumber = "";
        }
        String device = Build.MANUFACTURER + "-" + Build.BRAND + "-" + Build.MODEL;
        String content = String.format(application.getResources().getString(R.string.msg_content_email), myNumber,
                Build.VERSION.RELEASE, BuildConfig.VERSION_NAME, Config.REVISION, device);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getString(R.string.add_email_support)});
        email.putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.msg_title_email));
        email.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(email, content));
    }
}
