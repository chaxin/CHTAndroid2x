package com.damenghai.chahuitong2.bean.event;

/**
 * Created by Sgun on 15/10/12.
 */
public class AddCommentEvent {
    private int mStatusID;

    public AddCommentEvent(int statusID) {
        mStatusID = statusID;
    }

    public int getStatusID() {
        return mStatusID;
    }

}
