package com.service.customer.ui.contract;

import com.amap.api.location.AMapLocationListener;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.ui.dialog.listener.OnDialogInstallListner;

import java.io.File;

public interface WelcomeContract {

    interface View extends BaseView<Presenter>, OnDialogInstallListner, AMapLocationListener {

        boolean isActive();

        void showVersionUpdatePromptDialog(String prompt);

        void showDownloadPromptDialog(String url, File directory);
    }

    interface Presenter extends BasePresenter {

        void getConfig();
        
        void download();

        void startLocation();

        void stopLocation();

        void destroyLocation();
    }
}
