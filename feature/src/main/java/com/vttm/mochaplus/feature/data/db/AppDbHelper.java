/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.vttm.mochaplus.feature.data.db;

import com.vttm.mochaplus.feature.data.db.datasource.CallHistoryDataSource;
import com.vttm.mochaplus.feature.data.db.datasource.ContactDataSource;
import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.CallHistoryConstant;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.data.db.providers.RealmProvider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by HaiKE on 07/18/18.
 */

@Singleton
public class AppDbHelper implements DbHelper {
    private RealmProvider realmProvider;
    ContactDataSource contactDataSource;
    CallHistoryDataSource callHistoryDataSource;

    @Inject
    public AppDbHelper(RealmProvider realmProvider) {
        this.realmProvider = realmProvider;
        contactDataSource = new ContactDataSource(realmProvider);
        callHistoryDataSource = new CallHistoryDataSource(realmProvider);
    }

    @Override
    public List<ContactConstant> getListContact() {
        return contactDataSource.findAll();
    }

    @Override
    public List<CallHistoryConstant> getListCallHistory() {
        return callHistoryDataSource.findAll();
    }

    @Override
    public List<ContactConstant> insertAll(List<ContactConstant> elementList) throws RepositoryException {
        return contactDataSource.insertAll(elementList);
    }
}
