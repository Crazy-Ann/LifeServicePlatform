package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        boolean isRememberLoginInfo();

        void startMainActivity();
    }

    interface Presenter extends BasePresenter {

        void login(String account, String password);

        void clearLoginInfo();
    }
}
