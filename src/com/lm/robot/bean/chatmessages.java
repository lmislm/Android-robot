package com.lm.robot.bean;

import java.util.Date;

//��Ϣ����Ϣ�Ի���bean�����ƣ�ʱ�䣬��Ϣ���ݡ�
public class chatmessages {
	
	private String name;
	//����
	private String msg;
	//��Ϣ����
	private Type type;
	//����
	private Date date;
	//��Ϣ��ʱ��
	
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
	{//�����ö����������
		//��������ֵ
		INCOMING, OUTCOMING
		
	}
	//62 ��ʵ��һ�����췽��
	//���Դ���һ��ʱ��һ������
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
