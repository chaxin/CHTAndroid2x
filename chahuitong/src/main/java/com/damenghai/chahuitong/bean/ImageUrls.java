package com.damenghai.chahuitong.bean;

/**
 * Created by Sgun on 15/8/24.
 */
public class ImageUrls {
    // 中等质量图片url前缀
    //private static final String BMIDDLE_URL = "";
    // 原质量图片url前缀
    //private static final String ORIGINAL_URL = "";

    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;
    private int resId;

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

}
