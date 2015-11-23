package com.dynamic.framework;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class StubService extends Service{
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(getApplication(), "StubService onCreate", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Toast.makeText(getApplication(), "StubService onDestroy", Toast.LENGTH_LONG).show();
	}
	
	
	
	
	

}
