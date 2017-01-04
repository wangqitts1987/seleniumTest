package com.dayang.auto.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.dayang.auto.common.manager.BusinessManager;
import com.dayang.auto.common.manager.CaseManager;
import com.dayang.auto.common.manager.DataManager;
import com.dayang.auto.common.manager.ObjectManager;
import com.dayang.auto.common.manager.StepManager;
import com.dayang.auto.common.pojo.BusinessPojo;
import com.dayang.auto.common.pojo.CaseInstPojo;
import com.dayang.auto.common.pojo.CasePojo;
import com.dayang.auto.common.pojo.ConditionInstPojo;
import com.dayang.auto.common.pojo.DataPojo;
import com.dayang.auto.common.pojo.ObjectPojo;
import com.dayang.auto.common.pojo.RuntimeSet;
import com.dayang.auto.common.pojo.StepInstance;
import com.dayang.auto.common.pojo.StepPojo;
import com.dayang.auto.common.pojo.WLoopInstPojo;
import com.dayang.auto.common.pojo.BasicData;
import com.dayang.auto.common.pojo.BasicInstance;
import com.dayang.auto.common.util.Kx8AutoCommonUtil;
import com.google.common.io.Resources;

/**
 * 类描述：读取xml配置文件
 * <pre>
 * -------------History------------------
 *   DATE                 AUTHOR         VERSION        DESCRIPTION
 *  2016年10月27日               张才胜               V01.00.001		      新增内容   
 * </pre>
 * 
 * @author <a href="mailto:zhangcs@dayanginfo.com">张才胜</a>
 */
public class ReadConfig4Xml {
	
	private static final Logger LOGGER = Logger.getLogger(ReadConfig4Xml.class);

	/**
	 * 读取xml配置文件，根据根节点的名字来选择不同的读取方式进行处理
	 * @param configPath 在classpath中的配置文件路径
	 * 
	 */
	public void read(String configPath){
		try {

			SAXReader reader = new SAXReader();
			Document document = reader.read(Resources.getResource(configPath));
			LOGGER.info("read file:"+configPath);
			Element root = document.getRootElement();
			if (root == null){
				return;
			}

			String nodeName = root.getName();
			switch (nodeName) {
			case Kx8AutoCommonUtil.NODE_CASES:
				readCase(root);
				break;
			case Kx8AutoCommonUtil.NODE_OBJECTS:
				readObject(root);
				break;
			case Kx8AutoCommonUtil.NODE_STEPS:
				readStep(root);
				break;
			case Kx8AutoCommonUtil.NODE_BUSSINESSES:
				readBussiness(root);
				break;
			case Kx8AutoCommonUtil.NODE_RUN:
				readRun(root);
				break;
			case Kx8AutoCommonUtil.NODE_DATA:
				readData(root);
				break;
			case Kx8AutoCommonUtil.NODE_CONFIG:
				readConfigData(root);
				break;
			default:
				LOGGER.error("error node name:"+nodeName);
				break;
			}
			
		} catch (Exception e) {
			//LOGGER.error(e.getMessage(),e);
		}
	}

	/**文件读取中出错*/
	private void readError(){
		System.exit(0);
	}
	/**
	 * 读取run配置,run中包含bussiness和case
	 * @param root
	 */
	
	private void readConfigData(Element root) {
		List<Element> data = root.elements();
		for (Element e : data){
			if (!e.getName().equals("data")){
				LOGGER.error("error node name :"+e.getName());
				readError();
				continue;
			}
			String name = e.attributeValue("name");
			if(name == null){
				LOGGER.error(e.getName()+" can't find attribute:name");
				readError();
				continue;
			}
			Kx8AutoCommonUtil.readXmlList.add(name);
		}

	}
	private void readRun(Element root){
		List<Element> bussinesses = root.elements();
		for (Element e : bussinesses) {
			if (e.getName().equals("bussiness")) {
				String name = e.attributeValue("name");
				if (!BusinessManager.getBusiMap().containsKey(name)){
					LOGGER.error("can't find bussiness :"+name);
					readError();
					continue;
				}
				BusinessPojo buss = BusinessManager.getBusiMap().get(name);
				for (BasicInstance ti : buss.getCaseInstList()) {
					RuntimeSet.getInstList().add(ti);
				}
			}
			else if (e.getName().equals("case")) {
				String name = e.attributeValue("name");
				if (!CaseManager.getMap().containsKey(name)) {
					LOGGER.error("can't find case : " + name);
					readError();
					continue;
				}
				CasePojo ts = CaseManager.getMap().get(name);
				String filepath = e.attributeValue("filepath");
				RuntimeSet.getInstList().add(new CaseInstPojo(ts, filepath));
			}
			else{
				LOGGER.error("error run type:"+e.getName());
				readError();
			}
		}
	}

