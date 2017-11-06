package com.service.customer.ui.presenter;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushService;
import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.SharedPreferenceUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.service.PushIntentService;
import com.service.customer.ui.contract.LoginContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class LoginPresenter extends BasePresenterImplement implements LoginContract.Presenter {

    private Context context;
    private LoginContract.View view;

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
//        super.initialize();
        PushManager.getInstance().initialize(BaseApplication.getInstance(), PushService.class);
        PushManager.getInstance().registerPushIntentService(BaseApplication.getInstance(), PushIntentService.class);
        PushManager.getInstance().turnOnPush(context);
    }

    @Override
    public void login(final String account, final String password) {
        Api.getInstance().login(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.LOGIN,
                BuildConfig.SERVICE_URL + ServiceMethod.LOGIN,
                account,
                password,
                new ApiListener() {

                    @Override
                    public void success(BaseEntity baseEntity) {
                        LoginInfo loginInfo = (LoginInfo) baseEntity;
                        if (loginInfo != null) {
                            try {
                                BaseApplication.getInstance().setLoginInfo(loginInfo);
                                SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.INDEX_URL, loginInfo.getIndexUrl(), true);
                                SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.TASK_URL, loginInfo.getTaskUrl(), true);
                                if (view.isRememberLoginInfo()) {
                                    SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.ACCOUNT, account, true);
                                    SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.PASSWORD, password, true);
                                } else {
                                    SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.ACCOUNT, Regex.NONE.getRegext(), true);
                                    SharedPreferenceUtil.getInstance().putString(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.PASSWORD, Regex.NONE.getRegext(), true);
                                }
                                view.startMainActivity(Constant.Tab.HOME_PAGE);
                            } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        } else {
                            view.showPromptDialog(R.string.dialog_prompt_login_error, Constant.RequestCode.DIALOG_PROMPT_LOGIN_ERROR);
                        }
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {
                        clearLoginInfo();
                    }
                }
        );
    }

    @Override
    public void clearLoginInfo() {
        SharedPreferenceUtil.getInstance().remove(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.ACCOUNT, true);
        SharedPreferenceUtil.getInstance().remove(context, Constant.Profile.LOGIN_PROFILE, Context.MODE_PRIVATE, Constant.Profile.PASSWORD, true);
    }
}
