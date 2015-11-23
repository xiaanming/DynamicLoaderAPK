package com.dynamic.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.ArrayMap;
import android.util.Log;

import com.dynamic.framework.ActivityThreadHanderHook;
import com.dynamic.framework.ContextImplHook;
import com.dynamic.framework.CustomClassLoader;
import com.dynamic.framework.DelegateResources;
import com.dynamic.framework.InstrumentationHook;
import com.dynamic.framework.ManifestReader;
import com.dynamic.framework.PluginComponent;
import com.dynamic.framework.PluginManager;
import com.dynamic.framework.RefInvoke;
import com.dynamic.framework.RuntimeVariable;

public class DynamicApplication extends Application {
	private static final String TAG = DynamicApplication.class.getSimpleName();
	Set<String> mAssetPaths = new HashSet<String>();

	private static final String PLUGIN_DIRECTORY = "sdcard/plugin";

	
	private PluginManager mPluginManager = PluginManager.getInstance();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		RuntimeVariable.androidApplication = this;
		try {
			RuntimeVariable.delegateResources = initResources(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RuntimeVariable.mClassLoader = getClassLoader();

		File pluginDir = new File(PLUGIN_DIRECTORY);
		if (pluginDir.exists() && pluginDir.isDirectory()) {
			File[] files = pluginDir.listFiles();

			for (File file : files) {
				
				installPlugin(this, file.getAbsolutePath());
			}
		}
		
		PluginComponent.pluginInfos =  mPluginManager.parsePluginInfo(mPluginManager.getPluginInfoString("plugins.json", this));
		
		
//		Intent serviceIntent = new Intent(
//				"android.intent.dynamic.service");
//		serviceIntent.setPackage(getPackageName());
//		startService(serviceIntent);
		
	}
	

	@Override
	public void startActivity(Intent intent) {
		ContextImplHook context = new ContextImplHook(getBaseContext(), RuntimeVariable.delegateResources, getClassLoader());
		context.startActivity(intent);
	}


	@Override
	public void startActivity(Intent intent, Bundle options) {
		super.startActivity(intent, options);
	}


	@Override
	public ComponentName startService(Intent service) {
		ContextImplHook context = new ContextImplHook(getBaseContext(), RuntimeVariable.delegateResources, getClassLoader());
		return context.startService(service);
	}



	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		ContextImplHook context = new ContextImplHook(getBaseContext(), RuntimeVariable.delegateResources, getClassLoader());
		return context.bindService(service, conn, flags);
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}


