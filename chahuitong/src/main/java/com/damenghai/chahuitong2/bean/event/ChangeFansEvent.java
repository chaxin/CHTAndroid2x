package com.damenghai.chahuitong2.bean.event;

/**
 * Created by Sgun on 15/10/13.
 */
public class ChangeFansEvent {
    int leaderId;
    int count;

    public ChangeFansEvent(int leaderId, int count) {
        this.leaderId = leaderId;
        this.count = count;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
