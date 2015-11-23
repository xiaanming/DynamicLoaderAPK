package com.example.pluginactivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class NotRegisterService extends Service{

	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(getApplication(), "NotRegisterService  onCreate", Toast.LENGTH_LONG).show();
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		
		Toast.makeText(getApplication(), "NotRegisterService  onBind", Toast.LENGTH_LONG).show();
		
		return new Binder();
	}
	
	
	
	

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplication(), "NotRegisterService  onUnbind", Toast.LENGTH_LONG).show();
		
		return super.onUnbind(intent);
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Toast.makeText(getApplication(), "NotRegisterService  onDestroy", Toast.LENGTH_LONG).show();
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	

}
