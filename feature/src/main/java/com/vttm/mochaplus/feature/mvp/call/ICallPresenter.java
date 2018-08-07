package com.vttm.mochaplus.feature.mvp.call;

import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

import java.util.List;

public interface ICallPresenter<V extends ICallView> extends MvpPresenter<V>
{
    List<ContactConstant> loadContact();
}
