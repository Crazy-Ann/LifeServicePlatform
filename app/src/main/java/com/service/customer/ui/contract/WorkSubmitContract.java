package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.http.model.FileWrapper;

import java.util.List;

public interface WorkSubmitContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveWrokInfo(int workType, String workNote, List<FileWrapper> fileWrappers);

        void deleteFile();
    }
}
