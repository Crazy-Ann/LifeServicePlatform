package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.ui.contract.TaskContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.File;
import java.util.List;


public class TaskPresenter extends BasePresenterImplement implements TaskContract.Presenter {

    private TaskContract.View view;

    public TaskPresenter(Context context, TaskContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void submit(String data, List<File> file) {
        
    }
}
