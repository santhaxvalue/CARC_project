package com.sd.carc;

import android.app.Application;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by sureshkumar on 12/31/2016.
 */
public class CampusDisclosureApp extends Application {
    private static final String PROPERTY_ID = "UA-72203714-1";
    public static int GENERAL_TRACKER = 0;
    private static CampusDisclosureApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized CampusDisclosureApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(HomeBroadcast.ConnectivityReceiverListener listener) {
        HomeBroadcast.connectivityReceiverListener = listener;
    }

    public enum TrackerName {
        APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
    }

    public HashMap mTrackers = new HashMap();

    public CampusDisclosureApp() {
        super();
    }

//    public synchronized Tracker getTracker(TrackerName appTracker) {
//        if (mTrackers == null)
//            mTrackers = new HashMap();
//        if (!mTrackers.containsKey(appTracker)) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics
//                    .newTracker(PROPERTY_ID)
//                    : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics
//                    .newTracker(R.xml.global_tracker) : analytics
//                    .newTracker(R.xml.ecommerce_tracker);
//            mTrackers.put(appTracker, t);
//        }
//        return (Tracker) mTrackers.get(appTracker);
//    }
}

