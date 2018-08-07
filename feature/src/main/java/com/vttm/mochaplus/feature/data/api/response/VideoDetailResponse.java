package com.vttm.mochaplus.feature.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.model.VideoModel;

public class VideoDetailResponse {
    @SerializedName("lastId")
    @Expose
    private Integer lastId;
    @SerializedName("video_detail")
    @Expose
    private VideoModel videoDetail;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("ExecuteTime")
    @Expose
    private Integer executeTime;
    @SerializedName("code")
    @Expose
    private Integer code;

    public Integer getLastId() {
        return lastId;
    }

    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    public VideoModel getVideoDetail() {
        return videoDetail;
    }

    public void setVideoDetail(VideoModel videoDetail) {
        this.videoDetail = videoDetail;
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
