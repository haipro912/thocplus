package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.VideoModel;

import java.util.List;

public class VideoResponse {
    @SerializedName("lastId")
    @Expose
    private Integer lastId;
    @SerializedName("lastIdStr")
    @Expose
    private String lastIdStr;
    @SerializedName("result")
    @Expose
    private List<VideoModel> result = null;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("ExecuteTime")
    @Expose
    private Integer executeTime;
    @SerializedName("code")
    @Expose
    private Integer code;

    public VideoResponse(Object result) {

    }

    public Integer getLastId() {
        return lastId;
    }

    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    public String getLastIdStr() {
        return lastIdStr;
    }

    public void setLastIdStr(String lastIdStr) {
        this.lastIdStr = lastIdStr;
    }

    public List<VideoModel> getResult() {
        return result;
    }

    public void setResult(List<VideoModel> result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
