package com.vttm.mochaplus.feature.mvp.call;

import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.mvp.base.BasePresenter;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CallPresenter<V extends ICallView> extends BasePresenter<V>
        implements ICallPresenter<V> {

    @Inject
    public CallPresenter(AppDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public List<ContactConstant> loadContact() {
        return getDataManager().getListContact();
    }
}
