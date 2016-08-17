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

		// �����ֱ�Ӳ�ѯ��
		publishText.setText("ͬ����...");
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
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherFromServer(inputCity);
			break;
		default:
			break;
		}
	}

	private void queryWeatherFromServer(String inputCity) {
		//������ĳ����Ǻ��֣���ת����ƴ�����˴����õķ���ʹ����pinyin4j��Դ������
		String cityName=Cn2Spell.converterToSpell(inputCity);
		
		//��ѯ��֪�����ķ���������������ַ��ѡ���ѯ����һ���������Ϣ:
		String address = "https://api.thinkpage.cn/v3/weather/daily.json?key=clr3zyo0eswhpz05&location=" +cityName 
				+ "&language=zh-Hans&unit=c&start=0&days=1";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
						//�����߳���ִ�нӿڴ���Ļص�������
			
			public void onFinish(final String response) {
				
				//�����������Ӧ��������Ϣ,�浽SharedPreferences��
				Utility.handleWeatherResponse2(QueryRealTimeActivity.this, response);
				
				//�������̣߳���ȡSharedPreferences��������Ϣ������UI��ʾ����
				runOnUiThread(new Runnable() {
					public void run() {
						showWeather();
					}
				});
			}

			public void onError(Exception e) {
				//��ѯ�쳣��ӡջ�ټ��������̸߳���UI
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("ͬ��ʧ��");
						
					}
				});
			}
		});
	}

	// ��SharedPreferences�ļ��ж�ȡ�洢�õ�������Ϣ������ʾ��������(Ҫ�����߳�
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", "û������"));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText( prefs.getString("publish_time", "") + "����");
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