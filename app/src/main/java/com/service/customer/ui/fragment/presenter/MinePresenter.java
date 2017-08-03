package com.service.customer.ui.fragment.presenter;

import android.content.Context;

import com.service.customer.components.utils.LogUtil;
import com.service.customer.ui.contract.MineContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.File;

public class MinePresenter extends BasePresenterImplement implements MineContract.Presenter {

    private MineContract.View view;

    public MinePresenter(Context context, MineContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void modifyHeadImage(File file) {
        LogUtil.getInstance().print("file:" + file.getAbsolutePath());
    }

    @Override
    public void logout() {
        
    }
}
