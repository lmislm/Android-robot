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
	//发送请求的一个工具类
	//先了解往哪里发送，先定义一个url 
	private static final String URL = "http://www.tuling123.com/openapi/api";//1
	private static final String API_KEY="245ef75c0b1c45948ef8d439a9d25e7d";//获取一个key 10
	private static final HttpURLConnection urlNet = null;
	//35 增加方法，gson的转换 
	
	public static chatmessages sendmessages(String msg)
	{
		chatmessages chatMessages = new chatmessages();
		//第一步，执行doGet，用户发送一个消息，然后得到返回的一个结果
		 String jsonRes = doGet(msg);   //  服务器返回的json值，json字符串结果   37
		//json转化为result对象
		 Gson gson = new Gson();
		 //字符串传入
		 
		//result可能出现错误，捕获错误
		result result = null; 
		//尝试抛出异常
		try {
			result = gson.fromJson(jsonRes, result.class);
			//正常的话就是result返回的结果,然后给get设置其他属性
			chatMessages.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMessages.setMsg("服务器繁忙，亲，停一会儿再试吧");
		}
		//给chatmessage设置属性
		chatMessages.setDate(new Date()); //organize import 导入需要的类 
		chatMessages.setType(Type.INCOMING);
		
		return chatMessages;
	}
	//编写一个方法
	public static String doGet(String msg) {
		//由用户传入一个消息
		//拼接成一个完整的url然后返回

		//得到一个返回结果，返回为空
		String result="";
		//调用过程就是要url接一下api key然后接一个message 8

		//定义一个方法
		String url = setParams(msg);//为url设置参数   11
		//构造完 setParams之后得到一个完整的 url  13

		//然后进行    url 编码   14 
		//finally 释放所有资源之后把所有声明往外面提   21 第一个是inputstream 第二个是 
		InputStream is = null;

		//第二个
		ByteArrayOutputStream baos = null;

		//异常抛出
		try {
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn =  (HttpURLConnection) urlNet.openConnection();

			conn.setReadTimeout(5*1000); // 设置参数为5秒
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			//设置完属性之后通过connection拿到用户的string  然后通过这个拿到输入流    16
			is = conn.getInputStream(); // 拿到服务器返回用户的string      //附上is=     23

			//转化为string进行返回，
			int len = -1;
			//声明一个缓冲区
			byte[] buf = new byte[128];
			//需要一个扭转为string的一个类
			baos = new ByteArrayOutputStream();

			while((len = is.read(buf))!=-1 )//将内容读入缓冲区,如果到了结尾，结果为-1
			{
				//通过baos等一直读到上面这个流的结束 ,结果一直读到流的结束即等于-1时候跳出循环
				baos.write(buf, 0, len);
				//把这些数组含有string的构造方法 得到一个string 19 
				result = new String(baos.toByteArray());
			}	  
			baos.flush(); //调用util  清除缓冲区 24 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally //释放这些资源 20 
		{
			if(baos!=null)
				try {
					baos.close();
				} catch (IOException e) {
					//抛出异常快捷键shift+alt+z    27

					e.printStackTrace();
				}    //25 
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {


					e.printStackTrace();
				}     //26 
			//两边都有异常 进行抛出 
		}

		return result;		//7.api返回的结果
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

		return url; //然后进行return 12
	}
}


//1.先拼接一个完整的url