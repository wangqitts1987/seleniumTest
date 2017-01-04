package com.dayang.auto.common;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.dayang.auto.common.driver.NewBrowserEmulator;
import com.dayang.auto.common.pojo.RuntimeSet;
import com.dayang.auto.common.util.Kx8AutoCommonUtil;
import com.relevantcodes.extentreports.ExtentTest;

/**
 * 类描述：
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class ElementAction {

	private NewBrowserEmulator be;

	private static final Logger LOGGER = Logger.getLogger(Kx8AutoCommonUtil.class);
	
	public ElementAction(){
		
	}
	
	public ElementAction(NewBrowserEmulator be){
		this.be = be;
	}
	public NewBrowserEmulator GetEmulator(){
		return be;
	}
	
	/**
	 * 打开url地址
	 * @param url地址
	 */
	public void launch(String url){
		be.open(url);
	}
	
	public void sleep(String time)
	{	
	}
	
	/**
	 * 输入文本（清空内容）
	 * @param xpath 页面元素xpath对象
	 * @param text  需要输入的内容
	 *           
	 */
	public void type(String elementXpath, String text) {
		be.type(elementXpath,text);
		//be.test.log(be.test.getRunStatus(), "type element :" +elementXpath + "text " + text);
	}
	
	/**
	 * 单击（点击）页面元素
	 * @param elementXpath 页面元素xpath对象
	 */
	public void click(String elementXpath) {
		be.click(elementXpath);
		//be.test.log(be.test.getRunStatus(), "click element :" +elementXpath );
	}
	
	/**
	 * 提交表单信息
	 * @param elementXpath 页面元素xpath对象
	 */
	public void submit(String elementXpath) {
		be.submit(elementXpath);
	}
	
	/**
	 * 通过value值，选择对应下拉列表值
	 * @param elementXpath  下拉框页面元素，xpath对象
	 * @param vale  需要选中的value值
	 */
	public void selectByValue(String elementXpath, String value){
		be.selectByValue(elementXpath, value);
	}
	
	/**
	 * 通过test值，选择对应下拉列表值
	 * @param elementXpath  下拉框页面元素，xpath对象
	 * @param test  需要选中的下拉test值
	 */
	public void selectByVisibleText(String elementXpath, String test){
		be.selectByVisibleText(elementXpath, test);
	}
	
	/**
	 * 通过下拉列表索引，选择对应下拉列表值
	 * @param elementXpath  下拉框页面元素，xpath对象
	 * @param Index  需要选中的下拉列表索引，索引从0开始
	 */
	public void selectByIndex(String elementXpath, String Index){
		be.selectByIndex(elementXpath,Integer.parseInt(Index));
	}
	
	
	/**
	 * 获取页面元素的test值传给执行变量key
	 * @param elementXpath 页面元素，xpath对象
	 * @param key 执行变量名称
	 */
	public void getRunElement(String elementXpath,String key){
		String value = be.getText(elementXpath);
		RuntimeSet.GetRuntimeVar().put(key, value);
		LOGGER.info("get element:"+elementXpath+"'s test to" + key);
	}
	
	
	/**
	 * 验证页面元素是否包含制定文本
	 * @param elementXpath 页面元素xpath对象
	 * @param text 验证的文字信息
	 */
	public void checkElementText(String elementXpath,String text){
		String value = be.getText(elementXpath);
		if (!value.equals(text)){
			be.handleFailure("element:"+value+" is not equal to "+text);
		}
	}
	
	/**
	 * 注销浏览器
	 */
	public void quit()
	{
       be.quit();
	}
	
	public void checkElementExsit(String elementXpath,String key){
		WebElement e = be.expectElementExistOrNot(true,elementXpath,1000);
		String value;
		value = Boolean.toString(e ==null);
		RuntimeSet.GetRuntimeVar().put(key, value);
		LOGGER.info("element :"+ elementXpath +" exsit :"+value);
	}
	
	
	public void Test_t(String text){
		LOGGER.info("Tcondition "+text);
	}
	public void Test_f(String text){
		LOGGER.info("Fcondition "+text);
	}


}
