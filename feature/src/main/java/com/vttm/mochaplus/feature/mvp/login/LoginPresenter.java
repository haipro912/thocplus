package com.vttm.mochaplus.feature.mvp.login;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.api.request.GenOtpRequest;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Response;

public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    @Inject
    public LoginPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void genOtp(String phone, String countryCode) {
        getDataManager().genOTP(new GenOtpRequest(phone, countryCode),
                new ApiCallback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindData(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().bindData(null);
                    }
                });
    }
}
