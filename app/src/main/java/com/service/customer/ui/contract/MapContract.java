package com.service.customer.ui.contract;

import com.amap.api.maps.model.LatLng;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.net.entity.TaskInfos;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setEventMarker(TaskInfos taskInfos);
    }

    interface Presenter extends BasePresenter {

        void setMapOption(LatLng lating, float zoom, float bearing, float tilt);

        TaskInfos generateEventInfos();

    }
}
