package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.ListViewChatAdapter;
import com.damenghai.chahuitong.bean.Chat;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.utils.HttpUtils;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends BaseActivity implements OnClickListener {
    private TopBar mTopBar;
    private ListView mListView;
    private ListViewChatAdapter mAdapter;
    private ArrayList<Chat> mChats;

    private RelativeLayout mBtnAdd, mBtnSend;
    private EditText mMsgText;

    private int user_id, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mAdapter = new ListViewChatAdapter(ChatActivity.this, mChats);
                mListView.setAdapter(mAdapter);
                mListView.setSelection(mAdapter.getCount() - 1);
                mBtnSend.setOnClickListener(ChatActivity.this);
                super.handleMessage(msg);
            }
        };

        loadDatas(handler);
        setTopbar();

    }

    private void initView() {
        mTopBar = (TopBar) findViewById(R.id.id_chat_topbar);
        mListView = (ListView) findViewById(R.id.id_chat_lv);
        mBtnAdd = (RelativeLayout) findViewById(R.id.id_btn_add_img);
        mBtnSend = (RelativeLayout) findViewById(R.id.id_btn_send);
        mMsgText = (EditText) findViewById(R.id.id_msg_text);
    }

    private void loadDatas(final Handler handler) {
        mChats = new ArrayList<Chat>();
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bundle extra = getIntent().getExtras();
                int cid = 0;
                user_id = 0;
                if (extra != null) {
                    cid = extra.getInt("cid");
                    productId = extra.getInt("pid");
                }
                if (cid != 0 && productId != 0) {
                    String json = HttpUtils.doPost(Constants.API_CHAT,
                            "cid=" + cid + "&id=" + productId + "&userid=" + user_id);
                    try {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);
                            int fid = obj.getInt("fid");
                            int uid = obj.getInt("uid");
                            String addTime = obj.getString("addtime");
                            String title = obj.getString("title");
                            String content = obj.getString("content");

                            Chat chat = new Chat(
                                    addTime,
                                    content,
                                    user_id == fid ? ListViewChatAdapter.ITEM_TYPE_TO : ListViewChatAdapter.ITEM_TYPE_FROM);
                            mChats.add(chat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(0);
            }

        }).start();
    }

    private void setTopbar() {
        mTopBar.setTitle("对方用户名");
        mTopBar.setOnLeftClickListener(new OnLeftClickListener() {

            @Override
            public void onLeftClick() {
                finish();
            }

        });
    }

    @Override
    public void onClick(View v) {
        final String msgContent = mMsgText.getText().toString();
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final String date = format.format(time);

        AddMessageHandler handler = new AddMessageHandler(this, date, msgContent);

        sendMessage(handler, msgContent);

        mMsgText.setText("");
    }

    private void sendMessage(final Handler handler, final String content) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                String results = HttpUtils.doPost(Constants.API_SEND_MESSAGE, "id=" + productId + "&userid=" + user_id + "&content=" + content);
                Message msg = new Message();
                msg.obj = results;
                handler.sendMessage(msg);
            }

        }).start();

    }

    private static class AddMessageHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;
        private String mDate, mContent;

        private AddMessageHandler(ChatActivity activity, String date, String content) {
            mActivity = new WeakReference<ChatActivity>(activity);
            mDate = date;
            mContent = content;
        }

        @Override
        public void handleMessage(Message msg) {
            String results = (String) msg.obj;
            ChatActivity activity = mActivity.get();
            if(results.equals("1")) {
                activity.mChats.add(new Chat(mDate, mContent, 0));
                activity.mAdapter.notifyDataSetChanged();
                Toast.makeText(activity, "发送成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "发送失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
