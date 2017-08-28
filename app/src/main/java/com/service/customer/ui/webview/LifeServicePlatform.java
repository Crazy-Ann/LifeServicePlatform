package com.service.customer.ui.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.EvaluateInfo;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.activity.LocationMapActivity;
import com.service.customer.ui.activity.TaskEvaluateActivity;
import com.service.customer.ui.activity.TaskMapActivity;
import com.service.customer.ui.activity.TaskProcessingActivity;
import com.service.customer.ui.activity.TaskSubmitActivity;
import com.service.customer.ui.activity.VolunteerEvaluateActivity;
import com.service.customer.ui.activity.WapActivity;
import com.service.customer.ui.contract.implement.ActivityViewImplement;

import java.util.List;

public class LifeServicePlatform {

    public static void call(final WebView webView, String data) {
        final ActivityViewImplement activityViewImplement = (ActivityViewImplement) webView.getContext();
        String tag = JSONObject.parseObject(data).getString(Constant.JavaScript.TAG);
        final String parameter = JSONObject.parseObject(data).getString(Constant.JavaScript.PARAMETER);
        LogUtil.getInstance().print("object:" + JSONObject.parseObject(data).toString());
        LogUtil.getInstance().print("tag:" + tag);
        LogUtil.getInstance().print("parameter:" + parameter);
        if (!TextUtils.isEmpty(tag)) {
            Bundle bundle;
            switch (tag) {
                case Constant.JavaScript.NEW_WAP_PAGE:
                    if (!TextUtils.isEmpty(parameter)) {
                        bundle = new Bundle();
                        bundle.putString(Temp.TITLE.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE));
                        bundle.putString(Temp.URL.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.URL));
                        activityViewImplement.startActivity(WapActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, bundle);
                    }
                    break;
                case Constant.JavaScript.MAP_QUERY:
                    activityViewImplement.startActivity(TaskMapActivity.class);
                    break;
                case Constant.JavaScript.LOCATION_MAP:
                    activityViewImplement.startActivity(LocationMapActivity.class);
                    break;
                case Constant.JavaScript.WORK_LOG:
                    bundle = new Bundle();
                    bundle.putString(Temp.TITLE.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE));
                    bundle.putString(Temp.URL.getContent(), ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getWorkUrl());
                    activityViewImplement.startActivity(WapActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, bundle);
                    break;
                case Constant.JavaScript.TASK_PROCESSING:
                    bundle = new Bundle();
                    bundle.putString(Temp.TITLE.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE));
                    bundle.putString(Temp.BILL_NO.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.BILL_NO));
                    activityViewImplement.startActivity(TaskProcessingActivity.class, bundle);
                    break;
                case Constant.JavaScript.TASK_SUBMIT:
                    if (!TextUtils.isEmpty(parameter)) {
                        bundle = new Bundle();
                        bundle.putString(Temp.TITLE.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.TITLE));
                        bundle.putInt(Temp.TASK_TYPE.getContent(), JSONObject.parseObject(parameter).getIntValue(Constant.JavaScript.TASK_TYPE));
                        bundle.putString(Temp.BILL_NO.getContent(), JSONObject.parseObject(parameter).getString(Constant.JavaScript.BILL_NO));
                        activityViewImplement.startActivity(TaskSubmitActivity.class, bundle);
                    }
                    break;
                case Constant.JavaScript.TASK_EVALUATION:
                    if (!TextUtils.isEmpty(parameter)) {
                        Intent intent = new Intent(activityViewImplement, TaskEvaluateActivity.class);
                        intent.putExtra(Temp.EVALUATE_INFO.getContent(), new EvaluateInfo().parse(JSONObject.parseObject(parameter)));
                        intent.setExtrasClassLoader(EvaluateInfo.class.getClassLoader());
                        activityViewImplement.startActivity(intent);
                    }
                    break;
                case Constant.JavaScript.HELPER_EVALUATION:
                    if (!TextUtils.isEmpty(parameter)) {
                        Intent intent = new Intent(activityViewImplement, VolunteerEvaluateActivity.class);
                        intent.putExtra(Temp.EVALUATE_INFO.getContent(), new EvaluateInfo().parse(JSONObject.parseObject(parameter)));
                        intent.setExtrasClassLoader(EvaluateInfo.class.getClassLoader());
                        activityViewImplement.startActivity(intent);
                    }
                    break;
                //電話
                case Constant.JavaScript.PHONE_CALL:
                    if (!TextUtils.isEmpty(parameter)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activityViewImplement.getBasePresenterImplement().checkPermission(activityViewImplement, new PermissionCallback() {
                                @Override
                                public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                    activityViewImplement.startActivity(Intent.ACTION_CALL, Uri.parse("tel:" + JSONObject.parseObject(parameter).getString(Constant.JavaScript.PHONE)));
                                }

                                @Override
                                public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                    activityViewImplement.showPermissionPromptDialog();
                                }
                            });
                        } else {
                            activityViewImplement.startActivity(Intent.ACTION_CALL, Uri.parse("tel:" + JSONObject.parseObject(parameter).getString(Constant.JavaScript.PHONE)));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