	/**
	 * 读取Bussiness配置，一个bussiness包含由一个或多个case、condition、wloop组成，根据子节点名称的不同使用不同的读取方式进行读取
	 * @param root
	 */
	private void readBussiness(Element root) {
		List<Element> bussinesses = root.elements();

		for (Element bussiness : bussinesses) {
			String name = bussiness.attributeValue("name");
			if (BusinessManager.getBusiMap().containsKey(name)) {
				LOGGER.error("bussiness has already exsit:" + name);
				readError();
				continue;
			}
			List<Element> testInstances = bussiness.elements();
			List<BasicInstance> instanceList = new ArrayList<BasicInstance>();
			for (Element testInstance : testInstances) {
				//LOGGER.info("testInstance :" + testInstance.getName());
				if (testInstance.getName().equals(Kx8AutoCommonUtil.INSTANCE_CASE)){
					instanceList.add(readCaseInst(testInstance));
				}
				else if (testInstance.getName().equals(Kx8AutoCommonUtil.INSTANCE_CONDITION)){
					instanceList.add(readConditionInst(testInstance));
				}
				else if (testInstance.getName().equals(Kx8AutoCommonUtil.INSTANCE_WLOOP)){
					instanceList.add(readWLoopInst(testInstance));
				}
				else{
					LOGGER.error("error instance type :" + testInstance.getName());
					readError();
				}
			}
			BusinessPojo bs = new BusinessPojo(name, instanceList);
			BusinessManager.getBusiMap().put(name, bs);
		}
	}
	/**
	 * 读取bussiness中的case实例，每个case实例对应一个case名和一个data名
	 * @param root
	 */
	private CaseInstPojo readCaseInst(Element e){
		
		String casename = e.attributeValue("name");
		String data = e.attributeValue("data");
		if (casename == null){
			LOGGER.error("can't get name attribute of case");
			readError();
			return null;
		}
			
		if (!CaseManager.getMap().containsKey(casename)) {
			LOGGER.error("can't find case :" + casename);
			readError();
			return null;
		}
		CasePojo testcase = CaseManager.getMap().get(casename);
		CaseInstPojo instance = new CaseInstPojo(testcase, data);
		return instance;
	}
	/**
	 * 读取condition实例，每个condition包含3个部分：条件是否为真、条件为真时执行的tcase、条件为假时执行的fcase
	 * @param root
	 */
	private ConditionInstPojo readConditionInst(Element e){
		
		String value = e.attributeValue("value");
		String type = e.attributeValue("type");
		CaseInstPojo tcase = null;
		CaseInstPojo fcase = null;
		List<Element> testInstances = e.elements();
		for (Element instance : testInstances){
			//LOGGER.info("condition case :" + instance.getName());
			if (instance.getName().equals("tcase")){
				tcase = readCaseInst(instance);
			}
			else if (instance.getName().equals("fcase")){
				fcase = readCaseInst(instance);
			}
			else{
				LOGGER.error("error condition case :" + instance.getName());
				readError();
			}
		}
		return new ConditionInstPojo(value,type,tcase,fcase);
	}
	/**
	 * 读取wloop实例，每个wloop包含1个或多个case、condition
	 * @param root
	 */
	private WLoopInstPojo readWLoopInst(Element e){
		
		String value = e.attributeValue("value");
		String type = e.attributeValue("type");
		List<BasicInstance> list = new ArrayList<BasicInstance>();
		List<Element> testInstances = e.elements();
		for (Element instance : testInstances){
			if (instance.getName().equals(Kx8AutoCommonUtil.INSTANCE_CASE)){
				list.add(readCaseInst(instance));
			}
			else if(instance.getName().equals(Kx8AutoCommonUtil.INSTANCE_CONDITION)){
				list.add(readConditionInst(instance));
			}
			else{
				LOGGER.error("error Wloop :" + instance.getName());
				readError();
			}
		}
		return new WLoopInstPojo(value,type,list);
	}

