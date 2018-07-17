package com.vttm.mochaplus.feature.data.api.request;

/**
 * Created by HaiKE on 8/19/17.
 */

public class NewsContentRequest {

    int pid;
    int cateId;
    int contentId;
    int page;
    long unixTime;

    public NewsContentRequest(int pid, int contentId, int page, long unixTime) {
        this.pid = pid;
        this.contentId = contentId;
        this.page = page;
        this.unixTime = unixTime;
    }

    public NewsContentRequest(int pid, int cateId, int contentId)
    {
        this.contentId = contentId;
        this.cateId =cateId;
        this.pid = pid;
    }

    public NewsContentRequest(int pid, int cateId, int contentId, int page, long unixTime) {
        this.pid = pid;
        this.cateId = cateId;
        this.contentId = contentId;
        this.page = page;
        this.unixTime = unixTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
