package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.iflytek.cloud.ErrorCode;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.tts.listener.OnDictationListener;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.tts.listener.OnIntializeListener;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.validation.EditTextValidator;
import com.service.customer.components.validation.Validation;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.entity.validation.TaskValidation;
import com.service.customer.ui.contract.TaskProcessingContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.TaskProcessingPresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;
import com.service.customer.ui.widget.edittext.listener.OnVoiceClickListener;

import java.util.List;

public class TaskProcessingActivity extends ActivityViewImplement<TaskProcessingContract.Presenter> implements TaskProcessingContract.View, View.OnClickListener, OnDictationListener, OnLeftIconEventListener, OnVoiceClickListener, OnIntializeListener {

    private TaskProcessingPresenter taskProcessingPresenter;
    private VoiceEdittext vetDealNote;
    private Button btnProcessingCompleted;
    private Button btnCannotHandle;
    private Button btnCancelTask;
    private EditTextValidator editTextValidator;
    private int dealStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_processing);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        vetDealNote = ViewUtil.getInstance().findView(this, R.id.vetWorkNote);
        btnProcessingCompleted = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnProcessingCompleted, this);
        btnCannotHandle = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnCannotHandle, this);
        btnCancelTask = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnCancelTask, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_015293, true, R.mipmap.icon_back1, this, android.R.color.white, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()));

        vetDealNote.setHint(getString(R.string.text_descreption_prompt));
        vetDealNote.setTextCount(0);
        taskProcessingPresenter = new TaskProcessingPresenter(this, this);
        taskProcessingPresenter.initialize();

        setBasePresenterImplement(taskProcessingPresenter);
        getSavedInstanceState(savedInstanceState);

        switch (((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getMemberType()) {
            case Constant.AccountRole.WEI_JI_WEI:
                ViewUtil.getInstance().setViewVisible(btnProcessingCompleted);
                ViewUtil.getInstance().setViewVisible(btnCancelTask);
                ViewUtil.getInstance().setViewGone(btnCannotHandle);
            case Constant.AccountRole.JI_SHENG_BAN:
                ViewUtil.getInstance().setViewVisible(btnProcessingCompleted);
                ViewUtil.getInstance().setViewVisible(btnCannotHandle);
                ViewUtil.getInstance().setViewGone(btnCancelTask);
            case Constant.AccountRole.VOLUNTEER:
                ViewUtil.getInstance().setViewVisible(btnProcessingCompleted);
                ViewUtil.getInstance().setViewVisible(btnCannotHandle);
                ViewUtil.getInstance().setViewGone(btnCancelTask);
                break;
            default:
                break;
        }

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, vetDealNote.getEtContent(), true, null, new TaskValidation()));
        editTextValidator.execute(this, btnProcessingCompleted, com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);
        editTextValidator.execute(this, btnCannotHandle, com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance().setOnIntializeListener(this);
        vetDealNote.setOnVoiceClickListener(this);
        TTSUtil.getInstance().setOnDictationListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnProcessingCompleted:
                if (editTextValidator.validate(this)) {
                    dealStatus = Constant.DealStatus.PROCESSING_COMPLETED;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskProcessingPresenter.checkPermission(this, new PermissionCallback() {
                            @Override
                            public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(TaskProcessingActivity.this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                showPermissionPromptDialog();
                            }
                        });
                    } else {
                        taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                    }
                }
                break;
            case R.id.btnCannotHandle:
                if (editTextValidator.validate(this)) {
                    switch (((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getMemberType()) {
                        case Constant.AccountRole.VOLUNTEER:
                            dealStatus = Constant.DealStatus.CAN_NOT_HANDLE_VOLUNTEER;
                            break;
                        case Constant.AccountRole.JI_SHENG_BAN:
                            dealStatus = Constant.DealStatus.CAN_NOT_HANDLE_JI_SHENG_BAN;
                            break;
                        default:
                            break;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskProcessingPresenter.checkPermission(this, new PermissionCallback() {
                            @Override
                            public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(TaskProcessingActivity.this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                showPermissionPromptDialog();
                            }
                        });
                    } else {
                        taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                    }
                }
                break;
            case R.id.btnCancelTask:
                if (editTextValidator.validate(this)) {
                    dealStatus = Constant.DealStatus.CANCEL_TASK;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskProcessingPresenter.checkPermission(this, new PermissionCallback() {
                            @Override
                            public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                                taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(TaskProcessingActivity.this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                showPermissionPromptDialog();
                            }
                        });
                    } else {
                        taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
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
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    taskProcessingPresenter.checkPermission(this, new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                            taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(TaskProcessingActivity.this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                } else {
                    taskProcessingPresenter.dealTaskInfo(BundleUtil.getInstance().getStringData(this, Temp.BILL_NO.getContent()), dealStatus, vetDealNote.getText().trim());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDictation(String content) {
        LogUtil.getInstance().print("content:" + content);
        vetDealNote.setText(content);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTSUtil.getInstance().stopListening();
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
            case Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_SUCCESS:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_DEAL_TASK_INFO_SUCCESS");
                startMainActivity(Constant.Tab.TASK_MANAGEMENT);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_TTS_INTIALIZED_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TTS_INTIALIZED_ERROR");
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
    public void onVoiceClick() {
        if (!isFinishing()) {
            TTSUtil.getInstance().startListening(this);
        }
    }

    @Override
    public void onIntialize(int resultCode) {
        if (resultCode == ErrorCode.SUCCESS) {
            TTSUtil.getInstance().startPlaying(this, null);
        } else {
            showPromptDialog(R.string.tts_intialized_error_prompt, Constant.RequestCode.DIALOG_PROMPT_TTS_INTIALIZED_ERROR);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
