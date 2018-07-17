package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.ErrorModel;

/**
 * Created by HaiKE on 8/20/17.
 */

public class ErrorResponse {
    @SerializedName("error")
    @Expose
    private ErrorModel error;

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel errorModel) {
        this.error = errorModel;
    }
}
