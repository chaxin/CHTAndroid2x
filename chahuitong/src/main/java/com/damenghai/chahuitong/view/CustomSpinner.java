package com.damenghai.chahuitong.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.damenghai.chahuitong.R;

public class CustomSpinner extends LinearLayout implements OnClickListener{
	private String mLabelText;
	private int mTextMarginLeft, mTextMarginRight, mTextGravity;
	
	private TextView mLabel;
	private Spinner mSpinner;
	private ImageView mDrop;	
		
	ArrayAdapter<CharSequence> adapter;
	
	public CustomSpinner(Context context) {
		super(context);
	}

	public CustomSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);		
		mLabelText = ta.getString(R.styleable.CustomSpinner_labelName);
		mTextMarginLeft = (int) ta.getDimension(R.styleable.CustomSpinner_textMarginLeft, 0);
		mTextMarginRight = (int) ta.getDimension(R.styleable.CustomSpinner_textMarginRight, 0);
		mTextGravity = ta.getInt(R.styleable.CustomSpinner_textGravity, 0);
		ta.recycle();
		
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.custom_spinner, this);
		
		mLabel = (TextView) findViewById(R.id.id_label);
		mSpinner = (Spinner) findViewById(R.id.id_spinner);
		mDrop = (ImageView) findViewById(R.id.id_drop);

		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(mTextMarginLeft, 0, mTextMarginRight, 0);
		mLabel.setLayoutParams(params);
		
		switch (mTextGravity) {
		case 0:
			mSpinner.setGravity(Gravity.START);
			break;
		case 1:
			mSpinner.setGravity(Gravity.END);
			break;
		default:
			break;
		}
//		
//		LayoutInflater textInflater = LayoutInflater.from(context);
//		View v = textInflater.inflate(R.layout.simple_spinner_item, null);
//		LayoutParams text1Params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		text1Params.gravity = Gravity.END;
//		TextView text1 = (TextView) v.findViewById(android.R.id.text1);
//		text1.setLayoutParams(text1Params);
	
		//如果没设置标签名就隐藏视图
		if(mLabelText != null) {
			mLabel.setText(mLabelText);			
		} else {
			mLabel.setVisibility(View.GONE);
		}
		
		mDrop.setOnClickListener(this);
	}

	public void setAdapter(ArrayList<CharSequence> arrayList) {
        adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		mSpinner.performClick();
	}
	
	public String getSelectedItem() {
		return mSpinner.getSelectedItem().toString();
	}
}
