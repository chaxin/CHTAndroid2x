package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/22.
 */
public class Leader implements Serializable {
    private String member_avatar;
    private String member_name;
    private String rank;
    private boolean favorites;

    public String getAvatar() {
        return member_avatar;
    }

    public void setAvatar(String avatar) {
        this.member_avatar = avatar;
    }

    public String getName() {
        return member_name;
    }

    public void setName(String name) {
        this.member_name = name;
    }

    public String getTitle() {
        return rank;
    }

    public void setTitle(String title) {
        this.rank = title;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

}
