package com.vttm.mochaplus.feature.interfaces;

public interface NetworkConnectivityChangeListener {
    void onConnectivityChanged(boolean isNetworkAvailable, int connectedType);
}
