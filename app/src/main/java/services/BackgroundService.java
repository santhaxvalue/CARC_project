package services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import constants.AppConstants;

/**
 * Created by linuxuser on 20/6/17.
 */

public class BackgroundService extends AsyncTask<String, String, String> implements AppConstants {

    String url;

    public BackgroundService(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... fileURL) {
        try {
            String inputLine;

            URL urlToOpen = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlToOpen.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
