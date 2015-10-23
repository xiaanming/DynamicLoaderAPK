package com.dynamic.framework;

import java.lang.Throwable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

public class DelegateResources extends Resources {
	private static final String TAG = DelegateResources.class.getSimpleName();
	
	private static Set<String> assetPathsHistory;
	private static boolean ignoreOpt = false;
	private final static String[] ignoreOptBrands = { "Sony", "SEMC" };

	static {
		for (String brand : ignoreOptBrands) {
			if (Build.BRAND.equalsIgnoreCase(brand)) {
				ignoreOpt = true;
				break;
			}
		}
	}

	public DelegateResources(AssetManager asset, Resources res) {
		super(asset, res.getDisplayMetrics(), res.getConfiguration());
	}

	public static void newDelegateResources(Application application,
			Resources res, String newPath) throws Exception {
		Set<String> assetPaths;

		// above 5.0
		if (!ignoreOpt && Build.VERSION.SDK_INT > 20
				&& (assetPathsHistory != null)) {
			// not 1st time, just append the path
			AssetManager asset = application.getAssets();
			if (!TextUtils.isEmpty(newPath)
					&& !assetPathsHistory.contains(newPath)) {

				Class<?> paramterTypes[] = { String.class };
				RefInvoke.invokeMethod(asset, "addAssetPath", paramterTypes,
						newPath);

				assetPathsHistory.add(newPath);
			}
			return;
		}

		if ((assetPaths = generateNewAssetPaths(application, newPath)) == null) {
			// Already added, just return
			return;
		}

		AssetManager asset = AssetManager.class.newInstance();
		for (String path : assetPaths) {
			try {
				Class<?> paramterTypes[] = { String.class };
				if (Integer.parseInt((RefInvoke.invokeMethod(asset,
						"addAssetPath", paramterTypes, path)).toString()) == 0) {
					// if failed try three times
					int i = 0;
					for (i = 0; i < 3; i++) {
						if (Integer.parseInt((RefInvoke.invokeMethod(asset,
								"addAssetPath", paramterTypes, newPath))
								.toString()) != 0) {
							break;
						}
					}
				}
			} catch (NumberFormatException e) {
			}
		}


		Resources delegateResources = null;
		// fixed bug in xiaomi3
		if (res != null
				&& (res.getClass().getName()
						.equals("android.content.res.MiuiResources"))
				&& (Build.VERSION.SDK_INT <= 20)) {
			Class<?> miuiResClass = Class
					.forName("android.content.res.MiuiResources");
			Constructor<?> miuiCons = miuiResClass.getDeclaredConstructor(
					AssetManager.class, DisplayMetrics.class,
					Configuration.class);
			miuiCons.setAccessible(true);
			delegateResources = (Resources) miuiCons.newInstance(asset,
					res.getDisplayMetrics(), res.getConfiguration());
		} else {
			delegateResources = new DelegateResources(asset, res);
		}
		
		RuntimeVariable.delegateResources = delegateResources;

		assetPathsHistory = assetPaths;
		
		Log.i(TAG, assetPathsHistory.toString());
	}


	public static List<String> getOriginAssetsPath(AssetManager manager) {
		List<String> paths = new ArrayList<String>();
		try {
			Method method = manager.getClass().getDeclaredMethod(
					"getStringBlockCount");
			method.setAccessible(true);
			int assetsPathCount = (Integer) method.invoke(manager);
			for (int x = 0; x < assetsPathCount; x++) {
				// Cookies map to string blocks starting at 1
				String assetsPath = (String) manager.getClass()
						.getMethod("getCookieName", int.class)
						.invoke(manager, x + 1);
				if (!TextUtils.isEmpty(assetsPath)) {
					paths.add(assetsPath);
				}
			}
			return paths;
		} catch (Throwable e) {
			return new ArrayList<String>();
		}
	}


	private static List<String> mOriginAssetsPath = null;
	private static final String WebViewGoogleAssetPath = "/system/app/WebViewGoogle/WebViewGoogle.apk";

	private static Set<String> generateNewAssetPaths(Application application,
			String path) {
		Set<String> assetPaths = null;

		if ((path != null)
				&& (assetPathsHistory != null && assetPathsHistory
						.contains(path))) {
			// already added, just return null
			return null;
		}

		assetPaths = new LinkedHashSet<String>();
		assetPaths.add(application.getApplicationInfo().sourceDir);
		if (ignoreOpt && Build.VERSION.SDK_INT > 20) {
			assetPaths.add(WebViewGoogleAssetPath);
		}

		// get original asset paths on android above 5.0 in case webview not
		// added
		try {
			if (mOriginAssetsPath == null && Build.VERSION.SDK_INT > 20
					&& !ignoreOpt) {
				mOriginAssetsPath = getOriginAssetsPath(application
						.getResources().getAssets());
				assetPaths.addAll(mOriginAssetsPath);
			}
		} catch (Throwable e) {
		}

		if (assetPathsHistory != null)
			assetPaths.addAll(assetPathsHistory);

		if (!TextUtils.isEmpty(path))
			assetPaths.add(path);

		return assetPaths;
	}

	public static class ResInfo {
		public ResInfo(String type, int resId) {
			this.resId = resId;
			this.type = type;
		}

		public int resId;
		public String type;
	}

}
