package com.dynamic.main;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SecondActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		getSupportActionBar().setTitle("Ö÷¿Í");
	}

}
