package com.damenghai.chahuitong2.response;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public interface ResponseCallBackListener<T> {
    /**
     * 成功获取数据时调用
     *
     * @param t 返回的数据，已封装成实体类
     */
    void onSuccess(T t);

    /**
     * 出错时调用
     *
     * @param errorEvent 错误码
     * @param message   错误信息
     */
    void onError(int errorEvent, String message);
}
