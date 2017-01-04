package com.dayang.auto.common.pojo;

import org.apache.log4j.Logger;

import com.dayang.auto.common.ElementAction;
import com.dayang.auto.common.ReadConfig4Xml;
import com.dayang.auto.common.manager.ExtentManager;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Test;
/**
 * 类描述：ConditionInstPojo是条件判断的运行实例，由一个判断条件是否为真的BasicData数据、
 * 一个条件为真时执行的case实例和一个条件为假时执行的case实例
 *
 */
public class ConditionInstPojo implements BasicInstance{
	
	private BasicData flag;
	private CaseInstPojo trueInst;
	private CaseInstPojo falseInst;
    
	private static final Logger LOGGER = Logger.getLogger(ReadConfig4Xml.class);
    public ConditionInstPojo(){

    }
    public ConditionInstPojo(String value,String type,CaseInstPojo trueInst,CaseInstPojo falseInst){

    	this.flag = new BasicData(value,type);
    	this.trueInst = trueInst;
    	this.falseInst =falseInst;
    }
    public String GetInstanceName(){
		return "Condition";
	}
    /**
     * 执行条件实例，先获取条件判断的值，生成一个新的child ExtentTest，并将新的ExtentTest设置为当前ExtentTest，
     * 将新的ExtentTest设置为之前的ExtentTest的child，根据条件的值执行对应的用例实例，用例实例执行完后，根据child ExtentTest
     * 中Test的status的值来设置 parent ExtentTest的Test的status的值，最后关闭child ExtentTest
     */
	public void RunInstance(ExtentTest test){
		Boolean condition = Boolean.parseBoolean(this.flag.GetValue());
		LOGGER.info("this.flag.GetValue() " +this.flag.GetValue());
		//test.setStatus(LogStatus.INFO);
		
		ExtentTest ttest ;
		if (condition == Boolean.TRUE){
			ttest = ExtentManager.startExtentTest("trueInst "+trueInst.GetInstanceName());
			test.appendChild(ttest);
			
			trueInst.RunInstance(ttest);
		}
		else{
			ttest = ExtentManager.startExtentTest("falseInst "+falseInst.GetInstanceName());
			test.appendChild(ttest);
			
			falseInst.RunInstance(ttest);
		}
		ExtentManager.setParentStatus((Test) ttest.getTest());
		ExtentManager.getExtentInstance().endTest(ttest);
		
	}
	public CaseInstPojo GetTcase(){
		return this.trueInst;
	}
}
