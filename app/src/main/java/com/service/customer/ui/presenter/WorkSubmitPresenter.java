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
import com.service.customer.ui.contract.WorkSubmitContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.IOException;
import java.util.List;


public class WorkSubmitPresenter extends BasePresenterImplement implements WorkSubmitContract.Presenter {

    private Context context;
    private WorkSubmitContract.View view;

    public WorkSubmitPresenter(Context context, WorkSubmitContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
//        super.initialize();
        TTSUtil.getInstance().initializeSpeechRecognizer(context);
    }

    @Override
    public void saveWrokInfo(int workType, String workNote, List<FileWrapper> fileWrappers) {
        Api.getInstance().saveWrokInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.SAVE_WOORK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_WOORK_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                workType,
                workNote,
                fileWrappers,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        view.showPromptDialog(R.string.dialog_prompt_save_condolence_record_success, Constant.RequestCode.DIALOG_PROMPT_SAVE_WORK_INFO_SUCCESS);
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
