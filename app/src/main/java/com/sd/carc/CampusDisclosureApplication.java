package com.sd.carc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import constants.AppConstants;


public class CampusDisclosureApplication extends Application implements AppConstants {
    private static boolean activityVisible;
    static Context context;
    public static SharedPreferences getAppPref() {
        return appPref;
    }

    protected static SharedPreferences appPref;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appPref = getSharedPreferences(APP_PREF, MODE_PRIVATE);

    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static Context getContext() {
        return context;
    }
}

