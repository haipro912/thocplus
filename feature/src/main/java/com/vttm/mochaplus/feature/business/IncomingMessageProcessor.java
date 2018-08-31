package com.vttm.mochaplus.feature.business;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.chatlib.packet.ShareMusicMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.db.model.OfficerAccountConstant;
import com.vttm.mochaplus.feature.data.db.model.ReengMessageConstant;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.helper.NetworkHelper;
import com.vttm.mochaplus.feature.helper.PacketMessageId;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.model.ReengMessage;

import org.jivesoftware.smack.packet.Packet;

import java.util.Date;

/**
 * Created by thaodv on 22-Dec-14.
 */
public class IncomingMessageProcessor {
    private static final String TAG = IncomingMessageProcessor.class.getSimpleName();
    private final Resources mRes;
    private MessageBusiness mMessageBusiness;
    private ApplicationController mApplication;
    private String myNumber;
    private ContactBusiness mContactBusiness;
    private BlockContactBusiness mBlockContactBusiness;

    public IncomingMessageProcessor(MessageBusiness business, ApplicationController app) {
        mMessageBusiness = business;
        mApplication = app;
        mRes = app.getResources();
        mBlockContactBusiness = mApplication.getBlockContactBusiness();
    }

//    private ReengMessage mapSmsToReengMessage(int threadId, String userName, String smsContent, String smsSender) {
//        ReengMessage message = new ReengMessage();
//        message.setReceiver(userName);
//        message.setSender(smsSender);
//        message.setReadState(ReengMessageConstant.READ_STATE_UNREAD);
//        message.setThreadId(threadId);
//        message.setDirection(ReengMessageConstant.Direction.received);
//        // set time
//        Date date = new Date();
//        message.setTime(date.getTime());
//        message.setChatMode(ReengMessageConstant.MODE_GSM);
//        message.setMessageType(ReengMessageConstant.MessageType.text);
//        message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//        message.setContent(smsContent);
//        return message;
//    }
//
//    protected void processIncomingOfficerMessage(ApplicationController application,
//                                                 ReengMessagePacket receivedMessage,
//                                                 String officerId) {
//        mApplication.logDebugContent("processIncomingOfficerMessage: " + receivedMessage.toXML());
//        String officerName = receivedMessage.getOfficalName();                 //lay ten offical account
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        String avatarUrl = receivedMessage.getAvatarUrl();
//        ReengMessagePacket.SubType subType = receivedMessage.getSubType();
//        ThreadMessage mCorrespondingThread = mMessageBusiness.
//                findExistingOrCreateOfficerThread(officerId, officerName, avatarUrl, OfficerAccountConstant
//                        .ONMEDIA_TYPE_NONE);
//        if (subType == ReengMessagePacket.SubType.toast) {
//            if (!TextUtils.isEmpty(receivedMessage.getBody())) {
//                mMessageBusiness.onNonReengResponse(mCorrespondingThread.getId(), null, false, receivedMessage
//                        .getBody());
//            }
//        } else {
//            ReengMessage reengMessage = mapPacketToReengMessage(receivedMessage,
//                    mCorrespondingThread.getId(), officerId, myNumber, subType);
//            mMessageBusiness.notifyReengMessage(application,
//                    mCorrespondingThread, reengMessage, ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT);
//        }
//    }
//
//    /**
//     * chuyen packet nhan duoc thanh message, ap dung cho cac tin nhan den
//     *
//     * @param packet
//     */
//    public ReengMessage mapPacketToReengMessage(ReengMessagePacket packet,
//                                                int threadId, String fromNumber,
//                                                String userNumber, ReengMessagePacket.SubType subType) {
//        ReengMessage message = new ReengMessage();
//        message.setPacketId(packet.getPacketID()); // set packet id
//        message.setReceiver(userNumber);
//        message.setReadState(ReengMessageConstant.READ_STATE_UNREAD);
//        message.setThreadId(threadId);
//        message.setDirection(ReengMessageConstant.Direction.received);
//        // set time
//        message.setTime(packet.getTimeSend());
//        message.setExpired(packet.getExpired());
//        ReengMessagePacket.Type type = packet.getType();
//        // get file id (truong hop khong co thi de mac dinh la 0)
//        String fileIdString = packet.getFileId();
//        message.setReplyDetail(packet.getReply());
//        if (type == ReengMessagePacket.Type.chat) {
//            String sender = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(fromNumber);
//            if (sender == null)
//                message.setSender(fromNumber);
//            else
//                message.setSender(sender);
//        } else if (type == ReengMessagePacket.Type.groupchat) {
//            String sender = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(packet.getSender());
//            if (sender == null)
//                message.setSender(packet.getSender());
//            else
//                message.setSender(sender);
//            message.setSenderName(packet.getSenderName());
//            message.setSenderAvatar(packet.getLastAvatar());
//        } else if (type == ReengMessagePacket.Type.offical) {
//            message.setSender(fromNumber);
//        } else if (type == ReengMessagePacket.Type.roomchat) {
//            String sender = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(packet.getSender());
//            if (sender == null)
//                message.setSender(packet.getSender());
//            else
//                message.setSender(sender);
//            message.setSenderName(packet.getSenderName());
//            message.setSticky(packet.getStickyState());
//            message.setSenderAvatar(packet.getAvatarUrl());
//        }
//        switch (subType) {
//            case text:
//                message.setMessageType(ReengMessageConstant.MessageType.text);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setContent(packet.getBody());
//                message.setFileId(packet.getLargeEmo());// luu thong tin large emo vao truong fileId
//                // add queue decode emoticon
//                mApplication.getQueueDecodeEmo().addTask(message.getContent());
//                message.setTagContent(packet.getTextTag());
//                break;
//            case file_2:
//                String fileType;
//                String fileName = packet.getName();
//                if (MessageHelper.isImage(fileName)) {
//                    message.setMessageType(ReengMessageConstant.MessageType.image);
//                    fileType = "image";
//                    message.setFileName(MessageHelper.getUniqueFileName(fileName, fileType));
//                    message.setFileType("image");
//                    message.setVideoContentUri(packet.getRatio());
//                } else {
//                    message.setMessageType(ReengMessageConstant.MessageType.file);
//                    fileType = ReengMessageConstant.FileType.fromString(FileHelper.getExtensionFile(fileName))
//                            .toString();
//                    message.setFileType(fileType);
//                    message.setFileName(fileName);
//                }
//                message.setStatus(ReengMessageConstant.STATUS_NOT_LOAD);
//                message.setFileId(fileIdString);
//                message.setSize(packet.getSize());
//                message.setDirectLinkMedia(packet.getMediaLink());
//                break;
//            case image:
//                String imageName = packet.getName();
//                message.setMessageType(ReengMessageConstant.MessageType.image);
//                message.setFileType("image");
//                String uniqueImageName = MessageHelper.getUniqueFileName(imageName, "image");
//                Log.i(TAG, "uniqueImageName: " + uniqueImageName);
//                message.setFileName(uniqueImageName);
//                message.setStatus(ReengMessageConstant.STATUS_NOT_LOAD);
//                message.setFileId(fileIdString);
//                message.setSize(packet.getSize());
//                message.setDirectLinkMedia(packet.getMediaLink());
//                message.setContent(packet.getBody());
//                message.setVideoContentUri(packet.getRatio());
//                break;
//            case voicemail:
//                String voiceName = packet.getName();
//                message.setMessageType(ReengMessageConstant.MessageType.voicemail);
//                message.setFileType("voicemail");
//                String uniqueVoiceName = MessageHelper.getUniqueFileName(voiceName, "voicemail");
//                message.setFileName(uniqueVoiceName);
//                message.setStatus(ReengMessageConstant.STATUS_NOT_LOAD);
//                message.setFileId(fileIdString);
//                message.setSize(packet.getSize());
//                message.setDuration(packet.getDuration());
//                message.setDirectLinkMedia(packet.getMediaLink());
//                break;
//            case contact:
//                message.setMessageType(ReengMessageConstant.MessageType.shareContact);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setContent(packet.getName());
//                String newNumb = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(packet.getTel());
//                message.setFileName(newNumb == null ? packet.getTel() : newNumb);
//                break;
//            case sharevideov2:
//                String videoName = packet.getName();
//                message.setMessageType(ReengMessageConstant.MessageType.shareVideo);
//                message.setFileType("sharevideov2");
//                String uniqueVideoName = MessageHelper.getUniqueFileName(videoName, "sharevideov2");
//                message.setFileName(uniqueVideoName);
//                message.setStatus(ReengMessageConstant.STATUS_NOT_LOAD);
//                message.setFileId(fileIdString);
//                message.setSize(packet.getSize());
//                message.setDuration(packet.getDuration());
//                message.setImageUrl(packet.getVideoThumb());
//                message.setDirectLinkMedia(packet.getMediaLink());
//                break;
//            case voicesticker:
//                message.setMessageType(ReengMessageConstant.MessageType.voiceSticker);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setFileName(packet.getStickerPacket());
//                message.setSongId(packet.getStickerId());
//                break;
//            case greeting_voicesticker:
//                message.setMessageType(ReengMessageConstant.MessageType.greeting_voicesticker);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setContent(packet.getBody());
//                message.setFilePath(packet.getJsonListSticker());
//                break;
//            case invite_friend:
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                message.setContent(packet.getBody());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case invite_success:
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                message.setContent(packet.getBody());
//                message.setStatus(ReengMessageConstant.STATUS_SENT);
//                //message.setDirection(ReengMessageConstant.Direction.send);
//                break;
//            case location:
//                message.setMessageType(ReengMessageConstant.MessageType.shareLocation);
//                message.setContent(packet.getBody());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setFilePath(packet.getLat());// luu latitude vao file path
//                message.setImageUrl(packet.getLng());// luu longitude vao imageurl
//                break;
//            case restore:
//                message.setMessageType(ReengMessageConstant.MessageType.restore);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setFileName(fileIdString);  //luu tam packet ID cua message thu hoi
//                break;
//            case transfer_money:
//                message.setMessageType(ReengMessageConstant.MessageType.transferMoney);
//                message.setContent(packet.getAmountMoney());// luu amount vao content
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setFilePath(packet.getTimeTransferMoney());// luu time transfer
//                message.setImageUrl(packet.getUnitMoney());// luu unit money
//                break;
//            case event_sticky:
//                message.setMessageType(ReengMessageConstant.MessageType.event_follow_room);
//                message.setContent(packet.getBody());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setFileName(packet.getEventRoomName());
//                message.setFilePath(packet.getEventRoomId());
//                message.setImageUrl(packet.getEventRoomAvatar());
//                break;
//            case notification:
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                message.setContent(packet.getBody());
//                // deeplink notification
//                message.setDeepLinkLeftLabel(packet.getDlLeftLabel());
//                message.setDeepLinkLeftAction(packet.getDlLeftAction());
//                message.setDeepLinkRightLabel(packet.getDlRightLabel());
//                message.setDeepLinkRightAction(packet.getDlRightAction());
//                message.setFilePath(packet.getServiceType());
//                message.setDeepLinkInfo(true);  // convert sang json luu vao db
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case deeplink:
//                message.setMessageType(ReengMessageConstant.MessageType.deep_link);
//                message.setContent(packet.getBody());
//                message.setDeepLinkLeftLabel(packet.getDlLeftLabel());
//                message.setDeepLinkLeftAction(packet.getDlLeftAction());
//                message.setDeepLinkRightLabel(packet.getDlRightLabel());
//                message.setDeepLinkRightAction(packet.getDlRightAction());
//                message.setFilePath(packet.getServiceType());
//                message.setDeepLinkInfo(false);  // convert sang json luu vao db
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case gift:
//                message.setMessageType(ReengMessageConstant.MessageType.gift);
//                message.setContent(packet.getBody());
//                message.setGifThumbId(packet.getVideoThumb());
//                message.setGifImgId(packet.getGifImg());
//                message.setStatus(ReengMessageConstant.STATUS_NOT_LOAD);
//                break;
//            case fake_mo:
//                message.setMessageType(ReengMessageConstant.MessageType.fake_mo);
//                message.setContent(packet.getBody());
//                message.setFileId(packet.getFileId());// luu thong tin vao cac truong co san
//                message.setVideoContentUri(packet.getConfirm());
//                message.setImageUrl(packet.getLabel());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case notification_fake_mo:
//                message.setMessageType(ReengMessageConstant.MessageType.notification_fake_mo);
//                message.setContent(packet.getBody());
//                message.setFileId(packet.getFileId());// luu thong tin vao cac truong co san
//                message.setVideoContentUri(packet.getConfirm());
//                message.setImageUrl(packet.getLabel());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case image_link:
//                message.setMessageType(ReengMessageConstant.MessageType.image_link);
//                message.setContent(packet.getBody());
//                // message.setFileId(packet.getFileId());// luu thong tin vao cac truong co san
//                message.setImageUrl(packet.getImageLinkUrl());// image url
//                message.setDirectLinkMedia(packet.getImageLinkTo());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case advertise:
//                message.setMessageType(ReengMessageConstant.MessageType.advertise);
//                if (packet.getListAdvertise().get(0) != null) {
//                    String content = packet.getListAdvertise().get(0).getTitle();
//                    message.setContent(content);
//                }
//                message.setListAdvertiseFromPacket(packet.getListAdvertise());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case vote:
//                String voteDetail = packet.getPollDetail();
//                PollRequestHelper pollHelper = PollRequestHelper.getInstance(mApplication);
//                PollObject poll = pollHelper.parserPollObjectFromMessage(voteDetail);
//                if (poll == null) {
//                    message.setMessageType(ReengMessageConstant.MessageType.notification);
//                    message.setContent(mRes.getString(R.string.e601_error_but_undefined));
//                } else {
//                    if ("create_poll".equals(packet.getPollType())) {
//                        message.setMessageType(ReengMessageConstant.MessageType.poll_create);
//                        message.setContent(pollHelper.getContentMessageCreatePoll(poll));
//                    } else {
//                        message.setMessageType(ReengMessageConstant.MessageType.poll_action);
//                        message.setContent(pollHelper.getContentMessageActionPoll(poll, message.getSender(), false));
//                    }
//                    message.setFileId(poll.getPollId());// luu thong tin pollId vao fileId
//                    //message.setDirectLinkMedia(voteDetail);//TODO nêu cần thiết thì lưu detail vào đây
//                }
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case luckywheel_help:
//                message.setMessageType(ReengMessageConstant.MessageType.luckywheel_help);
//                message.setContent(packet.getBody());
//                message.setMusicState(ReengMessageConstant.MUSIC_STATE_NONE);// dùng biến này để check đã click đồng
//                // ý cầu cứu hay chưa
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case watch_video:
//                message.setMessageType(ReengMessageConstant.MessageType.watch_video);
//                message.setContent(packet.getBody());
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case bank_plus:
//                message.setMessageType(ReengMessageConstant.MessageType.bank_plus);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                //luu thong tin vao truong co san
//                message.setFileId(packet.getBPlusId());
//                message.setImageUrl(packet.getBPlusAmount());
//                message.setVideoContentUri(packet.getBPlusDesc());
//                message.setFilePath(packet.getBPlusType());
//                message.setMusicState(ReengMessageConstant.MUSIC_STATE_NONE);
//                String content;
//                if (ReengMessageConstant.BPLUS_PAY.equals(packet.getBPlusType())) {
//                    content = mRes.getString(R.string.bplus_holder_pay_received);
//                } else {
//                    content = mRes.getString(R.string.bplus_holder_claim_received);
//                }
//                message.setContent(content);
//                break;
//            case lixi_notification:
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                String senderName = mApplication.getMessageBusiness().getFriendNameOfLixi(packet.getLixiSender(), true);
//                String receiverName = mApplication.getMessageBusiness().getFriendNameOfLixi(packet.getLixiReceiver(),
//                        false);
//                String body = packet.getBody();
//                body = body.replaceAll("%1\\$s", receiverName).replaceAll("%2\\$s", senderName);
//                message.setContent(body);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                break;
//            case lixi:
//                message.setMessageType(ReengMessageConstant.MessageType.lixi);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setImageUrl(packet.getAmountLixi());
//                message.setContent(packet.getBody());
////                message.setFileId(packet.getIdStickerLixi());
//                message.setFilePath(packet.getRequestIdLixi());
//                message.setVideoContentUri(packet.getListMemberLixiStr());
//                break;
//
//            case pin_msg:
//                message.setMessageType(ReengMessageConstant.MessageType.pin_message);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setContent(packet.getBody());
//                message.setSize(packet.getPinMsgAction());
//                message.setSongId(packet.getPinType());
//                message.setImageUrl(packet.getPinMsgImg());
//                message.setFileName(packet.getPinMsgTitle());
//                message.setFilePath(packet.getPinMsgTarget());
//                break;
//            case suggest_voicesticker:
//                message.setMessageType(ReengMessageConstant.MessageType.suggest_voice_sticker);
//                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                message.setContent(packet.getBody());
//                message.setFilePath(packet.getStickerData());
//                message.setImageUrl(packet.getImgCover());
//                message.setFileName(packet.getImgAvatar());
//                break;
//            default:
//                break;
//        }
//        Log.i(TAG, "mapPacketToReengMessage " + message);
//        return message;
//    }
//
//    /**
//     * xu ly ban tin message group
//     *
//     * @param application
//     * @param receivedMessage
//     */
//    public void processIncomingGroupMessage(ApplicationController application, ReengMessagePacket receivedMessage) {
//        Log.i(TAG, "processIncomingGroupMessage");
//        mApplication.logDebugContent("processIncomingGroupMessage: " + receivedMessage.toXML());
//        String chatRoom = receivedMessage.getFrom().split("@")[0].trim();
//        //tim thread chat tuong ung hoac tao moi neu chua co
//        ThreadMessage mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewGroupThread(chatRoom);
//        boolean isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        SettingBusiness mSetting = SettingBusiness.getInstance(mApplication);
//        boolean isSendSeen = mSetting.getPrefEnableSeen();
//        //gui ban tin deliver truoc khi check duplicate
//        mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                .TYPE_THREAD_GROUP_CHAT, isOnChatScreen, isSendSeen);
//        // check duplicate
//        if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            mApplication.logDebugContent("processIncomingGroupMessage DuplicatePacket: " + receivedMessage
//                    .getPacketID());
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        // mapping packet
//        ReengMessagePacket.SubType subType = receivedMessage.getSubType();
//        if (subType == ReengMessagePacket.SubType.toast) {
//            if (!TextUtils.isEmpty(receivedMessage.getBody())) {
//                mMessageBusiness.onNonReengResponse(mCorrespondingThread.getId(), null, false, receivedMessage.getBody
//                        ());
//            }
//        } else {
//            ReengMessage reengMessage = mapPacketToReengMessage(receivedMessage,
//                    mCorrespondingThread.getId(), null, myNumber, subType);
//            if (isOnChatScreen) {
//                if (isSendSeen) {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//                } else {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//                }
//            }
//            mMessageBusiness.notifyReengMessage(application,
//                    mCorrespondingThread, reengMessage, ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT);
//        }
//    }
//
//    /**
//     * xu ly ban tin messgae 1-1
//     *
//     * @param application
//     * @param receivedMessage
//     */
//    public void processIncomingReengMessage(ApplicationController application, ReengMessagePacket receivedMessage) {
//        //neu dang trong man hinh chat thi gui luon ban tin seen
//        Log.i(TAG, "processIncomingReengMessage");
//        mApplication.logDebugContent("processIncomingReengMessage: " + receivedMessage.toXML());
//        String fromNumber = receivedMessage.getFrom().split("@")[0]; // lay so dien thoai nguoi gui
//        if (TextUtils.isEmpty(fromNumber)) {
//            return;
//        }
//        String newNumber = PrefixChangeNumberHelper.getInstant(application).convertNewPrefix(fromNumber);
//        if (newNumber != null) fromNumber = newNumber;
//        //xu ly neu ban tin la lam quen
//        ReengMessagePacket.SubType subType = receivedMessage.getSubType();
//        // ban tin invite friend hoac invite sucess thi insert vao bang stranger
//        ThreadMessage mCorrespondingThread;
//        StrangerBusiness strangerBusiness = mApplication.getStrangerBusiness();
//        boolean isOnChatScreen;
//        if (subType == ReengMessagePacket.SubType.invite_friend) {
//            // kiem tra va tao thread lam quen
//            mCorrespondingThread = strangerBusiness.processInviteFriend(fromNumber, receivedMessage);
//        } else if (subType == ReengMessagePacket.SubType.invite_success) {
//            // tao thread lam quen
//            mCorrespondingThread = strangerBusiness.processInviteSuccess(fromNumber, receivedMessage);
//        } else if (!TextUtils.isEmpty(receivedMessage.getExternal())) {// external giong appId
//            mCorrespondingThread = mApplication.getStrangerBusiness().
//                    createStrangerAndThreadAfterReceivedMessage(fromNumber,
//                            receivedMessage.getNick(), receivedMessage.getFromAvatar(), receivedMessage.getExternal());
//        } else {
//            // tao thread thuong
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromNumber, false);
//        }
//        // xem dang o man hinh nao
//        isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        SettingBusiness mSetting = SettingBusiness.getInstance(mApplication);
//        boolean isSendSeen = mSetting.getPrefEnableSeen();
//        boolean isBlockNumber = mBlockContactBusiness.isBlockNumber(fromNumber);
//        if (isBlockNumber) {    //Neu la block thi ko gui seen
//            mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                    .TYPE_THREAD_PERSON_CHAT, false, false);
//            mApplication.logDebugContent("processIncomingReengMessage block: " + fromNumber);
//            return;
//        } else {
//            mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                    .TYPE_THREAD_PERSON_CHAT, isOnChatScreen, isSendSeen);
//        }
//        //check duplicate
//        if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            mApplication.logDebugContent("processIncomingReengMessage DuplicatePacket: " + receivedMessage
//                    .getPacketID());
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        // message lam quen thanh cong thi ko tao thread, ko notify
//        /*if (mCorrespondingThread == null) { //TODO loi nay
//            return;
//        }*/
//        if (subType == ReengMessagePacket.SubType.toast) {
//            if (!TextUtils.isEmpty(receivedMessage.getBody())) {
//                mMessageBusiness.onNonReengResponse(mCorrespondingThread.getId(), null, false, receivedMessage
//                        .getBody());
//            }
//        } else {
//            ReengMessage reengMessage = mapPacketToReengMessage(receivedMessage,
//                    mCorrespondingThread.getId(), fromNumber, myNumber, subType);
//            /*dang trong man hinh chat, hoac setting khong cho gui seen
//            thi set state seen luon*/
//            if (isOnChatScreen) {
//                if (isSendSeen) {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//                } else {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//                }
//            }
//            mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                    reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//            /*if (mCorrespondingThread.isStranger() &&
//                    (subType == ReengMessagePacket.SubType.text || subType == ReengMessagePacket.SubType.image)) {
//                mApplication.getQueueCheckSpamStranger().addTask(reengMessage);
//            }*/
//        }
//    }
//
//    public void processIncomingRoomMessage(ApplicationController application, ReengMessagePacket receivedMessage) {
//        String roomId = receivedMessage.getFrom().split("@")[0]; // lay roomId
//        ThreadMessage threadMessage = mMessageBusiness.findRoomThreadByRoomId(roomId);
//        String fromNumber = receivedMessage.getSender();
//        boolean isOnChatScreen = (threadMessage != null && threadMessage.getId() == ReengNotificationManager
//                .getCurrentUIThreadId());
//        boolean isSendSeen = SettingBusiness.getInstance(mApplication).getPrefEnableSeen();
//        application.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT,
//                isOnChatScreen, isSendSeen);
//        if (threadMessage == null) return;// thread ko ton tai thi ko xu ly
//        if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        // mapping packet
//        ReengMessagePacket.SubType subType = receivedMessage.getSubType();
//        if (subType == ReengMessagePacket.SubType.toast) {
//            if (!TextUtils.isEmpty(receivedMessage.getBody())) {
//                mMessageBusiness.onNonReengResponse(threadMessage.getId(), null, false, receivedMessage.getBody());
//            }
//        } else {
//            ReengMessage reengMessage = mapPacketToReengMessage(receivedMessage,
//                    threadMessage.getId(), fromNumber, myNumber, subType);
//            if (isOnChatScreen) {
//                if (isSendSeen) {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//                } else {
//                    reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//                }
//            }
//            mMessageBusiness.notifyReengMessage(application, threadMessage, reengMessage, ThreadMessageConstant
//                    .TYPE_THREAD_ROOM_CHAT);
//        }
//    }
//
//    /**
//     * xu ly ban tin khong co subtype
//     *
//     * @param application
//     * @param receivedMessage
//     */
//    public void processIncomingNoSubtypeReengMessage(ApplicationController application,
//                                                     ReengMessagePacket receivedMessage, int threadType) {
//        XMPPManager xmppManager = application.getXmppManager();
//        if (!receivedMessage.isNoStore()) {// gap the no_store khong gui lai deliver
//            if (threadType == ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT) {
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay offical id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, ThreadMessageConstant
//                        .TYPE_THREAD_OFFICER_CHAT);
//            } else if (threadType == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay room id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, ThreadMessageConstant
//                        .TYPE_THREAD_ROOM_CHAT);
//            } else if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//                xmppManager.sendDeliverMessage(receivedMessage, threadType, false, false);
//            } else {    //group
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay room id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, ThreadMessageConstant
//                        .TYPE_THREAD_GROUP_CHAT);
//            }
//        }
//    }
//
//    /**
//     * xu ly ban tin type chua dinh nghia
//     *
//     * @param application
//     * @param receivedPacket
//     */
//    public void processIncomingNotSupportTypeMessage(ApplicationController application,
//                                                     ReengMessagePacket receivedPacket,
//                                                     String typeString,
//                                                     Packet originalPacket,
//                                                     ReengMessagePacket.Type type) {
//        XMPPManager xmppManager = application.getXmppManager();
//        String from = receivedPacket.getFrom(); //lay truong from full domain
//        String fromJid = from.split("@")[0]; // lay truong from
//        if (!receivedPacket.isNoStore()) {// co the no_store thi ko gui lai deliver
//            ReengEventPacket packetDeliver = new ReengEventPacket();
//            packetDeliver.setSubType(ReengMessagePacket.SubType.event);
//            packetDeliver.setEventType(ReengEventPacket.EventType.delivered);
//            packetDeliver.addToListIdOfEvent(receivedPacket.getPacketID());
//            //packetDeliver.setTypeString(typeString); TODO bản tin type không hiểu thì sẽ gửi deliver type chat
//            packetDeliver.setType(ReengMessagePacket.Type.chat);
//            // ban tin A đến thì gui lai A luon (full domain)
//            String myNumber = application.getReengAccountBusiness().getJidNumber();
//            if (myNumber != null && !myNumber.equals(fromJid)) {// ko gui deliver cho chinh minh
//                packetDeliver.setTo(from);
//                // gui ban tin deliver
//                xmppManager.sendXmppPacket(packetDeliver);
//            }
//        }
//        /*// gui ban tin iq thong bao khong ho tro type
//        IQInfo unknownPacket = new IQInfo(IQInfo.NAME_SPACE_UNKNOWN_MSG);
//        unknownPacket.setType(IQ.Type.GET);
//        unknownPacket.addElements("type", "0");
//        unknownPacket.addElements("subtype", "1");
//        unknownPacket.addElements("package", originalPacket.toXML());
//        xmppManager.sendXmppPacket(unknownPacket);*/
//
//    }
//
//    /**
//     * @param application
//     * @param receivedMessage
//     */
//    public void processIncomingNotSupportReengMessage(ApplicationController application,
//                                                      ReengMessagePacket receivedMessage,
//                                                      int threadType, Packet originalPacket) {
//        application.logDebugContent("processIncomingNotSupportReengMessage: " + receivedMessage.toXML());
//        XMPPManager xmppManager = application.getXmppManager();
//        //gui ban tin deliver truoc khi check duplicate
//        if (!receivedMessage.isNoStore()) {// khong co the no_store thi moi xu ly deliver
//            if (threadType == ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT) {
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay offical id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, threadType);
//            } else if (threadType == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay room id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, threadType);
//            } else if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//                String officerId = receivedMessage.getFrom().split("@")[0];     //lay room id
//                String packetId = receivedMessage.getPacketID();
//                xmppManager.sendConfirmDeliverMessage(packetId, officerId, null, threadType);
//            }
//        }
//
//        //TODO ko xu ly ban tin normal
//        //check duplicate
//        /*if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            application.logDebugContent("processIncomingNotSupportReengMessage DuplicatePacket: " + receivedMessage
//                    .getPacketID());
//            return;
//        }
//        String sender = receivedMessage.getFrom().split("@")[0]; // lay sender
//        if (sender == null) {
//            application.logDebugContent("processIncomingNotSupportReengMessage sender == null: ");
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ThreadMessage mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(sender, false);
//        boolean isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        SettingBusiness mSetting = SettingBusiness.getInstance(mApplication);
//        boolean isSendSeen = mSetting.getPrefEnableSeen();
//        // thread 1-1, block, bo qua
//        if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT &&
//                mBlockContactBusiness.isBlockNumber(sender)) {
//            application.logDebugContent("processIncomingNotSupportReengMessage isBlockNumber: " + sender);
//            return;
//        } else {
//
//            mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                    .TYPE_THREAD_PERSON_CHAT, isOnChatScreen, isSendSeen);
//        }*/
//        // gui ban tin iq thong bao khong ho tro type
//        /*IQInfo unknownPacket = new IQInfo(IQInfo.NAME_SPACE_UNKNOWN_MSG);
//        unknownPacket.setType(IQ.Type.GET);
//        unknownPacket.addElements("type", "1");
//        unknownPacket.addElements("subtype", "0");
//        unknownPacket.addElements("package", originalPacket.toXML());
//        xmppManager.sendXmppPacket(unknownPacket);*/
//
//
//        String fromNumber = receivedMessage.getFrom().split("@")[0]; // lay truong from
//        // ban tin invite friend hoac invite sucess thi insert vao bang stranger
//        ThreadMessage mCorrespondingThread;
//        boolean isOnChatScreen;
//        if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewGroupThread(fromNumber);
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT) {
//            String officerName = receivedMessage.getOfficalName();                 //lay ten offical account
//            myNumber = application.getReengAccountBusiness().getJidNumber();
//            String avatarUrl = receivedMessage.getAvatarUrl();
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateOfficerThread(
//                    fromNumber, officerName, avatarUrl, OfficerAccountConstant.ONMEDIA_TYPE_NONE);
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            mCorrespondingThread = mMessageBusiness.findRoomThreadByRoomId(fromNumber);
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            if (TextUtils.isEmpty(receivedMessage.getExternal())) {
//                mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromNumber, false);
//            } else {// external giong appId
//                mCorrespondingThread = mApplication.getStrangerBusiness().
//                        createStrangerAndThreadAfterReceivedMessage(fromNumber,
//                                receivedMessage.getNick(), receivedMessage.getExternal());
//            }
//        } else {
//            return;
//        }
//        if (mCorrespondingThread == null) return;
//
//        // xem dang o man hinh nao
//        isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        SettingBusiness mSetting = SettingBusiness.getInstance(mApplication);
//        boolean isSendSeen = mSetting.getPrefEnableSeen();
//        if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            boolean isBlockNumber = mBlockContactBusiness.isBlockNumber(fromNumber);
//            if (isBlockNumber) {    //Neu la block thi ko gui seen
//                mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                        .TYPE_THREAD_PERSON_CHAT, false, false);
//                mApplication.logDebugContent("processIncomingNotSupportTypeMessage block: " + fromNumber);
//                return;
//            } else {
//                mApplication.getXmppManager().sendDeliverMessage(receivedMessage, ThreadMessageConstant
//                        .TYPE_THREAD_PERSON_CHAT, isOnChatScreen, isSendSeen);
//            }
//        }
//        //check duplicate
//        if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            mApplication.logDebugContent("processIncomingNotSupportTypeMessage DuplicatePacket: " + receivedMessage
//                    .getPacketID());
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//
//        ReengMessage message = createMessageUpdateApp(fromNumber, mCorrespondingThread.getId(), threadType,
//                originalPacket.toXML(), receivedMessage);
////            dang trong man hinh chat, hoac setting khong cho gui seen
////            thi set state seen luon
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                message.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                message.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                message, mCorrespondingThread.getThreadType());
//    }
//
//    private ReengMessage createMessageUpdateApp(String sender, int threadId, int threadType, String rawMessage,
//                                                ReengMessagePacket receivedMessage) {
//        ReengMessage message = new ReengMessage();
//        message.setPacketId(receivedMessage.getPacketID()); // set packet id
//        message.setReceiver(myNumber);
//        if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT || threadType == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            message.setSender(receivedMessage.getSender());
//        } else
//            message.setSender(sender);
//        message.setReadState(ReengMessageConstant.READ_STATE_UNREAD);
//        message.setThreadId(threadId);
//        message.setDirection(ReengMessageConstant.Direction.received);
//        message.setMessageType(ReengMessageConstant.MessageType.update_app);
//        // set time
//        message.setTime(receivedMessage.getTimeSend());
//        message.setExpired(receivedMessage.getExpired());
//        message.setFilePath(rawMessage);
//        return message;
//    }
//
//    public void processIncomingShareMusicMessage(ApplicationController application, ShareMusicMessagePacket
//            receivedMessage) {
//        final ShareMusicMessagePacket.SubType subType = receivedMessage.getSubType();
//        String fromNumber = receivedMessage.getSender(); //lay so dien thoai nguoi gui
//        if (TextUtils.isEmpty(fromNumber)) {
//            return;
//        }
//        if (mBlockContactBusiness.isBlockNumber(fromNumber)) {
//            Log.d(TAG, fromNumber + " is blocked");
//            return;
//        }
//        // check lap packet id
//        if (mMessageBusiness.checkDuplicatePacket(receivedMessage.getPacketID())) {
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ThreadMessage mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromNumber, false);
//        if (mCorrespondingThread == null) {
//            return;
//        }
//        // them message notify thong bao ko con ho tro ban cu
//        if (subType == ShareMusicMessagePacket.SubType.nc_invite ||
//                subType == ShareMusicMessagePacket.SubType.nc_create) {
//            // new reeng message text, body no support
//            ReengMessage message = new ReengMessage();
//            message.setPacketId(receivedMessage.getPacketID()); // set packet id
//            message.setReceiver(myNumber);
//            message.setReadState(ReengMessageConstant.READ_STATE_UNREAD);
//            message.setThreadId(mCorrespondingThread.getId());
//            message.setDirection(ReengMessageConstant.Direction.received);
//            // set time
//            Date date = new Date();
//            message.setTime(date.getTime());
//            message.setSender(fromNumber);
//            message.setMessageType(ReengMessageConstant.MessageType.text);
//            message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//            mContactBusiness = mApplication.getContactBusiness();
//            Resources res = mApplication.getResources();
//            String featuresOldMusic = String.format(res.getString(R.string.features_old_music),
//                    mMessageBusiness.getFriendName(fromNumber));
//            message.setContent(featuresOldMusic);
//            mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                    message, mCorrespondingThread.getThreadType());
//            //gui lai ban tin cho B bao cap nhat phien ban moi
//            sendMessageSuggestFriendOldFeaturesMusic(fromNumber);
//        }
//    }
//
//    private void sendMessageSuggestFriendOldFeaturesMusic(String to) {
//        String content = mRes.getString(R.string.msg_receiver_features_old_music);
//        ReengMessagePacket message = new ReengMessagePacket();
//        //message.setTo(to + "@" + Constants.XMPP.XMPP_DOMAIN);
//        message.setTo(to + Constants.XMPP.XMPP_RESOUCE);
//        message.setType(ReengMessagePacket.Type.chat);
//        message.setSubType(ReengMessagePacket.SubType.text);
//        message.setPacketID(PacketMessageId.getInstance().
//                genPacketId(message.getType().toString(), message.getSubType().toString()));
//        message.setBody(content);
//        // send xmpp message co xu ly khi exception
//        mApplication.getXmppManager().sendXmppPacket(message);
//    }
//
//    protected boolean processIncomingSMS(String smsSender, String smsContent) {
//        Log.i(TAG, "processIncomingSMS smsSender = " + smsSender);
//        ReengAccountBusiness accountBusiness = mApplication.getReengAccountBusiness();
//        if (!accountBusiness.isValidAccount() || !NetworkHelper.isConnectInternet(mApplication)) {
//            return false;
//        }
//        if (mBlockContactBusiness.isBlockNumber(smsSender)) {
//            return false;
//        }
//        myNumber = accountBusiness.getJidNumber();
//        if (smsSender.equals(myNumber)) {
//            return false;
//        }
//        if (!PhoneNumberHelper.getInstant().isViettelNumber(smsSender)) {
//            return false;
//        }
//        if (!accountBusiness.isViettel()) {
//            return false;
//        }
//        ThreadMessage threadMessage = mMessageBusiness.findExistingOrCreateNewThread(smsSender);
//        if (threadMessage == null) {
//            return false;
//        } else {
//            // ReengMessage lastMessage = threadMessage.getAllMessages().get(threadMessage.getAllMessages().size() - 1);
//            //if (TimeHelper.checkTimeInDay(lastMessage.getTime())) {
//            ReengMessage reengMessage = mapSmsToReengMessage(threadMessage.getId(), myNumber, smsContent, smsSender);
//            mMessageBusiness.notifyReengMessage(mApplication,
//                    threadMessage, reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//            //return false;
//            //}
//        }
//        return false;
//    }
//
//    /**
//     * xu ly ban tin messgae 1-1
//     *
//     * @param application
//     * @param received
//     */
//    public void processIncomingReengMusic(ApplicationController application,
//                                          ReengMusicPacket received, ReengMessagePacket.Type type) {
//        //neu dang trong man hinh chat thi gui luon ban tin seen
//        Log.i(TAG, "processIncomingReengMusic :>> ");
//        String fromNumber = received.getFrom().split("@")[0]; // lay so dien thoai nguoi gui
//        if (fromNumber.length() == 11) {
//            String newNumberStranger = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(fromNumber);
//            if (newNumberStranger != null) {
//                fromNumber = newNumberStranger;
//            }
//        }
//        // type khac chat thi ko lam gi
//        if (type != ReengMessagePacket.Type.chat) {
//            int mThreadType;
//            if (type == ReengMessagePacket.Type.groupchat) {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT;
//            } else if (type == ReengMessagePacket.Type.roomchat) {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT;
//            } else {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
//            }
//            application.getXmppManager().sendDeliverMessage(received, mThreadType, false, false);
//            return;
//        }
//        if (TextUtils.isEmpty(fromNumber)) {
//            return;
//        }
//        //xu ly neu ban tin la lam quen
//        ReengMessagePacket.SubType subType = received.getSubType();
//        // ban tin invite friend hoac invite sucess thi insert vao bang stranger
//        ThreadMessage mCorrespondingThread;
//        StrangerBusiness strangerBusiness = mApplication.getStrangerBusiness();
//        boolean isOnChatScreen;
//        if (subType == ReengMessagePacket.SubType.music_stranger_accept ||
//                subType == ReengMessagePacket.SubType.music_stranger_reinvite) {
//            // kiem tra va tao thread nguoi la
//            mCorrespondingThread = strangerBusiness.processInComingStrangerMusic(fromNumber, received);
//        } else if (!TextUtils.isEmpty(received.getExternal())) {// external giong appId
//            mCorrespondingThread = mApplication.getStrangerBusiness().
//                    createStrangerAndThreadAfterReceivedMessage(fromNumber,
//                            received.getNick(), received.getExternal());
//        } else {// tao thread thuong
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromNumber, false);
//        }
//        // kiem tra block, gui seen...
//        isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        boolean isSendSeen = SettingBusiness.getInstance(mApplication).getPrefEnableSeen();
//        boolean isBlockNumber = mBlockContactBusiness.isBlockNumber(fromNumber);
//        // invite, leave co gui deliver
//        if (subType == ReengMessagePacket.SubType.watch_video) {
//            application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT,
//                    isOnChatScreen, isSendSeen);
//        } else if (subType == ReengMessagePacket.SubType.music_invite ||
//                subType == ReengMessagePacket.SubType.music_leave ||
//                subType == ReengMessagePacket.SubType.music_stranger_accept ||
//                subType == ReengMessagePacket.SubType.music_stranger_reinvite) {
//            if (isBlockNumber) {    //Neu la block thi ko gui seen
//                application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant
//                        .TYPE_THREAD_PERSON_CHAT, false, false);
//                Log.d(TAG, "processIncomingReengMusic: block number");
//                return;
//            } else {
//                application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant
//                        .TYPE_THREAD_PERSON_CHAT, isOnChatScreen, isSendSeen);
//            }
//        } else if (isBlockNumber) {// neu khong phai invite hoacj leave ma so bi block thi ko lam gi
//            Log.d(TAG, "processIncomingReengMusic: block number");
//            return;
//        }
//        // check lap packet id . gui thong ke ga
//        if (mMessageBusiness.checkDuplicatePacket(received.getPacketID())) {
//            return;
//        }
//        switch (subType) {
//            case music_invite:
//                processInviteMusic(application, received, fromNumber, subType,
//                        mCorrespondingThread, isOnChatScreen, isSendSeen);
//                break;
//            case music_leave:
//                processLeaveMusic(application, received, subType, mCorrespondingThread, fromNumber);
//                break;
//            case music_ping:
//                processPingMusic(application, received, mCorrespondingThread, fromNumber);
//                break;
//            case music_pong:
//                processPongMusic(application, received, mCorrespondingThread, fromNumber);
//                break;
//            case music_action:
//                processActionMusic(application, received, mCorrespondingThread, fromNumber);
//                break;
//            case music_action_response:
//                processActionResponseMusic(application, received, mCorrespondingThread, fromNumber);
//                break;
//            case music_stranger_accept:
//                processStrangerAcceptMusic(application, received,
//                        fromNumber, subType, mCorrespondingThread, isOnChatScreen, isSendSeen);
//                break;
//            case music_stranger_reinvite:
//                processStrangerReInviteMusic(application, received,
//                        fromNumber, subType, mCorrespondingThread, isOnChatScreen, isSendSeen);
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void processIncomingWatchVideo(ApplicationController application, ReengMusicPacket received,
//                                          ReengMessagePacket.Type type) {
//        Log.i(TAG, "processIncomingWatchVideo :>> ");
//        String fromJid = received.getFrom().split("@")[0];
//        if (TextUtils.isEmpty(fromJid)) {
//            return;
//        }
//        ThreadMessage mCorrespondingThread;
//        int mThreadType;
//        boolean isBlockNumber = false;
//        if (type == ReengMessagePacket.Type.groupchat) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT;
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewGroupThread(fromJid);
//        } else if (received.getType() == ReengMessagePacket.Type.offical) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
//            String officerName = received.getOfficalName();                 //lay ten offical account
//            myNumber = application.getReengAccountBusiness().getJidNumber();
//            String avatarUrl = received.getAvatarUrl();
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateOfficerThread(
//                    fromJid, officerName, avatarUrl, OfficerAccountConstant.ONMEDIA_TYPE_NONE);
//        } else if (type == ReengMessagePacket.Type.roomchat) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT;
//            mCorrespondingThread = mMessageBusiness.findRoomThreadByRoomId(fromJid);
//        } else {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT;
//            if (TextUtils.isEmpty(received.getExternal())) {
//                mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromJid, false);
//            } else {// external giong appId
//                mCorrespondingThread = mApplication.getStrangerBusiness().
//                        createStrangerAndThreadAfterReceivedMessage(fromJid, received.getNick(), received.getExternal
//                                ());
//            }
//            isBlockNumber = mBlockContactBusiness.isBlockNumber(fromJid);
//        }
//        if (mCorrespondingThread == null || isBlockNumber) {
//            application.getXmppManager().sendDeliverMessage(received, mThreadType, false, false);
//        } else {
//            boolean isOnChatScreen;
//            isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//            boolean isSendSeen = SettingBusiness.getInstance(mApplication).getPrefEnableSeen();
//            application.getXmppManager().sendDeliverMessage(received, mThreadType, isOnChatScreen, isSendSeen);
//            // check lap packet id . gui thong ke ga
//            if (!mMessageBusiness.checkDuplicatePacket(received.getPacketID())) {
//                processWatchVideo(application, received, fromJid, ReengMessagePacket.SubType.watch_video,
//                        mCorrespondingThread, isOnChatScreen, isSendSeen);
//            }
//        }
//    }
//
//    private void processWatchVideo(ApplicationController application, ReengMusicPacket received,
//                                   String fromJid, ReengMessagePacket.SubType subType,
//                                   ThreadMessage mCorrespondingThread, boolean isOnChatScreen, boolean isSendSeen) {
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                mCorrespondingThread, fromJid, myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        if (TextUtils.isEmpty(reengMessage.getContent())) {
//            String content;
//            String contentBold;
//            if (mCorrespondingThread.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//                String senderName = mMessageBusiness.getFriendNameOfGroup(reengMessage.getSender());
//                String threadName = mMessageBusiness.getThreadName(mCorrespondingThread);
//                content = String.format(mRes.getString(R.string.invite_group_watch_video_receiver), senderName,
//                        threadName);
//                contentBold = String.format(mRes.getString(R.string.invite_group_watch_video_receiver),
//                        TextHelper.textBoldWithHTML(senderName), threadName);
//            } else {
//                content = String.format(mRes.getString(R.string.invite_watch_video_receiver),
//                        mCorrespondingThread.getThreadName());
//                contentBold = String.format(mRes.getString(R.string.invite_watch_video_receiver),
//                        TextHelper.textBoldWithHTML(mCorrespondingThread.getThreadName()));
//            }
//            reengMessage.setContent(content);
//            reengMessage.setFileName(contentBold);
//        }
//        MediaModel mediaModel = new Gson().fromJson(reengMessage.getDirectLinkMedia(), MediaModel.class);
//        String info;
//        if (TextUtils.isEmpty(mediaModel.getSinger())) {
//            info = mediaModel.getName();
//        } else {
//            info = mediaModel.getName() + " - " + mediaModel.getSinger();
//        }
//        reengMessage.setFilePath(info);
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                reengMessage, mCorrespondingThread.getThreadType());
//    }
//
//    //processIncomingGroupMusic
//    public void processIncomingGroupMusic(ApplicationController application, ReengMusicPacket received) {
//        Log.i(TAG, "processIncomingGroupMusic :>> ");
//        String serverId = received.getFrom().split("@")[0];
//        if (TextUtils.isEmpty(serverId)) {
//            return;
//        }
//        // find thread, send deliver
//        ThreadMessage groupThread = mMessageBusiness.findExistingOrCreateNewGroupThread(serverId);
//        boolean isOnChatScreen = (groupThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        boolean isSendSeen = SettingBusiness.getInstance(mApplication).getPrefEnableSeen();
//        application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT,
//                isOnChatScreen, isSendSeen);
//        if (mMessageBusiness.checkDuplicatePacket(received.getPacketID())) {
//            return;
//        }
//        ReengMessagePacket.SubType subType = received.getSubType();
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//
//        MusicBusiness musicBusiness = mApplication.getMusicBusiness();
//        if (subType == ReengMessagePacket.SubType.music_action &&
//                received.getMusicAction() == ReengMusicPacket.MusicAction.change &&
//                !musicBusiness.isShowPlayerGroup(serverId)) {
//            subType = ReengMessagePacket.SubType.music_invite;// chuyen subtype maping message cho de
//        }
//        // mapping packet
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                groupThread, received.getSender(), myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        boolean actionNotSupport = false;
//        switch (subType) {
//            case music_invite:
//                reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_GROUP);
//                break;
//            case music_action:
//                if (received.getMusicAction() == ReengMusicPacket.MusicAction.change) {
//                    reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_ACCEPTED);
//                    musicBusiness.onReceivedChangeSong(received, serverId, reengMessage.getSongModel(musicBusiness));
//                } else {
//                    actionNotSupport = true;
//                }
//                break;
//            case music_request_change:
//                Log.d(TAG, "music_request_change:--groupThread.isAdmin(): " + groupThread.isAdmin() + " " +
//                        "--musicBusiness.isShowPlayerGroup(serverId) " + musicBusiness.isShowPlayerGroup(serverId));
//                if (groupThread.isAdmin() && musicBusiness.isShowPlayerGroup(serverId)) {
//                    reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_REQUEST_CHANGE);
//                } else {
//                    actionNotSupport = true;
//                }
//                break;
//            default:
//                break;
//        }
//        if (actionNotSupport) return;
//        mMessageBusiness.notifyReengMessage(application,
//                groupThread, reengMessage, ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT);
//    }
//
//    //processIncomingRoomMusic
//    public void processIncomingRoomMusic(ApplicationController application, ReengMusicPacket received) {
//        Log.i(TAG, "processIncomingRoomMusic :>> ");
//        String roomId = received.getFrom().split("@")[0]; // lay so dien thoai nguoi gui
//        if (TextUtils.isEmpty(roomId)) {
//            return;
//        }
//        ThreadMessage threadMessage = mMessageBusiness.findRoomThreadByRoomId(roomId);
//        boolean isOnChatScreen = (threadMessage != null && threadMessage.getId() == ReengNotificationManager
//                .getCurrentUIThreadId());
//        boolean isSendSeen = SettingBusiness.getInstance(mApplication).getPrefEnableSeen();
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT,
//                isOnChatScreen, isSendSeen);
//        if (threadMessage == null) return;
//        if (mMessageBusiness.checkDuplicatePacket(received.getPacketID())) {
//            //trung packet id
//            return;
//        }
//        ReengMessagePacket.SubType subType = received.getSubType();
//        if (subType != ReengMessagePacket.SubType.music_invite) {
//            return;// khong xu ly ban tin music #
//        }
//        // map message
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                threadMessage, received.getSender(), myNumber);
//        reengMessage.setDuration(0);
//        reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_ACCEPTED);
//        CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(reengMessage);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        mMessageBusiness.notifyReengMessage(application, threadMessage, reengMessage, ThreadMessageConstant
//                .TYPE_THREAD_ROOM_CHAT);
//    }
//
//    public void processIncomingCrbtGiftMessage(ApplicationController application,
//                                               ReengMusicPacket received, ReengMessagePacket.Type type) {
//        //neu dang trong man hinh chat thi gui luon ban tin seen
//        Log.i(TAG, "processIncomingReengMusic :>> ");
//        String fromNumber = received.getFrom().split("@")[0]; // lay so dien thoai nguoi gui
//        // type khac chat thi ko lam gi
//        if (type != ReengMessagePacket.Type.chat) {
//            int mThreadType;
//            if (type == ReengMessagePacket.Type.groupchat) {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT;
//            } else if (type == ReengMessagePacket.Type.roomchat) {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT;
//            } else {
//                mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
//            }
//            application.getXmppManager().sendDeliverMessage(received, mThreadType, false, false);
//            return;
//        }
//        if (TextUtils.isEmpty(fromNumber)) {
//            return;
//        }
//        //xu ly neu ban tin la lam quen
//        ReengMessagePacket.SubType subType = received.getSubType();
//        // ban tin invite friend hoac invite sucess thi insert vao bang stranger
//        ThreadMessage mCorrespondingThread;
//        boolean isOnChatScreen;
//        if (!TextUtils.isEmpty(received.getExternal())) {// external == appId
//            mCorrespondingThread = mApplication.getStrangerBusiness().
//                    createStrangerAndThreadAfterReceivedMessage(fromNumber,
//                            received.getNick(), received.getExternal());
//        } else {// tao thread thuong
//            mCorrespondingThread = mMessageBusiness.findExistingOrCreateNewThread(fromNumber, false);
//        }
//        // kiem tra block, gui seen...
//        isOnChatScreen = (mCorrespondingThread.getId() == ReengNotificationManager.getCurrentUIThreadId());
//        SettingBusiness mSetting = SettingBusiness.getInstance(mApplication);
//        boolean isSendSeen = mSetting.getPrefEnableSeen();
//        boolean isBlockNumber = mBlockContactBusiness.isBlockNumber(fromNumber);
//        // invite, leave co gui deliver
//        if (isBlockNumber) {    //Neu la block thi ko gui seen
//            application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT,
//                    false, false);
//            return;
//        } else {
//            application.getXmppManager().sendDeliverMessage(received, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT,
//                    isOnChatScreen, isSendSeen);
//        }
//        // check lap packet id . gui thong ke ga
//        if (mMessageBusiness.checkDuplicatePacket(received.getPacketID())) {
//            return;
//        }
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                mCorrespondingThread, fromNumber, myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_NONE);
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//    }
//
//    /**
//     * xu ly ban tin messgae 1-1
//     *
//     * @param application
//     * @param received
//     */
//    public void processIncomingAcceptRoomMusic(ApplicationController application,
//                                               ReengMusicPacket received, ReengMessagePacket.Type type) {
//        Log.i(TAG, "processIncomingAcceptRoomMusic :>> ");
//        String roomId = received.getFrom().split("@")[0].trim(); // lay roomId
//        checkAndSendDeliver(application, received, type);
//        // nhan dc ban tin type != roomchat
//        if (type != ReengMessagePacket.Type.roomchat) {
//            return;
//        }
//        ThreadMessage threadMessage = mMessageBusiness.findRoomThreadByRoomId(roomId);
//        if (threadMessage != null) {
//            boolean isOnChatScreen = (threadMessage.getId() == ReengNotificationManager.getCurrentUIThreadId());
//            myNumber = application.getReengAccountBusiness().getJidNumber();
//            if (mMessageBusiness.checkDuplicatePacket(received.getPacketID())) { // check duplicate
//                return;
//            }
//            int stateOnline = received.getRoomStateOnline();
//            threadMessage.setStateMusicRoom(received.getRoomStateMusic());
//            if (stateOnline == 1) {                     // online khong nghe playlist
//                threadMessage.setStateOnlineStar(1);
//                // mapping packet
//                ReengMessagePacket.SubType subType = received.getSubType();
//                String fromNumber = received.getSender();
//                ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                        threadMessage, fromNumber, myNumber);
//                MediaModel songModel = reengMessage.getSongModel(mApplication.getMusicBusiness());
//                if (songModel != null && !TextUtils.isEmpty(songModel.getId())) {
//                    // xu ly play ok thi moi notify message
//                    if (isOnChatScreen) {
//                        reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//                    }
//                    mApplication.getMusicBusiness().processPlayRoomMusic(threadMessage, reengMessage);
//                } else {// star online nhung chua play bai hat
//                    mApplication.getMusicBusiness().processJoinRoomChat(threadMessage);
//                }
//            } else {
//                threadMessage.setStateOnlineStar(0);
//                mApplication.getMusicBusiness().processJoinRoomChat(threadMessage);
//            }
//            mApplication.getMessageBusiness().notifyStateRoomChanged();
//        }
//    }
//
//    public void checkAndSendDeliver(ApplicationController application,
//                                    ReengMessagePacket received, ReengMessagePacket.Type type) {
//        int mThreadType = -1;
//        if (type == ReengMessagePacket.Type.chat) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT;
//        } else if (type == ReengMessagePacket.Type.offical) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT;
//        } else if (type == ReengMessagePacket.Type.roomchat) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT;
//        } else if (type == ReengMessagePacket.Type.groupchat) {
//            mThreadType = ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT;
//        }
//        application.getXmppManager().sendDeliverMessage(received, mThreadType, false, false);
//    }
//
//    public void processConfigRoomChat(ApplicationController application,
//                                      ReengEventPacket received, ReengMessagePacket.Type type) {
//        checkAndSendDeliver(application, received, type);
//        // nhan dc ban tin type == roomchat
//        if (type == ReengMessagePacket.Type.roomchat) {
//            String roomId = received.getFrom().split("@")[0].trim(); // lay roomId
//            ThreadMessage threadMessage = mMessageBusiness.findRoomThreadByRoomId(roomId);
//            if (received.getSubType() == ReengMessagePacket.SubType.event_expired) {
//                mApplication.getMusicBusiness().stopEventRoomMusic(roomId);
//                if (threadMessage == null) return;
//                String threadName = mMessageBusiness.getThreadName(threadMessage);
//                // mapping packet
//                myNumber = application.getReengAccountBusiness().getJidNumber();
//                ReengMessage message = new ReengMessage();
//                message.setPacketId(received.getPacketID()); // set packet id
//                message.setReceiver(myNumber);
//                message.setReadState(ReengMessageConstant.READ_STATE_READ);
//                message.setThreadId(threadMessage.getId());
//                message.setDirection(ReengMessageConstant.Direction.received);
//                message.setTime(received.getTimeSend());
//                message.setExpired(received.getExpired());
//                message.setSender(received.getSender());
//                message.setSticky(0);
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                message.setContent(String.format(mRes.getString(R.string.msg_expired_room),
//                        threadName, threadName));
//                mMessageBusiness.notifyReengMessage(application, threadMessage, message, ThreadMessageConstant
//                        .TYPE_THREAD_ROOM_CHAT);
//            } else if (received.getSubType() == ReengMessagePacket.SubType.star_unfollow) {
//                application.getMusicBusiness().processResponseLeaveRoom(roomId, true);
//            }
//        }
//    }
//
//    /**
//     * xu ly ban tin invite
//     *
//     * @param application
//     * @param received
//     * @param fromNumber
//     * @param subType
//     * @param mCorrespondingThread
//     * @param isOnChatScreen
//     * @param isSendSeen
//     */
//    private void processInviteMusic(ApplicationController application, ReengMusicPacket received, String fromNumber,
//                                    ReengMessagePacket.SubType subType, ThreadMessage mCorrespondingThread,
//                                    boolean isOnChatScreen, boolean isSendSeen) {
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                mCorrespondingThread, fromNumber, myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        int timeOut = TimeHelper.getTimeOutInviteMusic(received);
//        if (timeOut >= 1000) {// >1s
//            reengMessage.setDuration(timeOut);
//            CountDownInviteManager.getInstance(mApplication).startCountDownMessage(reengMessage);
//            reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_WAITING);
//        } else {
//            reengMessage.setDuration(0);
//            reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_TIME_OUT);
//            CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(reengMessage);
//        }
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//        mApplication.getMusicBusiness().updatePrefReceiveInviteMusic(received);
//    }
//
//    /**
//     * xu ly ban tin leave
//     *
//     * @param application
//     * @param received
//     * @param subType
//     * @param mCorrespondingThread
//     * @param fromNumber
//     */
//    private void processLeaveMusic(ApplicationController application, ReengMusicPacket received,
//                                   ReengMessagePacket.SubType subType, ThreadMessage mCorrespondingThread,
//                                   String fromNumber) {
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        if (musicBusiness.isExistListenMusic() &&
//                musicBusiness.getCurrentMusicSessionId().equals(received.getSessionId())) {// dang cung nghe nhan dc
//            // ban tin huy (insert notify huy)
//            myNumber = application.getReengAccountBusiness().getJidNumber();
//            ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                    mCorrespondingThread, fromNumber, myNumber);
//            musicBusiness.onReceivedLeaveMusic(reengMessage);
//            mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                    reengMessage, mCorrespondingThread.getThreadType());
//        } else {// khong cung nghe voi ai nhan hoac ban tin session id khac (tim va cap nhat message)
//            ReengMessage message = mMessageBusiness.findMessageInviteMusicBySessionMusicId(received.getSessionId(),
//                    mCorrespondingThread);
//            if (message != null && message.getDirection() == ReengMessageConstant.Direction.received &&
//                    message.getMusicState() == ReengMessageConstant.MUSIC_STATE_WAITING) {
//                // chi cap nhat message dang cho,
//                // nhung message da dong ys, hoac timeout roi thi ko cap nhat nua
//                message.setContent(String.format(mRes.getString(R.string.invite_share_music_canceled),
//                        mMessageBusiness.getFriendName(fromNumber)));
//                message.setFileName(String.format(mRes.getString(R.string.invite_share_music_canceled),
//                        TextHelper.textBoldWithHTML(mMessageBusiness.getFriendName(fromNumber))));
//                message.setDuration(0);
//                message.setMusicState(ReengMessageConstant.MUSIC_STATE_CANCEL);
//                CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(message);
//                mMessageBusiness.updateAllFieldsOfMessage(message);
//                mMessageBusiness.refreshThreadWithoutNewMessage(message.getThreadId());
//            }
//        }
//    }
//
//    /**
//     * xu ly ban tin music ping
//     *
//     * @param application
//     * @param received
//     * @param mCorrespondingThread
//     * @param fromNumber
//     */
//    private void processPingMusic(ApplicationController application, ReengMusicPacket received,
//                                  ThreadMessage mCorrespondingThread, String fromNumber) {
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        String currentSessionId = musicBusiness.getCurrentMusicSessionId();
//        if (currentSessionId != null && currentSessionId.equals(received.getSessionId())) {
//            if (!musicBusiness.isOnRoom()) {// nhan ping lan dau, start play music and update state
//                ReengMessage reengMessage = mMessageBusiness.
//                        findMessageInviteMusicBySessionMusicId(received.getSessionId(), mCorrespondingThread);
//                if (reengMessage != null) {
//                    reengMessage.setDuration(0);
//                    CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(reengMessage);
//                    reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_ACCEPTED);
//                    reengMessage.setFileName(String.format(mRes.getString(R.string.invite_share_music_accepted),
//                            TextHelper.textBoldWithHTML(mMessageBusiness.getFriendName(fromNumber))));
//                    reengMessage.setContent(String.format(mRes.getString(R.string.invite_share_music_accepted),
//                            mMessageBusiness.getFriendName(fromNumber)));
//                    // play music
//                    musicBusiness.onStartMusic(received.getSessionId());
//                    mMessageBusiness.refreshThreadWithoutNewMessage(mCorrespondingThread.getId());
//                    // bat dau chu ky pong
//                    musicBusiness.startTimerPongMusic();
//                    musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(), ReengMusicPacket
//                            .MusicStatus.available);
//                    // cap nhat db
//                    mMessageBusiness.updateAllFieldsOfMessage(reengMessage);
//                } else {// neu da bi xoa message thi play luon
//                    // play music
//                    musicBusiness.onStartMusic(received.getSessionId());
//                    // bat dau chu ky pong
//                    musicBusiness.startTimerPongMusic();
//                    musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(), ReengMusicPacket
//                            .MusicStatus.available);
//                }
//            } else {// dang play thi cap nhat count pong fail
//                musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(), ReengMusicPacket.MusicStatus
//                        .available);
//            }
//        } else if (currentSessionId != null) {// khac current id tra lai busy
//            musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(), ReengMusicPacket.MusicStatus.busy);
//        }
//    }
//
//    // xy ly ban tin pong music
//    private void processPongMusic(ApplicationController application, ReengMusicPacket received,
//                                  ThreadMessage mCorrespondingThread, String fromNumber) {
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        String currentSessionId = musicBusiness.getCurrentMusicSessionId();
//        if (currentSessionId != null && currentSessionId.equals(received.getSessionId()) &&
//                received.getMusicStatus() == ReengMusicPacket.MusicStatus.available) {
//            musicBusiness.onReceivePongPacket(fromNumber, received.getSessionId());
//        } else {
//            musicBusiness.onReceivePongBusyPacket(received.getSessionId(), mCorrespondingThread);
//        }
//    }
//
//    /**
//     * xu ly ban tin music action
//     *
//     * @param application
//     * @param received
//     * @param mCorrespondingThread
//     * @param fromNumber
//     */
//    private void processActionMusic(ApplicationController application, ReengMusicPacket received,
//                                    ThreadMessage mCorrespondingThread, String fromNumber) {
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        String currentSessionId = musicBusiness.getCurrentMusicSessionId();
//        // ban tin action cung session id
//        if (currentSessionId != null && currentSessionId.equals(received.getSessionId())) {
//            if (received.getMusicAction() == ReengMusicPacket.MusicAction.change) {
//                //nhan dc chuyen bai.insert message notify, next bai
//                myNumber = application.getReengAccountBusiness().getJidNumber();
//                ReengMessage reengMessage = mapPacketToMusicMessage(received, received.getSubType(),
//                        mCorrespondingThread, fromNumber, myNumber);
//                musicBusiness.onReceivedChangeSong(received, fromNumber, reengMessage.getSongModel(musicBusiness));
//                mMessageBusiness.notifyReengMessage(application,
//                        mCorrespondingThread, reengMessage, mCorrespondingThread.getThreadType());
//            } else {
//                // TODO chuyen bai. xoa bai ....
//            }
//        }
//    }
//
//    /**
//     * xy ly ban tin response_action
//     * thuc hien chuyen bai va tat loading luc gui action
//     *
//     * @param application
//     * @param received
//     * @param mCorrespondingThread
//     * @param fromNumber
//     */
//    private void processActionResponseMusic(ApplicationController application, ReengMusicPacket received,
//                                            ThreadMessage mCorrespondingThread, String fromNumber) {
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        String currentSessionId = musicBusiness.getCurrentMusicSessionId();
//        if (currentSessionId != null && currentSessionId.equals(received.getSessionId())) {
//            musicBusiness.onReceivedResponseAction(received, fromNumber, mCorrespondingThread);
//        }
//    }
//
//    /**
//     * xu ly ban tin invite
//     *
//     * @param application
//     * @param received
//     * @param fromNumber
//     * @param subType
//     * @param mCorrespondingThread
//     * @param isOnChatScreen
//     * @param isSendSeen
//     */
//    private void processStrangerAcceptMusic(ApplicationController application, ReengMusicPacket received,
//                                            String fromNumber, ReengMessagePacket.SubType subType,
//                                            ThreadMessage mCorrespondingThread, boolean isOnChatScreen,
//                                            boolean isSendSeen) {
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                mCorrespondingThread, fromNumber, myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        reengMessage.setDuration(0);
//        CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(reengMessage);
//        reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_ACCEPTED);
//        MusicBusiness musicBusiness = application.getMusicBusiness();
//        String currentSessionId = musicBusiness.getCurrentMusicSessionId();
//        if (!TimeHelper.checkTimeOutAcceptStrangerMusic(received)) {// chua qua time out thi play nhac
//            musicBusiness.setCurrentTimeStrangerMusic(-1);
//            if (currentSessionId != null && !currentSessionId.equals(received.getSessionId())) {
//                musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(),
//                        ReengMusicPacket.MusicStatus.busy);
//            } else {
//                if (TextUtils.isEmpty(currentSessionId)) {
//                    musicBusiness.onCreateSessionMusic(reengMessage.getImageUrl(),
//                            fromNumber, reengMessage.getSongModel(musicBusiness));
//                } else {
//                    musicBusiness.setCurrentNumberFriend(fromNumber);
//                }
//                musicBusiness.onStartMusic(received.getSessionId());
//                // bat dau chu ky pong
//                musicBusiness.startTimerPongMusic();
//                musicBusiness.onReceivePingPacket(fromNumber, received.getSessionId(),
//                        ReengMusicPacket.MusicStatus.available);
//            }
//        } else {// qua 1 phut time out reset rom cung nghe nguoi la
//            if (musicBusiness.isExistListenMusic() && musicBusiness.isWaitingStrangerMusic() &&
//                    currentSessionId != null && currentSessionId.equals(received.getSessionId())) {
//                musicBusiness.resetSessionMusic();
//            }
//        }
//        MusicBusiness.notifyReloadStrangerData();
//        Log.d(TAG, "what the hell?");
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//    }
//
//    /**
//     * xu ly ban tin invite
//     *
//     * @param application
//     * @param received
//     * @param fromNumber
//     * @param subType
//     * @param mCorrespondingThread
//     * @param isOnChatScreen
//     * @param isSendSeen
//     */
//    private void processStrangerReInviteMusic(ApplicationController application, ReengMusicPacket received,
//                                              String fromNumber, ReengMessagePacket.SubType subType,
//                                              ThreadMessage mCorrespondingThread, boolean isOnChatScreen,
//                                              boolean isSendSeen) {
//        myNumber = application.getReengAccountBusiness().getJidNumber();
//        ReengMessage reengMessage = mapPacketToMusicMessage(received, subType,
//                mCorrespondingThread, fromNumber, myNumber);
//        if (isOnChatScreen) {
//            if (isSendSeen) {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_READ);
//            } else {
//                reengMessage.setReadState(ReengMessageConstant.READ_STATE_SENT_SEEN);
//            }
//        }
//        int timeOut = TimeHelper.getTimeOutInviteMusic(received);
//        if (timeOut >= 1000) {// >1s
//            reengMessage.setDuration(timeOut);
//            CountDownInviteManager.getInstance(mApplication).startCountDownMessage(reengMessage);
//            reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_WAITING);
//        } else {
//            reengMessage.setDuration(0);
//            reengMessage.setMusicState(ReengMessageConstant.MUSIC_STATE_TIME_OUT);
//            CountDownInviteManager.getInstance(mApplication).stopCountDownMessage(reengMessage);
//        }
//        mMessageBusiness.notifyReengMessage(application, mCorrespondingThread,
//                reengMessage, ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT);
//        mApplication.getMusicBusiness().updatePrefReceiveInviteMusic(received);
//    }
//
//    /**
//     * chuyen packet nhan duoc thanh message, ap dung cho cac tin nhan den
//     * chi ap dung cho cung nghe 1-1
//     *
//     * @param packet
//     */
//    private ReengMessage mapPacketToMusicMessage(ReengMusicPacket packet, ReengMessagePacket.SubType subType,
//                                                 ThreadMessage threadMessage, String senderJid, String userNumber) {
//        ReengMessage message = new ReengMessage();
//        message.setPacketId(packet.getPacketID()); // set packet id
//        message.setReceiver(userNumber);
//        message.setReadState(ReengMessageConstant.READ_STATE_UNREAD);
//        message.setThreadId(threadMessage.getId());
//        message.setDirection(ReengMessageConstant.Direction.received);
//        message.setTime(packet.getTimeSend());
//        message.setExpired(packet.getExpired());
//        ReengMessagePacket.Type type = packet.getType();
//        String senderName;
//        String threadName = mMessageBusiness.getThreadName(threadMessage);
//        message.setReplyDetail(packet.getReply());
//        if (type == ReengMessagePacket.Type.chat) {
//            message.setSender(senderJid);
//            senderName = mMessageBusiness.getFriendName(message.getSender());
//        } else if (type == ReengMessagePacket.Type.groupchat) {
//            message.setSender(packet.getSender());
//            senderName = mMessageBusiness.getFriendNameOfGroup(message.getSender());
//        } else if (type == ReengMessagePacket.Type.offical) {
//            message.setSender(senderJid);
//            senderName = mMessageBusiness.getFriendName(message.getSender());
//        } else if (type == ReengMessagePacket.Type.roomchat) {
//            senderName = mMessageBusiness.getFriendNameOfRoom(packet.getSender(),
//                    packet.getSenderName(), threadName);
//            message.setSender(packet.getSender());
//            message.setSenderName(packet.getSenderName());
//            message.setSticky(packet.getStickyState());
//            message.setSenderAvatar(packet.getAvatarUrl());
//        } else {
//            senderName = senderJid;
//        }
//        // thong tin bai hat
//        String songId = packet.getSongId();
//        int songType = packet.getSongType();
//        String songName = packet.getSongName();
//        String singer = packet.getSinger();
//        String songUrl = packet.getSongUrl();
//        String mediaUrl = packet.getMediaUrl();
//        String songThumb = packet.getSongThumb();
//        String crbtCode = packet.getCrbtCode();
//        String crbtPrice = packet.getCrbtPrice();
//        String crbtSession = packet.getSession();
//        // neu co du lieu bai hat thi insert vao danh sach media model
//        if (!TextUtils.isEmpty(songId) && !TextUtils.isEmpty(mediaUrl)) {
//            MediaModel songModel = new MediaModel();
//            songModel.setId(songId);
//            songModel.setSongType(songType);
//            songModel.setName(songName);
//            songModel.setSinger(singer);
//            songModel.setUrl(songUrl);
//            songModel.setMedia_url(mediaUrl);
//            songModel.setImage(songThumb);
//            songModel.setCrbtCode(crbtCode);
//            songModel.setCrbtPrice(crbtPrice);
//            songModel.setAutoplayVideo(packet.getAutoPlayVideo());
//            message.setSongModel(songModel);
//            //mApplication.getMusicBusiness().insertNewMediaModel(songModel);
//        }
//        // set song id(05/04/2016, khong luu song id ma luu json nen mac dinh =-2)
//        message.setSongId(ReengMessage.SONG_ID_DEFAULT_NEW);
//        message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//        message.setSize(1);
//        switch (subType) {
//            case music_invite:
//                message.setMessageType(ReengMessageConstant.MessageType.inviteShareMusic);
//                message.setImageUrl(packet.getSessionId());// luu truong session vao imageurl
//                if (type == ReengMessagePacket.Type.roomchat || type == ReengMessagePacket.Type.groupchat) {
//                    message.setContent(String.format(mRes.getString(R.string.invite_share_music_receiver_room),
//                            senderName, threadName));
//                    message.setFileName(String.format(mRes.getString(R.string.invite_share_music_receiver_room),
//                            TextHelper.textBoldWithHTML(senderName), threadName));
//                } else {
//                    message.setContent(String.format(mRes.getString(R.string.invite_share_music_receiver), senderName));
//                    message.setFileName(String.format(mRes.getString(R.string.invite_share_music_receiver),
//                            TextHelper.textBoldWithHTML(senderName)));
//                }
//                break;
//            case music_leave:
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                message.setFileName(MessageConstants.NOTIFY_TYPE.leaveMusic.name());
//                message.setContent(String.format(mRes.getString(R.string.left_music_room), senderName));
//                break;
//            case music_action:
//                ReengMusicPacket.MusicAction musicAction = packet.getMusicAction();
//                if (musicAction == ReengMusicPacket.MusicAction.change) {
//                    // chuyen bai group
//                    message.setMessageType(ReengMessageConstant.MessageType.actionShareMusic);
//                    //message.setSongId(packet.getSongId());
//                    /*message.setContent(String.format(mRes.getString(R.string.friend_change_song),
//                            String.format(mMessageBusiness.getFriendName(fromNumber)), songName));*/
//                    message.setContent(String.format(mRes.getString(R.string.friend_change_song_v2),
//                            mMessageBusiness.getFriendName(senderJid)) + " \"" + songName + "\"");
//                    message.setFileName(String.format(mRes.getString(R.string.friend_change_song_v2),
//                            TextHelper.textBoldWithHTML(mMessageBusiness.getFriendName(senderJid))));
//                } else {
//                    //TODO xu ly chuyen bai, them bai
//                }
//                break;
//            case music_stranger_accept:
//                message.setMessageType(ReengMessageConstant.MessageType.inviteShareMusic);
//                message.setContent(String.format(mRes.getString(R.string.stranger_music_poster_accepted),
//                        senderName));
//                message.setFileName(String.format(mRes.getString(R.string.stranger_music_poster_accepted),
//                        TextHelper.textBoldWithHTML(senderName)));
//                // luu truong session vao imageurl
//                message.setImageUrl(packet.getSessionId());
//                break;
//            case music_stranger_reinvite:
//                message.setMessageType(ReengMessageConstant.MessageType.inviteShareMusic);
//                message.setContent(String.format(mRes.getString(R.string.invite_share_music_receiver), senderName));
//                message.setFileName(String.format(mRes.getString(R.string.invite_share_music_receiver),
//                        TextHelper.textBoldWithHTML(senderName)));
//                // luu truong session vao imageurl
//                message.setImageUrl(packet.getSessionId());
//                break;
//            case music_accept://room dong y nghe
//                message.setMessageType(ReengMessageConstant.MessageType.notification);
//                String myJidNumber = mApplication.getReengAccountBusiness().getJidNumber();
//                if (myJidNumber != null && myJidNumber.equals(senderJid)) {// dong y chinh minh
//                    message.setContent(String.format(mRes.getString(R.string.msg_accept_music_room_me),
//                            threadName));
//                } else {
//                    message.setContent(String.format(mRes.getString(R.string.msg_accept_music_room),
//                            threadName, senderName));
//                }
//                // luu truong session vao imageurl
//                message.setImageUrl(packet.getSessionId());
//                break;
//            case crbt_gift:
//                message.setMessageType(ReengMessageConstant.MessageType.crbt_gift);
//                message.setCrbtGiftSession(crbtSession);
//                message.setContent(String.format(mRes.getString(R.string.msg_receiver_crbt), senderName));
//                message.setFileName(String.format(mRes.getString(R.string.msg_receiver_crbt), TextHelper
//                        .textBoldWithHTML(senderName)));
//                break;
//            case music_request_change:
//                message.setMessageType(ReengMessageConstant.MessageType.inviteShareMusic);
//                message.setMusicState(ReengMessageConstant.MUSIC_STATE_REQUEST_CHANGE);
//                message.setContent(String.format(mRes.getString(R.string.friend_request_change_song),
//                        mMessageBusiness.getFriendName(senderJid)) + " \"" + songName + "\"");
//                message.setFileName(String.format(mRes.getString(R.string.friend_request_change_song),
//                        TextHelper.textBoldWithHTML(mMessageBusiness.getFriendName(senderJid))));
//                break;
//            case watch_video:
//                message.setMessageType(ReengMessageConstant.MessageType.watch_video);
//                message.setContent(packet.getBody());
//                message.setFileName(packet.getBody());
//                break;
//            default:
//                break;
//        }
//        return message;
//    }
//
//    public void processIncomingEventMessage(ReengMessagePacket receivedMessage) {
//        Log.i(TAG, "processIncomingEventMessage");
//        mMessageBusiness.processEventMessage(receivedMessage);
//    }
//
//    public void processIncomingNotificationImage(ReengMessagePacket receivedPacket) {
//        ReengNotificationManager.getInstance(mApplication).
//                drawNotifyPreviewImage(receivedPacket.getSubject(),
//                        receivedPacket.getBody(),
//                        receivedPacket.getMediaLink(),
//                        receivedPacket.getLink());
//    }
}