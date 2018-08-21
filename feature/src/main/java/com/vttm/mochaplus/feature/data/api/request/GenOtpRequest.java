package com.vttm.mochaplus.feature.data.api.request;

public class GenOtpRequest {

    String username;
    String countryCode;

    public GenOtpRequest(String username, String countryCode) {
        this.username = username;
        this.countryCode = countryCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
