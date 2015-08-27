package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/18.
 */
public class Personal implements Serializable {
    private String text;
    private int image;
    private String url;

    public Personal(String text, int image, String url) {
        this.text = text;
        this.image = image;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
