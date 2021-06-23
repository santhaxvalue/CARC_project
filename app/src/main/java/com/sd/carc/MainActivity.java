package com.sd.carc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Logger.LogLevel;
//import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import constants.AppConstants;
import firebase.Config;
import firebase.NotificationUtils;
import geofences.GeofenceTransitionsIntentService;
import services.BackgroundService;
import services.NetworkSchedulerService;

@SuppressLint("Registered")
public class MainActivity extends Activity implements AppConstants, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    @SuppressLint("StaticFieldLeak")
    public static IAWMain iawMain;

    @SuppressLint("StaticFieldLeak")
    protected static Context context;

    @SuppressLint("StaticFieldLeak")
    private static MainActivity mInstanceActivity;

    HomeBroadcast homeBroadCast;
    IntentFilter filter;
    //protected static Tracker crashTracker;
    public static SharedPreferences preferences;
    //static GpsCoordinates gpsCoordinates;
    protected GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //public static int MY_PERMISSION_LOCATION = 104;
    String userName;
    String applicationID;
    boolean isYoutubeActivityStarted,isPUMActivityStarted;

    // things to remove if not work
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    //private FirebaseAnalytics mFirebaseAnalytics;

//    //mixpanel
//    public static final String MIXPANEL_TOKEN = "32dbbec024a9c8bbd5ad0eed5e6d6ffe";
//    MixpanelAPI mixpanel;

//    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    double latitude;
    double longitude;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onetest:","MainActivity");
        mInstanceActivity = this;
        context = getApplicationContext();
        homeBroadCast = new HomeBroadcast();
        filter = new IntentFilter(); // Intent.CATEGORY_HOME
        preferences = getSharedPreferences("CDUCONN", MODE_PRIVATE);
//        mixpanel =
//                MixpanelAPI.getInstance(context, MIXPANEL_TOKEN);

//        if (crashTracker == null) {
//            System.out.println("CrashTracker is NULL");
//            crashTracker = ((CampusDisclosureApp) getApplication())
//                    .getTracker(CampusDisclosureApp.TrackerName.APP_TRACKER);
//        }

        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (iawMain == null) {
            iawMain = new IAWMain(context);
            iawMain.getESO();
            applicationID = iawMain.getAppID();
        }
        sendNotificationDataToServer();

        addPermissions();

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
//                Toast.makeText(context, "permissionRequest :"+permissionsToRequest.size(), Toast.LENGTH_SHORT).show();
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }

            //new code
//            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
//                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                //show popup for runtime permission
//                requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
//            }
            //new code

        }
    }

    //add required permissions
    public void addPermissions(){

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        //new code
        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        //new code
        permissions.add(android.Manifest.permission.CAMERA);
        permissions.add(android.Manifest.permission.CALL_PHONE);
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.BROADCAST_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
    }

    public static MainActivity getmInstanceActivity() {
        return mInstanceActivity;
    }

    public void sendNotificationDataToServer() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    String srvObjReference = intent.getStringExtra("srvObjReference");
                    if ((message != null && !message.isEmpty()) && (srvObjReference != null && !srvObjReference.isEmpty())) {
                        String esoAndMsgTxt = srvObjReference + "|" + message;
                        Sipdroid.loadURL("javascript:onNtfnOpen('" + esoAndMsgTxt + "')");
                        //Toast.makeText(getApplicationContext(), "Push notification: " + message + " | " + srvObjReference , Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
    }

    private void startTheApp() {
        Intent startIntent = new Intent(context, Sipdroid.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
        finish();
    }

    /******** newly added methods **********/
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        //old code

//        if (canMakeSmores()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
//            }
//        }
//
//        return true;

        //old code

        //new code

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

            if (canMakeSmores()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
                }
            }
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean statusone = (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
                if(!statusone){
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {


                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }else {
                    return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
                }
            }
        }

        }
        return true;

        //new code

    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("These permissions are mandatory for the application. Please allow access.")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public LocationTrack getGPSLocation() {
        locationTrack = new LocationTrack(MainActivity.this);

        if (locationTrack.canGetLocation()) {
            return locationTrack;
        } else {
            locationTrack.showSettingsAlert();
        }
        return locationTrack;
    }

    /******** newly added methods **********/

   /* public static GpsCoordinates getGpsLocation(Context context) {

        LocationManager mlocManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                location.getLatitude();
                location.getLongitude();
                gpsCoordinates = new GpsCoordinates(location.getLatitude(),
                        location.getLongitude());

            }
        };

        Location locationNetPvdr = mlocManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location locationGpsPvdr = mlocManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (locationNetPvdr == null && locationGpsPvdr != null
                || (locationNetPvdr != null && locationGpsPvdr != null)) {
            gpsCoordinates = new GpsCoordinates(locationGpsPvdr.getLatitude(),
                    locationGpsPvdr.getLongitude());

        } else if (locationGpsPvdr == null && locationNetPvdr != null) {
            gpsCoordinates = new GpsCoordinates(locationNetPvdr.getLatitude(),
                    locationNetPvdr.getLongitude());
        } else {
            gpsCoordinates = new GpsCoordinates(0, 0);
        }

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                mlocListener);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, mlocListener);
        return gpsCoordinates;
    }
    */

    protected void setCrashTracker(String screenName) {
        if (context == null)
            Log.e("LOG", "Context is NULL");

        Log.e("iAsist-LOG", screenName);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, bundle);

