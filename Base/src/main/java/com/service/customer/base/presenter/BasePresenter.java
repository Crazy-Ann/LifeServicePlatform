package com.service.customer.base.presenter;

import android.content.Context;

import com.service.customer.components.permission.listener.PermissionCallback;

public interface BasePresenter {

    void initialize();
    
    void checkPermission(Context context, PermissionCallback permissionCallback, String... permissions);
}
