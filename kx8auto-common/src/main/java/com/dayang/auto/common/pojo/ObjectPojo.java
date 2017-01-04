package com.dayang.auto.common.pojo;

/**
 * 类描述：测试对象实体类，对应object-*.xml
 * 
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class ObjectPojo {
	/** key-对应表单的name属性 */
	public String name;
	/** xpath路径 */
	public String xpath;

	public ObjectPojo() {

	}

	public ObjectPojo(String name, String xpath) {
		this.name = name;
		this.xpath = xpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

}
