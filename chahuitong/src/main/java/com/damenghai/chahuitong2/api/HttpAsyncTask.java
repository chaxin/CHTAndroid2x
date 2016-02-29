package com.damenghai.chahuitong2.api;

import android.os.AsyncTask;

import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.L;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.IOException;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public abstract class HttpAsyncTask<T> implements Api {

    private HttpRequest.HttpMethod mMethod;

    private String mUrl;

    private RequestParams mParams;

    private ResponseCallBackListener<T> mListener;

    public HttpAsyncTask(HttpRequest.HttpMethod method, String url, ResponseCallBackListener<T> listener) {
        mMethod = method;
        mUrl = url;
        mListener = listener;
    }

    public HttpAsyncTask(HttpRequest.HttpMethod method, String url, RequestParams params, ResponseCallBackListener<T> listener) {
        mMethod = method;
        mUrl = url;
        mParams = params;
        mListener = listener;
    }

    public void execute() {
        new AsyncTask<Void, Void, ResponseContent<T>>() {

            @Override
            protected ResponseContent<T> doInBackground(Void... voids) {
                try {
                    String result = mHttpUtils.sendSync(mMethod, mUrl, mParams).readString();
                    return parseNetworkResponse(result);
                } catch (IOException | HttpException e) {
                    L.d(e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(ResponseContent<T> t) {
                if (mListener != null && t != null) {

                    if (t.getCode() == 200) {
                        mListener.onSuccess(t.getContent());
                    } else {
                        mListener.onError(ErrorEvent.RESPONSE_ERROR_CODE, t.getMsg());
                    }

                }
            }

        }.execute();
    }

    public abstract ResponseContent<T> parseNetworkResponse(String result);

}
