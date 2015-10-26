package com.dynamic.framework;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class InstrumentationHook extends Instrumentation {

	private static final String TAG = "MainActivity";

	private Context context;
	private Instrumentation mBase;

	public InstrumentationHook(Instrumentation mBase, Context context) {
		this.context = context;
		this.mBase = mBase;
	}

	public ActivityResult execStartActivity(final Context who,
			final IBinder contextThread, final IBinder token,
			final Activity target, final Intent intent, final int requestCode) {
		
		
		return execStartActivityInternal(who, intent, new ExecStartActivityCallback() {
			
			@Override
			public ActivityResult execStartActivity() {
				Class<?> [] parameterTypes ={Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class};
				return (ActivityResult) RefInvoke.invokeMethod(mBase, "execStartActivity", parameterTypes, who, contextThread, token, target, intent, requestCode);
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public ActivityResult execStartActivity(final Context who,
			final IBinder contextThread, final IBinder token,
			final Activity target, final Intent intent, final int requestCode,
			final Bundle options) {
		
		return execStartActivityInternal(who, intent, new ExecStartActivityCallback() {
			
			@Override
			public ActivityResult execStartActivity() {
				Class<?> [] parameterTypes ={Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class};
				
				return (ActivityResult) RefInvoke.invokeMethod(mBase, "execStartActivity", parameterTypes, who, contextThread, token, target, intent, requestCode, options);
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public ActivityResult execStartActivity(final Context who,
			final IBinder contextThread, final IBinder token,
			final Fragment target, final Intent intent, final int requestCode) {
		
		
		return execStartActivityInternal(who, intent, new ExecStartActivityCallback() {
			
			@Override
			public ActivityResult execStartActivity() {
				Class<?> [] parameterTypes ={Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class};
				
				return (ActivityResult) RefInvoke.invokeMethod(mBase, "execStartActivity", parameterTypes, who, contextThread, token, target, intent, requestCode);
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public ActivityResult execStartActivity(final Context who,
			final IBinder contextThread, final IBinder token,
			final Fragment target, final Intent intent, final int requestCode,
			final Bundle options) {
		
		return execStartActivityInternal(who, intent, new ExecStartActivityCallback() {
			
			@Override
			public ActivityResult execStartActivity() {
				Class<?> [] parameterTypes ={Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class, Bundle.class};
				return (ActivityResult) RefInvoke.invokeMethod(mBase, "execStartActivity", parameterTypes, who, contextThread, token, target, intent, requestCode, options);
			}
		});
	}
	
	
	private static interface ExecStartActivityCallback {
		public ActivityResult execStartActivity();
	}
	
	
	public ActivityResult execStartActivityInternal(Context context, Intent intent, ExecStartActivityCallback callback){
		String packageName = null;
		String componentName = null;
		if (intent.getComponent() != null) {
			packageName = intent.getComponent().getPackageName();
			componentName = intent.getComponent().getClassName();
		} else {
			ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, 0);
			if (resolveInfo != null && resolveInfo.activityInfo != null) {
				packageName = resolveInfo.activityInfo.packageName;
				componentName = resolveInfo.activityInfo.name;
			}
		}
		
		
		PluginComponent.launchApplication(componentName);
		
		
		//测试不在宿主注册的Activity
		if(componentName.equalsIgnoreCase("com.example.pluginactivity.NotRegisterActivity")){
			intent.setComponent(new ComponentName(packageName, "com.dynamic.framework.ActionBarStubActivity"));
		}
		
		return callback.execStartActivity();
		
	}
	

	@Override
	public Activity newActivity(Class<?> clazz, Context context, IBinder token,
			Application application, Intent intent, ActivityInfo info,
			CharSequence title, Activity parent, String id,
			Object lastNonConfigurationInstance) throws InstantiationException,
			IllegalAccessException {

		Activity activity = mBase.newActivity(clazz, context, token,
				application, intent, info, title, parent, id,
				lastNonConfigurationInstance);

		// 设置Resource给Activity
		RefInvoke.setFieldObject("android.view.ContextThemeWrapper", activity,
				"mResources", context.getResources());
		return activity;
	}

	@Override
	public Activity newActivity(ClassLoader cl, String className, Intent intent)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		
		
		if(className.equalsIgnoreCase("com.dynamic.framework.ActionBarStubActivity")){
			className = "com.example.pluginactivity.NotRegisterActivity";
		}
		
		
		Activity activity = null;
		try {
			activity = mBase.newActivity(cl, className, intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		RefInvoke.setFieldObject("android.view.ContextThemeWrapper", activity,
				"mResources", context.getResources());
		
		return activity;
	}

	@Override
	public void callActivityOnCreate(Activity activity, Bundle icicle) {
		Context mBaseContext = activity.getBaseContext();
		
		ContextImplHook hook = new ContextImplHook(mBaseContext,
				context.getResources(), mBaseContext.getClassLoader());
		
		//插件中继承ActionBarActivity获取不到资源的问题
		if(activity instanceof ActionBarActivity && activity.getClass().getName().contains("com.example.pluginactivity")){
			hook.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
		}
		
//		hook.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
		

		RefInvoke.setFieldObject("android.view.ContextThemeWrapper", activity,
				"mBase", hook);

		RefInvoke.setFieldObject("android.content.ContextWrapper", activity,
				"mBase", hook);

		mBase.callActivityOnCreate(activity, icicle);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public UiAutomation getUiAutomation() {
		return mBase.getUiAutomation();
	}

	@Override
	public void onCreate(Bundle arguments) {
		mBase.onCreate(arguments);
	}

	@Override
	public void start() {
		mBase.start();
	}

	@Override
	public void onStart() {
		mBase.onStart();
	}

	@Override
	public boolean onException(Object obj, Throwable e) {
		return mBase.onException(obj, e);
	}

	@Override
	public void sendStatus(int resultCode, Bundle results) {
		mBase.sendStatus(resultCode, results);
	}

	@Override
	public void finish(int resultCode, Bundle results) {
		mBase.finish(resultCode, results);
	}

	@Override
	public void setAutomaticPerformanceSnapshots() {
		mBase.setAutomaticPerformanceSnapshots();
	}

	@Override
	public void startPerformanceSnapshot() {
		mBase.startPerformanceSnapshot();
	}

	@Override
	public void endPerformanceSnapshot() {
		mBase.endPerformanceSnapshot();
	}

	@Override
	public void onDestroy() {
		mBase.onDestroy();
	}

	@Override
	public Context getContext() {
		return mBase.getContext();
	}

	@Override
	public ComponentName getComponentName() {
		return mBase.getComponentName();
	}

	@Override
	public Context getTargetContext() {
		return mBase.getTargetContext();
	}

	@Override
	public boolean isProfiling() {
		return mBase.isProfiling();
	}

	@Override
	public void startProfiling() {
		mBase.startProfiling();
	}

	@Override
	public void stopProfiling() {
		mBase.stopProfiling();
	}

	@Override
	public void setInTouchMode(boolean inTouch) {
		mBase.setInTouchMode(inTouch);
	}

	@Override
	public void waitForIdle(Runnable recipient) {
		mBase.waitForIdle(recipient);
	}

	@Override
	public void waitForIdleSync() {
		mBase.waitForIdleSync();
	}

	@Override
	public void runOnMainSync(Runnable runner) {
		mBase.runOnMainSync(runner);
	}

	@Override
	public Activity startActivitySync(Intent intent) {
		return mBase.startActivitySync(intent);
	}

	@Override
	public void addMonitor(ActivityMonitor monitor) {
		mBase.addMonitor(monitor);
	}

	@Override
	public ActivityMonitor addMonitor(IntentFilter filter,
			ActivityResult result, boolean block) {
		return mBase.addMonitor(filter, result, block);
	}

	@Override
	public ActivityMonitor addMonitor(String cls, ActivityResult result,
			boolean block) {
		return mBase.addMonitor(cls, result, block);
	}

	@Override
	public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
		return mBase.checkMonitorHit(monitor, minHits);
	}

	@Override
	public Activity waitForMonitor(ActivityMonitor monitor) {
		return mBase.waitForMonitor(monitor);
	}

	@Override
	public Activity waitForMonitorWithTimeout(ActivityMonitor monitor,
			long timeOut) {
		return mBase.waitForMonitorWithTimeout(monitor, timeOut);
	}

	@Override
	public void removeMonitor(ActivityMonitor monitor) {
		mBase.removeMonitor(monitor);
	}

	@Override
	public boolean invokeMenuActionSync(Activity targetActivity, int id,
			int flag) {
		return mBase.invokeMenuActionSync(targetActivity, id, flag);
	}

	@Override
	public boolean invokeContextMenuAction(Activity targetActivity, int id,
			int flag) {
		return mBase.invokeContextMenuAction(targetActivity, id, flag);
	}

	@Override
	public void sendStringSync(String text) {
		mBase.sendStringSync(text);
	}

	@Override
	public void sendKeySync(KeyEvent event) {
		mBase.sendKeySync(event);
	}

	@Override
	public void sendKeyDownUpSync(int key) {
		mBase.sendKeyDownUpSync(key);
	}

	@Override
	public void sendCharacterSync(int keyCode) {
		mBase.sendCharacterSync(keyCode);
	}

	@Override
	public void sendPointerSync(MotionEvent event) {
		mBase.sendPointerSync(event);
	}

	@Override
	public void sendTrackballEventSync(MotionEvent event) {
		mBase.sendTrackballEventSync(event);
	}

	@Override
	public Application newApplication(ClassLoader cl, String className,
			Context context) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return mBase.newApplication(cl, className, context);
	}

	@Override
	public void callApplicationOnCreate(Application app) {
		mBase.callApplicationOnCreate(app);
	}

	@Override
	public void callActivityOnDestroy(Activity activity) {
		mBase.callActivityOnDestroy(activity);
	}

	@Override
	public void callActivityOnRestoreInstanceState(Activity activity,
			Bundle savedInstanceState) {
		mBase.callActivityOnRestoreInstanceState(activity, savedInstanceState);
	}

	@Override
	public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
		mBase.callActivityOnPostCreate(activity, icicle);
	}

	@Override
	public void callActivityOnNewIntent(Activity activity, Intent intent) {
		mBase.callActivityOnNewIntent(activity, intent);
	}

	@Override
	public void callActivityOnStart(Activity activity) {
		mBase.callActivityOnStart(activity);
	}

	@Override
	public void callActivityOnRestart(Activity activity) {
		mBase.callActivityOnRestart(activity);
	}

	@Override
	public void callActivityOnResume(Activity activity) {
		mBase.callActivityOnResume(activity);
	}

	@Override
	public void callActivityOnStop(Activity activity) {
		mBase.callActivityOnStop(activity);
	}

	@Override
	public void callActivityOnSaveInstanceState(Activity activity,
			Bundle outState) {
		mBase.callActivityOnSaveInstanceState(activity, outState);
	}

	@Override
	public void callActivityOnPause(Activity activity) {
		mBase.callActivityOnPause(activity);
	}

	@Override
	public void callActivityOnUserLeaving(Activity activity) {
		mBase.callActivityOnUserLeaving(activity);
	}

	@Override
	public void startAllocCounting() {
		mBase.startAllocCounting();
	}

	@Override
	public void stopAllocCounting() {
		mBase.stopAllocCounting();
	}

	@Override
	public Bundle getAllocCounts() {
		return mBase.getAllocCounts();
	}

	@Override
	public Bundle getBinderCounts() {
		return mBase.getBinderCounts();
	}

}
