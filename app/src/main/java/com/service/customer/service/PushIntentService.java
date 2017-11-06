package com.service.customer.service;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.utils.LogUtil;

import java.util.Arrays;

public class PushIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        LogUtil.getInstance().print("pid:" + pid);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtil.getInstance().print("client_id:" + clientid);
        BaseApplication.getInstance().setClientId(clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        LogUtil.getInstance().print("message_id:" + gtTransmitMessage.getMessageId());
        LogUtil.getInstance().print("payload_id:" + gtTransmitMessage.getPayloadId());
        LogUtil.getInstance().print("payload:" + Arrays.toString(gtTransmitMessage.getPayload()));
        LogUtil.getInstance().print("task_id:" + gtTransmitMessage.getTaskId());
        LogUtil.getInstance().print("app_id:" + gtTransmitMessage.getAppid());
        LogUtil.getInstance().print("client_id:" + gtTransmitMessage.getClientId());
        LogUtil.getInstance().print("package_name:" + gtTransmitMessage.getPkgName());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        LogUtil.getInstance().print("online:" + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        
    }
}
