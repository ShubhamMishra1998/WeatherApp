package com.codeking123.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    // Request code for location permission
    //Url for api call
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    //App id for api
    final String APP_ID = "e0b3c30469f29fb7fb13493ac784a263";
    final String LOGCAT_TAG = "Clima";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE=123;
    //Network provider for location
    final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
     LocationManager mLocationManager;
     LocationListener mLocationListener;
    TextView mCurrentCity;
    TextView mCurrentTemperature;
    ImageView mWeatherImage;
    TextView mMaxTemperature;
    TextView mSunrise;
    TextView mSunset;
    Button mSearchNewCity;
    ImageButton mNextMenu;
    TextView mHumidity;
    TextView mCurrentClouds;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mCurrentTemperature=findViewById(R.id.currentTemperature);
       mCurrentCity=findViewById(R.id.currentCity);
       mWeatherImage=findViewById(R.id.weatherImage);
       mMaxTemperature=findViewById(R.id.maxTemp);
       mSunset=findViewById(R.id.currentSunset);
       mSunrise=findViewById(R.id.currentSunrise);
       mSearchNewCity=findViewById(R.id.searchCity);
       mNextMenu=findViewById(R.id.nextMenu);
       mHumidity=findViewById(R.id.currentHumidity);
       mCurrentClouds=findViewById(R.id.currentClouds);

       mSearchNewCity.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent=new Intent(MainActivity.this,SearchCity.class);
               startActivity(myIntent);
           }
       });
       mNextMenu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent=new Intent(MainActivity.this,Details.class);
               startActivity(myIntent);
           }
       });
    }

    @Override
    protected void onResume() {
            super.onResume();
            Log.d(LOGCAT_TAG, "onResume is called");
            getWeatherForCurrentLocation();
        }


    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                        Log.d(LOGCAT_TAG, "onLocationChanged us called");
                        String Longitude = String.valueOf(location.getLongitude());
                         String Latitude = String.valueOf(location.getLatitude());
                        Log.d(LOGCAT_TAG, "Longitude  is " + Longitude);
                        RequestParams params = new RequestParams();
                        params.put("lat", Latitude);
                        params.put("lon", Longitude);
                        params.put("appid", APP_ID);
                        //Necessary parameters for api call for open weather map
                        networkingCall(params);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d(LOGCAT_TAG, "onStatusChanged is called");
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d(LOGCAT_TAG, "onProviderDisabled is called");

            }
        };
        // Asking permission for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        Location location = (Location) mLocationManager.getLastKnownLocation(LOCATION_PROVIDER);
        String  Longitude = String.valueOf(location.getLongitude());
        String Latitude = String.valueOf(location.getLatitude());
        Log.d(LOGCAT_TAG, "longitude is " + Longitude);
        Log.d(LOGCAT_TAG, "latitude is " + Latitude);
        RequestParams params = new RequestParams();
        params.put("lat", Latitude);
        params.put("lon", Longitude);
        params.put("appid", APP_ID);
        //Necessary parameters for api call for open weather map
        networkingCall(params);
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER,MIN_TIME,MIN_DISTANCE,mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getWeatherForCurrentLocation();
        }
        else
        {
            Log.d(LOGCAT_TAG,"Permission Denied");
        }
    }
    //Networking call to fetch live data
    private void networkingCall(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d(LOGCAT_TAG,"Response is "+response.toString());
                WeatherDataModel weatherData=WeatherDataModel.fromJson(response);
                updateUI(weatherData);

            }
            public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response) {
                Log.d(LOGCAT_TAG, "onFailure " + e.toString());
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                Log.d(LOGCAT_TAG, "wrong data is recived is this " + response.toString());

            }

        });
    }
    //Update user Interface
    private void updateUI(WeatherDataModel weather){
        Log.d("Clima","City is "+weather.getCity());
        Log.d("Clima","icon is "+weather.getIconName());
        Log.d("Clima","Max temp is +"+weather.getMaxTemperature());
        Log.d("Clima","Sunrise is "+weather.getSunrise());
           mCurrentCity.setText(weather.getCity());
           int resourseID=getResources().getIdentifier(weather.getIconName(),"drawable",getPackageName());
          mWeatherImage.setImageResource(resourseID);
          mCurrentTemperature.setText(weather.getTemperature());
          mMaxTemperature.setText(weather.getMaxTemperature());
          mSunrise.setText(weather.getSunrise());
          mSunset.setText(weather.getSunset());
          mHumidity.setText(weather.getHumidity());
          mCurrentClouds.setText(weather.getClouds());
    }
}
