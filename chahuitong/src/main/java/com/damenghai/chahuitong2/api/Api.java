package com.damenghai.chahuitong2.api;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public interface Api {
    String BASE_URL = "http://www.chahuitong.com/mobile/index.php?";

    /** 发送Http工具类 */
    HttpUtils mHttpUtils = new HttpUtils();

    /** Http请求Post方式*/
    HttpRequest.HttpMethod POST = HttpRequest.HttpMethod.POST;

    /** Http请求Get方式 */
    HttpRequest.HttpMethod GET = HttpRequest.HttpMethod.GET;

    /** 首页Banner图片根地址 */
    String IMAGE_PATH_ROOT = "http://www.chahuitong.com/wap/Public/upload/";

    /** 首页产品图片根地址 */
    String IMAGE_PRODUCT_PATH_ROOT = "http://www.chahuitong.com/data/upload/shop/store/goods/2/";

}
