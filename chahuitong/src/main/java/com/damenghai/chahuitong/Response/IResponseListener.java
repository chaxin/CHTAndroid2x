package com.damenghai.chahuitong.response;

/**
 * Created by Sgun on 15/9/19.
 */
public interface IResponseListener {
    void onSuccess(String response);
    void onError(String error);
    void onAllDone();
}
