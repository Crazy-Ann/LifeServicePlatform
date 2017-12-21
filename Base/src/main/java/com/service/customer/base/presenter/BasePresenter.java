package com.service.customer.base.presenter;

import android.content.Context;

import com.service.customer.components.permission.listener.PermissionCallback;

public interface BasePresenter {

    void initialize();

    void checkPermission(Context context, PermissionCallback permissionCallback, String... permissions);

    void startLocation();

    void stopLocation();

    void startTimedRefresh(long delay, long period);

    void cancelTimedRefresh();

    void destroyLocation();
    
    void saveAddressInfo(String longitude, String latitude, String address);
}
