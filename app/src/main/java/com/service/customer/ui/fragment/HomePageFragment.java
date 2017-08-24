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

import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.components.utils.HttpUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.contract.HomePageContract;
import com.service.customer.ui.contract.implement.FragmentViewImplement;
import com.service.customer.ui.presenter.HomePagePresenter;
import com.service.customer.ui.webview.CustomChromeClient;
import com.service.customer.ui.webview.CustomWebviewClient;
import com.service.customer.ui.webview.LifeServicePlatform;

import java.util.List;

public class HomePageFragment extends FragmentViewImplement<HomePageContract.Presenter> implements HomePageContract.View {

    private HomePagePresenter homePagePresenter;
    private WebView wvHomePage;

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
                    homePagePresenter.checkPermission(getActivity(), this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
