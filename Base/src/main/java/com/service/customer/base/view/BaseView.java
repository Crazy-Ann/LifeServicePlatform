package com.service.customer.base.view;

import com.service.customer.base.dialog.listener.OnDialogNegativeListener;
import com.service.customer.base.dialog.listener.OnDialogNeutralListener;
import com.service.customer.base.dialog.listener.OnDialogPositiveListener;
import com.service.customer.components.permission.listener.PermissionCallback;

public interface BaseView<T> extends PermissionCallback, OnDialogPositiveListener, OnDialogNegativeListener, OnDialogNeutralListener {

    void showNetWorkPromptDialog();

    void showPermissionPromptDialog();

    void showLoadingPromptDialog(int resoutId, int requestCode);

    void hideLoadingPromptDialog();

    void showPromptDialog(int resoutId, int requestCode);

    void showPromptDialog(String prompt, int requestCode);

    void startLoginActivity(boolean isClearLoginInfo);

    void startMainActivity(int tab);

    void startPermissionSettingActivity();

    void refusePermissionSetting();
}
