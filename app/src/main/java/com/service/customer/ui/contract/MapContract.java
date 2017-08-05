package com.service.customer.ui.contract;

import com.amap.api.maps.model.LatLng;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.net.entity.EventInfo;
import com.service.customer.net.entity.EventInfos;

import java.util.List;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setEventMarker(EventInfos eventInfos);
    }

    interface Presenter extends BasePresenter {

        void setMapOption(LatLng lating, float zoom, float bearing, float tilt);

        EventInfos generateEventInfos();

    }
}
