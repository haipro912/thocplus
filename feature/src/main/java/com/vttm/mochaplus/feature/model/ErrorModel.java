package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HaiKE on 8/20/17.
 */

public class ErrorModel implements Serializable {
    @SerializedName("Code")
    @Expose
    private int code;

    @SerializedName("Message")
    @Expose
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
