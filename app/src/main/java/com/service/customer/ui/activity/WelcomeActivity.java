package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.service.customer.R;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.ApplicationUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.ui.contract.WelcomeContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.DownloadDialog;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.WelcomePresenter;

import java.io.File;
import java.util.List;

public class WelcomeActivity extends ActivityViewImplement<WelcomeContract.Presenter> implements WelcomeContract.View, View.OnClickListener {

    private WelcomePresenter welcomePresenter;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.getInstance().print("onWindowFocusChanged:" + hasFocus);
        if (hasFocus && ViewUtil.getInstance().getNavigationBarStatus(this) != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_welcome);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        welcomePresenter = new WelcomePresenter(this, this);
        welcomePresenter.initialize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            welcomePresenter.checkPermission(this,this);
        } else {
            welcomePresenter.getConfig();
        }
        setBasePresenterImplement(welcomePresenter);
        getSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
            case Constant.RequestCode.INSTALL_APK:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    welcomePresenter.checkPermission(this,this);
                } else {
                    welcomePresenter.getConfig();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.quit_prompt))
                .setPositiveButtonText(this, R.string.confirm)
                .setNegativeButtonText(this, R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_QUIT)
                .show(this);
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
            case Constant.RequestCode.DIALOG_PROMPT_GET_CONFIG_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_GET_CONFIG_ERROR");
                onFinish("onPositiveButtonClicked_DIALOG_PROMPT_GET_CONFIG_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_VERSION_UPDATE:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_VERSION_UPDATE");
                welcomePresenter.download();
                break;
            case Constant.RequestCode.DIALOG_PROMPT_DOWNLOAD_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_DOWNLOAD_ERROR");
                onFinish("onPositiveButtonClicked_DIALOG_PROMPT_DOWNLOAD_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_INSTALL:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_INSTALL");
//                if (!TextUtils.isEmpty(welcomePresenter.getFilePath())) {
//                    ApplicationUtil.getInstance().chmod(Regex.PERMISSION.getRegext(), welcomePresenter.getFilePath());
//                    startActivityForResult(Intent.ACTION_VIEW, Uri.parse(Regex.FILE_HEAD.getRegext() + welcomePresenter.getFilePath()), Regex.FILE_TYPE.getRegext(), Constant.RequestCode.INSTALL_APK);
//                } else {
//                    showPromptDialog(R.string.install_failed_prompt, Constant.RequestCode.DIALOG_PROMPT_INSTALL_FAILED);
//                }
                if (!TextUtils.isEmpty(welcomePresenter.getFilePath())) {
                    ApplicationUtil.getInstance().chmod(Regex.PERMISSION.getRegext(), welcomePresenter.getFilePath());
                    startActivityForResult(Intent.ACTION_VIEW,
                                           IOUtil.getInstance().getFileUri(this, true, welcomePresenter.getFilePath()),
                                           Regex.FILE_TYPE.getRegext(),
                                           Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Intent.FLAG_GRANT_READ_URI_PERMISSION : com.service.customer.components.constant.Constant.FileProvider.DEFAULT_FLAG,
                                           Constant.RequestCode.INSTALL_APK);
                } else {
                    showPromptDialog(R.string.install_failed_prompt, Constant.RequestCode.DIALOG_PROMPT_INSTALL_FAILED);
                }
                break;
            case Constant.RequestCode.DIALOG_PROMPT_INSTALL_FAILED:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_INSTALL_FAILED");
                if (welcomePresenter.isForceUpdate()) {
                    onFinish("onPositiveButtonClicked_DIALOG_PROMPT_INSTALL_FAILED");
                } else {
                    startLoginActivity(false);
                }
                break;
            case Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TOKEN_ERROR");
                startLoginActivity(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_NET_WORK");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                refusePermissionSetting();
                break;
            case Constant.RequestCode.DIALOG_PROMPT_VERSION_UPDATE:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_VERSION_UPDATE");
                if (welcomePresenter.isForceUpdate()) {
                    onFinish("onNegativeButtonClicked_DIALOG_PROMPT_VERSION_UPDATE");
                } else {
                    startLoginActivity(false);
                }
                break;
            case Constant.RequestCode.DIALOG_PROMPT_DOWNLOAD:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_DOWNLOAD");
                if (welcomePresenter.isForceUpdate()) {
                    onFinish("onNegativeButtonClicked_DIALOG_PROMPT_DOWNLOAD");
                } else {
                    startLoginActivity(false);
                }
                break;
            case Constant.RequestCode.DIALOG_PROMPT_INSTALL:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_INSTALL");
                if (welcomePresenter.isForceUpdate()) {
                    onFinish("onNegativeButtonClicked_DIALOG_PROMPT_INSTALL");
                } else {
                    startLoginActivity(false);
                }
                break;
            case Constant.RequestCode.DIALOG_PROMPT_INSTALL_FAILED:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_INSTALL_FAILED");
                if (welcomePresenter.isForceUpdate()) {
                    onFinish("onPositiveButtonClicked_DIALOG_PROMPT_INSTALL_FAILED");
                } else {
                    startLoginActivity(false);
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
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
        welcomePresenter.getConfig();
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
    public void showVersionUpdatePromptDialog(String prompt) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(prompt)
                .setPositiveButtonText(this, R.string.download)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(false)
                .setCancelableOnTouchOutside(false)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_VERSION_UPDATE)
                .showAllowingStateLoss(this);
    }

    @Override
    public void showDownloadPromptDialog(String url, File derectory) {
        DownloadDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.dialog_prompt_download))
                .setUrl(url)
                .setDerectory(derectory)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(false)
                .setCancelableOnTouchOutside(false)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_DOWNLOAD)
                .show(this);
    }

    @Override
    public void onDialogInstall(String path) {
        welcomePresenter.setFilePath(path);
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.install_prompt))
                .setPositiveButtonText(this, R.string.install)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(false)
                .setCancelableOnTouchOutside(false)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_INSTALL)
                .showAllowingStateLoss(this);
    }

    @Override
    public void startMainActivity(int tab) {
        Bundle bundle = new Bundle();
        bundle.putInt(Temp.TAB.getContent(), tab);
        startActivity(MainActivity.class, bundle);
        onFinish("startMainActivity");
    }
}
