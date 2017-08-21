package com.service.customer.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.ApplicationUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.NetworkUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.ConfigInfo;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.WelcomeContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.File;
import java.io.IOException;


public class WelcomePresenter extends BasePresenterImplement implements WelcomeContract.Presenter {

    private Context context;
    private WelcomeContract.View view;
    private boolean isForceUpdate;
    private String filePath;

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
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void getConfig() {
        Api.getInstance().getConfig(context, view, BuildConfig.SERVICE_URL + ServiceMethod.CONFIG, BuildConfig.VERSION_CODE, new ApiListener() {

            @Override
            public void success(BaseEntity entity) {
                ConfigInfo configInfo = (ConfigInfo) entity;
                if (configInfo != null) {
                    BaseApplication.getInstance().setConfigInfo(configInfo);
                    if (ApplicationUtil.getInstance().getVersionCode(context) < configInfo.getVersion()) {
                        if (ApplicationUtil.getInstance().getVersionCode(context) < configInfo.getLowestVersion()) {
                            isForceUpdate = true;
                        } else {
                            isForceUpdate = false;
                        }
                        view.showVersionUpdatePromptDialog(configInfo.getUpdateMessage());
                    } else {
                        LoginInfo loginInfo = (LoginInfo) IOUtil.getInstance().readObject(context.getCacheDir().getAbsolutePath() + Constant.Cache.LOGIN_INFO_CACHE_PATH + Regex.LEFT_SLASH.getRegext() + LoginInfo.class.getSimpleName());
                        if (loginInfo == null || TextUtils.isEmpty(loginInfo.getToken())) {
                            view.startLoginActivity(false);
                        } else {
                            BaseApplication.getInstance().setLoginInfo(loginInfo);
                            view.startMainActivity(Constant.Tab.HOME_PAGE);
                        }
                    }
                } else {
                    view.showPromptDialog(R.string.dialog_prompt_get_config_error, Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR);
                }
            }

            @Override
            public void failed(BaseEntity entity, String errorCode, String errorMsg) {

            }
        });
    }

    @Override
    public void download() {
        LogUtil.getInstance().print("download");
        if (NetworkUtil.getInstance().isInternetConnecting(context)) {
            ConfigInfo configInfo = (ConfigInfo) BaseApplication.getInstance().getConfigInfo();
            try {
                File derectory = IOUtil.getInstance().getExternalFilesDir(context, Constant.FILE_NAME, null);
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
