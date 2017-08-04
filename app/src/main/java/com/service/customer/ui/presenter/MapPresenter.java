package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.ui.contract.MapContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;


public class MapPresenter extends BasePresenterImplement implements MapContract.Presenter {

    private MapContract.View view;

    public MapPresenter(Context context, MapContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

}
