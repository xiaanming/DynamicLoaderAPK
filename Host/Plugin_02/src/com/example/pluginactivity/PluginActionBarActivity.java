package com.example.pluginactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PluginActionBarActivity extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
