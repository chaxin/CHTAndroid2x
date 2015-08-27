package com.damenghai.chahuitong.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class VerticalSwipeRefreshView extends SwipeRefreshLayout {
    private int mTouchSlop;
    private float mPrevX;

    public VerticalSwipeRefreshView(Context context) {
        this(context, null);
    }

    public VerticalSwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            setColorScheme(android.R.color.holo_orange_dark, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light, android.R.color.holo_green_light);

            // 获取默认最小距离
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE :
                if(Math.abs(ev.getX() - mPrevX) > mTouchSlop + 60) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

}
