package com.service.customer.ui.activity.presenter;

import android.content.Context;

import com.service.customer.ui.contract.ServiceSubmitContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;


public class ServiceSubmitPresenter extends BasePresenterImplement implements ServiceSubmitContract.Presenter {

    private ServiceSubmitContract.View view;

    public ServiceSubmitPresenter(Context context, ServiceSubmitContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void submit(String data) {

    }
}
