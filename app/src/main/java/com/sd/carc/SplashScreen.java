package com.sd.carc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import beacons.BeaconMonitoringActivity;
import beacons.BeaconRangingActivity;
import services.LocationUpdatesIntentService;
import services.StickyService;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    private SharedPreferences preferences;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent stickyService = new Intent(this, StickyService.class);
        startService(stickyService);
        Intent stickyService1 = new Intent(this, LocationUpdatesIntentService.class);
        startService(stickyService1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Sipdroid.class);
                startActivity(intent);
                preferences = getApplicationContext().getSharedPreferences("CDUCONN", Context.MODE_PRIVATE);
                Boolean isBeaconActive = preferences.getBoolean("isBeaconActive",false);
                Log.d("beconservice:","beconservice:"+isBeaconActive);
                if(isBeaconActive){
                    startBeaconService(BeaconRangingActivity.class,true);
                    startBeaconService(BeaconMonitoringActivity.class,true);
                    startBeaconService(LocationUpdatesIntentService.class,true);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    protected void startBeaconService(Class classToStart, boolean isFinish) {
        Intent intent = new Intent(this, classToStart);
        startService(intent);
        // if (isFinish)
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }


}
