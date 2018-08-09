package com.vttm.chatlib.packet;

import java.io.Serializable;

/**
 * Created by HaiKE on 3/13/18.
 */

public class KeyValueConfig implements Serializable {
    private long id;
    private String key;
    private String value;

    public KeyValueConfig()
    {

    }

    public KeyValueConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
