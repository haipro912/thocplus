package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.NewsModel;

/**
 * Created by HaiKE on 8/19/17.
 */

public class NewsContentResponse extends ErrorResponse {
    @SerializedName("data")
    @Expose
    private NewsModel data;

    public NewsModel getData() {
        return data;
    }

    public void setData(NewsModel data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NewsContentResponse{" +
                "data=" + data +
                '}';
    }
}
