package com.stringee;

/**
 * Created by luannguyen on 9/27/16.
 */
public interface StringeeCallListener {

    public static enum StringeeConnectionState {
        NEW,
        CHECKING,
        CONNECTED,
        COMPLETED,
        FAILED,
        DISCONNECTED,
        CLOSED;

        private StringeeConnectionState() {

        }
    }

    public void onSetSdpSuccess(StringeeSessionDescription sdp);

    public void onCreateIceCandidate(StringeeIceCandidate iceCandidate);

    public void onChangeConnectionState(StringeeConnectionState state);

    public void onLocalStreamCreated(StringeeStream stream);

    public void onAddStream(StringeeStream StringeeStream);

}
