package com.dynamic.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String msg = intent.getStringExtra("msg");

			String text = "action = " + action + "\n" + "msg = " + msg;

			Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
		}

	}

	private ServiceReceiver mReceiver = new ServiceReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.dynamic.broadcast");
		registerReceiver(mReceiver, intentFilter);


		Button button = (Button) findViewById(R.id.btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.plugin_01");
				intent.setPackage(getPackageName());
				startActivity(intent);
			}
		});

		findViewById(R.id.start_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent serviceIntent = new Intent(
								"android.intent.dynamic.service");
						serviceIntent.setPackage(getPackageName());
						startService(serviceIntent);

					}
				});

		findViewById(R.id.stop_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent serviceIntent = new Intent(
								"android.intent.dynamic.service");
						serviceIntent.setPackage(getPackageName());
						stopService(serviceIntent);

					}
				});

		findViewById(R.id.load_plugin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								"android.intent.action.plugin_11111");
						startActivity(intent);
					}
				});

		findViewById(R.id.load_host).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);
				startActivity(intent);
			}
		});


		findViewById(R.id.bind_service).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent service = new Intent(
						"android.intent.dynamic.service");
				service.setPackage(getPackageName());
				bindService(service, conn, Context.BIND_AUTO_CREATE);
			}
		});

		findViewById(R.id.unbind_service).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mIsServiceConnected){
					return;
				}
				unbindService(conn);
				
				mIsServiceConnected = false;
			}
		});
		
		
		findViewById(R.id.load_other_app).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.baidu.com");  
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			}
		});

	}
	
	
	private boolean mIsServiceConnected;
	
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(MainActivity.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(MainActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();
			
			mIsServiceConnected = true;
		}
	};
	


	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
