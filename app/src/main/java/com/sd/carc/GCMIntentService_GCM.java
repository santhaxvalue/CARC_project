package com.sd.carc;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

//import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import constants.AppConstants;

/**
 * Created by sureshkumar on 12/31/2016.
 */
public class GCMIntentService_GCM extends IntentService implements AppConstants{

    public double curLat;
    public double curLong;
    public LocationManager locationManager;
    public Location location;
    public boolean isGpsEnabled;
    public boolean isNetworkEnabled;
    public static String reg_id;
    private String srvObjectReference;
    String encodedMsgTxt = "";

    NotificationManager mNotificationManager = null;
    Notification notification;

    public GCMIntentService_GCM() {
        super("GCM");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("GCM FROM CLIENT APP", "Message: Fantastic!!!");
        /*GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        String payloadMSG = "";
        String keyvalues = "";

        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
        // Extract the payload from the message
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equalsIgnoreCase(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equalsIgnoreCase(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equalsIgnoreCase(messageType)) {
                Set<String> keysetmessage = extras.keySet();
                for (String key : keysetmessage) {
                    System.out.println("KEY :" + key);
                    System.out.println("VALUES " + extras.get(key).toString());

                    if (key.equals("data.srvObjRef") || key.equals("srvObjRef")) {
                        srvObjectReference = extras.getString(key).toString();
                        Log.e("srvObjectReference", "srvObjectReference"
                                + srvObjectReference);
                    }

                    if ((key.equals("payload") || key.equals("data.payload"))) { // if
                        payloadMSG = extras.get(key).toString();
                    } else if (!key.equals("collapse_key")) {
                        keyvalues = keyvalues + key + "="
                                + extras.get(key).toString() + "&";
                        if (keyvalueString esoAndMsgTxt = srvObjectReference + "|" + payloadMSG;
                    Sipdroid.loadURL("javascript:onNtfnRcvd('" + esoAndMsgTxt + "')");s.contains("data.srvObjRef")
                                || keyvalues.contains("data.request"))
                            keyvalues = keyvalues.replace("data.", "");
                    }
                }
                if (!isAppInForeGround())
                    sendNotification(payloadMSG, srvObjectReference);
                else {

                }
            }
        } else {
            Log.e("NOTIFICATION", "Extras Null");
        }*/
    }

    private void showAlert(String payloadMSG) {
        System.out.println("payloadMSG " + payloadMSG);
        Intent alertIntent = new Intent(GCMIntentService_GCM.this,
                AlertDialogScreen.class);
        alertIntent.putExtra("alert_message", payloadMSG);
        alertIntent.putExtra("srvObjectReference", srvObjectReference);
        alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alertIntent);
    }

    private boolean isAppInForeGround() {
        return CampusDisclosureApplication.isActivityVisible();
    }

    public void sendNotification(String payloadMSG, String srvObjRef) {
        int icon = R.mipmap.icon;
        CharSequence tickerText = payloadMSG;
        CharSequence contentText = "";
        long when = System.currentTimeMillis();
        Integer id = (int) (long) when;
        try {
            contentText = URLDecoder.decode(payloadMSG, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String[] messageArray = message.split("~");
//        if(messageArray.length == 1) {
//            contentTitle = payloadMSG;
//            contentText = payloadMSG;
//        } else {
//            contentTitle = messageArray[0];
//            contentText = messageArray[1];
//        }
        Intent notificationIntent = null;

        notificationIntent = new Intent(GCMIntentService_GCM.this,
                MainAlertScreen.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("push_msg", payloadMSG);
        //notificationIntent.putExtra("keyvalue_msg", keyvalues);
        notificationIntent.putExtra("srvObjectReference", srvObjRef);
 
        PendingIntent contentIntent = PendingIntent.getActivity(
                GCMIntentService_GCM.this, id, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(GCMIntentService_GCM.this);
        builder.setTicker(tickerText);
        builder.setContentTitle(APP_NAME);
        builder.setContentText(contentText);
        builder.setStyle(new Notification.BigTextStyle().bigText(APP_NAME));
        builder.setStyle(new Notification.BigTextStyle().bigText(contentText));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(icon);
        } else {
            builder.setSmallIcon(icon);
        }
        builder.setContentIntent(contentIntent);
        builder.build();
        notification = builder.getNotification();
        notification.flags = Notification.DEFAULT_LIGHTS
                | Notification.FLAG_AUTO_CANCEL;

        System.out.println("NOTIFICATION ID :" + id);
        mNotificationManager.notify(id, notification);
    }
}
