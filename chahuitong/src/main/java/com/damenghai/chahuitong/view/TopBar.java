package com.damenghai.chahuitong.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.R;

public class TopBar extends LinearLayout {
	private ImageView leftView, rightView;
	private TextView titleView;
	private ImageView titleImage;

	private int leftSrc;
	private String titleText;
	private float titleTextSize;
	private int titleTextColor;
	private int titleSrc;
	private int rightSrc;

	private OnLeftClickListener leftListener;
	private onRightClickListener rightListener;

	public interface OnLeftClickListener {
		void onLeftClick();
	}
	
	public interface onRightClickListener {
		void onRightClick();
	}

	public void setOnLeftClickListener(OnLeftClickListener leftListener) {
		this.leftListener = leftListener;
	}
	
	public void setOnRightClickListener(onRightClickListener rightListener) {
		this.rightListener = rightListener;
	}

	@SuppressLint("NewApi")
	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// 获取attrs中自定义属性的值
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
		leftSrc = ta.getResourceId(R.styleable.TopBar_leftSrc, 0);

		titleText = ta.getString(R.styleable.TopBar_titleText);
		titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 0);
		titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, 0);
		titleSrc = ta.getResourceId(R.styleable.TopBar_titleImage, 0);

		rightSrc = ta.getResourceId(R.styleable.TopBar_rightSrc, 0);
		ta.recycle();

        if(isInEditMode()) return;

        //从布局文件中获取控件
		LayoutInflater.from(context).inflate(R.layout.top_bar, this);
		leftView = (ImageView) findViewById(R.id.titlebar_iv_left);
		titleView = (TextView) findViewById(R.id.titlebar_text);
		titleImage = (ImageView) findViewById(R.id.titlebar_image);
		rightView = (ImageView) findViewById(R.id.titlebar_iv_right);
		//将attrs中获取的值设置到控件中
		setLeftSrc(leftSrc);
        setRightSrc(rightSrc);
		setTitle(titleText);
        setTitleTextColor(titleTextColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
		setTitleBackgroud(titleSrc);

		leftView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(leftListener != null) {
					leftListener.onLeftClick();
				}
			}
		});

		rightView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(rightListener != null)
					rightListener.onRightClick();
			}
		});
	}

	public void setLeftSrc(int resid) {
		leftView.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
		leftView.setImageResource(resid);
	}

	public void setLeftVisibility(int visibility) {
		leftView.setVisibility(visibility);
	}
	
	public void setRightSrc(int resid) {
		rightView.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
		rightView.setImageResource(resid);
	}

	public void setRightVisibility(int visibility) {
		rightView.setVisibility(visibility);
	}

	public void setTitle(String text) {
        if(text != null) {
            titleView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
            titleView.setText(text);
        }
	}

	public void setTitleBackgroud(int resid) {
		titleImage.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
		titleImage.setImageResource(resid);
	}

	public void setTitleTextColor(int color) {
		titleView.setTextColor(color);
	}
	
}
