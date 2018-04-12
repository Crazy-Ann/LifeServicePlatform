package com.service.customer.ui.contract.implement;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.base.permission.Permission;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class BasePresenterImplement implements BasePresenter {

    private Context  context;
    private BaseView baseView;

    private AMapLocationClient       aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    private static ScheduledExecutorService scheduledExecutorService;
    private static ScheduledFuture<?>       scheduledFuture;

    public BasePresenterImplement(Context context, BaseView baseView) {
        this.context = context;
        this.baseView = baseView;
    }

    @Override
    public void initialize() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newScheduledThreadPool(2);
        }
        if (aMapLocationClient == null) {
            aMapLocationClient = new AMapLocationClient(context);
        }
        if (aMapLocationClientOption == null) {
            aMapLocationClientOption = new AMapLocationClientOption();
        }
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        aMapLocationClientOption.setHttpTimeOut(Constant.Map.LOCATION_TIME_OUT);
        aMapLocationClientOption.setInterval(Constant.Map.LOCATION_INTERVAL);
        aMapLocationClientOption.setWifiScan(true);
        aMapLocationClientOption.setGpsFirst(false);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setOnceLocationLatest(true);
//        aMapLocationClientOption.setOnceLocation(false);
//        aMapLocationClientOption.setOnceLocationLatest(false);
        aMapLocationClientOption.setSensorEnable(false);
        aMapLocationClientOption.setLocationCacheEnable(false);
        aMapLocationClientOption.setMockEnable(false);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(baseView);
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

    @Override
    public void startLocation() {
        if (aMapLocationClient != null) {
            LogUtil.getInstance().print("startLocation!!!!!!!!!!!!");
            aMapLocationClient.startLocation();
        }
    }

    @Override
    public void stopLocation() {
        if (aMapLocationClient != null && aMapLocationClient.isStarted()) {
            LogUtil.getInstance().print("stopLocation!!!!!!!!!!!!");
            aMapLocationClient.stopLocation();
        }
    }

    @Override
    public void destroyLocation() {
        if (aMapLocationClient != null) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
            aMapLocationClientOption = null;
        }
    }

    @Override
    public void startTimedRefresh(long delay, long period) {
        LogUtil.getInstance().print("startTimedRefresh");
        if (scheduledExecutorService != null) {
            if (scheduledFuture == null) {
                scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        startLocation();
                    }
                }, delay, period, TimeUnit.MINUTES);
            }
        }
//        if (timer != null) {
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    startLocation();
//                }
//            }, delay, period);
//        }
    }

    @Override
    public void cancelTimedRefresh() {
        LogUtil.getInstance().print("cancelTimedRefresh");
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
        stopLocation();
    }

    @Override
    public void saveAddressInfo(String longitude, String latitude, String address) {
        Api.getInstance().saveAddressInfo(
                context,
                baseView,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.SAVE_TASK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_ADDRESS_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                longitude,
                latitude,
                address,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        LogUtil.getInstance().print(context.getString(R.string.dialog_prompt_save_address_info_success));
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
