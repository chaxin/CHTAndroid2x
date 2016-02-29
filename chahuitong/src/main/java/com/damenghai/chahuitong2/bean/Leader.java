package com.damenghai.chahuitong2.bean;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/22.
 */
public class Leader implements Serializable {
    private int member_id;
    @Expose
    private String member_name;
    @Expose
    private String member_truename;
    @Expose
    private String member_avatar;
    @Expose
    private String member_sex;
    @Expose
    private String member_birthday;

    private String member_passwd;
    private String member_paypwd;
    private String member_email;
    private boolean member_email_bind;
    private String member_mobile;
    private boolean member_mobile_bind;
    private String member_qq;
    private String member_ww;
    private String member_login_num;
    private String member_time;
    private String member_login_time;
    private String member_old_login_time;
    private String member_qqopenid;
    private String member_qqinfo;
    private String member_sinaopenid;
    private String member_sinainfo;
    private String member_points;
    private String available_predeposit;
    private String freeze_predeposit;
    private String available_rc_balance;
    private String freeze_rc_balance;
    private String inform_allow;
    private String is_buy;
    private String is_allowtalk;
    private String member_state;
    private String member_snsvisitnum;
    private String member_areaid;
    private String member_cityid;
    private String member_provinceid;
    private String member_areainfo;
    private String member_privacy;
    private String member_quicklink;
    private String member_exppoints;
    private String inviter_id;
    private String chashi;
    private int guanzhu;
    private int follow;
    private int articles;
    private String rank;
    private int beInstered;


    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return TextUtils.isEmpty(member_name) ? "" : member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_truename() {
        return TextUtils.isEmpty(member_truename) ? "" : member_truename;
    }

    public void setMember_truename(String member_truename) {
        this.member_truename = member_truename;
    }

