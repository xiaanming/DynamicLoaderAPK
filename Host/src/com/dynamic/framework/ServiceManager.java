package com.dynamic.framework;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ServiceManager {
	
	private static List<String> mServiceClassNames =  Collections.synchronizedList(new LinkedList<String>());
	
	
	public static void addServiceComponent(String className ){
		mServiceClassNames.add(className);
	}
	
	
	public static String getServiceComponent(){
		if(mServiceClassNames.isEmpty()){
			return null;
		}
		
		return mServiceClassNames.remove(mServiceClassNames.size() - 1);
	}

}
