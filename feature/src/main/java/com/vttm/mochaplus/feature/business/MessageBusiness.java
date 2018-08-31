package com.vttm.mochaplus.feature.business;

import android.content.res.Resources;

import com.vttm.chatlib.packet.EventReceivedMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.ReengMessageListener;
import com.vttm.mochaplus.feature.model.ReengMessage;

import org.jivesoftware.smack.packet.Stanza;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageBusiness {
    private static final String TAG = MessageBusiness.class.getSimpleName();
    private static final int PACKET_ID_LIST_MAX_SIZE = 2000;
    private ReengMessageListener mReengMessageListenerThreadDetailInbox;
    private CopyOnWriteArrayList<ReengMessageListener> mReengMessageListenerArrayList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<ConfigGroupListener> mConfigGroupListenersArrayList = new CopyOnWriteArrayList<>();
    private EventAppListener mEventAppListener;
    private LockRoomListener mLockRoomListener;
    private ApplicationController mApplication;
    private CopyOnWriteArrayList<ThreadMessage> mThreadMessageArrayList;
//    private ReengMessageDataSource mReengMessageDataSource;
//    private ThreadMessageDataSource mThreadMessageDataSource;
//    private EventMessageDataSource mEventMessageDataSource;
//    private MessageImageDataSource mMessageImageDataSource;
//    private BlockContactBusiness mBlockContactBusiness;
    private DataManager dataManager;
    private ReengAccountBusiness mAccountBusiness;
    private Resources mRes;
    private boolean isDataLoaded = false;
    private int gsmMessageErrorCode;
    private String myNumber;
    private ContactBusiness mContactBusiness;
    private MessageRetryManager mMessageRetryManager;
    private SettingBusiness mSettingBusiness;
    private LinkedList<String> listPacketIdOfReceivedMessage;
    private ApplicationStateManager mApplicationStateManager;
    private IncomingMessageProcessor mIncomingMessageProcessor;
    private OutgoingMessageProcessor mOutgoingMessageProcessor;
    private CopyOnWriteArrayList<ReengMessage> listMessageProcessInfo;


    public MessageBusiness(ApplicationController app) {
        this.mApplication = app;
    }

    public void init() {
        dataManager = mApplication.getDataManager();
    }

    public void processGsmResponse(Stanza message) {
        
    }

    public void processReceivedPacket(ApplicationController application, EventReceivedMessagePacket event) {

    }
}
