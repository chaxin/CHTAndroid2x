package com.damenghai.chahuitong.listener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/9/17.
 */
public class UnFollowListener implements View.OnClickListener {
    private Leader mLeader;
    private Context mContext;
    private ViewHolder mHolder;
    private TextView mTv;

    public UnFollowListener(Context context, Leader leader, ViewHolder holder) {
        this.mContext = context;
        this.mLeader = leader;
        this.mHolder = holder;
    }

    public UnFollowListener( Context context,Leader leader, TextView tv) {
        this.mLeader = leader;
        this.mContext = context;
        this.mTv = tv;
    }

    @Override
    public void onClick(View view) {
        HodorRequest.removeFollow(mContext, mLeader.getMember_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") == 404) {
                        T.showShort(mContext, obj.getString("content"));
                    } else {
                        if (mHolder != null) {
                            mHolder.setText(R.id.leader_item_follow, "加关注")
                                    .setTextColor(R.id.leader_item_follow, android.R.color.black)
                                    .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_unfollowed)
                                    .setTextOnClickListener(R.id.leader_item_follow, new FollowListener(mContext, mLeader, mHolder));
                        } else if (mTv != null) {
                            mTv.setText("加关注");
                            mTv.setTextColor(mContext.getResources().getColor(android.R.color.black));
                            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_unfollowed);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            mTv.setCompoundDrawables(drawable, null, null, null);
                            mTv.setOnClickListener(new FollowListener(mContext, mLeader, mTv));
                        }

                        mLeader.setBeInstered(0);
                        T.showShort(mContext, "取消成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
