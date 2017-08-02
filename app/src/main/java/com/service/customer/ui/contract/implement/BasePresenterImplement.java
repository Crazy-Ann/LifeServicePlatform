package com.service.customer.ui.contract.implement;

import android.content.Context;

import com.service.customer.base.permission.Permission;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;

import java.util.Arrays;

public abstract class BasePresenterImplement implements BasePresenter {

    protected Context context;
    protected BaseView baseView;

    public BasePresenterImplement(Context context, BaseView baseView) {
        this.context = context;
        this.baseView = baseView;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void checkPermission(Context context, String... permissions) {
        LogUtil.getInstance().print("checkPermission");
        if (context != null) {
            if (permissions == null || permissions.length == 0) {
                permissions = Constant.PERMISSIONS;
            }
            if (!Permission.getInstance().hasPermission(context, permissions)) {
                Permission.getInstance().with(context)
                        .requestCode(com.service.customer.base.constant.Constant.RequestCode.PERMISSION)
                        .permission(permissions)
                        .callback(baseView)
                        .start();
            } else {
                baseView.onSuccess(com.service.customer.base.constant.Constant.RequestCode.PERMISSION, Arrays.asList(permissions));
            }
        }
    }
}
