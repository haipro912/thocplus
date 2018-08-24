package com.vttm.mochaplus.feature.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vttm.mochaplus.feature.helper.NetworkHelper;

/**
 * Created by HaiKE on 10/12/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkHelper.getInstance().onNetworkConnectivityChanged(context);
    }

//    protected Set<NetworkStateReceiverListener> listeners;
//    protected Boolean connected;
//
//    public NetworkChangeReceiver() {
//        listeners = new HashSet<NetworkStateReceiverListener>();
//        connected = null;
//    }


//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if(intent == null || intent.getExtras() == null)
//            return;
//
//        NetworkHelper.getInstance().onNetworkConnectivityChanged(context);

//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo ni = manager.getActiveNetworkInfo();
//
//        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
//            connected = true;
//        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
//            connected = false;
//        }
//
//        notifyStateToAll();
//    }

//    private void notifyStateToAll() {
//        for(NetworkStateReceiverListener listener : listeners)
//            notifyState(listener);
//    }
//
//    private void notifyState(NetworkStateReceiverListener listener) {
//        if(connected == null || listener == null)
//            return;
//
//        if(connected == true)
//            listener.networkAvailable();
//        else
//            listener.networkUnavailable();
//    }
//
//    public void addListener(NetworkStateReceiverListener l) {
//        listeners.add(l);
//        notifyState(l);
//    }
//
//    public void removeListener(NetworkStateReceiverListener l) {
//        listeners.remove(l);
//    }
//
//    public interface NetworkStateReceiverListener {
//        public void networkAvailable();
//        public void networkUnavailable();
//    }
}