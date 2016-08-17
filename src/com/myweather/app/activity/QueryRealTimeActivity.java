package com.myweather.app.activity;

import com.myweather.app.R;
import com.myweather.app.util.Cn2Spell;
import com.myweather.app.util.HttpCallbackListener;
import com.myweather.app.util.HttpUtil;
import com.myweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QueryRealTimeActivity extends Activity implements OnClickListener {

	private String inputCity;
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	private Button switchCity;
	private Button refreshWeather;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);

		Intent intent = getIntent();
		inputCity = intent.getStringExtra("input_city");
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);

		// 进活动就直接查询：
		publishText.setText("同步中...");
		weatherInfoLayout.setVisibility(View.INVISIBLE);
		cityNameText.setVisibility(View.INVISIBLE);
		queryWeatherFromServer(inputCity);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, FirstActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherFromServer(inputCity);
			break;
		default:
			break;
		}
	}

	private void queryWeatherFromServer(String inputCity) {
		//若输入的城市是汉字，先转换成拼音（此处调用的方法使用了pinyin4j开源包）：
		String cityName=Cn2Spell.converterToSpell(inputCity);
		
		//查询心知天气的服务器，以下是网址，选择查询今天一天的天气信息:
		String address = "https://api.thinkpage.cn/v3/weather/daily.json?key=clr3zyo0eswhpz05&location=" +cityName 
				+ "&language=zh-Hans&unit=c&start=0&days=1";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
						//在子线程中执行接口传入的回调方法：
			
			public void onFinish(final String response) {
				
				//处理服务器响应的天气信息,存到SharedPreferences中
				Utility.handleWeatherResponse2(QueryRealTimeActivity.this, response);
				
				//返回主线程，读取SharedPreferences中天气信息并更新UI显示出来
				runOnUiThread(new Runnable() {
					public void run() {
						showWeather();
					}
				});
			}

			public void onError(Exception e) {
				//查询异常打印栈踪迹并回主线程更新UI
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("同步失败");
						
					}
				});
			}
		});
	}

	// 从SharedPreferences文件中读取存储好的天气信息，并显示到界面上(要回主线程
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", "没有数据"));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText( prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);	
	}
	
	public void onBackPressed(){
		Intent intent = new Intent(this, FirstActivity.class);
		startActivity(intent);
		finish();
	}
}