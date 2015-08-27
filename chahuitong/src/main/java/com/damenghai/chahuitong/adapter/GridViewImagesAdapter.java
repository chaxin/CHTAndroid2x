package com.damenghai.chahuitong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.ImageUrls;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/8/24.
 */
public class GridViewImagesAdapter extends CommonAdapter<ImageUrls> {
    public GridViewImagesAdapter(Context context, List<ImageUrls> mDatas, int resId) {
        super(context, mDatas, resId);
    }
    @SuppressLint("NewApi")
    @Override
    public void convert(ViewHolder holder, ImageUrls imageUrls) {
        GridView gv = (GridView) holder.getParent();
        int numCols = gv.getNumColumns();
        int width = ((gv.getWidth() - gv.getHorizontalSpacing() * (numCols - 1))
                - gv.getPaddingLeft() - gv.getPaddingRight()) / numCols;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.getView(R.id.gridview_item_image).setLayoutParams(params);
        holder.setImageResource(R.id.gridview_item_image, imageUrls.getResId());
    }
}
