package com.damenghai.chahuitong.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Article;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.view.AbScrollView;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sgun on 15/8/18.
 */
public class ArticleWindow extends PopupWindow implements AbScrollView.OnDismissListener, View.OnClickListener {
    private String mId;

    private Article mArticle;

    private View mView;
    private AbScrollView mScroll;
    private RelativeLayout mBarLayout;
    private ImageView mBtnClose;
    private ImageView mBtnShare;
    private ImageView mBtnFavorites;
    private TextView mTvTitle;
    private TextView mTvContent;

    public ArticleWindow(Context context, String article_id) {
        super(context);

        struct();

        mId = article_id;

        loadData();

        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.article_detail_pop, null);

        initView();

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0x90000000);
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        mScroll = (AbScrollView) mView.findViewById(R.id.article_detail_layout);
        mScroll.setOnDismissListenre(this);
        mBarLayout = (RelativeLayout) mView.findViewById(R.id.article_topbar);
        mBtnClose = (ImageView) mBarLayout.findViewById(R.id.article_close);
        mBtnShare = (ImageView) mBarLayout.findViewById(R.id.article_share);
        mBtnFavorites = (ImageView) mBarLayout.findViewById(R.id.article_favorites);

        mTvTitle = (TextView) mView.findViewById(R.id.article_title);
        mTvContent = (TextView) mView.findViewById(R.id.article_content);

        mBtnClose.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        mBtnFavorites.setOnClickListener(this);
    }

    private void loadData() {
        if(mId == null) return;

        HodorAPI.articleShow(mId, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject content = obj.getJSONObject("content");
                    mArticle = new Article();
                    mArticle.setArticle_content(content.getString("article_content"));
                    mArticle.setTitle(content.getString("article_title"));
//                    mArticle = new Gson().fromJson(obj.getString("content"), Article.class);
                    setContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setContent() {
        if(mArticle == null) return;

        mTvTitle.setText(mArticle.getTitle());
        String html = mArticle.getArticle_content();
        Spanned content = Html.fromHtml(html, new MyImageGetter(), null);
        mTvContent.setText(content);
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
                T.showShort(ArticleWindow.this.getContentView().getContext(), "分享");
                break;
            case R.id.article_favorites :
                T.showShort(ArticleWindow.this.getContentView().getContext(), "收藏");
                break;
        }
    }

    private class MyImageGetter implements Html.ImageGetter {
        @Override
        public Drawable getDrawable(String source) {
            Log.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Log.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 5,
                    drawable.getIntrinsicHeight() * 5);
            Log.i("RG", "url---?>>>" + url);
            return drawable;
        }
    }

    // 这个类可以用来帮助开发者改进他们编写的应用，并且提供了各种的策略，
    // 这些策略能随时检查和报告开发者开发应用中存在的问题，比如可以监视
    // 那些本不应该在主线程中完成的工作或者其他的一些不规范和不好的代码
    public static void struct() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork() // or
                        // .detectAll()
                        // for
                        // all
                        // detectable
                        // problems
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
                .penaltyLog() // 打印logcat
                .penaltyDeath().build());
    }

}
