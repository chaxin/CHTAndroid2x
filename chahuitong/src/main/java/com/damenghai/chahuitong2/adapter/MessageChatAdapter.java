package com.damenghai.chahuitong2.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.ImageUrls;
import com.damenghai.chahuitong2.ui.activity.ImageBrowserActivity;
import com.damenghai.chahuitong2.utils.DateUtils;
import com.damenghai.chahuitong2.utils.ViewHolder;

import cn.bmob.im.BmobDownloadManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.inteface.DownloadListener;

public class MessageChatAdapter extends BaseListAdapter<BmobMsg> {
	//8种Item的类型
	//文本
	private final int TYPE_RECEIVER_TXT = 0;
	private final int TYPE_SEND_TXT = 1;
	//图片
	private final int TYPE_SEND_IMAGE = 2;
	private final int TYPE_RECEIVER_IMAGE = 3;
	//位置
	private final int TYPE_SEND_LOCATION = 4;
	private final int TYPE_RECEIVER_LOCATION = 5;
	//语音
	private final int TYPE_SEND_VOICE =6;
	private final int TYPE_RECEIVER_VOICE = 7;

	private String mCurrentObjectId;

	public MessageChatAdapter(Context context, List<BmobMsg> data) {
		super(context, data, 0);
		mCurrentObjectId = BmobUserManager.getInstance(mContext).getCurrentUserObjectId();
	}

	@Override
	public int getItemViewType(int position) {
		BmobMsg msg = mData.get(position);
		if(msg.getMsgType()==BmobConfig.TYPE_IMAGE){
			return msg.getBelongId().equals(mCurrentObjectId) ? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
		} else if (msg.getMsgType()==BmobConfig.TYPE_LOCATION){
			return msg.getBelongId().equals(mCurrentObjectId) ? TYPE_SEND_LOCATION : TYPE_RECEIVER_LOCATION;
		} else if (msg.getMsgType()==BmobConfig.TYPE_VOICE) {
			return msg.getBelongId().equals(mCurrentObjectId) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
		} else {
			return msg.getBelongId().equals(mCurrentObjectId) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 8;
	}

    private ViewHolder createViewByType(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(type == TYPE_RECEIVER_IMAGE){//图片类型
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_received_image, parent, position);
        } else if(type == TYPE_SEND_IMAGE) {
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_sent_image, parent, position);
        } else if(type == TYPE_RECEIVER_LOCATION){//位置类型
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_received_location, parent, position);
        } else if(type == TYPE_SEND_LOCATION) {
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_sent_location, parent, position);
        } else if(type == TYPE_RECEIVER_VOICE){//语音类型
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_received_voice, parent, position);
        } else if(type == TYPE_SEND_VOICE) {
            return ViewHolder.get(convertView, mContext, R.layout.item_chat_sent_voice, parent, position);
        } else {//剩下默认的都是文本
            return getItemViewType(position) == TYPE_RECEIVER_TXT ?
                    ViewHolder.get(convertView, mContext, R.layout.item_chat_received_message, parent, position)
                    :
                    ViewHolder.get(convertView, mContext, R.layout.item_chat_sent_message, parent, position);
        }
    }

	@Override
	public int getCount() {
		return mData.size();
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = createViewByType(position, convertView, parent);

        bindView(holder, position);

        return holder.getConvertView();
    }

    public void bindView(final ViewHolder holder, int position) {
        final BmobMsg item = getItem(position);

        holder.setText(R.id.tv_time, DateUtils.getChatTime(Long.parseLong(item.getMsgTime())));

        // 设置消息的发送状态
        if(getItemViewType(position)==TYPE_SEND_TXT
                ||getItemViewType(position)==TYPE_SEND_LOCATION
                ||getItemViewType(position)==TYPE_SEND_VOICE){//只有自己发送的消息才有重发机制
            //状态描述
            if(item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){//发送成功
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE);
                if(item.getMsgType()==BmobConfig.TYPE_VOICE){
                    holder.setVisibility(R.id.tv_send_status, View.GONE)
                            .setVisibility(R.id.tv_voice_length, View.VISIBLE);
                } else {
                    holder.setVisibility(R.id.tv_send_status, View.VISIBLE)
                            .setText(R.id.tv_send_status, "已发送");
                }
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_FAIL){//服务器无响应或者查询失败等原因造成的发送失败，均需要重发
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.VISIBLE)
                        .setVisibility(R.id.tv_send_status, View.INVISIBLE);
                if(item.getMsgType()==BmobConfig.TYPE_VOICE){
                    holder.setVisibility(R.id.tv_voice_length, View.INVISIBLE);
                }
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){//对方已接收到
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE);
                if(item.getMsgType()==BmobConfig.TYPE_VOICE){
                    holder.setVisibility(R.id.tv_send_status, View.GONE)
                            .setVisibility(R.id.tv_voice_length, View.VISIBLE);
                }
