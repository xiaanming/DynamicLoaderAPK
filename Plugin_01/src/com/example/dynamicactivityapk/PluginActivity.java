package com.example.dynamicactivityapk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class PluginActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_activity);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		findViewById(R.id.button).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PluginActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}});
		
		
		findViewById(R.id.button_1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("android.intent.action.plugin_11111");
				startActivity(intent);
			}});
		
		
		findViewById(R.id.button_service).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PluginActivity.this, PluginService.class);
				startService(intent);
			}});
		
		
		findViewById(R.id.button_service_stop).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PluginActivity.this, PluginService.class);
				stopService(intent);
			}});
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	


}
