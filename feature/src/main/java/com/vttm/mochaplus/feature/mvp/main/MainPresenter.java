package com.vttm.mochaplus.feature.mvp.main;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V>{

    @Inject
    public MainPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }
}
