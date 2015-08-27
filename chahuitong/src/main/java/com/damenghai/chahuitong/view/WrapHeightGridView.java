package com.damenghai.chahuitong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * Created by Sgun on 15/8/22.
 */
public class WrapHeightGridView extends GridView {

    public WrapHeightGridView(Context context) {
        super(context);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
