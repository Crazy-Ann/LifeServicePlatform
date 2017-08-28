package com.service.customer.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.amap.api.location.AMapLocation;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.HttpUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.contract.HomePageContract;
import com.service.customer.ui.contract.implement.FragmentViewImplement;
import com.service.customer.ui.presenter.HomePagePresenter;
import com.service.customer.ui.presenter.listener.CustomPhoneStateListener;
import com.service.customer.ui.presenter.listener.OnSaveTaskInfoListener;
import com.service.customer.ui.webview.CustomChromeClient;
import com.service.customer.ui.webview.CustomWebviewClient;
import com.service.customer.ui.webview.LifeServicePlatform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomePageFragment extends FragmentViewImplement<HomePageContract.Presenter> implements HomePageContract.View, OnSaveTaskInfoListener {

    private HomePagePresenter homePagePresenter;
    private WebView wvHomePage;
    private AMapLocation aMapLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        findViewById();
        initialize(savedInstanceState);
        setListener();
        return rootView;
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(rootView, R.id.inToolbar);
        wvHomePage = ViewUtil.getInstance().findView(rootView, R.id.wvHomePage);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        homePagePresenter = new HomePagePresenter(getActivity(), this);
        homePagePresenter.initialize();

        setBasePresenterImplement(homePagePresenter);
        getSavedInstanceState(savedInstanceState);

        switch (((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getMemberType()) {
            case Constant.AccountRole.WEI_JI_WEI:
            case Constant.AccountRole.JI_SHENG_BAN:
            case Constant.AccountRole.VOLUNTEER:
                break;
            case Constant.AccountRole.HELP_SEEKER:
                homePagePresenter.registerReceiver();
                homePagePresenter.startLocation();
                break;
            default:
                break;
        }

        wvHomePage.setWebViewClient(new CustomWebviewClient(getActivity()));
        wvHomePage.setWebChromeClient(new CustomChromeClient(Constant.JavaScript.INJECTED_NAME, LifeServicePlatform.class));
        wvHomePage.getSettings().setJavaScriptEnabled(true);
        wvHomePage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvHomePage.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wvHomePage.getSettings().setSupportMultipleWindows(false);
        wvHomePage.getSettings().setLoadWithOverviewMode(true);
        wvHomePage.getSettings().setDatabaseEnabled(true);
        wvHomePage.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvHomePage.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //        wvHomePage.getSettings().setUserAgentString(wvHomePage.getSettings().getUserAgentString() + Regex.SPACE.getRegext() + JS.UA.getContent() + Regex.SPACE.getRegext());
        wvHomePage.loadUrl(HttpUtil.getInstance().addParameter(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getIndexUrl(), RequestParameterKey.TOKEN, ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken()));
    }

    @Override
    protected void setListener() {
        CustomPhoneStateListener.getInstance().setOnSaveTaskInfoListener(this);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
            case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    homePagePresenter.checkPermission(getActivity(), new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePagePresenter.unregisterReceiver();
        if (wvHomePage != null) {
            wvHomePage.clearHistory();
            wvHomePage.clearCache(true);
            wvHomePage.destroy();
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showLocationPromptDialog(String prompt, int requestCode) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        homePagePresenter.stopLocation();
        switch (aMapLocation.getErrorCode()) {
            case AMapLocation.LOCATION_SUCCESS:
                this.aMapLocation = aMapLocation;
                LogUtil.getInstance().print("经度:" + aMapLocation.getLongitude());
                LogUtil.getInstance().print("纬度:" + aMapLocation.getLatitude());
                LogUtil.getInstance().print("精度:" + aMapLocation.getAccuracy());
                LogUtil.getInstance().print("地址:" + aMapLocation.getAddress());
                break;
            default:
                LogUtil.getInstance().print(aMapLocation.getErrorInfo());
                break;
        }
    }

    @Override
    public void onSaveTaskInfo(String data) {
        if (aMapLocation != null) {
            homePagePresenter.saveTaskInfo(String.valueOf(aMapLocation.getLongitude()), String.valueOf(aMapLocation.getLatitude()), aMapLocation.getAddress(), 6, ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getRealName()
                    + "于"
                    + new SimpleDateFormat(Regex.DATE_FORMAT_ALL.getRegext(), Locale.getDefault()).format(new Date(System.currentTimeMillis()))
                    + "拨打了"
                    + data
                    + ",请关注!", null);
        }
    }
}
