package com.service.customer.ui.presenter;

import android.content.Context;

import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;
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

    private MineContract.View view;

    public MinePresenter(Context context, MineContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void saveHeadImage(File file) {
        LogUtil.getInstance().print("file:" + file.getAbsolutePath());
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
                            try {
                                view.setHeadImage(((HeadImageInfo) baseEntity).getAccountAvatar());
                                IOUtil.getInstance().deleteFile(IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), com.service.customer.constant.Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_check_version_error, Constant.RequestCode.DIALOG_PROMPT_CHECK_VERSION_ERROR);

                        }
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }

    @Override
    public void logout() {

    }
}
