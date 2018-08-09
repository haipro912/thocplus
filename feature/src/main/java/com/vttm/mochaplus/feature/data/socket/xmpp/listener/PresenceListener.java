package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.mochaplus.feature.ApplicationController;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

public class PresenceListener implements StanzaListener {
    private static final String TAG = PresenceListener.class.getSimpleName();
    private ApplicationController mContext;

    public PresenceListener(ApplicationController context) {
        this.mContext = context;
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
//        AppLogger.d(TAG, "processPacket: " + packet.toXML());
//        ApplicationController application = (ApplicationController) mContext.getApplicationContext();
//        Presence presence = (Presence) packet;
//        if (presence.getType() == Presence.Type.error) {
//            mContext.logDebugContent("Presence.Type.error: " + packet.toXML());
//            return;
//        }
//        Presence.SubType subType = presence.getSubType();
//        if (subType == Presence.SubType.normal) {
//            if (presence.getType() == Presence.Type.unavailable) {
//                // ban tin offline
//                application.getContactBusiness().updateContactAfterReceiverPresence(presence);
//            } else {
//                mContext.logDebugContent("SubType.normal Type.unavailable: " + packet.toXML());
//                Log.i(TAG, "subType nomarl and sub");
//            }
//        } else if (subType == Presence.SubType.change_domain) {
//            // change domain
//            boolean isDomainMsgChange = UrlConfigHelper.getInstance(mContext).
//                    updateDomainMocha(presence.getDomainFile(), presence.getDomainMsg(),
//                            presence.getDomainOnMedia(), presence.getDomainImage(), "");
//            // change domain keeng
//            UrlConfigHelper.getInstance(application).changeDomainKeeng(presence.getDomainServiceKeeng(),
//                    presence.getDomainMedia2Keeng(), presence.getDomainImageKeeng());
//            //change config
//            application.getReengAccountBusiness().setConfigFromServer(presence.getVip(),
//                    presence.getCBNV(), presence.getCall(), presence.getSSL(),
//                    presence.getSmsIn(), presence.getCallOut(), presence.getAvnoNumber(),
//                    presence.getMochaApi(), presence.getAvnoEnable(), presence.getTabCallEnable());
//            // call config
//            if (isDomainMsgChange) {// reconnect
//                application.getReengAccountBusiness().updateDomainTask();
//            }
//        } else if (subType == Presence.SubType.music_info ||
//                subType == Presence.SubType.music_sub ||
//                subType == Presence.SubType.music_unsub ||
//                subType == Presence.SubType.confide_accepted) {
//            application.getMusicBusiness().updateStrangerMusicAfterReceiverPresence(presence, subType);
//        } else if (subType == Presence.SubType.contactInfo) {
//            application.getContactBusiness().updateContactAfterReceiverPresenceV3(presence);
//        } else if (subType == Presence.SubType.star_sub ||
//                subType == Presence.SubType.star_unsub) {
//            Log.i(TAG, "subType star_sub/unSub");
//            // khong xu ly truong hop nay
//        } else if (subType == Presence.SubType.count_users) {// nhan dc tin bao hieu so nguoi follow sao
//            application.getMusicBusiness().processChangeMemberFollow(presence);
//        } else if (subType == Presence.SubType.change_background) {
//            application.getMusicBusiness().processChangeBackgroundRoom(presence);
//        } else if (subType == Presence.SubType.feedInfo) {
//            application.getFeedBusiness().updateFeedAfterReceiverPresence(presence);
//        } else if (subType == Presence.SubType.updateInfo) {
//            application.getReengAccountBusiness().updateUserInfoFromPresence(presence);
//        } else if (subType == Presence.SubType.package_info) {
//            application.getReengAccountBusiness().updateUserPackageInfo(presence);
//        } else if (subType == Presence.SubType.strangerLocation) {
//            application.getReengAccountBusiness().updateLocationId(presence.getLocationId());
//        } else {    // chang avatar, stt, bg,fg ....
//            application.getContactBusiness().updateContactAfterReceiverPresence(presence);
//        }
    }
}