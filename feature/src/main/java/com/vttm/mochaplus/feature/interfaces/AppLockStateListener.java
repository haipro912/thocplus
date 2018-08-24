package com.vttm.mochaplus.feature.interfaces;

import com.vttm.mochaplus.feature.mvp.base.BaseActivity;

/**
 * Created by toanvk2 on 3/9/2016.
 */
public interface AppLockStateListener {
    void onWentToBackground();

    void onWentToForeground(BaseActivity activity);

    void onActivityStarted(BaseActivity activity);
}
