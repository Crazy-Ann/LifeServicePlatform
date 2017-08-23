package com.service.customer.ui.contract;

import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.net.entity.NotificationAnnouncementInfos;
import com.service.customer.net.entity.ServiceInfos;

public interface HomePageContract2 {

    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        ServiceInfos generateServiceInfos();

        NotificationAnnouncementInfos generateNotificationAnnouncementInfos();
    }
}
