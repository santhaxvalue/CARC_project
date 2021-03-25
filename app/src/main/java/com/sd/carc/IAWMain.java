package com.sd.carc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.util.Log;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import constants.AppConstants;

public class IAWMain {

    public static String userID;
    String deviceID = "", srvObjectReference = "", regString = "", appID = "",
            baseURL = "", sessionID = "", latitude = "", longitude = "", appName = "",locationTime="";
    Context context;
    static CookieStore cookieStore = new BasicCookieStore();
    String pingResponse = "";
    int appState;
    GpsCoordinates gpsCoordinates;
    boolean appStatus;
    private LocationTrack locationTrack;


    @SuppressLint("HardwareIds")
    public IAWMain(Context context) {
        this.context = context;
        deviceID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo != null ? pInfo.versionName : null;
        appName = context.getResources().getString(R.string.app_name);
        appID = AppConstants.APP_ID + version;
        System.out.println("Version: " + version + " App ID: " + appID);
        locationTrack = MainActivity.getmInstanceActivity().getGPSLocation();
//        latitude = getLatitudeValue();
//        longitude = getLongitudeValue();
        appState = -1;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getESO() {
        return srvObjectReference;
    }

    public void setESO(String srvObjectReference) {
        this.srvObjectReference = srvObjectReference;
        Log.e("srvObjectReference", "srvObjectReference in IAWMain: "
                + srvObjectReference);
    }

    public String getRegString() {
        return regString;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userId) {
        userID = userId;
    }

    public void setRegString(String regString) {
        Log.e("Log", "GOT regString in setRegString: " + regString);
        this.regString = regString;
    }

    public String getAppID() {
        return appID;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLoctionTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public String getLocTime() {
        return locationTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String getLongitudeValue() {
        return String.valueOf(locationTrack.getLongitude());
    }

    private String getLatitudeValue() {

        return String.valueOf(locationTrack.getLatitude());
    }

    public String getCurLatitude() {

        return String.valueOf(locationTrack.getCurLatitude());
    }

    public String getCurLongitude() {

        return String.valueOf(locationTrack.getCurLongitude());
    }

    public String getCurLocTime() {

        return String.valueOf(locationTrack.getCurLocTime());
    }

    public String getLocationTime() {
        return String.valueOf(locationTrack.getLocTime());
    }
    public String getPrevSpanSpeed() {
        return String.valueOf(locationTrack.getPrevSpanSpeed());
    }

    public String getCurrentSpeed() {
        return String.valueOf(locationTrack.getCurSpeed());
    }

    public LocationTrack getLocationTrack(){
        return locationTrack;
    }

    public Intent doCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        return callIntent;
    }

    public int getAppState() {
        return appState;
    }

    public void setAppState(int appState) {
        this.appState = appState;
    }

    public boolean getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(boolean appStatus) {
        this.appStatus = appStatus;
    }
}

