package com.vttm.mochaplus.feature.mvp.main;

import android.os.Bundle;

import com.vttm.mochaplus.feature.mvp.base.MvpView;

public interface IMainView  extends MvpView {
    void showFragment(int tabId, Bundle bundle);
}

