package com.sd.carc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class SelectLocPermission extends AppCompatActivity {

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_loc_permission);

        builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Showing Popup Correctly onBackPressed")
                .setCancelable(false)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

//                        Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();

                        SelectLocPermission.this.finish();

                        openPermissionSettings(SelectLocPermission.this);



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        SelectLocPermission.this.finish();
                    }
                });

        builder.show();

    }

    public void openPermissionSettings(Activity activity) {
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS,
//                Uri.parse("package:" + activity.getPackageName()));
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        activity.startActivity(intent);
//        activity.startActivity(intent);

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

    }

}