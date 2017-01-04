package com.dayang.auto.common.manager;

import java.util.HashMap;

import com.dayang.auto.common.pojo.ObjectPojo;

/**
 * 类描述：测试对象管理类，对应bussiness-*.xml
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class ObjectManager {

	public HashMap<String, ObjectPojo> map = new HashMap<String, ObjectPojo>();

	public static ObjectManager instance;

	/**
	 * 获取实例
	 * @return
	 */
	public static ObjectManager getInstance() {
		if (instance == null) {
			instance = new ObjectManager();
		}
		return instance;
	}

	/**
	 * 获取获取case数据
	 * @return
	 */
	public static HashMap<String, ObjectPojo> getMap() {
		return getInstance().map;
	}

	/**
	 * 根据key获取case对象
	 * @param key
	 * @return
	 */
	public ObjectPojo getCaseByKey(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

}
