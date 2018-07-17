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


import com.vttm.mochaplus.feature.di.ApplicationContext;
import com.vttm.mochaplus.feature.data.api.ApiHeader;
import com.vttm.mochaplus.feature.data.api.ApiHelper;
import com.vttm.mochaplus.feature.data.api.request.NewsContentRequest;
import com.vttm.mochaplus.feature.data.api.request.NewsRequest;
import com.vttm.mochaplus.feature.data.api.response.NewsContentResponse;
import com.vttm.mochaplus.feature.data.api.response.NewsResponse;
import com.vttm.mochaplus.feature.data.db.DbHelper;
import com.vttm.mochaplus.feature.data.prefs.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by LienVT code phan nay on 27/01/17.
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
    public Observable<NewsResponse> getNewsByCategory(NewsRequest request) {
        return null;
    }

    @Override
    public Observable<NewsContentResponse> getNewsContent(NewsContentRequest request) {
        return null;
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
}
