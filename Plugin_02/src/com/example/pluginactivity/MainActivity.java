package com.example.pluginactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	private boolean mIsBindSucc = false;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(MainActivity.this,
					name.getClassName() + "  onServiceConnected",
					Toast.LENGTH_SHORT).show();

			mIsBindSucc = true;

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i(TAG, "Plugin getResources = " + getResources()
				+ "  application = " + getApplication());

		Log.i(TAG, "Plugin context = " + getBaseContext() + "   activity = "
				+ this);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		findViewById(R.id.start_activity).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								PluginActionBarActivity.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.start_not_register).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterActivity.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.start_not_register_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterService.class);
						intent.putExtra("intent", "intent with value");
						startService(intent);
					}
				});

		findViewById(R.id.stop_not_register_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterService.class);
						stopService(intent);
					}
				});

		findViewById(R.id.bind_not_register_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterService.class);
						bindService(intent, mServiceConnection,
								Context.BIND_AUTO_CREATE);

					}
				});

		findViewById(R.id.unbind_not_register_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!mIsBindSucc)
							return;

						unbindService(mServiceConnection);
						mIsBindSucc = false;
					}
				});
		
		
		findViewById(R.id.start_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterService2.class);
						intent.putExtra("intent", "intent with value");
						startService(intent);
					}
				});
		
		
		findViewById(R.id.stop_service).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								NotRegisterService2.class);
						stopService(intent);
					}
				});

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

		if (id == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
