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
//44 ��� listview
import com.lm.robot.voice.VoiceToWord;


public class MainActivity extends Activity 
{
	//��ʼlistview�ı�д     45   
	public static String toMsg="";
	private ListView mMsgs;  //Ȼ������һ��������
	private ChatMessageAdapter mAdapter;//�ȴ���������
	private List<chatmessages> mDatas;
	//��Ҫ�ı�����Ȼ���ٳ�ʼ������     46
	public static EditText mInputMsg;
	@SuppressWarnings("unused")
	private Button mSendMsg;
	private TextView tv_back;
	private Button iv_voice;
	
	SQLiteOpenHelper openHelper;
    
	private Handler mHandler = new Handler()
	{//���߳��и��½��� ��67  ��д handlemessage
	//��дһ��handleMessage

		public void handleMessage(android.os.Message msg) 
		{
			//�ȴ��������߳�������ݵķ��أ����������߳��н��и���
			//�õ�obj,Ȼ��Ϊmessage��ֵ   75
			chatmessages fromMessage = (chatmessages) msg.obj;
			//��������ӵ� ���ݼ���
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			//���ݷ����仯    ������Ҫ֪ͨһ�� ��ˢ�½���
			
			//���µ����Ȳ�
			mMsgs.setSelection(mDatas.size()-1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//��������
		initView();
		//��������,��ʼ��һЩ
		initDatas();
		//��ʼ������ 
		initListener();
		//׼�����ݿ⣬��ȡ�����¼
		

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
		//�����������,�����ڲ���
		mSendMsg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) 
			{
				//�õ���ťinpoutmessage���������,���سɹ��Ļ����뵽listview��
				final String toMsg =	mInputMsg.getText().toString();
				
				if(TextUtils.isEmpty(toMsg))
				{
					Toast.makeText(MainActivity.this, "������Ϣ����Ϊ�հ�",
							Toast.LENGTH_SHORT).show();
					return;
				}
				//���select�����ֵ���Ͷˡ� �������handle��
//				mMsgs.setSelection(mDatas.size()-1);
				//chatListView.setSelection(chatList.size()-1);
				
				//��������£��ѵõ���message��Ϣ����ȥ
								
				//������onclickʱ�õ�message��Ӧ������������ݷ�װ��һ��message��  76
				chatmessages toMessage = new chatmessages();
				//�������ݵ���Ϣ    77
				
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				//�����ݼ��뵽listview���ݼ��С�79
				mDatas.add(toMessage);
				//֪ͨ����   81
				mAdapter.notifyDataSetChanged();
				
//				��Чlistviewʼ����Ͷ�
//				mMsgs.setSelection(mDatas.size()-1);
				
				//����������ı����
				
				mInputMsg.setText("");
				new Thread(){
					//��дrun����
					public void run() {
						 
					chatmessages fromMessage = HttpUtils.sendmessages(toMsg);//�������  �����������߳��в����������½�һ�����߳�  64
					//������  �����߳��в��ܸ������߳��еĿؼ�	 ����handel����������  66 
					//ʹ��listview�Ļ��϶��ø��½���
					
					//handle֮�� 74 �õ�֮��ֵ
					Message m = Message.obtain();
					m.obj = fromMessage;
					//���������õ���Ϣ֮�󸶸���osMessage,
					
					//Ȼ��ͨ��������handle����Ϣ���ͳ�ȥ
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

	private void initDatas() {//���������ص�
		mDatas = new ArrayList<chatmessages>();
		//util .data   65
		mDatas.add(new chatmessages("�ڸ�ʲô�أ�",Type.INCOMING,new Date()));
	//��ʼ�ܲ���
//		mDatas.add(new chatmessages("���",Type.OUTCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);

		mMsgs.setAdapter(mAdapter);
		
				//�������ʶ�� 110
	iv_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ��ʼ�����ټ���
				//MyRecognizerDialogLister.text="";
				VoiceToWord voice = new VoiceToWord(MainActivity.this,"5846a1a1");
				voice.GetWordFromVoice();
			}
		});
	}

	private void initView() {
		// �Ӳ��ֽ��洫����ListView��Id
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		//������ص����ٽ�������ת�� 47
		//����  111
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
