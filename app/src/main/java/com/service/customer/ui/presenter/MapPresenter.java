package com.service.customer.ui.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.service.customer.R;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.TaskInfos;
import com.service.customer.ui.contract.MapContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.IOException;


public class MapPresenter extends BasePresenterImplement implements MapContract.Presenter {

    private MapContract.View view;
    private AMap aMap;

    public AMap getAMap() {
        return aMap;
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
    }

    @Override
    public void setMapCamera(LatLng latLng, float zoom, float tilt, float bearing) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, zoom, tilt, bearing)));
//        AMapOptions aMapOptions = new AMapOptions();
//        aMapOptions.zoomGesturesEnabled(true);
//        aMapOptions.scrollGesturesEnabled(false);
//        aMapOptions.tiltGesturesEnabled(false);
//        aMapOptions.camera(new CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build());
    }

    @Override
    public TaskInfos generateEventInfos() {
        view.showLoadingPromptDialog(R.string.get_evnet_infos, Constant.RequestCode.DIALOG_PROMPT_GET_EVENT_INFOS);
        try {
            return new TaskInfos().parse(JSONObject.parseObject(IOUtil.getInstance().readString(context.getAssets().open("TaskInfos.json"))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
