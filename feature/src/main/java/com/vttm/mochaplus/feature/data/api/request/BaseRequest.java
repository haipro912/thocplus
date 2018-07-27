package com.vttm.mochaplus.feature.data.api.request;

public class BaseRequest {
    String revision;
    String domain;
    String clientType;
    String msisdn;
    String vip;

    public BaseRequest(String revision, String domain, String clientType, String msisdn, String vip) {
        this.revision = revision;
        this.domain = domain;
        this.clientType = clientType;
        this.msisdn = msisdn;
        this.vip = vip;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }
}
