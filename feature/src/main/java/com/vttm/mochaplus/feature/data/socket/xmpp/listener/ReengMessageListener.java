package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.business.MessageBusiness;
import com.vttm.mochaplus.feature.utils.AppLogger;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by toanvk2 on 11/1/14.
 */
public class ReengMessageListener implements StanzaListener {
    private static final String TAG = ReengMessageListener.class.getSimpleName();
    private ApplicationController application;
    private MessageBusiness mMessageBusiness;
    private ReengMessagePacket.Type type;

    public ReengMessageListener(ApplicationController application) {
        this.application = application;
        mMessageBusiness = application.getMessageBusiness();
    }
    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
        AppLogger.i(TAG, packet.toString());
        ReengMessagePacket receivedPacket = (ReengMessagePacket) packet;
        type = receivedPacket.getType();
        if (type.equals(ReengMessagePacket.Type.error)) {
            mMessageBusiness.processResponseForNonReengUser(receivedPacket);
        } else if (type == ReengMessagePacket.Type.normal && receivedPacket.getTypeString() != null) {
            // gui lai deliver voi message khong biet type
            mMessageBusiness.getIncomingMessageProcessor().
                    processIncomingNotSupportTypeMessage(application, receivedPacket,
                            receivedPacket.getTypeString(), packet, type);
        } else {
            //  get type
            int mThreadType;
            if (type == ReengMessagePacket.Type.event || ReengMessagePacket.SubType.containsEvent(receivedPacket.getSubType())) {
                // gui confirm deliver khi nhan dc event
                String from = receivedPacket.getFrom().split("@")[0];     //lay from
                String packetId = receivedPacket.getPacketID();
                application.getXmppManager().sendConfirmDeliverMessage(packetId, from, null, -1);
            }
            if (type == ReengMessagePacket.Type.chat) {
                mThreadType = ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT;
            } else if (type == ReengMessagePacket.Type.groupchat) {
                mThreadType = ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT;
            } else if (type == ReengMessagePacket.Type.offical) {
                mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
            } else if (type == ReengMessagePacket.Type.roomchat) {
                mThreadType = ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT;
            } else if (type == ReengMessagePacket.Type.event) {
                mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
            } else {
                // gui lai deliver voi message type khong support
                mMessageBusiness.getIncomingMessageProcessor().
                        processIncomingNotSupportTypeMessage(application, receivedPacket,
                                receivedPacket.getTypeString(), packet, type);
                return;
            }
            // kiem tra neu dang insert thread message vao database thi wait den khi nao insert xong.
            // neu chua insert db xong ma xu ly tiep thi co kha nang bi tach thread
            while (mMessageBusiness.isWaitInsertThread()) {
                Log.d(TAG, "mMessageBusiness.isWaitInsertThread()");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException", e);
                }
            }
            switch (receivedPacket.getSubType()) {
                case empty:
                    Log.d(TAG, "ReengMessageListener  subtype empty");
                    // gui lai deliver, khong xu ly gi
                    mMessageBusiness.getIncomingMessageProcessor().
                            processIncomingNoSubtypeReengMessage(application, receivedPacket, mThreadType);
                    break;
                case normal:
                    mMessageBusiness.getIncomingMessageProcessor().
                            processIncomingNotSupportReengMessage(application, receivedPacket, mThreadType, packet);
                    break;
                case event:
                    mMessageBusiness.processDeliverReengPacket(application, (ReengEventPacket) packet);
                    break;
                case sms_out:
                    mMessageBusiness.processResponseSmsOutPacket(application, (ReengEventPacket) packet, mThreadType);
                    break;
                case sms_in:
                case no_route:
                    // phong th server  bắn về bản tin no_route, ko xu ly
                    break;
                case typing:
                    mMessageBusiness.notifyTypingMessage((ReengEventPacket) packet);
                    break;
                case update:
                    mMessageBusiness.processForceUpdate((ReengEventPacket) packet);
                    break;
                case upgrade:
                    Log.d(TAG, "upgrade");
                    // gui lai deliver, khong xu ly gi
                    mMessageBusiness.getIncomingMessageProcessor().
                            processIncomingNoSubtypeReengMessage(application, receivedPacket, mThreadType);
                    break;
                case config:
                    mMessageBusiness.getIncomingMessageProcessor().processIncomingEventMessage(receivedPacket);
                    break;
                case notification_image:
                    mMessageBusiness.getIncomingMessageProcessor().processIncomingNotificationImage(receivedPacket);
                    break;
                case inbox_banner:
                    application.getConfigBusiness().processIncomingBannerMessage((ReengEventPacket) receivedPacket);
                    break;
                // event room
                case event_expired:
                case star_unfollow:
                    mMessageBusiness.getIncomingMessageProcessor().processConfigRoomChat(application,
                            (ReengEventPacket) packet, type);
                    break;
                case music_action:
                case music_invite:
                case music_action_response:
                case music_leave:
                case music_ping:
                case music_pong:
                case music_stranger_accept:
                case music_stranger_reinvite:
                case music_request_change:
                    if (type == ReengMessagePacket.Type.roomchat) {
                        mMessageBusiness.getIncomingMessageProcessor().processIncomingRoomMusic(application, (ReengMusicPacket) packet);
                    } else if (type == ReengMessagePacket.Type.groupchat) {
                        mMessageBusiness.getIncomingMessageProcessor().processIncomingGroupMusic(application, (ReengMusicPacket) packet);
                    } else {
                        mMessageBusiness.getIncomingMessageProcessor().processIncomingReengMusic(application, (ReengMusicPacket) packet, type);
                    }
                    break;
                case music_accept:
                    mMessageBusiness.getIncomingMessageProcessor().processIncomingAcceptRoomMusic(application,
                            (ReengMusicPacket) packet, type);
                    break;
                case crbt_gift:// tang nhac cho
                    mMessageBusiness.getIncomingMessageProcessor().processIncomingCrbtGiftMessage(application,
                            (ReengMusicPacket) packet, type);
                    break;
                case call:
                case call_rtc:
                    application.getCallBusiness().processIncomingCallOld((ReengCallPacket) receivedPacket, type);
                    break;
                case call_rtc_2:
                    application.getCallBusiness().processIncomingCallMessage((ReengCallPacket) receivedPacket, type);
                    break;
                case call_out:
                    application.getCallBusiness().processIncomingCallOutMessage((ReengCallOutPacket) receivedPacket, type);
                    break;
                case call_in:
                    application.getCallBusiness().processIncomingCallInMessage((ReengCallOutPacket) receivedPacket, type);
                    break;
                case watch_video:
                    mMessageBusiness.getIncomingMessageProcessor().processIncomingWatchVideo(application, (ReengMusicPacket) packet, type);
                    break;
                case delconfig:
                    application.getConfigBusiness().processDelConfig((ReengMessagePacket) packet);
                    break;
                case setconfig:
                    application.getConfigBusiness().processSetConfig((ReengMessagePacket) packet);
                    break;
                case sticky_banner:
                    application.getConfigBusiness().processConfigStickyBanner((ReengMessagePacket) packet);
                    break;
                default:// text, file, voice,iamge,contact,video, create,rename,invite, leave......
                    if (type == ReengMessagePacket.Type.chat) {
                        mMessageBusiness.getIncomingMessageProcessor().
                                processIncomingReengMessage(application, receivedPacket);
                    } else if (type == ReengMessagePacket.Type.groupchat) {
                        ReengMessagePacket.SubType subType = receivedPacket.getSubType();
                        if (ReengMessagePacket.SubType.containsConfigGroup(subType)) {
                            mMessageBusiness.processMessageConfigGroup(application, receivedPacket);
                        } else {
                            mMessageBusiness.getIncomingMessageProcessor().
                                    processIncomingGroupMessage(application, receivedPacket);
                        }
                    } else if (type == ReengMessagePacket.Type.offical) {
                        mMessageBusiness.processIncomingOfficerMessage(application, receivedPacket);
                    } else if (type == ReengMessagePacket.Type.roomchat) {
                        mMessageBusiness.getIncomingMessageProcessor().
                                processIncomingRoomMessage(application, receivedPacket);
                    }
                    break;
            }
        }
    }
}