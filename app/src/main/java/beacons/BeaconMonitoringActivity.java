package beacons;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import constants.AppConstants;

public class BeaconMonitoringActivity extends Service implements AppConstants {

    private BeaconManager beaconManager;
    public SharedPreferences preferences;
    public static String registeredUUID;

    public String getBeaconID() {
        preferences = getApplicationContext().getSharedPreferences("CDUCONN", Context.MODE_PRIVATE);
        registeredUUID = preferences.getString("UUID", "");
        return registeredUUID;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMonitoring();
        return START_STICKY;
    }

    public void startMonitoring() {
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString(getBeaconID()),
                        null, null));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                // could add an "enter" notification too if you want (-:
            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString(getBeaconID()), null, null));
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

