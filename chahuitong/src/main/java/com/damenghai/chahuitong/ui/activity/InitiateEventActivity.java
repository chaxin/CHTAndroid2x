package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.AddImageGridAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.ImageUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.WrapHeightGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/9/4.
 */
public class InitiateEventActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TopBar mTopBar;
    private EditText mTheme;
    private EditText mLocation;
    private EditText mStart;
    private EditText mDuration;
    private EditText mPhone;
    private EditText mCost;
    private WrapHeightGridView mGvImages;
    private Button mCommit;

    private Travel mTravel;

    private ArrayList<Uri> mImages;

    private AddImageGridAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_event);

        mTravel = (Travel) getIntent().getSerializableExtra("event");

        findViewById();

        initView();
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.initiate_top);
        mTheme = (EditText) findViewById(R.id.initiate_theme);
        mLocation = (EditText) findViewById(R.id.initiate_location);
        mStart = (EditText) findViewById(R.id.initiate_start);
        mDuration = (EditText) findViewById(R.id.initiate_duration);
        mPhone = (EditText) findViewById(R.id.initiate_phone);
        mCost = (EditText) findViewById(R.id.initiate_cost);
        mCommit = (Button) findViewById(R.id.initiate_commit);
        mGvImages = (WrapHeightGridView) findViewById(R.id.initiate_images);
    }

    private void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        if(mTravel != null) {
            mTheme.setText(mTravel.getTitle());
            mLocation.setText(mTravel.getLocation());
            mStart.setText(mTravel.getJoin_time());
            mDuration.setText(mTravel.getDuration() + "");
            mPhone.setText(mTravel.getPhone());
            mCost.setText(mTravel.getFee() + "");
        }

        mCommit.setOnClickListener(this);

        mImages = new ArrayList<Uri>();
        mImageAdapter = new AddImageGridAdapter(this, mImages, R.layout.gridview_item_image);
        mGvImages.setAdapter(mImageAdapter);
        mGvImages.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String theme = mTheme.getText().toString();
        String location = mLocation.getText().toString();
        String start = mStart.getText().toString();
        String duration = mDuration.getText().toString();
        String phone = mPhone.getText().toString();
        String cost = mCost.getText().toString();
        String comment = mCost.getText().toString();

        if(TextUtils.isEmpty(theme) || TextUtils.isEmpty(location) || TextUtils.isEmpty(start)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(comment)) {
            T.showShort(this, "请填写完整信息");
        }

        mTravel = new Travel();
        mTravel.setTitle(theme);
        mTravel.setLocation(location);
        mTravel.setJoin_time(start);
        mTravel.setDuration(Integer.parseInt(duration));
        mTravel.setPhone(phone);
        mTravel.setFee(Integer.parseInt(cost));

        commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUtils.GALLERY_REQUEST_CODE :
                if(resultCode == Activity.RESULT_CANCELED) return;

                Uri uri = data.getData();
                mImages.add(uri);
                mImageAdapter.notifyDataSetChanged();
                break;
            case ImageUtils.CAMERA_REQUEST_CODE :
                if(resultCode == Activity.RESULT_CANCELED) ImageUtils.deleteImageUri(this);

                mImages.add(ImageUtils.imageUriFromCamera);
                mImageAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == mImages.size()) {
            ImageUtils.showImagePickDialog(this);
        }
    }

    public void commit() {
        StringBuilder images = new StringBuilder();
        for(int i=0; i<mImages.size(); i++) {
            String imageBase64 = ImageUtils.getBase64FromUri(mImages.get(i), this);
            if(i != mImages.size() - 1) images.append(imageBase64 + ",");
            else images.append(imageBase64);
        }
        mTravel.setPics(images.toString());

        HodorAPI.initiateEvent(this, mTravel, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                T.showShort(InitiateEventActivity.this, "发送成功");
                finish();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                T.showShort(InitiateEventActivity.this, error);
            }
        });
    }

}
