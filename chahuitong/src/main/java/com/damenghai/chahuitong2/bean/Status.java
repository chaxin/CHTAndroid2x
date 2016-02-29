package com.damenghai.chahuitong2.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/24.
 */
public class Status implements Serializable {
    private int content_id;
    private int uid;
    private int class_id;
    private String title;
    private String time;
    private int avatar;
    private String content;
    private int share;
    private int view;
    private int comment;
    private String source;
    private String thumbnail_pic;
    private Leader memberInfo;
    private String image;

    private ArrayList<ImageUrls> imageUrlses;

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return time;
    }

    public void setCreated_at(String created_at) {
        this.time = created_at;
    }

    public String getText() {
        return content;
    }

    public void setText(String text) {
        this.content = text;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
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

    public Leader getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Leader memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<ImageUrls> getImageUrls() {
        imageUrlses = new ArrayList<ImageUrls>();
        if (!image.equals("")) {
            String[] images = image.split(",");
            for (String image1 : images) {
                ImageUrls imageUrls = new ImageUrls();
                imageUrls.setBmiddle_pic("http://www.chahuitong.com/data/upload/qunzi/" + image1);
                imageUrlses.add(imageUrls);
            }
        }
        return imageUrlses;
    }

    public String getThumbImage() {
        if(TextUtils.isEmpty(image)) {
            if(!image.contains(",")) return "http://www.chahuitong.com/data/upload/qunzi/" + image;
            else {
                return "http://www.chahuitong.com/data/upload/qunzi/" + image.substring(0, image.indexOf(","));
            }
        }
        return image;
    }

}
