package com.vttm.mochaplus.feature.data.socket.xmpp.listener;

public interface XMPPConnectivityChangeListener {
    void onXMPPConnected();
    void onXMPPDisconnected();
    void onXMPPConnecting();
}
