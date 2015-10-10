package com.damenghai.chahuitong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.WrapHeightGridView;

import java.util.List;

/**
 * Created by Sgun on 15/9/1.
 */
public class AddImageGridAdapter extends CommonAdapter<Uri> {

    public AddImageGridAdapter(Context context, List<Uri> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public Uri getItem(int position) {
        return position==mDatas.size() ? null : mDatas.get(position);
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

        if (holder.getPosition() == mDatas.size() && holder.getPosition() != 9) {
            holder.setImageResource(R.id.gridview_item_image, R.drawable.icon_addpic_unfocused)
                    .setVisibility(R.id.iv_delete_image, View.GONE);
        } else {
            holder.displayImageUri(R.id.gridview_item_image, uri)
                    .setVisibility(R.id.iv_delete_image, View.VISIBLE)
                    .setOnClickListener(R.id.iv_delete_image, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatas.remove(holder.getPosition());
                            notifyDataSetChanged();
                        }
                    });

        }
    }

    @Override
    public int getCount() {
        return mDatas.size() < 0 ? 1 : (mDatas.size() == 9 ? 9 : mDatas.size() + 1);
    }

}
