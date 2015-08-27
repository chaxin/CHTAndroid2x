package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/4.
 */
public class User implements Serializable {
    float predepoit;
    int point;
    String user_name;
    String avator;

    public float getPredepoit() {
        return predepoit;
    }

    public void setPredepoit(float predepoit) {
        this.predepoit = predepoit;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
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
