package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.chatlib.packet.VoiceMailGSMMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.business.MessageBusiness;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

public class VoicemailGSMResponseMessageListener implements StanzaListener {
    private static final String TAG = VoicemailGSMResponseMessageListener.class.getSimpleName();
    private MessageBusiness mMessageBusiness;

    public VoicemailGSMResponseMessageListener(ApplicationController context) {
        mMessageBusiness = context.getMessageBusiness();

    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
//        Log.i(TAG, "" + packet.toXML());
        VoiceMailGSMMessagePacket message = (VoiceMailGSMMessagePacket) packet;
        mMessageBusiness.processGsmResponse(message);
    }
}