//        if (crashTracker == null)
//            crashTracker = ((CampusDisclosureApp) getApplication()) // getApplication()
//                    .getTracker(CampusDisclosureApp.TrackerName.APP_TRACKER);
//
//        crashTracker.enableExceptionReporting(true);
//        GoogleAnalytics.getInstance(this).getLogger()
//                .setLogLevel(LogLevel.VERBOSE);
//        crashTracker.setScreenName(screenName);
//        crashTracker.send(new HitBuilders.AppViewBuilder().build());
//        crashTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        crashTracker.send(new HitBuilders.ExceptionBuilder().build());
    }

    //adding geofence
    public void addGeofencesHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    public void populateGeofenceList(String geofenceID, String latitude, String longitude, String radius) {
        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(geofenceID)
                .setCircularRegion(
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude),
                        Float.parseFloat(radius)
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.i("GEOFENCE CONNECTED", "Connected to GoogleApiClient");
                        addGeofencesHandler();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                }).build();
        mGoogleApiClient.connect();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void printText(String text) {
        System.out.println(text);
    }

    @SuppressLint("ApplySharedPref")
    public static void setNewWebviewActivityStatus(boolean flag) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("PUMACTIVITY", flag);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    public void setYoutubeActivityStatus(boolean flag) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("YOUTUBEACTIVITY", flag);
        editor.commit();
    }

    public void checkSpinner(ProgressDialog dialog) {
        if (dialog != null)
            if (dialog.isShowing())
                dialog.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            System.out.println("onResume in MainActivity");
            CampusDisclosureApplication.activityResumed();
            registerReceiver(homeBroadCast, filter);
            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            NotificationUtils.clearNotifications(getApplicationContext());

            isYoutubeActivityStarted = preferences.getBoolean("YOUTUBEACTIVITY", false);
            isPUMActivityStarted = preferences.getBoolean("PUMACTIVITY", false);

            if (!isYoutubeActivityStarted && !isPUMActivityStarted) {
                preferences = getApplicationContext().getSharedPreferences(
                        "CDUCONN", MODE_PRIVATE);

                Sipdroid.loadURL("javascript:setAppStatus('" + true + "')");
                userName = preferences.getString("USER_DETAILS", "");
                String URL_FOR_FOREGROUND = "'" + POST_ACTIVITY_EVENT_URL + "'msg=4b2d2d772f544b8c9ba76921a6b21352|user='" + userName + "'|appID='" + applicationID + "'|appStatus='" + AppStatus.appEntersForeground + "'|epDelay='" + 0 + "'";
                URL_FOR_FOREGROUND = URL_FOR_FOREGROUND.replaceAll("\'", "");

                BackgroundService downloader = new BackgroundService(URL_FOR_FOREGROUND);
                downloader.execute();
//                JSONObject props = new JSONObject();
//                props.put("msg", "4b2d2d772f544b8c9ba76921a6b21352");
//                props.put("AppForeground", AppStatus.appEntersForeground);
//                props.put("appID", applicationID);
//                props.put("user", userName);
//                props.put("epDelay", 0);
//                mixpanel.track("AppStatus", props);
            }
            setNewWebviewActivityStatus(false);
            setYoutubeActivityStatus(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        System.out.println("Home Button Pressed");
        super.onUserLeaveHint();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        iawMain.setAppStatus(true);
    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            System.out.println("onPause in MainActivity");
            CampusDisclosureApplication.activityPaused();
            unregisterReceiver(homeBroadCast);
            iawMain.setAppStatus(false);
            isYoutubeActivityStarted = preferences.getBoolean("YOUTUBEACTIVITY", false);
            isPUMActivityStarted = preferences.getBoolean("PUMACTIVITY", false);

            if (!isYoutubeActivityStarted && !isPUMActivityStarted) {

                preferences = getApplicationContext().getSharedPreferences(
                        "CDUCONN", MODE_PRIVATE);
                Sipdroid.loadURL("javascript:setAppStatus('" + false + "')");
                userName = preferences.getString("USER_DETAILS", "");
                String URL_FOR_BACKGROUND = "'" + POST_ACTIVITY_EVENT_URL + "'msg=4b2d2d772f544b8c9ba76921a6b21352|user='" + userName + "'|appID='" + applicationID + "'|appStatus='" + AppStatus.appEntersBackground + "'|epDelay='" + 0 + "'";
                URL_FOR_BACKGROUND = URL_FOR_BACKGROUND.replaceAll("\'", "");

                BackgroundService downloader = new BackgroundService(URL_FOR_BACKGROUND);
                downloader.execute();
//                JSONObject props = new JSONObject();
//                props.put("AppBackground", "AppInBackground");
//                props.put("msg", "4b2d2d772f544b8c9ba76921a6b21352");
//                props.put("AppBackground", AppStatus.appEntersBackground);
//                props.put("appID", applicationID);
//                props.put("user", userName);
//                props.put("epDelay", 0);
//                mixpanel.track("AppStatus", props);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onstart in FormActivity");
//        GoogleAnalytics.getInstance(MainActivity.this)
//                .reportActivityStart(this);
        Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
        startService(startServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onstop in FormActivity");
        //GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
        if (mGoogleApiClient != null && (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())) {
            mGoogleApiClient.disconnect();
        }
        stopService(new Intent(this, NetworkSchedulerService.class));
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:

//                Toast.makeText(context, "permissionRequest : --1", Toast.LENGTH_SHORT).show();

                for (Object perms : permissionsToRequest) {
//                    Toast.makeText(context, "permissionRequest : --2", Toast.LENGTH_SHORT).show();

                    if (!hasPermission(perms)) {
//                        Toast.makeText(context, "permissionRequest : --3", Toast.LENGTH_SHORT).show();

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
//                    Toast.makeText(context, "permissionRequest : --4", Toast.LENGTH_SHORT).show();



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        Toast.makeText(context, "permissionRequest : --5", Toast.LENGTH_SHORT).show();

                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
//                            Toast.makeText(context, "permissionRequest : --6", Toast.LENGTH_SHORT).show();

                            showMessageOKCancel(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(context, "permissionRequest : --7", Toast.LENGTH_SHORT).show();

                                            requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;


                //new code
//            case WRITE_EXTERNAL_STORAGE_CODE:
//
//                //if request is cancelled, the result arrays are empty
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    //permission was denied, show toast
//                    Toast.makeText(this, "Storage permission is required to store data", Toast.LENGTH_SHORT).show();
//                }
//
//
//            break;
            //new code

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationTrack != null) {
            locationTrack.stopListener();
        }
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if (location != null) {
////            location.setSpeed(0.0f);
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
////            setCurLatitude(latitude);
////            setCurLongitude(longitude);
////            setCurLocTime(location.getTime());
//
////            if (isFirstTime()){
////                lastLat = lastSpanLat = latitude;
////                lastLon = lastSpanLon = longitude;
////                setFirstTime(false);
////            }
////
////            lastlocation.setLatitude(lastLat);
////            lastlocation.setLongitude(lastLon);
////            double distance = lastlocation.distanceTo(location);
////            if (location.getAccuracy() < distance){
////                lastLat = latitude;
////                lastLon = longitude;
////            }
////
////            double speed = distance/(location.getTime() - lastlocation.getTime());
////            System.out.println(location.getTime() - lastlocation.getTime());
////
////            //if there is speed from location
////
////            lastlocation.setTime(location.getTime());
////            setCurSpeed(speed);
////
////            curCount ++;
////
////            if(curCount == (int) (MIN_TIME_BW_SPAN_SPEED / MIN_TIME_BW_UPDATES)){
////                lastSpanlocation.setLatitude(lastSpanLat);
////                lastSpanlocation.setLongitude(lastSpanLon);
////                distance = lastSpanlocation.distanceTo(location);
////                if (location.getAccuracy() < distance){
////                    lastSpanLat = latitude;
////                    lastSpanLon = longitude;
////                }
////                speed = distance/(location.getTime() - lastSpanlocation.getTime());
////
////                setPrevSpanSpeed(speed);
////                lastSpanlocation.setTime(location.getTime());
////
////                curCount = 0;
////            }
//
//            Sipdroid.loadURL("javascript:onLocationUpdate2:('" + latitude + "'" + "," + "'" + longitude + "'" + "," + "'" + location.getTime() + "')");
//
//        }
//    }

    enum AppStatus {
        appStarted, appEntersForeground, appEntersBackground, appClosed
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                boolean shouldProvideRationale =
//                        ActivityCompat.shouldShowRequestPermissionRationale(this,
//                                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//
//                if(shouldProvideRationale) {

                    Intent mIntent = new Intent(MainActivity.this, SelectLocPermission.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
//                }

            }

//        }catch (NullPointerException e){
//
//        }

//        try {
//            boolean backgroundone = ActivityCompat.checkSelfPermission(MainActivity.this,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
//            Toast.makeText(mInstanceActivity, "backgournd1: " +backgroundone, Toast.LENGTH_SHORT).show();
//        }catch (NullPointerException e){
//            Toast.makeText(mInstanceActivity, "backgournd1: Null Exception", Toast.LENGTH_SHORT).show();
//        }
    }
}
