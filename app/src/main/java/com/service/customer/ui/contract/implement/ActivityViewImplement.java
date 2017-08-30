package com.service.customer.ui.contract.implement;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.amap.api.location.AMapLocation;
import com.service.customer.R;
import com.service.customer.base.activity.BaseActivity;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.ActivityUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.ConfigInfo;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.activity.LoginActivity;
import com.service.customer.ui.activity.MainActivity;
import com.service.customer.ui.dialog.ProgressDialog;
import com.service.customer.ui.dialog.PromptDialog;

import java.util.List;

public abstract class ActivityViewImplement<T> extends BaseActivity implements BaseView<T> {

    private DialogFragment dialogFragment;
    private BasePresenterImplement basePresenterImplement;

    public BasePresenterImplement getBasePresenterImplement() {
        return basePresenterImplement;
    }

    public void setBasePresenterImplement(BasePresenterImplement basePresenterImplement) {
        this.basePresenterImplement = basePresenterImplement;
    }

    @Override
    public void showNetWorkPromptDialog() {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.net_work_error_prompt))
                .setPositiveButtonText(this, R.string.setting)
                .setNegativeButtonText(this, R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK)
                .show(this);
    }

    @Override
    public void showPermissionPromptDialog() {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.dialog_prompt_permission_error))
                .setPositiveButtonText(this, R.string.setting)
                .setNegativeButtonText(this, R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION)
                .setCancelableOnTouchOutside(false)
                .setCancelable(false)
                .showAllowingStateLoss(this);
    }

    @Override
    public void showLoadingPromptDialog(int resoutId, int requestCode) {
        if (dialogFragment != null) {
            ViewUtil.getInstance().hideDialog(dialogFragment);
        }
        dialogFragment = ProgressDialog.createBuilder(getSupportFragmentManager())
                .setPrompt(getString(resoutId))
                .setCancelableOnTouchOutside(false)
                .setCancelable(false)
                .setRequestCode(requestCode)
                .showAllowingStateLoss(getApplicationContext());
    }

    @Override
    public void hideLoadingPromptDialog() {
        ViewUtil.getInstance().hideDialog(dialogFragment);
    }

    @Override
    public void showLocationPromptDialog(String prompt, int requestCode) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(prompt)
                .setPositiveButtonText(this, R.string.try_again)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(true)
                .setCancelableOnTouchOutside(true)
                .setRequestCode(requestCode)
                .show(this);
    }

    @Override
    public void showPromptDialog(int resoutId, int requestCode) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(resoutId))
                .setPositiveButtonText(this, R.string.dialog_known)
                .setCancelable(true)
                .setCancelableOnTouchOutside(true)
                .setRequestCode(requestCode)
                .show(this);
    }

    @Override
    public void showPromptDialog(String prompt, int requestCode) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(prompt)
                .setPositiveButtonText(this, R.string.dialog_known)
                .setCancelable(true)
                .setCancelableOnTouchOutside(false)
                .setRequestCode(requestCode)
                .showAllowingStateLoss(this);
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " getSavedInstanceState() invoked!!");
        if (savedInstanceState != null) {
            BaseApplication.getInstance().setConfigInfo((ConfigInfo) savedInstanceState.getParcelable(Temp.CONFIG_INFO.getContent()));
            BaseApplication.getInstance().setLoginInfo((LoginInfo) savedInstanceState.getParcelable(Temp.LOGIN_INFO.getContent()));
        }
    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " setSavedInstanceState() invoked!!");
        savedInstanceState.putParcelable(Temp.CONFIG_INFO.getContent(), BaseApplication.getInstance().getConfigInfo());
        savedInstanceState.putParcelable(Temp.LOGIN_INFO.getContent(), BaseApplication.getInstance().getLoginInfo());
    }

    @Override
    public void startLoginActivity(boolean isClearLoginInfo) {
        if (isClearLoginInfo) {
            IOUtil.getInstance().deleteFile(BaseApplication.getInstance().getCacheDir().getAbsolutePath() + Constant.Cache.LOGIN_INFO_CACHE_PATH);
        }
        startActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        onFinish("startLoginActivity");
    }

    @Override
    public void startMainActivity(int tab) {
        Bundle bundle = new Bundle();
        bundle.putInt(Temp.TAB.getContent(), tab);
        startActivity(MainActivity.class, bundle);
        onFinish("startMainActivity");
    }

    @Override
    public void startPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(Regex.PACKAGE.getRegext() + getPackageName()));
        startActivityForResult(intent, Constant.RequestCode.PREMISSION_SETTING);
    }

    @Override
    public void refusePermissionSetting() {
        BaseApplication.getInstance().releaseInstance();
        ActivityUtil.removeAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.getInstance().getLoginInfo() != null) {
            switch (((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getMemberType()) {
                case Constant.AccountRole.WEI_JI_WEI:
                case Constant.AccountRole.JI_SHENG_BAN:
                case Constant.AccountRole.VOLUNTEER:
                    break;
                case Constant.AccountRole.HELP_SEEKER:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        basePresenterImplement.checkPermission(this, new PermissionCallback() {
                            @Override
                            public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                basePresenterImplement.startLocation();
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                showPermissionPromptDialog();
                            }
                        });
                    } else {
                        basePresenterImplement.startLocation();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        basePresenterImplement.stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basePresenterImplement.destroyLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        basePresenterImplement.stopLocation();
    }
}
