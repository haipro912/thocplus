package com.vttm.mochaplus.feature.data.api.request;

public class VideoDetailRequest extends BaseRequest {
    String url;

    public VideoDetailRequest(String url, String revision, String domain, String clientType, String msisdn, String vip) {
        super(revision, domain, clientType, msisdn, vip);

        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
