package com.example.dynamicactivityapk;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_activity_main);
		Resources mResources = getResources();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.i(TAG, "Plugin Resource " + mResources);
		
		findViewById(R.id.plugin_button).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "I came from plugin project", Toast.LENGTH_LONG).show();
			}});
		
		findViewById(R.id.buton_plugin).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("android.intent.action.plugin_02");
				intent.setPackage(MainActivity.this.getPackageName());
				startActivity(intent);
				
			}});
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
