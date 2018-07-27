package org.jivesoftware.smack.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by toanvk2 on 10/6/2016.
 */

public class CallData implements Parcelable {
    private String type;
    private String data;

    public CallData() {

    }

    public CallData(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSdp() {
        return "sdp".equals(type);
    }

    public String toXml() {
        StringBuilder buf = new StringBuilder();
        buf.append("<calldata>");
        buf.append("<type>").append("<![CDATA[").append(type).append("]]>").append("</type>");
        buf.append("<data>").append("<![CDATA[").append(data).append("]]>").append("</data>");
        buf.append("</calldata>");
        return buf.toString();
    }

    public String toString() {
        return "type: " + type + " data: " + data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CallData(Parcel parcel) {
        type = parcel.readString();
        data = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(data);
    }

    public static final Parcelable.Creator<CallData> CREATOR = new Parcelable.Creator<CallData>() {
        public CallData createFromParcel(Parcel in) {
            return new CallData(in);
        }

        public CallData[] newArray(int size) {
            return new CallData[size];
        }
    };
}