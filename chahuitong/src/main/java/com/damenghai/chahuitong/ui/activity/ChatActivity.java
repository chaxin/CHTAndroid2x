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

import com.damenghai.chahuitong.MyMessageReceiver;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.MessageChatAdapter;
import com.damenghai.chahuitong.bean.Chat;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.utils.HttpUtils;
import com.damenghai.chahuitong.view.TopBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;

public class ChatActivity extends BaseActivity implements OnClickListener {
    private TopBar mTopBar;
    private ListView mListView;

    private BmobChatUser mTargetUser;
    private String mTargetId;

    private static int MsgPagerNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTargetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
        mTargetId = mTargetUser.getObjectId();

        findViewById();

        initView();

    }

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.id_chat_topbar);
        mListView = (ListView) findViewById(R.id.id_chat_lv);
    }

    @Override
    protected void initView() {
        mTopBar.setTitle("对方用户名");
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });

        initListView();
    }

    private MessageChatAdapter mAdapter;

    private void initListView() {
        if(mAdapter != null) {
            if (MyMessageReceiver.mNewNum != 0) {// 用于更新当在聊天界面锁屏期间来了消息，这时再回到聊天页面的时候需要显示新来的消息
                int news=  MyMessageReceiver.mNewNum;//有可能锁屏期间，来了N条消息,因此需要倒叙显示在界面上
                int size = initMsgData().size();
//                for(int i=(news-1);i>=0;i--){
//                    mAdapter.add(initMsgData().get(size-(i+1)));// 添加最后一条消息到界面显示
//                }
                mListView.setSelection(mAdapter.getCount() - 1);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mAdapter = new MessageChatAdapter(this, initMsgData());
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<BmobMsg> initMsgData() {
        List<BmobMsg> list = BmobDB.create(this).queryMessages(mTargetId,MsgPagerNum);
        return list;
    }

    @Override
    public void onClick(View v) {

    }

}
