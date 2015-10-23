package com.dynamic.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class PluginManager {

	private static final PluginManager mInstance = new PluginManager();
	private static final String CHARSET = "UTF-8";

	private PluginManager() {
	}

	public static PluginManager getInstance() {
		return mInstance;
	}

	public List<PluginInfo> parsePluginInfo(String json) {
		List<PluginInfo> mPluginInfos = new ArrayList<PluginInfo>();
		
		try{
			JSONArray jsonArray = new JSONArray(json);
			for(int i=0; i<jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				PluginInfo info = new PluginInfo();
				info.setApplicationName(jsonObject.optString("applicationName"));
				info.setPkgName(jsonObject.optString("pkgName"));
				
				JSONArray activityArray = jsonObject.optJSONArray("activities");
				List<String> activities = new ArrayList<String>();
				for(int a=0; a<activityArray.length(); a++){
					activities.add(activityArray.optString(a));
				}
				info.setActivities(activities);
				
				JSONArray serviceArray = jsonObject.optJSONArray("services");
				List<String> services = new ArrayList<String>();
				for(int s=0; s<serviceArray.length(); s++){
					services.add(serviceArray.optString(s));
				}
				info.setServices(services);
				
				
				JSONArray receiverArray = jsonObject.optJSONArray("receivers");
				List<String> receivers = new ArrayList<String>();
				for(int r=0; r<receiverArray.length(); r++){
					receivers.add(activityArray.optString(r));
				}
				info.setReceivers(receivers);
				
				JSONArray contentProviderArray = jsonObject.optJSONArray("contentProviders");
				List<String> contentProviders = new ArrayList<String>();
				for(int c=0; c<contentProviderArray.length(); c++){
					contentProviders.add(contentProviderArray.optString(c));
				}
				info.setContentProviders(contentProviders);
				
				mPluginInfos.add(info);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mPluginInfos;
	}

	
	
	public String getPluginInfoString(String assetFile, Context context) {
		BufferedReader bufReader = null;
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(assetFile), CHARSET);
			bufReader = new BufferedReader(inputReader);
			String line = "";
			String result = "";
			while ((line = bufReader.readLine()) != null){
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
