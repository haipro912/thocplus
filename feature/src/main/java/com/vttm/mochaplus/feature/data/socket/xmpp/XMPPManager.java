package com.vttm.mochaplus.feature.data.socket.xmpp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

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
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.interfaces.XMPPConnectivityChangeListener;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;
import com.vttm.mochaplus.feature.utils.NetworkUtils;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.android.AndroidSmackInitializer;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import com.vttm.chatlib.sasl.NonSASLAuthInfo;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CopyOnWriteArrayList;

public class XMPPManager {
    private static final String TAG = XMPPManager.class.getSimpleName();
    private static final int PING_FAIL_THRESHOLD = 1; // 0 --> 1 --> 2
    private static CopyOnWriteArrayList<XMPPConnectivityChangeListener> mXmppConnectivityChangeListeners = new CopyOnWriteArrayList<>();
    private static int mConnectionState;
    private XMPPTCPConnectionConfiguration mConfig;
    private AbstractXMPPConnection mConnection;
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

    private SharedPreferences mPref;

    public XMPPManager(ApplicationController app) {
        mApplication = app;
        mPref = app.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
        configSmack();
//        mMessageRetryManager = MessageRetryManager.getInstance(app, this);
    }

    private void configSmack() {
        try {
            InetAddress addr = InetAddress.getByName(Config.Smack.DOMAIN_MSG);

            new AndroidSmackInitializer().initialize();
            SmackConfiguration.setDefaultReplyTimeout(Config.Smack.PACKET_REPLY_TIMEOUT);
            mConfig = XMPPTCPConnectionConfiguration.builder()
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setXmppDomain(JidCreate.domainBareFrom(Config.Smack.RESOURCE))
                    .setHostAddress(addr)
                    .setHost(Config.Smack.DOMAIN_MSG)
                    .setPort(Config.Smack.PORT_MSG)
                    .setResource(Config.Smack.RESOURCE)
                    .setClientType(Config.CLIENT_TYPE)
                    .setRevision(Config.REVISION)
                    .setCountryCode("VN")
                    .setSendPresence(false)
//                    .setSocketFactory(SSLSocketFactory.getDefault())
//                    .setUsernameAndPassword("user06", "user06")
                    .enableDefaultDebugger()
                    .build();
        }
        catch (UnknownHostException e) {

        }
        catch (XmppStringprepException e) {
            AppLogger.e(TAG, e);
        }
    }

    private void initConnectionAsAnonymous() throws InterruptedException, XMPPException, SmackException, IOException {
        AppLogger.i(TAG, "initConnectionAsAnonymous");
        if (mConnection == null) {
            AppLogger.i(TAG, "mConnection == null");
            if (mConfig == null) {
                AppLogger.i(TAG, "mConfig == null");
                configSmack();
            }
            mConnection  = new XMPPTCPConnection(mConfig);
        }
        if (!mConnection.isConnected()) {
            AppLogger.i(TAG, "!mConnection.isConnected()");
            mConnection.connect();
        }
    }

    public void destroyAnonymousConnection() {
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
    }

    public boolean isConnected() {
        if (mConnection == null)
            return false;
        return mConnection.isConnected();
    }

