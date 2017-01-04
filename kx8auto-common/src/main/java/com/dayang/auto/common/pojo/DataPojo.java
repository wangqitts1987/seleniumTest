package com.dayang.auto.common.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：用于保存数据，与case对应
 *      每个DataPojo包含一个步骤实例的list
 * 
 */
public class DataPojo{
	private String dataName;
	private List<StepInstance> data; 
	
	public DataPojo(){
		
	}
	
    public DataPojo(String dataName,List<StepInstance> data){
    	this.dataName = dataName;
    	this.data = data;
    }
    public List<StepInstance> GetData()
    {
    	return this.data;
    }
}

