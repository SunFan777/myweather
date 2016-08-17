package com.myweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myweather.app.model.City;
import com.myweather.app.model.County;
import com.myweather.app.model.MyWeatherDB;
import com.myweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {

	public synchronized static boolean handleProvincesResponse(MyWeatherDB myWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					myWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB, String response, int provinceId) {

		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					myWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCountiesResponse(MyWeatherDB myWeatherDB, String response, int cityId) {

		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					myWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	// 解析中国天气网服务器返回的JSON天气信息
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 存储中国天气网服务器返回的天气信息到SharedPreferences中去
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2,
			String weatherDesp, String publishTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);// 设置标志位,表示已经选中一个城市
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}

	// 解析心知天气服务器返回的JSON天气信息
	public static void handleWeatherResponse2(Context context, String response) {
		try {

			
			JSONObject jsonObject = new JSONObject(response);
			JSONArray resultsArray = jsonObject.getJSONArray("results");

			JSONObject resultsInfo = resultsArray.getJSONObject(0);
			
			JSONObject location=resultsInfo.getJSONObject("location");
			String cityName = location.getString("name");
			
			JSONArray daily = resultsInfo.getJSONArray("daily");
			JSONObject weatherInfo=daily.getJSONObject(0);			
			String temp1 = weatherInfo.getString("low");
			String temp2 = weatherInfo.getString("high");
			String weatherDesp = weatherInfo.getString("text_day");
						
			String publishTime = resultsInfo.getString("last_update");

			saveWeatherInfo2(context, cityName, temp1, temp2, weatherDesp, publishTime);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 存储心知天气服务器返回的天气信息到SharedPreferences中去
	public static void saveWeatherInfo2(Context context, String cityName, String temp1, String temp2,
			String weatherDesp, String publishTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("city_name", cityName);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
