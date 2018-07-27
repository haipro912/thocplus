package com.vttm.mochaplus.feature.data.api.request;

public class VideoRequest extends BaseRequest {
    int offset;
    int limit;
    int categoryid;
    String lastIdStr;

    public VideoRequest(String revision, String domain, String clientType, String msisdn, String vip) {
        super(revision, domain, clientType, msisdn, vip);
    }

    public VideoRequest(int offset, int limit, int categoryid, String lastIdStr, String revision, String domain, String clientType, String msisdn, String vip) {
        super(revision, domain, clientType, msisdn, vip);

        this.offset = offset;
        this.limit = limit;
        this.categoryid = categoryid;
        this.lastIdStr = lastIdStr;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getLastIdStr() {
        return lastIdStr;
    }

    public void setLastIdStr(String lastIdStr) {
        this.lastIdStr = lastIdStr;
    }
}
