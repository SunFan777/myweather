package com.myweather.app.activity;

import com.myweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity implements OnClickListener {

	private Button chooseAreaButton;
	private Button queryRealTimeWeatherButton;
	private EditText editText;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_layout);
		
		editText=(EditText)findViewById(R.id.query_real_time_weather_text);
		chooseAreaButton=(Button)findViewById(R.id.choose_area_button);
		queryRealTimeWeatherButton=(Button)findViewById(R.id.query_real_time_weather_button);
		chooseAreaButton.setOnClickListener(this);
		queryRealTimeWeatherButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.choose_area_button:
			Intent intent1=new Intent(this,ChooseAreaActivity.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.query_real_time_weather_button:
			String inputCity=editText.getText().toString();
			Intent intent2=new Intent(this,QueryRealTimeActivity.class);
			intent2.putExtra("input_city", inputCity);
			startActivity(intent2);
			finish();
			break;
		default:
			break;
		}
	}
	
	
}
