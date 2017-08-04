package com.service.customer.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.utils.ApplicationUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.NetworkUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.Api;
import com.service.customer.net.entity.ConfigInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.WelcomeContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.File;
import java.io.IOException;


public class WelcomePresenter extends BasePresenterImplement implements WelcomeContract.Presenter {

    private WelcomeContract.View view;
    private boolean isForceUpdate;
    private String filePath;
    private String serviceUrl;

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public WelcomePresenter(Context context, WelcomeContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void getConfig() {
        Api.getInstance().getConfig(context, view, new ApiListener() {

            @Override
            public void success(BaseEntity entity) {
                checkVersion((ConfigInfo) entity);
            }

            @Override
            public void failed(BaseEntity entity, String errorCode, String errorMsg) {
                if (entity != null) {
                    checkVersion((ConfigInfo) entity);
                }
            }
        });
    }

    @Override
    public void checkVersion(ConfigInfo configInfo) {
        if (configInfo != null) {
            BaseApplication.getInstance().setConfigInfo(configInfo);
            serviceUrl = configInfo.getServerUrl();
            if (ApplicationUtil.getInstance().getVersionCode(context) < configInfo.getClientVersion()) {
                if (ApplicationUtil.getInstance().getVersionCode(context) < configInfo.getLowestClientVersion()) {
                    isForceUpdate = true;
                } else {
                    isForceUpdate = false;
                }
                view.showVersionUpdatePromptDialog(configInfo.getUpdateMessage());
            } else {
                view.startLoginActivity();
            }
        } else {
            view.showPromptDialog(R.string.dialog_prompt_check_version_error, Constant.RequestCode.DIALOG_PROMPT_CHECK_VERSION_ERROR);
        }
    }

    @Override
    public void download() {
        LogUtil.getInstance().print("download");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            ConfigInfo configInfo = (ConfigInfo) BaseApplication.getInstance().getConfigInfo();
            try {
                File derectory = IOUtil.getInstance().getExternalStoragePublicDirectory(context, Constant.FILE_NAME, null);
                if (configInfo != null) {
                    String url = configInfo.getDownloadUrl();
                    if (!TextUtils.isEmpty(url)) {
                        LogUtil.getInstance().print(derectory);
                        view.showDownloadPromptDialog(url, derectory);
                    }
                } else {
                    view.showPromptDialog(R.string.dialog_prompt_download_error, Constant.RequestCode.DIALOG_PROMPT_DOWNLOAD_ERROR);
                }
            } catch (IOException e) {
                e.printStackTrace();
                view.showPromptDialog(e.getMessage(), Constant.RequestCode.DIALOG_PROMPT_DOWNLOAD_ERROR);
            }
        } else {
            view.showNetWorkPromptDialog();
        }
    }
}
