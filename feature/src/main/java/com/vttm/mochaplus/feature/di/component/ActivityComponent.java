package com.vttm.mochaplus.feature.di.component;

import com.vttm.mochaplus.feature.MainActivity;
import com.vttm.mochaplus.feature.di.PerActivity;
import com.vttm.mochaplus.feature.di.module.ActivityModule;
import com.vttm.mochaplus.feature.mvp.call.CallFragment;
import com.vttm.mochaplus.feature.mvp.contact.ContactFragment;
import com.vttm.mochaplus.feature.mvp.video.home.VideoFragment;
import com.vttm.mochaplus.feature.mvp.video.main.TabVideoFragment;

import dagger.Component;

/**
 * Created by HaiKE on 6/7/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(ContactFragment contactFragment);
    void inject(TabVideoFragment contactFragment);
    void inject(VideoFragment contactFragment);
    void inject(CallFragment contactFragment);
}
