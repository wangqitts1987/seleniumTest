package com.dayang.auto.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 类描述：获取基础配置工具类
 * <pre>
 * -------------History------------------
 *   DATE           AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容 
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */

public class ConfBaseUtils {
	private static final Logger LOGGER = Logger.getLogger(ConfBaseUtils.class);
	
	
	/**配置属性文件*/
	private static final Properties properties = new Properties();
	/**基础配置属性文件*/
	private static final Properties baseProperties = new Properties();
	
	
	
	static{
		try {
			InputStream inputStream = ConfBaseUtils.class.getClassLoader().getResourceAsStream(getRespath());
			if(inputStream != null){
				properties.load(inputStream);
			}
			InputStream baseInputStream = ConfBaseUtils.class.getClassLoader().getResourceAsStream("conf_base.properties");
			if(baseInputStream != null){
				baseProperties.load(baseInputStream);
			}
		} catch (IOException e) {
			LOGGER.error("未找到conf.properties配置文件");
		}
	}
	
	/**
	 * 获取配置文件路径
	 * @return 配置文件路径
	 */
	public static final String getRespath(){
		return "conf.properties";
	}
	

	/***
	 * 读取配置
	 * @param key 配置KEY
	 * @return value 配置值
	 */
	public static final String get(String key) {
		String confVal = properties.getProperty(key);
		if(StringUtils.isEmpty(confVal)){
			confVal = baseProperties.getProperty(key);
		}
		return confVal;
	}
	
	/**
	 * 获取测试报告的输出路径
	 * @return
	 */
	public static final String getTestReportPath(){
		return get("report.path");
	}
}
