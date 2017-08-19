package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.ui.contract.WapContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class WapPresenter extends BasePresenterImplement implements WapContract.Presenter {

    private Context context;
    private WapContract.View view;

    public WapPresenter(Context context, WapContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
