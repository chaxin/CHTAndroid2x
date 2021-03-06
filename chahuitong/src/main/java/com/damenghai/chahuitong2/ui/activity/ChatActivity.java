package com.damenghai.chahuitong2.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.MyMessageReceiver;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.MessageChatAdapter;
import com.damenghai.chahuitong2.adapter.NewRecordPlayClickListener;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.utils.ImageUtils;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.AudioRecorderButton;
import com.damenghai.chahuitong2.view.TopBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.UploadListener;

public class ChatActivity extends BaseActivity implements OnClickListener, cn.bmob.im.inteface.EventListener, AudioRecorderButton.OnSendMessageListener {
    private TopBar mTopBar;
    private PullToRefreshListView mPlv;
    private EditText mEtMsg;
    private Button mBtnAdd, mBtnEmo, mBtnVoice, mBtnKeyboard, mBtnSend;
    private LinearLayout mLayoutMore, mLayoutEmoji, mLayoutAdd;
    private TextView mTvPicture, mTvCamera, mTvLocation;

    private AudioRecorderButton mBtnRecorder;

    private BmobChatUser mTargetUser;
    private String mTargetId;

    private static int MsgPagerNum = 0;
    private NewMessageBroadcast mNewReceiver;
    private BmobChatManager mChatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTargetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
        mTargetId = mTargetUser.getObjectId();

        bindView();

        initView();

