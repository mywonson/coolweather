package com.coolweather.app.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	public static final String CREATE_WEATHERINDEX="create table WeatherIndex(id integer primary key,province text,city text,district text)";
	public CoolWeatherOpenHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_WEATHERINDEX);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO
	}
}