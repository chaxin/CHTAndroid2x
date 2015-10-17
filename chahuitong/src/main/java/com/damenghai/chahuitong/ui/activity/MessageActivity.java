package com.damenghai.chahuitong.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.MessageAdapter;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Message;
import com.damenghai.chahuitong.utils.L;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.BmobUser;

/**
 * Created by Sgun on 15/10/12.
 */
public class MessageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mPlv;
    private MessageAdapter mMessageAdapter;
    private NewBroadcastReceiver mNewReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        findViewById();

        initView();

        initNewBroadcastReceiver();
    }

    @Override
    protected void findViewById() {
        mPlv = (PullToRefreshListView) findViewById(R.id.message_plv);
    }

    @Override
    protected void initView() {
        mMessageAdapter = new MessageAdapter(this, BmobDB.create(this).queryRecents(), R.layout.listview_item_message);
        mPlv.setAdapter(mMessageAdapter);
        mPlv.setOnItemClickListener(this);
    }

    private void initNewBroadcastReceiver() {
        mNewReceiver = new NewBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
        // 优先级要低于ChatActivity
        intentFilter.setPriority(3);
        registerReceiver(mNewReceiver, intentFilter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        BmobRecent recent = mMessageAdapter.getItem(position - 1);
        //重置未读消息
        BmobDB.create(MessageActivity.this).resetUnread(recent.getTargetid());
        //组装聊天对象
        BmobChatUser user = new BmobChatUser();
        user.setAvatar(recent.getAvatar());
        user.setNick(recent.getNick());
        user.setUsername(recent.getUserName());
        user.setObjectId(recent.getTargetid());
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        openActivity(ChatActivity.class, bundle);
    }

    private class NewBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.d("rrrr");
        }

    }

}
