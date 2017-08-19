package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.ui.contract.TaskManagementContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class TaskManagementPresenter extends BasePresenterImplement implements TaskManagementContract.Presenter {

    private Context context;
    private TaskManagementContract.View view;

    public TaskManagementPresenter(Context context, TaskManagementContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

}
