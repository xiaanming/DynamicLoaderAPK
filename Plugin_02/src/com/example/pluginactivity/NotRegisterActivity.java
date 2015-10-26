package com.example.pluginactivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


public class NotRegisterActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Custom_Theme_AppCompat);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotRegisterActivity.this, NotRegisterActivity.class);
				startActivity(intent);
				
			}
		});
		
		findViewById(R.id.button_1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotRegisterActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
		
		final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
		
		
		findViewById(R.id.notification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotRegisterActivity.this);  
				mBuilder.setContentTitle("测试标题")
			    .setContentText("测试内容")
			    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图  
			    .setNumber(5)  
			    .setTicker("测试通知来啦") 
			    .setWhen(System.currentTimeMillis())
			    .setPriority(Notification.PRIORITY_DEFAULT) 
			    .setSmallIcon(R.drawable.ic_launcher)
			//  .setAutoCancel(true)//   
			    .setDefaults(Notification.DEFAULT_VIBRATE);
			    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
//			    .setSmallIcon(R.drawable.ic_launcher);//设置通知小
				
				
				Notification notification = mBuilder.build();  
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				
				mNotificationManager.notify(100, notification);
				
				
			}
		});
	}
	
	public PendingIntent getDefalutIntent(int flags){  
	    PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);  
	    return pendingIntent;  
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if(id == android.R.id.home){
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}