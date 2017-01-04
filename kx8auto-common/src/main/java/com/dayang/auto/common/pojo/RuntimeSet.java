package com.dayang.auto.common.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dayang.auto.common.ElementAction;


/**
 * 类描述：运行时数据集
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class RuntimeSet {

	//private static RuntimeSet instance;
	//杩愯鏃剁殑瀹炰緥锛屽湪杩愯鏃跺彇鍑簂ist鐨勫疄渚嬩竴涓�鎵ц
	private static List<BasicInstance> list = new ArrayList<BasicInstance>();
	//鐢ㄤ簬淇濆瓨杩愯鏃剁殑鍙橀噺
	private static HashMap<String,String> runtimeVar = new HashMap<String, String>();
	//private static List<String> dataList = new ArrayList<>();
    
	private static ElementAction action;
	/**
	public static RuntimeSet getInstance() {
		if (instance == null) {
			instance = new RuntimeSet();
		}
		return instance;
	}
    */
	
	private static HashMap<String,Method> methodMap;
	
	public static ElementAction getAction(){
		return action;
	}
	
	public static HashMap<String,Method> initMethodMap(ElementAction action){
		RuntimeSet.action = action;
		if (methodMap==null){
			methodMap = new HashMap<String,Method>();
			Class clazz = action.getClass();
			Method[] methods;
			try {
				methods = clazz.getMethods();
				for(Method m : methods){
					methodMap.put(m.getName(), m);
				}
				/**Method method = findMethod(methods,"type");
				if (method == null){
					System.out.println("NoSuchMethod");;
				}
				Class<?>[] test = method.getParameterTypes();
				Parameter[] ps = method.getParameters();
				for (Class<?> t:test){
					System.out.println(t.getName());
				}
				for (Parameter p: ps ){
					System.out.println(p.getName());
				}*/
			}
			catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return methodMap;
	}
	
	
	public static Method getMethod(String name){
		 return methodMap.get(name);
	}
	
	public static List<BasicInstance> getInstList() {
		return list;
	}

	public static HashMap<String,String> GetRuntimeVar()
	{
		return runtimeVar;
	}
    /**
	public static List<String> getDataList() {
		return dataList;
	}*/
	/**
	 * 娣诲姞鏁版嵁鍒伴泦鍚�
	 * @param list
	 
	public static void addDatalist(List<String> list){
		dataList.addAll(list);
	}
    */
}
