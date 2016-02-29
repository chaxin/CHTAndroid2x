package com.damenghai.chahuitong2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.WrapHeightGridView;

import java.util.List;

/**
 * Created by Sgun on 15/9/1.
 */
public class AddImageGridAdapter extends BaseListAdapter<Uri> {

    public AddImageGridAdapter(Context context, List<Uri> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public Uri getItem(int position) {
        return position== mData.size() ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, Uri uri) {
        WrapHeightGridView gv = (WrapHeightGridView) holder.getParent();
         int horizontalSpacing = gv.getHorizontalSpacing();
        int width = (gv.getWidth() - horizontalSpacing * 2 - gv.getPaddingLeft() - gv.getPaddingRight()) / 3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.getView(R.id.gridview_item_image).setLayoutParams(params);

        if (holder.getPosition() == mData.size() && holder.getPosition() != 9) {
            holder.setImageResource(R.id.gridview_item_image, R.drawable.icon_addpic_unfocused)
                    .setVisibility(R.id.iv_delete_image, View.GONE);
        } else {
            holder.displayImageUri(R.id.gridview_item_image, uri)
                    .setVisibility(R.id.iv_delete_image, View.VISIBLE)
                    .setOnClickListener(R.id.iv_delete_image, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mData.remove(holder.getPosition());
                            notifyDataSetChanged();
                        }
                    });

        }
    }

    @Override
    public int getCount() {
        return mData.size() < 0 ? 1 : (mData.size() == 9 ? 9 : mData.size() + 1);
    }

}