    public String getMember_avatar() {
        return TextUtils.isEmpty(member_avatar) ? "" : "http://www.chahuitong.com/data/upload/shop/avatar/" + member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_birthday() {
        return TextUtils.isEmpty(member_birthday) ? "" : member_birthday;
    }

    public void setMember_birthday(String member_birthday) {
        this.member_birthday = member_birthday;
    }

    public String getMember_passwd() {
        return member_passwd;
    }

    public void setMember_passwd(String member_passwd) {
        this.member_passwd = member_passwd;
    }

    public String getMember_paypwd() {
        return member_paypwd;
    }

    public void setMember_paypwd(String member_paypwd) {
        this.member_paypwd = member_paypwd;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public boolean isMember_email_bind() {
        return member_email_bind;
    }

    public void setMember_email_bind(boolean member_email_bind) {
        this.member_email_bind = member_email_bind;
    }

    public String getMember_mobile() {
        return member_mobile;
    }

    public void setMember_mobile(String member_mobile) {
        this.member_mobile = member_mobile;
    }

    public boolean isMember_mobile_bind() {
        return member_mobile_bind;
    }

    public void setMember_mobile_bind(boolean member_mobile_bind) {
        this.member_mobile_bind = member_mobile_bind;
    }

    public String getMember_qq() {
        return member_qq;
    }

    public void setMember_qq(String member_qq) {
        this.member_qq = member_qq;
    }

    public String getMember_ww() {
        return member_ww;
    }

    public void setMember_ww(String member_ww) {
        this.member_ww = member_ww;
    }

    public String getMember_login_num() {
        return member_login_num;
    }

    public void setMember_login_num(String member_login_num) {
        this.member_login_num = member_login_num;
    }

    public String getMember_time() {
        return member_time;
    }

    public void setMember_time(String member_time) {
        this.member_time = member_time;
    }

    public String getMember_login_time() {
        return member_login_time;
    }

    public void setMember_login_time(String member_login_time) {
        this.member_login_time = member_login_time;
    }

    public String getMember_old_login_time() {
        return member_old_login_time;
    }

    public void setMember_old_login_time(String member_old_login_time) {
        this.member_old_login_time = member_old_login_time;
    }

    public String getMember_qqopenid() {
        return member_qqopenid;
    }

    public void setMember_qqopenid(String member_qqopenid) {
        this.member_qqopenid = member_qqopenid;
    }

    public String getMember_qqinfo() {
        return member_qqinfo;
    }

    public void setMember_qqinfo(String member_qqinfo) {
        this.member_qqinfo = member_qqinfo;
    }

    public String getMember_sinaopenid() {
        return member_sinaopenid;
    }

    public void setMember_sinaopenid(String member_sinaopenid) {
        this.member_sinaopenid = member_sinaopenid;
    }

    public String getMember_sinainfo() {
        return member_sinainfo;
    }

    public void setMember_sinainfo(String member_sinainfo) {
        this.member_sinainfo = member_sinainfo;
    }

    public String getMember_points() {
        return member_points;
    }

    public void setMember_points(String member_points) {
        this.member_points = member_points;
    }

    public String getAvailable_predeposit() {
        return available_predeposit;
    }

    public void setAvailable_predeposit(String available_predeposit) {
        this.available_predeposit = available_predeposit;
    }

    public String getFreeze_predeposit() {
        return freeze_predeposit;
    }

    public void setFreeze_predeposit(String freeze_predeposit) {
        this.freeze_predeposit = freeze_predeposit;
    }

    public String getAvailable_rc_balance() {
        return available_rc_balance;
    }

    public void setAvailable_rc_balance(String available_rc_balance) {
        this.available_rc_balance = available_rc_balance;
    }

    public String getFreeze_rc_balance() {
        return freeze_rc_balance;
    }

    public void setFreeze_rc_balance(String freeze_rc_balance) {
        this.freeze_rc_balance = freeze_rc_balance;
    }

    public String getInform_allow() {
        return inform_allow;
    }

    public void setInform_allow(String inform_allow) {
        this.inform_allow = inform_allow;
    }

    public String getIs_buy() {
        return is_buy;
    }

    public void setIs_buy(String is_buy) {
        this.is_buy = is_buy;
    }

    public String getIs_allowtalk() {
        return is_allowtalk;
    }

    public void setIs_allowtalk(String is_allowtalk) {
        this.is_allowtalk = is_allowtalk;
    }

    public String getMember_state() {
        return member_state;
    }

    public void setMember_state(String member_state) {
        this.member_state = member_state;
    }

    public String getMember_snsvisitnum() {
        return member_snsvisitnum;
    }

    public void setMember_snsvisitnum(String member_snsvisitnum) {
        this.member_snsvisitnum = member_snsvisitnum;
    }

    public String getMember_areaid() {
        return member_areaid;
    }

    public void setMember_areaid(String member_areaid) {
        this.member_areaid = member_areaid;
    }

    public String getMember_cityid() {
        return member_cityid;
    }

    public void setMember_cityid(String member_cityid) {
        this.member_cityid = member_cityid;
    }

    public String getMember_provinceid() {
        return member_provinceid;
    }

    public void setMember_provinceid(String member_provinceid) {
        this.member_provinceid = member_provinceid;
    }

    public String getMember_areainfo() {
        return member_areainfo;
    }

    public void setMember_areainfo(String member_areainfo) {
        this.member_areainfo = member_areainfo;
    }

    public String getMember_privacy() {
        return member_privacy;
    }

    public void setMember_privacy(String member_privacy) {
        this.member_privacy = member_privacy;
    }

    public String getMember_quicklink() {
        return member_quicklink;
    }

    public void setMember_quicklink(String member_quicklink) {
        this.member_quicklink = member_quicklink;
    }

    public String getMember_exppoints() {
        return member_exppoints;
    }

    public void setMember_exppoints(String member_exppoints) {
        this.member_exppoints = member_exppoints;
    }

    public String getInviter_id() {
        return inviter_id;
    }

    public void setInviter_id(String inviter_id) {
        this.inviter_id = inviter_id;
    }

    public String getChashi() {
        return chashi;
    }

    public void setChashi(String chashi) {
        this.chashi = chashi;
    }

    public int getFans() {
        return guanzhu;
    }

    public void setFans(int guanzhu) {
        this.guanzhu = guanzhu;
    }

    public int getFollowing() {
        return follow;
    }

    public void setFollowing(int follow) {
        this.follow = follow;
    }

    public int getStatuses() {
        return articles;
    }

    public void setStatuses(int articles) {
        this.articles = articles;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    public int getBeInstered() {
        return beInstered;
    }

    public void setBeInstered(int beInstered) {
        this.beInstered = beInstered;
    }

}
