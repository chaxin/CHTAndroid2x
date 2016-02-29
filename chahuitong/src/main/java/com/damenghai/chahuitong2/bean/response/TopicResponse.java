package com.damenghai.chahuitong2.bean.response;

import com.damenghai.chahuitong2.bean.Leader;
import com.damenghai.chahuitong2.bean.Status;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/26.
 */
public class TopicResponse {
    private Leader memberInfo;
    private ArrayList<Status> content;

    public Leader getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Leader memberInfo) {
        this.memberInfo = memberInfo;
    }

    public ArrayList<Status> getContent() {
        return content;
    }

    public void setContent(ArrayList<Status> content) {
        this.content = content;
    }
}
