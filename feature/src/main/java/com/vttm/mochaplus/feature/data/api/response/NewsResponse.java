package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.NewsModel;

import java.util.ArrayList;

/**
 * Created by HaiKE on 8/19/17.
 */

public class NewsResponse extends ErrorResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<NewsModel> data = new ArrayList<>();

    public ArrayList<NewsModel> getData() {
        return data;
    }

    public void setData(ArrayList<NewsModel> data) {
        this.data = data;
    }
}
