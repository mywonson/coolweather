package com.coolweather.app.model;
public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);
}