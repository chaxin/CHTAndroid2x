package com.damenghai.chahuitong.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/25.
 */
public class Comment implements Serializable {
    private int id;
    private int member_id;
    private int content_id;
    private int uid;
    private String comment;
    private String comment_time;
    private int reply_id;
    private String member_name;
    private String member_avatar;
    private int reply_number;

    private String source;
    private ArrayList<Comment> replys;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

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

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_avatar() {
        return member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public int getReply_number() {
        return reply_number;
    }

    public void setReply_number(int reply_number) {
        this.reply_number = reply_number;
    }

    public String getText() {
        return comment;
    }

    public void setText(String text) {
        this.comment = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return comment_time;
    }

    public void setTime(String time) {
        this.comment_time = time;
    }

    public ArrayList<Comment> getReplys() {
        return replys;
    }

    public void setReplys(ArrayList<Comment> replys) {
        this.replys = replys;
    }

}
