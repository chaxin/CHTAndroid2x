package com.damenghai.chahuitong2.api;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public interface ErrorEvent {
    /** 参数为空 */
    int PARAM_NULL = 0x501;

    /** 参数错误 */
    int PARAM_ILLEGAL = 0x502;

    /** 未登录 */
    int SHOULD_LOGIN = 0x503;

    /** 网络问题错误码 */
    int NETWORK_ERROR_CODE = 0x504;

    /** 获取数据、解析数据失败错误码 */
    int RESPONSE_ERROR_CODE = 0x505;

    /** 网络不可用错误信息 */
    String NETWORK_ERROR_MSG = "网络连接不可用";

    /** 获取数据、解析数据失败错误码 */
    String RESPONSE_ERROR_MSG = "数据获取失败";
}
