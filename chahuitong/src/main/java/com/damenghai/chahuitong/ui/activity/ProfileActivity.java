package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.UserAPI;
import com.damenghai.chahuitong.response.StringListener;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.utils.DateUtils;
import com.damenghai.chahuitong.utils.ImageConfigHelper;
import com.damenghai.chahuitong.utils.ImageUtils;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.RoundImageView;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Sgun on 15/9/18.
 */
public class ProfileActivity extends BaseActivity implements OnClickListener {
    private final int NAME_REQUEST_CODE = 0x600;
    private final int NICKNAME_REQUEST_CODE = 0x601;

    private final String[] SEX = new String[] {"女", "男"};

    private TopBar mTopBar;
    private RoundImageView mAvatar;
    private TextView mName;
    private TextView mNickname;
    private TextView mSex;
    private TextView mBorn;

    private Leader mLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById();

        initView();

        loadData();
    }

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.profile_bar);
        mAvatar = (RoundImageView) findViewById(R.id.profile_avatar);
        mName = (TextView) findViewById(R.id.profile_name);
        mNickname = (TextView) findViewById(R.id.profile_nickname);
        mSex = (TextView) findViewById(R.id.profile_sex);
        mBorn = (TextView) findViewById(R.id.profile_born);
    }

    @Override
    protected void initView() {
        mLeader = new Leader();
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                setResult(Activity.RESULT_CANCELED);
                finishActivity();
            }
        });

        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                goHome();
            }
        });
    }

    private void loadData() {
        UserAPI.showProfile(ProfileActivity.this, new StringListener(ProfileActivity.this) {
            @Override
            public void onSuccess(String response) {
                Leader leader = new Gson().fromJson(response, Leader.class);
                if (leader != null) {
                    setInfo(leader);
                }
            }
        });
    }

    private void setInfo(Leader leader) {
        BitmapUtils utils = new BitmapUtils(this);
        utils.display(mAvatar, leader.getMember_avatar(), ImageConfigHelper.getAvatarConfig(this));
        mName.setText(leader.getMember_truename());
        mNickname.setText(leader.getMember_name());
        mSex.setText(!TextUtils.isEmpty(leader.getMember_sex()) ? (leader.getMember_sex().equals("1") ? "男" : "女") : "");
        mBorn.setText(leader.getMember_birthday());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ImageUtils.GALLERY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED || data == null) return;
            ImageUtils.showZoomImage(ProfileActivity.this, data.getData());

        } else if(requestCode == ImageUtils.CAMERA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED) {
                ImageUtils.deleteImageUri(ProfileActivity.this);
                return;
            }

            ImageUtils.showZoomImage(ProfileActivity.this, ImageUtils.imageUri);
        } else if(requestCode == ImageUtils.ZOOM_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED || data == null) return;
            Uri uri = data.getData();
            mAvatar.setImageURI(uri);
            mLeader.setMember_avatar(ImageUtils.getBase64FromUri(ProfileActivity.this, uri));
        } else if(requestCode == NAME_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED || data == null) return;
            String text = data.getStringExtra("text");
            mName.setText(text);
            mLeader.setMember_truename(text);
        } else if(requestCode == NICKNAME_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED || data == null) return;
            Bundle bundle = data.getExtras();
            String text = bundle.getString("text");
            mNickname.setText(text);
            mLeader.setMember_name(text);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_avatar_layout :
                ImageUtils.showImagePickDialog(ProfileActivity.this);
                break;
            case R.id.profile_name_layout :
                Bundle name = new Bundle();
                name.putString("text", mName.getText().toString());
                name.putString("title", "修改姓名");
                openActivityForResult(ModifyActivity.class, NAME_REQUEST_CODE, name);
                break;
            case R.id.profile_nickname_layout :
                Bundle nickname = new Bundle();
                nickname.putString("text", mNickname.getText().toString());
                nickname.putString("title", "修改昵称");
                openActivityForResult(ModifyActivity.class, NICKNAME_REQUEST_CODE, nickname);
                break;
            case R.id.profile_sex_layout :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("修改性别");
                builder.setItems(SEX, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSex.setText(SEX[i]);
                        mLeader.setMember_sex(i + "");
                    }
                });
                builder.create().show();
                break;
            case R.id.profile_born_layout :
                DateUtils.showDateDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String born = year + "-" + (month + 1) + "-" + date;
                        mBorn.setText(born);
                        mLeader.setMember_birthday(born);
                    }
                });
                break;
            case R.id.profile_address_layout :
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/address");
                openActivity(WebViewActivity.class, bundle);
                break;
            case R.id.profile_security_layout :
                Bundle security = new Bundle();
                security.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/changepw");
                openActivity(WebViewActivity.class, security);
                break;
            case R.id.profile_commit :
                UserAPI.updateProfile(ProfileActivity.this, mLeader, new StringListener(ProfileActivity.this) {
                    @Override
                    public void onSuccess(String response) {
                        T.showShort(ProfileActivity.this, "修改成功");
                        setResult(Activity.RESULT_OK);
                        finishActivity();
                    }
                });
                break;
        }
    }
}
