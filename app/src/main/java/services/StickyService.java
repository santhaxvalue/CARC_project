package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ramsrini on 11/01/18.
 */

public class StickyService extends Service {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public Context context;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        reportAppClosed();
        Log.d(getClass().getName(), "App just got removed from Recents!");
    }

    private void reportAppClosed(){
        context = getApplicationContext();
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());


        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("CDUCONN", MODE_PRIVATE);
        Log.e("Log", "DATE has been updated to : " + date);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString("APP_CLOSED_TIME", date);
        editor.commit();
    }

}