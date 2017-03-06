package com.lm.robot.test;

import com.lm.robot.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtils extends AndroidTestCase {//
//工具类的测试类，33
	public void testSendInfo()
	{
	String res = HttpUtils.doGet("讲个笑话吧");
	Log.e("TAG", res);
	 res = HttpUtils.doGet("给我讲个鬼故事吧");
	Log.e("TAG", res);
	 res = HttpUtils.doGet("你好");
	Log.e("TAG", res);
	}
}
//测试工具类，点击方法名 右键       run as 