package com.vttm.mochaplus.feature.di.component;

import com.vttm.mochaplus.feature.MainActivity;
import com.vttm.mochaplus.feature.di.PerActivity;
import com.vttm.mochaplus.feature.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by HaiKE on 6/7/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

}
