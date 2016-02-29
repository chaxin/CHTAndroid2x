package com.damenghai.chahuitong2.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.pay.wxpay.Constants;
import com.damenghai.chahuitong2.utils.T;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {}

	@Override
	public void onResp(BaseResp resp) {
        EventBus.getDefault().post(resp);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode == 0) {
                T.showShort(WXPayEntryActivity.this, R.string.toast_pay_success);
            } else {
                T.showShort(WXPayEntryActivity.this, R.string.toast_pay_fail);
            }
            finish();
		}
    }

    private void openActivity(Class<? extends Activity> clazz, int flags) {
        Intent intent = new Intent(WXPayEntryActivity.this, clazz);
        if(flags != 0) intent.setFlags(flags);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
}