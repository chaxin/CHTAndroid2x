package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.ArticleApi;
import com.damenghai.chahuitong2.bean.Article;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.ArticleWindow;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/15.
 */
public class NewsFragment extends Fragment implements OnRefreshListener<ListView>, OnLastItemVisibleListener, OnItemClickListener {
    private static final String KEY_CONTENT = "NewsFragment:content";

    private String mContent;

    private PullToRefreshListView mPlv;

    private ArrayList<Article> mDatas;

    private Adapter mAdapter;

    private int mCurrPage;

    private ArticleApi mApi;

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

        mApi = new ArticleApi(getActivity());

        mDatas = new ArrayList<>();

        mAdapter = new Adapter(getActivity(), mDatas, R.layout.listview_item_news);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        mPlv = (PullToRefreshListView) view.findViewById(R.id.articles_listview);

        mPlv.setAdapter(mAdapter);

        initView();

        loadData(1);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        mPlv.setOnRefreshListener(this);

        mPlv.setOnLastItemVisibleListener(this);

        mPlv.setOnItemClickListener(this);
    }

    private void loadData(final int page) {

        mApi.showNewses(mContent, page, new ResponseCallBackListener<List<Article>>() {
            @Override
            public void onSuccess(List<Article> articles) {
                if (page == 1) mDatas.clear();

                mCurrPage = page;

                mAdapter.addList(articles);

                if (mPlv.isRefreshing()) mPlv.onRefreshComplete();
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(getActivity(), message);
            }
        });

    }

    private void showDetailWindow(Article article) {
        ArticleWindow popupWindow = new ArticleWindow(getActivity(), article);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnim);
        popupWindow.showAtLocation(getActivity().findViewById(R.id.article_rl), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        showDetailWindow(mDatas.get(position - 1));
    }

    @Override
    public void onLastItemVisible() {
        loadData(mCurrPage + 1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    private class Adapter extends BaseListAdapter<Article> {

        public Adapter(Context context, List<Article> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Article article) {
            if (holder.getPosition() == 0) {
                holder.setVisibility(R.id.news_item_header, View.VISIBLE)
                        .setVisibility(R.id.news_item_layout, View.GONE)
                        .setText(R.id.news_header_title, article.getTitle())
                        .setText(R.id.news_header_date, article.getTime())
                        .setText(R.id.news_header_desc, article.getAbstract())
                        .loadImage(R.id.news_header_image, (article.getImage().getName().contains("upload")
                                ? "http://www.chahuitong.com/data/upload/cms/article/"
                                : "http://www.chahuitong.com/data/upload/cms/article/")
                                + article.getImage().getPath() + "/" + article.getImage().getName());

            } else {
                holder.setVisibility(R.id.news_item_header, View.GONE)
                        .setVisibility(R.id.news_item_layout, View.VISIBLE)
                        .setText(R.id.news_item_title, article.getTitle())
                        .setText(R.id.news_item_date, article.getTime())
                        .setText(R.id.news_item_desc, article.getAbstract())
                        .loadDefaultImage(R.id.news_item_image, (article.getImage().getName().contains("upload")
                                ? "http://www.chahuitong.com/data/upload/cms/article/"
                                : "http://www.chahuitong.com/data/upload/cms/article/")
                                + article.getImage().getPath() + "/" + article.getImage().getName());
            }
        }
    }
}
