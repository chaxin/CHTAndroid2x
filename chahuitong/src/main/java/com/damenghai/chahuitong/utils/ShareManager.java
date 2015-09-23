package com.damenghai.chahuitong.utils;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * Created by Sgun on 15/9/22.
 */
public class ShareManager {
    public static UMSocialService create(Context context) {
        UMSocialService controller = UMServiceFactory.getUMSocialService("com.umeng.share");

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,"wx58ea4f88c26aa4b0","1999b86ded76a858588083ac46615b8d");
        wxHandler.addToSocialSDK();

        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,"wx58ea4f88c26aa4b0","1999b86ded76a858588083ac46615b8d");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 分享给qq好友，参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "1104563629", "rJbMttJCa47MBsCk");
        qqSsoHandler.addToSocialSDK();

        //分享到qq空间，参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "1104563629", "rJbMttJCa47MBsCk");
        qZoneSsoHandler.addToSocialSDK();

        //设置新浪SSO handler
        controller.getConfig().setSsoHandler(new SinaSsoHandler());

        return  controller;
    }
}
