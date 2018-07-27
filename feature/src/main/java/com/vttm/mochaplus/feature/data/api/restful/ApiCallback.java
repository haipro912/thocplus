package com.vttm.mochaplus.feature.data.api.restful;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HaiKE on 11/29/16.
 */

public class ApiCallback<T> implements Callback<T> {

    private Callback<T> callback;

    private boolean canceled;

    public ApiCallback() {}

    public ApiCallback(Callback<T> callback) {
        this.callback = callback;
        canceled = false;
    }

    public void cancel() {
        canceled = true;
        callback = null;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!canceled) {
            callback.onResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!canceled) {
            callback.onFailure(call, t);
        }
    }
}