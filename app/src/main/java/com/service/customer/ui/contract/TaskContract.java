package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.http.model.FileWrapper;

import java.util.List;

public interface TaskContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLocationPromptDialog(int resoutId, int requestCode);
    }

    interface Presenter extends BasePresenter {

        void location();

        void saveTaskInfo(String longitude, String latitude, String address, String descreption, List<FileWrapper> fileWrappers);
    }
}
