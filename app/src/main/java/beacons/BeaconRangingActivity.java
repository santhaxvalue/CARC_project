package beacons;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.estimote.sdk.internal.utils.L;
import com.sd.carc.Sipdroid;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import constants.AppConstants;

public class BeaconRangingActivity extends Service implements AppConstants {

    private BeaconManager beaconManager;
    private Region region;
    public SharedPreferences preferences;
    public static String registeredUUID;
    private Region[] BEACONS;

    private Region[] getBeacons(String beaconId) {
        BEACONS = new Region[]{
                new Region("beacon1", UUID.fromString(beaconId), null, null) //uuid without "-"
        };

        return BEACONS;
    }

    private Utils.Proximity getPreferedBeaconProximity() {
        preferences = getApplicationContext().getSharedPreferences("CDUCONN", Context.MODE_PRIVATE);
        String proximity = preferences.getString("PROXIMITY", "");
        Utils.Proximity preferedProximity;

        switch (proximity) {
            case "IMMEDIATE":
                preferedProximity = Utils.Proximity.IMMEDIATE;
                break;
            case "NEAR":
                preferedProximity = Utils.Proximity.NEAR;
                break;
            case "FAR":
                preferedProximity = Utils.Proximity.FAR;
                break;
            default:
                preferedProximity = Utils.Proximity.UNKNOWN;
        }

        return preferedProximity;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMonitoring();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startMonitoring() {
        if (beaconManager == null) {
            beaconManager = new BeaconManager(this);

            // Configure verbose debug logging.
            L.enableDebugLogging(true);

            /**
             * Scanning
             */
            beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 1);
            //region = new Region("ranged region", UUID.fromString(registeredUUID), null, null);
            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region paramRegion, List<Beacon> paramList) {
                    Utils.Proximity preferedProximity = getPreferedBeaconProximity();
                    if (paramList != null && !paramList.isEmpty()) {
                        Beacon beacon = paramList.get(0);
                        Utils.Proximity proximity = Utils.computeProximity(beacon);
                        if (proximity == preferedProximity) {
                            Log.d("TAG", "entered in region " + paramRegion.getProximityUUID());

                            String beaconDetails = String.format("%s|%s|%s|%s",
                                                                    proximity.toString(),
                                                                    beacon.getProximityUUID().toString(),
                                                                    beacon.getMajor(),
                                                                    beacon.getMinor());
                            Sipdroid.loadURL("javascript:onBeaconDetected('" + beaconDetails + "')");
                            Log.d("TAG", "Ranged Beacon Details " + beaconDetails);
                        }
                    }
                }
            });
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    preferences = getApplicationContext().getSharedPreferences("CDUCONN", Context.MODE_PRIVATE);
                    registeredUUID = preferences.getString("UUID", "");
                    Log.d("BEACON RANGING", "connected");
                    for (Region region : getBeacons(registeredUUID)) {
                        beaconManager.startRanging(region);
                    }
                }
            });
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }*/


}

