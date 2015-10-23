package com.example.pluginactivity;

import android.app.Application;
import android.widget.Toast;

public class PluginApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(this, "com.example.pluginactivity.PluginApplication  onCreate()", Toast.LENGTH_SHORT).show();
	}
	
	

}
