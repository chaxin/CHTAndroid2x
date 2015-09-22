package com.damenghai.chahuitong.view;

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
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.AbScrollView.OnDismissListener;

/**
 * Created by Sgun on 15/8/18.
 */
public class ArticleWindow extends PopupWindow implements OnDismissListener, OnClickListener, OnTouchListener {
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
