package com.damenghai.chahuitong.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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
	private ImageView mLeftImage, mRightImage;
	private TextView mTitleView;
	private ImageView mTitleImage;
    private TextView mRightText;

    private OnButtonClickListener mListener;
	private OnLeftClickListener leftListener;
	private onRightClickListener rightListener;

	public interface OnLeftClickListener {
		void onLeftClick();
	}

	public interface onRightClickListener {
		void onRightClick();
	}

	public interface OnButtonClickListener {
		void onLeftClick();
		void onRightClick();
	}

	public void setOnLeftClickListener(OnLeftClickListener leftListener) {
		this.leftListener = leftListener;
	}

	public void setOnRightClickListener(onRightClickListener rightListener) {
		this.rightListener = rightListener;
	}

	public void setOnButtonClickListener(OnButtonClickListener l) {
		this.mListener = l;
	}

	@SuppressLint("NewApi")
	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);

        Resources res = getResources();
        float defaultTextSize = res.getDimension(R.dimen.text_title);
        int defaultTextColor = res.getColor(android.R.color.white);
		
		// 获取attrs中自定义属性的值
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        int leftSrc = ta.getResourceId(R.styleable.TopBar_leftSrc, 0);
        String titleText = ta.getString(R.styleable.TopBar_titleText);
        float titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, defaultTextSize);
        int titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, defaultTextColor);
        int titleSrc = ta.getResourceId(R.styleable.TopBar_titleImage, 0);
        int rightSrc = ta.getResourceId(R.styleable.TopBar_rightSrc, 0);
        String rightText = ta.getString(R.styleable.TopBar_rightText);
		ta.recycle();

        if(isInEditMode()) return;

        //从布局文件中获取控件
		LayoutInflater.from(context).inflate(R.layout.top_bar, this);
		mLeftImage = (ImageView) findViewById(R.id.titlebar_iv_left);
		mTitleView = (TextView) findViewById(R.id.titlebar_text);
		mTitleImage = (ImageView) findViewById(R.id.titlebar_image);
		mRightImage = (ImageView) findViewById(R.id.titlebar_iv_right);
        mRightText = (TextView) findViewById(R.id.right_text);
        //将attrs中获取的值设置到控件中
		setLeftSrc(leftSrc);
        setRightSrc(rightSrc);
        setRightText(rightText);
		setTitle(titleText);
        setTitleTextColor(titleTextColor);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
		setTitleBackgroud(titleSrc);

		mLeftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLeftClick();
                } else if (leftListener != null) {
                    leftListener.onLeftClick();
                }
            }
        });

		mRightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick();
                } else if (rightListener != null) {
                    rightListener.onRightClick();
                }
            }
        });
	}

	public void setLeftSrc(int resid) {
		mLeftImage.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
		mLeftImage.setImageResource(resid);
	}

	public void setLeftVisibility(int visibility) {
		mLeftImage.setVisibility(visibility);
	}
	
	public void setRightSrc(int resid) {
		mRightImage.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
		mRightImage.setImageResource(resid);
	}

    public void setRightText(CharSequence text) {
        mRightText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        mRightText.setText(text);
    }

	public void setRightVisibility(int visibility) {
		mRightImage.setVisibility(visibility);
	}

	public void setTitle(CharSequence text) {
        if(text != null) {
            mTitleView.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
            mTitleView.setText(text);
        }
	}

	public void setTitleBackgroud(int resid) {
		mTitleImage.setVisibility(resid > 0 ? VISIBLE : GONE);
		mTitleImage.setImageResource(resid);
	}

	public void setTitleTextColor(int color) {
		mTitleView.setTextColor(color);
	}
	
}
