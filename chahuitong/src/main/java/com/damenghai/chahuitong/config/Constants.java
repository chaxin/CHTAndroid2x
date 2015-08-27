package com.damenghai.chahuitong.config;

public interface Constants {
    /**
     * 保存数据的文件夹名称
     */
    String SHARED_PREFERERENCE_NAME = "chahuitong_prefs";

    /**
     * 服务器接口根路径
     */
    String API_URL = "http://www.chahuitong.com/mobile";


    /**
     * 茶市地址
     */
     String MARKET_URL = "/app/b2b/index.php/Home/";

    /**
     * 图片路径
     */
     String IMAGE_URL = "http://www.chahuitong.com/mobile/app/b2b/Public/upload/";

    /**
     * 接口根地址
     */
     String API_ROOT = "http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/";

    /**
     * 获取登录用户的信息
     */
     String URL_GET_USER_INFO = API_ROOT + "api";

    /**
     * 获取茶市所有产品
     */
     String API_MARKET_PRODUCT = API_ROOT + "homeapi";

    /**
     * 获取个人产品列表
     */
     String API_MARKET_MY_LIST = API_ROOT + "myListapi";

    /**
     * 发布产品
     */
     String URL_POST_SAVE = API_ROOT + "post_save";

    /**
     * 删除产品
     */
     String API_DELETE_PRODUCT = API_ROOT + "deleteapi";

    /**
     * 我的消息列表
     */
     String API_MY_NEWSLIST = API_ROOT + "newapi";

    /**
     * 消息对话的具体内容
     */
     String API_CHAT = API_ROOT + "newslistapi";

    /**
     * 发送消息
     */
     String API_SEND_MESSAGE = API_ROOT + "newssaveapi";

}
