package com.aidanrosswood.hothouston;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "My_MainActivity";

    private long timeOfLastPress = -99999999999L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton getTemperature = (ImageButton) findViewById(R.id.getTemperature);
        getTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //As per API guidelines only request temp every 10 mins
                long timeSinceLastPress = System.currentTimeMillis() - timeOfLastPress;
                if(timeSinceLastPress < 1000 * 60 * 10) {
                    long timeUntilNextPress = 1000 * 60 * 10 - timeSinceLastPress;
                    timeUntilNextPress /= 1000;
                    String secs = timeUntilNextPress % 60 > 10 ? timeUntilNextPress % 60 + "" : "0" + (timeUntilNextPress % 60);
                    Toast toast = Toast.makeText(getApplicationContext(), "You must wait " + (timeUntilNextPress / 60) + ":" + secs + " to update the temperature.", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                String url = buildURL("houston", "us");
                new GetTempTask(){

                    @Override
                    protected void onPostExecute(Double celcius) {
                        Log.d(TAG, "" + celcius);
                        if(celcius == -1.0){
                            Toast toast = Toast.makeText(getApplicationContext(), "Error retrieving temperature from server :(", Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        timeOfLastPress = System.currentTimeMillis();
                        String temp = String.format("%.2f", celcius.doubleValue());
                        ((TextView)findViewById(R.id.tempTextField)).setText("The temperature in Houston is " + temp + "\u00B0C");
                    }

                }.execute(url);
            }
        });
    }

    private String buildURL(String city, String countryCode){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=";
        url += city + ',' + countryCode;
        url += "&APPID=" + getString(R.string.open_weather_api_key);
        return url;
    }
}