//                else{
//                    holder.setVisibility(R.id.tv_send_status, View.VISIBLE)
//                            .setText(R.id.tv_send_status, "已阅读");
//                }
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_START){//开始上传
                holder.setVisibility(R.id.progress_load, View.VISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE)
                        .setVisibility(R.id.tv_send_status, View.INVISIBLE);
                if(item.getMsgType()==BmobConfig.TYPE_VOICE){
                    holder.setVisibility(R.id.tv_voice_length, View.GONE);
                }
            }
        }

        //根据类型显示内容
        final String text = item.getContent();
        switch (item.getMsgType()) {
            case BmobConfig.TYPE_TEXT:
                try {
//                    SpannableString spannableString = FaceTextUtils.toSpannableString(mContext, text);
                    holder.setText(R.id.tv_message, text);
                } catch (Exception e) {
                }
                break;

            case BmobConfig.TYPE_IMAGE://图片类
                try {
                    if (!TextUtils.isEmpty(text)) { //发送成功之后存储的图片类型的content和接收到的是不一样的
                        dealWithImage(position, holder, item);
                    }
                    holder.setOnClickListener(R.id.iv_picture, new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Intent intent =new Intent(mContext,ImageBrowserActivity.class);
                            ArrayList<ImageUrls> photos = new ArrayList<ImageUrls>();
                            ImageUrls urls = new ImageUrls();
                            urls.setBmiddle_pic(getImageUrl(item));
                            photos.add(urls);
                            intent.putExtra("pic", photos);
                            intent.putExtra("position", 0);
                            mContext.startActivity(intent);
                        }
                    });

                } catch (Exception e) {}
                break;

            case BmobConfig.TYPE_LOCATION://位置信息
                try {
                    if (text != null && !text.equals("")) {
                        String address  = text.split("&")[0];
                        final String latitude = text.split("&")[1];//维度
                        final String longtitude = text.split("&")[2];//经度
                        holder.setText(R.id.tv_location, address);
//                                .setOnFocusChangeListener(R.id.tv_location, new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View arg0) {
//                                // TODO Auto-generated method stub
//                                Intent intent = new Intent(mContext, LocationActivity.class);
//                                intent.putExtra("type", "scan");
//                                intent.putExtra("latitude", Double.parseDouble(latitude));//维度
//                                intent.putExtra("longtitude", Double.parseDouble(longtitude));//经度
//                                mContext.startActivity(intent);
//                            }
//                        });
                    }
                } catch (Exception e) {

                }
                break;
            case BmobConfig.TYPE_VOICE://语音消息
                try {
                    if (!TextUtils.isEmpty(text)) {
                        holder.setVisibility(R.id.tv_voice_length, View.VISIBLE);
                        String content = item.getContent();
                        if (item.getBelongId().equals(mCurrentObjectId)) {//发送的消息
                            if(item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED
                                    ||item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){//当发送成功或者发送已阅读的时候，则显示语音长度
                                String length = content.split("&")[2];
                                holder.setVisibility(R.id.tv_voice_length, View.VISIBLE)
                                        .setText(R.id.tv_voice_length, length + "\''");
                            }else{
                                holder.setVisibility(R.id.tv_voice_length, View.INVISIBLE);
                            }
                        } else {//收到的消息
                            boolean isExists = BmobDownloadManager.checkTargetPathExist(mCurrentObjectId, item);
                            if(!isExists){//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
                                String netUrl = content.split("&")[0];
                                final String length = content.split("&")[1];
                                BmobDownloadManager downloadTask = new BmobDownloadManager(mContext,item,new DownloadListener() {

                                    @Override
                                    public void onStart() {
                                        holder.setVisibility(R.id.progress_load, View.VISIBLE)
                                            .setVisibility(R.id.tv_voice_length, View.GONE)
                                            .setVisibility(R.id.iv_voice, View.INVISIBLE); // 只有下载完成才显示播放的按钮
                                    }

                                    @Override
                                    public void onSuccess() {
                                        holder.setVisibility(R.id.progress_load, View.GONE)
                                                .setVisibility(R.id.tv_voice_length, View.VISIBLE)
                                                .setText(R.id.tv_voice_length, length+"\''")
                                                .setVisibility(R.id.iv_voice, View.VISIBLE);
                                    }
                                    @Override
                                    public void onError(String error) {
                                        holder.setVisibility(R.id.progress_load, View.GONE)
                                                .setVisibility(R.id.tv_voice_length, View.GONE)
                                                .setVisibility(R.id.iv_voice, View.INVISIBLE);
                                    }
                                });
                                downloadTask.execute(netUrl);
                            }else{
                                String length = content.split("&")[2];
                                holder.setText(R.id.tv_voice_length, length);
                            }
                        }
                    }
                    //播放语音文件
                    holder.setOnClickListener(R.id.iv_voice, new NewRecordPlayClickListener(mContext, item, (ImageView) holder.getView(R.id.iv_voice)));
                } catch (Exception e) {

                }

                break;
            default:
                break;
        }
	}

    /** 获取图片的地址--
     * @Description: TODO
     * @param @param item
     * @param @return
     * @return String
     * @throws
     */
    private String getImageUrl(BmobMsg item){
        String showUrl = "";
        String text = item.getContent();
        if(item.getBelongId().equals(mCurrentObjectId)){//
            if(text.contains("&")){
                showUrl = text.split("&")[0];
            }else{
                showUrl = text;
            }
        }else{//如果是收到的消息，则需要从网络下载
            showUrl = text;
        }
        return showUrl;
    }

    /**
     *
     * 处理图片
     *
     */
    private void dealWithImage(int position, final ViewHolder holder, BmobMsg item){
        String text = item.getContent();
        if(getItemViewType(position)==TYPE_SEND_IMAGE){ //发送的消息
            if(item.getStatus()==BmobConfig.STATUS_SEND_START){
                holder.setVisibility(R.id.progress_load, View.VISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE)
                        .setVisibility(R.id.tv_send_status, View.INVISIBLE);
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE)
                        .setVisibility(R.id.tv_send_status, View.VISIBLE)
                        .setText(R.id.tv_send_status, "已发送");
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_FAIL){
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.VISIBLE)
                        .setVisibility(R.id.tv_send_status, View.INVISIBLE);
            } else if (item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){
                holder.setVisibility(R.id.progress_load, View.INVISIBLE)
                        .setVisibility(R.id.iv_fail_resend, View.INVISIBLE)
                        .setVisibility(R.id.tv_send_status, View.VISIBLE)
                        .setText(R.id.tv_send_status, "已阅读");
            }
//			如果是发送的图片的话，因为开始发送存储的地址是本地地址，发送成功之后存储的是本地地址+"&"+网络地址，因此需要判断下
            String showUrl = "";
            if(text.contains("&")){
                showUrl = text.split("&")[0];
            } else {
                showUrl = text;
            }
            //为了方便每次都是取本地图片显示
            holder.loadImage(R.id.iv_picture, showUrl);
        } else {
            holder.loadImage(R.id.iv_picture, text)
                    .setVisibility(R.id.progress_load, View.INVISIBLE);
        }
    }

    @Override
    public void convert(ViewHolder holder, BmobMsg bmobMsg) {

    }

}
