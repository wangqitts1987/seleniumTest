package com.dayang.auto.common.pojo;
import com.dayang.auto.conf.ConfGlobalUtils;
import com.dayang.auto.common.pojo.RuntimeSet;

/**
 * BasicData类用于保存程序中的3类值：common、runtime、global
 * common是普通类型，执行取datavalue的值使用
 * runtime为运行时变量，从RuntimeSet获取运行时变量的列表，使用datavalue作为key来取真实的值
 * global为全局变量，为程序启动时就从kx8auto-conf
 */
public class BasicData{

	private String datatype;
	private String datavalue;
	
	public BasicData(){
		
	}
	public BasicData(String datavalue,String datatype){
		this.datatype = datatype;
		this.datavalue = datavalue;
	}
	public BasicData(String datavalue){
		this.datavalue = datavalue ;
		this.datatype = "common";
	}
	public String GetValue(){
		if (datatype == null || datatype.equals("common") ){
			return this.datavalue;
		}
		else if(datatype.equals("runtime")){
			return RuntimeSet.GetRuntimeVar().get(datavalue);
		}
		else if(datatype.equals("global")){
			return ConfGlobalUtils.get(datavalue);
		}
		else{
			return null;
		}
	}
	public String getDatatype()
	{
		return this.datatype;
	}

}