package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.TaskSubmitContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.IOException;
import java.util.List;


public class TaskSubmitPresenter extends BasePresenterImplement implements TaskSubmitContract.Presenter {

    private Context context;
    private TaskSubmitContract.View view;

    public TaskSubmitPresenter(Context context, TaskSubmitContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        TTSUtil.getInstance().initializeSpeechRecognizer(context);
    }

    @Override
    public void startLocation() {
        view.showLoadingPromptDialog(R.string.location_prompt, Constant.RequestCode.DIALOG_PROGRESS_LOCATION);
        super.startLocation();
    }

    @Override
    public void saveTaskInfo(String longitude, String latitude, String address, int taskType, String taskNote, List<FileWrapper> fileWrappers) {
        Api.getInstance().saveTaskInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.SAVE_TASK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_TASK_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                longitude,
                latitude,
                address,
                taskType,
                taskNote,
                fileWrappers,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        view.showPromptDialog(R.string.dialog_prompt_save_task_info_success, Constant.RequestCode.DIALOG_PROMPT_SAVE_TASK_INFO_SUCCESS);
                        deleteFile();
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }

    @Override
    public void deleteFile() {
        LogUtil.getInstance().print("deleteFile");
        try {
            IOUtil.getInstance().deleteFile(IOUtil.getInstance().getExternalFilesDir(BaseApplication.getInstance(), Constant.FILE_NAME, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
