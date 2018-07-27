package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.VideoCategoryModel;

import java.util.List;

public class VideoCategoryResponse {
    @SerializedName("result")
    @Expose
    private List<VideoCategoryModel> result = null;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("code")
    @Expose
    private String code;

    public VideoCategoryResponse(Object result) {

    }

    public List<VideoCategoryModel> getResult() {
        return result;
    }

    public void setResult(List<VideoCategoryModel> result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
