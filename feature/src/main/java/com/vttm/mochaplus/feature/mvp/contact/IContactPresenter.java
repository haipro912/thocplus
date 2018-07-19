package com.vttm.mochaplus.feature.mvp.contact;

import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.mvp.base.MvpPresenter;

import java.util.List;

public interface IContactPresenter<V extends IContactView> extends MvpPresenter<V>
{
    List<ContactConstant> loadContact();
}
