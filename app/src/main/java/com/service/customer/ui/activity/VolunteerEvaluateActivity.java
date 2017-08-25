package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iflytek.cloud.ErrorCode;
import com.service.customer.R;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.tts.listener.OnDictationListener;
import com.service.customer.components.tts.listener.OnIntializeListener;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.validation.EditTextValidator;
import com.service.customer.components.validation.Validation;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.EvaluateInfo;
import com.service.customer.net.entity.validation.EvaluateValidation;
import com.service.customer.ui.contract.VolunteerEvaluateContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.VolunteerEvaluatePresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;
import com.service.customer.ui.widget.edittext.listener.OnVoiceClickListener;
import com.service.customer.ui.widget.ratingbar.RatingBar;

import java.util.List;

public class VolunteerEvaluateActivity extends ActivityViewImplement<VolunteerEvaluateContract.Presenter> implements VolunteerEvaluateContract.View, OnLeftIconEventListener, View.OnClickListener, OnDictationListener, OnVoiceClickListener, OnIntializeListener {

    private VolunteerEvaluatePresenter volunteerEvaluatePresenter;
    private ImageView ivHeadImage;
    private TextView tvRealName;
    private RatingBar rbEvaluate;
    private VoiceEdittext vetEvaluate;
    private Button btnSubmit;
    private EditTextValidator editTextValidator;
    private EvaluateInfo evaluateInfo;

    private boolean hasLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_evaluate);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        ivHeadImage = ViewUtil.getInstance().findView(this, R.id.ivHeadImage);
        tvRealName = ViewUtil.getInstance().findView(this, R.id.tvRealName);
        rbEvaluate = ViewUtil.getInstance().findView(this, R.id.rbEvaluate);
        vetEvaluate = ViewUtil.getInstance().findView(this, R.id.vetEvaluate);
        btnSubmit = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnSubmit, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_015293, true, R.mipmap.icon_back1, this, android.R.color.white, getString(R.string.evaluate));
        TTSUtil.getInstance().initializeSpeechRecognizer(this);

        volunteerEvaluatePresenter = new VolunteerEvaluatePresenter(this, this);
        volunteerEvaluatePresenter.initialize();

        setBasePresenterImplement(volunteerEvaluatePresenter);
        super.initialize(savedInstanceState);

        vetEvaluate.setHint(getString(R.string.evaluate_prompt1));
        vetEvaluate.setTextCount(0);

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, vetEvaluate.getEtContent(), true, null, new EvaluateValidation()));
        editTextValidator.execute(this, btnSubmit, com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);

        evaluateInfo = BundleUtil.getInstance().getParcelableIntentData(this, Temp.EVALUATE_INFO.getContent());
        if (evaluateInfo != null) {
            GlideUtil.getInstance().with(this, evaluateInfo.getAccountAvatar(), null, getResources().getDrawable(R.mipmap.ic_launcher_round), DiskCacheStrategy.NONE, ivHeadImage);
            tvRealName.setText(evaluateInfo.getRealName());
        }
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance().setOnIntializeListener(this);
        vetEvaluate.setOnVoiceClickListener(this);
        TTSUtil.getInstance().setOnDictationListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    volunteerEvaluatePresenter.checkPermission(this, new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                            if (evaluateInfo != null) {
                                if (rbEvaluate.getSelectedCount() > 0) {
                                    if (editTextValidator.validate(VolunteerEvaluateActivity.this)) {
                                        volunteerEvaluatePresenter.scoreAssistInfo(evaluateInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                                    }
                                } else {
                                    ToastUtil.getInstance().showToast(VolunteerEvaluateActivity.this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
                                }
                            } else {
                                showPromptDialog(R.string.dialog_prompt_save_condolence_record_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
                            }
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                } else {
                    if (evaluateInfo != null) {
                        if (rbEvaluate.getSelectedCount() > 0) {
                            if (editTextValidator.validate(this)) {
                                volunteerEvaluatePresenter.scoreAssistInfo(evaluateInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                            }
                        } else {
                            ToastUtil.getInstance().showToast(this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
                        }
                    } else {
                        showPromptDialog(R.string.dialog_prompt_save_condolence_record_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
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
                    volunteerEvaluatePresenter.checkPermission(this, new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                            if (hasLocation) {
                                if (evaluateInfo != null) {
                                    if (rbEvaluate.getSelectedCount() > 0) {
                                        if (editTextValidator.validate(VolunteerEvaluateActivity.this)) {
                                            volunteerEvaluatePresenter.scoreAssistInfo(evaluateInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                                        }
                                    } else {
                                        ToastUtil.getInstance().showToast(VolunteerEvaluateActivity.this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
                                    }
                                } else {
                                    showPromptDialog(R.string.dialog_prompt_save_condolence_record_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
                                }
                            } else {
                                volunteerEvaluatePresenter.startLocation();
                            }
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                } else {
                    if (hasLocation) {
                        if (evaluateInfo != null) {
                            if (rbEvaluate.getSelectedCount() > 0) {
                                if (editTextValidator.validate(VolunteerEvaluateActivity.this)) {
                                    volunteerEvaluatePresenter.scoreAssistInfo(evaluateInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                                }
                            } else {
                                ToastUtil.getInstance().showToast(VolunteerEvaluateActivity.this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
                            }
                        } else {
                            showPromptDialog(R.string.dialog_prompt_save_condolence_record_error, Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR);
                        }
                    } else {
                        volunteerEvaluatePresenter.startLocation();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDictation(String content) {
        LogUtil.getInstance().print("content:" + content);
        vetEvaluate.setText(content);
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
            case Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_SUCCESS:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SCORE_HELPER_INFO_SUCCESS");
                startMainActivity(Constant.Tab.TASK_MANAGEMENT);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SCORE_VOLUNTEER_INFO_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_DIALOG_PROMPT_SCORE_HELPER_INFO_ERROR");
                onFinish("DIALOG_PROMPT_EVALUATE_INFO_ERROR");
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
        volunteerEvaluatePresenter.stopLocation();
        switch (aMapLocation.getErrorCode()) {
            case AMapLocation.LOCATION_SUCCESS:
                LogUtil.getInstance().print("经度:" + aMapLocation.getLongitude());
                LogUtil.getInstance().print("纬度:" + aMapLocation.getLatitude());
                LogUtil.getInstance().print("精度:" + aMapLocation.getAccuracy());
                LogUtil.getInstance().print("地址:" + aMapLocation.getAddress());
                volunteerEvaluatePresenter.saveAddressInfo(String.valueOf(aMapLocation.getLongitude()), String.valueOf(aMapLocation.getLatitude()), aMapLocation.getAddress());
                break;
            default:
                LogUtil.getInstance().print(aMapLocation.getErrorInfo());
                break;
        }
        hasLocation = true;
    }
}
