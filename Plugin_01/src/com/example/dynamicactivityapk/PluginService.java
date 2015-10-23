package com.example.dynamicactivityapk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PluginService extends Service{
	private static final String TAG = PluginService.class.getSimpleName();
	

	@Override
	public void onCreate() {
		Intent intent = new Intent();
		intent.putExtra("msg", TAG + " onCreate");
		intent.setAction("com.dynamic.broadcast");
		sendBroadcast(intent);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Intent intent = new Intent();
		intent.putExtra("msg", TAG + "  onDestroy");
		intent.setAction("com.dynamic.broadcast");
		sendBroadcast(intent);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Intent i = new Intent();
		i.putExtra("msg", TAG + "  onBind");
		i.setAction("com.dynamic.broadcast");
		sendBroadcast(i);
		
		return new Binder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Intent i = new Intent();
		i.putExtra("msg", TAG + " onUnbind");
		i.setAction("com.dynamic.broadcast");
		sendBroadcast(i);
		return super.onUnbind(intent);
	}
	
	
	
	public void test(){
		Intent i = new Intent();
		i.putExtra("msg", "我是插件Service test()方法");
		i.setAction("com.dynamic.broadcast");
		sendBroadcast(i);
	}
	
	
	
	
	
	

}
