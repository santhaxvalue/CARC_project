package utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.AppConstants;

/**
 * Created by linuxuser on 20/6/17.
 */

public class Downloader extends AsyncTask<String, String, String> implements AppConstants {

    String urlToDownload;
    String fileName;

    public Downloader(String urlToDownload, String fileName) {
        this.urlToDownload = urlToDownload;
        this.fileName = fileName;
    }

    @Override
    protected String doInBackground(String... fileURL) {
        File destination = null;
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            if (urlToDownload.endsWith(".mp4")) {
                //fileName = "Video_" + timeStamp + ".mp4";
                destination = new File(Environment.getExternalStorageDirectory()
                        + File.separator + APP_NAME + "/Videos");
            } else if (urlToDownload.endsWith(".png")) {
                destination = new File(Environment.getExternalStorageDirectory()
                        + File.separator + APP_NAME + "/Images");
            } else if (urlToDownload.endsWith(".mp3") || urlToDownload.endsWith(".m4a")) {
                destination = new File(Environment.getExternalStorageDirectory()
                        + File.separator + APP_NAME + "/Audios");
            } else if (urlToDownload.endsWith(".json")) {
                destination = new File(Environment.getExternalStorageDirectory()
                        + File.separator + APP_NAME + "/Data");
            }

            URL url = new URL(urlToDownload);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            FileOutputStream outputStream;
            try {
                if (!destination.exists()) {
                    if (!destination.mkdir())
                        Log.e("LOG", "Failed to make Directory!");
                }
                File newFile = new File(destination, fileName);
                if(!newFile.exists()) {
                    outputStream = new FileOutputStream(newFile);
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len1);
                    }
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }
}
