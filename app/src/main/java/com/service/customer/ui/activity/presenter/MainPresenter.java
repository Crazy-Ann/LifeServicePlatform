package com.service.customer.ui.activity.presenter;

import android.content.Context;

import com.service.customer.ui.contract.MainContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class MainPresenter extends BasePresenterImplement implements MainContract.Presenter {

    private MainContract.View view;

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
