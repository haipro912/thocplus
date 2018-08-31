package com.vttm.mochaplus.feature.data.api.request;

public class VideoRelateRequest {
    int offset;
    int limit;
    String lastIdStr = "";
    String query;

    public VideoRelateRequest(String query, int offset, int limit, String lastIdStr) {
        this.offset = offset;
        this.limit = limit;
        this.lastIdStr = lastIdStr;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getLastIdStr() {
        return lastIdStr;
    }

    public void setLastIdStr(String lastIdStr) {
        this.lastIdStr = lastIdStr;
    }
}
