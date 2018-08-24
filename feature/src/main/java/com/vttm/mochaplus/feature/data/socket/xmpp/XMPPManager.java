package com.vttm.mochaplus.feature.data.socket.xmpp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.vttm.chatlib.MochaXMPPException;
import com.vttm.chatlib.filter.FromContainsFilter;
import com.vttm.chatlib.packet.EventReceivedMessagePacket;
import com.vttm.chatlib.packet.GSMMessagePacket;
import com.vttm.chatlib.packet.IQInfo;
import com.vttm.chatlib.packet.ReengMessagePacket;
import com.vttm.chatlib.packet.ShareMusicMessagePacket;
import com.vttm.chatlib.packet.VoiceMailGSMMessagePacket;
import com.vttm.chatlib.sasl.NonSASLAuthInfo;
import com.vttm.chatlib.utils.Log;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.BuildConfig;
import com.vttm.mochaplus.feature.business.ContactBusiness;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.GSMMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.PresenceListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.RecevedSoloMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.ReengMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.ShareMusicMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.SuccessNonSASLListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.VoicemailGSMResponseMessageListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.XMPPConnectionListener;
import com.vttm.mochaplus.feature.data.socket.xmpp.listener.interfaces.XMPPConnectivityChangeListener;
import com.vttm.mochaplus.feature.helper.DeviceHelper;
import com.vttm.mochaplus.feature.helper.NetworkHelper;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.Config;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaCollector;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.android.AndroidSmackInitializer;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
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

    private static long lastTimeConnectedSucceed = 0;
    private boolean needSendIQUpdate;

    private SharedPreferences mPref;

    public enum IqInfoFirstTime {
        AFTER_INSTALL(0),
        AFTER_UPDATE(1),
        NORMAL(2);
        public int VALUE;

        IqInfoFirstTime(int VALUE) {
            this.VALUE = VALUE;
        }
    }

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
        connect();
    }

    private void connect() throws InterruptedException, XMPPException, SmackException, IOException {
        if (!mConnection.isConnected()) {
            AppLogger.i(TAG, "!mConnection.isConnected()");
            mConnection.connect();

            //Enable auto reconnection when connection is closed for some reason
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
            reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
            reconnectionManager.setFixedDelay(5);
            reconnectionManager.enableAutomaticReconnection();
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
//        mConnection.login("0948222060", "3072931738153512489568803", AbstractXMPPConnection.TOKEN_AUTH_NON_SASL);

        addConnectionListener();
//        sendPresenceAfterLogin(AbstractXMPPConnection.CODE_AUTH_NON_SASL, true);
        // start ping
        startPing();
        // set roster
//        ApplicationController application = (ApplicationController) mContext.getApplicationContext();
//        application.loadDataAfterLogin();
//        DeviceHelper.checkToSendDeviceIdAfterLogin(application, mPhoneNumber);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public synchronized void connectByToken(ApplicationController ctx, String mPhoneNumber, String mToken, String countryCode) {
        Log.f(TAG, "[VIETTEL] connect to server by token");
        mApplication = ctx;
        if (mConfig == null) {
            configSmack();
        }
        if (mConfig == null || mApplication == null || TextUtils.isEmpty(mPhoneNumber) || TextUtils.isEmpty(mToken)) {
            return;
        }
        String messageException = "default";
        try {
            mConfig.setUsername(mPhoneNumber);
            mConfig.setToken(mToken);
            mConfig.setRevision(Config.REVISION);
            mConfig.setCountryCode(countryCode);
        } catch (NullPointerException ex) {
            return;
        }

        if (mConnection == null) {
            mConnection  = new XMPPTCPConnection(mConfig);
        }
        if (mConnection.isAuthenticated()) {
            Log.f(TAG, "[VIETTEL] reconnectByToken but already authenticated --> return now");
            notifyXMPPConnected();
//            mApplication.getReengAccountBusiness().processChangeNumber();
            return;
        }
        // updateState(MessageConstants.CONNECTION_STATE.CONNECTING);
        boolean loginSuccessful = false;
        try {
            // add success packet filter to get token
            addSASLListener(mApplication);
            // logint
            addAllListener(mApplication);
            connect();
            loginSuccessful = true;
            Log.f(TAG, "login successful");
            addConnectionListener();
//            sendPresenceAfterLogin(AbstractXMPPConnection.TOKEN_AUTH_NON_SASL, false);
        } catch (IllegalStateException ie) {
            Log.e(TAG, "Exception", ie);
            messageException = "IllegalStateException";
        } catch (XMPPException xe) {
            Log.e(TAG, "Exception", xe);
//            XMPPError xmppError = xe.getXMPPError();
//            if (xmppError != null) {
//                int code = xmppError.getCode();
//                if (code == XMPPCode.E401_UNAUTHORIZED || code == XMPPCode.E409_RESOURCE_CONFLICT) {
//                    ReengAccountBusiness reengAccountBusiness = mApplication.getReengAccountBusiness();
//                    ReportHelper.reportLockAccount(mApplication,
//                            reengAccountBusiness.getJidNumber(),
//                            reengAccountBusiness.getToken(),
//                            ctx.getString(XMPPCode.getResourceOfCode(code)));
//                    reengAccountBusiness.lockAccount(mApplication, false);
//                    // Client is locked  (toanvk2)
//                    destroyXmpp();
//                    checkAppForegroundAndGotoLogin();
//                }
//                messageException = "XMPPException : " + code;
//            } else {
//                messageException = "XMPPException xmppError == null";
//            }
        } catch (Exception e) {
            messageException = "Exception" + e.getMessage();
            Log.e(TAG, "Exception", e);
        } finally {
            ContactBusiness contactBusiness = mApplication.getContactBusiness();
            if (!loginSuccessful) {
                destroyAnonymousConnection();
                notifyXMPPDisconneted();
                stopPing();
                Log.f(TAG, "loginFail: " + messageException);
            } else {
                notifyXMPPConnected();
//                mApplication.getReengAccountBusiness().processChangeNumber();
                startPing();
                // login success get all contact, syncontact
//                contactBusiness.queryPhoneNumberInfo();
//                mApplication.getReengAccountBusiness().uploadDataIfNeeded();
            }
        }
    }


    private void startPing() {

    }

    private void stopPing() {

    }

    /**
     * send available presence to server after login by code, pass, token
     */
    private void sendPresenceAfterLogin(String mechanism, boolean isLoginByCode) {
        Presence presenceOnline = new Presence(Presence.Type.available);
        presenceOnline.setState(null);
        presenceOnline.setSubType(Presence.SubType.available);
        // gui ban tin avaiable
        sendPresencePacket(presenceOnline);
        // gui ban tin client info
        IqInfoFirstTime iqInfoFirstTime;
        if (isLoginByCode) {
            iqInfoFirstTime = IqInfoFirstTime.AFTER_INSTALL;
        } else
            iqInfoFirstTime = IqInfoFirstTime.NORMAL;
        sendIQClientInfo(mechanism, false, iqInfoFirstTime.VALUE);
        // gui ban tin foreground neu reconect ma  o tren foreground
        checkAndSendPresenceForeground();
        // check and register regId,login bằng code có force update regId
//        FireBaseHelper.getInstance(mApplication).checkServiceAndRegister(isLoginByCode);
//        mApplication.getReengAccountBusiness().checkAndSendIqGetLocation();
    }

    private void checkAndSendPresenceForeground() {
        mApplication.getAppStateManager().checkStateAfterLoginSuccess();
        if (mApplication.getAppStateManager().isAppWentToFg()) {// gui
            // foreground khi reconect
            mApplication.getAppStateManager().setSendForeground(true);
            sendPresenceBackgroundOrForeground(false);
        } else {
            mApplication.getAppStateManager().setAppWentToBg(true);
            mApplication.getAppStateManager().setSendForeground(false);
        }// reconect ma ap dang o bg
    }

    private Thread mThreadClientInfo;

    /**
     * gui ban tin client info
     */
    public void sendIQClientInfo(String mechanism, boolean isForce, final int firstTime) {
        //check last revision send client info
        final int currentVersion = BuildConfig.VERSION_CODE;
        int oldVersion = mPref.getInt(AppConstants.PREFERENCE.PREF_CLIENT_INFO_CODE_VERSION, -1);
        final String currentLanguage = mApplication.getReengAccountBusiness().getDeviceLanguage();
        String oldLanguage = mPref.getString(AppConstants.PREFERENCE.PREF_CLIENT_INFO_DEVICE_LANGUAGE, currentLanguage);
        if (!isForce && mechanism.equals(AbstractXMPPConnection.TOKEN_AUTH_NON_SASL)) {// release && login token
            if ((currentVersion == oldVersion) && (currentLanguage.equals(oldLanguage))) {
                Log.i(TAG, "currentVersion == oldVersion = " + currentVersion +
                        " currentLanguage == oldLanguage = " + currentLanguage);
                return;
            }
        }
        if (mThreadClientInfo == null) {
            mThreadClientInfo = new Thread() {
                @Override
                public void run() {
                    Log.i(TAG, "update info --> send client info");
                    String versionName = BuildConfig.VERSION_NAME;
                    String device = Build.MANUFACTURER + "-" + Build.BRAND + "-" + Build.MODEL;
                    String osVer = Build.VERSION.RELEASE;
                    String deviceId = DeviceHelper.getDeviceId(mApplication);
                    IQInfo iqInfo = new IQInfo(IQInfo.NAME_SPACE_CLIENT_INFO);
                    iqInfo.setType(IQ.Type.set);
                    iqInfo.addElements("platform", "Android");
                    iqInfo.addElements("os_version", osVer);
                    iqInfo.addElements("device", device);
                    iqInfo.addElements("revision", Config.REVISION);
                    iqInfo.addElements("version", versionName);
                    if (needSendIQUpdate) {
                        iqInfo.addElements("firsttime", String.valueOf(XMPPManager.IqInfoFirstTime.AFTER_UPDATE.VALUE));
                        needSendIQUpdate = false;
                    } else
                        iqInfo.addElements("firsttime", String.valueOf(firstTime));
                    if (!TextUtils.isEmpty(deviceId)) {
                        iqInfo.addElements("device_id", deviceId);
                    }
                    //iqInfo.addElements("device_id", DeviceHelper.getDeviceId(mApplication));
                    iqInfo.addElements("language", currentLanguage);
                    try {
                        IQ result = sendPacketThenWaitingResponse(iqInfo, false);
                        if (result != null && result.getType() != null && result.getType() == IQ.Type.result) {
                            Log.i(TAG, "update info --> success");
                            mPref.edit().putInt(AppConstants.PREFERENCE.PREF_CLIENT_INFO_CODE_VERSION, currentVersion)
                                    .apply();
                            mPref.edit().putString(AppConstants.PREFERENCE.PREF_CLIENT_INFO_DEVICE_LANGUAGE,
                                    currentLanguage).apply();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                    } finally {
                        mThreadClientInfo = null;
                    }
                }
            };
            mThreadClientInfo.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mThreadClientInfo.start();
        }
    }

    public void sendPresenceBackgroundOrForeground(boolean background) {
        Presence presenceInfo = new Presence(Presence.Type.available);
        if (background) {
            presenceInfo.setSubType(Presence.SubType.background);
        } else {
            presenceInfo.setSubType(Presence.SubType.foreground);
        }
        sendPresencePacket(presenceInfo);
    }

    public void sendPresencePacket(Presence packet) {
//        packet.setStanzaId(PacketMessageId.getInstance().genPacketId(packet.getType().toString(), packet.getSubType().toString()));
        sendXmppPacket(packet);
    }

    public void sendXmppPacket(Stanza packet) {
        // neu chua xac thuc XMPP thi ko gui ban tin j ca
        // tranh viec khi dang reconnect lai gui cac ban tin xmpp --> login fail
        if (!isAuthenticated()) {
            Log.i(TAG, "sendXmppPacket !isAuthenticated() ???????????????????????");
            return;
        }
        try {
            mConnection.sendStanza(packet);
        } catch (NullPointerException e) {
            Log.e(TAG, "Exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }


    public IQ sendPacketThenWaitingResponse(Stanza packet, boolean isAnonymous)
            throws XMPPException, IllegalStateException, NullPointerException, InterruptedException, IOException, SmackException {
        if (isAnonymous) {
            initConnectionAsAnonymous();
        }
        if (mConnection == null) {
            throw new MochaXMPPException("no connection");
        }
        if (!isAnonymous && !mConnection.isAuthenticated()) {
            throw new MochaXMPPException("not authenticated");
        }
        StanzaIdFilter filter = new StanzaIdFilter(packet.getStanzaId());
        StanzaCollector response = mConnection.createStanzaCollector(filter);
        mConnection.sendStanza(packet);
        // mConnection.disconnect();
        return (IQ) response.nextResult(SmackConfiguration.getDefaultReplyTimeout());
    }

    /**
     * @param packet iqGroup
     * @return
     */
    public IQ sendPacketIQGroupThenWaitingResponse(Stanza packet) throws XMPPException, IllegalStateException, NullPointerException, SmackException.NotConnectedException, InterruptedException {
        if (mConnection == null) {
            throw new MochaXMPPException("no connection");
        }
        if (!mConnection.isAuthenticated()) {
            throw new MochaXMPPException("not authenticated");
        }
        StanzaIdFilter filter = new StanzaIdFilter(packet.getStanzaId());
        StanzaCollector response = mConnection.createStanzaCollector(filter);
        mConnection.sendStanza(packet);
        // mConnection.disconnect();
        return (IQ) response.nextResult(SmackConfiguration.getDefaultReplyTimeout());
    }

    public boolean isAuthenticated() {
        return (isConnected() && mConnection.isAuthenticated());
    }

    /**
     * listener for connection
     */
    private void addConnectionListener() {
        if (isConnected()) {
            xmppConnectionListener = new XMPPConnectionListener(mApplication, mConnection, this);
            mConnection.addConnectionListener(xmppConnectionListener);
            NetworkHelper.addNetworkConnectivityChangeListener(xmppConnectionListener);
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
        NetworkHelper.addNetworkConnectivityChangeListener(null);
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


    public static synchronized void notifyXMPPConnecting() {
        mConnectionState = AppConstants.CONNECTION_STATE.CONNECTING;
        if (mXmppConnectivityChangeListeners == null)
            return;
        for (XMPPConnectivityChangeListener listener : mXmppConnectivityChangeListeners) {
            Log.i(TAG, "notifyXMPPConnecting");
            if (listener != null) {
                listener.onXMPPConnecting();
            }
        }
    }

    private static synchronized void notifyXMPPConnected() {
        lastTimeConnectedSucceed = System.currentTimeMillis();
        mConnectionState = AppConstants.CONNECTION_STATE.CONNECTED;
        //PopupHelper.getInstance(mApplication).hideDialogProgressChangeDomain();
        if (mXmppConnectivityChangeListeners == null)
            return;
        Log.i(TAG, "notifyXMPPConnected");
        for (XMPPConnectivityChangeListener listener : mXmppConnectivityChangeListeners) {
            if (listener != null) {
                listener.onXMPPConnected();
            }
        }
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

    public void destroyXmpp() throws SmackException.NotConnectedException {
        if (mConnection != null) {
            mConnection.disconnect(new Presence(Presence.Type.unavailable));
            notifyXMPPDisconneted();
        }
        mConnection = null;
        mConfig = null;
    }

    public void setTokenForConfig(String token) {
        mConfig.setToken(token);
    }

    public void setNeedSendIQUpdate(boolean needSendIQUpdate) {
        this.needSendIQUpdate = needSendIQUpdate;
    }

    public static int getConnectionState() {
        return mConnectionState;
    }

    public static long getLastTimeConnectedSucceed() {
        return lastTimeConnectedSucceed;
    }
}
