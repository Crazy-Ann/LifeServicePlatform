package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.TaskProcessingContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;


public class TaskProcessingPresenter extends BasePresenterImplement implements TaskProcessingContract.Presenter {

    private Context context;
    private TaskProcessingContract.View view;

    public TaskProcessingPresenter(Context context, TaskProcessingContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        TTSUtil.getInstance().initializeSpeechRecognizer(context);
    }

    @Override
    public void dealTaskInfo(String billNo, int dealStatus, String dealNote) {
        Api.getInstance().dealTaskInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.DEAL_TASK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.DEAL_TASK_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                billNo,
                dealStatus,
                dealNote,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        view.showPromptDialog(R.string.dialog_prompt_deal_task_info_success, Constant.RequestCode.DIALOG_PROMPT_DEAL_TASK_INFO_SUCCESS);
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
