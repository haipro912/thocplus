package com.vttm.mochaplus.feature.mvp.login;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.business.LoginBusiness;
import com.vttm.mochaplus.feature.business.XMPPCode;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPResponseCode;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;
import com.vttm.mochaplus.feature.utils.ToastUtils;

public class LoginFragment extends BaseFragment {

    ApplicationController mApplication;
    AppCompatButton btnLogin;
    EditText edtPhone;
    EditText edtPass;
    TextView btnGetCode;
    LoginActivity loginActivity;

    String currentPhone;
    String currentRegionCode = Config.COUNTRY_CODE;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof LoginActivity)
            loginActivity = (LoginActivity) activity;
        mApplication = (ApplicationController) loginActivity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        return view;
    }

    @Override
    protected void setUp(View view) {
        btnLogin = view.findViewById(R.id.btn_login);
        edtPhone = view.findViewById(R.id.input_phone);
        edtPass = view.findViewById(R.id.input_password);
        btnGetCode = view.findViewById(R.id.link_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGenOTP();
            }
        });
    }

    private void doLogin() {
//        currentPhone = "01695603459";
        edtPass.setText("295199");
        edtPhone.setText("0948222060");
        currentPhone = edtPhone.getText().toString().trim();
        new LoginByCodeAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edtPass.getText().toString());
    }

    private void doGenOTP() {
//        if (!mCkBoxAgree.isChecked()) {
//            mLoginActivity.showError(getString(R.string.error_agree_term),
//                    mLoginActivity.getResources().getString(R.string.note_title));
//        } else {
        String numberInput = edtPhone.getText().toString().trim();
        PhoneNumberUtil phoneUtil = mApplication.getPhoneUtil();
        Phonenumber.PhoneNumber phoneNumberProtocol = PhoneNumberHelper.getInstant().
                getPhoneNumberProtocol(phoneUtil, numberInput, currentRegionCode);
        if (PhoneNumberHelper.getInstant().isValidPhoneNumber(phoneUtil, phoneNumberProtocol)) {
            currentRegionCode = mApplication.getPhoneUtil().getRegionCodeForNumber(phoneNumberProtocol);
            currentPhone = PhoneNumberHelper.getInstant().getNumberJidFromNumberE164(
                    phoneUtil.format(phoneNumberProtocol, PhoneNumberUtil.PhoneNumberFormat.E164));

            loginActivity.gotoGenOtp(currentPhone, currentRegionCode);
        } else {
            ToastUtils.makeText(loginActivity, "Số điện thoại ko hợp lệ!");
        }
    }



    private class LoginByCodeAsyncTask extends AsyncTask<String, XMPPResponseCode, XMPPResponseCode> {
        String mPassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mLoginActivity.showLoadingDialog("", R.string.processing);
        }

        @Override
        protected XMPPResponseCode doInBackground(String[] params) {
            mPassword = params[0];
            ApplicationController applicationController = (ApplicationController) loginActivity.getApplication();
            LoginBusiness loginBusiness = applicationController.getLoginBusiness();
            XMPPResponseCode responseCode = loginBusiness.loginByCode(applicationController, currentPhone, mPassword, currentRegionCode, true);
            return responseCode;
        }

        @Override
        protected void onPostExecute(XMPPResponseCode responseCode) {
            super.onPostExecute(responseCode);
            AppLogger.i(TAG, "responseCode: " + responseCode);

            btnLogin.setEnabled(true);
            try {
                if (responseCode.getCode() == XMPPCode.E200_OK) {
//                    isLoginDone = true;
                    AppLogger.i(TAG, "E200_OK: " + responseCode);

//                    if (mListener != null && !isSaveInstanceState) {
//                        mListener.displayPersonalInfo();
//                    }
                    loginActivity.gotoMainActivity();
                } else {
//                    mLoginActivity.hideLoadingDialog();
//                    mLoginActivity.showError(responseCode.getDescription(), null);
                }
            } catch (Exception e) {
                AppLogger.e(TAG, "Exception", e);
//                mLoginActivity.hideLoadingDialog();
            }
        }
    }
}
