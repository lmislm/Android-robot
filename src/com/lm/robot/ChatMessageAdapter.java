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
//集成baseAdapter 适配器 45
public class ChatMessageAdapter extends BaseAdapter {
	//压榨布局 flater     49
	private LayoutInflater mInflater;
	//50 
	private List<chatmessages> mDatas;
	
	//121
	private Context context;

	//先初始化适配器，（方法，数据集）  48
	public ChatMessageAdapter(Context context,List<chatmessages> mDatas)
	{	//添加context
		
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();//消息的个数
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
	//两个布局，复写两个方法  52
	//91 写入位置变化
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//getview的方法体
		chatmessages chatmessages = mDatas.get(position);
		//添加hodler模式提升效率   59
		ViewHolder viewHolder = null;
		//通过itemtype返回不同布局
		if(convertView == null)
		{
			if(getItemViewType(position)==0)
			{  
				convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_from_msg_date);
				//消息textview
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			}else
			{    //右边的item
				convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
				//消息textview
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(viewHolder);
		}else//convertview为空的时候
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//设置数据    61
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.mDate.setText(df.format(chatmessages.getDate()));
		viewHolder.mMsg.setText(chatmessages.getMsg());
		
		return convertView;
	}

	private final class ViewHolder//头像和名称固定
	{
		TextView mDate;
		TextView mMsg;
	}



	@Override
	public int getItemViewType(int position) {
		chatmessages chatmessages =  mDatas.get(position);
		//通过位置拿到聊天信息    53
		if(chatmessages.getType()==Type.INCOMING)
		{//接收消息
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
