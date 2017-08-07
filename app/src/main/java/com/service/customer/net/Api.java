package com.service.customer.net;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.base.constant.net.ResponseCode;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.http.request.HttpRequest;
import com.service.customer.components.http.request.RequestParameter;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.NetworkUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.net.reponse.ConfigResponse;
import com.service.customer.net.reponse.LoginResponse;
import com.service.customer.net.reponse.ModifyPasswordResponse;
import com.service.customer.net.reponse.ModifyRealNameResponse;
import com.service.customer.net.reponse.SaveHeadImageResponse;

import java.util.HashMap;

public class Api {

    private static Api api;

    private Api() {
        // cannot be instantiated
    }

    public static synchronized Api getInstance() {
        if (api == null) {
            api = new Api();
        }
        return api;
    }

    public void getConfig(final Context context, final BaseView view, final ApiListener apiListener) {
        LogUtil.getInstance().print("getVersion");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.VERSION, String.valueOf(BuildConfig.VERSION_CODE));
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.CONFIG, parameters, null, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, BuildConfig.SERVICE_URL, requestParameter, new ConfigResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("获取配置信息开始");
                        view.showLoadingPromptDialog(R.string.get_config, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("获取配置信息成功:" + configInfo.toString());
                        if (configInfo != null) {
                            apiListener.success(configInfo);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_get_version_error, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("获取配置信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("获取配置信息失败,code:" + code + ",message:" + message);
                        if (!TextUtils.isEmpty(code)) {
                            switch (code) {
                                case ResponseCode.ERROR_CODE_VERSION:
                                    if (configInfo != null) {
                                        apiListener.failed(configInfo, code, message);
                                    } else {
                                        view.showPromptDialog(R.string.dialog_prompt_get_version_error, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                                    }
                                    break;
                                default:
                                    view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                                    break;
                            }
                        } else {
                            view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("获取版本信息结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("获取版本信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void login(final Context context, final BaseView view, String url, String account, String password, final ApiListener apiListener) {
        LogUtil.getInstance().print("login");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.ACCOUNT, account);
            parameters.put(RequestParameterKey.PASSWORD, password);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.LOGIN, parameters, null, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new LoginResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("登录开始");
                        view.showLoadingPromptDialog(R.string.login_prompt, Constant.RequestCode.DIALOG_PROGRESS_LOGIN);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("登录成功:" + loginInfo.toString());
                        if (loginInfo != null) {
                            apiListener.success(loginInfo);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_login_error, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("登录失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("登录失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("登录结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("登录失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void modifyPassword(final Context context, final BaseView view, String url, String account, String password, String token, final ApiListener apiListener) {
        LogUtil.getInstance().print("modifyPassword");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.ACCOUNT, account);
            parameters.put(RequestParameterKey.PASSWORD, password);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.MODIFY_PASSWORD, parameters, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new ModifyPasswordResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("修改密码开始");
                        view.showLoadingPromptDialog(R.string.modify_password_prompt, Constant.RequestCode.DIALOG_PROGRESS_MODIFY_PASSWORD);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("修改密码成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("修改密码失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("修改密码失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_ERROR);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("修改密码结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("修改密码失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void modifyRealName(final Context context, final BaseView view, String url, String realName, String token, final ApiListener apiListener) {
        LogUtil.getInstance().print("modifyRealName");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.REAL_NAME, realName);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.MODIFY_NAME, parameters, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new ModifyRealNameResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("修改昵称开始");
                        view.showLoadingPromptDialog(R.string.modify_real_name_prompt, Constant.RequestCode.DIALOG_PROGRESS_MODIFY_REAL_NAME);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("修改昵称成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("修改昵称失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("修改昵称失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("修改昵称结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("修改昵称失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void saveHeadImage(final Context context, final BaseView view, String url, String token, FileWrapper fileWrapper, final ApiListener apiListener) {
        LogUtil.getInstance().print("saveHeadImage");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, FileWrapper> fileWrappers = new HashMap<>();
            fileWrappers.put(RequestParameterKey.UPLOAD_IMAGE, fileWrapper);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.SAVE_HEAD_IMAGE, null, fileWrappers, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new SaveHeadImageResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("修改头像开始");
                        view.showLoadingPromptDialog(R.string.save_head_image_prompt2, Constant.RequestCode.DIALOG_PROGRESS_SAVE_HEAD_IMAGE);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("修改头像成功:" + object.toString());
                        if (headImageInfo != null) {
                            apiListener.success(headImageInfo);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_save_head_image_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("修改头像失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("修改头像失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("修改头像结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("修改头像失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void saveTaskInfo(final Context context, final BaseView view, String url, String token, String taskType, double longitude, double latitude, String address, String taskNote, FileWrapper fileWrapper, final ApiListener apiListener) {
        LogUtil.getInstance().print("saveTaskInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.TASK_TYPE, taskType);
            parameters.put(RequestParameterKey.LONGITUDE, String.valueOf(longitude));
            parameters.put(RequestParameterKey.LATITUDE, String.valueOf(latitude));
            parameters.put(RequestParameterKey.ADDRESS, address);
            parameters.put(RequestParameterKey.TASK_NOTE, taskNote);
            HashMap<String, FileWrapper> fileWrappers = new HashMap<>();
            fileWrappers.put(RequestParameterKey.UPLOAD_IMAGE, fileWrapper);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.TASK_INFO, parameters, fileWrappers, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new SaveHeadImageResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交任务开始");
                        view.showLoadingPromptDialog(R.string.save_task_info_prompt, Constant.RequestCode.DIALOG_PROGRESS_SAVE_TASK_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交任务成功:" + object.toString());
                        if (headImageInfo != null) {
                            apiListener.success(headImageInfo);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_save_task_info_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交任务失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交任务失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交任务结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交任务失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }
}
