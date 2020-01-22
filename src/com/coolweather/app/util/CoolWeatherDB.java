package com.coolweather.app.util;
import java.util.ArrayList;
import java.util.List;
import com.coolweather.app.db.CoolWeatherOpenHelper;
import com.coolweather.app.model.WeatherIndex;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class CoolWeatherDB {
	public static final String DB_NAME="cool_weather";  //数据库名
	public static final int VERSION=1;                  //数据库版本
	private static CoolWeatherDB coolWeatherDB;         //可以保证全局范围内只会有一个CoolWeatherDB的实例
	private SQLiteDatabase db;
	/**
	 * 将构造方法私有化
	 * 单例类   并提供了一个getInstance()方法来获取CoolWeatherDB的实例
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper coolWeatherOpenHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=coolWeatherOpenHelper.getWritableDatabase();
	}
	/**
	 * 获取CoolWeatherDB的实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null) coolWeatherDB=new CoolWeatherDB(context);
		return coolWeatherDB;
	}
	//select province from WeatherIndex groupBy province
	public List<String> selectProvinces(){
	   List<String> dataList=new ArrayList<String>();
	    Cursor cursor=db.query("WeatherIndex", new String[]{"province"}, null, null, "province", null, null);
	    if(cursor.moveToFirst()){
	    	do{
	    		dataList.add(cursor.getString(0));
	    	}while(cursor.moveToNext());
	    }
	    return dataList;
	}
	/**
	 * 将data存储到数据库
	 */
	public void saveWeatherIndex(ContentValues values){
		if(values!=null) db.insert("WeatherIndex", null, values);
	}
	
	/**
	 * read WeatherIndex from database
	 */
	public List<WeatherIndex> loadWeatherIndex(){
		List<WeatherIndex> list=new ArrayList<WeatherIndex>();
		Cursor cursor=db.query("WeatherIndex",null,null,null,null,null,null);
		if(cursor.moveToFirst()){
			do{;
			WeatherIndex weatherIndex=new WeatherIndex();
			weatherIndex.setId(cursor.getInt(cursor.getColumnIndex("id")));
			weatherIndex.setProvince(cursor.getString(cursor.getColumnIndex("province")));
			weatherIndex.setCity(cursor.getString(cursor.getColumnIndex("city")));
			weatherIndex.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
			list.add(weatherIndex);
		      }while(cursor.moveToNext());
	      }
          return list;
  }
	public WeatherIndex loadWeatherIndexSingle(){
		WeatherIndex index;
		Cursor cursor=db.query("WeatherIndex",null,"id=?",new String[]{String.valueOf(100)},null,null,null);
		if(cursor.moveToFirst()){
			index=new WeatherIndex();
			index.setId(cursor.getInt(cursor.getColumnIndex("id")));
			index.setProvince(cursor.getString(cursor.getColumnIndex("province")));
			index.setCity(cursor.getString(cursor.getColumnIndex("city")));
			index.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
			return index;
		}
		return null;
	}
	
	public List<String> selectCitiesByProvince(String selectedProvince) {
		Cursor cursor=db.query("WeatherIndex",new String[]{"city"},"province=?",new String[]{selectedProvince},"city",null,null);
		List<String> dataList=new ArrayList<String>();
		if(cursor.moveToFirst()){
			do{
				dataList.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		return dataList;
	}
	public List<String> selectDistrictsBySelectedCity(String selectedCity) {
		List<String> dataList=new ArrayList<String>();
		Cursor cursor=db.query("WeatherIndex",new String[]{"district"},"city=?",new String[]{selectedCity}, "district", null, null);
		if(cursor.moveToFirst()){
			do{
				dataList.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		return dataList;
	}
}






