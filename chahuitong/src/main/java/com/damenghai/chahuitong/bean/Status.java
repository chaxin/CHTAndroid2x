package com.damenghai.chahuitong.bean;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/24.
 */
public class Status {
    private int avatar;
    private String created_at;
    private String text;
    private String source;
    private String thumbnail_pic;
    private User user;

    private ArrayList<ImageUrls> imageUrls;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ImageUrls> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<ImageUrls> imageUrls) {
        this.imageUrls = imageUrls;
    }


}
