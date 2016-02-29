package com.damenghai.chahuitong2.api;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.damenghai.chahuitong2.bean.Article;
import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.response.IResponseListener;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资讯API
 *
 * Created by Sgun on 15/9/22.
 */
public class ArticleApi implements Api {

    private static final String BASE_URL = "http://www.chahuitong.com/wap/index.php/home/news/";

    private static final String NEWS_LIST = BASE_URL + "showMoreApi";

    private static final String ARTICLE_INFO = BASE_URL + "showMoreApi";

    private Context mContext;

    public ArticleApi(Context context) {
        mContext = context;
    }

    /**
     * 获取资讯文章
     *
     * @param id
     *              栏目的ID
     * @param page
     *              页数
     * @param listener
     */
    public void showNewses(final String id, final int page, final ResponseCallBackListener<List<Article>> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, ErrorEvent.NETWORK_ERROR_MSG);
            }
            return;
        }

        if (TextUtils.isEmpty(id) || page < 0) {
            if (listener != null) {
                listener.onError(ErrorEvent.PARAM_NULL, "参数错误");
            }
            return;
        }

        new AsyncTask<Void, Void, List<Article>>() {

            @Override
            protected List<Article> doInBackground(Void... voids) {
                RequestParams params = new RequestParams();
                params.addBodyParameter("id", id);
                params.addBodyParameter("page", page + "");
                try {
                    String result = mHttpUtils.sendSync(POST, NEWS_LIST, params).readString();
                    ResponseContent<List<Article>> response = new Gson().fromJson(result, new TypeToken<ResponseContent<List<Article>>>(){}.getType());
                    return response.getContent();
                } catch (IOException | HttpException | JsonSyntaxException e) {
                    if (listener != null) listener.onError(ErrorEvent.RESPONSE_ERROR_CODE, ErrorEvent.RESPONSE_ERROR_MSG);
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Article> articles) {
                super.onPostExecute(articles);
                if (articles != null && listener != null) listener.onSuccess(articles);
            }
        }.execute();

    }

    /**
     * 资讯中文章详情
     *
     * @param id
     * @param l
     */
    public static void articleInfo(String id, IResponseListener l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("article_id", id);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/news/newsDetailApi", map, l);
    }

}
