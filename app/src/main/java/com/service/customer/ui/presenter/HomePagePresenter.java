package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.ui.contract.HomePageContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class HomePagePresenter extends BasePresenterImplement implements HomePageContract.Presenter {

    private Context context;
    private HomePageContract.View view;

    public HomePagePresenter(Context context, HomePageContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
