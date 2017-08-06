package com.service.customer.ui.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMapOptions;
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

    public MapPresenter(Context context, MapContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void setMapOption(LatLng latLng, float zoom, float bearing, float tilt) {
        AMapOptions aOptions = new AMapOptions();
        aOptions.zoomGesturesEnabled(true);
        aOptions.scrollGesturesEnabled(false);
        aOptions.camera(new CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build());
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
