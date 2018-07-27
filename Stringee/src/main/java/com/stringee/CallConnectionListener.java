package com.stringee;

/**
 * Created by toanvk2 on 12/2/2017.
 */

public interface CallConnectionListener {
    void onSuccess(String result);

    void onError(String err);

    void onInfo(String info);
}
