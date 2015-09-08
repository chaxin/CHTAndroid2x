package com.damenghai.chahuitong.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.ui.activity.WriteCommentActivity;
import com.damenghai.chahuitong.utils.StringUtils;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/8/29.
 */
public class StatusCommentAdapter extends CommonAdapter<Comment> {
    public StatusCommentAdapter(Context context, List<Comment> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public void convert(ViewHolder holder, final Comment comment) {
        if(comment.getMemberInfo() != null) {
            holder.loadDefaultImage(R.id.comment_avatar, comment.getMemberInfo().getMember_avatar())
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
                intent.putExtra("reply_to", comment.getMemberInfo().getMember_name());
                mContext.startActivity(intent);
            }
        });
    }

}
