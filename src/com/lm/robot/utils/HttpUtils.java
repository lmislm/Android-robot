package com.lm.robot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lm.robot.bean.chatmessages;
import com.lm.robot.bean.chatmessages.Type;
import com.lm.robot.bean.result;

public class HttpUtils {
	//���������һ��������
	//���˽������﷢�ͣ��ȶ���һ��url 
	private static final String URL = "http://www.tuling123.com/openapi/api";//1
	private static final String API_KEY="245ef75c0b1c45948ef8d439a9d25e7d";//��ȡһ��key 10
	private static final HttpURLConnection urlNet = null;
	//35 ���ӷ�����gson��ת�� 
	
	public static chatmessages sendmessages(String msg)
	{
		chatmessages chatMessages = new chatmessages();
		//��һ����ִ��doGet���û�����һ����Ϣ��Ȼ��õ����ص�һ�����
		 String jsonRes = doGet(msg);   //  ���������ص�jsonֵ��json�ַ������   37
		//jsonת��Ϊresult����
		 Gson gson = new Gson();
		 //�ַ�������
		 
		//result���ܳ��ִ��󣬲������
		result result = null; 
		//�����׳��쳣
		try {
			result = gson.fromJson(jsonRes, result.class);
			//�����Ļ�����result���صĽ��,Ȼ���get������������
			chatMessages.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMessages.setMsg("��������æ���ף�ͣһ������԰�");
		}
		//��chatmessage��������
		chatMessages.setDate(new Date()); //organize import ������Ҫ���� 
		chatMessages.setType(Type.INCOMING);
		
		return chatMessages;
	}
	//��дһ������
	public static String doGet(String msg) {
		//���û�����һ����Ϣ
		//ƴ�ӳ�һ��������urlȻ�󷵻�

		//�õ�һ�����ؽ��������Ϊ��
		String result="";
		//���ù��̾���Ҫurl��һ��api keyȻ���һ��message 8

		//����һ������
		String url = setParams(msg);//Ϊurl���ò���   11
		//������ setParams֮��õ�һ�������� url  13

		//Ȼ�����    url ����   14 
		//finally �ͷ�������Դ֮�������������������   21 ��һ����inputstream �ڶ����� 
		InputStream is = null;

		//�ڶ���
		ByteArrayOutputStream baos = null;

		//�쳣�׳�
		try {
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn =  (HttpURLConnection) urlNet.openConnection();

			conn.setReadTimeout(5*1000); // ���ò���Ϊ5��
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			//����������֮��ͨ��connection�õ��û���string  Ȼ��ͨ������õ�������    16
			is = conn.getInputStream(); // �õ������������û���string      //����is=     23

			//ת��Ϊstring���з��أ�
			int len = -1;
			//����һ��������
			byte[] buf = new byte[128];
			//��Ҫһ��ŤתΪstring��һ����
			baos = new ByteArrayOutputStream();

			while((len = is.read(buf))!=-1 )//�����ݶ��뻺����,������˽�β�����Ϊ-1
			{
				//ͨ��baos��һֱ��������������Ľ��� ,���һֱ�������Ľ���������-1ʱ������ѭ��
				baos.write(buf, 0, len);
				//����Щ���麬��string�Ĺ��췽�� �õ�һ��string 19 
				result = new String(baos.toByteArray());
			}	  
			baos.flush(); //����util  ��������� 24 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally //�ͷ���Щ��Դ 20 
		{
			if(baos!=null)
				try {
					baos.close();
				} catch (IOException e) {
					//�׳��쳣��ݼ�shift+alt+z    27

					e.printStackTrace();
				}    //25 
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {


					e.printStackTrace();
				}     //26 
			//���߶����쳣 �����׳� 
		}

		return result;		//7.api���صĽ��
	}
	private static String setParams(String msg) {
		//setParams() ctrl+1
		String url = "";     
		try {
			url = URL+"?key="+API_KEY+"&info="+URLEncoder.encode(msg,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url; //Ȼ�����return 12
	}
}


//1.��ƴ��һ��������url