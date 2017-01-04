package com.dayang.auto.common.manager;

import java.util.HashMap;

import com.dayang.auto.common.pojo.CasePojo;

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
public class CaseManager {

	public HashMap<String, CasePojo> caseMap = new HashMap<String, CasePojo>();
	public static CaseManager instance;
	

	private CaseManager() {
	}


	public static CaseManager getInstance() {
		if (instance == null) {
			instance = new CaseManager();
		}
		return instance;
	}

	public static HashMap<String, CasePojo> getMap() {
		return getInstance().caseMap;
	}
}
