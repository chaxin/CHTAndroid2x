package com.damenghai.chahuitong2.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Comment;
import com.damenghai.chahuitong2.ui.activity.WriteCommentActivity;
import com.damenghai.chahuitong2.utils.StringUtils;
import com.damenghai.chahuitong2.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/8/29.
 */
public class StatusCommentAdapter extends BaseListAdapter<Comment> {
    public StatusCommentAdapter(Context context, List<Comment> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public int getCount() {
        return mData.size() == 0 ? 1 : mData.size();
    }

    @Override
    public Comment getItem(int position) {
        return mData.size() == 0 ? null : mData.get(position);
    }

    @Override
    public void convert(ViewHolder holder, final Comment comment) {
        if(mData.size() == 0) {
            holder.setVisibility(R.id.comment_none, View.VISIBLE)
                    .setVisibility(R.id.comment_avatar, View.GONE)
                    .setVisibility(R.id.comment_user_info, View.GONE);
        } else {
            holder.setVisibility(R.id.comment_none, View.GONE)
                    .setVisibility(R.id.comment_avatar, View.VISIBLE)
                    .setVisibility(R.id.comment_user_info, View.VISIBLE);

            if (comment.getMemberInfo() != null) {
                holder.loadAvatarImage(R.id.comment_avatar, comment.getMemberInfo().getMember_avatar())
                        .setText(R.id.comment_user, comment.getMemberInfo().getMember_name());
            }
            holder.setText(R.id.comment_time, comment.getComment_time())
                    .setText(R.id.comment_text, StringUtils.getSpannableContent(mContext,
                            (TextView) holder.getView(R.id.comment_text),
                            (comment.getReply_to() != null && comment.getReply_to().equals(""))
                                    ? comment.getComment()
                                    : "回复@" + comment.getReply_to() + ":" + comment.getComment()));
            holder.getView(R.id.comment_rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WriteCommentActivity.class);
                    intent.putExtra("status_id", comment.getContent_id());
                    if (comment.getMemberInfo() != null && !TextUtils.isEmpty(comment.getMemberInfo().getMember_name())) {
                        intent.putExtra("reply_to", comment.getMemberInfo().getMember_name());
                    }
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
