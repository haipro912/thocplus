package com.vttm.mochaplus.feature.mvp.video.main;

import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

public interface ITabVideoPresenter<V extends ITabVideoView> extends MvpPresenter<V>
{
    void loadCategory();
}
