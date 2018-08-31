package com.vttm.mochaplus.feature.mvp.video.detail;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.api.request.VideoDetailRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRelateRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Response;

public class VideoDetailPresenter<V extends IVideoDetailView> extends BasePresenter<V>
        implements IVideoDetailPresenter<V> {

    @Inject
    public VideoDetailPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadVideoRelate(String query, int offset, int limit, String lastId) {
        getDataManager().getVideoRelate(new VideoRelateRequest(query, offset, limit, lastId),
                new ApiCallback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindDataVideoRelate(response.body());
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindDataVideoRelate(null);
                    }
                });
    }

    @Override
    public void loadVideoDetail(String url) {
        getDataManager().getVideoDetail(new VideoDetailRequest(url),
                new ApiCallback<VideoDetailResponse>() {
                    @Override
                    public void onResponse(Call<VideoDetailResponse> call, Response<VideoDetailResponse> response) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindDataVideoDetail(response.body());
                    }

                    @Override
                    public void onFailure(Call<VideoDetailResponse> call, Throwable t) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindDataVideoDetail(null);
                    }
                });
    }
}
