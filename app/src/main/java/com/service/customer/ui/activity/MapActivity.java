package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.AnimationUtil;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.TaskInfo;
import com.service.customer.net.entity.TaskInfos;
import com.service.customer.ui.contract.MapContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.MapPresenter;

import java.util.List;

public class MapActivity extends ActivityViewImplement<MapContract.Presenter> implements MapContract.View, View.OnClickListener, OnLeftIconEventListener, AMap.OnMarkerClickListener, DistrictSearch.OnDistrictSearchListener {

    private MapPresenter mapPresenter;
    private MapView mapView;
    private LinearLayout llTaskInfo;
    private ImageView ivHeadImage;
    private TextView tvRealName;
    private ImageView ivClose;
    private TextView tvDescreption;

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
        llTaskInfo = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.llTaskInfo, this);
        ivHeadImage = ViewUtil.getInstance().findView(this, R.id.ivHeadImage);
        tvRealName = ViewUtil.getInstance().findView(this, R.id.tvRealName);
        ivClose = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivClose, this);
        tvDescreption = ViewUtil.getInstance().findView(this, R.id.tvDescreption);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_383857, true, R.mipmap.icon_back1, this, android.R.color.white, getString(R.string.map_event));
        mapView.onCreate(savedInstanceState);

        mapPresenter = new MapPresenter(this, this);
        mapPresenter.initialize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mapPresenter.checkPermission(this,this);
        } else {
            mapPresenter.getTaskInfos();
        }
        setBasePresenterImplement(mapPresenter);
        getSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void setListener() {
        mapPresenter.getAMap().setOnMarkerClickListener(this);
        mapPresenter.getDistrictSearch().setOnDistrictSearchListener(this);
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
        switch (view.getId()) {
            case R.id.llTaskInfo:
                //todo 被求助角色
                //TaskInfo taskInfo = (TaskInfo) llTaskInfo.getTag();
                //if (taskInfo != null) {
                //    Bundle bundle = new Bundle();
                //   bundle.putParcelable(Temp.TASK_INFO.getContent(), taskInfo);
                //   startActivity(EvaluateActivity.class, bundle);
                //}
                break;
            case R.id.ivClose:
                AnimationUtil.getInstance().fadeOutByAlphaAnimation(llTaskInfo, 100, 100);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mapPresenter.checkPermission(this,this);
                } else {
                    mapPresenter.getTaskInfos();
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
                mapPresenter.getBoundary(getString(R.string.miyun_district));
                break;
            case Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TOKEN_ERROR");
                startLoginActivity(true);
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
            case Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_LOCATION_ERROR");
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
        mapPresenter.getTaskInfos();
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
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void showBoundaryPromptDialog(int resoutId, int requestCode) {
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(resoutId))
                .setPositiveButtonText(this, R.string.try_again)
                .setNegativeButtonText(this, R.string.cancel)
                .setCancelable(true)
                .setCancelableOnTouchOutside(true)
                .setRequestCode(requestCode)
                .show(this);
    }

    @Override
    public void setEventMarker(TaskInfos taskInfos) {
        if (taskInfos != null) {
            for (TaskInfo taskInfo : taskInfos.getTaskInfos()) {
                mapPresenter.getAMap().addMarker(new MarkerOptions()
                                                         .position(new LatLng(taskInfo.getLatitude(), taskInfo.getLongitude()))
                                                         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                         .draggable(false)).setObject(taskInfo);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            TaskInfo taskInfo = (TaskInfo) marker.getObject();
            AnimationUtil.getInstance().fadeInByAlphaAnimation(llTaskInfo, 100, 100);
            GlideUtil.getInstance().with(this, taskInfo.getAccountAvatar(), null, null, DiskCacheStrategy.NONE, ivHeadImage);
            tvRealName.setText(taskInfo.getRealName());
            tvDescreption.setText(taskInfo.getTasNote());
            llTaskInfo.setTag(taskInfo);
        }
        return true;
    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        hideLoadingPromptDialog();
        if (districtResult.getAMapException() != null) {
            switch (districtResult.getAMapException().getErrorCode()) {
                case AMapException.CODE_AMAP_SUCCESS:
                    final DistrictItem districtItem = districtResult.getDistrict().get(0);
                    if (districtItem == null) {
                        return;
                    }
                    LatLonPoint centerLatLng = districtItem.getCenter();
                    if (centerLatLng != null) {
                        mapPresenter.mapCameraOperation(new LatLng(centerLatLng.getLatitude(), centerLatLng.getLongitude()), Constant.Map.ZOOM, Constant.Map.BEARING, Constant.Map.TILT);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] districtBoundaries = districtItem.districtBoundary();
                            if (districtBoundaries != null) {
                                for (String districtBoundary : districtBoundaries) {
                                    PolylineOptions polylineOption = new PolylineOptions();
                                    boolean isStartPoint = true;
                                    LatLng startPointLatLng = null;
                                    for (String latstr : districtBoundary.split(Regex.SEMICOLON.getRegext())) {
                                        String[] latLngs = latstr.split(Regex.COMMA.getRegext());
                                        if (isStartPoint) {
                                            isStartPoint = false;
                                            startPointLatLng = new LatLng(Double.parseDouble(latLngs[1]), Double.parseDouble(latLngs[0]));
                                        }
                                        polylineOption.add(new LatLng(Double.parseDouble(latLngs[1]), Double.parseDouble(latLngs[0])));
                                    }
                                    if (startPointLatLng != null) {
                                        polylineOption.add(startPointLatLng);
                                    }
                                    polylineOption.width(10).color(Color.BLUE);
                                    mapPresenter.getAMap().addPolyline(polylineOption);
                                }
                            }
                        }
                    });
                    break;
                default:
                    showBoundaryPromptDialog(R.string.dialog_prompt_get_boundary_error, Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
                    break;
            }
        } else {
            showBoundaryPromptDialog(R.string.dialog_prompt_get_boundary_error, Constant.RequestCode.DIALOG_PROMPT_LOCATION_ERROR);
        }
    }

    @Override
    public void OnLeftIconEvent() {
        onFinish("OnLeftIconEvent");
    }

    @Override
    public void startMainActivity(int tab) {

    }
}

