package com.damenghai.chahuitong2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.utils.DateUtils;
import com.damenghai.chahuitong2.utils.ViewHolder;

import java.util.List;

import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;

/**
 * Created by Sgun on 15/10/12.
 */
public class MessageAdapter extends BaseListAdapter<BmobRecent> {

    public MessageAdapter(Context context, List<BmobRecent> data, int resId) {
        super(context, data, resId);
    }

    @Override
    public void convert(ViewHolder holder, BmobRecent message) {

        //填充数据
        String avatar = message.getAvatar();
        holder.loadAvatarImage(R.id.iv_recent_avatar, TextUtils.isEmpty(avatar) ? "" : avatar)
                .setText(R.id.tv_recent_name, message.getUserName())
                .setText(R.id.tv_recent_time, DateUtils.getChatTime(message.getTime()));

        //显示内容
        if(message.getType()== BmobConfig.TYPE_TEXT){
            holder.setText(R.id.tv_recent_msg, message.getMessage());
        }else if(message.getType()==BmobConfig.TYPE_IMAGE){
            holder.setText(R.id.tv_recent_msg, "[图片]");
        }else if(message.getType()==BmobConfig.TYPE_LOCATION){
            String all =message.getMessage();
            if(all!=null &&!all.equals("")){//位置类型的信息组装格式：地理位置&维度&经度
                String address = all.split("&")[0];
                holder.setText(R.id.tv_recent_msg, "[位置]" + address);
            }
        }else if(message.getType()==BmobConfig.TYPE_VOICE){
            holder.setText(R.id.tv_recent_msg, "[语音]");
        }

        int num = BmobDB.create(mContext).getUnreadCount(message.getTargetid());
        if (num > 0) {
            holder.setVisibility(R.id.tv_recent_unread, View.VISIBLE)
                    .setText(R.id.tv_recent_unread, num + "");
        } else {
            holder.setVisibility(R.id.tv_recent_unread, View.GONE);
        }
    }
}
