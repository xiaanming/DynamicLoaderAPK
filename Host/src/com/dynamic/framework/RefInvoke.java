package com.dynamic.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class RefInvoke {
	public static Object invokeStaticMethod(String className,
			String methodName, Class<?>[] parameterTypes, Object... pareVaules) {

		try {
			Class<?> cls = Class.forName(className);
			Method method = cls.getMethod(methodName, parameterTypes);
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			return method.invoke(null, pareVaules);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Object invokeMethod(String className, Object obj,
			String methodName, Class<?>[] parameterTypes, Object... pareVaules) {
		try {
			Class<?> cls = Class.forName(className);
			Method method = cls.getDeclaredMethod(methodName, parameterTypes);
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			return method.invoke(obj, pareVaules);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Object invokeMethod(Object obj, String methodName,
			Class<?>[] parameterTypes, Object... args) {
		try {
			Method method = obj.getClass()
					.getMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(obj, args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 反射得到类的静态属性（包括私有和保护）
	 * 
	 * @param class_name
	 * @param filedName
	 * @return
	 */
	public static Object getStaticFieldObject(String class_name,
			String filedName) {

		try {
			Class<?> obj_class = Class.forName(class_name);
			Field field = obj_class.getDeclaredField(filedName);
			field.setAccessible(true);
			return field.get(null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static Object getFieldObject(Object object, String fieldName) {
		Class<?> cls = object.getClass();
		try {
			Field field = cls.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(object);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static Object getFieldObject(Object object, String className, String fieldName) {
		try {
			Class<?> cls = Class.forName(className);
			Field field = cls.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(object);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void setFieldObject(Object object, String fieldName,
			Object vaule) {
		try {
			Class<?> cls = object.getClass();
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, vaule);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void setFieldObject(String className, Object object,
			String fieldName, Object vaule) {
		try {
			Class<?> cls = Class.forName(className);
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, vaule);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 设置类的静态属性（包括私有和保护）
	 * 
	 * @param class_name
	 * @param filedName
	 * @param filedVaule
	 */
	public static void setStaticObject(String class_name, String filedName,
			Object filedVaule) {
		try {
			Class obj_class = Class.forName(class_name);
			Field field = obj_class.getDeclaredField(filedName);
			field.setAccessible(true);
			field.set(null, filedVaule);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Method getMethod(String className, String methodName, Class<?>... parameterTypes){
		Class<?> cls;
		try {
			cls = Class.forName(className);
			return cls.getDeclaredMethod(methodName, parameterTypes);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
