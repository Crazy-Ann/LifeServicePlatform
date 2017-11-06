package com.service.customer.net;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
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
import com.service.customer.net.response.ConfigResponse;
import com.service.customer.net.response.DealTaskInfoResponse;
import com.service.customer.net.response.LoginResponse;
import com.service.customer.net.response.MemberListResponse;
import com.service.customer.net.response.ModifyPasswordResponse;
import com.service.customer.net.response.ModifyRealNameResponse;
import com.service.customer.net.response.SaveAddressInfoResponse;
import com.service.customer.net.response.SaveHeadImageResponse;
import com.service.customer.net.response.SaveTaskInfoResponse;
import com.service.customer.net.response.SaveWorkInfoResponse;
import com.service.customer.net.response.ScoreTaskInfoResponse;
import com.service.customer.net.response.TaskListResponse;

import java.util.HashMap;
import java.util.List;

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

    private void handleFailedResponse(BaseView view, int promptCode, JSONObject object) {
        switch (object.getString(ResponseParameterKey.CODE)) {
            case ResponseCode.TOKEN_ERROR:
                view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR);
                break;
            default:
                view.showPromptDialog(object.getString(ResponseParameterKey.MESSAGE), promptCode);
                break;
        }
    }

    public void getConfig(final Context context, final BaseView view, String url, int version, final ApiListener apiListener) {
        LogUtil.getInstance().print("getConfig");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.VERSION, String.valueOf(version));
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.CONFIG, parameters, null, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new ConfigResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("获取配置信息开始");
//                        view.showLoadingPromptDialog(R.string.get_config, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("获取配置信息成功:" + configInfo.toString());
                        if (configInfo != null) {
                            apiListener.success(configInfo);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_get_config_error, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("获取版本信息结束");
//                        view.hideLoadingPromptDialog();
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
            parameters.put(RequestParameterKey.DEVICE_ID, BaseApplication.getInstance().getClientId());
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR, object);
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
                        view.showLoadingPromptDialog(R.string.modify_password_prompt1, Constant.RequestCode.DIALOG_PROGRESS_MODIFY_PASSWORD);
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_ERROR, object);
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR, object);
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR, object);
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

    public void saveTaskInfo(final Context context, final BaseView view, String url, String token, String longitude, String latitude, String address, int taskType, String taskNote, List<FileWrapper> fileWrappers, final ApiListener apiListener) {
        LogUtil.getInstance().print("saveTaskInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.TASK_TYPE, String.valueOf(taskType));
            parameters.put(RequestParameterKey.LONGITUDE, longitude);
            parameters.put(RequestParameterKey.LATITUDE, latitude);
            parameters.put(RequestParameterKey.ADDRESS, address);
            parameters.put(RequestParameterKey.TASK_NOTE, taskNote);
            HashMap<String, FileWrapper> fileParameters = new HashMap<>();
            if (fileWrappers != null) {
                for (FileWrapper fileWrapper : fileWrappers) {
                    fileParameters.put(fileWrapper.getFileName(), fileWrapper);
                }
            }
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.TASK_INFO, parameters, fileParameters, token, false);
            if (requestParameter != null) {
                requestParameter.setMultipart(true);
                HttpRequest.getInstance().doPost(context, url, requestParameter, new SaveTaskInfoResponse() {

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
                        apiListener.success(null);
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
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_ERROR, object);
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

    public void saveWrokInfo(final Context context, final BaseView view, String url, String token, int workType, String workNote, List<FileWrapper> fileWrappers, final ApiListener apiListener) {
        LogUtil.getInstance().print("saveWrokInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.WORK_TYPE, String.valueOf(workType));
            parameters.put(RequestParameterKey.WORK_NOTE, workNote);
            HashMap<String, FileWrapper> fileParameters = new HashMap<>();
            if (fileWrappers != null) {
                for (FileWrapper fileWrapper : fileWrappers) {
                    fileParameters.put(fileWrapper.getFileName(), fileWrapper);
                }
            }
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.WROK_INFO, parameters, fileParameters, token, false);
            if (requestParameter != null) {
                requestParameter.setMultipart(true);
                HttpRequest.getInstance().doPost(context, url, requestParameter, new SaveWorkInfoResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交工作日志开始");
                        view.showLoadingPromptDialog(R.string.save_condolence_record_prompt, Constant.RequestCode.DIALOG_PROGRESS_SAVE_WORK_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交工作日志成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交工作日志失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交工作日志失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交工作日志结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交工作日志失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void scoreTaskInfo(final Context context, final BaseView view, String url, String token, String billNo, int score, String note, final ApiListener apiListener) {
        LogUtil.getInstance().print("scoreAssistInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.BILL_NO, billNo);
            parameters.put(RequestParameterKey.SCORE_NUM, String.valueOf(score));
            parameters.put(RequestParameterKey.NOTE, note);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.SCORE_INFO, parameters, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new ScoreTaskInfoResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交任务评价信息开始");
                        view.showLoadingPromptDialog(R.string.score_task_info_prompt, Constant.RequestCode.DIALOG_PROGRESS_SCORE_TASK_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交任务评价信息成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交任务评价信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SCORE_TASK_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交任务评价信息失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SCORE_TASK_INFO_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交任务评价信息结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交任务评价信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SCORE_TASK_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_TASK_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void dealTaskInfo(final Context context, final BaseView view, String url, String token, String billNo, int dealStatus, String dealNote, final ApiListener apiListener) {
        LogUtil.getInstance().print("dealTaskInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.BILL_NO, billNo);
            parameters.put(RequestParameterKey.DEAL_STATUS, String.valueOf(dealStatus));
            parameters.put(RequestParameterKey.DEAL_NOTE, dealNote);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.DEAL_TASK_INFO, parameters, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new DealTaskInfoResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交任务处理信息开始");
                        view.showLoadingPromptDialog(R.string.deal_task_info_prompt, Constant.RequestCode.DIALOG_PROGRESS_DEAL_TASK_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交任务处理信息成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交任务处理信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交任务处理信息失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交任务处理信息结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交任务处理信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void scoreAssistInfo(final Context context, final BaseView view, String url, String token, String billNo, int score, String note, final ApiListener apiListener) {
        LogUtil.getInstance().print("scoreAssistInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.BILL_NO, billNo);
            parameters.put(RequestParameterKey.SCORE_NUM, String.valueOf(score));
            parameters.put(RequestParameterKey.NOTE, note);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.SCORE_ASSIST, parameters, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new ScoreTaskInfoResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交志愿者评价信息开始");
                        view.showLoadingPromptDialog(R.string.score_volunteer_info_prompt, Constant.RequestCode.DIALOG_PROGRESS_SCORE_VOLUNTEER_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交志愿者评价信息成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交志愿者评价信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交志愿者评价信息失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交志愿者评价信息结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交志愿者评价信息失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void saveAddressInfo(final Context context, final BaseView view, String url, String token, String longitude, String latitude, String address, final ApiListener apiListener) {
        LogUtil.getInstance().print("saveAddressInfo");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.LONGITUDE, longitude);
            parameters.put(RequestParameterKey.LATITUDE, latitude);
            parameters.put(RequestParameterKey.ADDRESS, address);
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(null, parameters, null, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new SaveAddressInfoResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("提交定位开始");
                        LogUtil.getInstance().print(context.getString(R.string.save_address_info_prompt));
//                        view.showLoadingPromptDialog(R.string.save_address_info_prompt, Constant.RequestCode.DIALOG_PROGRESS_SAVE_ADDRESS_INFO);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("提交定位成功:" + object.toString());
                        apiListener.success(null);
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("提交定位失败,code:" + code + ",message:" + message);
//                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_ADDRESS_INFO_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("提交定位失败,code:" + code + ",message:" + message);
//                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_SAVE_ADDRESS_INFO_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("提交定位结束");
//                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("提交定位失败,code:" + code + ",message:" + message);
//                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_SAVE_ADDRESS_INFO_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_SAVE_ADDRESS_INFO_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void taskList(final Context context, final BaseView view, String url, String token, int pageindex, final ApiListener apiListener) {
        LogUtil.getInstance().print("taskList");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.PAGE_INDEX, String.valueOf(pageindex));
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.TASK_LIST, parameters, null, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new TaskListResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("获取任务列表开始");
                        view.showLoadingPromptDialog(R.string.task_list_prompt, Constant.RequestCode.DIALOG_PROGRESS_TASK_LIST);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("获取任务列表成功:" + object.toString());
                        if (taskInfos != null) {
                            apiListener.success(taskInfos);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_task_list_error, Constant.RequestCode.DIALOG_PROMPT_TASK_LIST_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("获取任务列表失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_TASK_LIST_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("获取任务列表失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_TASK_LIST_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("获取任务列表结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("获取任务列表失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_TASK_LIST_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_TASK_LIST_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }

    public void memberList(final Context context, final BaseView view, String url, String token, int pageindex, final ApiListener apiListener) {
        LogUtil.getInstance().print("memberList");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(RequestParameterKey.PAGE_INDEX, String.valueOf(pageindex));
            RequestParameter requestParameter = Request.getInstance().generateRequestParameters(RequestParameterKey.MEMBER_LIST, parameters, null, token, false);
            if (requestParameter != null) {
                HttpRequest.getInstance().doPost(context, url, requestParameter, new MemberListResponse() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.getInstance().print("获取目标人群列表开始");
                        view.showLoadingPromptDialog(R.string.member_list_prompt, Constant.RequestCode.DIALOG_PROGRESS_MEMBER_LIST);
                    }

                    @Override
                    public void onResponseSuccess(JSONObject object) {
                        super.onResponseSuccess(object);
                        LogUtil.getInstance().print("获取目标人群列表成功:" + object.toString());
                        if (memberInfos != null) {
                            apiListener.success(memberInfos);
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_member_list_error, Constant.RequestCode.DIALOG_PROMPT_TASK_MEMBER_ERROR);
                        }
                    }

                    @Override
                    public void onResponseFailed(String code, String message) {
                        super.onResponseFailed(code, message);
                        LogUtil.getInstance().print("获取目标人群列表失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_TASK_MEMBER_ERROR);
                        apiListener.failed(null, code, message);
                    }

                    @Override
                    public void onResponseFailed(String code, String message, JSONObject object) {
                        super.onResponseFailed(code, message, object);
                        LogUtil.getInstance().print("获取目标人群列表失败,code:" + code + ",message:" + message);
                        handleFailedResponse(view, Constant.RequestCode.DIALOG_PROMPT_TASK_MEMBER_ERROR, object);
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        LogUtil.getInstance().print("获取目标人群列表结束");
                        view.hideLoadingPromptDialog();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        super.onFailed(code, message);
                        LogUtil.getInstance().print("获取目标人群列表失败,code:" + code + ",message:" + message);
                        view.showPromptDialog(message, Constant.RequestCode.DIALOG_PROMPT_TASK_MEMBER_ERROR);
                    }
                });
            } else {
                view.showPromptDialog(R.string.request_data_error, Constant.RequestCode.DIALOG_PROMPT_TASK_MEMBER_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }
}
