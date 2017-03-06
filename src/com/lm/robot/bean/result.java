package com.lm.robot.bean;

public class result {
//整个过程 
	//映射服务器返回的结果
	private int code;
	private String text;
	//生成getsetter  
	
	//引入Gson把字符串转化为对象   34 
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
