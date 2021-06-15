package com.sd.carc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.TimeZone;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import firebase.NotificationUtils;
import services.LocationUpdatesIntentService;
import services.receivers.LocationUpdatesBroadcastReceiver;

@SuppressLint("Registered")
public class LocationTrack extends Service implements LocationListener {

    private final Context mContext;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;



    boolean checkGPS = false;


    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;
    double currentLatitude = 0;
    double currentLongitude = 0;
    double curLatitude;
    double curLongitude;
    double curLocTime;
    long locTime;
    double lastLon = 0;
    double lastLat = 0;
    long lastTime = 0;
    Location lastlocation = new Location("last");
    private double curSpeed = 0;


    int curCount = 0;
    int locRequests = 0;
    double lastSpanLon = 0;
    double lastSpanLat = 0;
    long lastSpanTime = 0;
    Location lastSpanlocation = new Location("lastSpan");
    private double prevSpanSpeed = 0;

    private double maxSpeed;
    private long timeStopped;

//    public SharedPreferences preferences = getSharedPreferences("CDUCONN", MODE_PRIVATE);


    private boolean isFirstTime;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    private static final String FILE_NAME = "carc_log.txt";

    ArrayList<String> list = new ArrayList<>();


    private long MIN_TIME_BW_UPDATES = 1000 * 1;
    private long MIN_TIME_BW_SPAN_SPEED = 1000 * 120;

    protected LocationManager locationManager;
    LocationCallback locationCallback = null;
    String callbackMethod = null;

    public LocationTrack(final Context mContext) {
        this.mContext = mContext;
        getLocation();
        getCurrentLocation(null);
        curSpeed = 0;
        maxSpeed = 0;
        timeStopped = 0;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest = new LocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Location loc = locationResult.getLastLocation();
                System.out.println("Latitude" + loc.getLatitude() + "Longitude" + loc.getLatitude() + "Time" + loc.getTime());
                Log.d("locationDetails:","locationDetails:"+"Latitude:" + loc.getLatitude() + "Longitude:" + loc.getLatitude() + "Time:" + loc.getTime());
                String lat = String.valueOf(loc.getLatitude());
                String longi = String.valueOf(loc.getLongitude());
                String locatTime = loc.getTime() + "";

                String appstate = "";
                if (!NotificationUtils.isAppIsInBackground(mContext)){
                    appstate = "Foreground";
                }else {
                    appstate = "Background";
                }

                Log.d("appstatus1:","appstatus1:"+appstate);

                Calendar cal = Calendar.getInstance();
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm a");
                String localTime = date.format(currentLocalTime);

                Log.d("appstatus11:","appstatus11:"+localTime);

                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                String dateone = dateFormat.format(cal1.getTime());

                Log.d("appstatus111:","appstatus111:"+dateone);


                Log.d("locationDetailsNew:","locationDetailsNew:"+ "Date/Time: "+ dateone + " AppState: "+appstate+" Latitude: "+lat+" Longitude: "+longi);


                //old code working fine in lower device
                // Check if we're running on Android 5.0 or higher
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                    writeToFile(dateone, appstate, lat, longi);
//                } else {
//                    generateNoteOnSD(LocationTrack.this,"carclogfile",lat,longi,dateone,appstate);
//                }
                //old code working fine in lower device

                //new code
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//                    boolean status = checkWriteExternalPermission();
//
//                    Toast.makeText(mContext, "Status : Status : Status ", Toast.LENGTH_SHORT).show();
//
                        saveToTextFile(lat,longi,dateone,appstate);
//
//
                }else {
                    generateNoteOnSD(LocationTrack.this,"carclogfile",lat,longi,dateone,appstate);
                }
                //new code