	private void installPlugin(Application application, String pluginPath) {
		try {
			File dirFile = new File(application.getCacheDir(), "plugin");
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			String directory = dirFile.getAbsolutePath();
			
//			PackageManager pm = application.getPackageManager();
//            PackageInfo info = pm.getPackageArchiveInfo(pluginPath, 0);
//            
//			getAndroidManifestString(pluginPath);
			
			
			//
			CustomClassLoader pluginClassLoader = new CustomClassLoader(pluginPath,
					directory, null, RuntimeVariable.mClassLoader);
			
			RuntimeVariable.mClassLoader = pluginClassLoader;

			
			DelegateResources.newDelegateResources(application, RuntimeVariable.delegateResources, pluginPath);
			
			Resources mNewResources = RuntimeVariable.delegateResources;
			

			Context mBase = application.getBaseContext();

			RefInvoke.setFieldObject("android.app.ContextImpl", mBase,
					"mResources", mNewResources);

			RefInvoke.setFieldObject("android.app.ContextImpl", mBase,
					"mTheme", null);


			Object currentActivityThread = RefInvoke.invokeStaticMethod(
					"android.app.ActivityThread", "currentActivityThread",
					new Class[] {}, new Object[] {});
			String packageName = application.getPackageName();
			ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldObject(
					currentActivityThread, "mPackages");
			WeakReference mLoaderApkWeakRef = (WeakReference) mPackages
					.get(packageName);
			Object mLoaderApk = mLoaderApkWeakRef.get();

			Log.i(TAG, "mLoaderApk = " + mLoaderApk);

			RefInvoke.setFieldObject(mLoaderApk, "mClassLoader",
					pluginClassLoader);

			RefInvoke.setFieldObject(mLoaderApk, "mResources", mNewResources);
			
			

			//inject instrumentation
			Instrumentation mInstrumentation = (Instrumentation) RefInvoke
					.getFieldObject(currentActivityThread, "mInstrumentation");

			InstrumentationHook hook = new InstrumentationHook(
					mInstrumentation, application.getBaseContext());

			RefInvoke.setFieldObject(currentActivityThread, "mInstrumentation",
					hook);
			
			
			Handler mH = (Handler) RefInvoke.getFieldObject(currentActivityThread, "mH");
			Handler.Callback mCallback = (Callback) RefInvoke.getFieldObject(mH, "mCallback");
			ActivityThreadHanderHook activityThreadHanderHook = new ActivityThreadHanderHook(mCallback);
			RefInvoke.setFieldObject(mH, "mCallback",
					activityThreadHanderHook);
			
			
			//inject application
			RefInvoke.setFieldObject(currentActivityThread, "mInitialApplication", RuntimeVariable.androidApplication);
			RefInvoke.setFieldObject(mLoaderApk, "mApplication", RuntimeVariable.androidApplication);

		} catch (Exception e) {
			Log.i(TAG, "load apk classloader error:" + Log.getStackTraceString(e));
		}
	}
	
	
	private Resources initResources(Application application) throws Exception {
		Resources res = null;

		res = application.getResources();
		if (res != null) {
			return res;
		}

		PackageManager pm = application.getPackageManager();
		res = pm.getResourcesForApplication(application.getApplicationInfo());

		return res;
	}
	
	
	private String getAndroidManifestString(String zipPath) throws Exception {
		
//		AssetManager asset = AssetManager.class.newInstance();
//		Class<?> paramterTypes[] = { String.class };
//		int cookie = (int) RefInvoke.invokeMethod(asset, "addAssetPath", paramterTypes,
//				zipPath);
//		
//		XmlResourceParser parser = asset.openXmlResourceParser(cookie, "AndroidManifest.xml");
//		
//		int type;
//		
//		 while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
//	                && (type != XmlPullParser.END_TAG)) {
//	            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
//	                continue;
//	            }
//	            
//	            
//	            
//	            String tagName = parser.getName();
//	            
//	            System.out.println("tagName = " + tagName);
//		 }
	       		
		
		//解析插件Manifest的信息
		Class<?> packageParserCls = Class.forName("android.content.pm.PackageParser");
		Object mPackageParser = packageParserCls.newInstance();
		
		Object mPackage =  RefInvoke.invokeMethod(mPackageParser, "parsePackage", new Class[]{File.class, int.class}, new File(zipPath), 0);
		
		
		System.out.println(mPackage);
		
		
		
		
		

		StringBuffer manifestStrigBuffer = new StringBuffer();
		BufferedReader br = null;
		try {
			ZipFile zipFile = new ZipFile(new File(zipPath), ZipFile.OPEN_READ);
			ZipEntry zipEntry = zipFile.getEntry("AndroidManifest.xml");
			
			String manifestXml = ManifestReader.getManifestXMLFromAPK(zipFile, zipEntry);
			
			System.out.println("manifestxml = " + manifestXml);
			
			
//			br = new BufferedReader(new InputStreamReader(
//					zipFile.getInputStream(zipEntry), "UTF-8"));
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				manifestStrigBuffer.append(line);
//			}

//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			factory.setNamespaceAware(true);
//			XmlPullParser parser = factory.newPullParser();
//			parser.setInput(new StringReader(manifestStrigBuffer.toString()));
//			int eventType = parser.getEventType();
//			do{
//				
//				System.out.println("eventType = " + eventType);
//				
//				switch (eventType){
//				case XmlPullParser.START_DOCUMENT:{
//					break;
//				}
//				case XmlPullParser.START_TAG:{
//					System.out.println("tag = " + parser.getName());
//					break;
//				}
//				case XmlPullParser.END_TAG: {
//                    break;
//                }
//				}
//				
//				eventType = parser.next();
//			
//			
//			}while (eventType != XmlPullParser.END_DOCUMENT);
			
			
//			 XmlResourceParser parser = new XmlResourceParser();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return manifestStrigBuffer.toString();
	}

}
