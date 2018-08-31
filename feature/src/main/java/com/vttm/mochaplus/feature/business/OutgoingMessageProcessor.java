package com.vttm.mochaplus.feature.business;

import android.content.res.Resources;

import com.vttm.mochaplus.feature.ApplicationController;

/**
 * Created by thaodv on 22-Dec-14.
 */
public class OutgoingMessageProcessor {
    private static final String TAG = OutgoingMessageProcessor.class.getSimpleName();
    private MessageBusiness mMessageBusiness;
    private ApplicationController mApplication;
    private Resources mRes;

    public OutgoingMessageProcessor(MessageBusiness business, ApplicationController app) {
        mMessageBusiness = business;
        mApplication = app;
        mRes = mApplication.getResources();
    }

//    public void sendMochaToSmsUnlimited(final ReengMessage message,
//                                        final BaseSlidingFragmentActivity mParentActivity, final ThreadMessage
//                                                threadMessage) {
//        if (!NetworkHelper.isConnectInternet(mParentActivity)) {
//            mParentActivity.showToast(mRes.getString(R.string.error_internet_disconnect), Toast.LENGTH_LONG);
//            return;
//        }
//        if (message == null || threadMessage == null) {
//            mParentActivity.showToast(mRes.getString(R.string.e601_error_but_undefined), Toast.LENGTH_LONG);
//            return;
//        }
//        mParentActivity.showLoadingDialog(null, mRes.getString(R.string.waiting));
//        String url = UrlConfigHelper.getInstance(mParentActivity).getUrlConfigOfFile(Config.UrlEnum.MOCHA_2_SMS);
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse: " + response);
//                mParentActivity.hideLoadingDialog();
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//                    int errorCode = -1;
//                    if (responseObject.has(Constants.HTTP.REST_CODE)) {
//                        errorCode = responseObject.getInt(Constants.HTTP.REST_CODE);
//                    }
//                    if (errorCode != HTTPCode.E200_OK) {
//                        mParentActivity.showToast(mRes.getString(R.string.e601_error_but_undefined), Toast.LENGTH_LONG);
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, "Exception", e);
//                    mParentActivity.showToast(mRes.getString(R.string.e500_internal_server_error), Toast.LENGTH_LONG);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e(TAG, "VolleyError", volleyError);
//                mParentActivity.hideLoadingDialog();
//                mParentActivity.showToast(mRes.getString(R.string.e604_error_connect_server), Toast.LENGTH_LONG);
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                ReengAccountBusiness accountBusiness = mApplication.getReengAccountBusiness();
//                HashMap<String, String> params = new HashMap<>();
//                long currentTime = TimeHelper.getCurrentTime();
//                ReengMessagePacket packet = mapMessageToPacket(message, threadMessage);
//                String packetXml = packet.toXML();
//                StringBuilder sb = new StringBuilder().
//                        append(accountBusiness.getJidNumber()).
//                        append(message.getReceiver()).
//                        append(packetXml).
//                        append(accountBusiness.getToken()).
//                        append(currentTime);
//                String dataEncrypt = HttpHelper.encryptDataV2(mApplication, sb.toString(), accountBusiness.getToken());
//                params.put(Constants.HTTP.REST_MSISDN, accountBusiness.getJidNumber());
//                params.put(Constants.HTTP.MESSGE.TO, message.getReceiver());
//                params.put(Constants.HTTP.MESSGE.CONTENT, packetXml);
//                params.put(Constants.HTTP.TIME_STAMP, String.valueOf(currentTime));
//                params.put(Constants.HTTP.DATA_SECURITY, dataEncrypt);
//                return params;
//            }
//        };
//        VolleyHelper.getInstance(mApplication).addRequestToQueue(request, TAG, false);
//    }
//
//    /**
//     * request restore message
//     *
//     * @param message
//     * @param threadType
//     * @param groupSize
//     * @param listener
//     */
//    public void handleRestoreReengMessage(final ReengMessage message, final int threadType, final int groupSize,
//                                          final OnResponseHanderMessageListener listener) {
//        if (!NetworkHelper.isConnectInternet(mApplication)) {
//            listener.onError(mRes.getString(R.string.error_internet_disconnect));
//        } else if (message.getStatus() == ReengMessageConstant.STATUS_FAIL) {
//            message.setMessageType(ReengMessageConstant.MessageType.restore);
//            message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//            mMessageBusiness.updateAllFieldsOfMessage(message);
//            listener.onRestoreSuccess();
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT ||
//                threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            String url = UrlConfigHelper.getInstance(mApplication).getUrlConfigOfFile(Config.UrlEnum.RESTORE_MESSAGE);
//            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d(TAG, "onResponse: " + response);
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//                        int errorCode = -1;
//                        if (responseObject.has(Constants.HTTP.REST_CODE)) {
//                            errorCode = responseObject.getInt(Constants.HTTP.REST_CODE);
//                        }
//                        if (errorCode == HTTPCode.E200_OK) {
//                            if (responseObject.has(Constants.HTTP.REST_NOT_SUPPORT)) {
//                                if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//                                    //size ko tinh chinh minh
//                                    JSONArray arrayInvalidUser = responseObject.getJSONArray(
//                                            Constants.HTTP.REST_NOT_SUPPORT);
//                                    int notSupportSize = arrayInvalidUser.length();
//                                    if (notSupportSize == groupSize) {
//                                        listener.onError(mRes.getString(R.string.not_suppport_restore));
//                                    } else {
//                                        message.setMessageType(ReengMessageConstant.MessageType.restore);
//                                        message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                                        mMessageBusiness.updateAllFieldsOfMessage(message);
//                                        Log.i(TAG, "RestoreReengMessage: " + message);
//                                        listener.onRestoreSuccess();
//                                    }
//                                } else {
//                                    listener.onError(mRes.getString(R.string.not_suppport_restore));
//                                }
//                            } else {
//                                message.setMessageType(ReengMessageConstant.MessageType.restore);
//                                message.setStatus(ReengMessageConstant.STATUS_RECEIVED);
//                                mMessageBusiness.updateAllFieldsOfMessage(message);
//                                Log.i(TAG, "RestoreReengMessage: " + message);
//                                listener.onRestoreSuccess();
//                            }
//                        } else {
//                            listener.onError(mRes.getString(R.string.e601_error_but_undefined));
//                        }
//                    } catch (Exception e) {
//                        Log.e(TAG, "Exception", e);
//                        listener.onError(mRes.getString(R.string.e500_internal_server_error));
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    listener.onError(mRes.getString(R.string.e604_error_connect_server));
//                    Log.e(TAG, "VolleyError", volleyError);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    String threadTypeString = "";
//                    ReengAccountBusiness accountBusiness = mApplication.getReengAccountBusiness();
//                    HashMap<String, String> params = new HashMap<>();
//                    long currentTime = TimeHelper.getCurrentTime();
//                    if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//                        threadTypeString = ReengMessagePacket.Type.chat.toString();
//                    } else if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//                        threadTypeString = ReengMessagePacket.Type.groupchat.toString();
//                    }
//                    StringBuilder sb = new StringBuilder().
//                            append(message.getSender()).
//                            append(message.getReceiver()).
//                            append(message.getPacketId()).
//                            append(threadTypeString).
//                            append(accountBusiness.getToken()).
//                            append(currentTime);
//                    String dataEncrypt = HttpHelper.encryptDataV2(mApplication, sb.toString(), accountBusiness
//                            .getToken());
//                    params.put(Constants.HTTP.MESSGE.FROM, message.getSender());
//                    params.put(Constants.HTTP.MESSGE.TO, message.getReceiver());
//                    params.put(Constants.HTTP.MESSGE.MSG_ID, message.getPacketId());
//                    params.put(Constants.HTTP.MESSGE.TYPE, threadTypeString);
//                    params.put(Constants.HTTP.TIME_STAMP, String.valueOf(currentTime));
//                    params.put(Constants.HTTP.DATA_SECURITY, dataEncrypt);
//                    return params;
//                }
//            };
//            VolleyHelper.getInstance(mApplication).addRequestToQueue(request, TAG, false);
//        } else {
//            listener.onError(mRes.getString(R.string.not_suppport_restore));
//        }
//    }
//
//    public ReengMessagePacket mapMessageToPacket(ReengMessage newMessage, ThreadMessage threadMessage) {
//        int threadType = threadMessage.getThreadType();
//        // send message
//        ReengMessagePacket packet = new ReengMessagePacket();
//        packet.setPacketID(newMessage.getPacketId());
//        if (threadType == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            String receiver = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(newMessage.getReceiver());
//            Log.i(TAG, "-------PREFIX-----convert receiver: " + receiver + " orig receiver: " + newMessage.getReceiver());
//            if (receiver == null)
//                packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_RESOUCE);
//            else
//                packet.setTo(receiver + Constants.XMPP.XMPP_RESOUCE);
//            packet.setType(ReengMessagePacket.Type.chat);
//            // neu la thread lam quen thi them external
//            StrangerPhoneNumber strangerPhoneNumber = threadMessage.getStrangerPhoneNumber();
//            if (strangerPhoneNumber != null && threadMessage.isStranger()) {
//                packet.setExternal(strangerPhoneNumber.getAppId());
//                String myName = strangerPhoneNumber.getMyName();
//                if (TextUtils.isEmpty(myName)) {
//                    myName = mApplication.getReengAccountBusiness().getUserName();
//                }
//                packet.setNick(myName);
//                //get noncontact + add lastseen vao packet
//                NonContact nonContact = mApplication.getContactBusiness().getExistNonContact(strangerPhoneNumber
//                        .getPhoneNumber());
//                if (nonContact != null) {
//                    long lastSeen = nonContact.getLastSeen();
//                    if (lastSeen == 0) {
//                        packet.setLastSeen(System.currentTimeMillis());
//                    } else if (lastSeen > 0) {
//                        packet.setLastSeen(lastSeen);
//                    }
//                }
//            }
//            packet.setCState(newMessage.getCState());
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            String myName = mApplication.getReengAccountBusiness().getUserName();
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_GROUP_RESOUCE);
//            packet.setType(ReengMessagePacket.Type.groupchat);
//            packet.setSenderName(myName);
//            packet.setLastAvatar(mApplication.getReengAccountBusiness().getLastChangeAvatar());
//            //packet.setGroupClass(threadMessage.getGroupClass());
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_OFFICER_CHAT) {
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_OFFICAL_RESOUCE);
//            packet.setType(ReengMessagePacket.Type.offical);
//            packet.setNoStore(true);
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            String myName = mApplication.getReengAccountBusiness().getUserName();
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_ROOM_RESOUCE);
//            packet.setType(ReengMessagePacket.Type.roomchat);
//            packet.setSenderName(myName);
//            packet.setAvatarUrl(mApplication.getReengAccountBusiness().getLastChangeAvatar());
//            packet.setNoStore(true);
//        } else if (threadType == ThreadMessageConstant.TYPE_THREAD_BROADCAST_CHAT) {
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_BROADCAST_RESOUCE);
//            packet.setType(ReengMessagePacket.Type.chat);
//            packet.setPhoneNumbers(threadMessage.getPhoneNumbers());
//        }
//        ReengMessageConstant.MessageType messageType = newMessage
//                .getMessageType();
//        // reply
//        String reply = newMessage.getReplyDetail();
//        packet.setReply(reply);
//        String avnoNumber = mApplication.getReengAccountBusiness().getAVNONumber();
//        packet.setAvnoNumber(avnoNumber);
//        switch (messageType) {
//            case text:
//                packet.setSubType(ReengMessagePacket.SubType.text);
//                packet.setBody(newMessage.getContent());
//                packet.setLargeEmo(newMessage.getFileId());
//                packet.setTextTag(newMessage.getTagContent());
//                break;
//            case file:
//                packet.setSubType(ReengMessagePacket.SubType.file_2);
//                packet.setFileId(newMessage.getFileId());
//                packet.setName(newMessage.getFileName());
//                packet.setMediaLink(newMessage.getDirectLinkMedia());
//                packet.setSize(newMessage.getSize());
//                //TODO tạm thời ko cần gửi extension file đi. nhận được thì parser từ fileName luôn
//                //packet.setRatio(newMessage.getVideoContentUri());
//                break;
//            case image:
//                packet.setSubType(ReengMessagePacket.SubType.image);
//                packet.setFileId(newMessage.getFileId());
//                packet.setName(newMessage.getFileName());
//                packet.setSize(newMessage.getSize());
//                packet.setMediaLink(newMessage.getDirectLinkMedia());
//                packet.setRatio(newMessage.getVideoContentUri());
//                break;
//            case voicemail:
//                packet.setSubType(ReengMessagePacket.SubType.voicemail);
//                packet.setFileId(newMessage.getFileId());
//                packet.setName(newMessage.getFileName());
//                packet.setSize(newMessage.getSize());
//                packet.setDuration(newMessage.getDuration());
//                packet.setMediaLink(newMessage.getDirectLinkMedia());
//                break;
//            case shareContact:
//                packet.setSubType(ReengMessagePacket.SubType.contact);
//                packet.setName(newMessage.getContent());
//                packet.setTel(newMessage.getFileName());
//                break;
//            case shareVideo:
//                packet.setSubType(ReengMessagePacket.SubType.sharevideov2);
//                packet.setFileId(newMessage.getFileId());
//                packet.setName(newMessage.getFileName());
//                packet.setSize(newMessage.getSize());
//                packet.setDuration(newMessage.getDuration());
//                packet.setVideoThumb(newMessage.getImageUrl());
//                packet.setMediaLink(newMessage.getDirectLinkMedia());
//                break;
//            case voiceSticker:
//                packet.setSubType(ReengMessagePacket.SubType.voicesticker);
//                packet.setStickerPacket(newMessage.getFileName());
//                packet.setStickerId(newMessage.getSongId());
//                break;
//            case shareLocation:
//                packet.setSubType(ReengMessagePacket.SubType.location);
//                packet.setBody(newMessage.getContent());
//                packet.setLat(newMessage.getFilePath());// latitude
//                packet.setLng(newMessage.getImageUrl());// longitude
//                break;
//            case transferMoney:
//                packet.setSubType(ReengMessagePacket.SubType.transfer_money);
//                packet.setAmountMoney(newMessage.getContent());
//                packet.setUnitMoney(newMessage.getImageUrl());
//                packet.setTimeTransferMoney(newMessage.getFilePath());
//                break;
//            case notification:
//                break;
//            case inviteShareMusic:
//                break;
//            case actionShareMusic:
//                break;
//            case bank_plus:
//                packet.setSubType(ReengMessagePacket.SubType.bank_plus);
//                packet.setBPlusType(newMessage.getFilePath());
//                packet.setBPlusAmount(newMessage.getImageUrl());
//                packet.setBPlusDesc(newMessage.getVideoContentUri());
//                packet.setBPlusId(newMessage.getFileId());
//                break;
//            case lixi:
//                packet.setSubType(ReengMessagePacket.SubType.lixi);
//                packet.setAmountLixi(newMessage.getImageUrl());
//                packet.setBody(newMessage.getContent());
//                packet.setSplitRandom(newMessage.getDuration());
//                packet.setOrderId(newMessage.getFileId());
//                packet.setRequestIdLixi(newMessage.getFilePath());
//                packet.setListMemberLixiStr(newMessage.getVideoContentUri());
//                break;
//
//            case pin_message:
//                packet.setSubType(ReengMessagePacket.SubType.pin_msg);
//                packet.setBody(newMessage.getContent());
//                packet.setPinMsgAction(newMessage.getSize());
//                packet.setPinMsgImg(newMessage.getImageUrl());
//                packet.setPinMsgTitle(newMessage.getFileName());
//                packet.setPinType(newMessage.getSongId());
//                packet.setPinMsgTarget(newMessage.getPacketId());
//                break;
//        }
//        return packet;
//    }
//
//    public ReengMusicPacket mapMusicMessageToPacket(ReengMessage newMessage, MediaModel songModel, ThreadMessage
//            threadMessage) {
//        ReengMusicPacket packet = new ReengMusicPacket();
//        packet.setPacketID(newMessage.getPacketId());
//        if (newMessage.getMessageType() == ReengMessageConstant.MessageType.actionShareMusic) {
//            packet.setSubType(ReengMessagePacket.SubType.music_action);
//            packet.setMusicAction(ReengMusicPacket.MusicAction.change);
//            packet.setNoStore(true);
//        } else if (newMessage.getMessageType() == ReengMessageConstant.MessageType.watch_video) {
//            packet.setSubType(ReengMessagePacket.SubType.watch_video);
//            packet.setNoStore(true);
//        } else {
//            if (newMessage.getMusicState() == ReengMessageConstant.MUSIC_STATE_REQUEST_CHANGE) {
//                packet.setSubType(ReengMessagePacket.SubType.music_request_change);
//                packet.setNoStore(true);
//            } else {
//                packet.setSubType(ReengMessagePacket.SubType.music_invite);
//                if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//                    packet.setNoStore(true);
//                } else {
//                    packet.setNoStore(false);
//                }
//            }
//        }
//        if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            packet.setType(ReengMessagePacket.Type.chat);
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_RESOUCE);
//            // neu la thread lam quen thi them external
//            StrangerPhoneNumber strangerPhoneNumber = threadMessage.getStrangerPhoneNumber();
//            if (threadMessage.isStranger() && strangerPhoneNumber != null) {
//                packet.setExternal(strangerPhoneNumber.getAppId());
//                String myName = strangerPhoneNumber.getMyName();
//                if (TextUtils.isEmpty(myName)) {
//                    myName = mApplication.getReengAccountBusiness().getUserName();
//                }
//                packet.setNick(myName);
//            }
//            packet.setCState(newMessage.getCState());
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            packet.setType(ReengMessagePacket.Type.groupchat);
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_GROUP_RESOUCE);
//            //packet.setGroupClass(threadMessage.getGroupClass());
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            String myName = mApplication.getReengAccountBusiness().getUserName();
//            packet.setType(ReengMessagePacket.Type.roomchat);
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_ROOM_RESOUCE);
//            packet.setSenderName(myName);
//        } else {
//            return null;// type khong dc ho tro
//        }
//        String avnoNumber = mApplication.getReengAccountBusiness().getAVNONumber();
//        packet.setAvnoNumber(avnoNumber);
//        // voi tin invite thi lay packet id lam session id luon
//        packet.setSessionId(newMessage.getImageUrl());
//        packet.setSongType(songModel.getSongType());
//        packet.setSongId(songModel.getId());
//        packet.setSongName(songModel.getName());
//        packet.setSinger(songModel.getSinger());
//        packet.setSongUrl(songModel.getUrl());
//        packet.setMediaUrl(songModel.getMedia_url());
//        packet.setSongThumb(songModel.getImage());
//        packet.setCrbtCode(songModel.getCrbtCode());
//        packet.setCrbtPrice(songModel.getCrbtPrice());
//        return packet;
//    }
//
//    public ReengMusicPacket mapVideoMessageToPacket(ReengMessage newMessage, MediaModel songModel, ThreadMessage
//            threadMessage) {
//        ReengMusicPacket packet = new ReengMusicPacket();
//        packet.setPacketID(newMessage.getPacketId());
//        packet.setSubType(ReengMessagePacket.SubType.watch_video);
//        if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            packet.setType(ReengMessagePacket.Type.chat);
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_RESOUCE);
//            // neu la thread lam quen thi them external
//            StrangerPhoneNumber strangerPhoneNumber = threadMessage.getStrangerPhoneNumber();
//            if (threadMessage.isStranger() && strangerPhoneNumber != null) {
//                packet.setExternal(strangerPhoneNumber.getAppId());
//                String myName = strangerPhoneNumber.getMyName();
//                if (TextUtils.isEmpty(myName)) {
//                    myName = mApplication.getReengAccountBusiness().getUserName();
//                }
//                packet.setNick(myName);
//            }
//            packet.setCState(newMessage.getCState());
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            packet.setType(ReengMessagePacket.Type.groupchat);
//            packet.setTo(newMessage.getReceiver() + Constants.XMPP.XMPP_GROUP_RESOUCE);
//            //packet.setGroupClass(threadMessage.getGroupClass());
//        }
//        String avnoNumber = mApplication.getReengAccountBusiness().getAVNONumber();
//        packet.setAvnoNumber(avnoNumber);
//        // voi tin invite thi lay packet id lam session id luon
//        packet.setSongId(songModel.getId());
//        packet.setSongName(songModel.getName());
//        packet.setSinger(songModel.getSinger());
//        packet.setSongUrl(songModel.getUrl());
//        packet.setMediaUrl(songModel.getMedia_url());
//        packet.setSongThumb(songModel.getImage());
//        return packet;
//    }
//
//    public ReengCallPacket mapCallMessageToPacket(MochaCallMessage message, ThreadMessage threadMessage,
//                                                  boolean isConfide, boolean isVideoCall, boolean isOnlyAudio) {
//        ReengCallPacket packet = new ReengCallPacket();
//        packet.setPacketID(message.getPacketId());
//        packet.setCallConfide(isConfide);
//        packet.setVideoCall(isVideoCall);
//        packet.setOnlyAudio(isOnlyAudio);
//        packet.setTimeConnect(message.getTimeConnect());
//        packet.setRestartICESuccess(message.isRestartICESuccess());
//        packet.setSettingXML(message.getSettingXML());
//        packet.setRestartReason(message.getRestartReason());
//        packet.setCountryCode(mApplication.getReengAccountBusiness().getRegionCode());
//        packet.setLanguageCode(mApplication.getReengAccountBusiness().getCurrentLanguage());
//        if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            packet.setType(ReengMessagePacket.Type.chat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_RESOUCE);
//            // neu la thread lam quen thi them external
//            StrangerPhoneNumber strangerPhoneNumber = threadMessage.getStrangerPhoneNumber();
//            String friendName;
//            if (threadMessage.isStranger() && strangerPhoneNumber != null) {
//                packet.setExternal(strangerPhoneNumber.getAppId());
//                String myName = strangerPhoneNumber.getMyName();
//                if (TextUtils.isEmpty(myName)) {
//                    myName = mApplication.getReengAccountBusiness().getUserName();
//                }
//                packet.setNick(myName);
//                friendName = strangerPhoneNumber.getFriendName();
//            } else {
//                packet.setNick(mApplication.getReengAccountBusiness().getUserName());
//                friendName = threadMessage.getThreadName();
//            }
//            if (isConfide && message.getCallData() != null) {// ban tin call data tâm sự người lạ
//                packet.setStrangerPosterName(friendName);
//                packet.setStrangerAvatar(mApplication.getReengAccountBusiness().getLastChangeAvatar());
//            }
//            packet.setCState(message.getCState());
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            packet.setType(ReengMessagePacket.Type.groupchat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_GROUP_RESOUCE);
//            //packet.setGroupClass(threadMessage.getGroupClass());
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            String myName = mApplication.getReengAccountBusiness().getUserName();
//            packet.setType(ReengMessagePacket.Type.roomchat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_ROOM_RESOUCE);
//            packet.setSenderName(myName);
//        } else {
//            return null;// type khong dc ho tro
//        }
//        String avnoNumber = mApplication.getReengAccountBusiness().getAVNONumber();
//        packet.setAvnoNumber(avnoNumber);
//        packet.setSubType(ReengMessagePacket.SubType.call_rtc_2);
//        packet.setCaller(message.getCaller());
//        packet.setCallee(message.getCallee());
//        packet.setCallError(message.getCallError());
//        packet.setCallData(message.getCallData());
//        packet.setCallSession(message.getCallSession());
//        packet.setIceServers(message.getIceServers());
//        return packet;
//    }
//
//    public ReengCallOutPacket mapCallOutMessageToPacket(MochaCallOutMessage message, ThreadMessage threadMessage) {
//        ReengCallOutPacket packet = new ReengCallOutPacket();
//        packet.setPacketID(message.getPacketId());
//        packet.setTimeConnect(message.getTimeConnect());
//        packet.setCountryCode(mApplication.getReengAccountBusiness().getRegionCode());
//        packet.setLanguageCode(mApplication.getReengAccountBusiness().getCurrentLanguage());
//        if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_PERSON_CHAT) {
//            packet.setType(ReengMessagePacket.Type.chat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_RESOUCE);
//            // neu la thread lam quen thi them external
//            StrangerPhoneNumber strangerPhoneNumber = threadMessage.getStrangerPhoneNumber();
//            if (threadMessage.isStranger() && strangerPhoneNumber != null) {
//                packet.setExternal(strangerPhoneNumber.getAppId());
//                String myName = strangerPhoneNumber.getMyName();
//                if (TextUtils.isEmpty(myName)) {
//                    myName = mApplication.getReengAccountBusiness().getUserName();
//                }
//                packet.setNick(myName);
//            }
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_GROUP_CHAT) {
//            packet.setType(ReengMessagePacket.Type.groupchat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_GROUP_RESOUCE);
//        } else if (threadMessage.getThreadType() == ThreadMessageConstant.TYPE_THREAD_ROOM_CHAT) {
//            String myName = mApplication.getReengAccountBusiness().getUserName();
//            packet.setType(ReengMessagePacket.Type.roomchat);
//            packet.setTo(message.getReceiver() + Constants.XMPP.XMPP_ROOM_RESOUCE);
//            packet.setSenderName(myName);
//        } else {
//            return null;// type khong dc ho tro
//        }
//        if (message.isCallIn()) {
//            packet.setSubType(ReengMessagePacket.SubType.call_in);
//        } else {
//            packet.setSubType(ReengMessagePacket.SubType.call_out);
//        }
//        String avnoNumber = mApplication.getReengAccountBusiness().getAVNONumber();
//        packet.setAvnoNumber(avnoNumber);
//        packet.setCaller(message.getCaller());
//        packet.setCallee(message.getCallee());
//        packet.setCallStatus(message.getCallStatus());
//        packet.setCallOutType(message.getCallOutType());
//        packet.setCallOutData(message.getCallData());
//        packet.setCallSession(message.getCallSession());
//        packet.setIceServers(message.getIceServers());
//        packet.setRestartICESuccess(message.isRestartICESuccess());
//        packet.setRestartReason(message.getRestartReason());
//        return packet;
//    }

    public interface OnResponseHanderMessageListener {
        void onRestoreSuccess();

        void onError(String msgError);
    }
}