package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.service.customer.R;
import com.service.customer.base.handler.ActivityHandler;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.MessageUtil;
import com.service.customer.components.utils.ThreadPoolUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.EventInfo;
import com.service.customer.net.entity.EventInfos;
import com.service.customer.ui.contract.MapContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.MapPresenter;

import java.util.List;

public class MapActivity extends ActivityViewImplement<MapContract.Presenter> implements MapContract.View, View.OnClickListener, OnLeftIconEventListener, AMap.OnMarkerClickListener {

    private MapPresenter mapPresenter;
    private MapView      mapView;
    private AMap         aMap;
    private MapHandler   mapHandler;

    private class MapHandler extends ActivityHandler<MapActivity> {

        public MapHandler(MapActivity activity) {
            super(activity);
        }

        @Override
        protected void handleMessage(MapActivity activity, Message msg) {
            switch (msg.what) {
                case com.service.customer.constant.Constant.Message.GET_EVENT_INFOS_SUCCESS:
                    hideLoadingPromptDialog();
                    setEventMarker((EventInfos) msg.obj);
                    break;
                case com.service.customer.constant.Constant.Message.GET_EVNET_INFOS_FAILED:
                    hideLoadingPromptDialog();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        mapView = ViewUtil.getInstance().findView(this, R.id.mapView);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_1f90f0, true, R.mipmap.icon_back1, this, android.R.color.white, BundleUtil.getInstance().getStringData(this, Temp.TITLE.getContent()));
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        mapHandler = new MapHandler(this);
        mapPresenter = new MapPresenter(this, this);
        mapPresenter.initialize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mapPresenter.checkPermission(this);
        } else {
            mapPresenter.generateEventInfos();
        }
        setBasePresenterImplement(mapPresenter);
        getSavedInstanceState(savedInstanceState);

        mapPresenter.setMapOption(Constant.Map.MIYUN_DISTRICT, Constant.Map.ZOOM, Constant.Map.BEARING, Constant.Map.TILT);
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                EventInfos eventInfos = mapPresenter.generateEventInfos();
                if (eventInfos != null) {
                    mapHandler.sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_EVENT_INFOS_SUCCESS, eventInfos));
                } else {
                    mapHandler.sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_EVNET_INFOS_FAILED));
                }
            }
        });
    }

    @Override
    protected void setListener() {
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mapPresenter.checkPermission(this);
                } else {
                    mapPresenter.generateEventInfos();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_NET_WORK_ERROR");
                Intent intent;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setComponent(new ComponentName(Regex.ANDROID_SETTING.getRegext(), Regex.ANDROID_SETTING_MORE.getRegext()));
                    intent.setAction(Intent.ACTION_VIEW);
                }
                startActivityForResult(intent, Constant.RequestCode.NET_WORK_SETTING);
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                startPermissionSettingActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_NET_WORK");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                refusePermissionSetting();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {
        mapPresenter.generateEventInfos();
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        showPermissionPromptDialog();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setEventMarker(EventInfos eventInfos) {
        if (eventInfos != null) {
            for (EventInfo eventInfo : eventInfos.getEventInfos()) {
                aMap.addMarker(new MarkerOptions()
                                       .position(new LatLng(eventInfo.getLongitude(), eventInfo.getLatitude()))
                                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                       .draggable(false)).setObject(eventInfo);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        EventInfo eventInfo = (EventInfo) marker.getObject();
        LogUtil.getInstance().print("url:" + eventInfo.getAccountAvatar());
        LogUtil.getInstance().print("name:" + eventInfo.getRealName());
        LogUtil.getInstance().print("title:" + eventInfo.getTitle());
        LogUtil.getInstance().print("descreption:" + eventInfo.getDescreption());
        LogUtil.getInstance().print("longitude:" + eventInfo.getLongitude());
        LogUtil.getInstance().print("latitude:" + eventInfo.getLatitude());
        return true;
    }

    @Override
    public void OnLeftIconEvent() {
        onFinish("OnLeftIconEvent");
    }
}

