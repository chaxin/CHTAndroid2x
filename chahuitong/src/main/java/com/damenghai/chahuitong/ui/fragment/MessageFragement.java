package com.damenghai.chahuitong.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Conversation;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageFragement extends Fragment {
	private View view;

//	private PullToRefresh mListView;
	private ArrayList<Conversation> mDatas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_item_fragment_msg, container, false);
		initView();
		mDatas = new ArrayList<Conversation>();

		Handler handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);

//				mListView.setAdapter(new CommonAdapter<Conversation>(getActivity(), mDatas, R.layout.listview_item_message) {
//
//					@Override
//					public void convert(ViewHolder holder, Conversation t) {
//						holder.loadUrlImage(R.id.id_msg_img, t.getAvatar());
//					}
//
//				});
//
//				mListView.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						Conversation message = mDatas.get(position);
//						Intent intent = new Intent(getActivity(), ChatActivity.class);
//						intent.putExtra("cid", message.getCid());
//						intent.putExtra("pid", message.getPid());
//						startActivity(intent);
//					}
//				});
			}
		};

		loadDatas(handler);

		return view;
	}

	private void initView() {
//		mListView = (PullToRefresh) view.findViewById(R.id.id_listview_msg);
	}

	private void loadDatas(final Handler handler) {
		final int userId = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {
				String json = HttpUtils.doPost(Constants.API_MY_NEWSLIST,
						"userid=" + userId);
				try {
					JSONArray array = new JSONArray(json);
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String pic = obj.getString("pic");
						int cid = obj.getInt("cid");
						int pid = obj.getInt("pid");
						int readed = obj.getInt("readed");
						int total = obj.getInt("total");

						Conversation msgList = new Conversation();
						if(!pic.trim().equals("") && !pic.equals("null")) {
							msgList.setAvatar("http://www.chahuitong.com/mobile/app/b2b/Public/upload/" + pic);
						} else {
							msgList.setAvatar("");
						}
						msgList.setCid(cid);
						msgList.setPid(pid);
						msgList.setReaded(readed);
						msgList.setTotal(total);

						mDatas.add(msgList);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}

		}).start();
	}

}
