package com.dynamic.main;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class SecondActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		getSupportActionBar().setTitle("Host");
		
		findViewById(R.id.notification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				 
		        CharSequence tickerText = "Hello";
		        long when = System.currentTimeMillis();
		 
		        Notification notification = new Notification(R.drawable.ic_launcher, tickerText, when);
		        notification.flags = Notification.FLAG_AUTO_CANCEL;
		        
		        Intent intent = new Intent()  
		        .setAction(Intent.ACTION_MAIN)  
		        .addCategory(Intent.CATEGORY_LAUNCHER)  
		        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  
		        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		        .setClass(getApplication(), SecondActivity.class); 
		         
		        PendingIntent pi = PendingIntent.getActivity(SecondActivity.this, 0, intent, 0);
		        notification.setLatestEventInfo(SecondActivity.this, "宿主通知消息", "Notification", pi);
		 
		        nm.notify(100, notification);
				
			}
		});
	}

}
