package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sd.carc.Sipdroid;

/**
 * Created by ramsrini on 02/03/18.
 */

public class AlertBox {
    public static AlertDialog.Builder showPageErrorAlert(Context ctx, final String urlToLoad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("Service Unavailable");
        builder.setMessage("Please try after sometime!!!");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sipdroid.loadURL(urlToLoad);
            }
        });
        return builder;
    }
}
