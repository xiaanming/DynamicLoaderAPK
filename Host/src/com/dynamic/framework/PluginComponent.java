package com.dynamic.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class PluginComponent {
	public static List<PluginInfo> pluginInfos = new ArrayList<PluginInfo>();
	
	static Map<String, Application> pluginApplications = new HashMap<String, Application>();
	
	
	public static PluginInfo locateComponent(String componentName){
		for(PluginInfo info : pluginInfos){
			if(info.getActivities().contains(componentName)
					|| info.getReceivers().contains(componentName)
					|| info.getServices().contains(componentName)
					|| info.getContentProviders().contains(componentName)){
				return info;
			}
		}
		return null;
	}
	
	
	public static void launchApplication(String componentName){
		if(TextUtils.isEmpty(componentName))
			return;
		PluginInfo info = locateComponent(componentName);
		if(null != info){
			String pkgName = info.getPkgName();
			String applicationName = info.getApplicationName();
			
			if(pluginApplications.containsKey(pkgName)){
				return;
			}
			try{
				Application application = newApplication(applicationName, RuntimeVariable.mClassLoader);
				application.onCreate();
				pluginApplications.put(pkgName, application);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	protected static Application newApplication(String applicationClassName,
			ClassLoader cl) throws Exception {
		final Class<?> applicationClass = cl.loadClass(applicationClassName);

		if (applicationClass == null) {
			throw new ClassNotFoundException(applicationClassName);
		}
		Application app = (Application) applicationClass.newInstance();
		
		Class<?>[] parameterTypes = {Context.class};
		RefInvoke.invokeMethod("android.app.Application", app, "attach", parameterTypes, RuntimeVariable.androidApplication);

		return app;
	}

}
