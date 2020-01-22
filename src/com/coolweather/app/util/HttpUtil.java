package com.coolweather.app.util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Looper;
import com.coolweather.app.model.HttpCallbackListener;
public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in=connection.getInputStream();
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while((line=bufferedReader.readLine())!=null){
						response.append(line);
					}
					if(listener!=null){
						//回调onFinish()
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(listener!=null){
						//回调onExcpetion()
						listener.onError(e);
				}
			}finally{
				if(connection!=null){
				   connection.disconnect();
				}
			}
		}
	  }).start();
    }
}