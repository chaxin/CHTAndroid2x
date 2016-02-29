package com.damenghai.chahuitong2.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.damenghai.chahuitong2.MyMessageReceiver;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.MessageAdapter;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

/**
 * Created by Sgun on 15/10/12.
 */
public class MessageActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener
        , EventListener, PullToRefreshBase.OnRefreshListener {

    private TopBar mTopBar;
    private PullToRefreshListView mPlv;
    private MessageAdapter mMessageAdapter;
    private NewMessageBroadcast mNewMessageReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //开启定时检测服务（单位为秒）-在这里检测后台是否还有未读的消息，有的话就取出来
        //如果你觉得检测服务比较耗流量和电量，你也可以去掉这句话-同时还有onDestory方法里面的stopPollService方法
        BmobChat.getInstance(this).startPollService(10);

        bindView();

        initView();

        initNewMessageBroadcast();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.message_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.message_plv);
    }

    @Override
    protected void initView() {
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

        mMessageAdapter = new MessageAdapter(this, BmobDB.create(this).queryRecents(), R.layout.listview_item_message);
        mPlv.setAdapter(mMessageAdapter);
        mPlv.setOnItemClickListener(this);
        mPlv.getRefreshableView().setOnItemLongClickListener(this);
    }

    private void initNewMessageBroadcast() {
        mNewMessageReceive = new NewMessageBroadcast();
        IntentFilter filter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
        filter.setPriority(3);
        registerReceiver(mNewMessageReceive, filter);
    }

    private class NewMessageBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
            // 记得把广播给终结掉
            abortBroadcast();
        }

    }

    private void deleteRecent(BmobRecent recent) {
        mMessageAdapter.remove(recent);
        BmobDB.create(this).deleteRecent(recent.getTargetid());
        BmobDB.create(this).deleteMessages(recent.getTargetid());
    }

    private void showDialog(final BmobRecent recent) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("删除会话").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRecent(recent);
            }
        }).setNegativeButton("取消", null);
        dialog.show();
    }

    private void refresh() {
        mMessageAdapter = new MessageAdapter(this, BmobDB.create(this).queryRecents(), R.layout.listview_item_message);
        mPlv.setAdapter(mMessageAdapter);
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
        refresh();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        BmobRecent recent = mMessageAdapter.getItem(i - 1);
        showDialog(recent);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyMessageReceiver.ehList.add(this);// 监听推送的消息
        //清空消息未读数-这个要在刷新之后
        MyMessageReceiver.mNewNum=0;
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 监听推送的消息
        MyMessageReceiver.ehList.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mNewMessageReceive);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BmobChat.getInstance(this).stopPollService();
    }

    @Override
    public void onMessage(BmobMsg bmobMsg) {
        refresh();
    }

    @Override
    public void onReaded(String s, String s1) {

    }

    @Override
    public void onNetChange(boolean b) {
        if(!b) {
            T.showShort(this, R.string.toast_network_invalid);
        }
    }

    @Override
    public void onAddUser(BmobInvitation bmobInvitation) {

    }

    @Override
    public void onOffline() {

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        refresh();
    }
}
