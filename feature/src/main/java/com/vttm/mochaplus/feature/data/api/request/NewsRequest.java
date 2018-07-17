package com.vttm.mochaplus.feature.data.api.request;

/**
 * Created by HaiKE on 8/19/17.
 */

public class NewsRequest {

    int cateId;
    int num;
    int page;
    long unixTime;

    public NewsRequest(int cateId, int page, int num)
    {
        this.page = page;
        this.num = num;
        this.cateId =cateId;
    }

    public NewsRequest(int cateId, int page, int num, long unixTime) {
        this.cateId = cateId;
        this.num = num;
        this.page = page;
        this.unixTime = unixTime;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
