package com.stringee;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeIceCandidate {
    public final String sdpMid;
    public final int sdpMLineIndex;
    public final String sdp;

    public StringeeIceCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
        this.sdpMid = sdpMid;
        this.sdpMLineIndex = sdpMLineIndex;
        this.sdp = sdp;
    }

    public String toString() {
        return this.sdpMid + ":" + this.sdpMLineIndex + ":" + this.sdp;
    }
}
