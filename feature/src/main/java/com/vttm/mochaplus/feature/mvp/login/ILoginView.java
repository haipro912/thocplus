package com.vttm.mochaplus.feature.mvp.login;

import com.vttm.mochaplus.feature.mvp.base.MvpView;

public interface ILoginView extends MvpView {
    void bindData(String response);
}

