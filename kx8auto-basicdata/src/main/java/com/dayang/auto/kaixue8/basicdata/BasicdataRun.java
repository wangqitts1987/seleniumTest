package com.dayang.auto.kaixue8.basicdata;

import com.dayang.auto.common.util.Kx8AutoCommonUtil;

/**
 * 类描述：基础数据应用启动类
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */

public class BasicdataRun {
	
	public static void main(String[] args) {
		
		Kx8AutoCommonUtil.initConfigData("config.xml");
		Kx8AutoCommonUtil.runTestCase4Chrome("run.xml");
	}
	
//	@Test(testName="main")
//	public void run4TestNg(){
//		Kx8AutoCommonUtil.initConfigData("common","login");
//		Kx8AutoCommonUtil.runTestCase4Chrome("run.xml");
//	}
	
}
