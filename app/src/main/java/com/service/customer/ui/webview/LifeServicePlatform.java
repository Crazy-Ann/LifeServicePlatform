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
import com.service.customer.net.entity.TaskInfo;
import com.service.customer.ui.activity.EvaluateActivity;
import com.service.customer.ui.activity.TaskActivity;
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
                //Volunteer.html
                case Constant.JavaScript.POLICIES_REGULATIONS:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), WapActivity.class);
                        String url = JSONObject.parseObject(parameter).getString(Constant.JavaScript.URL);
                        if (!TextUtils.isEmpty(url)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.URL.getContent(), url + Regex.QUESTION_MARK.getRegext() + RequestParameterKey.TOKEN + Regex.EQUALS.getRegext() + ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken());
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.QUERY_ANALYSIS:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.INFORMATION_MANAGEMENT:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.EVENT_QUERY:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                case Constant.JavaScript.MAP_QUERY:
                    webView.getContext().startActivity(new Intent(webView.getContext(), WapActivity.class));
                    break;
                //Demander.html
                case Constant.JavaScript.EMERGENCY_CALL_FOR_HELP:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.APPLIANCE_MAINTENANCE:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.LIVING_FACILITIES_MAINTENANCE:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.OTHER_LIFE_EVENTS:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.PSYCHOLOGICAL_COUNSELING:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.DOCTOR_MEDICINE:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.OTHER:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), TaskActivity.class);
                        String title = JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE);
                        if (!TextUtils.isEmpty(title)) {
                            bundle = new Bundle();
                            bundle.putString(Temp.TITLE.getContent(), title);
                            bundle.putBoolean(Temp.NEED_LOCATION.getContent(), JSONObject.parseObject(parameter).getBooleanValue(Constant.JavaScript.LOCATION));
                            intent.putExtras(bundle);
                            webView.getContext().startActivity(intent, bundle);
                        } else {
                            webView.getContext().startActivity(intent);
                        }
                    }
                    break;
                case Constant.JavaScript.IMMEDIATE_EVALUATION:
                    if (!TextUtils.isEmpty(parameter)) {
                        intent = new Intent(webView.getContext(), EvaluateActivity.class);
                        //TODO 模拟数据
                        bundle = new Bundle();
                        bundle.putParcelable(Temp.TASK_INFO.getContent(), new TaskInfo().parse(JSONObject.parseObject("{\"list\":[{\"accountavatar\":\"\",\"address\":\"测试地址\",\"billno\":\"N170806000004611\",\"latitude\":101,\"longitude\":101,\"realname\":\"志愿者A号\",\"status\":0,\"tasknote\":\"notes\"}],\"pagecount\":1,\"pageindex\":1,\"result\":true,\"totalrecord\":1}")));
                        intent.putExtras(bundle);
                        webView.getContext().startActivity(intent, bundle);
                        webView.getContext().startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
