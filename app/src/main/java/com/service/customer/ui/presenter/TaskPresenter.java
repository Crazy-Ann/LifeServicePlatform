package com.service.customer.ui.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.TaskContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.util.List;


public class TaskPresenter extends BasePresenterImplement implements TaskContract.Presenter {

    private TaskContract.View view;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    public AMapLocationClient getAMapLocationClient() {
        return aMapLocationClient;
    }

    public AMapLocationClientOption getAMapLocationClientOption() {
        return aMapLocationClientOption;
    }

    public TaskPresenter(Context context, TaskContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        aMapLocationClient = new AMapLocationClient(BaseApplication.getInstance());
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setGpsFirst(true);
        aMapLocationClientOption.setHttpTimeOut(Constant.Map.LOCATION_TIME_OUT);
        aMapLocationClientOption.setInterval(Constant.Map.LOCATION_INTERVAL);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setOnceLocationLatest(false);
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS);
        aMapLocationClientOption.setSensorEnable(false);
        aMapLocationClientOption.setWifiScan(true);
        aMapLocationClientOption.setLocationCacheEnable(false);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
    }

    @Override
    public void location() {
        view.showLoadingPromptDialog(R.string.location_prompt, Constant.RequestCode.DIALOG_PROGRESS_LOCATION);
        aMapLocationClient.startLocation();
    }

    @Override
    public void saveTaskInfo(String longitude, String latitude, String address, String taskNote, List<FileWrapper> fileWrappers) {
        LogUtil.getInstance().print("saveTaskInfo:");
        Api.getInstance().saveTaskInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl(),
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_HEAD_IMAGE,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                1,
                longitude,
                latitude,
                address,
                taskNote,
                fileWrappers,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {

                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
