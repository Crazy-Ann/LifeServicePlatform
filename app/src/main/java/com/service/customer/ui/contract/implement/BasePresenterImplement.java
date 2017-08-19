package com.service.customer.ui.contract.implement;

import android.content.Context;

import com.service.customer.base.permission.Permission;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;

import java.util.Arrays;

public abstract class BasePresenterImplement implements BasePresenter {


    public BasePresenterImplement() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void checkPermission(Context context, PermissionCallback permissionCallback, String... permissions) {
        LogUtil.getInstance().print("checkPermission");
        if (context != null && permissionCallback != null) {
            if (permissions == null || permissions.length == 0) {
                permissions = Constant.PERMISSIONS;
            }
            if (!Permission.getInstance().hasPermission(context, permissions)) {
                Permission.getInstance().with(context)
                        .requestCode(com.service.customer.base.constant.Constant.RequestCode.PERMISSION)
                        .permission(permissions)
                        .callback(permissionCallback)
                        .start();
            } else {
                permissionCallback.onSuccess(com.service.customer.base.constant.Constant.RequestCode.PERMISSION, Arrays.asList(permissions));
            }
        }
    }
}
