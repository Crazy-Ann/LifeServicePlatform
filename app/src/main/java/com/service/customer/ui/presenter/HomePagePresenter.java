package com.service.customer.ui.presenter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.HomePageContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;
import com.service.customer.ui.presenter.listener.CustomPhoneStateListener;

import java.util.List;

public class HomePagePresenter extends BasePresenterImplement implements HomePageContract.Presenter {

    private Context context;
    private HomePageContract.View view;
    private BroadcastReceiver broadcastReceiver;

    public HomePagePresenter(Context context, HomePageContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void registerReceiver() {
        LogUtil.getInstance().print("registerReceiver");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Intent.ACTION_NEW_OUTGOING_CALL:
                        ((TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE)).listen(CustomPhoneStateListener.getInstance(), PhoneStateListener.LISTEN_CALL_STATE);
                        CustomPhoneStateListener.getInstance().setPhoneNumber(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
                        break;
                    default:
                        LogUtil.getInstance().print(intent.getAction());
                        break;
                }
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
    }

    @Override
    public void unregisterReceiver() {
        LogUtil.getInstance().print("unregisterReceiver");
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void saveTaskInfo(String longitude, String latitude, String address, int taskType, String taskNote, List<FileWrapper> fileWrappers) {
        Api.getInstance().saveTaskInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.SAVE_TASK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_TASK_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                longitude,
                latitude,
                address,
                taskType,
                taskNote,
                fileWrappers,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        LogUtil.getInstance().print(context.getString(R.string.dialog_prompt_save_task_info_success));
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
