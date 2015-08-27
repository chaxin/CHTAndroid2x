package com.damenghai.chahuitong.ui.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.damenghai.chahuitong.AppManager;
import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.ui.fragment.WebFragment;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity {

    /**
     * 网站首页
     */
    public static final String WEB_ROOT = "http://www.chahuitong.com/wap/";

    public static final int ITEM_MAIN = 0;
    public static final int ITEM_BRAND = 1;
    public static final int ITEM_CATEGORY = 2;
    public static final int ITEM_CART = 3;
    public static final int ITEM_PERSONAL = 4;

    private final String WEB_BRAND = "http://www.chahuitong.com/wap/index.php/Home/Index/brand";
    private final String WEB_CATEGORY = "http://www.chahuitong.com/wap/index.php/Home/Index/category";
    private final String WEB_CART = "http://www.chahuitong.com/wap/index.php/Home/Index/cart";
    private final String WEB_MEMBER = "http://www.chahuitong.com/wap/index.php/Home/Index/member";

    private TopBar mTopBar;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioMain, mRadioBrand, mRadioCategory, mRadioCart, mRadioMember;

    private long backTime = 0L;
    private WebFragment mCurrentTab;
    private SparseArray<WebFragment> mFragments;

    public void setCurrentItem(int item) {
        switch (item) {
            case ITEM_MAIN :
                mRadioMain.setChecked(true);
                break;
            case ITEM_BRAND :
                mRadioBrand.setChecked(true);
                break;
            case ITEM_CATEGORY :
                mRadioCategory.setChecked(true);
                break;
            case ITEM_CART :
                mRadioCart.setChecked(true);
                break;
            case ITEM_PERSONAL :
                mRadioMember.setChecked(true);
                break;
            default :
                mRadioMain.setChecked(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initServices();
        findViewById();
        initView();
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.topBar);
        mRadioGroup = (RadioGroup) findViewById(R.id.id_home_tab);

        mRadioMain = (RadioButton) findViewById(R.id.home_tab_main);
        mRadioBrand = (RadioButton) findViewById(R.id.home_tab_brand);
        mRadioCategory = (RadioButton) findViewById(R.id.home_tab_category);
        mRadioCart = (RadioButton) findViewById(R.id.home_tab_cart);
        mRadioMember = (RadioButton) findViewById(R.id.home_tab_member);
    }

    private void initView() {
        mFragments = new SparseArray<WebFragment>();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                switch (checkedId) {
                    case R.id.home_tab_main:
                        addFragment(checkedId, WEB_ROOT);
                        mTopBar.setLeftVisibility(View.VISIBLE);
                        mTopBar.setRightVisibility(View.GONE);
                        mTopBar.setTitle("");
                        mTopBar.setTitleBackgroud(R.drawable.title_logo);
                        mTopBar.setBackgroundResource(R.color.primary);
                        mTopBar.setLeftSrc(R.drawable.btn_menu_item_selector);
                        mTopBar.setOnLeftClickListener(new OnLeftClickListener() {

                            @Override
                            public void onLeftClick() {
                                ((RadioButton) findViewById(R.id.home_tab_category)).setChecked(true);
                            }

                        });
                        break;
                    case R.id.home_tab_brand:
                        mTopBar.setTitle("品牌");
                        mTopBar.setTitleTextColor(getResources().getColor(android.R.color.white));
                        mTopBar.setLeftVisibility(View.GONE);
                        mTopBar.setRightVisibility(View.GONE);
                        mTopBar.setBackgroundResource(R.color.primary);
                        addFragment(checkedId, WEB_BRAND);
                        break;
                    case R.id.home_tab_category:
                        mTopBar.setTitle("所有分类");
                        mTopBar.setTitleTextColor(getResources().getColor(android.R.color.white));
                        mTopBar.setLeftVisibility(View.GONE);
                        mTopBar.setRightVisibility(View.GONE);
                        mTopBar.setBackgroundResource(R.color.primary);
                        addFragment(checkedId, WEB_CATEGORY);
                        break;
                    case R.id.home_tab_cart:
                        mTopBar.setTitle("购物车");
                        mTopBar.setTitleTextColor(getResources().getColor(android.R.color.white));
                        mTopBar.setLeftVisibility(View.GONE);
                        mTopBar.setRightVisibility(View.GONE);
                        mTopBar.setBackgroundResource(R.color.primary);
                        addFragment(checkedId,WEB_CART);
                        break;
                    case R.id.home_tab_member:
                        mTopBar.setTitle("我的");
                        mTopBar.setTitleTextColor(getResources().getColor(android.R.color.white));
                        mTopBar.setTitleTextColor(getResources().getColor(R.color.personal_top_bar_title));
                        mTopBar.setBackgroundColor(getResources().getColor(R.color.personal_top_bar_bg));
                        mTopBar.setLeftVisibility(View.VISIBLE);
                        mTopBar.setRightVisibility(View.VISIBLE);
                        mTopBar.setLeftSrc(R.drawable.icon_email);
                        mTopBar.setRightSrc(R.drawable.icon_logout_normal);
                        mTopBar.setOnLeftClickListener(new OnLeftClickListener() {
                            @Override
                            public void onLeftClick() {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/msg");
                                openActivity(ContentActivity.class, bundle);
                            }
                        });
                        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
                            @Override
                            public void onRightClick() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("是否退出登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences mSp = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = mSp.edit();
                                        editor.putString("key", "");
                                        editor.commit();
                                        if (mCurrentTab != null) {
                                            mCurrentTab.removeCookie();
                                        }
                                        removeFragment(R.id.home_tab_cart);
                                        removeFragment(R.id.home_tab_member);
                                        setCurrentItem(ITEM_MAIN);
                                    }

                                }).setNegativeButton("取消", null);
                                builder.create().show();
                            }
                        });
                        addFragment(checkedId, WEB_MEMBER);
                        break;
                }
            }
        });

        mRadioMain.setChecked(true);
    }

    // 初始化第三方服务
    private void initServices() {
        // 设置任意网络环境下都提示更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 友盟自动检测更新
        UmengUpdateAgent.update(this);
        // bugly初始化
        //CrashReport.initCrashReport(getApplicationContext(), "101172467", false);
    }

    private void addFragment(int checkedId, String url) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WebFragment fragment = mFragments.get(checkedId);
        if (fragment == null) {
            fragment = WebFragment.get(url);
            mFragments.put(checkedId, fragment);
            ft.add(R.id.id_fragment_main, fragment);
        } else {
            ft.show(fragment);
        }
        if (mCurrentTab != null) ft.hide(mCurrentTab);
        mCurrentTab = fragment;
        ft.commit();
    }

    public void removeFragment(int checkedId) {
        WebFragment fragment = mFragments.get(checkedId);
        if(fragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();

            mFragments.remove(checkedId);
            if(mCurrentTab == fragment) {
                mCurrentTab = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - backTime > 2000) {
                if (!mRadioMain.isChecked()) {
                    mRadioMain.setChecked(true);
                } else {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    backTime = System.currentTimeMillis();
                }
            } else {
                MobclickAgent.onKillProcess(this);
                AppManager.getInstance().AppExit(this);
            }
        }
        return true;
    }
}