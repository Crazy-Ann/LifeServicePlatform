package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.validation.EditTextValidator;
import com.service.customer.components.validation.Validation;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.entity.validation.NewPasswordValidation;
import com.service.customer.ui.contract.ModifyPasswordContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.ModifyPasswordPresenter;

import java.util.List;

public class ModifyPasswordActivity extends ActivityViewImplement<ModifyPasswordContract.Presenter> implements View.OnClickListener, OnLeftIconEventListener, ModifyPasswordContract.View {

    private ModifyPasswordPresenter modifyPasswordPresenter;
    private EditText etNewPassword;
    private ImageButton ibNewPasswordDisplay;
    private ImageButton ibNewPasswordEmpty;
    private EditText etNewPasswordAgain;
    private ImageButton ibNewPasswordAgainDisplay;
    private ImageButton ibNewPasswordAgainEmpty;
    private Button btnModifyPassword;
    private EditTextValidator editTextValidator;

    private boolean isNewPasswordHidden;
    private boolean isNewPasswordAgainHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        etNewPassword = ViewUtil.getInstance().findView(this, R.id.etNewPassword);
        ibNewPasswordDisplay = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibNewPasswordDisplay, this);
        ibNewPasswordEmpty = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibNewPasswordEmpty, this);
        etNewPasswordAgain = ViewUtil.getInstance().findView(this, R.id.etNewPasswordAgain);
        ibNewPasswordAgainDisplay = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibNewPasswordAgainDisplay, this);
        ibNewPasswordAgainEmpty = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ibNewPasswordAgainEmpty, this);
        btnModifyPassword = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnModifyPassword, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_015293, true, R.mipmap.icon_back1, this, android.R.color.white, getString(R.string.modify_password));

        modifyPasswordPresenter = new ModifyPasswordPresenter(this, this);
        modifyPasswordPresenter.initialize();

        setBasePresenterImplement(modifyPasswordPresenter);
        getSavedInstanceState(savedInstanceState);

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, etNewPassword, true, ibNewPasswordEmpty, new NewPasswordValidation()));
        editTextValidator.add(new Validation(null, etNewPasswordAgain, true, ibNewPasswordAgainEmpty, new NewPasswordValidation()));
        editTextValidator.execute(this, btnModifyPassword, R.drawable.frame_fillet_e4e4e4, R.drawable.frame_fillet_015293, android.R.color.white, android.R.color.white, null, null, false);
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
            case R.id.ibNewPasswordDisplay:
                if (isNewPasswordHidden) {
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_on, null, null, DiskCacheStrategy.NONE, ibNewPasswordDisplay);
                } else {
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_off, null, null, DiskCacheStrategy.NONE, ibNewPasswordDisplay);
                }
                isNewPasswordHidden = !isNewPasswordHidden;
                break;
            case R.id.ibNewPasswordEmpty:
                etNewPassword.setText(null);
                break;
            case R.id.ibNewPasswordAgainDisplay:
                if (isNewPasswordAgainHidden) {
                    etNewPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_on, null, null, DiskCacheStrategy.NONE, ibNewPasswordAgainDisplay);
                } else {
                    etNewPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    GlideUtil.getInstance().with(this, R.mipmap.icon_eye_off, null, null, DiskCacheStrategy.NONE, ibNewPasswordAgainDisplay);
                }
                isNewPasswordAgainHidden = !isNewPasswordAgainHidden;
                break;
            case R.id.ibNewPasswordAgainEmpty:
                etNewPasswordAgain.setText(null);
                break;
            case R.id.btnModifyPassword:
                if (editTextValidator.validate(this)) {
                    if (TextUtils.equals(etNewPassword.getText(), etNewPasswordAgain.getText())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            modifyPasswordPresenter.checkPermission(this, new PermissionCallback() {
                                @Override
                                public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                    modifyPasswordPresenter.modifyPassword(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getPhone(), etNewPassword.getText().toString());
                                }

                                @Override
                                public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                                }
                            });
                        } else {
                            modifyPasswordPresenter.modifyPassword(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getPhone(), etNewPassword.getText().toString());
                        }
                    } else {
                        ToastUtil.getInstance().showToast(this, R.string.modify_password_prompt2, Toast.LENGTH_SHORT);
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
                    modifyPasswordPresenter.checkPermission(this, new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                            modifyPasswordPresenter.modifyPassword(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getPhone(), etNewPassword.getText().toString());
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                } else {
                    modifyPasswordPresenter.modifyPassword(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getPhone(), etNewPassword.getText().toString());
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
            case Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TOKEN_ERROR");
                startLoginActivity(true);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_SUCCESS:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_MODIFY_PASSWORD_SUCCESS");
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
    public boolean isActive() {
        return false;
    }

    @Override
    public void OnLeftIconEvent() {
        onFinish("OnLeftIconEvent");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
    }
}
