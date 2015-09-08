package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * 茶客聚聚的每一次行程
 *
 * Created by Sgun on 15/8/22.
 */
public class Travel implements Serializable {
    private int active_id;
    private String active_title;
    private String location;
    private String join_time;
    private int last_time;
    private String tplphone;
    private int free;
    private String content;
    private String pics;
    private String time;
    private int uid;
    private Leader memberInfo;

    public String getTitle() {
        return active_title;
    }

    public void setTitle(String title) {
        this.active_title = title;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getActive_id() {
        return active_id;
    }

    public void setActive_id(int active_id) {
        this.active_id = active_id;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    public int getDuration() {
        return last_time;
    }

    public void setDuration(int last_time) {
        this.last_time = last_time;
    }

    public String getPhone() {
        return tplphone;
    }

    public void setPhone(String tplphone) {
        this.tplphone = tplphone;
    }

    public int getFee() {
        return free;
    }

    public void setFee(int free) {
        this.free = free;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Leader getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Leader memberInfo) {
        this.memberInfo = memberInfo;
    }

    /**
     * 获取第一张图片
     * @return
     */
    public String getThumbImage() {
        if(pics.equals("")) return "";
        else if(!pics.contains(",")) return "http://www.chahuitong.com/data/upload/qunzi/" + pics;
        else return "http://www.chahuitong.com/data/upload/qunzi/" + pics.substring(0, pics.indexOf(","));
    }

}
