package com.vttm.mochaplus.feature.data.socket.xmpp;

import android.content.Context;

import com.vttm.chatlib.filter.FromContainsFilter;
import com.vttm.chatlib.packet.EventReceivedMessagePacket;
import com.vttm.chatlib.packet.GSMMessagePacket;
import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.chatlib.packet.ShareMusicMessagePacket;
import com.vttm.chatlib.packet.VoiceMailGSMMessagePacket;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.GSMMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.PresenceListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.RecevedSoloMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.ReengMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.ShareMusicMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.SuccessNonSASLListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.VoicemailGSMResponseMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.XMPPConnectionListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.XMPPConnectivityChangeListener;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.NetworkUtils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;

import java.util.concurrent.CopyOnWriteArrayList;

public class XMPPManager {
    private static final String TAG = XMPPManager.class.getSimpleName();
    private static final int PING_FAIL_THRESHOLD = 1; // 0 --> 1 --> 2
    private static CopyOnWriteArrayList<XMPPConnectivityChangeListener> mXmppConnectivityChangeListeners = new CopyOnWriteArrayList<>();
    private static int mConnectionState;
    private ConnectionConfiguration mConfig;
    private XMPPConnection mConnection;
    private ApplicationController mApplication;
    private boolean isDisconnecting = false;

    // ------------listener-----------------------------------------
    private XMPPConnectionListener xmppConnectionListener;
    private PresenceListener presenceListener;
    // message
    private ReengMessageListener reengMessageListener;
    private ShareMusicMessageListener shareMusicMessageListener;
    private RecevedSoloMessageListener recevedSoloMessageListener;
    private GSMMessageListener gsmMessageListener;
    private VoicemailGSMResponseMessageListener voicemailGSMResponseMessageListener;
    private SuccessNonSASLListener successNonSASLListener;

    public XMPPManager(ApplicationController app) {
        mApplication = app;
        mPref = app.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
        configSmack();
        mMessageRetryManager = MessageRetryManager.getInstance(app, this);
    }

    private void configSmack() {

    }


    public boolean isConnected() {
        if (mConnection == null)
            return false;
        return mConnection.isConnected();
    }










    /**
     * listener for connection
     */
    private void addConnectionListener() {
        if (isConnected()) {
            xmppConnectionListener = new XMPPConnectionListener(mApplication,
                    mConnection, this);
            mConnection.addConnectionListener(xmppConnectionListener);
            NetworkUtils.addNetworkConnectivityChangeListener(xmppConnectionListener);
        }
    }

    private void addAllListener(ApplicationController applicationController) {
        // contact
        this.addPresenceChangedListener();
        this.addReengMessageListener(applicationController);
        this.addShareMusicMessageListener(applicationController);
        // message solo
        this.addMessageReceivedListener(applicationController);
        this.addGSMMessageListener(applicationController);
        this.addVoiceMailGSMResponseMessageListener(applicationController);
    }

    private void addPresenceChangedListener() {
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(Presence.class));
        presenceListener = new PresenceListener(mApplication);
        mConnection.addSyncStanzaListener(presenceListener, filter);
    }

    /**
     * addReengMessageListener
     */
    private void addReengMessageListener(
            ApplicationController applicationController) {
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(ReengMessagePacket.class));
        reengMessageListener = new ReengMessageListener(applicationController);
        mConnection.addSyncStanzaListener(reengMessageListener, filter);
    }

    /**
     * addReengMessageListener
     */
    private void addShareMusicMessageListener(ApplicationController applicationController) {
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(ShareMusicMessagePacket.class));
        shareMusicMessageListener = new ShareMusicMessageListener(applicationController);
        mConnection.addSyncStanzaListener(shareMusicMessageListener, filter);
    }

    /**
     * addGSMMessageListener
     *
     * @author HaiKE
     */
    private void addGSMMessageListener(ApplicationController app) {
        String filterString = AppConstants.XMPP.XMPP_SMS + "." + AppConstants.XMPP.XMPP_DOMAIN;
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(GSMMessagePacket.class), new FromContainsFilter(filterString));
        gsmMessageListener = new GSMMessageListener(app);
        mConnection.addSyncStanzaListener(gsmMessageListener, filter);
    }

    /**
     * add voicemail response
     *
     * @author HaiKE
     */
    private void addVoiceMailGSMResponseMessageListener(ApplicationController app) {
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(VoiceMailGSMMessagePacket.class));
        voicemailGSMResponseMessageListener = new VoicemailGSMResponseMessageListener(app);
        mConnection.addSyncStanzaListener(voicemailGSMResponseMessageListener, filter);
    }

    private void addMessageReceivedListener(ApplicationController applicationController) {
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(EventReceivedMessagePacket.class));
        recevedSoloMessageListener = new RecevedSoloMessageListener(
                applicationController);
        mConnection.addSyncStanzaListener(recevedSoloMessageListener, filter);

    }

    private void removeSASLListener() {
        if (successNonSASLListener != null) {
            mConnection.removeSyncStanzaListener(successNonSASLListener);
        }
    }

    public void removeConnectionListener() {
        if (mConnection != null) {
            if (xmppConnectionListener != null) {
                mConnection.removeConnectionListener(xmppConnectionListener);
            }
            removeSASLListener();
        }
        NetworkUtils.addNetworkConnectivityChangeListener(null);
    }

    public void removeAllListener() {
        AppLogger.i(TAG, "removeAllListener");
        if (mConnection != null) {
            if (presenceListener != null) {
                mConnection.removeSyncStanzaListener(presenceListener);
            }
            if (reengMessageListener != null) {
                mConnection.removeSyncStanzaListener(reengMessageListener);
            }
            if (recevedSoloMessageListener != null) {
                mConnection.removeSyncStanzaListener(recevedSoloMessageListener);
            }
            if (gsmMessageListener != null) {
                mConnection.removeSyncStanzaListener(gsmMessageListener);
            }
            if (voicemailGSMResponseMessageListener != null) {
                mConnection.removeSyncStanzaListener(voicemailGSMResponseMessageListener);
            }
        }
        // set null listener
        presenceListener = null;
        reengMessageListener = null;
        recevedSoloMessageListener = null;
        gsmMessageListener = null;
        voicemailGSMResponseMessageListener = null;
        xmppConnectionListener = null;
    }
}