        initNewMessageBroadcast();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.id_chat_topbar);
        mPlv = (PullToRefreshListView) findViewById(R.id.id_chat_lv);

        mTvPicture = (TextView) findViewById(R.id.tv_picture);
        mTvCamera = (TextView) findViewById(R.id.tv_camera);
        mTvLocation = (TextView) findViewById(R.id.tv_location);

        mEtMsg = (EditText) findViewById(R.id.edit_user_comment);
        mBtnAdd = (Button) findViewById(R.id.chat_btn_add);
        mBtnEmo = (Button) findViewById(R.id.chat_btn_emo);
        mBtnVoice = (Button) findViewById(R.id.chat_btn_voice);
        mBtnKeyboard = (Button) findViewById(R.id.chat_btn_keyboard);
        mBtnSend = (Button) findViewById(R.id.chat_btn_send);
        mBtnRecorder = (AudioRecorderButton) findViewById(R.id.chat_btn_speak);

        mLayoutAdd = (LinearLayout) findViewById(R.id.layout_add);
        mLayoutEmoji = (LinearLayout) findViewById(R.id.layout_emo);
        mLayoutMore = (LinearLayout) findViewById(R.id.layout_more);
    }

    @Override
    protected void initView() {
        mChatManager = BmobChatManager.getInstance(this);

        mTopBar.setTitle("与" + mTargetUser.getUsername() + "聊天");
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

        mTvPicture.setOnClickListener(this);
        mTvLocation.setOnClickListener(this);
        mTvCamera.setOnClickListener(this);

        mBtnAdd.setOnClickListener(this);
        mBtnEmo.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnRecorder.setTargetId(mTargetId);
        mBtnRecorder.setOnSendMessageListener(this);

        mEtMsg.setOnClickListener(this);
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mBtnKeyboard.setVisibility(View.GONE);
                    mBtnVoice.setVisibility(View.GONE);
                } else {
                    if (mBtnVoice.getVisibility() != View.VISIBLE) {
                        mBtnVoice.setVisibility(View.VISIBLE);
                        mBtnSend.setVisibility(View.GONE);
                        mBtnKeyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });

        mBtnKeyboard.setOnClickListener(this);
        mBtnVoice.setOnClickListener(this);

        initListView();
    }

    private void initListView() {
        initOrRefresh();

        mPlv.getRefreshableView().setSelection(mAdapter.getCount());
    }

    private void initNewMessageBroadcast() {
        mNewReceiver = new NewMessageBroadcast();
        // 注册接收消息广播
        IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
        //设置广播的优先级别大于MessageActivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(mNewReceiver, intentFilter);
    }

    private class NewMessageBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra("fromId");
            String msgId = intent.getStringExtra("msgId");
            String msgTime = intent.getStringExtra("msgTime");
            // 收到这个广播的时候，message已经在消息表中，可直接获取
            if(TextUtils.isEmpty(from) && TextUtils.isEmpty(msgId) && TextUtils.isEmpty(msgTime)){
                BmobMsg msg = BmobChatManager.getInstance(ChatActivity.this).getMessage(msgId, msgTime);
                if (!from.equals(mTargetId))// 如果不是当前正在聊天对象的消息，不处理
                    return;
                //添加到当前页面
                mAdapter.add(msg);
                // 定位
                mPlv.getRefreshableView().setSelection(mAdapter.getCount());
                //取消当前聊天对象的未读标示
                BmobDB.create(ChatActivity.this).resetUnread(mTargetId);
            }
            // 记得把广播给终结掉
            abortBroadcast();
        }

    }

    public static final int NEW_MESSAGE = 0x001;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NEW_MESSAGE) {
                BmobMsg message = (BmobMsg) msg.obj;
                String uid = message.getBelongId();
                BmobMsg m = BmobChatManager.getInstance(ChatActivity.this).getMessage(message.getConversationId(), message.getMsgTime());
                if (!uid.equals(mTargetId))// 如果不是当前正在聊天对象的消息，不处理
                    return;
                mAdapter.add(m);
                // 定位
                mPlv.getRefreshableView().setSelection(mAdapter.getCount());
                //取消当前聊天对象的未读标示
                BmobDB.create(ChatActivity.this).resetUnread(mTargetId);
            }
        }
    };

    private MessageChatAdapter mAdapter;

    private void initOrRefresh() {
        if(mAdapter != null) {
            if (MyMessageReceiver.mNewNum != 0) {// 用于更新当在聊天界面锁屏期间来了消息，这时再回到聊天页面的时候需要显示新来的消息
                int news=  MyMessageReceiver.mNewNum;//有可能锁屏期间，来了N条消息,因此需要倒叙显示在界面上
                int size = initMsgData().size();
                for(int i=(news-1);i>=0;i--){
                    mAdapter.add(initMsgData().get(size-(i+1)));// 添加最后一条消息到界面显示
                }
                mPlv.getRefreshableView().setSelection(mAdapter.getCount());
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mAdapter = new MessageChatAdapter(this, initMsgData());
            mPlv.setAdapter(mAdapter);
        }
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<BmobMsg> initMsgData() {
        List<BmobMsg> list = BmobDB.create(this).queryMessages(mTargetId, MsgPagerNum);
        return list;
    }

    @Override
    public void onMessage(BmobMsg bmobMsg) {
        Message mHandlerMsg = mHandler.obtainMessage(NEW_MESSAGE);
        mHandlerMsg.obj = bmobMsg;
        mHandler.sendMessage(mHandlerMsg);
    }

    @Override
    public void onReaded(String conversionId, String msgTime) {
        // 此处应该过滤掉不是和当前用户的聊天的回执消息界面的刷新
        if (conversionId.split("&")[1].equals(mTargetId)) {
            // 修改界面上指定消息的阅读状态
            for (BmobMsg msg : mAdapter.getList()) {
                if (msg.getConversationId().equals(conversionId)
                        && msg.getMsgTime().equals(msgTime)) {
                    msg.setStatus(BmobConfig.STATUS_SEND_RECEIVERED);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNetChange(boolean isNetConnected) {
        if (!isNetConnected) {
            T.showShort(ChatActivity.this, R.string.toast_network_invalid);
        }
    }

    @Override
    public void onAddUser(BmobInvitation bmobInvitation) {

    }

    @Override
    public void onOffline() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 新消息到达，重新刷新界面
        initOrRefresh();
        MyMessageReceiver.ehList.add(this);// 监听推送的消息
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知和清空未读消息数
        BmobNotifyManager.getInstance(this).cancelNotify();
        BmobDB.create(this).resetUnread(mTargetId);
        //清空消息未读数-这个要在刷新之后
        MyMessageReceiver.mNewNum=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyMessageReceiver.ehList.remove(this);// 监听推送的消息
        // 停止录音
        mBtnRecorder.cancelRecord();
        // 停止播放录音
        if (NewRecordPlayClickListener.isPlaying
                && NewRecordPlayClickListener.currentPlayListener != null) {
            NewRecordPlayClickListener.currentPlayListener.stopPlayRecord();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case ImageUtils.CAMERA_REQUEST_CODE :
                    sendImageMessage(ImageUtils.imageUri);
                    break;
                case ImageUtils.GALLERY_REQUEST_CODE :
                    Uri uri = data.getData();
                    sendImageMessage(uri);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_user_comment :
                mPlv.getRefreshableView().setSelection(mAdapter.getCount());
                if(mLayoutMore.getVisibility() == View.VISIBLE) {
                    mLayoutMore.setVisibility(View.GONE);
                }
                break;
            case R.id.chat_btn_add :
                if (mLayoutMore.getVisibility() == View.GONE) {
                    mLayoutMore.setVisibility(View.VISIBLE);
                    mLayoutAdd.setVisibility(View.VISIBLE);
                    mLayoutEmoji.setVisibility(View.GONE);
                    hideSoftInputView();
                } else {
                    if (mLayoutEmoji.getVisibility() == View.VISIBLE) {
                        mLayoutEmoji.setVisibility(View.GONE);
                        mLayoutAdd.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutMore.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.chat_btn_emo :

                break;
            case R.id.chat_btn_send:
                final String msg = mEtMsg.getText().toString();
                if (msg.equals("")) {
                    T.showShort(ChatActivity.this, "请输入发送消息!");
                    return;
                }
                boolean isNetConnected = NetworkUtils.isConnected(this);
                if (!isNetConnected) {
                    T.showShort(this, R.string.toast_network_invalid);
                    // return;
                }
                // 组装BmobMessage对象
                BmobMsg message = BmobMsg.createTextSendMsg(this, mTargetId, msg);
                message.setExtra("Bmob");
                // 默认发送完成，将数据保存到本地消息表和最近会话表中
                mChatManager.sendTextMessage(mTargetUser, message);
                // 刷新界面
                refreshMessage(message);
                break;
            case R.id.chat_btn_voice :
                mEtMsg.setVisibility(View.GONE);
                mLayoutMore.setVisibility(View.GONE);
                mBtnVoice.setVisibility(View.GONE);
                mBtnKeyboard.setVisibility(View.VISIBLE);
                mBtnRecorder.setVisibility(View.VISIBLE);
                hideSoftInputView();
                break;
            case R.id.chat_btn_keyboard :
                showEditState(false);
                break;
            case R.id.tv_picture :
                ImageUtils.pickImageFromGallery(ChatActivity.this);
                break;
            case R.id.tv_camera :
                ImageUtils.pickImageFromCamera(ChatActivity.this);
                break;
        }
    }

    /**
     * 发送图片消息
     *
     * @param uri
     */
    private void sendImageMessage(Uri uri) {
        if(mLayoutMore.getVisibility() == View.VISIBLE) {
            mLayoutMore.setVisibility(View.GONE);
            mLayoutAdd.setVisibility(View.GONE);
            mLayoutEmoji.setVisibility(View.GONE);
        }

        String path = ImageUtils.getRealPathFromURI(uri, ChatActivity.this);

        mChatManager.sendImageMessage(mTargetUser, path, new UploadListener() {
            @Override
            public void onStart(BmobMsg bmobMsg) {
                refreshMessage(bmobMsg);
            }

            @Override
            public void onSuccess() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                T.showShort(ChatActivity.this, s);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 发送语音
     *
     * @param path
     * @param time
     */
    @Override
    public void sendMessage(final String path, final int time) {
        mChatManager.sendVoiceMessage(mTargetUser, path, time, new UploadListener() {
            @Override
            public void onStart(BmobMsg bmobMsg) {
                refreshMessage(bmobMsg);
            }

            @Override
            public void onSuccess() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                mAdapter.notifyDataSetChanged();
                T.showShort(ChatActivity.this, "发送失败");
            }
        });
    }

    /**
     * 刷新界面
     */
    private void refreshMessage(BmobMsg msg) {
        // 更新界面
        mAdapter.add(msg);
        mPlv.getRefreshableView().setSelection(mAdapter.getCount());
        mEtMsg.setText("");
    }

    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     * @Title: showEditState
     * @Description: TODO
     * @param @param isEmo: 用于区分文字和表情
     * @return void
     * @throws
     */
    private void showEditState(boolean isEmo) {
        mEtMsg.setVisibility(View.VISIBLE);
        mBtnKeyboard.setVisibility(View.GONE);
        mBtnVoice.setVisibility(View.VISIBLE);
        mBtnRecorder.setVisibility(View.GONE);
        mEtMsg.requestFocus();
        if (isEmo) {
            mLayoutMore.setVisibility(View.VISIBLE);
            mLayoutMore.setVisibility(View.VISIBLE);
//            layout_emo.setVisibility(View.VISIBLE);
            mLayoutAdd.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            mLayoutMore.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 显示软键盘
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(mEtMsg, 0);
        }
    }

}
