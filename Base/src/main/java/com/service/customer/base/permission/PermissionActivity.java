package com.service.customer.base.permission;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.service.customer.base.R;
import com.service.customer.base.activity.BaseActivity;
import com.service.customer.base.constant.Constant;
import com.service.customer.base.constant.Temp;
import com.service.customer.components.permission.listener.PermissionListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public final class PermissionActivity extends BaseActivity {

    public static PermissionListener permissionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        String[] permissions = getIntent().getStringArrayExtra(Temp.PERMISSIONS.getContent());
        if (permissionListener == null || permissions == null) {
            onFinish("initialize");
        } else {
            requestPermissions(permissions, Constant.RequestCode.PERMISSION);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.RequestCode.PERMISSION:
                if (permissionListener != null) {
                    permissionListener.onRequestPermissionsResult(permissions, grantResults);
                }
                onFinish("onRequestPermissionsResult");
                break;
            default:
                break;
        }
    }
}
