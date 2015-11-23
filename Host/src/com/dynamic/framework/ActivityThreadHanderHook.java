package com.dynamic.framework;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class ActivityThreadHanderHook implements Handler.Callback {
	public static final int CREATE_SERVICE          = 114;
    public static final int SERVICE_ARGS            = 115;
    public static final int STOP_SERVICE            = 116;
	
	
    private Handler.Callback mTarget = null;
	
	
	public ActivityThreadHanderHook(Handler.Callback callback){
		this.mTarget = callback;
	}


	@Override
	public boolean handleMessage(Message msg) {
		System.out.println("ActivityThreadHanderHook msg.what = " + msg.what);
		switch(msg.what){
		case CREATE_SERVICE:
			String componentName = ServiceManager.getServiceComponent();
			if(!TextUtils.isEmpty(componentName)){
				Object createServiceData = msg.obj;
				System.out.println("createServiceData = " + createServiceData);
				ServiceInfo serviceInfo = (ServiceInfo) RefInvoke.getFieldObject(createServiceData, "info");
				
				serviceInfo.name = componentName;
				
				RefInvoke.setFieldObject(createServiceData, "info", serviceInfo);
			}
			
			
			break;
		case STOP_SERVICE:
			break;
		}
		
		
		return mTarget == null ? false : mTarget.handleMessage(msg);
		
		
	}

	
	
	
	

}
