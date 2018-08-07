package com.vttm.mochaplus.feature.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;

public class LoginActivity extends BaseActivity{

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
}