                MainActivity.iawMain.setLatitude(lat);
                MainActivity.iawMain.setLongitude(longi);
                if (callbackMethod != null) {
                    Sipdroid.loadURL("javascript:" + callbackMethod + "('" + lat + "'" + "," + "'" + longi + "'" + "," + "'" + locatTime + "')");
                }
            }




            public void generateNoteOnSD(Context context, String sFileName, String latitude, String longitude, String date, String appstatus) {
                try {
                    File root = new File(Environment.getExternalStorageDirectory(), "Carc-App");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, sFileName);
                    FileWriter writer = new FileWriter(gpxfile,true);
                    writer.append("Date/Time: "+ date + " AppState: "+appstatus+" Latitude: "+latitude+" Longitude: "+longitude).append("\n");
                    writer.flush();
                    writer.close();
//                    list.add(sBody);
//                    for(int i=0;i<list.size();i++) {
//                        writer.append(list.get(i)).append("\n");
//                    }
//                    writer.flush();
//                    writer.close();
//                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void saveToTextFile(String latitude, String longitude, String date, String appstatus) {

                //get current time for file name
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

                try{


                    //path tp storage
//            File path = Environment.getExternalStorageDirectory();

                    String path = mContext.getExternalFilesDir(null).getAbsolutePath();


                    //create folder named "My Files"
                    File dir = new File(path + "/Carc-App/");
                    dir.mkdirs();
                    //file name
                    String filename = "MyFile_" + timestamp + ".txt"; // e.g MyFile_20210615_23445.txt

                    File file = new File(dir, filename);

                    //FileWriter class is used to store characters in file
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("Date/Time: "+ date + " AppState: "+appstatus+" Latitude: "+latitude+" Longitude: "+longitude);
                    bw.close();

                    //show file name and path where file is saved
//                    Toast.makeText(mContext, filename+" is saved to\n" +dir, Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    //if anything goes wrong
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        };

    }

    private boolean checkWriteExternalPermission()
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = mContext.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void writeToFile(String dateonestr, String appstatestr, String latstr, String longistr) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("latlng_report.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("Date/Time: "+ dateonestr + " AppState: "+appstatestr+" Latitude: "+latstr+" Longitude: "+longistr);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void startTracking(int accuracy, String callbackMethod) {
        if (accuracy == 0) {
            accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY;
        }
        setLocationRequest(accuracy);
        locRequests += 1;
        this.callbackMethod = callbackMethod;
        requestLocationUpdates();
    }

    public void startTracking(long updateInterval, int accuracy, String callbackMethod) {
        if (accuracy == 0) {
            accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY;
        }
        setLocationRequest(updateInterval, updateInterval, accuracy);
        locRequests += 1;
        this.callbackMethod = callbackMethod;
        requestLocationUpdates();
    }

    public void stopTracking() {
        locRequests -= 1;
        if (locRequests <= 0) {
            removeLocationUpdates();
        }
    }

    private void setLocationRequest(long updateInterval, long fastestUpdateInterval, int priority) {

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
        mLocationRequest.setInterval(updateInterval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(fastestUpdateInterval);

        mLocationRequest.setPriority(priority);


    }

    private void setLocationRequest(int priority) {

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
//        mLocationRequest.setInterval(updateInterval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
//        mLocationRequest.setFastestInterval(fastestUpdateInterval);

        mLocationRequest.setPriority(priority);


    }

    private PendingIntent getPendingIntent() {
        // Note: for apps targeting API level 25 ("Nougat") or lower, either
        // PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
        // location updates. For apps targeting API level O, only
        // PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
        // started in the background in "O".

        // TODO(developer): uncomment to use PendingIntent.getService().
//        if (android.os.Build.VERSION.SDK_INT <= 25){}
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT <= 25) {
            Intent intent = new Intent(this, LocationUpdatesIntentService.class);
            intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
            return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            try {
                Intent intent = new Intent(LocationTrack.this, LocationUpdatesBroadcastReceiver.class);
                intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
                return PendingIntent.getBroadcast(LocationTrack.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            } catch (SecurityException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Requests start of location updates.
     */
    public void requestLocationUpdates() {
        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());

            //old code
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);

            //new code

            if (android.os.Build.VERSION.SDK_INT <= 25) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
            }else {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests removal of location updates.
     */
    public void removeLocationUpdates() {
//        mFusedLocationClient.removeLocationUpdates(getPendingIntent());

        //old code
//        mFusedLocationClient.removeLocationUpdates(locationCallback);

        if (android.os.Build.VERSION.SDK_INT <= 25) {
            mFusedLocationClient.removeLocationUpdates(getPendingIntent());
        }else {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }



    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//            if(preferences.getLong("updateInterval",0) != 0){
//                MIN_TIME_BW_UPDATES = preferences.getLong("updateInterval",0);
//            }

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    if (locationManager != null) {
//                        loc = locationManager
//                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        if (loc != null) {
//                            latitude = loc.getLatitude();
//                            longitude = loc.getLongitude();
//                        }
//                    }


                }


                if (checkNetwork) {


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                    if (locationManager != null) {
//                        loc = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                    }
//
//                    if (loc != null) {
//                        latitude = loc.getLatitude();
//                        longitude = loc.getLongitude();
//                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    public void getCurrentLocation(final String returnMethod) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        // Get the latest position from device
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    MainActivity.iawMain.setLatitude(String.valueOf(currentLatitude));
                    MainActivity.iawMain.setLongitude(String.valueOf(currentLongitude));
                    if (returnMethod != null) {
                        Sipdroid.loadURL("javascript:" + returnMethod + "('" + currentLatitude + "'" + "," + "'" + currentLongitude + "')");
                    }
                }
            }
        });


    }

    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public long getMinTimeBwUpdates() {
        return MIN_TIME_BW_UPDATES;
    }

    public void setMinTimeBwUpdates(long frequency) {
        MIN_TIME_BW_UPDATES = frequency;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public Long getLocTime() {
        if (loc != null) {
            locTime = loc.getTime();
        }
        return locTime;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void stopListener() {
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(LocationTrack.this);
        }
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
        if (curSpeed > maxSpeed) {
            maxSpeed = curSpeed;
        }
    }

    public void setPrevSpanSpeed(double speed) {
        this.prevSpanSpeed = speed;

    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public double getPrevSpanSpeed() {
        return prevSpanSpeed;
    }

    public void setTimeStopped(long timeStopped) {
        this.timeStopped += timeStopped;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setCurLatitude(double lat) {
        this.curLatitude = lat;
    }

    public void setCurLongitude(double longi) {
        this.curLongitude = longi;
    }

    public void setCurLocTime(double locTime) {
        this.curLocTime = locTime;
    }

    public double getCurLatitude() {
        return curLatitude;
    }

    public double getCurLongitude() {
        return curLongitude;
    }

    public double getCurLocTime() {
        return curLocTime;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            location.setSpeed(0.0f);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            setCurLatitude(latitude);
            setCurLongitude(longitude);
            setCurLocTime(location.getTime());

//            if (isFirstTime()){
//                lastLat = lastSpanLat = latitude;
//                lastLon = lastSpanLon = longitude;
//                setFirstTime(false);
//            }
//
//            lastlocation.setLatitude(lastLat);
//            lastlocation.setLongitude(lastLon);
//            double distance = lastlocation.distanceTo(location);
//            if (location.getAccuracy() < distance){
//                lastLat = latitude;
//                lastLon = longitude;
//            }
//
//            double speed = distance/(location.getTime() - lastlocation.getTime());
//            System.out.println(location.getTime() - lastlocation.getTime());
//
//            //if there is speed from location
//
//            lastlocation.setTime(location.getTime());
//            setCurSpeed(speed);
//
//            curCount ++;
//
//            if(curCount == (int) (MIN_TIME_BW_SPAN_SPEED / MIN_TIME_BW_UPDATES)){
//                lastSpanlocation.setLatitude(lastSpanLat);
//                lastSpanlocation.setLongitude(lastSpanLon);
//                distance = lastSpanlocation.distanceTo(location);
//                if (location.getAccuracy() < distance){
//                    lastSpanLat = latitude;
//                    lastSpanLon = longitude;
//                }
//                speed = distance/(location.getTime() - lastSpanlocation.getTime());
//
//                setPrevSpanSpeed(speed);
//                lastSpanlocation.setTime(location.getTime());
//
//                curCount = 0;
//            }

//            Sipdroid.loadURL("javascript:onLocationUpdate('" + latitude + "'" + "," + "'" + longitude + "'" + "," + "'" + location.getTime() + "')");

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class isStillStopped extends AsyncTask<Void, Integer, String> {
        int timer = 0;

        @Override
        protected String doInBackground(Void... unused) {
            try {
                while (getCurSpeed() == 0) {
                    Thread.sleep(1000);
                    timer++;
                }
            } catch (InterruptedException t) {
                return ("The sleep operation failed");
            }
            return ("return object when task is finished");
        }

        @Override
        protected void onPostExecute(String message) {
            setTimeStopped(timer);
        }
    }



}

