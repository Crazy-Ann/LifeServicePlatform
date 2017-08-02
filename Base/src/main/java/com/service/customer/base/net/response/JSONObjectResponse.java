package com.service.customer.base.net.response;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.http.response.HttpResponse;

public abstract class JSONObjectResponse extends HttpResponse<JSONObject> {

    private BaseEntity baseEntity;

    public JSONObjectResponse() {
        super();
        type = JSONObject.class;
        baseEntity = new BaseEntity();
    }

    @Override
    public void onSuccess(JSONObject object) {
        baseEntity.parse(object);
        if (Boolean.valueOf(baseEntity.getReturnResult())) {
            onResponseSuccess(object);
        } else {
            onResponseFailed(baseEntity.getReturnCode(), baseEntity.getReturnMessage(), object);
        }
    }

    public abstract void onParseData(JSONObject object);

    public abstract void onResponseSuccess(JSONObject object);

    public abstract void onResponseFailed(String code, String message);

    public abstract void onResponseFailed(String code, String message, JSONObject object);
}
