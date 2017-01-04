package com.dayang.auto.common.pojo;

import java.util.List;

import com.dayang.auto.common.ElementAction;
import com.dayang.auto.common.manager.ExtentManager;
import com.dayang.auto.common.util.Kx8AutoCommonUtil;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Test;
/**
 * 类描述：WLoopInstPojo是循环的运行实例，由一个条件判断的值、
 *      一个条件为真时的执行实例列表组成，不支持循环嵌套循环
 *      执行实例列表中需有步骤重置条件判断的值，否则会进入死循环
 */
public class WLoopInstPojo implements BasicInstance{
	
	List<BasicInstance> list;
	BasicData flag;
	public WLoopInstPojo(){

	}
	public WLoopInstPojo(String value,String type,List<BasicInstance> list){

		this.flag = new BasicData(value,type);
		this.list = list;
	}
    public String GetInstanceName(){
		return "WLoop";
	}
	public void RunInstance(ExtentTest test){
		System.out.println("wloop this.flag.GetValue()" + this.flag.GetValue());
		int i = 0;
		//while (Boolean.parseBoolean(this.flag.GetValue()))
		
		while (Boolean.TRUE)
		{
			if (i==3)
				break;

			ExtentTest itest = ExtentManager.startExtentTest("wloop "+i);
			//itest.setStatus(LogStatus.PASS);
			//ExtentManager.SetCurrentTest(itest);
			test.appendChild(itest);
			for(BasicInstance instance : this.list){
				ExtentTest wtest = ExtentManager.startExtentTest(instance.GetInstanceName());
				//ExtentManager.SetCurrentTest(wtest);
				itest.appendChild(wtest);
				instance.RunInstance(wtest);

				ExtentManager.setParentStatus((Test) wtest.getTest());
				ExtentManager.getExtentInstance().endTest(wtest);
			}
			ExtentManager.getExtentInstance().endTest(itest);
			i++;
		}
	}
}
