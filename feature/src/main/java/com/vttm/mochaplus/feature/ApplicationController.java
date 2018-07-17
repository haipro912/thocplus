package com.vttm.mochaplus.feature;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.vttm.mochaplus.BuildConfig;
import com.vttm.mochaplus.feature.di.component.ApplicationComponent;
import com.vttm.mochaplus.feature.di.component.DaggerApplicationComponent;
import com.vttm.mochaplus.feature.di.module.ApplicationModule;
import com.vttm.mochaplus.feature.data.AppDataManager;
import com.vttm.mochaplus.feature.utils.AppLogger;

import javax.inject.Inject;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationController extends Application {

    @Inject
    AppDataManager mDataManager;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        AppLogger.init();

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        CalligraphyConfig.initDefault(mCalligraphyConfig);

        //Init Realm
        Realm.init(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
