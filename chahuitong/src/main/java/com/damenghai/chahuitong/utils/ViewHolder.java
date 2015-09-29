package com.damenghai.chahuitong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

/**
 * 用于ListView复用视图控件的辅助类
 * @author LiaoPeiKun
 *
 */
public class ViewHolder {
	/**
	 * 上下文
	 */
	private Context mContext;
	
	/**
	 * 用于存放视图中需要复用的控件对象，map integers to Objects
	 */
	private SparseArray<View> mViews;
	
	/**
	 * 存放当前Item的位置
	 */
	private int mPosition;
	
	/**
	 * 复用的布局文件
	 */
	private View mConvertView;

	/**
	 * 父布局
	 *
	 */
	private ViewGroup mParent;

	public ViewHolder(Context context, int resId, ViewGroup parent, int position) {
		mPosition = position;
		mContext = context;
		mParent = parent;
		mConvertView = LayoutInflater.from(context).inflate(resId, parent, false);
		mViews = new SparseArray<View>();
		mConvertView.setTag(this);
	}
	
	/**
	 * 复用的入口函数，用于判断当前的视图是否有值，有值就不再重复New对象
	 * @param convertView 复用的视图
	 * @param context 创建视图的上下文
	 * @param resId 布局文件
	 * @param parent 
	 * @param position ListView中的Item位置
	 * @return
	 */
	public static ViewHolder get(View convertView, Context context, int resId, ViewGroup parent, int position) {
		if(convertView == null) {
			return new ViewHolder(context, resId, parent, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}
	
	/**
	 * 通过id获取存放在SparseArray<View>中View对象，如果没有这个对象就获取后put进去
	 * @param resId 需要查找的视图id
	 * @return 返回视图对象
	 */
	public<T extends View> T getView(int resId) {
		View view = mViews.get(resId);
		
		if(view == null) {
			view = mConvertView.findViewById(resId);
			mViews.put(resId, view);
		}
		return (T) view;
	}
	
	/**
	 * 获得复用视图
	 * @return
	 */
	public View getConvertView() {
		return mConvertView;
	}

	public ViewGroup getParent() {
		return mParent;
	}

	public int getPosition() {
		return mPosition;
	}

	public ViewHolder setVisibility(int viewId, int visibility) {
		getView(viewId).setVisibility(visibility);
		return this;
	}

	/**
	 * 设置TextView的文本
	 * @param viewId 需要设置的TextView控件
	 * @param text 需要设置的文本
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder setText(int viewId, CharSequence text) {
		TextView tv = getView(viewId);
		if(tv != null) tv.setText(text);
		return this;
	}

	public ViewHolder setTextColor(int viewId, int color) {
		TextView tv = getView(viewId);
		if(tv != null) tv.setTextColor(mContext.getResources().getColor(color));
		return this;
	}

	public ViewHolder setTextDrawableTop(int viewId, int resId) {
		TextView tv = getView(viewId);
		Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		tv.setCompoundDrawables(null, drawable, null, null);
		return this;
	}

	public ViewHolder setTextDrawableLeft(int viewId, int resId) {
		TextView tv = getView(viewId);
		Drawable drawable = mContext.getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		tv.setCompoundDrawables(drawable, null, null, null);
		return this;
	}

	public ViewHolder setTextOnClickListener(int resId, OnClickListener listener) {
		if(listener == null) return null;
		TextView tv = getView(resId);
		tv.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置ImageView的背景资源
	 * @param viewId 需要设置的ImageView控件
	 * @param resId 需要设置的背景资源id
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(resId);
		return this;
	}

	public ViewHolder displayImageUri(int viewId, Uri uri) {
		ImageView iv = getView(viewId);
		BitmapUtils utils = new BitmapUtils(mContext);
		utils.display(iv, ImageUtils.getRealPathFromURI(uri, mContext), ImageConfigHelper.getImageConfig(mContext));
		return this;
	}

	/**
	 * 在控件中显示网络图片
	 * @param viewId 控件的ID
	 * @param url 网络图片的完整url
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder loadDefaultImage(int viewId, String url) {
		if(url != null) {
			BitmapUtils util = new BitmapUtils(mContext, mContext.getCacheDir().getAbsolutePath());
			if (getView(viewId) != null && !url.equals(""))
				util.display(getView(viewId), url, ImageConfigHelper.getDefaultConfig(mContext));
		}
		return this;
	}

	/**
	 * 在控件中显示网络图片
	 * @param viewId 控件的ID
	 * @param url 网络图片的完整url
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder loadImage(int viewId, String url) {
		if(url != null) {
			BitmapUtils util = new BitmapUtils(mContext, mContext.getCacheDir().getAbsolutePath());
			if (getView(viewId) != null && !url.equals(""))
				util.display(getView(viewId), url, ImageConfigHelper.getImageConfig(mContext));
		}
		return this;
	}

    /**
     * 在控件中显示网络图片
     * @param viewId 控件的ID
     * @param url 网络图片的完整url
     * @return 返回Holder对象可用于链式编程
     */
    public ViewHolder loadAvatarImage(int viewId, String url) {
        if(url != null) {
            BitmapUtils util = new BitmapUtils(mContext, mContext.getCacheDir().getAbsolutePath());
            if (getView(viewId) != null && !url.equals(""))
                util.display(getView(viewId), url, ImageConfigHelper.getAvatarConfig(mContext));
        }
        return this;
    }

	/**
	 * 设置控件点击事件
	 * @param viewId 控件ID
	 * @param l 监听事件
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder setOnClickListener(int viewId, OnClickListener l) {
		View view = getView(viewId);
		if(view != null) view.setOnClickListener(l);
		return this;
	}
	
	/**
	 * 设置控件点击事件
	 * @param viewId 控件ID
	 * @return 返回Holder对象可用于链式编程
	 */
	public ViewHolder setAnimotion(int viewId, Animation animation) {
		View view = getView(viewId);
		if(view != null) view.setAnimation(animation);
		return this;
	}
}
