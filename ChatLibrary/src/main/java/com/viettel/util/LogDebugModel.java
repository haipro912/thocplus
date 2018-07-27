package com.viettel.util;

import java.text.SimpleDateFormat;

/**
 * Created by thanhnt72 on 4/4/2018.
 */

public class LogDebugModel {
    private long timeStamp;
    private String content;

    private static final String SDF_FULL = "yyyy/MM/dd HH:mm:ss.SSS";
    SimpleDateFormat spf = new SimpleDateFormat(SDF_FULL);

    public LogDebugModel(long timeStamp, String content) {
        this.timeStamp = timeStamp;
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return spf.format(timeStamp) + " | " + content;
    }


}
