package com.example.pluginactivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class NotRegisterService2 extends Service{

	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(getApplication(), "NotRegisterService222222  onCreate", Toast.LENGTH_LONG).show();
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		
		Toast.makeText(getApplication(), "NotRegisterService222222  onBind", Toast.LENGTH_LONG).show();
		
		return new Binder();
	}
	
	
	
	

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplication(), "NotRegisterService222222  onUnbind", Toast.LENGTH_LONG).show();
		
		return super.onUnbind(intent);
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Toast.makeText(getApplication(), "NotRegisterService222222  onDestroy", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplication(), "NotRegisterService222222  onStartCommand  = " + intent.getStringExtra("intent"), Toast.LENGTH_LONG).show();
		
		return super.onStartCommand(intent, flags, startId);
	}

}
