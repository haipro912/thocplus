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

import android.app.Activity;
import android.content.Context;


import com.vttm.mochaplus.feature.di.ActivityContext;
import com.vttm.mochaplus.feature.di.PerActivity;
import com.vttm.mochaplus.feature.mvp.main.IMainPresenter;
import com.vttm.mochaplus.feature.mvp.main.IMainView;
import com.vttm.mochaplus.feature.mvp.main.MainPresenter;
import com.vttm.mochaplus.feature.utils.rx.AppSchedulerProvider;
import com.vttm.mochaplus.feature.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    IMainPresenter<IMainView> provideMainPresenter(MainPresenter<IMainView>
                                                           presenter) {
        return presenter;
    }

//    @Provides
//    @PerActivity
//    ISplashPresenter<ISplashView> provideSplashPresenter(SplashPresenter<ISplashView>
//                                                                 presenter) {
//        return presenter;
//    }
//
//    @Provides
//    ITabNewsPresenter<ITabNewsView> provideTabNewsPresenter(TabNewsPresenter<ITabNewsView>
//                                                                    presenter) {
//        return presenter;
//    }
//
//    @Provides
//    ITabRadioPresenter<ITabRadioView> provideTabRadioPresenter(TabRadioPresenter<ITabRadioView>
//                                                                       presenter) {
//        return presenter;
//    }
//
//    @Provides
//    IChildNewsPresenter<IChildNewsView> provideChildNewsPresenter(ChildNewsPresenter<IChildNewsView>
//                                                                          presenter) {
//        return presenter;
//    }


}
