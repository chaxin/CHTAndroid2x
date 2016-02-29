package com.damenghai.chahuitong2.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.ImageUrls;
import com.damenghai.chahuitong2.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/8/24.
 */
public class GridViewImagesAdapter extends BaseListAdapter<ImageUrls> {
    public GridViewImagesAdapter(Context context, List<ImageUrls> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void convert(ViewHolder holder, ImageUrls imageUrls) {
        GridView gv = (GridView) holder.getParent();
        int numCols = gv.getNumColumns();
        int width;

        if(Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.JELLY_BEAN) {
            width = (gv.getWidth() - gv.getPaddingLeft() * (numCols + 1)) / numCols;
        } else {
            width = ((gv.getWidth() - gv.getHorizontalSpacing() * (numCols - 1))
                    - gv.getPaddingLeft() - gv.getPaddingRight()) / numCols;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.getView(R.id.gridview_item_image).setLayoutParams(params);
        holder.loadImage(R.id.gridview_item_image, imageUrls.getBmiddle_pic());
    }
}
