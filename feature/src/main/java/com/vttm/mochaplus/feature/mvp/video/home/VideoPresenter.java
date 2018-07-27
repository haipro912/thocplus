package com.vttm.mochaplus.feature.mvp.video.home;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.data.api.request.VideoRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Response;

public class VideoPresenter<V extends IVideoView> extends BasePresenter<V>
        implements IVideoPresenter<V> {

    @Inject
    public VideoPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadData(int offset, int limit, int categoryId, String lastId) {
        getDataManager().getVideoList(new VideoRequest(offset, limit, categoryId, lastId, "15059", "hl2.mocha.com.vn",
                        "Android", "01628874431", "NOVIP"),
                new ApiCallback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindData(response.body());
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindData(null);
                    }
                });
    }
}
