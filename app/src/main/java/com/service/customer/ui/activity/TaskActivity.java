package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.service.customer.R;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.tts.OnDictationListener;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.ui.contract.TaskContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.TaskPresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;

import java.util.List;

public class TaskActivity extends ActivityViewImplement<TaskContract.Presenter> implements TaskContract.View, View.OnClickListener, OnDictationListener, OnLeftIconEventListener, AMapLocationListener {

    private TaskPresenter taskPresenter;
    private TextView tvLocation;
    private VoiceEdittext vetContent;
    private RecyclerView rvDescreption;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        tvLocation = ViewUtil.getInstance().findView(this, R.id.tvLocation);
        vetContent = ViewUtil.getInstance().findView(this, R.id.vetContent);
        btnSubmit = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnSubmit, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_1f90f0, true, R.mipmap.icon_back1, this, android.R.color.white, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()));
        TTSUtil.getInstance(this).initializeSpeechRecognizer();

        vetContent.setTextCount(0);
        taskPresenter = new TaskPresenter(this, this);
        taskPresenter.initialize();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            taskPresenter.checkPermission(this);
        } else {
            taskPresenter.location();
        }

        setBasePresenterImplement(taskPresenter);
        getSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance(this).setOnDictationListener(this);
        taskPresenter.getAMapLocationClient().setLocationListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnSubmit:
//                taskPresenter.submit(null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
            case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    taskPresenter.checkPermission(this);
                } else {
                    taskPresenter.location();
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
                taskPresenter.location();
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
        taskPresenter.location();
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        showPermissionPromptDialog();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showLocationPromptDialog(int resoutId, int requestCode) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(resoutId))
                .setPositiveButtonText(this, R.string.try_again)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(true)
                .setCancelableOnTouchOutside(true)
                .setRequestCode(requestCode)
                .show(this);
    }

    @Override
    public void OnLeftIconEvent() {
        onFinish("OnLeftIconEvent");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        hideLoadingPromptDialog();
        switch (aMapLocation.getErrorCode()) {
            case AMapLocation.LOCATION_SUCCESS:
                LogUtil.getInstance().print("经    度:" + aMapLocation.getLongitude());
                LogUtil.getInstance().print("纬    度:" + aMapLocation.getLatitude());
                LogUtil.getInstance().print("精    度:" + aMapLocation.getAccuracy());
                LogUtil.getInstance().print("地    址:" + aMapLocation.getAddress());
                tvLocation.setText(aMapLocation.getAddress());
                break;
            default:
                showLocationPromptDialog(R.string.dialog_prompt_location_error, Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
                break;
        }
    }
}
