package com.service.customer.ui.presenter;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.service.customer.R;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.constant.Constant;
import com.service.customer.constant.ServiceMethod;
import com.service.customer.net.Api;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.net.entity.TaskInfos;
import com.service.customer.net.listener.ApiListener;
import com.service.customer.ui.contract.TaskMapContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;


public class TaskMapPresenter extends BasePresenterImplement implements TaskMapContract.Presenter {

    private Context context;
    private TaskMapContract.View view;
    private AMap aMap;
    private DistrictSearch districtSearch;
    private DistrictSearchQuery districtSearchQuery;

    public AMap getAMap() {
        return aMap;
    }

    public DistrictSearch getDistrictSearch() {
        return districtSearch;
    }

    public TaskMapPresenter(Context context, TaskMapContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        aMap = view.getMapView().getMap();
        districtSearch = new DistrictSearch(BaseApplication.getInstance());
        districtSearchQuery = new DistrictSearchQuery();
    }

    @Override
    public void getBoundary(String condition) {
        view.showLoadingPromptDialog(R.string.get_boundary_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_BOUNDARY);
        districtSearchQuery.setKeywords(condition);
        districtSearchQuery.setShowBoundary(true);
        districtSearch.setQuery(districtSearchQuery);
        districtSearch.searchDistrictAsyn();
    }

    @Override
    public void mapCameraOperation(LatLng latLng, float zoom, float tilt, float bearing) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, zoom, tilt, bearing)));
//        AMapOptions aMapOptions = new AMapOptions();
//        aMapOptions.zoomGesturesEnabled(true);
//        aMapOptions.scrollGesturesEnabled(false);
//        aMapOptions.tiltGesturesEnabled(false);
//        aMapOptions.camera(new CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build());
    }

    @Override
    public void getTaskList() {
        Api.getInstance().taskList(
                context,
                view,
//                ((ConfigInfo) BaseApplication.getInstance().getConfigInfo()).getServerUrl() + ServiceMethod.TASK_LIST,
                BuildConfig.SERVICE_URL + ServiceMethod.TASK_LIST,
                ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken(),
                1,
                new ApiListener() {

                    @Override
                    public void success(final BaseEntity baseEntity) {
                        getBoundary(context.getString(R.string.miyun_district));
                        if (baseEntity != null) {
                            view.setEventMarker((TaskInfos) baseEntity);
                        }
                    }

                    @Override
                    public void failed(BaseEntity entity, String errorCode, String errorMessage) {

                    }
                }
        );
    }
}
