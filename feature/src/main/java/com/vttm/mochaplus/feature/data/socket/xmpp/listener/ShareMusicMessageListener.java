package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.business.MessageBusiness;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by thaodv on 26-Nov-14.
 */
public class ShareMusicMessageListener implements StanzaListener {
    private static final String TAG;

    static {
        TAG = ShareMusicMessageListener.class.getSimpleName();
    }

    private ApplicationController application;
    private MessageBusiness mMessageBusiness;

    public ShareMusicMessageListener(ApplicationController application) {
        this.application = application;
        mMessageBusiness = application.getMessageBusiness();
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
//        Log.i(TAG, "" + packet.toXML());
//        ShareMusicMessagePacket shareMusicMessagePacket = (ShareMusicMessagePacket) packet;
//        ReengMessagePacket.Type type = shareMusicMessagePacket.getType();
//        if (type == ReengMessagePacket.Type.normal && shareMusicMessagePacket.getTypeString() != null) {
//            // tra lai ban tin deliver voi type chua biet
//            String from = shareMusicMessagePacket.getFrom(); //lay truong from full domain
//            String fromJid = from.split("@")[0]; // lay truong from
//            ReengEventPacket packetSend = new ReengEventPacket();
//            packetSend.setSubType(ReengMessagePacket.SubType.event);
//            // neu sv can thi set no_route và no_store
//            //        packet.setSubType(ReengMessagePacket.SubType.no_route);
//            //        packet.setNoStore(true);
//            packetSend.setEventType(ReengEventPacket.EventType.delivered);
//            packetSend.setPacketID(PacketMessageId.getInstance().
//                    genPacketId(packetSend.getType().toString(), packetSend.getSubType().toString()));
//            packetSend.addToListIdOfEvent(shareMusicMessagePacket.getPacketID());
//            packetSend.setTypeString(shareMusicMessagePacket.getTypeString());
//            // ban tin A đến thì gui lai A luon (full domain)
//            String myNumber = application.getReengAccountBusiness().getJidNumber();
//            if (myNumber != null && myNumber.equals(fromJid)) {// ko gui deliver cho chinh minh
//                return;
//            }
//            packet.setTo(from);
//            // gui ban tin
//            application.getXmppManager().sendXmppPacket(packetSend);
//        } else {
//            mMessageBusiness.getIncomingMessageProcessor().processIncomingShareMusicMessage(application, shareMusicMessagePacket);
//        }
    }
}
