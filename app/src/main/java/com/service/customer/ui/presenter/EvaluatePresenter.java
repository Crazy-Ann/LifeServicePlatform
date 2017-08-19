package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.EvaluateContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class EvaluatePresenter extends BasePresenterImplement implements EvaluateContract.Presenter {
    
    private Context context;
    private EvaluateContract.View view;

    public EvaluatePresenter(Context context, EvaluateContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void evaluate(String billNo, int score, String note) {
        Api.getInstance().scoreTaskInfo(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl(),
                BuildConfig.SERVICE_URL + ServiceMethod.SCORE_TASK_INFO,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                billNo,
                score,
                note,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
