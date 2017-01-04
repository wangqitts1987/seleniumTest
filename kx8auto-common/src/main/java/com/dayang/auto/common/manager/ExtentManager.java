package com.dayang.auto.common.manager;

import java.util.HashMap;
import java.util.Map;

import com.dayang.auto.common.driver.NewBrowserEmulator;
import com.dayang.auto.conf.ConfBaseUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.ScreenCapture;
import com.relevantcodes.extentreports.model.Test;

/**
 * ExtentManager类用来管理extentReport和MyExtentTest，其中MyExtentTest是对Extenttest的封装，增加了错误信息和截图
 * 
 */
public class ExtentManager {

	
	private static ExtentReports extent;
    private static NewBrowserEmulator be;
	
    /** curTest用来保存当前正在执行的ExtentTest */
	private static ExtentTest curTest ;
	
	private static String errorInfo;
	private static String screenShotPath;
	public static ExtentReports getExtentInstance(){
		if (extent==null){
			extent = new ExtentReports(ConfBaseUtils.getTestReportPath(), Boolean.TRUE,NetworkMode.OFFLINE);
		}
		return extent;
	}
	
	public static void setBrowserEmulator(NewBrowserEmulator be){
		ExtentManager.be = be;
	}
	/** 新增一个ExtentTest，并将其设置为当前活动的ExtentTest */
	public static ExtentTest startExtentTest(String name){
		ExtentTest test = extent.startTest(name);
		curTest = test;
		return test;
	}
	
	/**
	 * 处理错误，设置Test的status为fail，保存路径和错误描述
	 * @param String des ：错误描述
	 * @param String path：错误截图路劲
	 */
	public static void errorHandle(String des,String path){
		//curTest.getTest().setStatus(LogStatus.FAIL);
		Test t =(Test) curTest.getTest();
		t.setStatus(LogStatus.FAIL);
		errorInfo = des;
		screenShotPath = path;
		//t.setDescription(des);
		//ScreenCapture capture = new ScreenCapture();
		//capture.setSource(path);
		//t.setScreenCapture(capture);
	}
	public static void errorHandle(Throwable e){
		String path = be.getShotpath();
		String des = e.toString();
		errorHandle(des,path);
	}
	public static void errorHandle(String des){
		String path = be.getShotpath();
		errorHandle(des,path);
	}
	
	public static ExtentTest getCurrentTest(){
		return curTest;
	}


	public static void SetCurrentTest(ExtentTest test){
		curTest = test;
	}
	/**
	 * 设置父ExtentTest中Test的status，如果父Test还存在父节点，则递归向上设置
	 * @param Test t : ExtentTest的成员变量Test test，可以通过getTest获得
	 */
    public static void setParentStatus(Test t){
    	//Test t = (Test) test.getTest();
    	if (t.isChildNode){
    		Test parent = t.getParentTest();
    		findStatus(parent,t.getStatus());
    	    setParentStatus(parent);
    	}
    }
    
    /**
     * 使用logStatus来设置Test中的status的状态，只能使用高状态来设置低状态的Test
     * @param Test t : ExtentTest的成员变量Test test，可以通过getTest获得
     * @param LogStatus logStatus: 状态
     */
    private static void findStatus(Test t,LogStatus logStatus) {
        if (t.getStatus() == LogStatus.FATAL) return;
        
        if (logStatus == LogStatus.FATAL) {
            t.setStatus(logStatus);
            return;
        }
        
        if (t.getStatus() == LogStatus.FAIL) return;
        
        if (logStatus == LogStatus.FAIL) {
        	t.setStatus(logStatus);
            return;
        }
        
        if (t.getStatus() == LogStatus.ERROR) return;
        
        if (logStatus == LogStatus.ERROR) {
        	t.setStatus(logStatus);
            return;
        }
        
        if (t.getStatus() == LogStatus.WARNING) return;
        
        if (logStatus == LogStatus.WARNING) {
        	t.setStatus(logStatus);
            return;
        }
        
        if (t.getStatus() == LogStatus.SKIP) return;
        
        if (logStatus == LogStatus.SKIP) {
        	t.setStatus(logStatus);
            return;
        }
        
        if (t.getStatus() == LogStatus.PASS) return;
        
        if (logStatus == LogStatus.PASS) {
        	t.setStatus(logStatus);
            return;
        }        
        
        if (t.getStatus() == LogStatus.INFO) return;
        
        if (logStatus == LogStatus.INFO) {
        	t.setStatus(logStatus);
            return;
        }
        
        t.setStatus(LogStatus.UNKNOWN);
    }
    
    /**
     * 输出报告的步骤
     * @param String text ：输出时需添加的string
     * @param ExtentTest test ： 
     */
	public static void testReport(String text,ExtentTest test){
		LogStatus status = test.getTest().getStatus();
		//test.addScreenCapture(screenShotPath);
		if (status == LogStatus.FAIL){
			//text += test.getTest().getDescription();
			//text += test.addScreenCapture(((Test)test.getTest()).get)
			text += errorInfo;
			text += test.addScreenCapture(screenShotPath);
		}
		else{
			test.getTest().setStatus(LogStatus.PASS);
		}
        test.log(test.getTest().getStatus(),text);
	}
}
