package com.lm.robot;

import java.text.SimpleDateFormat;
import java.util.List;

import com.lm.robot.bean.chatmessages;
import com.lm.robot.bean.chatmessages.Type;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//����baseAdapter ������ 45
public class ChatMessageAdapter extends BaseAdapter {
	//ѹե���� flater     49
	private LayoutInflater mInflater;
	//50 
	private List<chatmessages> mDatas;
	
	//121
	private Context context;

	//�ȳ�ʼ���������������������ݼ���  48
	public ChatMessageAdapter(Context context,List<chatmessages> mDatas)
	{	//���context
		
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();//��Ϣ�ĸ���
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//�������֣���д��������  52
	//91 д��λ�ñ仯
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//getview�ķ�����
		chatmessages chatmessages = mDatas.get(position);
		//���hodlerģʽ����Ч��   59
		ViewHolder viewHolder = null;
		//ͨ��itemtype���ز�ͬ����
		if(convertView == null)
		{
			if(getItemViewType(position)==0)
			{  
				convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_from_msg_date);
				//��Ϣtextview
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			}else
			{    //�ұߵ�item
				convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
				//��Ϣtextview
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(viewHolder);
		}else//convertviewΪ�յ�ʱ��
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//��������    61
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.mDate.setText(df.format(chatmessages.getDate()));
		viewHolder.mMsg.setText(chatmessages.getMsg());
		
		return convertView;
	}

	private final class ViewHolder//ͷ������ƹ̶�
	{
		TextView mDate;
		TextView mMsg;
	}



	@Override
	public int getItemViewType(int position) {
		chatmessages chatmessages =  mDatas.get(position);
		//ͨ��λ���õ�������Ϣ    53
		if(chatmessages.getType()==Type.INCOMING)
		{//������Ϣ
			return 0;
		}
		return 1;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
