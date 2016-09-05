package com.aidanrosswood.hothouston;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aidan on 2016-09-05.
 */
public class GetTempTask extends AsyncTask<String, Void, Double>{

    private static final String TAG = "My_GetTempTask";

    @Override
    protected Double doInBackground(String... params) {
        String page = "";
        String urlString = params[0];
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                page += line;
            }

            Log.d(TAG, page);
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(page);
            JSONObject main = json.getJSONObject("main");
            double kelvin = main.getDouble("temp");
            return kelvin - 273.15;
        }catch (JSONException exception){exception.printStackTrace();}

        return -1.0;
    }
}
