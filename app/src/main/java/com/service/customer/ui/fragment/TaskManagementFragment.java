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
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.contract.TaskManagementContract;
import com.service.customer.ui.contract.implement.FragmentViewImplement;
import com.service.customer.ui.presenter.TaskManagementPresenter;
import com.service.customer.ui.webview.CustomChromeClient;
import com.service.customer.ui.webview.LifeServicePlatform;
import com.yjt.bridge.InjectedWebviewClient;

import java.util.List;

public class TaskManagementFragment extends FragmentViewImplement<TaskManagementContract.Presenter> implements TaskManagementContract.View {


    private TaskManagementPresenter taskManagementPresenter;
    private WebView wvTaskManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task_management, container, false);
        findViewById();
        initialize(savedInstanceState);
        setListener();
        return rootView;
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(rootView, R.id.inToolbar);
        wvTaskManagement = ViewUtil.getInstance().findView(rootView, R.id.wvTaskManagement);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_383857, android.R.color.white, false, getString(R.string.task_management), null);

        taskManagementPresenter = new TaskManagementPresenter(getActivity(), this);
        taskManagementPresenter.initialize();

        setBasePresenterImplement(taskManagementPresenter);
        getSavedInstanceState(savedInstanceState);

        wvTaskManagement.setWebViewClient(new InjectedWebviewClient(getActivity()));
        wvTaskManagement.setWebChromeClient(new CustomChromeClient(Constant.JavaScript.INJECTED_NAME, LifeServicePlatform.class));
        wvTaskManagement.getSettings().setJavaScriptEnabled(true);
        wvTaskManagement.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvTaskManagement.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wvTaskManagement.getSettings().setSupportMultipleWindows(false);
        wvTaskManagement.getSettings().setLoadWithOverviewMode(true);
        wvTaskManagement.getSettings().setDatabaseEnabled(true);
        wvTaskManagement.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvTaskManagement.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        wvTaskManagement.getSettings().setUserAgentString(wvTaskManagement.getSettings().getUserAgentString() + Regex.SPACE.getRegext() + JS.UA.getContent() + Regex.SPACE.getRegext());
        LogUtil.getInstance().print("url:" + ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getTaskUrl() + Regex.QUESTION_MARK.getRegext() + RequestParameterKey.TOKEN + Regex.EQUALS.getRegext() + ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken());
        wvTaskManagement.loadUrl(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getTaskUrl() + Regex.QUESTION_MARK.getRegext() + RequestParameterKey.TOKEN + Regex.EQUALS.getRegext() + ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getToken());
        //wvTaskManagement.loadUrl(Constant.ASSET_URL.TASK_LIST);
    }

    @Override
    protected void setListener() {

    }

    //TODO
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //wvTaskManagement.loadUrl(Constant.ASSET_URL.TASK_LIST);
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
                    taskManagementPresenter.checkPermission(getActivity(), this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wvTaskManagement != null) {
            wvTaskManagement.clearHistory();
            wvTaskManagement.clearCache(true);
            wvTaskManagement.destroy();
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

    @Override
    public void startMainActivity(int tab) {

    }
}
