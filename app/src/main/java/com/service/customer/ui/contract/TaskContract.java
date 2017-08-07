package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;

import java.io.File;
import java.util.List;

public interface TaskContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLocationPromptDialog(int resoutId, int requestCode);
    }

    interface Presenter extends BasePresenter {

        void location();

        void submit(String data, List<File> file);
    }
}
