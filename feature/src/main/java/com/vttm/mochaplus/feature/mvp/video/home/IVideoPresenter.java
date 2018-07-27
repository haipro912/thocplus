package com.vttm.mochaplus.feature.mvp.video.home;

import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

public interface IVideoPresenter<V extends IVideoView> extends MvpPresenter<V>
{
    void loadData(int offset, int limit, int categoryId, String lastId);
}
