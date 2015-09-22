package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/4.
 */
public class User implements Serializable {
    String predepoit;
    String point;
    String user_name;
    String avator;

    public String getPredepoit() {
        return predepoit;
    }

    public void setPredepoit(String predepoit) {
        this.predepoit = predepoit;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String user_name) {
        this.user_name = user_name;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }
}
