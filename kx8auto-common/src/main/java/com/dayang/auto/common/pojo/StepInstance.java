package com.dayang.auto.common.pojo;
/**
 * 类描述：测试步骤实例类，包含测试步骤的名称和测试步骤对应的数据
 * 
 */
public class StepInstance {
	
	private BasicData bd;
	private String stepName;
	public StepInstance(){
		
	}
	public StepInstance(BasicData bd,String stepName){
		this.bd = bd;
		this.stepName = stepName;
	}
	public String GetStepName(){
		return stepName;
	}
	public BasicData GetBasicData(){
		return bd;
	}
}
