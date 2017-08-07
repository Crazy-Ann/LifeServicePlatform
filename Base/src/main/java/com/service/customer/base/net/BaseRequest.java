package com.service.customer.base.net;

import android.text.TextUtils;

import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.http.request.RequestParameter;
import com.service.customer.components.utils.DeviceUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.SecurityUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BaseRequest {

    protected BaseRequest() {
        // cannot be instantiated
    }

    protected HashMap<String, String> generateRequestParameters(String type, HashMap<String, String> bizContent, String token) {
        HashMap<String, String> parameters = new HashMap<>();
        if (!TextUtils.isEmpty(type)) {
            parameters.put(RequestParameterKey.TYPE, type);
        }
        if (bizContent != null) {
            parameters.putAll(bizContent);
        }
        if (!TextUtils.isEmpty(token)) {
            parameters.put(RequestParameterKey.TOKEN, String.valueOf(token));
        }
        parameters.put(RequestParameterKey.CLIENT_INFO, DeviceUtil.getInstance().getDeviceInfo(BaseApplication.getInstance(), false, BuildConfig.VERSION_CODE));
        parameters.put(RequestParameterKey.TIMESTAMP, new SimpleDateFormat(Regex.DATE.getRegext(), Locale.getDefault()).format(new Date(System.currentTimeMillis())));
        parameters.put(RequestParameterKey.CODE, SecurityUtil.getInstance().encryptMD5("type=" + type
                                                                                               + "&token=" + token
                                                                                               + "&encryptData=" + bizContent
                                                                                               + "&key=" + BuildConfig.DECRYPT_KEY));
        return parameters;
    }

    protected RequestParameter formatParameters(HashMap<String, String> parameters, HashMap<String, FileWrapper> fileWrappers, boolean isJson) {
        RequestParameter parameter = new RequestParameter();
        parameter.setJsonType(isJson);
//        String requestData = SecurityUtil.getInstance().encryptAES(jsonObject.toString(), BuildConfig.DECRYPT_KEY);
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                if (!TextUtils.isEmpty(value)) {
                    parameter.addFormDataParameter(key, value);
                    LogUtil.getInstance().print("parameters:" + key + " = " + value);
                }
            }
            if (fileWrappers != null) {
                for (String key : fileWrappers.keySet()) {
                    FileWrapper fileWrapper = fileWrappers.get(key);
                    if (fileWrapper != null && fileWrapper.getFile() != null) {
                        parameter.addFormDataParameter(key, fileWrapper);
                        LogUtil.getInstance().print("fileWrappers:" + key + " = " + fileWrapper.getFileName());
                    }
                }
            }
            return parameter;
        } else {
            return null;
        }
    }
}
