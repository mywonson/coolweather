package com.coolweather.app.activity;
import java.util.ArrayList;
import java.util.List;
import com.coolweather.app.R;
import com.coolweather.app.model.HttpCallbackListener;
import com.coolweather.app.util.CoolWeatherDB;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_DISTRICT = 2;
	private int currentLevel;
	private static  ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private String selectedProvince;
	private String selectedCity;
	private String selectedDistrict;
	private List<String> dataList=new ArrayList<String>();
	private  HttpCallbackListener listener=new HttpCallbackListener(){
		@Override
		public void onFinish(String response) {
			boolean flag=Utility.handleProvincesResponse(coolWeatherDB, response);
			if(flag) {
				Log.d("handleProvincesResponse","handleProvincesResponse successfully worked");
			}else{
				Toast.makeText(ChooseAreaActivity.this, "handleProvincesResponse failed worked!", Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onError(Exception e) {
			e.printStackTrace();
			Log.d("sendHttpRequest","date request failed!");
		}
	};
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		if(coolWeatherDB.loadWeatherIndexSingle()==null){
			showProgressDialog();
			HttpUtil.sendHttpRequest("http://v.juhe.cn/weather/citys?key=943e1fd0b2675e60943934906374d45d", listener);
			closeProgressDialog();
		}
		titleText=(TextView)findViewById(R.id.title_text);
		listView=(ListView)findViewById(R.id.list_view);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,long arg3) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=dataList.get(index);
					queryCities(selectedProvince);  // 加载city级数据
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=dataList.get(index);
					queryDistricts(selectedCity);      //加载district级数据
				}else if(currentLevel==LEVEL_DISTRICT){
					selectedDistrict=dataList.get(index);
					queryWeather(selectedDistrict);
				}
			}

			private void queryWeather(String selectedDistrict) {
				//TODO
			}
		});
		queryProvinces();       // 加载省级数据
	}
	private void queryProvinces(){
		dataList.clear();
		dataList=coolWeatherDB.selectProvinces();
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter); 
		listView.setSelection(0);
		titleText.setText("中国");
		currentLevel=LEVEL_PROVINCE;
	}
	private void queryCities(String selectedProvince){
	  dataList.clear();
	  dataList=coolWeatherDB.selectCitiesByProvince(selectedProvince);
	  adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
	  listView.setAdapter(adapter); 
	  listView.setSelection(0);
	  titleText.setText(selectedProvince);
	  currentLevel=LEVEL_CITY;
	}
	
	private void queryDistricts(String selectedCity){
		dataList.clear();
		dataList=coolWeatherDB.selectDistrictsBySelectedCity(selectedCity);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter); 
		listView.setSelection(0);
		titleText.setText(selectedCity);
		currentLevel=LEVEL_DISTRICT;
	}
	
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
	  progressDialog.show();
	}
	private void closeProgressDialog() {
		if(progressDialog!=null) progressDialog.dismiss();
	}
	@Override
	public void onBackPressed() {
		if(currentLevel==LEVEL_DISTRICT) {
			queryCities(selectedProvince);
		}else if(currentLevel==LEVEL_CITY){
			queryProvinces();
		}else if(currentLevel==LEVEL_PROVINCE) finish();
	}
}