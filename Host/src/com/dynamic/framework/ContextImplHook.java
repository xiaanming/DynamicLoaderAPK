package com.dynamic.framework;


import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

public class ContextImplHook extends ContextWrapper {

	private int mThemeResource;
    Resources.Theme mTheme;
    private LayoutInflater mInflater;
     
    Resources mResources;
    private ClassLoader mClassLoader;
 
 
    public ContextImplHook(Context base, Resources resources, ClassLoader classLoader) {
        super(base);
        mResources = resources;
        mClassLoader = classLoader;
    } 
 
 
    @Override 
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    } 
     
    @Override 
    public ClassLoader getClassLoader() {
    	return mClassLoader;
    } 
 
 
    @Override 
    public AssetManager getAssets() {
        return mResources.getAssets();
    } 
     
    @Override 
    public Resources getResources() {
    	Log.i("MainActivity", "ContextImplHook Class= " + mResources);
        return mResources;
    } 
     
    @Override 
    public void setTheme(int resid) {
        mThemeResource = resid;
        initializeTheme(); 
    } 
 
 
    @Override 
    public Resources.Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        } 
 
 
        Object result = RefInvoke.invokeStaticMethod(Resources.class.getName(), "selectDefaultTheme", 
        		new Class[]{int.class,int.class}, 
        		new Object[]{mThemeResource, getBaseContext().getApplicationInfo().targetSdkVersion});
        if (result != null) {
        	mThemeResource =  (Integer)result;
        } 
         
        initializeTheme(); 
 
 
        return mTheme;
    } 


    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
    }


    @Override
    public void startActivities(Intent[] intents) {
    	super.startActivities(intents);
    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {
    	super.startActivities(intents, options);
    }

 
 
    @Override 
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            } 
            return mInflater;
        } 
        return getBaseContext().getSystemService(name);
    } 
 
 
    private void initializeTheme() { 
        final boolean first = mTheme == null;
        if (first) {
            mTheme = getResources().newTheme();
            Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                mTheme.setTo(theme);
            } 
        } 
        mTheme.applyStyle(mThemeResource, true);
    } 

    @Override
    public void startActivity(Intent intent) {
    	
		String packageName = null;
		String componentName = null;
		if (intent.getComponent() != null) {
			packageName = intent.getComponent().getPackageName();
			componentName = intent.getComponent().getClassName();
		} else {
			ResolveInfo resolveInfo = getBaseContext().getPackageManager().resolveActivity(intent, 0);
			if (resolveInfo != null && resolveInfo.activityInfo != null) {
				packageName = resolveInfo.activityInfo.packageName;
				componentName = resolveInfo.activityInfo.name;
			}
		}
		
		PluginComponent.launchApplication(componentName);
        
		super.startActivity(intent);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		String packageName = null;
		String componentName = null;
		if (service.getComponent() != null) {
			packageName = service.getComponent().getPackageName();
			componentName = service.getComponent().getClassName();
		} else {
			ResolveInfo resolveInfo = getBaseContext().getPackageManager().resolveService(service, 0);
			if (resolveInfo != null && resolveInfo.serviceInfo != null) {
				packageName = resolveInfo.serviceInfo.packageName;
				componentName = resolveInfo.serviceInfo.name;
			}
		}
		
		PluginComponent.launchApplication(componentName);

        return super.bindService(service, conn, flags);
    }

    @Override
    public ComponentName startService(Intent service) {
		String packageName = null;
		String componentName = null;
		if (service.getComponent() != null) {
			packageName = service.getComponent().getPackageName();
			componentName = service.getComponent().getClassName();
		} else {
			ResolveInfo resolveInfo = getBaseContext().getPackageManager().resolveService(service, 0);
			if (resolveInfo != null && resolveInfo.serviceInfo != null) {
				packageName = resolveInfo.serviceInfo.packageName;
				componentName = resolveInfo.serviceInfo.name;
			}
		}

		PluginComponent.launchApplication(componentName);
		
        return super.startService(service);
    }
}
