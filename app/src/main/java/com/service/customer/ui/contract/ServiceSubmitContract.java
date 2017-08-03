package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;

public interface ServiceSubmitContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void submit(String data);
    }
}
