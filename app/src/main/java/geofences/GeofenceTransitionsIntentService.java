package geofences;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.sd.carc.LocationTrack;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.sd.carc.R;
import com.sd.carc.Sipdroid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sd.carc.IAWMain;


/**
 * Created by sureshkumar on 1/10/2017.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";

    public GeofenceTransitionsIntentService() {
        super(TAG);  // use TAG to name the IntentService worker thread
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    event.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        final int geofenceTransition = event.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            final List<Geofence> triggeringGeofences = event.getTriggeringGeofences();

            // Get the transition details as a String.
            final String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    String geofenceId = triggeringGeofences.get(0).getRequestId();
                String locTime = String.valueOf(Sipdroid.iawMain.getLocationTime());

                if (geofenceTransition == 1) {
//                        Sipdroid.loadURL("javascript:onGeofenceIn('" + geofenceId + "'"+"'","'" + avgspeed + "'"+"','"+ insspeedval +"')");
                        Sipdroid.loadURL("javascript:onGeofenceIn('" + geofenceId + "'" + "," + "'" + locTime + "')");

                } else if (geofenceTransition == 2) {
//                        Sipdroid.loadURL("javascript:onGeofenceOut('" + geofenceId + "'"+"','" + avgspeed + "'"+"','"+ insspeedval +"')");
                        Sipdroid.loadURL("javascript:onGeofenceOut('" + geofenceId + "'" + "," + "'" + locTime + "')");

                    }
                }
            });
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    class geofenceEvent extends AsyncTask<Void, Integer, String> {


        @Override
        protected String doInBackground(Void... unused) {
            try {
//                if (geofenceTransition == 1) {
////                        Sipdroid.loadURL("javascript:onGeofenceIn('" + geofenceId + "'"+"'","'" + avgspeed + "'"+"','"+ insspeedval +"')");
//                    Sipdroid.loadURL("javascript:onGeofenceIn('" + geofenceId + "'" + "," + "'" + locTime + "')");
//
//                } else if (geofenceTransition == 2) {
////                        Sipdroid.loadURL("javascript:onGeofenceOut('" + geofenceId + "'"+"','" + avgspeed + "'"+"','"+ insspeedval +"')");
//                    Sipdroid.loadURL("javascript:onGeofenceOut('" + geofenceId + "'" + "," + "'" + locTime + "')");
//
//                }
                Thread.sleep(2000);
            } catch (InterruptedException t) {
                return ("The sleep operation failed");
            }
            return ("return object when task is finished");

        }
    }
}
