package com.stringee;

import android.util.Log;

import org.webrtc.MediaConstraints;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeSetSdpObserver implements SdpObserver {

    private boolean setOfferSdp;
    private boolean setLocalSdp;
    private StringeeCall stringeeCall;

    public StringeeSetSdpObserver(boolean setLocalSdp, boolean setOfferSdp, StringeeCall stringeeCall) {
        this.setLocalSdp = setLocalSdp;
        this.setOfferSdp = setOfferSdp;
        this.stringeeCall = stringeeCall;
    }

    @Override
    public void onSetSuccess() {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("SetSdpObserver: onSetSuccess");
        }
        if (setLocalSdp) {
            Log.e("Stringee", "...................................... SDP on Set local Success");
            SessionDescription sessionDescription = stringeeCall.getLocalDescription();
            if (stringeeCall.listener != null && !stringeeCall.isSdpSent()) {
                StringeeSessionDescription.Type type = StringeeSessionDescription.Type.OFFER;
                if (sessionDescription.type == SessionDescription.Type.OFFER) {
                    type = StringeeSessionDescription.Type.OFFER;
                } else if (sessionDescription.type == SessionDescription.Type.PRANSWER) {
                    type = StringeeSessionDescription.Type.PRANSWER;
                } else if (sessionDescription.type == SessionDescription.Type.ANSWER) {
                    type = StringeeSessionDescription.Type.ANSWER;
                }
                StringeeSessionDescription sdp = new StringeeSessionDescription(type, sessionDescription.description);

                stringeeCall.listener.onSetSdpSuccess(sdp);
                stringeeCall.setSdpSent(true);
            }
        } else {
            Log.e("Stringee", "...................................... SDP on Set remote Success");
        }
        if (setOfferSdp) {
            //            Log.e("Stringee", "...................................... set Offer");
        } else {
            //            Log.e("Stringee", "...................................... set Answer");
        }

        if (!setLocalSdp && setOfferSdp) {
            //set remote SDP thanh cong va remote SDP la Offer:
            //
            // Create SDP constraints.
            MediaConstraints sdpMediaConstraints = new MediaConstraints();
            sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
            sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
            //sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));
            //create thanh cong thi ham: onCreateSuccess trong SDPObserver se duoc goi
            Log.e("Stringee", "+++++++++++++++++++++++ create answer");
            StringeeCreateSdpObserver createSdpObserver = new StringeeCreateSdpObserver(false, stringeeCall);
            stringeeCall.createAnswer(createSdpObserver, sdpMediaConstraints);
        }
    }

    @Override
    public void onSetFailure(String s) {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("SetSdpObserver: onSetFailure: " + s);
        }
        if (setLocalSdp) {
            Log.e("Stringee", "+++++++++++++++++++++++ SDP on Set local Failure: " + s);
        } else {
            Log.e("Stringee", "+++++++++++++++++++++++ SDP on Set remote Failure: " + s);
        }
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        /*if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("SetSdpObserver: onCreateSuccess");
        }*/
    }

    @Override
    public void onCreateFailure(String s) {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("SetSdpObserver: onCreateFailure");
        }
    }
}
