package com.damenghai.chahuitong.adapter;

import java.util.List;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.utils.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用的ListView适配器，所有的ListView适配器都应该继承该类
 * @author LiaoPeiKun
 *
 * @param <T> 用于存放视图数据的Bean类
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	/**
	 * 创建ViewHolder所需要的上下文
	 */
	protected Context mContext;

	/**
	 * 创建ViewHolder所需要的布局文件
	 */
	protected int mResId;

	/**
	 * 所有存放视图数据Bean类的容器
	 */
	protected List<T> mDatas;

	public CommonAdapter(Context context, List<T> mDatas, int resId) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mResId = resId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(convertView, mContext, mResId, parent, position);

		convert(holder, getItem(position));

		return holder.getConvertView();
	}

	/**
	 * 暴露给子类的接口，用于设置每个Item的具体数据，每个子类有各自的实现
	 * @param holder 复用视图控件的辅助类，通过它可以获得视图中的各个控件
	 * @param t 存放视图数据Bean类
	 */
	public abstract void convert(ViewHolder holder, T t);
}
