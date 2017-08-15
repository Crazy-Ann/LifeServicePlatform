package com.service.customer.ui.webview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.activity.WapActivity;

public class LifeServicePlatform {

    public static void call(final WebView webView, String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String tag = jsonObject.getString(Constant.JavaScript.TAG);
        String parameter = jsonObject.getString(Constant.JavaScript.PARAMETER);
        LogUtil.getInstance().print("object:" + jsonObject.toString());
        LogUtil.getInstance().print("tag:" + tag);
        LogUtil.getInstance().print("parameter:" + parameter);
        if (!TextUtils.isEmpty(tag)) {
            Bundle bundle;
            Intent intent;
            switch (tag) {
                case Constant.JavaScript.A:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), WapActivity.class);
                        bundle = new Bundle();
                        StringBuilder stringBuilder = new StringBuilder();
                        String url = JSONObject.parseObject(parameter).getString(Constant.JavaScript.URL);
                        if (!TextUtils.isEmpty(url)) {
                            stringBuilder = new StringBuilder(url);
                            stringBuilder.append(Regex.QUESTION_MARK.getRegext()).append(RequestParameterKey.TOKEN).append(Regex.EQUALS.getRegext()).append(((LoginInfo)BaseApplication.getInstance().getLoginInfo()).getToken());
                            bundle.putString(Temp.URL.getContent(), stringBuilder.toString());
                        }
                        intent.putExtras(bundle);
                        webView.getContext().startActivity(intent);
                    }
                    break;
                case Constant.JavaScript.B:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.C:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.D:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.E:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
