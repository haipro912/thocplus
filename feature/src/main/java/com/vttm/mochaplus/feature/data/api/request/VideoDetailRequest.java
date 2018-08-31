package com.vttm.mochaplus.feature.data.api.request;

public class VideoDetailRequest {
    String url;

    public VideoDetailRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
