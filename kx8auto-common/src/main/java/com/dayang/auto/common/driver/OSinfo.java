package com.dayang.auto.common.driver;


public class OSinfo {
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static OSinfo _instance = new OSinfo();
	private Platform platform;
	
	private OSinfo(){}
	
	public static boolean isLinux(){
		return OS.indexOf("linux") >= 0;
	}
	
	public static boolean isWindows(){
		return OS.indexOf("windows") >= 0;
	}
	
	public static Platform getOSname(){
		if(isLinux()){
			_instance.platform = Platform.Linux;
		}else if(isWindows()){
			_instance.platform = Platform.Windows;
		}else{
			_instance.platform = Platform.Others;
		}
		return _instance.platform;
	}
	
	
}

enum Platform{
	Any("any"),
	Linux("Linux"),
	Windows("Windows"),
	Others("Others");
	
	private Platform(String desc){
		this.description = desc;
	}
	
	public String toString(){
		return description;
	}
	
	private String description;
	
}