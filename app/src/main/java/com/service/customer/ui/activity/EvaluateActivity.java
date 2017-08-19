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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.tts.OnDictationListener;
import com.service.customer.components.tts.TTSUtil;
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
import com.service.customer.net.entity.TaskInfo;
import com.service.customer.net.entity.validation.TaskValidation;
import com.service.customer.ui.contract.EvaluateContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.EvaluatePresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;
import com.service.customer.ui.widget.ratingbar.RatingBar;

import java.util.List;

public class EvaluateActivity extends ActivityViewImplement<EvaluateContract.Presenter> implements EvaluateContract.View, OnLeftIconEventListener, View.OnClickListener, OnDictationListener {

    private EvaluatePresenter evaluatePresenter;
    private ImageView ivHeadImage;
    private TextView tvRealName;
    private RatingBar rbEvaluate;
    private VoiceEdittext vetEvaluate;
    private Button btnSubmit;
    private EditTextValidator editTextValidator;
    private TaskInfo taskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
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
        initializeToolbar(R.color.color_383857, true, R.mipmap.icon_back1, this, android.R.color.white, getString(R.string.service_evaluate));

        evaluatePresenter = new EvaluatePresenter(this, this);
        evaluatePresenter.initialize();

        setBasePresenterImplement(evaluatePresenter);
        getSavedInstanceState(savedInstanceState);

        vetEvaluate.setHint(getString(R.string.evaluate_prompt1));
        vetEvaluate.setTextCount(0);

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, vetEvaluate.getEtContent(), true, null, new TaskValidation()));
        editTextValidator.execute(this, btnSubmit, com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);

        taskInfo = BundleUtil.getInstance().getParcelableData(this, (Temp.TASK_INFO.getContent()));
        if (taskInfo != null) {
            GlideUtil.getInstance().with(this, taskInfo.getAccountAvatar(), null, getResources().getDrawable(R.mipmap.ic_launcher_round), DiskCacheStrategy.NONE, ivHeadImage);
            tvRealName.setText(taskInfo.getRealName());
        }
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance(this).setOnDictationListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    evaluatePresenter.checkPermission(this,this);
                } else {
                    if (taskInfo != null) {
                        if (rbEvaluate.getSelectedCount() > 0) {
                            if (editTextValidator.validate(this)) {
                                evaluatePresenter.evaluate(taskInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                            }
                        } else {
                            ToastUtil.getInstance().showToast(this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
                        }
                    } else {
                        showPromptDialog(R.string.dialog_prompt_task_infos_error, Constant.RequestCode.DIALOG_PROMPT_TASK_INFO_ERROR);
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
        if (taskInfo != null) {
            if (rbEvaluate.getSelectedCount() > 0) {
                if (editTextValidator.validate(this)) {
                    evaluatePresenter.evaluate(taskInfo.getBillNo(), rbEvaluate.getSelectedCount(), vetEvaluate.getText());
                }
            } else {
                ToastUtil.getInstance().showToast(this, R.string.evaluate_prompt2, Toast.LENGTH_SHORT);
            }
        } else {
            showPromptDialog(R.string.dialog_prompt_task_infos_error, Constant.RequestCode.DIALOG_PROMPT_TASK_INFO_ERROR);
        }
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
    public void OnLeftIconEvent() {
        onFinish("OnLeftIconEvent");
    }

    @Override
    public void startMainActivity(int tab) {

    }
}
