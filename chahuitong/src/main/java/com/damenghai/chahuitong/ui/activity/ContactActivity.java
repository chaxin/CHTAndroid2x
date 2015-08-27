package com.damenghai.chahuitong.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.utils.HttpUtils;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;

public class ContactActivity extends BaseActivity implements OnClickListener {
	private int mId;
	private TopBar mTopbar;
	private TextView mTvDesc;
	private EditText mContent;
	private Button mCall, mSubmit;

	private Product mProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		mProduct = (Product) getIntent().getSerializableExtra("product");
        findViewById();

        initView();
	}

	private void findViewById() {
		mTopbar = (TopBar) findViewById(R.id.id_contact_topbar);
		mTvDesc = (TextView) findViewById(R.id.id_product_info);
		mCall = (Button) findViewById(R.id.id_call_phone);
		mSubmit = (Button) findViewById(R.id.id_msg_submit);
		mContent = (EditText) findViewById(R.id.id_input_msg);
	}

	private void initView() {
		mCall.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mTopbar.setOnLeftClickListener(new OnLeftClickListener() {

            @Override
            public void onLeftClick() {
                finish();
            }

        });

        mTvDesc.setText(mProduct.getDesc() + mProduct.getPhone());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_call_phone:
			Intent intent = new Intent(Intent.ACTION_DIAL); 
			intent.setData(Uri.parse("tel:" + mProduct.getPhone()));
			startActivity(intent);
			break;
		case R.id.id_msg_submit:
			submitMessage();
			break;
		}
	}
	
	private void submitMessage() {
		
		final String content = mContent.getText().toString();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int userId = 0;
				String results = HttpUtils.doPost(Constants.API_SEND_MESSAGE, "userid=" + userId + "&id=" + mId + "&content=" + content);
				if(results.equals("1")) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(ContactActivity.this).setMessage("留言成功").setPositiveButton("确定", null).show();
							if(mContent.length() > 0) {
								mContent.setText("");
							}
						}
						
					});
				}
			}
			
		}).start();
		
	}

}
