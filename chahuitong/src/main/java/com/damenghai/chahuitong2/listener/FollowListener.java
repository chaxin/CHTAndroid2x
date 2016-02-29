package com.damenghai.chahuitong2.listener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.bean.Leader;
import com.damenghai.chahuitong2.bean.event.ChangeFansEvent;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Sgun on 15/9/17.
 */
public class FollowListener implements View.OnClickListener {
    private Leader mLeader;
    private Context mContext;
    private ViewHolder mHolder;
    private TextView mTv;

    public FollowListener(Context context, Leader leader, ViewHolder holder) {
        this.mContext = context;
        this.mLeader = leader;
        this.mHolder = holder;
    }

    public FollowListener(Context context, Leader leader, TextView tv) {
        this.mLeader = leader;
        this.mContext = context;
        this.mTv = tv;
    }

    @Override
    public void onClick(View view) {
        AccountApi.addFollow(mContext, mLeader.getMember_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") == 404) {
                        T.showShort(mContext, obj.getString("content"));
                    } else {
                        if (mHolder != null) {
                            mHolder.setText(R.id.leader_item_follow, R.string.text_followed)
                                    .setTextColor(R.id.leader_item_follow, R.color.primary)
                                    .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_followed)
                                    .setTextOnClickListener(R.id.leader_item_follow, new UnFollowListener(mContext, mLeader, mHolder));
                        } else if (mTv != null) {
                            mTv.setText(R.string.text_followed);
                            mTv.setTextColor(mContext.getResources().getColor(R.color.primary));
                            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_followed);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            mTv.setCompoundDrawables(drawable, null, null, null);
                            mTv.setOnClickListener(new UnFollowListener(mContext, mLeader, mTv));
                        }

                        mLeader.setBeInstered(1);
                        T.showShort(mContext, R.string.toast_follow_success);
                        EventBus.getDefault().post(new ChangeFansEvent(mLeader.getMember_id(), 1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
