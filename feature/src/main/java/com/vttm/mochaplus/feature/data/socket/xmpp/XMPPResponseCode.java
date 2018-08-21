package com.vttm.mochaplus.feature.data.socket.xmpp;

/**
 * Created by TSB on 6/29/2014.
 */
public class XMPPResponseCode {
    private int code;
    private String description;
    private Object contain;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getContain() {
        return contain;
    }

    public void setContain(Object contain) {
        this.contain = contain;
    }
}
