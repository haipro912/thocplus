package com.vttm.mochaplus.feature.business;

import com.vttm.chatlib.packet.EventReceivedMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;

import org.jivesoftware.smack.packet.Stanza;

public class MessageBusiness {
    private static final String TAG = MessageBusiness.class.getSimpleName();
    private ApplicationController mApplication;


    public MessageBusiness(ApplicationController app) {
        this.mApplication = app;
    }

    public void init() {

    }

    public void processGsmResponse(Stanza message) {
        
    }

    public void processReceivedPacket(ApplicationController application, EventReceivedMessagePacket event) {

    }
}
