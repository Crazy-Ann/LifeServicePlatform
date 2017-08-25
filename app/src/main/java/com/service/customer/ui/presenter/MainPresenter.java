package com.service.customer.ui.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.service.customer.ui.contract.MainContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class MainPresenter extends BasePresenterImplement implements MainContract.Presenter {

    private Context context;
    private MainContract.View view;
    private BroadcastReceiver broadcastReceiver;

    public MainPresenter(Context context, MainContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

}
