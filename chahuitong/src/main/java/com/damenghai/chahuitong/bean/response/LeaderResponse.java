package com.damenghai.chahuitong.bean.response;

import com.damenghai.chahuitong.bean.Leader;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/26.
 */
public class LeaderResponse {
    private ArrayList<Leader> content;
    private int code;

    public ArrayList<Leader> getContent() {
        return content;
    }

    public void setContent(ArrayList<Leader> content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
