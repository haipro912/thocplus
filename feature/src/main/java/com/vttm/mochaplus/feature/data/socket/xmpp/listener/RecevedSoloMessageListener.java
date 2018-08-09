package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.mochaplus.feature.ApplicationController;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

public class RecevedSoloMessageListener implements StanzaListener {
    private ApplicationController application;

    public RecevedSoloMessageListener(ApplicationController application) {
        this.application = application;
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
//        EventReceivedMessage event = (EventReceivedMessage) packet;
//        MessageBusiness messageBusiness = application.getMessageBusiness();
//        messageBusiness.processReceivedPacket(application, event);
    }
}