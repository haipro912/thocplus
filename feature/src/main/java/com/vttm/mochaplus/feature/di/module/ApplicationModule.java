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

package com.vttm.mochaplus.feature.di.module;

import android.app.Application;
import android.content.Context;

import com.vttm.mochaplus.feature.di.ApiInfo;
import com.vttm.mochaplus.feature.di.ApplicationContext;
import com.vttm.mochaplus.feature.di.DatabaseInfo;
import com.vttm.mochaplus.feature.di.PreferenceInfo;
import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.data.api.ApiHeader;
import com.vttm.mochaplus.feature.data.api.ApiHelper;
import com.vttm.mochaplus.feature.data.api.AppApiHelper;
import com.vttm.mochaplus.feature.data.db.AppDbHelper;
import com.vttm.mochaplus.feature.data.db.DbHelper;
import com.vttm.mochaplus.feature.data.prefs.AppPreferencesHelper;
import com.vttm.mochaplus.feature.data.prefs.PreferencesHelper;
import com.vttm.mochaplus.feature.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return "";
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey, PreferencesHelper preferencesHelper) {
//        SharedPref pref = new SharedPref(ApplicationController.getContext());
//        String msisdn = pref.getString(AppConstants.KEY_MSISDN, "");
//        return new ApiHeader.ProtectedApiHeader(
//                apiKey,
//                AppConstants.USER,
//                AppConstants.PASS,
//                AppConstants.VERSION,
//                AppConstants.DEVICE,
//                Utilities.getDeviceID(ApplicationController.getContext(), true),
//                msisdn,
//                Utilities.getUIID(ApplicationController.getContext()));
        return null;
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
                .build();
    }
}
