package com.lm.robot.test;

import com.lm.robot.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtils extends AndroidTestCase {//
//������Ĳ����࣬33
	public void testSendInfo()
	{
	String res = HttpUtils.doGet("����Ц����");
	Log.e("TAG", res);
	 res = HttpUtils.doGet("���ҽ�������°�");
	Log.e("TAG", res);
	 res = HttpUtils.doGet("���");
	Log.e("TAG", res);
	}
}
//���Թ����࣬��������� �Ҽ�       run as 