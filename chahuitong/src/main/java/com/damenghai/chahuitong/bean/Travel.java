package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * 茶客聚聚的每一次行程
 *
 * Created by Sgun on 15/8/22.
 */
public class Travel implements Serializable {
    private int imageRes;
    private String title;
    private String time;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
