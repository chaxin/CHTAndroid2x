package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.PersonalAPI;
import com.damenghai.chahuitong2.base.BaseApplication;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.bean.Personal;
import com.damenghai.chahuitong2.bean.User;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.fragment.CoterieFragment;
import com.damenghai.chahuitong2.utils.ImageConfigHelper;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.RoundImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.views.PgyerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends BaseFragmentActivity implements OnItemClickListener, OnClickListener {
    public static int LOGIN_REQUEST_CODE = 0x100;
    public static int PROFILE_REQUEST_CODE = 0x101;

    public static boolean isLogin = false;

    private String mKey;
    private User mUser;

    private ImageView mIvBack;
    private RoundImageView mIvAvatar;
    private TextView mTvUsername;
    private ImageView mIvLogout;
    private Button mBtnLogin;
    private GridView mGridView;
    private TextView mDeposit;
    private Button mBtnVoucher;
    private TextView mPoint;
    private ArrayList<Personal> mDatas;
    private Adapter mAdapter;
    private LinearLayout mLayout;
    private FrameLayout mMyCoterie;
    private CoterieFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        PgyFeedbackShakeManager.unregister();

        findViewById();

        initView();

        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) return;
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mKey = SessionKeeper.readSession(PersonalActivity.this);
            if(mKey.equals("")) return;
            loadUserInfo();
        } else if(requestCode == PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadUserInfo();
        }
    }

    private void findViewById() {
        mLayout = (LinearLayout) findViewById(R.id.personal_items);
        mMyCoterie = (FrameLayout) findViewById(R.id.personal_coterie);
        mIvBack = (ImageView) findViewById(R.id.personal_back);
        mIvAvatar = (RoundImageView) findViewById(R.id.personal_avatar);
        mTvUsername = (TextView) findViewById(R.id.personal_username);
        mIvLogout = (ImageView) findViewById(R.id.personal_logout);
        mBtnLogin = (Button) findViewById(R.id.personal_login);
        mGridView = (GridView) findViewById(R.id.personal_gridview);
        mDeposit = (TextView) findViewById(R.id.personal_deposit);
        mBtnVoucher = (Button) findViewById(R.id.personal_voucher);
        mPoint = (TextView) findViewById(R.id.personal_point);
    }

    private void initView() {
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(PersonalActivity.this);

        mKey = SessionKeeper.readSession(this);
        if(!mKey.equals("")) {
            loadUserInfo();
        } else {
            mBtnLogin.setVisibility(View.VISIBLE);
            isLogin = false;
        }

        mDatas = new ArrayList<Personal>();
        mAdapter = new Adapter(this, mDatas, R.layout.gridview_item_personal);

        mGridView.setAdapter(mAdapter);

        mIvBack.setOnClickListener(this);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
            }
        });

        mIvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                builder.setMessage("确定要退出吗？");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionKeeper.writeSession(PersonalActivity.this, "");
                        mIvAvatar.setVisibility(View.GONE);
                        mTvUsername.setVisibility(View.GONE);
                        mBtnLogin.setVisibility(View.VISIBLE);
                        mDeposit.setVisibility(View.INVISIBLE);
                        mPoint.setVisibility(View.INVISIBLE);
                        mIvLogout.setVisibility(View.GONE);
                        BaseApplication.getInstance().logout();
                        isLogin = false;
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.create().show();

            }
        });

        mGridView.setOnItemClickListener(this);
    }

    private void loadUserInfo() {
        PersonalAPI.userInfo(mKey, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    mUser = new Gson().fromJson(datas.getJSONObject("member_info").toString(), User.class);
                    if (mUser != null) setUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUserInfo() {
        BitmapUtils util = new BitmapUtils(this);
        util.display(mIvAvatar, mUser.getAvator(), ImageConfigHelper.getAvatarConfig(PersonalActivity.this));
        mIvAvatar.setOnClickListener(this);
        mTvUsername.setText(mUser.getUsername());
        mDeposit.setText(mUser.getPredepoit());
        mDeposit.setVisibility(View.VISIBLE);
        mPoint.setText(mUser.getPoint());
        mPoint.setVisibility(View.VISIBLE);
        mIvAvatar.setVisibility(View.VISIBLE);
        mTvUsername.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.GONE);
        mIvLogout.setVisibility(View.VISIBLE);
        isLogin = true;
    }

    private void initData() {
        mDatas.add(new Personal("我的订单", R.drawable.icon_personal_order, "http://www.chahuitong.com/wap/index.php/Home/Index/orderList"));
        mDatas.add(new Personal("购物车", R.drawable.icon_personal_cart, "http://www.chahuitong.com/wap/index.php/Home/Index/cart"));
        mDatas.add(new Personal("我的收藏", R.drawable.icon_personal_favorites, "http://www.chahuitong.com/wap/index.php/Home/Index/favorites"));
        mDatas.add(new Personal("收货地址", R.drawable.icon_personal_address, "http://www.chahuitong.com/wap/index.php/Home/Index/address"));
        mDatas.add(new Personal("通知中心", R.drawable.icon_personal_msg, "http://www.chahuitong.com/wap/index.php/Home/Index/msg"));
        mDatas.add(new Personal("社区", R.drawable.icon_personal_friends, "coterie"));
        mDatas.add(new Personal("评价", R.drawable.icon_personal_comment, "http://www.chahuitong.com/wap/index.php/Home/Index/message"));
        mDatas.add(new Personal("意见反馈", R.drawable.icon_personal_feedback, "feedback"));
        mDatas.add(new Personal("免费话费", R.drawable.icon_personal_phone, "http://114.215.108.10:30001/Web/CHT/index.html"));
    }

    private void backDown() {
        if(mMyCoterie.getVisibility() == View.VISIBLE && mFragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.remove(mFragment);
            ft.commit();

            mMyCoterie.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        } else {
            finishActivity();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url = mDatas.get(i).getUrl();
        if(url.contains("114.215.108.10:30001")) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            openActivity(WebViewActivity.class, bundle);
            return;
        }
        if(isLogin) {
            Class<? extends Activity> clazz = mDatas.get(i).getClazz();
            if (clazz != null) {
                openActivity(clazz);
            } else if (!TextUtils.isEmpty(url)) {
                if (url.contains("coterie")) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    mFragment = new CoterieFragment();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(R.id.personal_coterie, mFragment);
                    ft.commit();

                    mLayout.setVisibility(View.GONE);
                    mMyCoterie.setVisibility(View.VISIBLE);
                } else if (url.contains("feedback")) {
                    PgyFeedbackShakeManager.setShakingThreshold(Integer.MAX_VALUE);

                    // 以对话框的形式弹出
                    PgyerDialog.setDialogTitleBackgroundColor("#1b8b80");
                    PgyFeedback.getInstance().showDialog(PersonalActivity.this);
                } else if(url.contains("orderList")) {
                    openActivity(OrderListActivity.class);
                } else if(url.contains("address")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("state", "delete");
                    openActivity(AddressListActivity.class, bundle);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    openActivity(WebViewActivity.class, bundle);
                }
            }
        } else {
            openActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back :
                backDown();
                break;
            case R.id.personal_avatar :
                openActivityForResult(ProfileActivity.class, PROFILE_REQUEST_CODE);
                break;
            case R.id.personal_voucher :
                if(!SessionKeeper.readSession(PersonalActivity.this).equals("")) {
                    openActivity(VoucherActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            backDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Adapter extends BaseListAdapter<Personal> {

        public Adapter(Context context, List<Personal> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(convertView, mContext, mResId, parent, position);

            convert(holder, getItem(position));

            int itemHeight = (mGridView.getHeight() - mGridView.getPaddingLeft() * 4) / 3;

            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);

            holder.getConvertView().setLayoutParams(params);
            return holder.getConvertView();
        }

        @Override
        public void convert(ViewHolder holder, Personal personal) {
            holder.setText(R.id.personal_item_text, personal.getText())
                    .setImageResource(R.id.personal_item_image, personal.getImage());
            if(holder.getPosition() == getCount() - 1) {
                TextView tv = holder.getView(R.id.personal_item_text);
                tv.setTextColor(getResources().getColor(R.color.red));
            } else {
                TextView tv = holder.getView(R.id.personal_item_text);
                tv.setTextColor(getResources().getColor(R.color.text_caption));
            }
        }
    }

}
