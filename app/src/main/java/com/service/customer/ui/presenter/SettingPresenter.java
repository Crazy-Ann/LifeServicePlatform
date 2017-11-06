package com.service.customer.ui.presenter;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.service.customer.ui.contract.SettingContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class SettingPresenter extends BasePresenterImplement implements SettingContract.Presenter {

    private Context context;
    private SettingContract.View view;

    public SettingPresenter(Context context, SettingContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
//        super.initialize();
    }

    @Override
    public void logout() {
        if (PushManager.getInstance().isPushTurnedOn(context)) {
            PushManager.getInstance().turnOffPush(context);
        }
        view.startLoginActivity(true);
    }
}
