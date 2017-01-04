package com.dayang.auto.common.pojo;

/**
 * 类描述：步骤实体类
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class StepPojo {
	/** 步骤名称 */
	public String objectName;

	/** 动作名称 */
	public String actionName;
    public String stepName;
	public StepPojo() {

	}

	public StepPojo(String stepName, String objectName, String actionName) {
		this.stepName = stepName;
		this.objectName = objectName;
		this.actionName = actionName;
	}

	public String getStepName(){
		return stepName;
	}
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
