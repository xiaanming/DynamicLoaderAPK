package com.dynamic.main;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.dynamic.framework.DelegateResources;
import com.dynamic.framework.InstrumentationHook;
import com.dynamic.framework.PluginComponent;
import com.dynamic.framework.PluginManager;
import com.dynamic.framework.RefInvoke;
import com.dynamic.framework.RuntimeVariable;

import dalvik.system.DexClassLoader;

public class DynamicApplication extends Application {
	private static final String TAG = DynamicApplication.class.getSimpleName();
	Set<String> mAssetPaths = new HashSet<String>();

	private static final String PLUGIN_DIRECTORY = "sdcard/plugin";

	
	private PluginManager mPluginManager = PluginManager.getInstance();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		RuntimeVariable.androidApplication = this;
		RuntimeVariable.delegateResources = this.getResources();
		RuntimeVariable.mClassLoader = getClassLoader();

		File pluginDir = new File(PLUGIN_DIRECTORY);
		if (pluginDir.exists() && pluginDir.isDirectory()) {
			File[] files = pluginDir.listFiles();

			for (File file : files) {
				installPlugin(this, file.getAbsolutePath());
			}
		}
		
		PluginComponent.pluginInfos =  mPluginManager.parsePluginInfo(mPluginManager.getPluginInfoString("plugins.json", this));
		
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	

	private void installPlugin(Application application, String pluginPath) {
		try {
			String directory = application.getCacheDir().getAbsolutePath();
			DexClassLoader pluginClassLoader = new DexClassLoader(pluginPath,
					directory, directory, RuntimeVariable.mClassLoader);
			
			RuntimeVariable.mClassLoader = pluginClassLoader;

			
			DelegateResources.newDelegateResources(application, RuntimeVariable.delegateResources, pluginPath);
			
			Resources mNewResources = RuntimeVariable.delegateResources;

			Context mBase = application.getBaseContext();

			RefInvoke.setFieldObject("android.app.ContextImpl", mBase,
					"mResources", mNewResources);

			RefInvoke.setFieldObject("android.app.ContextImpl", mBase,
					"mTheme", null);


			// ���ö�̬���ػ���
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
			
			
			//inject application
			RefInvoke.setFieldObject(currentActivityThread, "mInitialApplication", RuntimeVariable.androidApplication);
			RefInvoke.setFieldObject(mLoaderApk, "mApplication", RuntimeVariable.androidApplication);

		} catch (Exception e) {
			Log.i(TAG,
					"load apk classloader error:" + Log.getStackTraceString(e));
		}
	}
	

}