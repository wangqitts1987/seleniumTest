package com.dayang.auto.common.manager;

import java.util.HashMap;

import com.dayang.auto.common.pojo.StepPojo;

/**
 * 类描述：步骤管理类
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class StepManager {

	/**步骤map*/
	public HashMap<String, StepPojo> stepMap = new HashMap<String, StepPojo>();

	public static StepManager instance;

	public static StepManager getInstance() {
		if (instance == null) {
			instance = new StepManager();
		}
		return instance;
	}

	public static HashMap<String, StepPojo> getMap() {
		return getInstance().stepMap;
	}

}
