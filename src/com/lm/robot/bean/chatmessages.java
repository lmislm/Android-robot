package com.lm.robot.bean;

import java.util.Date;

//消息：消息对话的bean，名称，时间，消息内容。
public class chatmessages {
	
	private String name;
	//名称
	private String msg;
	//消息内容
	private Type type;
	//类型
	private Date date;
	//消息的时间
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public enum Type
	{//定义个枚举左右类型
		//包含两个值
		INCOMING, OUTCOMING
		
	}
	//62 多实现一个构造方法
	//可以传入一个时间一个方法
	public chatmessages()
	{
		
	}

	public chatmessages(String msg, Type type, Date date) {
		super();
		this.msg = msg;
		this.type = type;
		this.date = date;
	}

	
	
}
