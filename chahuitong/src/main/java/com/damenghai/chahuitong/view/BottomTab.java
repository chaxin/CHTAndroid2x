package com.damenghai.chahuitong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.R;

public class BottomTab extends LinearLayout implements OnClickListener {
	private LinearLayout ll_home;
	private LinearLayout ll_publish;
	private LinearLayout ll_msg;
	private LinearLayout ll_mine;
	private ImageButton tabHomeImg;
	private ImageButton tabPublishImg;
	private ImageButton tabMsgImg;

	private ImageButton tabMineImg;
	private TextView tabHomeText;
	private TextView tabPublishText;
	private TextView tabMsgText;
	private TextView tabMineText;
	
	OnTabChangeListener listener;
	
	public interface OnTabChangeListener {
		void onTabSelected(int arg0);
	}
	
	public BottomTab(Context context) {
		super(context);
	}
	
	public void setOnTabChangeListener(OnTabChangeListener listener) {
		this.listener = listener;
	}
	
	public BottomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.market_tab, this);
		
		ll_home = (LinearLayout) findViewById(R.id.ll_tab_home);
		ll_publish = (LinearLayout) findViewById(R.id.ll_tab_publish);
		ll_msg = (LinearLayout) findViewById(R.id.ll_tab_msg);
		ll_mine = (LinearLayout) findViewById(R.id.ll_tab_mine);

		ll_home.setOnClickListener(this);
		ll_publish.setOnClickListener(this);
		ll_msg.setOnClickListener(this);
		ll_mine.setOnClickListener(this);

		tabHomeImg = (ImageButton) findViewById(R.id.tab_home_img);
		tabPublishImg = (ImageButton) findViewById(R.id.tab_publish_img);
		tabMsgImg = (ImageButton) findViewById(R.id.tab_msg_img);
		tabMineImg = (ImageButton) findViewById(R.id.tab_mine_img);
		
		tabHomeText = (TextView) findViewById(R.id.tab_home_text);
		tabPublishText = (TextView) findViewById(R.id.tab_publish_text);
		tabMsgText = (TextView) findViewById(R.id.tab_msg_text);
		tabMineText = (TextView) findViewById(R.id.tab_mine_text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_tab_home:
			listener.onTabSelected(0);
			selectItem(0);
			break;
		case R.id.ll_tab_publish:
			listener.onTabSelected(1);
			selectItem(1);
			break;
		case R.id.ll_tab_msg:
			listener.onTabSelected(2);
			selectItem(2);
			break;
		case R.id.ll_tab_mine:
			listener.onTabSelected(3);
			selectItem(3);
			break;
		default:
			break;
		}
		
	}

	private void clearState() {
		tabHomeImg.setImageResource(R.drawable.tab_home_normal);
		tabPublishImg.setImageResource(R.drawable.tab_publish_normal);
		tabMsgImg.setImageResource(R.drawable.tab_msg_normal);
		tabMineImg.setImageResource(R.drawable.tab_mine_normal);
		
		tabHomeText.setTextColor(getResources().getColor(R.color.light_gray));
		tabPublishText.setTextColor(getResources().getColor(R.color.light_gray));
		tabMsgText.setTextColor(getResources().getColor(R.color.light_gray));
		tabMineText.setTextColor(getResources().getColor(R.color.light_gray));
	}

	public void selectItem(int arg0) {
		clearState();
		switch (arg0) {
		case 0:
			tabHomeImg.setImageResource(R.drawable.tab_home_pressed);
			tabHomeText.setTextColor(getResources().getColor(R.color.primary));
			break;
		case 1:
			tabPublishImg.setImageResource(R.drawable.tab_publish_pressed);
			tabPublishText.setTextColor(getResources().getColor(R.color.primary));
			break;
		case 2:
			tabMsgImg.setImageResource(R.drawable.tab_msg_pressed);
			tabMsgText.setTextColor(getResources().getColor(R.color.primary));
			break;
		case 3:
			tabMineImg.setImageResource(R.drawable.tab_mine_pressed);
			tabMineText.setTextColor(getResources().getColor(R.color.primary));
			break;
		default:
			break;
		}
	}
}
