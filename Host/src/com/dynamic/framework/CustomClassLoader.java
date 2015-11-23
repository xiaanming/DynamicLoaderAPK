package com.dynamic.framework;

import dalvik.system.DexClassLoader;

public class CustomClassLoader extends DexClassLoader{





	public CustomClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
	}
	
	
	

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("name = " + name);
		
		
		return super.findClass(name);
	}
	
	
	

}
