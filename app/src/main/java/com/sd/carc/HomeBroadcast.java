package com.sd.carc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by sureshkumar on 1/2/2017.
 */
public class HomeBroadcast extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public HomeBroadcast() {
        super();
    }

    public HomeBroadcast(ConnectivityReceiverListener listener){
        connectivityReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks= new Network[0];
            networks = cm.getAllNetworks();
            if (cm != null) {
                for (Network netinfo : networks) {
                    NetworkInfo ni = cm.getNetworkInfo(netinfo);
                    if (ni.isConnected() && ni.isAvailable()) {
                        isConnected = true;
                    }
                }
            }
        } else {
            NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
            if(activeNetwork != null) {
                isConnected = (activeNetwork.isConnected() && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));
            }
        }

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }

//        if (intent.getAction().equals(
//                "android.provider.Telephony.SMS_RECEIVED")) {
//            Bundle bundle = intent.getExtras(); // ---get the SMS message
//            // passed in---
//            SmsMessage[] msgs = null;
//            // String msg_from;
//            if (bundle != null) {
//                // ---retrieve the SMS message received---
//                try {
//                    Object[] pdus = (Object[]) bundle.get("pdus");
//                    msgs = new SmsMessage[pdus.length];
//                    for (int i = 0; i < msgs.length; i++) {
//                        msgs[i] = SmsMessage
//                                .createFromPdu((byte[]) pdus[i]);
//                        // msg_from = msgs[i].getOriginatingAddress();
//                        String msgBody = msgs[i].getMessageBody();
//                        // do your stuff
//                    }
//                } catch (Exception e) {
//                    // Log.d("Exception caught",e.getMessage());
//                }
//            }
//        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) CampusDisclosureApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
