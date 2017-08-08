package com.service.customer.ui.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.TaskInfos;
import com.service.customer.ui.contract.MapContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.IOException;


public class MapPresenter extends BasePresenterImplement implements MapContract.Presenter {

    private MapContract.View view;
    private AMap aMap;
    private DistrictSearch districtSearch;
    private DistrictSearchQuery districtSearchQuery;

    public AMap getAMap() {
        return aMap;
    }

    public DistrictSearch getDistrictSearch() {
        return districtSearch;
    }

    public MapPresenter(Context context, MapContract.View view) {
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
    public TaskInfos getEventInfos() {
        view.showLoadingPromptDialog(R.string.get_evnet_infos, Constant.RequestCode.DIALOG_PROMPT_GET_EVENT_INFOS);
        try {
            return new TaskInfos().parse(JSONObject.parseObject(IOUtil.getInstance().readString(context.getAssets().open("TaskInfos.json"))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
