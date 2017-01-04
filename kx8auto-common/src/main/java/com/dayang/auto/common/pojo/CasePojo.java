package com.dayang.auto.common.pojo;

import java.util.List;

/**
 * 类描述：case实体类，对应case-*.xml，每个case包含一个step的list
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class CasePojo {

	/**case名字*/
	private String caseName;
	/**case的step的list*/
	private List<StepPojo> stepList;

	public CasePojo(List<StepPojo> stepList) {
		this.stepList = stepList;
	}

	public CasePojo() {

	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public List<StepPojo> getStepList() {
		return stepList;
	}

	public void setStepList(List<StepPojo> stepList) {
		this.stepList = stepList;
	}

}
