package com.stringee;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

/**
 * Created by luannguyen on 1/25/16.
 */
public class PCObserver implements PeerConnection.Observer {

    private StringeeCall stringeeCall;

    public PCObserver(StringeeCall stringeeCall) {
        this.stringeeCall = stringeeCall;
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.e("Stringee", "+++++++++++++++++++++++ onSignalingChange");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.e("Stringee", "+++++++++++++++++++++++ onIceConnectionChange " + iceConnectionState.toString());
        if (stringeeCall.listener != null) {
            StringeeCallListener.StringeeConnectionState state = StringeeCallListener.StringeeConnectionState.NEW;
            if (iceConnectionState == PeerConnection.IceConnectionState.NEW) {
                state = StringeeCallListener.StringeeConnectionState.NEW;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.CHECKING) {
                state = StringeeCallListener.StringeeConnectionState.CHECKING;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
                state = StringeeCallListener.StringeeConnectionState.CONNECTED;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.COMPLETED) {
                state = StringeeCallListener.StringeeConnectionState.COMPLETED;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.FAILED) {
                state = StringeeCallListener.StringeeConnectionState.FAILED;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
                state = StringeeCallListener.StringeeConnectionState.DISCONNECTED;
            } else if (iceConnectionState == PeerConnection.IceConnectionState.CLOSED) {
                state = StringeeCallListener.StringeeConnectionState.CLOSED;
            }

            if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
                stringeeCall.iceConnected = true;

                //add lai ice candidate con trong queue
                while (!stringeeCall.queueIceCandidate.isEmpty()) {
                    StringeeIceCandidate candidate = stringeeCall.queueIceCandidate.get(0);
                    stringeeCall.queueIceCandidate.remove(0);

                    stringeeCall.addIceCandidate(candidate, false);
                }
            }

            stringeeCall.listener.onChangeConnectionState(state);
        }
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Log.e("Stringee", "+++++++++++++++++++++++ onIceConnectionReceivingChange");
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.e("Stringee", "+++++++++++++++++++++++ onIceGatheringChange " + iceGatheringState.toString());
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.e("Stringee", "+++++++++++++++++++++++ onIceCandidate");

        if (stringeeCall.listener != null) {
            StringeeIceCandidate ice = new StringeeIceCandidate(iceCandidate.sdpMid, iceCandidate.sdpMLineIndex, iceCandidate.sdp);
            stringeeCall.listener.onCreateIceCandidate(ice);
        }
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        Log.e("Stringee", "+++++++++++++++++++++++ onIceCandidatesRemoved");
    }

    @Override
    public void onAddStream(final MediaStream stream) {
        Log.e("Stringee", "+++++++++++++++++++++++ onAddStream");
        if (stringeeCall.listener != null) {
            StringeeStream stringeeStream = new StringeeStream();
            stringeeStream.setMediaStream(stream);
            stringeeCall.listener.onAddStream(stringeeStream);
        }
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.e("Stringee", "+++++++++++++++++++++++ onRemoveStream");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.e("Stringee", "+++++++++++++++++++++++ onDataChannel");
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.e("Stringee", "+++++++++++++++++++++++ onRenegotiationNeeded");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

    }
}
