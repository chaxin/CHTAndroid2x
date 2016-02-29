package com.damenghai.chahuitong.bean;

import com.damenghai.chahuitong.config.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/25.
 */
public class Comment implements Serializable {
    private int uid;
    private int id;
    private String reply_to;
    private int content_id;
    private String member_avatar;
    private String member_name;
    private String comment;
    private String comment_time;
    private int member_id;
    private int reply_id;
    private Leader memberInfo;
    private ArrayList<Comment> reply;

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


    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_avatar() {
        return Constants.AVATAR_ROOT + member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public Leader getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Leader memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getReply_to() {
        return reply_to;
    }

    public void setReply_to(String reply_to) {
        this.reply_to = reply_to;
    }

    public ArrayList<Comment> getReply() {
        return reply;
    }

    public void setReply(ArrayList<Comment> reply) {
        this.reply = reply;
    }
}
