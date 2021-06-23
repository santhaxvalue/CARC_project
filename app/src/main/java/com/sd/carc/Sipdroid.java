package com.sd.carc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.pubnub.api.PNConfiguration;
//import com.pubnub.api.PubNub;
//import com.pubnub.api.callbacks.PNCallback;
//import com.pubnub.api.callbacks.SubscribeCallback;
//import com.pubnub.api.enums.PNStatusCategory;
//import com.pubnub.api.models.consumer.PNPublishResult;
//import com.pubnub.api.models.consumer.PNStatus;
//import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
//import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
//import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
//import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
//import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
//import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
//import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
//import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import barcodescanner.ScannerActivity;
import firebase.Config;
import media.AudioRecordActivity;
import media.VideoPlayer;
import services.BackgroundService;
import services.NetworkSchedulerService;
import services.receivers.SMSReceiver;
import signature.Capture;
import utils.AlertBox;
import utils.Helper;
import utils.KairosActivity;
import utils.RealPathUtil;
import youtubeplayer.YoutubePlayerFullScreen;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class Sipdroid extends MainActivity implements DialogInterface.OnDismissListener, HomeBroadcast.ConnectivityReceiverListener {
    //GoogleCloudMessaging gcm;
    static String SHARED_PREF_NAME = "SIPDROID";
    static String GCM_REGSTRING_NAME = "GCMREGSTRING";

    @SuppressLint("StaticFieldLeak")
    public static WebView browser;
    private final static String TAG = "Sipdroid";

    String returnMethodName, urlToPost;
    AudioManager audioManager;
    MediaPlayer mPlayer;
    String absolutePingURL;
    String pingResponse = "";
    public static String ipAddress = "";
    public static String IP_ADDRESS_US = "162.242.228.231";
    static String PROPERTY_APP_VERSION = "PROPERTY_APP_VERSION";
    private String regString = "";
    LocationManager locationManager;
    String callbackMethodName;
    private static final int YOU_TUBE_CODE = 4444;
    boolean isLoaded = false;
    private TextToSpeech textToSpeech;
    ProgressBar pbar;
    private static final int IMAGE_CAPTURE = 100, IMAGE_FROM_GALLERY = 101;
    private static final int VIDEO_CAPTURE = 1, VIDEO_FROM_GALLERY = 2;
    private static final int AUDIO_CAPTURE = 3, AUDIO_FROM_GALLERY = 4;
    int isAudioPlaying = 0;
    int audioLength;
    String filePath;
    private static final int SIGNATURE_CODE = 5;
    static String videoFilePath = "";
    String responseValue, fileName;
    int statusCode;
    Helper helper = new Helper();
    String imageAsBase64;
    android.hardware.Camera camera;

    // NFC related Variables
    ProgressDialog dialog, dialogNFC;
    NfcAdapter nfcAdapter;
    long nfcId;
    String name, villaNo;
    Tag nfcTag;
    Ndef ndef;
    static boolean isWrite;
    String dataToWrite;
    boolean isLockNFC, isNFCIdRead;
    String tagTypeToRead;
    Handler nfcHandler;
    Runnable nfcRunnable;
    int nfcTimeout;
    boolean isReadOrWriteCompleted;
    boolean isAllowIntentToReadNfc;
    String readedTagType = "";

    //flags to handle page loads
    boolean isPageLoadFailure = false;
    String urlBeingLoaded = "";

    String appClosedTime = "";

    @SuppressLint("StaticFieldLeak")
    private static Sipdroid mInstanceActivity;
    private boolean isReceiverRegistered = false;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 555;
    private long locUpdateFrequency = 0;


    //String ua = "Mozilla/5.0 (Android; Tablet; rv:20.0) Gecko/20.0 Firefox/20.0";

    @Override
    protected void onStart() {
        super.onStart();
    }



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("onetest:","Sipdroid");



        try {
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            mInstanceActivity = this;

            audioManager = (AudioManager) getBaseContext().getSystemService(
                    AUDIO_SERVICE);
            setContentView(R.layout.activity_main);
            context = getApplicationContext();
            preferences = getApplicationContext().getSharedPreferences("CDUCONN",
                    MODE_PRIVATE);
            /*showTurnOnLocationPrompt();*/
            browser = findViewById(R.id.activity_main_webview);
            pbar = findViewById(R.id.progressBar);
            pbar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

            WebSettings myWS = browser.getSettings();
            String uaStr = myWS.getUserAgentString();
            printText("uaStr = " + uaStr);

            browser.getSettings().setUseWideViewPort(true);
            browser.getSettings().setLoadWithOverviewMode(true);

            browser.setWebViewClient(new MyWebViewClient());

            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            browser.addJavascriptInterface(new WebAppInterface(this), "Android");
            browser.getSettings().setAppCacheEnabled(true);
            browser.getSettings().setUseWideViewPort(true);
            browser.getSettings().setLoadWithOverviewMode(true);
            browser.getSettings().setBuiltInZoomControls(false);
            browser.getSettings().setDisplayZoomControls(true);
            browser.getSettings().setSupportZoom(false);
            browser.getSettings().setDomStorageEnabled(true);
            browser.getSettings().setPluginState(WebSettings.PluginState.ON);
            browser.getSettings().setAllowContentAccess(true);
            browser.getSettings().setAllowFileAccess(true);
            browser.getSettings().setAllowFileAccessFromFileURLs(true);
            browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
            browser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//            browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            browser.getSettings().setPluginState(WebSettings.PluginState.ON);
            browser.getSettings().setUserAgentString("0");
            browser.getSettings().setDatabaseEnabled(true);
            browser.getSettings().setAppCacheMaxSize(WebSettings.LOAD_NO_CACHE); // 10MB
            browser.setHapticFeedbackEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }

            browser.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            myWS = browser.getSettings();
            uaStr = myWS.getUserAgentString();
            printText("uaStr = " + uaStr);

            browser.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message,
                                         JsResult result) {
                    return super.onJsAlert(view, url, message, result);
                }

                @Override
                public boolean onJsConfirm(WebView view, String url,
                                           String message, JsResult result) {
                    return super.onJsConfirm(view, url, message, result);
                }

                @Override
                public boolean onJsPrompt(WebView view, String url, String message,
                                          String defaultValue, JsPromptResult result) {
                    return super.onJsPrompt(view, url, message, defaultValue,
                            result);
                }

                public void onProgressChanged(WebView view, int progress) {
                    pbar.setProgress(progress);
                    if (progress == 100) {
                        pbar.setVisibility(View.GONE);
                    } else {
                        pbar.setVisibility(View.VISIBLE);
                    }
                }
            });

            reportAppStart();
            checkConnection();
            setCrashTracker(getClass().getName());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            setNewWebviewActivityStatus(false);

            //nfc object creations
            nfcHandler = new Handler();

            preferences = getApplicationContext().getSharedPreferences(
                    "CDUCONN", MODE_PRIVATE);
            appClosedTime = preferences.getString("APP_CLOSED_TIME", "");

            if (appClosedTime != null && !appClosedTime.isEmpty()) {
                String dateDifference = Helper.getDateDiffInStr(appClosedTime);
                if (!dateDifference.isEmpty()) {
                    reportAppClosed(dateDifference);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                scheduleJob();
            }
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(SERVER_CLIENT_ID)
                    .requestServerAuthCode(SERVER_CLIENT_ID)
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            if (checkAlreadySaved().isEmpty())
                callGCMRegistration(context, SENDER_ID);
            splashHandler.sendEmptyMessage(0);

        } catch (Exception e) {
            System.out.println("exception from asist :----------------->");
            e.printStackTrace();
        }



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            Intent mIntent = new Intent(Sipdroid.this, SelectLocPermission.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(mIntent);
//        }

    }




    private void registerSmsReceiver() {
        SMSReceiver smsReceiver = new SMSReceiver();
        if (!isReceiverRegistered) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
                this.registerReceiver(smsReceiver, intentFilter);
                isReceiverRegistered = true;
            } else {
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
                this.registerReceiver(smsReceiver, intentFilter);
                isReceiverRegistered = true;
            }
        }
    }



    private void unregisterSmsReceiver() {
        SMSReceiver smsReceiver = new SMSReceiver();
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
            isReceiverRegistered = false;
        }
    }

    public void updateOtp(String messageFrom, String message) {
        String storedNumber = preferences.getString("MOBILE_NUMBER", "");
        if (storedNumber != null && storedNumber.equals(messageFrom)) {
            Sipdroid.loadURL("javascript:" + returnMethodName + "('" + messageFrom + "'" + ",'" + message + "')");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(myJob);
        }
    }

    public static Sipdroid getmInstanceActivity() {
        return mInstanceActivity;
    }

    @SuppressLint("HandlerLeak")
    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                preferences = getApplicationContext().getSharedPreferences(
                        "CDUCONN", MODE_PRIVATE);
                absolutePingURL = preferences.getString("PING_URL_ABS", "");

                if (!(absolutePingURL != null && absolutePingURL.isEmpty())) {
                    Log.e("URL", absolutePingURL);
                    pingResponse = absolutePingURL;
                } else {
                    pingResponse = ABS_PING_URL;
                }

                if (!pingResponse.isEmpty()) {
                    browser.loadUrl(pingResponse);
                    ipAddress = IP_ADDRESS_US;// updated with constant not from the url
                    Log.d("ipAddress", ipAddress);
                } else
                    printText("pingResponse is NULL");
            }
        }
    };

    /***** App report methods *****/
    private void reportAppStart() {
        try {
            preferences = getApplicationContext().getSharedPreferences(
                    "CDUCONN", MODE_PRIVATE);
            userName = preferences.getString("USER_DETAILS", "");
            String APP_START_URL = "'" + POST_ACTIVITY_EVENT_URL + "'msg=4b2d2d772f544b8c9ba76921a6b21352|user='" + userName + "'|appID='" + applicationID + "'|appStatus='" + AppStatus.appStarted + "'|epDelay='" + 0 + "'";
            APP_START_URL = APP_START_URL.replaceAll("\'", "");
            BackgroundService downloader = new BackgroundService(APP_START_URL);
            downloader.execute();
//            JSONObject props = new JSONObject();
//            props.put("msg", "4b2d2d772f544b8c9ba76921a6b21352");
//            props.put("AppStart", AppStatus.appStarted);
//            props.put("appID", applicationID);
//            props.put("user", userName);
//            props.put("epDelay", 0);
//            mixpanel.track("AppStatus", props);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportOnBackgroundNotification() {
        try {
            iawMain = new IAWMain(context);
            String APP_START_URL = "'" + POST_ACTIVITY_EVENT_URL + "'msg=8o8p3r45987740cca0ff6a8d25getlolf|latitude='" + iawMain.getLatitude() + "'|longitude='" + iawMain.getLongitude() + "'|unqID='" + iawMain.getDeviceID() + "'";
            APP_START_URL = APP_START_URL.replaceAll("\'", "");
            BackgroundService downloader = new BackgroundService(APP_START_URL);
            downloader.execute();
//            JSONObject props = new JSONObject();
//            props.put("msg", "8o8p3r45987740cca0ff6a8d25getlolf");
//            props.put("latitude", iawMain.getLatitude());
//            props.put("longitude", iawMain.getLongitude());
//            props.put("unqID", iawMain.getDeviceID());
//            mixpanel.track("AppStatus", props);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportAppClosed(String timeDelay) {
        try {
            context = getApplicationContext();
            if (iawMain == null) {
                iawMain = new IAWMain(context);
            }
            preferences = getApplicationContext().getSharedPreferences(
                    "CDUCONN", MODE_PRIVATE);
            userName = preferences.getString("USER_DETAILS", "");
            String APP_CLOSED_URL = "'" + POST_ACTIVITY_EVENT_URL + "'msg=4b2d2d772f544b8c9ba76921a6b21352|user='" + userName + "'|appID='" + applicationID + "'|appStatus='" + AppStatus.appClosed + "'|epDelay='" + timeDelay + "'";
            APP_CLOSED_URL = APP_CLOSED_URL.replaceAll("\'", "");
            BackgroundService downloader = new BackgroundService(APP_CLOSED_URL);
            downloader.execute();
//            JSONObject props = new JSONObject();
//            props.put("msg", "4b2d2d772f544b8c9ba76921a6b21352");
//            props.put("AppClosed", AppStatus.appClosed);
//            props.put("appID", applicationID);
//            props.put("user", userName);
//            props.put("epDelay", timeDelay);
//            mixpanel.track("AppStatus", props);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***** App report methods *****/

    // Barcode scanner
    private void launchBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.addExtra("PROMPT_MESSAGE", "");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /***** Firebase related methods *****/
    public void createFirebaseDb() {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();


        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("users");


        // Creating new user node, which returns the unique key value
        // new user node would be /users/$userid/
        String userId = mFirebaseDatabase.push().getKey();

        String timeStamp = ServerValue.TIMESTAMP.toString();

        //creating user object
        User user = new User("deva", "deva@gmail.com", ServerValue.TIMESTAMP);

        // pushing user to 'users' node using the userId
        if (userId != null) {
            mFirebaseDatabase.child(userId).setValue(user);
        }


        mFirebaseDatabase.orderByChild(userId).startAt("1513248751120");

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    User user = dataSnapshot.getValue(User.class);
                    showShortToast(context, "User name: " + user.name + ", email " + user.email);
                    printText("onDataChange = " + user);
                } catch (Exception e) {
                    printText("Exception in ondatachange:" + e);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Get2it", "Failed to read value.", error.toException());
                showShortToast(context, "Failed to read value." + error.toException());

            }
        });
    }

    private String retrieveRegString() {
        SharedPreferences preferences = context.getSharedPreferences(
                SHARED_PREF_NAME, MODE_PRIVATE);
        String gcmRegString = preferences.getString(GCM_REGSTRING_NAME, "");
        printText("RegString is RETRIEVED locally: " + gcmRegString);
        return gcmRegString;
    }

    private String checkAlreadySaved() {
        SharedPreferences preferences = context.getSharedPreferences(
                SHARED_PREF_NAME, MODE_PRIVATE);
        String gcmRegString = preferences.getString(GCM_REGSTRING_NAME, "");
        printText("RegString is RETRIEVED locally: " + gcmRegString);

        int regVersion = preferences.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        if (regVersion != currentVersion)
            return "";

        return gcmRegString;
    }

    @SuppressLint("StaticFieldLeak")
    private void callGCMRegistration(final Context context,
                                     final String theGCMProjNo) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                checkNotNull(theGCMProjNo, "SENDER_ID");
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                regString = pref.getString("regId", null);
                printText("gcmStoredId is Not NULL: " + regString);
                /*gcm = GoogleCloudMessaging.getInstance(context);
                try {
                    regString = gcm.register(theGCMProjNo);
                    printText();("gcmStoredId is Not NULL: " + regString);
                } catch (IOException e) {
                    printText();("===THE EXCEPTION IS NULL GCM====");
                    e.printStackTrace();
                }*/
                return regString;
            }

            @Override
            protected void onPostExecute(String resultRegString) {
                super.onPostExecute(resultRegString);
                if (resultRegString != null && !resultRegString.isEmpty()) {
                    iawMain.setRegString(resultRegString);
                    saveLocally(resultRegString);
                } else {
                    String savedID = retrieveRegString();
                    if (!savedID.isEmpty())
                        iawMain.setRegString(savedID);
                }
            }
        }.execute(null, null, null);
    }

    @SuppressLint("StringFormatInvalid")
    private void checkNotNull(Object reference, String name) {
        if (reference == null) {
            throw new NullPointerException(getApplicationContext().getString(
                    R.string.app_name, name));
        }
    }

    private void saveLocally(String gcmRegString) {
        SharedPreferences preferences = context.getSharedPreferences(
                SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int currentVersion = getAppVersion(context);
        editor.putInt(PROPERTY_APP_VERSION, currentVersion);
        editor.putString(GCM_REGSTRING_NAME, gcmRegString);
        editor.apply();
        printText("RegString is SAVED locally: " + gcmRegString
                + " , " + currentVersion);
    }
    /***** Firebase related methods *****/


    /***** Battery,Flash light, and so on *****/
    public void doPlayTTS(final String ttsText, final String speed,
                          final String lang) {

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locale = new Locale(lang);
                    int result = textToSpeech.setLanguage(locale);
                    textToSpeech.setSpeechRate(convertStringToFloat(speed));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak(ttsText);
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    private int getBatteryLevel() {
        int batLevel = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return batLevel;
    }

    private void toggleFlashLight(Boolean isLightOn) {
        if (isLightOn) {
            camera = android.hardware.Camera.open();
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();

        } else {
            camera = android.hardware.Camera.open();
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
        }
    }

    private void raiseVolume() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    // method used to open browser
    public void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void checkWIFIStatus() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected()) {
            printText("Connected with wifi");
        } else {
            printText("Not Connected with wifi");
        }

    }

    public void startFloatingService() {
        printText("Came to float stopped and cd");
        splashHandler.sendEmptyMessage(0);
    }

    private void showTurnOnLocationPrompt() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

    }

    public void runSessionTimeOutThread(int timeOut) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                browser.loadUrl("javascript:sessionClosed()");
            }
        }, timeOut);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pkgInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float convertStringToFloat(String strValue) {
        float numberInFloat = 0;
        try {
            numberInFloat = Float.valueOf(strValue);
        } catch (Exception e) {
            System.out.println(e);
        }
        return numberInFloat;
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private boolean toggleBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            return false;
        } else {
            mBluetoothAdapter.enable();
            return true;
        }
    }
    /***** Battery,Flash light, and so on *****/


    /***** Network Connection related methods *****/
    private void checkConnection() {
        boolean isConnected = HomeBroadcast.isConnected();
        /*if (!isConnected)*/
        showSnack(isConnected, false);
    }

    public void showSnack(boolean isConnected, boolean giveAccessToLoadWebview) {
        if (isConnected) {
            if (giveAccessToLoadWebview) {
                if (!isLoaded) {
                    loadURL(urlBeingLoaded);
                }
            }
        } else {
            /*findViewById(R.id.no_internet).setVisibility(View.VISIBLE);*/
//            if (!isLoaded)
//                isLoaded = false;
//            else
//                isLoaded = true;
            showAlert(this).show();
        }
    }

    public AlertDialog.Builder showAlert(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("No Internet Connection");
        builder.setMessage("check your mobile data and wifi!!");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnection();
            }
        });
        return builder;
    }
    /***** Network Connection related methods *****/


    /*********** Methods for new webview *********/
    private void openPayUMoneyWebview(String urlToOpen, String returnMethod) {
        setNewWebviewActivityStatus(true);
        try {
            Intent intentToCreateNewWebview = new Intent(context, PUMWebviewActivity.class);
            intentToCreateNewWebview.putExtra("urlToLoadWebview", urlToOpen);
            intentToCreateNewWebview.putExtra("retunMethodName", returnMethod);
            intentToCreateNewWebview.putExtra("buttonVisibility", "");
            startActivity(intentToCreateNewWebview);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private void openWebview(String urlToOpen, String returnMethod, String buttonVisibity) {
        setNewWebviewActivityStatus(true);
        try {
            Intent intentToCreateNewWebview = new Intent(context, PUMWebviewActivity.class);
            intentToCreateNewWebview.putExtra("urlToLoadWebview", urlToOpen);
            intentToCreateNewWebview.putExtra("retunMethodName", returnMethod);
            intentToCreateNewWebview.putExtra("buttonVisibility", buttonVisibity);
            startActivity(intentToCreateNewWebview);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public static void paymentResponse(final String returnMethodName, final String returnValue) {
        setNewWebviewActivityStatus(false);
        Sipdroid.loadURL("javascript:" + returnMethodName + "('" + returnValue + "')");
    }

    public static void webviewResponse(final String returnMethodName, final String returnValue) {
        setNewWebviewActivityStatus(false);
        Sipdroid.loadURL("javascript:" + returnMethodName + "('" + returnValue + "')");
    }

    /*********** Methods for new webview *********/


    @SuppressLint("StaticFieldLeak")
    public class postData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            KairosActivity hh = new KairosActivity();
            String temp = hh.HttpConnection(strings[0], strings[1], strings[2]);
            showShortToast(context, "Result: " + temp);
            return null;
        }
    }

    /***** Media related methods ******/
    private void selectMedia(int action) {
        if (action == 1) {
            final CharSequence[] items = {"Take Photo", "Choose from Library"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Sipdroid.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        startIntent(1);
                    } else if (items[item].equals("Choose from Library")) {
                        galleryIntent("image/*", IMAGE_FROM_GALLERY);
                    }
                }
            });
            builder.show();
        } else if (action == 2) {
            final CharSequence[] items = {"Record Video", "Choose from Library"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Sipdroid.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("Add Video!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Record Video")) {
                        startIntent(2);
                    } else if (items[item].equals("Choose from Library")) {
                        galleryIntent("video/*", VIDEO_FROM_GALLERY);
                    }
                }
            });
            builder.show();
        } else if (action == 3) {
            final CharSequence[] items = {"Record Audio", "Choose from Library"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Sipdroid.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("Add Audio!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Record Audio")) {
                        startIntent(3);
                    } else if (items[item].equals("Choose from Library")) {
                        galleryIntent("audio/*", AUDIO_FROM_GALLERY);
                    }
                }
            });
            builder.show();
        }
    }

    public void startIntent(int intentToOpen) {
        if (intentToOpen == 1) {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
            }
        } else if (intentToOpen == 2) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

            // set video quality
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);
            }
        } else if (intentToOpen == 3) {
            Intent intent = new Intent(Sipdroid.this, AudioRecordActivity.class);
            startActivityForResult(intent, AUDIO_CAPTURE);
        }
    }

    public void galleryIntent(String type, int SELECT_FILE) {
        boolean gallery = true;
        Intent intent = new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @SuppressLint("LongLogTag")
    public File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(PATH_FOR_VIDEO);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(PATH_FOR_VIDEO, "Oops! Failed create "
                        + PATH_FOR_VIDEO + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VIDEO_" + timeStamp + ".mp4");
            videoFilePath = mediaFile.getAbsolutePath();
        } else {
            return null;
        }

        return mediaFile;
    }

    public void playLocalAudio(boolean playSound) {
        if (playSound) {
            mPlayer = MediaPlayer.create(getApplicationContext(),
                    R.raw.ambulance_small);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    System.out.print("completed");
                    if (!callbackMethodName.isEmpty())
                        Sipdroid.loadURL("javascript:" + callbackMethodName + "('" + true + "')");
                }
            });
            if (!mPlayer.isPlaying())
                try {
                    mPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
        } else {
            if (mPlayer != null && mPlayer.isPlaying()) {
                Log.e("LOG", "MediaPlayer pause!!!");
                mPlayer.stop();
            } else
                Log.e("LOG", "MediaPlayer is NOT PLAYING!!!");
        }
    }

    /***** Media related methods ******/


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(tel);
                return true;
            } else if (url.contains("mailto:")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;

            } else {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!url.equals("about:blank")) {
                isPageLoadFailure = false;
                urlBeingLoaded = url;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //show webview
            browser.setVisibility(View.VISIBLE);
            isLoaded = !isPageLoadFailure;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(final WebView view, int errorCode, String description,
                                    final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (urlBeingLoaded.equals(failingUrl)) {
                isLoaded = false;
                isPageLoadFailure = true;
                view.loadUrl("about:blank");
            } else {
                browser.loadUrl("javascript:onPgRcvdHttpErr('" + failingUrl + "','" + errorCode + "')");
//                        view.loadUrl("about:blank");
//                        AlertBox.showPageErrorAlert(Sipdroid.this, failingUrl).show();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            String failingURL = request.getUrl().toString();
            int errorCode = error.getErrorCode();
            if (urlBeingLoaded.equals(failingURL)) {
                isLoaded = false;
                isPageLoadFailure = true;
                view.loadUrl("about:blank");
            } else {
                browser.loadUrl("javascript:onPgRcvdHttpErr('" + failingURL + "','" + errorCode + "')");
//                        view.loadUrl("about:blank");
//                        AlertBox.showPageErrorAlert(Sipdroid.this, failingURL).show();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            String failingURL = request.getUrl().toString();
            int errorCode = errorResponse.getStatusCode();
            if (urlBeingLoaded.equals(failingURL)) {
                isLoaded = false;
                isPageLoadFailure = true;
                view.loadUrl("about:blank");
                AlertBox.showPageErrorAlert(Sipdroid.this, urlBeingLoaded).show();
            } else {
                browser.loadUrl("javascript:onPgRcvdHttpErr('" + failingURL + "','" + errorCode + "')");
//                        view.loadUrl("about:blank");
//                        AlertBox.showPageErrorAlert(Sipdroid.this, failingURL).show();
            }
        }
    }

    public class WebAppInterface {
        Context mContext;

        public WebAppInterface(Context c) {
            mContext = c;
        }

        public WebAppInterface() {
        }



        @JavascriptInterface
        public void publishPubNub(String userId,String publishKey,String subscribeKey) {
            Log.e("publishPubNub:", "publishPubNub:" + userId +" : "+publishKey+" : "+subscribeKey);

            String userIdstr = userId;
            String publishKeystr = publishKey;
            String subscribeKeystr = subscribeKey;

            pubNubPublishMethod(userIdstr,publishKeystr,subscribeKeystr);

        }


        @JavascriptInterface
        public void startAppNormal(boolean flag) {
            Log.e("appnormal", "" + flag);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FLOATVIEW", flag);
            editor.commit();
            startFloatingService();
        }

        @JavascriptInterface
        public void setUser(String userDetails) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);
            Log.e("Log", "UserDetail has been updated to : " + userDetails);
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            editor.putString("USER_DETAILS", userDetails);
            editor.commit();
        }



        @JavascriptInterface
        public void toggleDisableBack(boolean flag) {
            Log.e("toggle_BackOff", "" + flag);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("BACK_BTN", flag);
            editor.commit();
        }

        @JavascriptInterface
        public void do_browser_back(boolean flag) {
            Log.e("BROWSER_BACK", "" + flag);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("BROWSER_BACK", flag);
            editor.commit();
        }

        @JavascriptInterface
        public boolean isAppNormal() {
            boolean bool_AppNormal = preferences.getBoolean("FLOATVIEW", false);
            return bool_AppNormal;
        }

        @JavascriptInterface
        public boolean isAppInForeground() {
            return iawMain.getAppStatus();
        }

        @JavascriptInterface
        public void showPageErrorAlert(String title, String message) {
            isLoaded = false;
            isPageLoadFailure = true;
            AlertBox.showPageErrorAlert(Sipdroid.this, urlBeingLoaded);
        }

        @JavascriptInterface
        public void openNewWebview(String urlToOpen, String returnMethod) {
            openPayUMoneyWebview(urlToOpen, returnMethod);
        }


        @JavascriptInterface
        public void openNewWebview(String urlToOpen, String returnMethod, String buttonVisibity) {
            openWebview(urlToOpen, returnMethod, buttonVisibity);
        }

        @JavascriptInterface
        public void googleSignIn(String returnMethod) {
            returnMethodName = returnMethod;
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }


        @JavascriptInterface
        public void startSMSListener(String mobileNumber, String returnMethod) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MOBILE_NUMBER", mobileNumber);
            editor.commit();
            returnMethodName = returnMethod;
            registerSmsReceiver();
        }

        @JavascriptInterface
        public void stopSMSListener() {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MOBILE_NUMBER", "");
            editor.commit();
            unregisterSmsReceiver();
        }

        @JavascriptInterface
        public void launchScanner(String returnMethod) {
            returnMethodName = returnMethod;
            launchBarcodeScanner();
        }

        @JavascriptInterface
        public void turnOnFlashLight(Boolean flag) {
            //returnMethodName = returnMethod;
            toggleFlashLight(flag);
        }

        @JavascriptInterface
        public boolean turnOnBluetooth() {
            //returnMethodName = returnMethod;
            return toggleBluetooth();
        }

        @JavascriptInterface
        public void getWifiStatus(String returnMethod) {
            returnMethodName = returnMethod;
            checkWIFIStatus();
        }

        @JavascriptInterface
        public void call(String phoneNumber) {
            printText("call() method is called: " + phoneNumber);
            showShortToast(context, "PhoneNumber: " + phoneNumber);
            Intent callIntent = iawMain.doCall(phoneNumber);
            if (callIntent != null)
                startActivity(callIntent);
        }

        @JavascriptInterface
        public void setABSURL(String url) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);
            Log.e("Log", "PING_URL_ABS as been updated to : " + url);
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            editor.putString("PING_URL_ABS", url);
            editor.commit();
        }

        @JavascriptInterface
        public void setCurrPageUrl(String url) {
            urlBeingLoaded = url;
        }

        @JavascriptInterface
        public void setProximity(String range) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);
            Log.e("Log", "PROXIMITY for beacon as been updated to : "
                    + range);
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            editor.putString("PROXIMITY", range);
            editor.commit();
        }

        /********** NFC Related Bridge calls ***********/

        @JavascriptInterface
        public void startNFCScan(int timeout, String dataType, String returnMethod) {

            isWrite = false;
            returnMethodName = returnMethod;
            tagTypeToRead = dataType;
            nfcTimeout = timeout;
            isReadOrWriteCompleted = false;
            isAllowIntentToReadNfc = true;

            nfcAdapter = NfcAdapter.getDefaultAdapter(Sipdroid.this);

            setupForegroundDispatch(Sipdroid.this, nfcAdapter);

            switch (checkNFC()) {
                case 0:
                    showProgressDialog("", "Scan NFC Card For Read");
                    break;
                case 1:
                    enableNFC();
                    break;
                case -1:
                    showLongToast(context, "This Device doesn't support NFC!");
                    break;
            }

            startNFCHandler(nfcTimeout);
        }

        @JavascriptInterface
        public void onNFCIdRead() {
            isWrite = false;
            isNFCIdRead = true;
            switch (checkNFC()) {
                case 0:
                    showProgressDialog("", "Scan NFC Card...");
                    break;
                case 1:
                    enableNFC();
                    break;
                case -1:
                    showLongToast(context, "This Device doesn't support NFC!");
                    break;
            }
        }

        @JavascriptInterface
        public void doNFCWrite(String id, String name, String villaNo,
                               boolean lockNFC) {
            if (checkNFC() != -1) {
                if (dialogNFC == null)
                    showProgressDialog("", "Scan NFC Card...");
                else
                    showProgressDialog("", "Scan NFC Card...");

                isWrite = true;
                StringBuilder whatJSON = new StringBuilder();
                whatJSON.append(id);
                whatJSON.append(",");
                whatJSON.append(name);
                whatJSON.append(",");
                whatJSON.append(villaNo);
                dataToWrite = whatJSON.toString();
                isLockNFC = lockNFC;
                processWriteNFC(dataToWrite);
            } else
                showLongToast(context, "This Device doesn't support NFC!");
        }

        @JavascriptInterface
        public void doNFCWrite(JSONObject json) { // String json

            printText("json: " + json);
            if (checkNFC() != -1) {
                if (dialogNFC == null)
                    showProgressDialog("", "Scan NFC Card...");
                else
                    showProgressDialog("", "Scan NFC Card...");

                isWrite = true;
                // StringBuilder whatJSON = new StringBuilder();
                // whatJSON.append(id);
                // whatJSON.append(",");
                // whatJSON.append(name);
                // whatJSON.append(",");
                // whatJSON.append(villaNo);
                dataToWrite = json.toString();
                try {
                    if (json.has("lockNFC"))
                        isLockNFC = (Boolean) json.get("lockNFC");
                    isLockNFC = false;
                    processWriteNFC(dataToWrite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                showLongToast(context, "This Device doesn't support NFC!");
        }

        @JavascriptInterface
        public void doNFCWrite(int timeout, String json, String returnMethod) { // json
            returnMethodName = returnMethod;
            nfcTimeout = timeout;
            isReadOrWriteCompleted = false;
            isAllowIntentToReadNfc = true;
            printText("json as String: " + json);

            nfcAdapter = NfcAdapter.getDefaultAdapter(Sipdroid.this);

            setupForegroundDispatch(Sipdroid.this, nfcAdapter);

            if (checkNFC() != -1) {
                if (dialogNFC == null)
                    showProgressDialog("", "Scan NFC Card For Write");
                else
                    showProgressDialog("", "Scan NFC Card For Write");
                startNFCHandler(nfcTimeout);
                isWrite = true;
                // StringBuilder whatJSON = new StringBuilder();
                // whatJSON.append(id);
                // whatJSON.append(",");
                // whatJSON.append(name);
                // whatJSON.append(",");
                // whatJSON.append(villaNo);
                dataToWrite = json;
                //try {
                    /*JSONObject jObj = new JSONObject(json);
                    if (jObj.has("lockNFC"))
                        isLockNFC = (Boolean) jObj.get("lockNFC");*/
                isLockNFC = false;
                processWriteNFC(dataToWrite);
                /*} catch (JSONException e) {
                    e.printStackTrace();
                }*/
            } else
                showLongToast(context, "This Device doesn't support NFC!");
        }

        @JavascriptInterface
        public void deleteNFCData(int timeout, String returnMethod) { // json
            returnMethodName = returnMethod;
            nfcTimeout = timeout;
            isReadOrWriteCompleted = false;
            isAllowIntentToReadNfc = true;

            nfcAdapter = NfcAdapter.getDefaultAdapter(Sipdroid.this);

            setupForegroundDispatch(Sipdroid.this, nfcAdapter);

            if (checkNFC() != -1) {
                if (dialogNFC == null)
                    showProgressDialog("", "Scan NFC Card For Write");
                else
                    showProgressDialog("", "Scan NFC Card For Write");
                startNFCHandler(nfcTimeout);
            } else
                showLongToast(context, "This Device doesn't support NFC!");
        }

        /********** NFC Related Bridge calls ***********/

        @JavascriptInterface
        public void startBeacon(String beaconID) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);
            if (!beaconID.isEmpty()) {
                Log.e("Log", "Beacon activity started for UUID : "
                        + beaconID);
                SharedPreferences.Editor editor;
                editor = preferences.edit();
                editor.putString("UUID", beaconID);
                editor.commit();
            }
        }

        @JavascriptInterface
        public void stopBeacon(String beaconID) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);
            if (!beaconID.isEmpty()) {
                Log.e("Log", "Beacon activity started for UUID : "
                        + beaconID);
                SharedPreferences.Editor editor;
                editor = preferences.edit();
                editor.putString("UUID", beaconID);
                editor.commit();
            }
        }

        @JavascriptInterface
        public void setMonitoringPrefs(Boolean monitorGF, Boolean monitorBeacon, Boolean monitorSMS) {
            SharedPreferences preferences = context.getApplicationContext()
                    .getSharedPreferences("CDUCONN", MODE_PRIVATE);

            Log.e("Log", "Geo Fence Monitoring Preferences set to : "
                    + monitorGF);
            Log.e("Log", "Beacon Monitoring Preferences set to : "
                    + monitorBeacon);
            Log.e("Log", " SMS Monitoring Preferences set to : "
                    + monitorSMS);
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            editor.putBoolean("isGeoFenceActive", monitorGF);
            editor.putBoolean("isBeaconActive", monitorBeacon);
            editor.putBoolean("isSMSActive", monitorSMS);
            editor.commit();
        }

        @JavascriptInterface
        public String getDeviceID() {
            System.out
                    .println("getDeviceID is Called!" + iawMain.getDeviceID());
            return iawMain.getDeviceID();
        }

        @JavascriptInterface
        public String getAppID() {
            return iawMain.getAppID();
        }

        @JavascriptInterface
        public int getBatteryStatus() {
            return getBatteryLevel();
        }

        @JavascriptInterface
        public String getRegString() {
            String reg = iawMain.getRegString();
            if (reg.isEmpty()) {
                reg = retrieveRegString();
            }
            printText("getRegString is Called!" + reg);
            return reg;
        }


        @JavascriptInterface
        public String getESO() {
            printText("getESO is Called!" + iawMain.getESO());
            return iawMain.getESO();
        }

        @JavascriptInterface
        public String getLatitude() {
            String latitude = iawMain.getLatitude();
            printText("getLatitude is Called!" + latitude);
            return latitude;
        }

        @JavascriptInterface
        public String getLongitude() {
            String longitude = iawMain.getLongitude();
            printText("getLongitude is Called!" + longitude);
            return longitude;
        }

        @JavascriptInterface
        public void getCurrentLocation(String returnMethod) {
            locationTrack.getCurrentLocation(returnMethod);
        }

        @JavascriptInterface
        public void setState(int appState) {
            Log.e("LOG", "appState: " + appState);
            iawMain.setAppState(appState);
        }

        @JavascriptInterface
        public void setSessionTimeOut(int timeOut) {
            runSessionTimeOutThread(timeOut);
        }

        // Method to check user registration
        @JavascriptInterface
        public void setRegistrationStatus(int status) {
            browser.loadUrl("javascript:RegistrationStatus(" + status + ")");
        }

        @JavascriptInterface
        public void takePicture(String urlToPostImg, String returnMethod) {
            printText("takepicture");
            returnMethodName = returnMethod;
            urlToPost = urlToPostImg;

            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
            }
        }

        @JavascriptInterface
        public void takeImage(String urlToPostImg, String returnMethod) {
            returnMethodName = returnMethod;
            urlToPost = urlToPostImg;
            printText("returnMethodName" + returnMethod);
            selectMedia(1);
        }

        @JavascriptInterface
        public void takeVideo(String urlToPostImg, String returnMethod) {
            returnMethodName = returnMethod;
            urlToPost = urlToPostImg;
            printText("returnMethodName" + returnMethod);
            selectMedia(2);
        }

        @JavascriptInterface
        public void takeAudio(String urlToPostImg, String returnMethod) {
            returnMethodName = returnMethod;
            urlToPost = urlToPostImg;
            printText("returnMethodName" + returnMethod);
            selectMedia(3);
        }

        @JavascriptInterface
        public void takeSignature(String urlToPostSign, String returnSignnatureMethod) {
            returnMethodName = returnSignnatureMethod;
            urlToPost = urlToPostSign;
            Intent openSignatureIntent = new Intent(Sipdroid.this, Capture.class);
            startActivityForResult(openSignatureIntent, 3);
        }

        @JavascriptInterface
        public void openIntent(String url, String type) {
            if (type.equals("browser")) {
                openBrowser(url);
            }
        }

        @JavascriptInterface
        public void pushLocalNtfn(String msg, String srvObjRef) {
            printText("push local notification is called" + msg + srvObjRef);
            sendNotification(msg, srvObjRef);
        }

        @JavascriptInterface
        public void silentMode(boolean isSilent) {
            int currentRingMode = audioManager.getRingerMode();
            if (currentRingMode == AudioManager.RINGER_MODE_NORMAL) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (currentRingMode == AudioManager.RINGER_MODE_SILENT) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        }

        @JavascriptInterface
        public void playVideo(String videoID, String returnMethod,
                              String skipBtnName) {
            printText("playvideoo" + videoID + returnMethod);
            callbackMethodName = returnMethod;
            Intent videointent = new Intent(Sipdroid.this,
                    YoutubePlayerFullScreen.class);
            videointent.putExtra("VIDEO_ID", videoID);
            videointent.putExtra("isFullScreen", true);
            videointent.putExtra("SKIPBTN_NAME", skipBtnName);
            startActivityForResult(videointent, YOU_TUBE_CODE);
        }

        @JavascriptInterface
        public void playVideo(String videoName, String playFrom) {
            Intent videointent = new Intent(Sipdroid.this,
                    VideoPlayer.class);
            videointent.putExtra("playFrom", playFrom);
            videointent.putExtra("Video_Name", videoName);
            startActivity(videointent);
        }

        @JavascriptInterface
        public void playTTS(String ttsText, String speedInStr, String lang) {
            if (!ttsText.isEmpty() && !speedInStr.isEmpty() && !lang.isEmpty()) {
                doPlayTTS(ttsText, speedInStr, lang);
            }
        }

        @JavascriptInterface
        public void playAudio(String playSound, String playFrom) {
            File file = new File(PATH_FOR_AUDIO + playSound);

            String urlToPlay;

            if ((playSound.startsWith("http://")) || (playSound.startsWith("https://")) ) {
                urlToPlay = playSound;
            } else {
                urlToPlay = SERVER_PATH + "images/" + playSound;
            }

            try {
                if (mPlayer == null) {
                    mPlayer = new MediaPlayer();
                }
                if (isAudioPlaying == 0) {
                    if (playFrom.equals("local")) {
                        Uri myUri1 = Uri.fromFile(file);
                        //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mPlayer.setDataSource(getApplicationContext(), myUri1);
                        } catch (IllegalArgumentException e) {
                        }
                    } else if (playFrom.equals("remote")) {
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mPlayer.setDataSource(urlToPlay);
                        } catch (IllegalArgumentException e) {
                        }
                    }
                    mPlayer.prepare();
                    if (!mPlayer.isPlaying()) {
                        try {
                            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                public void onPrepared(MediaPlayer mp) {
                                    mPlayer.start();
                                    isAudioPlaying++;
                                }
                            });
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e("LOG", "MediaPlayer stopped!!!");
                    mPlayer.stop();
                    mPlayer = null;
                    isAudioPlaying = 0;
                }
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            mp.stop();
                            mPlayer = null;
                            isAudioPlaying = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void pauseAndResume(boolean playSound) {
            Log.e("LOG", "pauseAndResume is Called: " + playSound);
            if (mPlayer.isPlaying())
                if (playSound) {
                    try {
                        mPlayer.pause();
                        audioLength = mPlayer.getCurrentPosition();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    mPlayer.seekTo(audioLength);
                    mPlayer.start();
                }
            else
                Log.e("LOG", "MediaPlayer is NOT PLAYING!!!");
        }

        @JavascriptInterface
        public void playSound(boolean playSound) {
            Log.e("LOG", "playSound is Called: " + playSound);
            raiseVolume();

            if (playSound) {
                mPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.ambulance_small);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (!mPlayer.isPlaying())
                    try {
                        mPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
            } else {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    Log.e("LOG", "MediaPlayer pause!!!");
                    mPlayer.stop();
                } else
                    Log.e("LOG", "MediaPlayer is NOT PLAYING!!!");
            }
        }

        @JavascriptInterface
        public void playSound(boolean playSound, String returnMethod) {
            Log.e("LOG", "playSound is Called: " + playSound);
            //raiseVolume();
            playLocalAudio(playSound);
            callbackMethodName = returnMethod;
        }

        @JavascriptInterface
        public void startGeofence(String geofenceID, String latitude, String longitude, String radius) {
            populateGeofenceList(geofenceID, latitude, longitude, radius);
            // Kick off the request to build GoogleApiC13.032849", "80.2430675lient.
            buildGoogleApiClient();
        }

        @JavascriptInterface
        public void setFrequency(String frequency, String accuracy, String callbackMethod) {
            printText("setLocTrkg is Called!" +  frequency+":"+ callbackMethod);
            Log.d("setLocTrkg is Called!:","setLocTrakg is Called!:"+ frequency+": "+callbackMethod);
            int accuracyInt = 0;
//            long interval = Long.parseLong(frequency);
            long interval = 10000;  // 60000
            Log.d("interval:","interval:"+interval);
            if (frequency.equals("-1")) {
                locationTrack.stopTracking();
                locUpdateFrequency = -1;
//                locFlag = false;

                Log.d("intervalnew1:","intervalnew1:"+interval+frequency);

            } else {
                Log.d("intervalnew11:","intervalnew11:"+interval+frequency);

                if (locUpdateFrequency != 0 && locUpdateFrequency != interval && locUpdateFrequency != -1) {
                    locationTrack.stopTracking();
                }
                locUpdateFrequency = interval;
                if (accuracy != null && !accuracy.isEmpty()) {
                    accuracyInt = Integer.parseInt(accuracy);
                }
//                locationTrack.startTracking(Long.parseLong(frequency),accuracyInt);
//                locFlag=true;
//                sleepInterval=Long.parseLong(frequency);
//                new locationUpdate().execute();

                locationTrack.startTracking(interval, accuracyInt, callbackMethod);


            }
        }

        @JavascriptInterface
        public void setLocTrkg(String type, String frequency, String callbackMethod) {
            printText("setLocTrkg is Called!" + type+ ":" + frequency+":"+ callbackMethod);
            int trkgType = Integer.parseInt(type);
            switch (trkgType) {
                case -1:
                    setFrequency("-1", null, callbackMethod);
                    break;
                case 0:
                    getCurrentLocation(callbackMethod);
                    break;
                case 1:
//                    setFrequency(frequency, null, callbackMethod);
                    setFrequency("10000", null, callbackMethod);
                    break;
                case 2:
//                    setFrequency(frequency, 102 + "", callbackMethod);
                    setFrequency("10000", 102 + "", callbackMethod);
                    break;
            }
        }

        @SuppressLint("LongLogTag")
        @JavascriptInterface
        public void stopGeofence(String geofenceID) {
            if (!mGoogleApiClient.isConnected()) {
                return;
            }
            try {
                ArrayList<String> geofencIds = new ArrayList<String>();
                geofencIds.add(geofenceID);

                LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, geofencIds)
                        .setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    // Remove notifiation here
                                    printText("success");
                                }
                            }
                        });
            } catch (SecurityException securityException) {
                Log.i("Exception on stopping geofence", securityException.toString());
            }
        }
    }

    private void pubNubPublishMethod(String userId,String publishKey,String subscribeKey) {


        PNConfiguration pnConfiguration = new PNConfiguration();

        //old code
//            pnConfiguration.setSubscribeKey("sub-c-add539c0-88d5-11eb-99bb-ce4b510ebf19");
//            pnConfiguration.setPublishKey("pub-c-c47ce208-b259-43ee-8c55-94c4ec0beee9");
//            pnConfiguration.setUuid("carc1");

        //new code
        pnConfiguration.setSubscribeKey(subscribeKey);
        pnConfiguration.setPublishKey(publishKey);
//        pnConfiguration.setUuid(userId);
        pnConfiguration.setUuid("carc1");

        PubNub pubnub = new PubNub(pnConfiguration);


//        final String channelName = userId;
        final String channelName = "carc1";

        // create message payload using Gson
        final JsonObject messageJsonObject = new JsonObject();
//        messageJsonObject.addProperty("userId", userId);
//        messageJsonObject.addProperty("publishKey", publishKey);
//        messageJsonObject.addProperty("subscribeKey", subscribeKey);

        float latflo = Float.parseFloat(MainActivity.iawMain.getLatitude());
        float lngflo = Float.parseFloat(MainActivity.iawMain.getLongitude());

        messageJsonObject.addProperty("lat", latflo);
        messageJsonObject.addProperty("lng", lngflo);

//        Log.d("Message to send: ","Message to send: " + messageJsonObject.toString());
        Log.d("javascript:onLocationUpdate4:","javascript:onLocationUpdate4:pubnub " + messageJsonObject.toString());



        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (status.isError()) {

//                                    Toast.makeText(Sipdroid.this, "Data not added in PubNub", Toast.LENGTH_SHORT).show();
                                    Log.d("Data_not_added:","Data_not_added:");

                                    // Message successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

//                                    Toast.makeText(Sipdroid.this, "Data successfully added in PubNub", Toast.LENGTH_SHORT).show();
                                    Log.d("Data_added:","Data_added:");


                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                JsonElement receivedMessageObject = message.getMessage();
                Log.d("Receivedmessage content:","Received message content: " + receivedMessageObject.toString());
                // extract desired parts of the payload, using Gson
//                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
//                Log.d("msg content: ","msg content: " + msg);

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }

            @Override
            public void signal(PubNub pubnub, PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(PubNub pubnub, PNMessageActionResult pnMessageActionResult) {

            }

            @Override
            public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList(channelName)).execute();


    }

    /********** NFC Related Methods ***********/

    public void startNFCHandler(int timeout) {
        nfcHandler.postDelayed(nfcRunnable = new Runnable() {
            @Override
            public void run() {
                if (nfcAdapter != null) {
                    checkSpinner(dialog);
                    checkSpinner(dialogNFC);
                    if (iawMain.getAppStatus()) {
                        stopForegroundDispatch(Sipdroid.this, nfcAdapter);
                    }
                    if (!isReadOrWriteCompleted) {
                        loadURL("javascript:" + returnMethodName + "('failure'" + "," + "'Failed due to timeout')");
                    }
                    makeNFCHandlersNull();
                }
            }
        }, timeout);
    }

    public void makeNFCHandlersNull() {
        nfcAdapter = null;
        nfcHandler.removeCallbacks(nfcRunnable);
        isAllowIntentToReadNfc = false;
        isReadOrWriteCompleted = true;
        tagTypeToRead = NFCDataType.NONE.toString();
        checkSpinner(dialog);
        checkSpinner(dialogNFC);
    }

    public void showProgressDialog(String title, String messageToDisplay) {
        dialogNFC = new ProgressDialog(Sipdroid.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialogNFC.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogNFC.setTitle(title);
        dialogNFC.setCancelable(false);
        dialogNFC.setMessage(messageToDisplay);
        dialogNFC.show();
    }

    private void enableNFC() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(Sipdroid.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertbox.setTitle("Info");
        alertbox.setMessage("NFC is Disabled, Please enable it.");
        alertbox.setPositiveButton("Turn On",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("InlinedApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Intent intent = new Intent(
                                    Settings.ACTION_NFC_SETTINGS);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }
                });
        alertbox.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertbox.show();
    }

    private int checkNFC() {
        int nfcState = 0;
        if (nfcAdapter == null)
            nfcState = -1;
        if (nfcAdapter != null)
            if (nfcAdapter.isEnabled())
                nfcState = 0;
            else
                nfcState = 1;

        return nfcState;
    }

    private void processWriteNFC(String whatJSON) {
        if (nfcTag != null) {
            ndef = Ndef.get(nfcTag);
            if (ndef.isConnected()) {
                write(whatJSON);
            } else {
                // checkSpinner(dialogNFC);
                // dialogNFC = ProgressDialog.show(HomeWebview.this, "",
                // "Scan NFC Card", true);
            }
        } else {
            checkSpinner(dialogNFC);
            dialogNFC = ProgressDialog.show(Sipdroid.this, "",
                    "Scan NFC Card", true);
        }
    }

    private void write(String whatJSON) {
        //NdefRecord[] recordsToWrite = { createNdefMessage(whatJSON) };
        NdefMessage ndefMsg = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            NdefRecord recordsToWrite = NdefRecord.createTextRecord("en", whatJSON);
            ndefMsg = new NdefMessage(recordsToWrite);
        } else {
            NdefRecord[] recordsToWrite = {createNdefMessage(whatJSON)};
            ndefMsg = new NdefMessage(recordsToWrite);
        }

        try {
            if (ndefMsg.toByteArray().length < ndef.getMaxSize()) {
                ndef.connect();
                ndef.writeNdefMessage(ndefMsg);
                ndef.close();
                // showAlertBox(context, "NFC Alert",
                // "Writing finished successfully!", true, false);
                if (isLockNFC)
                    if (ndef.makeReadOnly()) {
                        showShortToast(context, "NFC Tag is Locked");
                    } else
                        showShortToast(context, "Error in Locking NFC Tag");

                checkSpinner(dialogNFC);
                //loadURL("javascript:doNFCWriteSuccess()");
                loadURL("javascript:" + returnMethodName + "('success'" + "," + "'Writing finished successfully')");
            } else {
                checkSpinner(dialogNFC);
                loadURL("javascript:" + returnMethodName + "('failure'" + "," + "'Data exceeds the max tag size')");
            }
        } catch (FormatException e) {
            checkSpinner(dialogNFC);
            // showAlertBox(context, "Error", "Error in writing NFC Tag", true,
            // false);
            //loadURL("javascript:doNFCWriteFailure()");
            loadURL("javascript:" + returnMethodName + "('failure'" + "," + "'Error in writing NFC Tag')");
            e.printStackTrace();
        } catch (IOException e) {
            checkSpinner(dialogNFC);
            // showAlertBox(context, "Error", "Error in writing NFC Tag", true,
            // false);
            //loadURL("javascript:doNFCWriteFailure()");
            loadURL("javascript:" + returnMethodName + "('failure'" + "," + "'Error in writing NFC Tag')");
            e.printStackTrace();
        }
        makeNFCHandlersNull();
    }

    private NdefRecord createNdefMessage(String dataToWrite) {
        String language = "en";
        byte[] textBytes = dataToWrite.getBytes();
        NdefRecord ndefRecord = null;
        byte[] langBytes = language.getBytes(StandardCharsets.US_ASCII);
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        byte[] textType;
        if (dataToWrite.contains("https") || dataToWrite.contains("http"))
            textType = NdefRecord.RTD_URI;
        else
            textType = NdefRecord.RTD_TEXT;

        ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, textType,
                new byte[0], payload);
        return ndefRecord;
    }

    private void eraseNFCData(Tag tag) {
        try {
            Ndef ndefTag = Ndef.get(tag);
            ndefTag.connect();
            ndefTag.writeNdefMessage(new NdefMessage(new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)));
            ndefTag.close();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private String readText(NdefRecord record)
            throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 0063;

        return new String(payload, languageCodeLength + 1, payload.length
                - languageCodeLength - 1, textEncoding);
    }

    @SuppressLint("NewApi")
    private void handleIntent(Intent newIntent) {
        if (isAllowIntentToReadNfc) {
            if (newIntent != null) {
                String action = newIntent.getAction();
                if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                    Tag tag = newIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    nfcTag = tag;
                    StringBuilder strBuilder = new StringBuilder();
                    Ndef ndefTagInfo = getNdefTagInfo(tag);

                    byte[] id = tag.getId();
                    if (id != null) {
                        nfcId = getDec(id);
                        int tagSize = ndefTagInfo.getMaxSize();
                        boolean isTagWritable = ndefTagInfo.isWritable();
                        // strBuilder.append("DEC ID: " + nfcId + "\n");
                        // editTextNFC.setText("" + nfcId);
                        // editTextNFC.setEnabled(false);
                        // TextareaeditText.setText("" + nfcId);

                        // processWriteNFC();
                        // Calling NFC Write JS Function

                        if (isNFCIdRead) {
                            loadURL("javascript:" + returnMethodName + "('" + nfcId + "')");
                        } else {
                            JSONObject jsonObj = new JSONObject();
                            JSONObject jsonToPost = new JSONObject();
                            String nfcContent = readFromNFCTag(tag);
                            printText("nfcContent: " + nfcContent);
                            if (nfcContent == null) {
                                readedTagType = NFCDataType.NONE.toString();
                                try {
                                    jsonToPost.put("tagID", nfcId);
                                    jsonToPost.put("Data", "");
//                                jsonObj.put("name", ""); // Dhivya
//                                jsonObj.put("villa_no", ""); // 8970
                                    // nfcContent = jsonObj.toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // strBuilder.append("{");
                                // strBuilder.append("id");
                                // strBuilder.append(":");
                                // strBuilder.append(nfcId);
                                // strBuilder.append("}");
                                // nfcContent = strBuilder.toString();
                            } else {
                                try {
                                    String tagId = "";
                                    String data = "";
                                    jsonObj = new JSONObject(nfcContent);
                                    JSONObject jsonData = jsonObj;
                                    JSONObject content = new JSONObject(nfcContent);
                                    if (jsonObj.has("tagID")) {
                                        tagId = jsonObj.getString("tagID");
                                    }
                                    if (jsonObj.has("Data")) {
                                        data = jsonObj.getString("Data");
                                    }
                                    if (tagId == null || tagId.isEmpty()) {
                                        jsonToPost.put("tagID", nfcId);
                                        jsonToPost.put("Data", content);
                                    } else if ((tagId != null || !tagId.isEmpty()) && (data == null || data.isEmpty())) {
                                        jsonToPost.put("tagID", nfcId);
                                        jsonToPost.put("Data", content);
                                    } else if ((tagId != null || !tagId.isEmpty()) && (data != null || !data.isEmpty())) {
                                        jsonToPost.put("tagID", nfcId);
                                        jsonToPost.put("Data", jsonData.get("Data"));
                                    }
                                    jsonToPost.put("tagSize", tagSize);
                                    jsonToPost.put("isWritable", isTagWritable);
                                } catch (JSONException e) {
                                    try {
                                        jsonToPost.put("tagID", nfcId);
                                        jsonToPost.put("Data", nfcContent);
                                        jsonToPost.put("tagSize", tagSize);
                                        jsonToPost.put("isWritable", isTagWritable);
                                    } catch (JSONException e1) {
                                        e.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }

                                // String[] nfcContents = nfcContent.split(",");
                                // if (nfcContents.length > 0) {
                                // name = nfcContents[1];
                                // villaNo = nfcContents[2];
                                // }
                            }

                            checkSpinner(dialogNFC);
                            // villaNo = villaNo.trim();
                            if (isWrite) {
                                printText("Came to  isWrite :" + isWrite
                                        + " , " + dataToWrite);
                                write(dataToWrite);
                            } else {
                                // webView.loadUrl("javascript:onNFCEnd('" + nfcId +
                                // "','"
                                // + name + "','" + villaNo + "')");
                                if (tagTypeToRead.equals(readedTagType) || tagTypeToRead.equals(NFCDataType.NONE.toString()) || readedTagType.equals(NFCDataType.NONE.toString())) {
                                    loadURL("javascript:" + returnMethodName + "('" + jsonToPost + "'" + "," + "'read successful')");
                                } else {
                                    loadURL("javascript:" + returnMethodName + "('failure'" + "," + "'Tag type mismatch error')");
                                }
//                            loadURL("javascript:onNFCEnd('" + jsonObj
//                                    + "')"); // nfcContent
                                printText("nfcContent: " + nfcContent);
                                makeNFCHandlersNull();
                            }
                        }
                    }
                }
            }
        }
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }

    private Ndef getNdefTagInfo(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }
        return ndef;
    }

    private String readTag(Tag tagToRead) {
        StringBuilder allRecord = new StringBuilder();

        Ndef ndef = Ndef.get(tagToRead);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        int length = records.length;
        printText("RECORD LENGTH: " + length);
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
                    && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    readedTagType = NFCDataType.TEXTTYPE.toString();
                    //allRecord.append(readText(ndefRecord) + "\n");
                    allRecord.append(readText(ndefRecord));
                } catch (UnsupportedEncodingException e) {
                    Log.e("LOG", "Unsupported Encoding", e);
                }
            } else if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
                    && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI)) {
                try {
                    readedTagType = NFCDataType.URL.toString();
                    //allRecord.append(readText(ndefRecord) + "\n");
                    allRecord.append(readText(ndefRecord));
                } catch (UnsupportedEncodingException e) {
                    Log.e("LOG", "Unsupported Encoding", e);
                }
            }
        }
        return allRecord.toString();
    }

    private String readFromNFCTag(Tag readableTag) {
        Intent intentGot = getIntent();
        String nfcContent = null;
        String action = intentGot.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcContent = readTag(tag);

            // if (nfcContent != null)
            // showAlertBox(context, "NFC Data", nfcContent, true, false);
            // new NdefReaderTask().execute(tag);
        }
        return nfcContent;
    }

    private void setupForegroundDispatch(Activity activity,
                                         NfcAdapter nfcAdapter) {
        Intent intent = new Intent(activity.getApplicationContext(),
                activity.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pandingIntent = PendingIntent.getActivity(
                activity.getApplicationContext(), 0, intent, 0);
        nfcAdapter
                .enableForegroundDispatch(activity, pandingIntent, null, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Toast.makeText(getApplicationContext(), "New NFC Tag Found!",
        // Toast.LENGTH_SHORT).show();
        setIntent(intent);
        handleIntent(intent);
    }

    private void stopForegroundDispatch(Activity activity, NfcAdapter nfcAdapter) {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    /********** NFC Related Methods ***********/


    public void sendNotification(String payloadMSG, String srvObjRef) {
        NotificationManager mNotificationManager = null;
        Notification notification;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.mipmap.icon;
        long when = System.currentTimeMillis();
        int id = (int) (long) when;
        Intent notificationIntent = null;


        notificationIntent = new Intent(this,
                MainAlertScreen.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("push_msg", payloadMSG);
        notificationIntent.putExtra("srvObjectReference", srvObjRef);
        Log.e("srvObjectReference", "srvObjectReference...  "
                + srvObjRef);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, id, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker(payloadMSG);
        builder.setContentTitle(payloadMSG);
        builder.setContentText(payloadMSG);
        builder.setSmallIcon(icon);
        builder.setContentIntent(contentIntent);
        builder.build();
        notification = builder.getNotification();
        notification.flags = Notification.DEFAULT_LIGHTS
                | Notification.FLAG_AUTO_CANCEL;

        printText("NOTIFICATION ID :" + id);
        mNotificationManager.notify(id, notification);
    }

    public static void loadURL(final String urlToLoad) {
        //App Crash fix when loading page as null value
        if (urlToLoad != null && !urlToLoad.isEmpty() && !urlToLoad.equals("null")) {
            //            if (!urlToLoad.isEmpty()) {
            if (browser != null)
                browser.post(new Runnable() {
                    public void run() {
                        System.out.println("Load url" + urlToLoad);
                        Log.d("Load url888:","Load url888:" + urlToLoad);
                        browser.loadUrl(urlToLoad);
                    }
                });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            boolean isAppNormal = preferences.getBoolean("FLOATVIEW", false);
            Log.e("isAppNormal", "" + isAppNormal);

            if (isAppNormal) {
                printText("backpress to float stopped");
                onBackPressed();
            } else {
                onBackPressed();
                printText("backpress to float started");
            }
        } else if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            boolean isAppNormal = preferences.getBoolean("FLOATVIEW", false);
            Log.e("isAppNormal", "" + isAppNormal);

            if (isAppNormal) {
                printText("home button pressed float stopped");

            } else {
                printText("home button pressed float started");
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        boolean isbrowser_back = preferences.getBoolean("BROWSER_BACK", false);
        boolean isBack_Btn = preferences.getBoolean("BACK_BTN", false);
        if (isbrowser_back) {
            printText("browser back selected");
            browser.goBack();

        } else if (isBack_Btn) {
            printText("Back is disabled");

        } else {
            printText("Back is Enabled");
            Intent goBack = new Intent(Intent.ACTION_MAIN);
            goBack.addCategory(Intent.CATEGORY_HOME);
            startActivity(goBack);
        }


//        Toast.makeText(this, "Bye Bye-onBackPressed", Toast.LENGTH_SHORT).show();

//        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            boolean shouldProvideRationale =
//                    ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//
//            if(shouldProvideRationale) {

                    Intent mIntent = new Intent(Sipdroid.this, SelectLocPermission.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
//                }

                Sipdroid.this.finish();
            }

//        }catch (NullPointerException e){
//
//        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            Intent mIntent = new Intent(Sipdroid.this, SelectLocPermission.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(mIntent);
//        }
//
//        Sipdroid.this.finish();

//        try {
//            boolean backgroundone = ActivityCompat.checkSelfPermission(Sipdroid.this,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
//            Toast.makeText(mInstanceActivity, "backgournd2: " +backgroundone, Toast.LENGTH_SHORT).show();
//        }catch (NullPointerException e){
//            Toast.makeText(mInstanceActivity, "backgournd2: Null Exception", Toast.LENGTH_SHORT).show();
//        }








    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            if (account != null && account.getIdToken() != null) {
                //loadURL("javascript:" + returnMethodName + "('" + account.getIdToken() + "')");
                requestAccessToken(account);
            } else {
                loadURL("javascript:" + returnMethodName + "('')");
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void requestAccessToken(GoogleSignInAccount googleAccount) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", SERVER_CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("redirect_uri", "")
                .add("code", Objects.requireNonNull(googleAccount.getServerAuthCode()))
                .build();
        final Request request = new Request.Builder()
                .url(OAUTH_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String message = jsonObject.toString(5);
                    Log.i(TAG, message);
                    loadURL("javascript:" + returnMethodName + "('" + jsonObject.getString("access_token") + "')");
                    printText(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "onActivityResult: .");
        String destFileName = "";
        switch (requestCode) {
            case RESULT_OK:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String message = scanResult.getContents();
                Sipdroid.loadURL("javascript:" + returnMethodName + "('" + message + "')");
                break;
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            case IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    destFileName = "PNG_" + getTimeStamp() + ".png";//fileName on server
                    String srcFileName = ""; //fileName in local system
                    Bundle extras = data.getExtras();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    Bitmap thumbnail = (Bitmap) extras.get("data");
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
                    imageAsBase64 = helper.encode(thumbnail);
                    if (imageAsBase64 != null) {
                        new postData().execute(imageAsBase64, "georgeclooney", "MyGallery");
                    } else {
                        showLongToast(context, "No base64 data found!");
                    }
//                File destination = new File(Environment.getExternalStorageDirectory()
//                        + File.separator + APP_NAME + "/Images");
//                FileOutputStream fos;
//                try {
//                    if (!destination.exists()) {
//                        if (!destination.mkdir())
//                            Log.e("LOG", "Failed to make Directory!");
//                    }
//                    srcFileName = destFileName; //using generated fileName to save in temp loc
//
//                    File newFile = new File(destination, srcFileName);//save local
//                    fos = new FileOutputStream(newFile);
//                    fos.write(bytes.toByteArray());
//                    fos.close();
//                    filePath = newFile.getAbsolutePath(); // local (source file) file loc path;
//                    printText();("File Path: " + filePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.e("Log", filePath);
//                imageAsBase64 = helper.encode(thumbnail);
//                if(imageAsBase64 != null) {
//                    new postData().execute(imageAsBase64,"georgeclooney","MyGallery");
//                }
//                else{
//                    Toast.makeText(Sipdroid.this,"No base64 data found!",Toast.LENGTH_LONG).show();
//                }

                    //postMediaToServer(filePath, destFileName);
                }
                break;
            case IMAGE_FROM_GALLERY:
                destFileName = "PNG_" + getTimeStamp() + ".png";//fileName on server
                final Uri imageUri = data.getData();
                filePath = RealPathUtil.getPath(context, imageUri);
                postMediaToServer(filePath, destFileName);
                break;
            case YOU_TUBE_CODE:
                if (resultCode == RESULT_OK) {
                    if (null != data) {
                        String url = "javascript:" + callbackMethodName + "()";
                        Log.e("LOG", "URL: " + url);
                        browser.loadUrl(url);
                    }
                }
                break;
            case SIGNATURE_CODE:
                if (resultCode == RESULT_OK) {
                    destFileName = "IMAGE_" + getTimeStamp() + ".png";
                    filePath = data.getStringExtra("imagePath");
                    printText("imagefilepath: " + filePath);
                    postMediaToServer(filePath, destFileName);
                }
                break;
            case AUDIO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    filePath = data.getStringExtra("audioPath");
                    destFileName = "AUDIO_" + getTimeStamp() + ".mp3";
                    postMediaToServer(filePath, destFileName);
                }
                break;
            case AUDIO_FROM_GALLERY:
                final Uri audioUri = data.getData();
                filePath = RealPathUtil.getPath(context, audioUri);
                destFileName = "AUDIO_" + getTimeStamp() + ".mp3";
                postMediaToServer(filePath, destFileName);
                break;
            case VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    if (!videoFilePath.isEmpty()) {
                        filePath = videoFilePath;
                        destFileName = "VIDEO_" + getTimeStamp() + ".mp4";
                        postMediaToServer(filePath, destFileName);
                    }
                }
                break;
            case VIDEO_FROM_GALLERY:
                final Uri videoUri = data.getData();
                filePath = RealPathUtil.getPath(context, videoUri);
                destFileName = "VIDEO_" + getTimeStamp() + ".mp4";
                postMediaToServer(filePath, destFileName);
                break;
        }
    }

    private void postMediaToServer(String filePath, String destFileName) {
        fileName = destFileName;
        Thread postThread = new Thread(postRunnable);
        postThread.start();
    }

    Runnable postRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                responseValue = postMedia();
                printText("responseValue: " + responseValue);
                Log.e("responseValue", responseValue);
                postHandler.sendEmptyMessage(0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (responseValue != null)
                if (!responseValue.isEmpty()) {
                    // String methodCall = "javascript:" + returnMathodName +
                    // "('"
                    // + fileName + "', '" + responseValue + "')"; // statusCode

                    String methodCall = "javascript:" + returnMethodName + "('"
                            + fileName + "', 'SUCCESS')";

                    Log.e("LOG", "Method Call: " + methodCall);
                    //
                    browser.loadUrl(methodCall);
                }
        }
    };


    @SuppressLint("SimpleDateFormat")
    public String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
    }

    protected String postMedia() {
        String responseString = null;
        DefaultHttpClient httpClient;
        HttpPost httpPost;
        org.apache.http.entity.mime.MultipartEntity multipartEntity;
        File fileToPost;
        HttpResponse httpResponse;
        HttpEntity responseEntity;
        HttpParams params = new BasicHttpParams();

        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.USER_AGENT,
                "Android Mobile Application");
        try {
            httpClient = new DefaultHttpClient(params);
            if (!fileName.isEmpty())
                urlToPost = urlToPost + "?fileName=" + fileName;
            Log.e("LOG", "urlToPost with FileName: " + urlToPost);

            httpPost = new HttpPost(urlToPost);
            Log.e("urlToPost", urlToPost);
            multipartEntity = new org.apache.http.entity.mime.MultipartEntity(
                    org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE);
            fileToPost = new File(filePath);
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                multipartEntity.addPart("image", new FileBody(fileToPost,
                        "image/png"));
            } else if (fileName.endsWith(".mp4")) {
                multipartEntity.addPart("video", new FileBody(fileToPost,
                        "image/mp4"));
            } else if (fileName.endsWith(".mp3")) {
                multipartEntity.addPart("audio", new FileBody(fileToPost,
                        "image/mp3"));
            }
            httpPost.setEntity(multipartEntity);
            httpResponse = httpClient.execute(httpPost);
            responseEntity = httpResponse.getEntity();
            statusCode = httpResponse.getStatusLine().getStatusCode();
            Log.e("statusCode", "" + statusCode);
            responseString = EntityUtils.toString(responseEntity);
            Log.e("RESULT", responseString);
            return responseString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }


    @Override
    protected void onResume() {
        super.onResume();
        CampusDisclosureApp.getInstance().setConnectivityListener(this);

        //audio player handling
        if (audioLength >= 1) {
            try {
                mPlayer.seekTo(audioLength);
                mPlayer.start();
                audioLength = 0;

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        //nfc handling
        if (nfcAdapter != null)
            setupForegroundDispatch(Sipdroid.this, nfcAdapter);
        else
            printText("NFC Adapter is null in onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //audio player handling
        if (mPlayer != null && mPlayer.isPlaying()) {
            Log.e("LOG", "MediaPlayer pause!!!");
            mPlayer.pause();
            audioLength = mPlayer.getCurrentPosition();
        } else {
            Log.e("LOG", "MediaPlayer is NOT PLAYING!!!");
        }

        //nfc handling
        if (nfcAdapter != null)
            stopForegroundDispatch(Sipdroid.this, nfcAdapter);
        else
            printText("NFC Adapter is null in onPause()");
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected, true);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
//        mixpanel.flush();

        Toast.makeText(mInstanceActivity, "Destroy", Toast.LENGTH_SHORT).show();


//        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                boolean shouldProvideRationale =
//                        ActivityCompat.shouldShowRequestPermissionRationale(this,
//                                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//
//                if(shouldProvideRationale) {

                    Intent mIntent = new Intent(Sipdroid.this, SelectLocPermission.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
//                }

                Sipdroid.this.finish();
            }

//        }catch (NullPointerException e){
//
//        }

    }

    enum NFCDataType {
        TEXTTYPE, URL, NONE
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        Toast.makeText(this, "Bye Bye-onSaveInstanceState", Toast.LENGTH_SHORT).show();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            Intent mIntent = new Intent(Sipdroid.this, SelectLocPermission.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(mIntent);
//        }

//        Sipdroid.this.finish();

    }



}
