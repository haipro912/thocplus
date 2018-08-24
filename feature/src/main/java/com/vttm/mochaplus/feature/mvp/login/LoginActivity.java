package com.vttm.mochaplus.feature.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.vttm.mochaplus.feature.MainActivity;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements ILoginView{

    private BaseFragment currentFragment;

    @Inject
    ILoginPresenter<ILoginView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        showFragment(AppConstants.LOGIN.TAB_LOGIN);
    }

    @Override
    protected void notifyNetworkChange(boolean flag) {

    }

    private void showFragment(int id)
    {
        switch (id)
        {
            case AppConstants.LOGIN.TAB_LOGIN:
                currentFragment = LoginFragment.newInstance();
                break;
            case AppConstants.LOGIN.TAB_REGISTER:
                currentFragment = RegisterFragment.newInstance();
                break;
            case AppConstants.LOGIN.TAB_VERIFY:
                currentFragment = VerifyCodeFragment.newInstance();
                break;
            default:
                currentFragment = LoginFragment.newInstance();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, currentFragment).commitAllowingStateLoss();
    }

    public void gotoGenOtp(String phone, String coutryCode) {
        if(presenter != null)
            presenter.genOtp(phone, coutryCode);
    }

    @Override
    public void bindData(String response) {

    }

    public void gotoMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
