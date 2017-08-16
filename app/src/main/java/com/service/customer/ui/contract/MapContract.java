package com.service.customer.ui.contract;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.service.customer.base.presenter.BasePresenter;
import com.service.customer.base.view.BaseView;
import com.service.customer.net.entity.TaskInfos;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        MapView getMapView();

        void showBoundaryPromptDialog(int resoutId, int requestCode);

        void setEventMarker(TaskInfos taskInfos);
    }

    interface Presenter extends BasePresenter {

        void getBoundary(String condition);

        void mapCameraOperation(LatLng lating, float zoom, float tilt, float bearing);

        void getTaskInfos();

    }
}
