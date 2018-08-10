package com.vttm.mochaplus.feature.data.socket.xmpp.listener.interfaces;

public interface XMPPConnectivityChangeListener {
    void onXMPPConnected();
    void onXMPPDisconnected();
    void onXMPPConnecting();
}
