package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

import android.text.TextUtils;

import com.vttm.chatlib.utils.Log;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.model.ReengAccount;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;
import com.vttm.chatlib.sasl.NonSASLAuthInfo;

/**
 * listener get token
 *
 * @author thaodv
 */
public class SuccessNonSASLListener implements StanzaListener {
    private static final String TAG = SuccessNonSASLListener.class.getSimpleName();
    private ApplicationController mApplication;

    public SuccessNonSASLListener(ApplicationController mApplication) {
        this.mApplication = mApplication;
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
        NonSASLAuthInfo success = (NonSASLAuthInfo) packet;
        String token = success.getToken();
        XMPPManager mXMPPManager = mApplication.getXmppManager();
        if (!TextUtils.isEmpty(token)) {
            ReengAccount reengAccount = mApplication.getReengAccountBusiness().getCurrentAccount();
            reengAccount.setToken(token);
            reengAccount.setActive(true);
            mApplication.getReengAccountBusiness().updateReengAccount(reengAccount);
            mXMPPManager.setTokenForConfig(token);
        } else {
            Log.i(TAG, "received empty token");
        }
    }
}