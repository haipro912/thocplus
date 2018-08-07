package com.vttm.mochaplus.feature.mvp.video.detail;

import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

public interface IVideoDetailPresenter<V extends IVideoDetailView> extends MvpPresenter<V>
{
    void loadVideoRelate(String query, int offset, int limit, String lastId);
    void loadVideoDetail(String url);
}
