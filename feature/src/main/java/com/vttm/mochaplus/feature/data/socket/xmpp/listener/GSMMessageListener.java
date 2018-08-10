package com.vttm.mochaplus.feature.data.socket.xmpp.listener;


import com.vttm.chatlib.packet.GSMMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.business.MessageBusiness;
import com.vttm.mochaplus.feature.utils.AppLogger;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

public class GSMMessageListener implements StanzaListener {
    private ApplicationController context;
    private static final String TAG = "GSMMessageListener";
    private MessageBusiness mMessageBusiness;

    public GSMMessageListener(ApplicationController context) {
        this.context = context;
        mMessageBusiness = context.getMessageBusiness();
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
        // get packet from message
        GSMMessagePacket message = (GSMMessagePacket) packet;
//        AppLogger.i(TAG, "" + message.toXML());
        mMessageBusiness.processGsmResponse(message);
    }
}