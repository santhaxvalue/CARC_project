package com.sd.carc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

/**
 * Created by sureshkumar on 12/31/2016.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName compName = new ComponentName(
                context.getPackageName(),
                GCMIntentService_GCM.class.getName());
        startWakefulService(context, intent.setComponent(compName));
    }
}

