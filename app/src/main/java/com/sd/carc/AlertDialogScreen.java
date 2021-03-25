package com.sd.carc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

public class AlertDialogScreen extends Sipdroid {
    String message;
    String srvObjectReference = "";
    String pushMessage = "";
    public static IAWMain iawMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getIntent().getStringExtra("alert_message");
        Log.e("message", message);
        srvObjectReference = getIntent().getStringExtra("srvObjectReference");
        pushMessage = getIntent().getStringExtra("push_msg");
        setCrashTracker(getClass().getName());
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Alert");
        if (message != "")
            alertBuilder.setMessage(message +" Please Check your Inbox");
        alertBuilder.setCancelable(false);
        alertBuilder.setNeutralButton("Ok", new OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!srvObjectReference.isEmpty()&& !pushMessage.isEmpty()) {
                    String esoAndMsgTxt = srvObjectReference + "|" + pushMessage;
                    System.out.println("Setting ESO: " + srvObjectReference);
                    iawMain.setESO(esoAndMsgTxt);
                }

                int appState = iawMain.getAppState();
                Log.e("LOG", "APP STATE in AlertDialogScreen: " + appState);
                if (appState == 0) {
                    System.out
                            .println("Loading javascript:onNtfnOpenInLogin() Method in Sipdroid WebView");
                    Sipdroid.browser.loadUrl("javascript:onNtfnOpenInLogin()");
                }

                dialog.cancel();
                finish();
            }
        });
        alertBuilder.show();
    }
}

