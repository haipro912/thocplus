package com.stringee;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeCreateSdpObserver implements SdpObserver {

    private boolean createOffer;
    private StringeeCall stringeeCall;

    public StringeeCreateSdpObserver(boolean createOffer, StringeeCall stringeeCall) {
        this.createOffer = createOffer;
        this.stringeeCall = stringeeCall;
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("CreateSdpObserver: onCreateSuccess");
        }
        Log.e("Stringee", "Create sdp success " + sessionDescription.description);
        SessionDescription modifiedSdp = sessionDescription;
        String preferedAudioCodec = stringeeCall.getPreferedAudioCodec();
        if (preferedAudioCodec != null) {
            String desc = StringeeUtils.preferCodec(modifiedSdp.description, preferedAudioCodec, true);
            modifiedSdp = new SessionDescription(modifiedSdp.type, desc);
        }

        String preferedVideoCodec = stringeeCall.getPreferedVideoCodec();
        if (preferedVideoCodec != null) {
            String desc = StringeeUtils.preferCodec(modifiedSdp.description, preferedVideoCodec, false);
            modifiedSdp = new SessionDescription(modifiedSdp.type, desc);
        }

        /*String desc = StringeeUtils.preferCodec(modifiedSdp.description, "VP9", false);
        modifiedSdp = new SessionDescription(modifiedSdp.type, desc);*/

        Log.e("Stringee", "SDP modified:  " + modifiedSdp.description);

        stringeeCall.setLocalDescription(modifiedSdp);
        StringeeSetSdpObserver setSdpObserver = new StringeeSetSdpObserver(true, createOffer, stringeeCall);
        stringeeCall.setLocalDescription(setSdpObserver, modifiedSdp);
    }

    @Override
    public void onCreateFailure(String s) {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("CreateSdpObserver: onCreateFailure: " + s);
        }
        if (createOffer) {
            Log.e("Stringee", "+++++++++++++++++++++++ SDP on Create Offer Failure: " + s);
        } else {
            Log.e("Stringee", "+++++++++++++++++++++++ SDP on Create Answer Failure: " + s);
        }
    }

    @Override
    public void onSetSuccess() {
       /* if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("CreateSdpObserver: onSetSuccess");
        }*/
    }

    @Override
    public void onSetFailure(String s) {
        if (stringeeCall.getConnectionListener() != null) {
            stringeeCall.getConnectionListener().onSuccess("CreateSdpObserver: onSetFailure");
        }
    }
}
