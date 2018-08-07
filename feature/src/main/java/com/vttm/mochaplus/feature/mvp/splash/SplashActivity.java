package com.vttm.mochaplus.feature.mvp.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void setUp() {
        //Check login chua


    }

    @Override
    protected void notifyNetworkChange(boolean flag) {

    }
}
