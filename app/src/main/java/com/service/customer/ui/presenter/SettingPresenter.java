package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;
import com.service.customer.ui.contract.SettingContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class SettingPresenter extends BasePresenterImplement implements SettingContract.Presenter {

    private SettingContract.View view;

    public SettingPresenter(Context context, SettingContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void logout() {
        view.startLoginActivity(true);
    }
}
