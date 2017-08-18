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
import com.amap.api.location.AMapLocationListener;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.base.handler.ActivityHandler;
import com.service.customer.base.sticky.adapter.FixedStickyViewAdapter;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.tts.OnDictationListener;
import com.service.customer.components.tts.TTSUtil;
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
import com.service.customer.ui.contract.TaskContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.TaskPresenter;
import com.service.customer.ui.widget.edittext.VoiceEdittext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskActivity extends ActivityViewImplement<TaskContract.Presenter> implements TaskContract.View, View.OnClickListener, OnDictationListener, OnLeftIconEventListener, AMapLocationListener, FixedStickyViewAdapter.OnItemClickListener {

    private TaskPresenter taskPresenter;
    private TextView tvLocation;
    private VoiceEdittext vetDescreption;
    private RecyclerView rvTaskImage;
    private TaskImageAdapter taskImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private Button btnSubmit;
    private EditTextValidator editTextValidator;

    private AMapLocation aMapLocation;
    private List<TaskImageInfo> taskImageInfos;

    private TaskHandler taskHandler;

    private class TaskHandler extends ActivityHandler<TaskActivity> {

        public TaskHandler(TaskActivity activity) {
            super(activity);
        }

        @Override
        protected void handleMessage(TaskActivity activity, Message msg) {
            switch (msg.what) {
                case Constant.Message.GET_IMAGE_SUCCESS:
                    hideLoadingPromptDialog();
                    taskImageInfos.add((TaskImageInfo) msg.obj);
                    taskImageAdapter.setData(taskImageInfos);
                    break;
                case Constant.Message.GET_IMAGE_FAILED:
                    hideLoadingPromptDialog();
                    showPromptDialog(R.string.dialog_prompt_get_image_error, Constant.RequestCode.DIALOG_PROMPT_GET_IMAGE_ERROR);
                    break;
                default:
                    break;
            }
        }
    }

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
        vetDescreption = ViewUtil.getInstance().findView(this, R.id.vetDescreption);
        rvTaskImage = ViewUtil.getInstance().findView(this, R.id.rvTaskImage);
        btnSubmit = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnSubmit, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_383857, true, R.mipmap.icon_back1, this, android.R.color.white, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()));
        TTSUtil.getInstance(this).initializeSpeechRecognizer();

        vetDescreption.setHint(getString(R.string.text_descreption_prompt));
        vetDescreption.setTextCount(0);
        taskHandler = new TaskHandler(this);
        taskPresenter = new TaskPresenter(this, this);
        taskPresenter.initialize();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            taskPresenter.checkPermission(this);
        } else {
            if (BundleUtil.getInstance().getBooleanData(this, Temp.NEED_LOCATION.getContent())) {
                ViewUtil.getInstance().setViewVisible(tvLocation);
                taskPresenter.location();
            } else {
                ViewUtil.getInstance().setViewGone(tvLocation);
            }
        }

        setBasePresenterImplement(taskPresenter);
        getSavedInstanceState(savedInstanceState);

        editTextValidator = new EditTextValidator();
        editTextValidator.add(new Validation(null, vetDescreption.getEtContent(), true, null, new TaskValidation()));
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
        TTSUtil.getInstance(this).setOnDictationListener(this);
        taskPresenter.getAMapLocationClient().setLocationListener(this);
        taskImageAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (BundleUtil.getInstance().getBooleanData(this, Temp.NEED_LOCATION.getContent())) {
                    if (aMapLocation != null) {
                        if (editTextValidator.validate(this)) {
                            List<FileWrapper> fileWrappers = new ArrayList<>();
                            for (TaskImageInfo taskImageInfo : taskImageInfos) {
                                File file = taskImageInfo.getFile();
                                if (file != null) {
                                    fileWrappers.add(new FileWrapper(file));
                                }
                            }
                            taskPresenter.saveTaskInfo(String.valueOf(aMapLocation.getLongitude()), String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getAddress()), 1, vetDescreption.getText().trim(), fileWrappers);
                        }
                    } else {
                        showLocationPromptDialog(R.string.dialog_prompt_location_error, Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
                    }
                } else {
                    if (editTextValidator.validate(this)) {
//                        if (taskImageInfos != null && taskImageInfos.size() > 2) {
                        List<FileWrapper> fileWrappers = new ArrayList<>();
                        for (TaskImageInfo taskImageInfo : taskImageInfos) {
                            File file = taskImageInfo.getFile();
                            if (file != null) {
                                fileWrappers.add(new FileWrapper(file));
                            }
                        }
                        taskPresenter.saveWrokInfo(1, vetDescreption.getText().trim(), fileWrappers);
//                        }else{
//                        }
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
                    taskPresenter.checkPermission(this);
                } else {
                    if (BundleUtil.getInstance().getBooleanData(this, Temp.NEED_LOCATION.getContent())) {
                        ViewUtil.getInstance().setViewVisible(tvLocation);
                        taskPresenter.location();
                    } else {
                        ViewUtil.getInstance().setViewGone(tvLocation);
                    }
                }
                break;
            case Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH:
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + taskImageInfos.size() + Regex.IMAGE_JPG.getRegext());
                            if (file != null) {
                                TaskImageInfo taskImageInfo = new TaskImageInfo();
                                taskImageInfo.setFile(file);
                                taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, taskImageInfo));
                            } else {
                                taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
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
                                File file = IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + taskImageInfos.size() + Regex.IMAGE_JPG.getRegext());
                                Bitmap photo = ImageUtil.getNarrowBitmap(BaseApplication.getInstance(), uri, 0.5f);
                                if (file != null && BitmapUtil.getInstance().saveBitmap(photo, file.getAbsolutePath())) {
                                    TaskImageInfo taskImageInfo = new TaskImageInfo();
                                    taskImageInfo.setFile(file);
                                    taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, taskImageInfo));
                                } else {
                                    taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                                }
                            } catch (InterruptedException | ExecutionException | IOException e) {
                                e.printStackTrace();
                                taskHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
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
        vetDescreption.setText(content);
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
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                startActivityForResult(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Regex.IMAGE_PATH.getRegext(), Constant.RequestCode.REQUEST_CODE_ALBUM, null);
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                LogUtil.getInstance().print("onNeutralButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                try {
                    HashMap<String, Parcelable> map = new HashMap<>();
                    map.put(MediaStore.EXTRA_OUTPUT, Uri.parse(IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext()).getAbsolutePath()));
                    startActivityForResultWithParcelable(MediaStore.ACTION_IMAGE_CAPTURE, map, Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
        if (BundleUtil.getInstance().getBooleanData(this, Temp.NEED_LOCATION.getContent())) {
            ViewUtil.getInstance().setViewVisible(tvLocation);
            taskPresenter.location();
        } else {
            ViewUtil.getInstance().setViewGone(tvLocation);
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
    public void startMainActivity(int tab) {
        Bundle bundle = new Bundle();
        bundle.putInt(Temp.TAB.getContent(), tab);
        startActivity(MainActivity.class, bundle);
        onFinish("startMainActivity");
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
                this.aMapLocation = aMapLocation;
                LogUtil.getInstance().print("经度:" + aMapLocation.getLongitude());
                LogUtil.getInstance().print("纬度:" + aMapLocation.getLatitude());
                LogUtil.getInstance().print("精度:" + aMapLocation.getAccuracy());
                LogUtil.getInstance().print("地址:" + aMapLocation.getAddress());
                tvLocation.setText(aMapLocation.getAddress());
                break;
            default:
                showLocationPromptDialog(R.string.dialog_prompt_location_error, Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
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
}
