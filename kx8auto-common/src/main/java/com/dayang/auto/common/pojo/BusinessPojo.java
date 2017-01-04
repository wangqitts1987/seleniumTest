package com.dayang.auto.common.pojo;

import java.util.List;

/**
 * 类描述：Bussiness实体类，对应bussiness-*.xml，每个bussiness包含一个运行实例的list
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class BusinessPojo {

	/**名字*/
	private String name;

	/**case集合*/
	private List<BasicInstance> caseInstList;

	public BusinessPojo() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BasicInstance> getCaseInstList() {
		return caseInstList;
	}

	public void setCaseList(List<BasicInstance> caseInstList) {
		this.caseInstList = caseInstList;
	}

	public BusinessPojo(String name, List<BasicInstance> caseInstList) {
		super();
		this.name = name;
		this.caseInstList = caseInstList;
	}

}
