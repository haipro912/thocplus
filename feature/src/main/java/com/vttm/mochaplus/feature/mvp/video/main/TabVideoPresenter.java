package com.vttm.mochaplus.feature.mvp.video.main;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.data.api.request.BaseRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.Config;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Response;

public class TabVideoPresenter<V extends ITabVideoView> extends BasePresenter<V>
        implements ITabVideoPresenter<V> {

    @Inject
    public TabVideoPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadCategory() {
        getDataManager().getVideoCategory(new BaseRequest(Config.REVISION, Config.DOMAIN_VIDEO, Config.CLIENT_TYPE, "01628874431", "NOVIP"),
                new ApiCallback<VideoCategoryResponse>() {
            @Override
            public void onResponse(Call<VideoCategoryResponse> call, Response<VideoCategoryResponse> response) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().bindData(response.body());
            }

            @Override
            public void onFailure(Call<VideoCategoryResponse> call, Throwable t) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().bindData(null);
            }
        });
    }
}
