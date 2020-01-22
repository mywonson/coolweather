package com.coolweather.app.util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.text.TextUtils;
public class Utility {
	/**
	 * 解析和处理服务器返回的data
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			try {
				JSONObject jsonObject1=new JSONObject(response);
				JSONArray result=jsonObject1.getJSONArray("result");
				ContentValues values=new ContentValues();
				int length=result.length();
				for(int i=0;i<length;i++){
					JSONObject jsonObject2=result.getJSONObject(i);
					values.put("id", jsonObject2.getInt("id"));
					values.put("province",jsonObject2.getString("province"));
					values.put("city",jsonObject2.getString("city"));
					values.put("district",jsonObject2.getString("district"));
					coolWeatherDB.saveWeatherIndex(values);
					values.clear();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}