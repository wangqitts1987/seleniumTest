package com.dayang.auto.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.dayang.auto.common.ElementAction;
import com.dayang.auto.common.ReadConfig4Xml;
import com.dayang.auto.common.driver.LogTools;
import com.dayang.auto.common.driver.NewBrowserEmulator;
import com.dayang.auto.common.manager.BusinessManager;
import com.dayang.auto.common.manager.CaseManager;
import com.dayang.auto.common.manager.DataManager;
import com.dayang.auto.common.manager.ExtentManager;
import com.dayang.auto.common.manager.ObjectManager;
import com.dayang.auto.common.manager.StepManager;
import com.dayang.auto.common.pojo.BasicInstance;
import com.dayang.auto.common.pojo.BusinessPojo;
import com.dayang.auto.common.pojo.CaseInstPojo;
import com.dayang.auto.common.pojo.CasePojo;
import com.dayang.auto.common.pojo.DataPojo;
import com.dayang.auto.common.pojo.ObjectPojo;
import com.dayang.auto.common.pojo.RuntimeSet;
import com.dayang.auto.common.pojo.StepPojo;
import com.dayang.auto.conf.ConfBaseUtils;
import com.dayang.auto.conf.ConfDomainUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 类描述：工具类
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */

public class Kx8AutoCommonUtil {
	
	private static final Logger LOGGER = Logger.getLogger(Kx8AutoCommonUtil.class);
	
	/**配置文件点名称-Objects*/
	public static final String NODE_OBJECTS = "objects";
	/**配置文件点名称-Cases*/
	public static final String NODE_CASES = "cases";
	/**配置文件点名称-Steps*/
	public static final String NODE_STEPS = "steps";
	/**配置文件点名称-Bussinesses*/
	public static final String NODE_BUSSINESSES = "bussinesses";
	/**配置文件点名称-run*/
	public static final String NODE_RUN = "run";
	/**配置文件点名称-data*/
	public static final String NODE_DATA = "data";
	/**配置文件点名称-run*/
	public static final String NODE_CONFIG = "configData";
	
	public static final String INSTANCE_CASE ="case";
	public static final String INSTANCE_CONDITION ="condition";
	public static final String INSTANCE_WLOOP ="wloop";
	
	public static List<String> readXmlList = new ArrayList<String>(); 
	/**
	 * 初始化配置数据
	 * @param modelName 模块名称，比如case-login.xml的模块名称就是login
	 */
	public static void initConfigData(String runConfPath){
		ReadConfig4Xml readConfig = new ReadConfig4Xml();
		readConfig.read(runConfPath);
		

		if(readXmlList != null && readXmlList.size() > 0){
			for (int i = 0; i < readXmlList.size(); i++) {
				String model = readXmlList.get(i);
				
				String objectPath = "object/object-"+model+".xml";
				String stepPath = "step/step-"+model+".xml";
				String casePath = "case/case-"+model+".xml";
				String bussinessPath = "bussiness/bussiness-"+model+".xml";
				String dataPath = "data/data-"+model+".xml";
				
				readConfig.read(objectPath);
				readConfig.read(stepPath);
				readConfig.read(casePath);
				readConfig.read(bussinessPath);
				readConfig.read(dataPath);
			}
			Map<String, ObjectPojo> objectMap = ObjectManager.getMap();
			Map<String, StepPojo> stepMap = StepManager.getMap();
			Map<String, CasePojo> caseMap = CaseManager.getMap();
			Map<String, BusinessPojo> busiMap = BusinessManager.getBusiMap();
			Map<String, DataPojo> dataMap = DataManager.getMap();
			LOGGER.info("DataMap:" + JSON.toJSONString(dataMap));
			LOGGER.info("busiMap:" + JSON.toJSONString(busiMap));
			LOGGER.info("caseMap:" + JSON.toJSONString(caseMap));
			LOGGER.info("stepMap:" + JSON.toJSONString(stepMap));
			LOGGER.info("objectMap:" + JSON.toJSONString(objectMap));
			
		}else{
			LOGGER.error("配置数据不存在.....");
		}
	}
	
	/**
	 * 运行testcase 
	 * @param runConfPath run配置文件路径
	 */
	public static void runTestCase4Chrome(String runConfPath){
		

		LogTools.delScreenShot();
	
		ReadConfig4Xml readConfig = new ReadConfig4Xml();
		readConfig.read(runConfPath);
		
		NewBrowserEmulator be = new NewBrowserEmulator();
		ExtentManager.setBrowserEmulator(be);
		ElementAction action = new ElementAction(be);
		RuntimeSet.initMethodMap(action);
		
		WebDriver driver = be.getBrowserCore();
		driver.get(ConfDomainUtils.getDomain());
		
		List<BasicInstance> caseList = RuntimeSet.getInstList();
		
		ExtentReports extent =ExtentManager.getExtentInstance();
		if(caseList != null){
			for (BasicInstance caseInstPojo : caseList) {
				/**
				 * 对于每个运行实例，新增一个ExtentTest来记录这个实例运行时的状态
				 * 包括实例执行是否成功、错误时的提示信息和错误时的截图路径
				 * 每个实例执行时，都会将新增的ExtentTest设置为当前的ExtentTest
				 * 在其他位置执行错误时，会获取当前的ExtentTest，并将错误信息和路径写入  
				 */
				if (caseInstPojo == null){
					continue;
				}
				String name = caseInstPojo.GetInstanceName();
				ExtentTest test = ExtentManager.startExtentTest(name);

				try {
					caseInstPojo.RunInstance(test);
					
				} catch (Exception e) {
					LOGGER.error(e.getMessage(),e);
					ExtentManager.errorHandle(e);
					ExtentManager.testReport("", test);
				}
				extent.endTest(test);
				extent.flush();
			}
		}
		be.quit();
	}
}
