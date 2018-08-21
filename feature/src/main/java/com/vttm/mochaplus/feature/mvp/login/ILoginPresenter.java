package com.vttm.mochaplus.feature.mvp.login;

import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

public interface ILoginPresenter<V extends ILoginView> extends MvpPresenter<V>
{
    void genOtp(String phone, String countryCode);
}
