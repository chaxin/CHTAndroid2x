package com.damenghai.chahuitong.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.NewsAPI;
import com.damenghai.chahuitong.response.JsonObjectListener;
import com.damenghai.chahuitong.utils.ShareManager;
import com.damenghai.chahuitong.view.AbScrollView.OnDismissListener;
import com.umeng.socialize.controller.UMSocialService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/8/18.
 */
public class ArticleWindow extends PopupWindow implements OnDismissListener, OnClickListener, OnTouchListener {
    private Context mContext;

    private String mId;

    private View mView;
    private LinearLayout mLoadding;
    private AbScrollView mScroll;
    private WebView mWebView;
    private LinearLayout mBarLayout;
    private ImageView mBtnClose;
    private ImageView mBtnShare;

    public ArticleWindow(Context context, String article_id) {
        super(context);

        mContext = context;

        mId = article_id;

        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.article_detail_pop, null);

        initView();

        loadData();

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0x90000000);
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        mLoadding = (LinearLayout) mView.findViewById(R.id.article_loading);
        mScroll = (AbScrollView) mView.findViewById(R.id.article_detail_layout);
        mScroll.setOnDismissListenre(this);

        mWebView = (WebView) mView.findViewById(R.id.article_web);
        mBarLayout = (LinearLayout) mView.findViewById(R.id.article_topbar);
        mBtnClose = (ImageView) mBarLayout.findViewById(R.id.article_close);
        mBtnShare = (ImageView) mBarLayout.findViewById(R.id.article_share);

        mBtnClose.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
    }


    private void loadData() {
        if(mId == null) return;

        mWebView.loadUrl("http://www.chahuitong.com/wap/index.php/Home/News/detail/article_id/" + mId);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mLoadding.setVisibility(View.GONE);
                mScroll.setVisibility(View.VISIBLE);
                mScroll.setOnTouchListener(ArticleWindow.this);
                super.onPageFinished(view, url);
            }
        });

    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_close :
                dismiss();
                break;
            case R.id.article_share :
                final UMSocialService controller = ShareManager.create(mContext);
                NewsAPI.articleInfo(mId, new JsonObjectListener(mContext) {
                    @Override
                    public void onSuccess(JSONObject object) {
                        try {
                            controller.setShareContent(object.getString("article_title") + ", author:" + object.getString("article_author")
                                    + ", http://www.chahuitong.com/wap/index.php/Home/News/detail/article_id/" + mId);
                            controller.openShare((Activity) mContext, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            int top = mView.findViewById(R.id.article_ll).getTop();
            int y = (int) motionEvent.getY();
            if(y < top) {
                dismiss();
                return true;
            }
        }
        return false;
    }
}
