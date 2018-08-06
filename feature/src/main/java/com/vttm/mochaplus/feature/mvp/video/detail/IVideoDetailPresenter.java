package com.vttm.mochaplus.feature.mvp.video.detail;

import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

public interface IVideoDetailPresenter<V extends IVideoDetailView> extends MvpPresenter<V>
{
    void loadData(int offset, int limit, int categoryId, String lastId);
}
