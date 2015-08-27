package com.damenghai.chahuitong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Chat;

public class ListViewChatAdapter extends BaseAdapter {
	public ArrayList<Chat> mDatas;
	private LayoutInflater mInflater;
	
	public static final int ITEM_TYPE_TO = 0;
	public static final int ITEM_TYPE_FROM = 1;
			
	public ListViewChatAdapter(Context mContext, ArrayList<Chat> mDatas) {
		super();
		this.mDatas = mDatas;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemViewType(int position) {
		return mDatas.get(position).getType();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Chat getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			switch (getItemViewType(position)) {
			case ITEM_TYPE_TO:
				convertView = mInflater.inflate(R.layout.listview_item_chatto, parent, false);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_chat_date);
				holder.mContent = (TextView) convertView.findViewById(R.id.id_chat_content);
				convertView.setTag(holder);
				break;
			case ITEM_TYPE_FROM:
				convertView = mInflater.inflate(R.layout.listview_item_chatfrom, parent, false);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_chat_date);
				holder.mContent = (TextView) convertView.findViewById(R.id.id_chat_content);
				convertView.setTag(holder);
				break;
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Chat chat = mDatas.get(position);
		holder.mContent.setText(chat.getContent());
		holder.mDate.setText(chat.getDate());
		
		return convertView;
	}

	private class ViewHolder {
		TextView mDate, mContent;
	}
}
