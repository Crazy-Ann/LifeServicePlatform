package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.http.model.FileWrapper;

import java.util.List;

public interface HomePageContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void registerReceiver();

        void unregisterReceiver();

        void saveTaskInfo(String longitude, String latitude, String address, int taskType, String taskNote, List<FileWrapper> fileWrappers);
    }
}