	/**
	 * 读取case配置，每个case包含一个或多个step
	 * @param root
	 */
	private void readCase(Element root) {
		List<Element> cases = root.elements();

		for (Element testcase : cases) {
			String casename = testcase.attributeValue("name");
			if (casename == null){
				LOGGER.error("can't get name attribute of case");
				readError();
			}
			if (CaseManager.getMap().containsKey(casename)) {
				LOGGER.error("case has already exsit:" + casename);
				readError();
				continue;
			}
			List<Element> steps = testcase.elements();
			List<StepPojo> steplist = new ArrayList<StepPojo>();
			for (Element step : steps) {
				String stepname = step.attributeValue("name");
				if (!StepManager.getMap().containsKey(stepname)) {
					LOGGER.error("can't find step :" + stepname);
					readError();
					continue;
				}
				StepPojo ts = StepManager.getMap().get(stepname);
				steplist.add(ts);
			}
			CasePojo casePojo = new CasePojo();
			casePojo.setCaseName(casename);
			casePojo.setStepList(steplist);
			CaseManager.getMap().put(casename, casePojo);
		}

	}

	/**
	 *读取step配置 每个step包含一个object(可没有)和一个action
	 * @param root
	 */
	private void readStep(Element root) {
		List<Element> steps = root.elements();

		for (Element step : steps) {
			String stepname = step.attributeValue("name");
			if (stepname == null){
				LOGGER.error("can't get name attribute of step");
				readError();
			}
			if (StepManager.getMap().containsKey(stepname)) {
				LOGGER.error("step exsit :" + stepname);
				readError();
				continue;
			}
			String action = step.element("actionname").attributeValue("name");
			
			Element testobject = step.element("objectname");
			StepPojo teststep;
			if (testobject == null){
				teststep = new StepPojo(stepname,null, action);
			}
			else{
				String objectname = testobject.attributeValue("name");
				if(!ObjectManager.getMap().containsKey(objectname)){
					LOGGER.error("can't find object:" + objectname);
					readError();
				}
				teststep = new StepPojo(stepname,objectname, action);
			}
			StepManager.getMap().put(stepname, teststep);
		}

	}

	/**
	 *读取object配置
	 * @param root
	 */
	private void readObject(Element root) {
		List<Element> objects = root.elements();

		for (Element obj : objects) {
			String name = obj.attributeValue("name");
			if (name == null){
				LOGGER.error("can't get name attribute of object");
				readError();
			}
			if (ObjectManager.getMap().containsKey(name)) {
				LOGGER.error("object exsit :" + name);
				readError();
				continue;
			}
			String xpath = obj.attributeValue("xpath");
			ObjectPojo data = new ObjectPojo(name, xpath);
			ObjectManager.getMap().put(name, data);
		}
	}

	/**
	 * 读取实例数据
	 * @param root
	 */
	private void readData(Element root) {
		List<Element> datas = root.elements();
		//System.out.println(root.getName()+datas.size());
		for (Element data : datas) {
			String name = data.attributeValue("name");
			if (DataManager.getMap().containsKey(name)) {
				LOGGER.error("data exsit:" + name);
				readError();
				continue;
			}
			List<StepInstance> list = new ArrayList<StepInstance>();
			List<Element> steps = data.elements();
			for (Element step : steps){
				String value = step.attributeValue("value");
				String type = step.attributeValue("type");
				BasicData bd = new BasicData(value,type);
				String stepName = step.attributeValue("name");
				if(stepName == null){
					LOGGER.error("can't find name attribute of step");
					readError();
				}
				list.add(new StepInstance(bd,stepName));
			}
			//System.out.println(JSON.toJSONString(list));
			DataPojo dataPojo = new DataPojo(name,list);
			//System.out.println(JSON.toJSONString(dataPojo));
			//System.out.println(JSON.toJSONString(dataPojo.GetData()));
			DataManager.getMap().put(name, dataPojo);
			//System.out.println(JSON.toJSONString(DataManager.getMap().get(name)));
		}
		
	}
}
