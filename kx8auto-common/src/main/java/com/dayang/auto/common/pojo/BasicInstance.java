package com.dayang.auto.common.pojo;

import com.dayang.auto.common.ElementAction;
import com.relevantcodes.extentreports.ExtentTest;

/**
 * 接口BasicInstance用于实例的执行，所有实现BasicInstance接口的类实现自己的RunInstance，
 * 每个bussiness有一个运行实例BasicInstance的list，根据实例的不同执行自己的RunInstance
 * 
 */
public interface BasicInstance {

	public void RunInstance(ExtentTest test);
	public String GetInstanceName();
}
