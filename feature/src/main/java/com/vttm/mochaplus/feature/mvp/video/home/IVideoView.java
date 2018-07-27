package com.vttm.mochaplus.feature.mvp.video.home;

import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.mvp.base.MvpView;

public interface IVideoView extends MvpView{
    void bindData(VideoResponse response);
}
