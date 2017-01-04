package com.dayang.auto.common.manager;

import java.util.HashMap;

import com.dayang.auto.common.pojo.BusinessPojo;

/**
 * 类描述：Bussiness管理类，对应bussiness-*.xml文件
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class BusinessManager {

	public HashMap<String, BusinessPojo> busiMap = new HashMap<String, BusinessPojo>();

	public static BusinessManager instance;

	public static BusinessManager getInstance() {
		if (instance == null) {
			instance = new BusinessManager();
		}
		return instance;
	}

	public static HashMap<String, BusinessPojo> getBusiMap() {
		return getInstance().busiMap;
	}
}
