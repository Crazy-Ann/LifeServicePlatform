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
import com.service.customer.ui.contract.HelperEvaluateContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class HelperEvaluatePresenter extends BasePresenterImplement implements HelperEvaluateContract.Presenter {

    private Context context;
    private HelperEvaluateContract.View view;

    public HelperEvaluatePresenter(Context context, HelperEvaluateContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        TTSUtil.getInstance().initializeSpeechRecognizer(context);
    }

    @Override
    public void scoreAssistInfo(String billNo, int score, String note) {
        Api.getInstance().scoreAssistInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.SCORE_TASK_INFO,
                BuildConfig.SERVICE_URL + ServiceMethod.SCORE_ASSIST_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                billNo,
                score,
                note,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        view.showPromptDialog(R.string.dialog_prompt_score_helper_info_success, Constant.RequestCode.DIALOG_PROMPT_SCORE_HELPER_INFO_SUCCESS);
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
