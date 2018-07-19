package com.vttm.mochaplus.feature.helper.image;

import android.graphics.Bitmap;

/**
 * Created by nguye on 14/07/16.
 */
public interface ImageLoaderCallback {
    void onCompleted(Bitmap bitmap);
    void onFailed(Exception e);
}
