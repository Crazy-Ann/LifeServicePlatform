package com.service.customer.base.view;

import android.support.annotation.NonNull;

import com.service.customer.base.dialog.listener.OnDialogNegativeListener;
import com.service.customer.base.dialog.listener.OnDialogNeutralListener;
import com.service.customer.base.dialog.listener.OnDialogPositiveListener;
import com.service.customer.components.permission.listener.PermissionCallback;

public interface BaseView<T> extends PermissionCallback, OnDialogPositiveListener, OnDialogNegativeListener, OnDialogNeutralListener {

    void setLoginPresenter(@NonNull T loginPresenter);

    void showNetWorkPromptDialog();

    void showPermissionPromptDialog();

    void showLoadingPromptDialog(int resoutId, int requestCode);

    void hideLoadingPromptDialog();

    void showPromptDialog(int resoutId, int requestCode);

    void showPromptDialog(String prompt, int requestCode);

    void startPermissionSettingActivity();

    void refusePermissionSetting();
}
