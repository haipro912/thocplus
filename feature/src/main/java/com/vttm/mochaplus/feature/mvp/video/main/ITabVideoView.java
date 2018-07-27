package com.vttm.mochaplus.feature.mvp.video.main;

import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.mvp.base.MvpView;

public interface ITabVideoView  extends MvpView{
    void bindData(VideoCategoryResponse response);
}
