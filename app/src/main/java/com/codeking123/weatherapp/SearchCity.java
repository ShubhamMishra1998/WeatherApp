package com.codeking123.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchCity extends AppCompatActivity {
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "e0b3c30469f29fb7fb13493ac784a263";
    EditText mSearchCity;
    ImageButton mBackButton;
    TextView mCity;
    TextView mTemperature;
    TextView mMaxTemperature;
    TextView mHumidity;
    TextView mClouds;
    TextView mPressure;
    TextView mSunrise;
    TextView mSunset;
    Switch mSwitch;
    boolean isFehrenheit=false;
    String newcity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchnewcity);
        mSearchCity=findViewById(R.id.newCity);
        mBackButton=findViewById(R.id.backButton);
        mCity=findViewById(R.id.yourCity);
        mTemperature=findViewById(R.id.searchTemperature);
        mMaxTemperature=findViewById(R.id.highTemp);
        mHumidity=findViewById(R.id.searchHumidity);
        mClouds=findViewById(R.id.searchClouds);
        mPressure=findViewById(R.id.searchPressure);
        mSunrise=findViewById(R.id.searchSunrise);
        mSunset=findViewById(R.id.searchSunset);
        mSwitch=findViewById(R.id.switchFahrenheti);
        //Enter your search city in Edit Text

       mSearchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                newcity=mSearchCity.getText().toString();
               Log.d("Clima","new city you entered is "+newcity);
               getWeatherForYourSearchCity(newcity);
               return true;
           }
       });
       mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
               if(isChecked) {
                   isFehrenheit = true;
                   getWeatherForYourSearchCity(newcity);
               }
               else
               {
                   isFehrenheit=false;
                   getWeatherForYourSearchCity(newcity);
               }

           }
       });
       mBackButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });


    }
    private void getWeatherForYourSearchCity(String newcity){
        RequestParams params=new RequestParams();
        params.put("appid",APP_ID);
        params.put("q",newcity);
        networkingCall(params);
    }
    //Networking Call
    private void networkingCall(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("Clima","Response is "+response.toString());
                WeatherDataModel weatherData=WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }
            public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response) {
                Log.d("Clima", "onFailure " + e.toString());
                Toast.makeText(SearchCity.this, "Request failed", Toast.LENGTH_SHORT).show();
                Log.d("Clima", "wrong data is recived is this " + response.toString());
            }

        });
    }
    private void updateUI(WeatherDataModel weatherData){
        String TemperatureMax;
        String Temperature;
        String T=weatherData.getTemp();
        String M=weatherData.getMax();
        int Temp=Integer.parseInt(T);
        int mTemp=Integer.parseInt(M);
        if(isFehrenheit){
            double d=(9.0/5.0)*Temp*1.0;
            double d1=(9.0/5.0)*mTemp*1.0;
            Temp=(int)d;
            mTemp=(int)d1;
            Temp+=32;
            mTemp+=32;
            String s1=Integer.toString(Temp);
            String s2=Integer.toString(mTemp);
            Temperature="Temperature "+s1+"°";
            TemperatureMax="Max "+s2+"°";
        }
        else
        {
            String s1=Integer.toString(Temp);
            String s2=Integer.toString(mTemp);
            Temperature="Temperature "+s1+"°";
            TemperatureMax="Max "+s2+"°";

        }
        mCity.setText(weatherData.getCity());
        mTemperature.setText(Temperature);
        mMaxTemperature.setText(TemperatureMax);
        mHumidity.setText(weatherData.getHumidity());
        mClouds.setText(weatherData.getClouds());
        mPressure.setText(weatherData.getPressure());
        mSunrise.setText(weatherData.getSunrise());
        mSunset.setText(weatherData.getSunset());
        mSearchCity.setText("");

    }
}
