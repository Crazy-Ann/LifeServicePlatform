package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.ActivityUtil;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.SharedPreferenceUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.validation.EditTextValidator;
import com.service.customer.components.validation.Validation;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.validation.AccountValidation;
import com.service.customer.net.entity.validation.PasswordValidation;
import com.service.customer.ui.presenter.LoginPresenter;
import com.service.customer.ui.contract.LoginContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;

import java.util.List;


public class LoginActivity extends ActivityViewImplement<LoginContract.Presenter> implements LoginContract.View, View.OnClickListener {

    private LoginPresenter loginPresenter;
    private EditText etAccount;
    private ImageButton ibAccountEmpty;
    private EditText etPassword;
    private ImageButton ibPasswordDisplay;
    private ImageButton ibPasswordEmpty;
    private Button btnLogin;
    private CheckBox cbRememberLoginInfo;
    private boolean isPasswordHidden;
    private EditTextValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        etAccount = ViewUtil.getInstance().findView(this, R.id.etAccount);
        ibAccountEmpty = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibAccountEmpty, this);
        etPassword = ViewUtil.getInstance().findView(this, R.id.etPassword);
        ibPasswordDisplay = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibPasswordDisplay, this);
        ibPasswordEmpty = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibPasswordEmpty, this);
        cbRememberLoginInfo = ViewUtil.getInstance().findView(this, R.id.cbRememberLoginInfo);
        btnLogin = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnLogin, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        loginPresenter = new LoginPresenter(this, this);
        loginPresenter.initialize();

        setBasePresenterImplement(loginPresenter);
        getSavedInstanceState(savedInstanceState);

        validator = new EditTextValidator();
        validator.add(new Validation(null, etAccount, true, ibAccountEmpty, new AccountValidation()));
        validator.add(new Validation(null, etPassword, true, ibPasswordEmpty, new PasswordValidation()));
        validator.execute(this, btnLogin,
                          com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                          com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                          com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                          com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);

        LogUtil.getInstance().print("name:" + SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.ACCOUNT, null, true));
        LogUtil.getInstance().print("password:" + SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.PASSWORD, null, true));
        LogUtil.getInstance().print("url1:" + SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.INDEX_URL, null, true));
        LogUtil.getInstance().print("url2:" + SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.TASK_URL, null, true));
        etAccount.setText(SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.ACCOUNT, null, true));
        etPassword.setText(SharedPreferenceUtil.getInstance().getString(this, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.PASSWORD, null, true));
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ibAccountEmpty:
                etAccount.setText(null);
                break;
            case R.id.ibPasswordDisplay:
                if (isPasswordHidden) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_on, null, null, DiskCacheStrategy.NONE, ibPasswordDisplay);
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_off, null, null, DiskCacheStrategy.NONE, ibPasswordDisplay);
                }
                isPasswordHidden = !isPasswordHidden;
                break;
            case R.id.ibPasswordEmpty:
                etPassword.setText(null);
                break;
            case R.id.btnLogin:
                if (validator.validate(this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        loginPresenter.checkPermission(this);
                    } else {
                        loginPresenter.login(etAccount.getText().toString(), etPassword.getText().toString());
                    }
                }
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
                    loginPresenter.checkPermission(this);
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
            case Constant.RequestCode.DIALOG_PROMPT_QUIT:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_QUIT");
                BaseApplication.getInstance().releaseInstance();
                ActivityUtil.removeAll();
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
            case Constant.RequestCode.DIALOG_PROMPT_QUIT:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_QUIT");
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
        loginPresenter.login(etAccount.getText().toString(), etPassword.getText().toString());
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
    public boolean isRememberLoginInfo() {
        return cbRememberLoginInfo.isChecked();
    }

    @Override
    public void startMainActivity() {
        startActivity(MainActivity.class);
        onFinish("startMainActivity");
    }
}
