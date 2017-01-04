package com.dayang.auto.common.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dayang.auto.common.ElementAction;
import com.dayang.auto.common.ReadConfig4Xml;
import com.dayang.auto.common.manager.DataManager;
import com.dayang.auto.common.manager.ExtentManager;
import com.dayang.auto.common.manager.ObjectManager;
import com.dayang.auto.common.util.Kx8AutoCommonUtil;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;

/**
 * 类描述：CaseInstPojo是case的运行实例，由一个case和一个对应的data组成
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class CaseInstPojo implements BasicInstance{
	/** case对象 */
	private CasePojo testcase;
	/** 数据名字 */
	private String dataName;
	
	private static final Logger LOGGER = Logger.getLogger(CaseInstPojo.class);
	public CaseInstPojo() {
		
	}
	

	public CaseInstPojo(CasePojo testcase, String dataName) {
		this.testcase = testcase;
		this.dataName = dataName;
	}

	public CasePojo getTestcase() {
		return testcase;
	}

	public String getDataName() {
		return dataName;
	}

	public void setTestcase(CasePojo testcase) {
		this.testcase = testcase;
	}

	public void setDataname(String dataName) {
		this.dataName = dataName;
	}
	
	public String GetInstanceName(){
		return testcase.getCaseName();
	}
	/**
	 * 用于case实例的执行，获取case中的每个step，将data与step结合，依次执行每个step
	 * 每个step有对应的objcet和action，根据action的名称执行ElementAction类中对应的函数
	 * @param action
	 */
	public void RunInstance(ExtentTest test){
		CasePojo casePojo = this.testcase;
		String dataName = this.dataName;
		
		DataPojo data = DataManager.getMap().get(dataName);
		if (dataName!=null && data == null){
			ExtentManager.errorHandle("");
			ExtentManager.testReport("case "+ casePojo.getCaseName()+" can't find data :"+dataName,test );
			LOGGER.error("case "+ casePojo.getCaseName()+" can't find data :"+dataName);
			return;
		}
		List<StepPojo> stepList = casePojo.getStepList();
		int i = 0;
		for (StepPojo step : stepList){
			String objectName = step.getObjectName();
			String actionName = step.getActionName();
			String value = getData(data,i,step.getStepName());
			if (value != null){
				i++;
			}
			ElementAction action = RuntimeSet.getAction();
			Method method = RuntimeSet.getMethod(actionName);
			if (method== null){
				errorHandle("can't find method :"+actionName,test);
				continue;
			}
			int paramCount = method.getParameterCount();
			String errorInfo = "function "+actionName+" need "+paramCount+ " param,objectName="+objectName+" value="+value;
			try{
				if (paramCount == 0){
					if(objectName == null && value == null){
						method.invoke(action);
						ExtentManager.testReport(step.stepName, test);
					}
					else{
						errorHandle(errorInfo,test);
					}
				}
				else if(paramCount == 1){
					if ((objectName == null) && (value != null)){
						method.invoke(action,value);
						ExtentManager.testReport(step.stepName, test);
					}
					else if ((objectName != null) && (value == null)){
						String xpath = ObjectManager.getMap().get(objectName).getXpath();
						method.invoke(action,xpath);
						ExtentManager.testReport(step.stepName, test);
					}
					else{
						errorHandle(errorInfo,test);
					}
				}	
				else if(paramCount == 2){
					if ((objectName != null) && (value != null)){
						String xpath = ObjectManager.getMap().get(objectName).getXpath();
						method.invoke(action,xpath,value);
						ExtentManager.testReport(step.stepName, test);
					}
					else{
						errorHandle(errorInfo,test);
					}
				}
				else{
					errorHandle(errorInfo,test);
				}
			}
			catch(Exception e){
				errorHandle(e.toString(),test);
			}
		}
	}
		
	
	/**
	 * 如果data为空或者data中保存数据的list为空，则返回null
	 * 如果index已到最后，则说明该case后面的step都不需要数据，返回null
	 * 如果这条数据的步骤名与当前step的名字不一致，则认为当前step不需要数据，返回null
	 * 
	 *@param data ：用来保存数据的类
	 *@param index：记录当前读到了第几条数据
	 *@param stepName：当前执行的step的名字 
	 */
	private String getData(DataPojo data,int index,String stepName){
		if (data == null || data.GetData() == null){
			return null;
		}
		if (index >= data.GetData().size()){
			return null;
		}
		StepInstance stepInst = data.GetData().get(index);
		if (!stepInst.GetStepName().equals(stepName) ){
			return null;
		}
		else{
			String value = stepInst.GetBasicData().GetValue();
			if (value == null){
				LOGGER.error("can't find value... type = "+stepInst.GetBasicData().getDatatype()
						+" value = "+stepInst.GetBasicData().GetValue());
			}
			return value;
		}
	}
	
	private void errorHandle(String errorInfo,ExtentTest test){
		LOGGER.error(errorInfo);
		ExtentManager.errorHandle("");
		ExtentManager.testReport(errorInfo, test);
	}
}
