package com.damenghai.chahuitong.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.base.BaseFragmentActivity;
import com.damenghai.chahuitong.bean.Personal;
import com.damenghai.chahuitong.bean.User;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.fragment.CoterieFragment;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.RoundImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends BaseFragmentActivity implements OnItemClickListener, OnClickListener {
    public static int LOGIN_REQUEST_CODE = 0x100;

    public static boolean isLogin = false;

    private String mKey;
    private User mUser;

    private ImageView mIvBack;
    private RoundImageView mIvAvator;
    private TextView mTvUsername;
    private ImageView mIvLogout;
    private Button mBtnLogin;
    private GridView mGridView;
    private Button mBtnBalance;
    private Button mBtnPurse;
    private Button mBtnPoint;
    private ArrayList<Personal> mDatas;
    private Adapter mAdapter;
    private LinearLayout mLayout;
    private FrameLayout mMyCoterie;
    private CoterieFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

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
        }
    }

    private void findViewById() {
        mLayout = (LinearLayout) findViewById(R.id.personal_items);
        mMyCoterie = (FrameLayout) findViewById(R.id.personal_coterie);
        mIvBack = (ImageView) findViewById(R.id.personal_back);
        mIvAvator = (RoundImageView) findViewById(R.id.personal_avator);
        mTvUsername = (TextView) findViewById(R.id.personal_username);
        mIvLogout = (ImageView) findViewById(R.id.personal_logout);
        mBtnLogin = (Button) findViewById(R.id.personal_login);
        mGridView = (GridView) findViewById(R.id.personal_gridview);
        mBtnBalance = (Button) findViewById(R.id.personal_balance);
        mBtnPurse = (Button) findViewById(R.id.personal_purse);
        mBtnPoint = (Button) findViewById(R.id.personal_point);
    }

    private void initView() {
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
                SessionKeeper.writeSession(PersonalActivity.this, "");
                mIvAvator.setVisibility(View.GONE);
                mTvUsername.setVisibility(View.GONE);
                mBtnLogin.setVisibility(View.VISIBLE);
                isLogin = false;
            }
        });

        mGridView.setOnItemClickListener(this);

        mBtnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtnPurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadUserInfo() {
        HodorAPI.userInfo(mKey, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    mUser = new Gson().fromJson(datas.getJSONObject("member_info").toString(), User.class);
                    if(mUser != null) setUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUserInfo() {
        BitmapUtils util = new BitmapUtils(this, this.getCacheDir().getAbsolutePath());
        util.configDefaultLoadingImage(R.drawable.default_load_image);
        util.configDefaultLoadFailedImage(R.drawable.default_load_image);
        util.display(mIvAvator, mUser.getAvator());
        mTvUsername.setText(mUser.getUsername());
        mIvAvator.setVisibility(View.VISIBLE);
        mTvUsername.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.GONE);
        isLogin = true;
    }

    private void initData() {
        mDatas.add(new Personal("我的订单", R.drawable.icon_personal_order, "http://www.chahuitong.com/wap/index.php/Home/Index/orderList"));
        mDatas.add(new Personal("购物车", R.drawable.icon_personal_cart, "http://www.chahuitong.com/wap/index.php/Home/Index/cart"));
        mDatas.add(new Personal("我的收藏", R.drawable.icon_personal_favorites, "http://www.chahuitong.com/wap/index.php/Home/Index/favorites"));
        mDatas.add(new Personal("用户信息", R.drawable.icon_personal_info, "http://www.chahuitong.com/wap/index.php/Home/Index/myInfo"));
        mDatas.add(new Personal("收货地址", R.drawable.icon_personal_address, "http://www.chahuitong.com/wap/index.php/Home/Index/address"));
        mDatas.add(new Personal("我的发布", R.drawable.icon_personal_publish, "http://www.chahuitong.com/mobile/app/b2b/index.php/Home/Index/myList"));
        mDatas.add(new Personal("通知中心", R.drawable.icon_personal_msg, "http://www.chahuitong.com/wap/index.php/Home/Index/msg "));
        mDatas.add(new Personal("圈子", R.drawable.icon_personal_friends, "coterie"));
        mDatas.add(new Personal("评价", R.drawable.icon_personal_comment, "http://www.chahuitong.com/wap/index.php/Home/Index/message"));
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
        if (url != null && !url.equals("")) {
            if(isLogin) {
                if(url.contains("myList")) {
                    openActivity(MyTeaMarketActivity.class);
                    return;
                } else if(url.contains("coterie")) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    mFragment = new CoterieFragment();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(R.id.personal_coterie, mFragment);
                    ft.commit();

                    mLayout.setVisibility(View.GONE);
                    mMyCoterie.setVisibility(View.VISIBLE);

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("user", mUser);
//                    openActivity(CoterieActivity.class, bundle);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                openActivity(ContentActivity.class, bundle);
            } else {
                openActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back :
                backDown();
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

    private class Adapter extends CommonAdapter<Personal> {

        public Adapter(Context context, List<Personal> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(convertView, mContext, mResId, parent, position);

            convert(holder, getItem(position));

            int itemHeight = (mGridView.getHeight() - mGridView.getVerticalSpacing() * 4) / 3;

            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);

            holder.getConvertView().setLayoutParams(params);
            return holder.getConvertView();
        }

        @Override
        public void convert(ViewHolder holder, Personal personal) {
            holder.setText(R.id.personal_item_text, personal.getText())
                    .setImageResource(R.id.personal_item_image, personal.getImage());
        }
    }

}
