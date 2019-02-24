package com.codeking123.weatherapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.jar.JarException;

public class WeatherDataModel {
    private String mCity;
    private String mTemperature;
    private String mIconName;
    private int mIconNumber;
    private String mCurrentCondition;
    private String mMaxTemperature;
    private String mMinTemperature;
    private String mSunrise;
    private String mSunset;
    private String mWindSpeed;
    private String mHumidity;
    private String mPressure;
    private String mClouds;

    public static WeatherDataModel fromJson(JSONObject jsonObject){
        WeatherDataModel weatherData =new WeatherDataModel();
        try {
            weatherData.mCity=jsonObject.getString("name");
            Log.d("Clima"," City is"+weatherData.mCity);
            double temp=jsonObject.getJSONObject("main").getDouble("temp")-273.0;
            int roundTemp=(int)Math.rint(temp);
            weatherData.mTemperature=Integer.toString(roundTemp);
            weatherData.mIconNumber=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mIconName=updateWeatherIcon(weatherData.mIconNumber);
            Log.d("Clima","icon is"+weatherData.mIconName);
            double minTemp=jsonObject.getJSONObject("main").getDouble("temp_min")-273.0;
            int roundminTemp=(int)Math.rint(minTemp);
            weatherData.mMinTemperature=Integer.toString(roundminTemp);
            double maxTemp=jsonObject.getJSONObject("main").getDouble("temp_max")-273.0;
            int roundmaxTemp=(int)Math.rint(maxTemp);
            weatherData.mMaxTemperature=Integer.toString(roundmaxTemp);
            int humidity=jsonObject.getJSONObject("main").getInt("humidity");
            weatherData.mHumidity=Integer.toString(humidity);
            long sunRise=jsonObject.getJSONObject("sys").getLong("sunrise");
            weatherData.mSunrise=Time(sunRise);
            long sunSet=jsonObject.getJSONObject("sys").getLong("sunset");
            weatherData.mSunset=Time(sunSet);
            double windSpeed=jsonObject.getJSONObject("wind").getDouble("speed");
            weatherData.mWindSpeed=Double.toString(windSpeed);
            int clouds=jsonObject.getJSONObject("clouds").getInt("all");
            weatherData.mClouds=Integer.toString(clouds);
            int pressure=jsonObject.getJSONObject("main").getInt("pressure");
            weatherData.mPressure=Integer.toString(pressure);
            weatherData.mCurrentCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return weatherData;
    }

    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }
    private static String Time(long unixSeconds){
        //long unixSeconds=1550937827;
        Calendar now =Calendar.getInstance();
        TimeZone timeZone=now.getTimeZone();
      //  Log.d("Clima","GMT format is "+timeZone.getDisplayName());
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat jdf=new SimpleDateFormat("HH:mm");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String javaDate=jdf.format(date);
       // Log.d("Clima","date is "+javaDate);
        //"GMT+5:30"
        return javaDate;
    }

    public String getCity() {
        return mCity;
    }

    public String getTemperature() {
        return mTemperature+"°";
    }

    public String getIconName() {
        return mIconName;
    }

    public int getIconNumber() {
        return mIconNumber;
    }


    public String getMaxTemperature() {
        return "Max "+mMaxTemperature+"°";
    }

    public String getMinTemperature() {
        return mMinTemperature+"°";
    }

    public String getSunrise() {
        return "Sunrise "+mSunrise;
    }

    public String getSunset() {
        return "Sunset "+mSunset;
    }


    public String getHumidity() {
        return "Humidity "+mHumidity+"%";
    }

    public String getPressure() {
        return mPressure+" hPa";
    }

    public String getClouds() {
        return "Clouds "+mClouds+"%";
    }
   public String getTemp(){
        return mTemperature ;
   }
   public String getMax(){
        return mMaxTemperature;
   }

}
