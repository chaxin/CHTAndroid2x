package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Article;
import com.damenghai.chahuitong.bean.response.ArticleResponse;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.ArticleWindow;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/15.
 */
public class NewsFragment extends Fragment {
    private static final String KEY_CONTENT = "NewsFragment:content";

    private View mView;
    private View mHeaderView;

    private PullToRefreshListView plv;
    private LinearLayout mHeader;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvDate;
    private ImageView mBtnShare;

    private ArrayList<Article> mDatas;
    private Adapter mAdapter;

    private String mContent;
    private int mCurrPage;

    public static NewsFragment get(String content) {
        NewsFragment fragment = new NewsFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_articles, container, false);

        plv = (PullToRefreshListView) mView.findViewById(R.id.articles_listview);

        ListView lv = plv.getRefreshableView();
        mHeaderView = inflater.inflate(R.layout.header_news_listview, lv, false);
        lv.addHeaderView(mHeaderView);
        mDatas = new ArrayList<Article>();
        mAdapter = new Adapter(getActivity(), mDatas, R.layout.listview_item_news);
        plv.setAdapter(mAdapter);

        initView();

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(0);
    }

    private void initView() {
        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(0);
            }
        });

        plv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData(mCurrPage + 1);
            }
        });

        plv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showWindow(mDatas.get(i - 2).getId());
            }
        });
    }

    private void loadData(final int page) {
        HodorAPI.newsShow(mContent, page, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                if (page == 0) {
                    mDatas.clear();
                }
                mCurrPage = page;

                try {
                    ArticleResponse articleResponse = new Gson().fromJson(response, ArticleResponse.class);
                    addData(articleResponse.getmArticles());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                plv.onRefreshComplete();
            }
        });
    }

    private void addData(ArrayList<Article> articles) {
        if(articles == null) return;
        for(int i=0; i<articles.size(); i++) {
            if(i == 0 && mCurrPage == 0) {
                initHeader(articles.get(0));
            } else {
                Article article = articles.get(i);
                if (!mDatas.contains(article)) {
                    mDatas.add(article);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void initHeader(final Article article) {
        mHeader = (LinearLayout) mHeaderView.findViewById(R.id.header_ll);
        mTvTitle = (TextView) mHeaderView.findViewById(R.id.news_header_title);
        mTvDate = (TextView) mHeaderView.findViewById(R.id.news_header_date);
        mTvDesc = (TextView) mHeaderView.findViewById(R.id.news_header_desc);
        mBtnShare = (ImageView) mHeaderView.findViewById(R.id.news_header_share);

        if(article == null) return;
        mTvTitle.setText(article.getTitle());
        mTvDate.setText(article.getTime());
        mTvDesc.setText(article.getAbstract());

        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWindow(article.getId());
            }
        });
    }

    private void showWindow(String id) {
        ArticleWindow popupWindow = new ArticleWindow(getActivity(), id);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnim);
        popupWindow.showAtLocation(getActivity().findViewById(R.id.article_rl), Gravity.TOP, 0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    private class Adapter extends CommonAdapter<Article> {

        public Adapter(Context context, List<Article> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Article article) {
            holder.setText(R.id.news_item_title, article.getTitle())
                    .setText(R.id.news_item_date, article.getTime())
                    .setText(R.id.news_item_desc, article.getAbstract())
                    .loadDefaultImage(R.id.news_item_image, "http://www.chahuitong.com/data/upload/cms/article/60/" + article.getImage().getName());
        }
    }
}
