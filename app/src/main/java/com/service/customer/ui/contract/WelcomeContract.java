package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.net.entity.ConfigInfo;
import com.service.customer.ui.dialog.listener.OnDialogInstallListner;

import java.io.File;

public interface WelcomeContract {

    interface View extends BaseView<Presenter>, OnDialogInstallListner {

        boolean isActive();

        void showVersionUpdatePromptDialog(String prompt);

        void showDownloadPromptDialog(String url, File derectory);

        void startLoginActivity();
    }

    interface Presenter extends BasePresenter {

        void getConfig();

        void checkVersion(ConfigInfo configInfo);

        void download();
    }
}
