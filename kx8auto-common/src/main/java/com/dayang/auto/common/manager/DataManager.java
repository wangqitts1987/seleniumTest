package com.dayang.auto.common.manager;

import java.util.HashMap;

import com.dayang.auto.common.pojo.DataPojo;

/**
 * 类描述：CasePojo管理类，对应case-*.xml
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class DataManager {

	public HashMap<String, DataPojo> dataMap = new HashMap<String, DataPojo>();
	public static DataManager instance;
	
	private DataManager() {
	}


	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	public static HashMap<String, DataPojo> getMap() {
		return getInstance().dataMap;
	}
}
