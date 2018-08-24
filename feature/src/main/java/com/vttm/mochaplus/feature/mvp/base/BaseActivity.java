/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.vttm.mochaplus.feature.mvp.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.broadcast.NetworkChangeReceiver;
import com.vttm.mochaplus.feature.business.ReengAccountBusiness;
import com.vttm.mochaplus.feature.di.component.ActivityComponent;
import com.vttm.mochaplus.feature.di.component.DaggerActivityComponent;
import com.vttm.mochaplus.feature.di.module.ActivityModule;
import com.vttm.mochaplus.feature.event.NetworkEvent;
import com.vttm.mochaplus.feature.helper.NetworkHelper;
import com.vttm.mochaplus.feature.helper.Version;
import com.vttm.mochaplus.feature.utils.CommonUtils;
import com.vttm.mochaplus.feature.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by janisharali on 27/01/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback, NetworkHelper.NetworkChangedCallback {

    private ProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    private Unbinder mUnBinder;
    protected BaseFragment currentFragment;

    private ApplicationController applicationController;
    private NetworkChangeReceiver networkReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationController = (ApplicationController) getApplication();

        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((ApplicationController) getApplication()).getComponent())
                .build();

        registerNetWorkReceiver();
        NetworkHelper.getInstance().setNetworkChangedCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }


    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackbar.show();
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
//        startActivity(LoginActivity.getStartIntent(this));
//        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();

        unRegisterNetWorkReceiver();
        NetworkHelper.getInstance().setNetworkChangedCallback(null);
    }

    protected abstract void setUp();
    protected abstract void notifyNetworkChange(boolean flag);

    @Override
    public void onNetworkChanged(boolean isNetworkAvailable) {
        notifyNetworkChange(isNetworkAvailable);
        EventBus.getDefault().post(new NetworkEvent(isNetworkAvailable));
    }

    private void registerNetWorkReceiver() {
        if (Version.hasN()) {
            networkReceiver = new NetworkChangeReceiver();
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unRegisterNetWorkReceiver() {
        if (Version.hasN() && networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }

    private void checkConnection()
    {
        ReengAccountBusiness accountBusiness = applicationController.getReengAccountBusiness();
        if (!applicationController.getXmppManager().isAuthenticated() &&
                accountBusiness.isValidAccount() &&
                NetworkHelper.isConnectInternet(getApplicationContext())) {


//            Thread reconnectThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                    XMPPManager mXmppManager = applicationController.getXmppManager();
//                    ReengAccountBusiness mReengAccountBusiness = applicationController.getReengAccountBusiness();
//                    if (mXmppManager != null) {
//                        XMPPManager.notifyXMPPConnecting();
//                        mXmppManager.connectByToken(applicationController, mReengAccountBusiness.getJidNumber(),
//                                mReengAccountBusiness.getToken(), mReengAccountBusiness.getRegionCode());
//                    }
//                }
//            });
//            reconnectThread.setDaemon(true);
//            reconnectThread.start();


//            //neu co mang va chua ket noi toi XMPP thi thuc hien ket noi
//            if (IMService.isReady()) {
//                Log.i(TAG, " ---> connectByToken when network is available");
//                IMService.getInstance().connectByToken();
//            } else {
////                    startService(new Intent(this, IMService.class));
//                ApplicationController applicationController = (ApplicationController) getApplicationContext();
//                if (applicationController != null)
//                    applicationController.startIMService();
//            }
        }
    }
}
