package com.sd.carc;

/**
 * Created by sureshkumar on 12/31/2016.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainAlertScreen extends MainActivity {
    public static SharedPreferences preferences;
    String pushMessage = "";
    String req = "";
    String title = "";
    String srvObjectReference = "";
    String encodedMsgTxt = "";


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("************* SipdroidAlertScreen *************");
        pushMessage = getIntent().getStringExtra("payload");
        req = getIntent().getStringExtra("request");
        title = getIntent().getStringExtra("title");
        srvObjectReference = getIntent().getStringExtra("srvObjRef");
        preferences = getSharedPreferences("CDUCONN", MODE_PRIVATE);
        setCrashTracker(getClass().getName());
        Thread pushRunnable = new Thread(push);
        pushRunnable.start();

    }

    private void startTheApp() {
        Intent startIntent = new Intent(context, Sipdroid.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
        finish();
    }

    Runnable push = new Runnable() {
        @Override
        public void run() {
            try {
                if (!srvObjectReference.isEmpty()) {
                    String baseurl = iawMain.getBaseURL();
                    String sessionID = iawMain.getSessionID();
                    System.out.println("baseurl: " + baseurl);
                    System.out.println("sessionID: " + sessionID);
                    pushHandler.sendEmptyMessage(0);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    Handler pushHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    String esoAndMsgTxt = srvObjectReference + "|" + pushMessage;
                    if (Sipdroid.browser != null) {
                        //Sipdroid.loadURL("javascript:onNtfnOpenNG('" + srvObjectReference + "," + pushMessage + "," + title + "," + req + "')");
                        Sipdroid.loadURL("javascript:onNtfnOpen('" + esoAndMsgTxt + "')");
                        finish();
                    } else {
                        if (!srvObjectReference.isEmpty())
                            iawMain.setESO(srvObjectReference);

                        startTheApp();
                    }
                }
                break;
                case 1:
                    onBackPressed();
                    break;

                default:
                    break;
            }
        }
    };
}

