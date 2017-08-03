package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.TTSUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.widget.edittext.VoiceEdittext;
import com.service.customer.components.widget.edittext.listener.OnDictationListener;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.ui.activity.presenter.ServiceSubmitPresenter;
import com.service.customer.ui.contract.ServiceSubmitContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;

import java.util.List;

public class ServiceSubmitActivity extends ActivityViewImplement<ServiceSubmitContract.Presenter> implements ServiceSubmitContract.View, View.OnClickListener, OnDictationListener {

    private ServiceSubmitPresenter serviceSubmitPresenter;
    private VoiceEdittext vetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_submit);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        vetContent = ViewUtil.getInstance().findView(this, R.id.vetContent);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_1f90f0, R.color.color_ffffff, false, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()), null);
        TTSUtil.getInstance(this).initializeSpeechRecognizer();
        vetContent.setTextCount(0);
        serviceSubmitPresenter = new ServiceSubmitPresenter(this, this);
        serviceSubmitPresenter.initialize();

        setBasePresenterImplement(serviceSubmitPresenter);
        getSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance(this).setOnDictationListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
            case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    serviceSubmitPresenter.checkPermission(BaseApplication.getInstance());
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onDictation(String content) {
        LogUtil.getInstance().print("content:" + content);
        vetContent.setText(content);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTSUtil.getInstance(this).stopListening();
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_NET_WORK_ERROR");
                Intent intent;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setComponent(new ComponentName(Regex.ANDROID_SETTING.getRegext(), Regex.ANDROID_SETTING_MORE.getRegext()));
                    intent.setAction(Intent.ACTION_VIEW);
                }
                startActivityForResult(intent, Constant.RequestCode.NET_WORK_SETTING);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                startPermissionSettingActivity();
                break;
            default:
                break;
        }
    }


    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_NET_WORK_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                refusePermissionSetting();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
        serviceSubmitPresenter.submit(null);
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        showPermissionPromptDialog();
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
