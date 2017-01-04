package com.dayang.auto.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * 类描述：获取驱动配置工具类
 * <pre>
 * -------------History------------------
 *   DATE           AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容 
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */

public class ConfGlobalUtils {
	private static final Logger LOGGER = Logger.getLogger(ConfGlobalUtils.class);
	
	
	/**配置属性文件*/
	private static final Properties properties = new Properties();
	
	static{
		try {
			InputStream inputStream = ConfGlobalUtils.class.getClassLoader().getResourceAsStream(getRespath());
			if(inputStream != null){
				properties.load(inputStream);
			}
		} catch (IOException e) {
			LOGGER.error("未找到global.properties配置文件");
		}
	}
	
	/**
	 * 获取配置文件路径
	 * @return 配置文件路径
	 */
	public static final String getRespath(){
		return "global.properties";
	}
	

	/***
	 * 读取配置
	 * @param key 配置KEY
	 * @return value 配置值
	 */
	public static final String get(String key) {
		LOGGER.info("get key:"+key+" value:"+properties.getProperty(key));
		return properties.getProperty(key);
	}
	
	/**
	 * 获取基础路径
	 * @return
	 */
    public static final String getTimeout()
    {
    	return get("TimeOut");
    }
}
