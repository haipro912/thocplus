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

package com.vttm.mochaplus.feature.data;

import android.content.Context;

import com.vttm.mochaplus.feature.data.api.ApiHeader;
import com.vttm.mochaplus.feature.data.api.ApiHelper;
import com.vttm.mochaplus.feature.data.api.request.BaseRequest;
import com.vttm.mochaplus.feature.data.api.request.GenOtpRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoDetailRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRelateRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.data.db.DbHelper;
import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.CallHistoryConstant;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.data.db.model.ReengAccountConstant;
import com.vttm.mochaplus.feature.data.prefs.PreferencesHelper;
import com.vttm.mochaplus.feature.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by HaiKE code phan nay on 27/01/17.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private Context mContext;
    private DbHelper mDbHelper;
    private PreferencesHelper mPreferencesHelper;
    private ApiHelper mApiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public void getVideoDetail(VideoDetailRequest request, ApiCallback<VideoDetailResponse> callBack) {
        mApiHelper.getVideoDetail(request, callBack);
    }

    @Override
    public void getVideoRelate(VideoRelateRequest request, ApiCallback<VideoResponse> callBack) {
        mApiHelper.getVideoRelate(request, callBack);
    }

    @Override
    public void genOTP(GenOtpRequest request, ApiCallback<String> callBack) {
        mApiHelper.genOTP(request, callBack);
    }

    @Override
    public void getVideoCategory(BaseRequest request, ApiCallback callBack) {
        mApiHelper.getVideoCategory(request, callBack);
    }

    @Override
    public void getVideoList(VideoRequest request, ApiCallback callBack) {
        mApiHelper.getVideoList(request, callBack);
    }

    @Override
    public void updateSessionToken(String sessionToken) {

    }

    @Override
    public String getSessionToken() {
        return null;
    }

    @Override
    public void setSessionToken(String sessionToken) {

    }

    @Override
    public List<ContactConstant> getListContact() {
        return mDbHelper.getListContact();
    }

    @Override
    public List<CallHistoryConstant> getListCallHistory() {
        return mDbHelper.getListCallHistory();
    }

    @Override
    public List<ContactConstant> insertAllContact(List<ContactConstant> elementList) throws RepositoryException {
        return mDbHelper.insertAllContact(elementList);
    }

    @Override
    public ReengAccountConstant getAccount() {
        return mDbHelper.getAccount();
    }

    @Override
    public void updateAccount(ReengAccountConstant account) {
        mDbHelper.updateAccount(account);
    }

    @Override
    public void updateAccountToken(long reengAccountID, String token) {
        mDbHelper.updateAccountToken(reengAccountID, token);
    }

    @Override
    public ReengAccountConstant insertAccount(ReengAccountConstant element) throws RepositoryException {
        return mDbHelper.insertAccount(element);
    }

    @Override
    public void removeAccount(ReengAccountConstant element) {
        mDbHelper.removeAccount(element);
    }
}
