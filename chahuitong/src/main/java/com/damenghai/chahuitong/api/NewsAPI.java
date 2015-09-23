package com.damenghai.chahuitong.api;

import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.response.IResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 资讯API
 *
 * Created by Sgun on 15/9/22.
 */
public class NewsAPI {
    /**
     * 获取资讯文章
     *
     * @param id
     *              栏目的ID
     * @param page
     *              页数
     * @param l
     */
    public static void newsShow(String id, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("page", page + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/news/showMoreApi/", map, l);
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
