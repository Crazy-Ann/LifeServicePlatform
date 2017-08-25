package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.ModifyPasswordContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

public class ModifyPasswordPresenter extends BasePresenterImplement implements ModifyPasswordContract.Presenter {

    private Context context;
    private ModifyPasswordContract.View view;

    public ModifyPasswordPresenter(Context context, ModifyPasswordContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void modifyPassword(String account, String password) {
        Api.getInstance().modifyPassword(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.MODIFY_PASSWORD,
                BuildConfig.SERVICE_URL + ServiceMethod.MODIFY_PASSWORD,
                account,
                password,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        view.showPromptDialog(R.string.dialog_prompt_modify_password_success, Constant.RequestCode.DIALOG_PROMPT_MODIFY_PASSWORD_SUCCESS);
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
