package com.vttm.mochaplus.feature.mvp.video.detail;

import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.mvp.base.MvpView;

public interface IVideoDetailView extends MvpView{
    void bindDataVideoRelate(VideoResponse response);
    void bindDataVideoDetail(VideoDetailResponse response);
}
