package com.service.customer.net.response;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.base.net.response.JSONObjectResponse;
import com.service.customer.components.utils.LogUtil;

import okhttp3.Headers;
import okhttp3.Response;

public class DealTaskInfoResponse extends JSONObjectResponse {


    public DealTaskInfoResponse() {
        super();
    }

    @Override
    public void onParseData(JSONObject object) {
    }

    @Override
    public void onResponseSuccess(JSONObject object) {
        onParseData(object);
    }

    @Override
    public void onResponseFailed(String code, String message) {

    }

    @Override
    public void onResponseFailed(String code, String message, JSONObject object) {
        onParseData(object);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int progress, long speed, boolean isDone) {
        LogUtil.getInstance().print("DealTaskInfoResponse's progress:" + progress + ",speed:" + speed + ",isDone:" + isDone);
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onResponse(Response httpResponse, String response, Headers headers) {

    }

    @Override
    public void onResponse(String response, Headers headers) {

    }

    @Override
    public void onSuccess(Headers headers, JSONObject object) {

    }

    @Override
    public void onFailed(int code, String message) {

    }
}
