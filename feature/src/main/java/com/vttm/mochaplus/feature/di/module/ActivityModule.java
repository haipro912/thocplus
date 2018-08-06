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
import com.vttm.mochaplus.feature.mvp.contact.ContactPresenter;
import com.vttm.mochaplus.feature.mvp.contact.IContactPresenter;
import com.vttm.mochaplus.feature.mvp.contact.IContactView;
import com.vttm.mochaplus.feature.mvp.main.IMainPresenter;
import com.vttm.mochaplus.feature.mvp.main.IMainView;
import com.vttm.mochaplus.feature.mvp.main.MainPresenter;
import com.vttm.mochaplus.feature.mvp.video.detail.IVideoDetailPresenter;
import com.vttm.mochaplus.feature.mvp.video.detail.IVideoDetailView;
import com.vttm.mochaplus.feature.mvp.video.detail.VideoDetailPresenter;
import com.vttm.mochaplus.feature.mvp.video.home.IVideoPresenter;
import com.vttm.mochaplus.feature.mvp.video.home.IVideoView;
import com.vttm.mochaplus.feature.mvp.video.home.VideoPresenter;
import com.vttm.mochaplus.feature.mvp.video.main.ITabVideoPresenter;
import com.vttm.mochaplus.feature.mvp.video.main.ITabVideoView;
import com.vttm.mochaplus.feature.mvp.video.main.TabVideoPresenter;
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
    IMainPresenter<IMainView> provideMainPresenter(MainPresenter<IMainView> presenter) {
        return presenter;
    }

    @Provides
    IContactPresenter<IContactView> provideContactPresenter(ContactPresenter<IContactView> presenter) {
        return presenter;
    }

    @Provides
    ITabVideoPresenter<ITabVideoView> provideTabVideoPresenter(TabVideoPresenter<ITabVideoView> presenter) {
        return presenter;
    }

    @Provides
    IVideoPresenter<IVideoView> provideVideoPresenter(VideoPresenter<IVideoView> presenter) {
        return presenter;
    }

    @Provides
    IVideoDetailPresenter<IVideoDetailView> provideVideoDetailPresenter(VideoDetailPresenter<IVideoDetailView> presenter) {
        return presenter;
    }
}
