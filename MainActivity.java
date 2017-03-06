package com.lm.robot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lm.robot.R;
import com.lm.robot.bean.chatmessages;
import com.lm.robot.bean.chatmessages.Type;
import com.lm.robot.utils.HttpUtils;
//44 添加 listview
import com.lm.robot.voice.VoiceToWord;


public class MainActivity extends Activity 
{
	//开始listview的编写     45   
	public static String toMsg="";
	private ListView mMsgs;  //然后设置一个适配器
	private ChatMessageAdapter mAdapter;//先创建适配器
	private List<chatmessages> mDatas;
	//需要的变量，然后再初始化变量     46
	public static EditText mInputMsg;
	@SuppressWarnings("unused")
	private Button mSendMsg;
	private TextView tv_back;
	private Button iv_voice;
	
	SQLiteOpenHelper openHelper;
    
	private Handler mHandler = new Handler()
	{//子线程中更新界面 ，67  复写 handlemessage
	//复写一个handleMessage

		public void handleMessage(android.os.Message msg) 
		{
			//等待接收子线程完成数据的返回，必须在子线程中进行更新
			//得到obj,然后为message赋值   75
			chatmessages fromMessage = (chatmessages) msg.obj;
			//将数据添加到 数据集中
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			//数据发生变化    但是需要通知一下 来刷新界面
			
			//更新到最后比不
			mMsgs.setSelection(mDatas.size()-1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//建立方法
		initView();
		//建立方法,初始化一些
		initDatas();
		//初始化方法 
		initListener();
		//准备数据库，存取聊天记录
		

//		String dbName=Environment.getExternalStorageDirectory().get
		String dbName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Chat.db";
		try{
			File file=new File(dbName);
			if(!file.exists()){
				InputStream fis=getAssets().open("database/Chat.db");
				FileOutputStream fos=new FileOutputStream(dbName,false);
				byte[] buff=new byte[1024];
				int len=0;
				while((len=fis.read(buff))>0){
					fos.write(buff, 0,len);
				}
				fos.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		openHelper=new SQLiteOpenHelper(this,dbName,null,1){

			@Override
			public void onCreate(SQLiteDatabase db) {
//				 db.execSQL("CREATE TABLE mDatas ( INTEGER PRIMARY KEY AUTOINCREMENT, " + 
//                         "mInputMsg VARCHER(20), " +               
//                         "OutMsg VARCHER(20)"
//                         );
//				 Log.i( "create table", null);
				
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

	private void initListener() {
		//创建这个方法,匿名内部类
		mSendMsg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) 
			{
				//拿到按钮inpoutmessage里面的数据,返回成功的话加入到listview中
				final String toMsg =	mInputMsg.getText().toString();
				
				if(TextUtils.isEmpty(toMsg))
				{
					Toast.makeText(MainActivity.this, "发送消息不能为空啊",
							Toast.LENGTH_SHORT).show();
					return;
				}
				//添加select，保持到最低端。 这个必须handle。
//				mMsgs.setSelection(mDatas.size()-1);
				//chatListView.setSelection(chatList.size()-1);
				
				//正常情况下，把得到的message消息传过去
								
				//当我们onclick时拿到message，应当把输入的数据封装成一个message。  76
				chatmessages toMessage = new chatmessages();
				//发送数据的消息    77
				
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				//把数据加入到listview数据集中。79
				mDatas.add(toMessage);
				//通知更新   81
				mAdapter.notifyDataSetChanged();
				
//				无效listview始终最低端
//				mMsgs.setSelection(mDatas.size()-1);
				
				//最后把输入框文本清空
				
				mInputMsg.setText("");
				new Thread(){
					//复写run方法
					public void run() {
						 
					chatmessages fromMessage = HttpUtils.sendmessages(toMsg);//网络操作  ，不能在主线程中操作，必须新建一个子线程  64
					//新问题  ，子线程中不能更新主线程中的控件	 启用handel更新主界面  66 
					//使用listview的话肯定得更新界面
					
					//handle之后 74 拿到之后赋值
					Message m = Message.obtain();
					m.obj = fromMessage;
					//网络请求拿到消息之后付给，osMessage,
					
					//然后通过声明的handle将消息发送出去
					mHandler.sendMessage(m);
					
					};
			
			
				}.start();
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
			}
		});

	}

	private void initDatas() {//适配器返回的
		mDatas = new ArrayList<chatmessages>();
		//util .data   65
		mDatas.add(new chatmessages("在干什么呢？",Type.INCOMING,new Date()));
	//开始总测试
//		mDatas.add(new chatmessages("你好",Type.OUTCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);

		mMsgs.setAdapter(mAdapter);
		
				//添加语音识别 110
	iv_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 初始化，再加载
				//MyRecognizerDialogLister.text="";
				VoiceToWord voice = new VoiceToWord(MainActivity.this,"5846a1a1");
				voice.GetWordFromVoice();
			}
		});
	}

	private void initView() {
		// 从布局界面传过来ListView的Id
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		//导入相关的类再进行类型转化 47
		//声音  111
		tv_back=(TextView) findViewById(R.id.tv_back);
		iv_voice=(Button) findViewById(R.id.iv_voice);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
