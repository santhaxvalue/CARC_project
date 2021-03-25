package com.sd.carc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;


public class PUMWebviewActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, HomeBroadcast.ConnectivityReceiverListener {
    public static WebView browser;
    String returnMethodName;
    String buttonVisibility;
    private Context context;
    boolean isLoaded = false;
    boolean isPageLoadFailure = false;
    private String urlToLoadNewWebview;
    private Toolbar toolbar;
    private ImageButton closeButton;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }

        setContentView(R.layout.activity_webview);
        context = getApplicationContext();
        browser = findViewById(R.id.activity_main_webview);
        toolbar = findViewById(R.id.tool_bar);
        closeButton= findViewById(R.id.close_btn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        browser.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isPageLoadFailure = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                browser.setVisibility(View.VISIBLE);
                if (!isPageLoadFailure) {
                    isLoaded = true;
                    checkConnection(true);
                } else {
                    isLoaded = false;
                }
            }

            //@SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(final WebView view, int errorCode, String description,
                                        final String failingUrl) {
                isLoaded = false;
                isPageLoadFailure = true;
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("about:blank");
                checkConnection(true);
                //AlertBox.showPageErrorAlert(PUMWebviewActivity.this).show();
            }

//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                view.loadUrl("about:blank");
//                AlertBox.showPageErrorAlert(PUMWebviewActivity.this).show();
//            }
//
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                view.loadUrl("about:blank");
//                AlertBox.showPageErrorAlert(PUMWebviewActivity.this).show();
//            }
        });
        browser.setHorizontalScrollBarEnabled(true);
        browser.setVerticalScrollBarEnabled(true);

        browser.addJavascriptInterface(new WebAppInterface(this), "Android");
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        browser.getSettings().setDomStorageEnabled(true);
        browser.clearHistory();
        browser.clearCache(true);
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //browser.getSettings().setSupportZoom(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.setInitialScale(1);
        browser.getSettings().setLoadWithOverviewMode(true);

        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setDisplayZoomControls(false);
        browser.getSettings().setSupportZoom(false);

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

        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            urlToLoadNewWebview = bundle.getString("urlToLoadWebview");
            returnMethodName = bundle.getString("retunMethodName");
            buttonVisibility = bundle.getString("buttonVisibility");
            splashHandler.sendEmptyMessage(0);
        } else {
            Toast.makeText(context, "Something went wrong, Try again.", Toast.LENGTH_LONG).show();
        }

        if(buttonVisibility.equals("true")){
            closeButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Sipdroid.webviewResponse(returnMethodName,"closed");
                    finish();
                }

            });
        }else{
            toolbar.setVisibility(View.GONE);
            closeButton.setVisibility(View.GONE);
        }
    }

    private void checkConnection(boolean giveAccessToLoadWebview) {
        boolean isConnected = HomeBroadcast.isConnected();
        showSnack(isConnected, giveAccessToLoadWebview);
    }

    private void showSnack(boolean isConnected, boolean giveAccessToLoadWebview) {
        if (isConnected) {
            if (giveAccessToLoadWebview) {
                if (!isLoaded) {
                    splashHandler.sendEmptyMessage(0);
                }
            }
        } else {
            showAlert(this).show();
        }
    }

    public AlertDialog.Builder showAlert(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("No Internet Connection");
        builder.setMessage("check your mobile data or wifi!!");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnection(false);
            }
        });
        return builder;
    }


    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!urlToLoadNewWebview.isEmpty()) {
                        browser.loadUrl(urlToLoadNewWebview);
                    }
                    break;
            }
        }
    };

    public void runSessionTimeOutThread(int timeOut) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                browser.loadUrl("javascript:sessionClosed()");
            }
        }, timeOut);
    }

    public class WebAppInterface {
        Context mContext;

        public WebAppInterface(Context c) {
            mContext = c;
        }

        public WebAppInterface() {
        }

        @JavascriptInterface
        public void closeWebview(String returnValue) {
            Sipdroid.paymentResponse(returnMethodName,returnValue);
            finish();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        CampusDisclosureApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}