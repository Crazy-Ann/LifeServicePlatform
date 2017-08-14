package com.service.customer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service.customer.R;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.ui.contract.implement.FragmentViewImplement;

import java.util.List;

public class TaskManagementFragment extends FragmentViewImplement {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task_management, container, false);
        findViewById();
        initialize(savedInstanceState);
        setListener();
        return rootView;
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(rootView, R.id.inToolbar);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_383857, android.R.color.white, false, getString(R.string.task_management), null);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
                case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        taskManagementPresenter.checkPermission(BaseApplication.getInstance());
                    }
                    break;
                default:
                    break;
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

    }
}
