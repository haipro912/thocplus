package com.vttm.mochaplus.feature.event;

public class NetworkEvent {
    boolean isOnline;

    public NetworkEvent(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
