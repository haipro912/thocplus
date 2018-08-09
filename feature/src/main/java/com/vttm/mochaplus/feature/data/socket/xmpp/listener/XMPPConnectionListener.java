package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.business.ReengAccountBusiness;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.interfaces.NetworkConnectivityChangeListener;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.NetworkUtils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;

/**
 * Created by TSB on 7/27/2014.
 */
public class XMPPConnectionListener implements ConnectionListener, NetworkConnectivityChangeListener {
    private static final String TAG = XMPPConnectionListener.class.getSimpleName();
    private ApplicationController mContext;
    private XMPPConnection mXMPPConnection;
    private XMPPManager mXmppManager;
    private int connectedType;

    public XMPPConnectionListener(ApplicationController ctx, XMPPConnection conn, XMPPManager manager) {
        mContext = ctx;
        mXMPPConnection = conn;
        this.mXmppManager = manager;
        connectedType = NetworkUtils.checkTypeConnection(ctx);
        AppLogger.i(TAG, "connectedType = " + connectedType);
    }


    @Override
    public void connected(XMPPConnection connection) {

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {
        AppLogger.i(TAG, "connectionClosed"); //server ngat
//        mContext.logDebugContent("received </stream> --> close connection");
        manualDisconnect();
        if (NetworkUtils.isConnected(mContext)) {
            //  login lai thoi gian ke tu lan reconnect thanh cong lan cuoi
            this.loginByToken();
        }
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        AppLogger.e(TAG, "connectionClosedOnError", e); //loi mang
        if (e instanceof XMPPException) {
            XMPPException xmppEx = (XMPPException) e;
            StreamError error = xmppEx.getStreamError();
            // Make sure the error is not null
            if (error != null) {
                String reason = error.getCode();
                if ("conflict".equals(reason)) {
//                    co may khac dang nhap vao tai khoan nay
                    ReengAccountBusiness reengAccountBusiness = mContext.getReengAccountBusiness();
                    ReportHelper.reportLockAccount(mContext, reengAccountBusiness.getJidNumber(),
                            reengAccountBusiness.getToken(), "conflict account");
                    mContext.removeCountNotificationIcon();
                    manualDisconnect();
                    return;
                }
            }
        }
        manualDisconnect();
        //thu nghiem ko reconnect luc nay nua
        if (NetworkUtils.isConnected(mContext)) {
            this.loginByToken();
        }
    }

    private void loginByToken() {
        if (IMService.isReady()) {
            IMService.getInstance().connectByToken();
        } else {
            AppLogger.i(TAG, "IMService not ready -> start service");
//            mContext.startService(new Intent(mContext.getApplicationContext(), IMService.class));

            ApplicationController applicationController = (ApplicationController) mContext.getApplicationContext();
            applicationController.startIMService();
        }
    }

    @Override
    public void onConnectivityChanged(boolean isNetworkAvailable, int newConnectedType) {
//        AppLogger.f(TAG, "[VIETTEL] Network Connection has been changed: " + isNetworkAvailable);
//        mContext.logDebugContent("[VIETTEL] Network Connection has been changed: " + isNetworkAvailable);
        if (isNetworkAvailable) {
            if (mXMPPConnection != null && (!mXMPPConnection.isConnected() || !mXMPPConnection.isAuthenticated())) {
                AppLogger.i(TAG, "[VIETTEL] Call connectByToken when network is changed to available status");
                // Wait 150 ms for processes to clean-up, then shutdown.
                try {
                    Thread.sleep(150);
                } catch (Exception e) {
                    AppLogger.e(TAG, "Exception", e);
                }
                this.loginByToken();
            } else {
                //truong hop da login roi, chuyen doi giua wifi <--> 3G, ma isNetworkAvailable ko tra ve false
                AppLogger.i(TAG, "preConnectedType = " + connectedType + " newConnectedType = " + newConnectedType);
                if (connectedType != newConnectedType && (
                        (connectedType == NetworkUtils.TYPE_WIFI && newConnectedType == NetworkUtils.TYPE_MOBILE) ||
                                (connectedType == NetworkUtils.TYPE_MOBILE && newConnectedType == NetworkUtils
                                        .TYPE_WIFI))) {
                    manualDisconnect();
                }
            }
        } else {
            //manual disconnect
            manualDisconnect();
        }
        connectedType = newConnectedType;
    }

    private void manualDisconnect() {
        mXmppManager.manualDisconnect();
    }
}