    public void connectByCode(ApplicationController mContext, String mPhoneNumber, String password, String countryCode) throws XMPPException, IllegalStateException, InterruptedException, IOException, SmackException {
        initConnectionAsAnonymous();
        // add success packet filter to get token
        addSASLListener(mContext);
        // login
        addAllListener(mContext);

        mConnection.login(mPhoneNumber, password, AbstractXMPPConnection.CODE_AUTH_NON_SASL);

        addConnectionListener();
//        sendPresenceAfterLogin(Connection.CODE_AUTH_NON_SASL, true);
//        // start ping
//        startPing();
//        // set roster
//        ApplicationController application = (ApplicationController) mContext.getApplicationContext();
//        application.loadDataAfterLogin();
//        DeviceHelper.checkToSendDeviceIdAfterLogin(application, mPhoneNumber);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public synchronized void connectByToken(ApplicationController ctx, String mPhoneNumber,
                                            String mToken, String countryCode) {
//        Log.f(TAG, "[VIETTEL] connect to server by token");
////        ctx.iDebugContent("[VIETTEL] connect to server by token");
//        final long time = System.currentTimeMillis();
//        mApplication = ctx;
//        if (mConfig == null) {
//            configSmack();
//        }
//        if (mConfig == null || mApplication == null || TextUtils.isEmpty(mPhoneNumber) || TextUtils.isEmpty(mToken)) {
//            return;
//        }
//        String messageException = "default";
////        String errorCode = Constants.iKQI.StateKQI.ERROR_EXCEPTION.getValue();
//        try {
//            mConfig.setUserName(mPhoneNumber);
//            mConfig.setToken(mToken);
//            mConfig.setRevision(Config.REVISION);
//            mConfig.setCountryCode(countryCode);
//        }
//        catch (NullPointerException ex)
//        {
//            return;
//        }
//
//        if (mConnection == null) {
//            mConnection = new XMPPConnection(mConfig, mApplication);
//        }
//        if (mConnection.isAuthenticated()) {
//            Log.f(TAG, "[VIETTEL] reconnectByToken but already authenticated --> return now");
////            ctx.iDebugContent("[VIETTEL] reconnectByToken but already authenticated --> return now");
//            notifyXMPPConnected();
//            return;
//        }
//        boolean loginSuccessful = false;
//        try {
//            // add success packet filter to get token
//            addSASLListener(mApplication);
//            // logint
//            addAllListener(mApplication);
//            mConnection.connect();
//            loginSuccessful = true;
//            Log.f(TAG, "login successful");
////            long deltaTime = (System.currentTimeMillis() - time);
////            ctx.iDebugContent("login successful take: " + deltaTime);
////            LogKQIHelper.getInstance(mApplication).saveLogKQI(Constants.iKQI.iIN_BY_TOKEN,
////                    String.valueOf(deltaTime), String.valueOf(time), Constants.iKQI.StateKQI.SUCCESS.getValue());
//            addConnectionListener();
//            sendPresenceAfterLogin(Connection.TOKEN_AUTH_NON_SASL, false);
//        } catch (IllegalStateException ie) {
//            Log.e(TAG, "Exception", ie);
//            messageException = "IllegalStateException";
//        } catch (XMPPException xe) {
//            Log.e(TAG, "Exception", xe);
////            XMPPError xmppError = xe.getXMPPError();
////            if (xmppError != null) {
////                int code = xmppError.getCode();
////                if (code == XMPPCode.E401_UNAUTHORIZED
////                        || code == XMPPCode.E409_RESOURCE_CONFLICT) {
////                    ReengAccountBusiness reengAccountBusiness = mApplication
////                            .getReengAccountBusiness();
////                    ReportHelper.reportLockAccount(mApplication,
////                            reengAccountBusiness.getJidNumber(),
////                            reengAccountBusiness.getToken(),
////                            ctx.getString(XMPPCode.getResourceOfCode(code)));
////                    reengAccountBusiness.lockAccount(mApplication, false);
////                    // Client is locked  (toanvk2)
////                    destroyXmpp();
////                    checkAppForegroundAndGotoLogin();
////                }
////                messageException = "XMPPException : " + code;
////                errorCode = String.valueOf(code);
////            } else {
////                messageException = "XMPPException xmppError == null";
////            }
//        } catch (Exception e) {
//            messageException = "Exception" + e.getMessage();
//            Log.e(TAG, "Exception", e);
//        } finally {
////            ContactBusiness contactBusiness = mApplication.getContactBusiness();
////            if (!loginSuccessful) {
////                destroyAnonymousConnection();
////                notifyXMPPDisconneted();
////                stopPing();
////                if (Constants.VIP.getListNumber().contains(mPhoneNumber)) {
////                    String cate = mApplication.getString(R.string.ga_category_vip) + mPhoneNumber.substring(6);
////                    mApplication.trackingEvent(cate, messageException, TimeHelper.formatTimeInDay(System
////                            .currentTimeMillis()));
////                }
////                Log.f(TAG, "loginFail: " + messageException);
////                ctx.iDebugContent("connectByToken loginFail: " + messageException);
////
////                long deltaTime = (System.currentTimeMillis() - time);
////                LogKQIHelper.getInstance(mApplication).saveLogKQI(Constants.iKQI.iIN_BY_TOKEN,
////                        String.valueOf(deltaTime), String.valueOf(time), errorCode);
////            } else {
////                notifyXMPPConnected();
////                startPing();
////                // login success get all contact, syncontact
////                contactBusiness.queryPhoneNumberInfo();
////                mApplication.getReengAccountBusiness().uploadDataIfNeeded();
////            }
//        }
    }






    /**
     * listener for connection
     */
    private void addConnectionListener() {
        if (isConnected()) {
            xmppConnectionListener = new XMPPConnectionListener(mApplication, mConnection, this);
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

    private void addSASLListener(ApplicationController mContext) {
        StanzaFilter saslFilter = new AndFilter(new StanzaTypeFilter(NonSASLAuthInfo.class));
        successNonSASLListener = new SuccessNonSASLListener(mContext);
        mConnection.addSyncStanzaListener(successNonSASLListener, saslFilter);
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

    public void manualDisconnect() {

    }

    public static synchronized void notifyXMPPDisconneted() {
        mConnectionState = AppConstants.CONNECTION_STATE.NOT_CONNECT;
        if (mXmppConnectivityChangeListeners == null)
            return;
        for (XMPPConnectivityChangeListener listener : mXmppConnectivityChangeListeners) {
            if (listener != null) {
                listener.onXMPPDisconnected();
            }
        }
    }

    public void setTokenForConfig(String token) {
        mConfig.setToken(token);
    }

    public static int getConnectionState() {
        return mConnectionState;
    }
}
