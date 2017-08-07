package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;

import java.io.File;

public interface MineContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();
        
        void setHeadImage(String url);

        void startLoginActivity();
    }

    interface Presenter extends BasePresenter {

        void saveHeadImage(File file);

        void logout();
    }
}
