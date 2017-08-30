package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.iflytek.cloud.ErrorCode;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.handler.ActivityHandler;
import com.service.customer.base.sticky.adapter.FixedStickyViewAdapter;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.tts.listener.OnDictationListener;
import com.service.customer.components.tts.listener.OnIntializeListener;
import com.service.customer.components.utils.BitmapUtil;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.ImageUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.MessageUtil;
import com.service.customer.components.utils.ThreadPoolUtil;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.validation.EditTextValidator;
import com.service.customer.components.validation.Validation;
import com.service.customer.components.widget.sticky.decoration.GridLayoutDividerItemDecoration;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.TaskImageInfo;
import com.service.customer.net.entity.validation.TaskValidation;
import com.service.customer.ui.adapter.TaskImageAdapter;
import com.service.customer.ui.binder.TaskImageBinder;
import com.service.customer.ui.contract.TaskSubmitContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.TaskSubmitPresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;
import com.service.customer.ui.widget.edittext.listener.OnVoiceClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskSubmitActivity extends ActivityViewImplement<TaskSubmitContract.Presenter> implements TaskSubmitContract.View, View.OnClickListener, OnDictationListener, OnLeftIconEventListener, FixedStickyViewAdapter.OnItemClickListener, OnVoiceClickListener, OnIntializeListener {

    private TaskSubmitPresenter taskSubmitPresenter;
    private TextView tvLocation;
    private VoiceEdittext vetTaskNote;
    private RecyclerView rvTaskImage;
    private TaskImageAdapter taskImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private Button btnSubmit;
    private EditTextValidator editTextValidator;

    private AMapLocation aMapLocation;
    private List<TaskImageInfo> taskImageInfos;

    private TaskSubmitHandler taskSubmitHandler;

    private static class TaskSubmitHandler extends ActivityHandler<TaskSubmitActivity> {

        public TaskSubmitHandler(TaskSubmitActivity activity) {
            super(activity);
        }

        @Override
        protected void handleMessage(TaskSubmitActivity activity, Message msg) {
            switch (msg.what) {
                case Constant.Message.GET_IMAGE_SUCCESS:
                    activity.hideLoadingPromptDialog();
                    activity.taskImageInfos.add((TaskImageInfo) msg.obj);
                    activity.taskImageAdapter.setData(activity.taskImageInfos);
                    break;
                case Constant.Message.GET_IMAGE_FAILED:
                    activity.hideLoadingPromptDialog();
                    activity.showPromptDialog(R.string.dialog_prompt_get_image_error, Constant.RequestCode.DIALOG_PROMPT_GET_IMAGE_ERROR);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        tvLocation = ViewUtil.getInstance().findView(this, R.id.tvLocation);
        vetTaskNote = ViewUtil.getInstance().findView(this, R.id.vetWorkNote);
        rvTaskImage = ViewUtil.getInstance().findView(this, R.id.rvTaskImage);
        btnSubmit = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnSubmit, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_015293, true, R.mipmap.icon_back1, this, android.R.color.white, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()));
        vetTaskNote.setHint(getString(R.string.text_descreption_prompt));
        vetTaskNote.setTextCount(0);
        taskSubmitHandler = new TaskSubmitHandler(this);

        taskSubmitPresenter = new TaskSubmitPresenter(this, this);
        taskSubmitPresenter.initialize();

        setBasePresenterImplement(taskSubmitPresenter);
        getSavedInstanceState(savedInstanceState);

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, vetTaskNote.getEtContent(), true, null, new TaskValidation()));
        editTextValidator.execute(this, btnSubmit, com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE,
                                  com.service.customer.components.constant.Constant.View.DEFAULT_RESOURCE, null, null, true);

        taskImageInfos = new ArrayList<>();
        TaskImageInfo taskImageInfo = new TaskImageInfo();
        taskImageInfo.setResourceId(R.mipmap.icon_add);
        taskImageInfos.add(taskImageInfo);

        taskImageAdapter = new TaskImageAdapter(this, new TaskImageBinder(this, rvTaskImage), true);
        gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        rvTaskImage.setLayoutManager(gridLayoutManager);
        SparseArray<GridLayoutDividerItemDecoration.ItemDecorationProps> itemDecorationProps = new SparseArray<>();
        itemDecorationProps.put(com.service.customer.components.constant.Constant.View.GRID_LAYOUT, new GridLayoutDividerItemDecoration.ItemDecorationProps(ViewUtil.getInstance().dp2px(this, getResources().getDimension(R.dimen.dp_5)), ViewUtil.getInstance().dp2px(this, getResources().getDimension(R.dimen.dp_5)), true, true));
        rvTaskImage.addItemDecoration(new GridLayoutDividerItemDecoration(itemDecorationProps));
        rvTaskImage.setAdapter(taskImageAdapter);
        taskImageAdapter.setData(taskImageInfos);
    }

    @Override
    protected void setListener() {
        TTSUtil.getInstance().setOnIntializeListener(this);
        vetTaskNote.setOnVoiceClickListener(this);
        TTSUtil.getInstance().setOnDictationListener(this);
        taskImageAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (aMapLocation != null) {
                    if (editTextValidator.validate(this)) {
                        List<FileWrapper> fileWrappers = new ArrayList<>();
                        for (TaskImageInfo taskImageInfo : taskImageInfos) {
                            File file = taskImageInfo.getFile();
                            if (file != null) {
                                fileWrappers.add(new FileWrapper(file));
                            }
                        }
                        taskSubmitPresenter.saveTaskInfo(String.valueOf(aMapLocation.getLongitude()), String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getAddress()), BundleUtil.getInstance().getIntData(this, Temp.TASK_TYPE.getContent()), vetTaskNote.getText().trim(), fileWrappers);
                    }
                } else {
                    showLocationPromptDialog(getString(R.string.dialog_prompt_location_error), Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
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
                    taskSubmitPresenter.checkPermission(this, new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
                            taskSubmitPresenter.startLocation();
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                } else {
                    taskSubmitPresenter.startLocation();
                }
                break;
            case Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH:
                showLoadingPromptDialog(R.string.get_image_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_IMAGE);
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = IOUtil.getInstance().getExternalFilesDir(BaseApplication.getInstance(), Constant.FILE_NAME, taskImageInfos.size() + Regex.IMAGE_JPG.getRegext());
                            if (file != null && BitmapUtil.getInstance().saveBitmap(ImageUtil.getNarrowBitmap(BaseApplication.getInstance(), IOUtil.getInstance().getFileUri(BaseApplication.getInstance(), true, file), 0.25f), file.getAbsolutePath())) {
                                TaskImageInfo taskImageInfo = new TaskImageInfo();
                                taskImageInfo.setFile(file);
                                taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, taskImageInfo));
                            } else {
                                taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        } catch (IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                        }
                    }
                });
                break;
            case Constant.RequestCode.REQUEST_CODE_ALBUM:
                if (data != null) {
                    final Uri uri = data.getData();
                    showLoadingPromptDialog(R.string.get_image_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_IMAGE);
                    ThreadPoolUtil.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = IOUtil.getInstance().getExternalFilesDir(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SINGLE_SLASH.getRegext() + taskImageInfos.size() + Regex.IMAGE_JPG.getRegext());
                                Bitmap photo = ImageUtil.getNarrowBitmap(BaseApplication.getInstance(), uri, 0.25f);
                                if (file != null && BitmapUtil.getInstance().saveBitmap(photo, file.getAbsolutePath())) {
                                    TaskImageInfo taskImageInfo = new TaskImageInfo();
                                    taskImageInfo.setFile(file);
                                    taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, taskImageInfo));
                                } else {
                                    taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                                }
                            } catch (InterruptedException | ExecutionException | IOException e) {
                                e.printStackTrace();
                                taskSubmitHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDictation(String content) {
        LogUtil.getInstance().print("content:" + content);
        vetTaskNote.setText(content);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTSUtil.getInstance().stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskSubmitPresenter.deleteFile();
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
                taskSubmitPresenter.startLocation();
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                startActivityForResult(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Regex.IMAGE_PATH.getRegext(), Constant.RequestCode.REQUEST_CODE_ALBUM, null);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TOKEN_ERROR");
                startLoginActivity(true);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_SUCCESS:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SAVE_TASK_INFO_SUCCESS");
                startMainActivity(Constant.Tab.TASK_MANAGEMENT);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_SUCCESS:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SAVE_WORK_INFO_SUCCESS");
                onFinish("DIALOG_PROMPT_SAVE_WORK_INFO_SUCCESS");
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                LogUtil.getInstance().print("onNeutralButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                try {
                    HashMap<String, Parcelable> map = new HashMap<>();
                    map.put(MediaStore.EXTRA_OUTPUT, IOUtil.getInstance().getFileUri(this, true, IOUtil.getInstance().getExternalFilesDir(this, Constant.FILE_NAME, Regex.LEFT_SINGLE_SLASH.getRegext() + taskImageInfos.size() + Regex.IMAGE_JPG.getRegext()).getAbsolutePath()));
                    startActivityForResultWithParcelable(MediaStore.ACTION_IMAGE_CAPTURE, Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Intent.FLAG_GRANT_READ_URI_PERMISSION : com.service.customer.components.constant.Constant.FileProvider.DEFAULT_FLAG, map, Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        hideLoadingPromptDialog();
        taskSubmitPresenter.stopLocation();
        switch (aMapLocation.getErrorCode()) {
            case AMapLocation.LOCATION_SUCCESS:
                this.aMapLocation = aMapLocation;
                LogUtil.getInstance().print("经度:" + aMapLocation.getLongitude());
                LogUtil.getInstance().print("纬度:" + aMapLocation.getLatitude());
                LogUtil.getInstance().print("精度:" + aMapLocation.getAccuracy());
                LogUtil.getInstance().print("地址:" + aMapLocation.getAddress());
                tvLocation.setText(aMapLocation.getAddress());
                break;
            default:
                showLocationPromptDialog(aMapLocation.getErrorInfo(), Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        if (taskImageInfos.size() < 4) {
            PromptDialog.createBuilder(getSupportFragmentManager())
                    .setTitle(getString(R.string.dialog_prompt))
                    .setPrompt(getString(R.string.select_image_prompt))
                    .setPositiveButtonText(this, R.string.album)
                    .setNegativeButtonText(this, R.string.photograph)
                    .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE)
                    .setCancelableOnTouchOutside(true)
                    .setCancelable(true)
                    .show(this);
        } else {
            ToastUtil.getInstance().showToast(this, R.string.get_task_image_prompt, Toast.LENGTH_SHORT);
        }
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
}
