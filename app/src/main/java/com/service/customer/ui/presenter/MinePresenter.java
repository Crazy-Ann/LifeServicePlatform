package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.HeadImageInfo;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.MineContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.File;
import java.io.IOException;

public class MinePresenter extends BasePresenterImplement implements MineContract.Presenter {

    private Context context;
    private MineContract.View view;

    public MinePresenter(Context context, MineContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void saveHeadImage(File file) {
        Api.getInstance().saveHeadImage(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl(),
                BuildConfig.SERVICE_URL + ServiceMethod.SAVE_HEAD_IMAGE,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                new FileWrapper(file),
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        HeadImageInfo headImageInfo = (HeadImageInfo) baseEntity;
                        if (headImageInfo != null) {
                            view.setHeadImage(((HeadImageInfo) baseEntity).getAccountAvatar());
                            deleteFile();
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_submit_picture_error, Constant.RequestCode.DIALOG_PROMPT_SUBMIT_PICTURE_ERROR);
                        }
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }

    @Override
    public void deleteFile() {
        try {
            IOUtil.getInstance().deleteFile(IOUtil.getInstance().getExternalFilesDir(BaseApplication.getInstance(), Constant.FILE_NAME, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
