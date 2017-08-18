package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.service.customer.R;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.ui.contract.WapContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.presenter.WapPresenter;
import com.service.customer.ui.webview.CustomChromeClient;
import com.service.customer.ui.webview.LifeServicePlatform;
import com.service.customer.ui.webview.listener.OnReceivedTitleListener;
import com.yjt.bridge.InjectedWebviewClient;

import java.util.List;

public class WapActivity extends ActivityViewImplement<WapContract.Presenter> implements WapContract.View, OnLeftIconEventListener, OnReceivedTitleListener {

    private WapPresenter presenter;
    private WebView wvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wap);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(this, R.id.inToolbar);
        wvContent = ViewUtil.getInstance().findView(this, R.id.wvContent);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_383857, true, R.mipmap.icon_back1, this, android.R.color.white, null);

        presenter = new WapPresenter(this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            presenter.checkPermission(this);
        }
        presenter.initialize();

        setBasePresenterImplement(presenter);
        getSavedInstanceState(savedInstanceState);

        wvContent.setWebViewClient(new InjectedWebviewClient(this));
        wvContent.setWebChromeClient(new CustomChromeClient(Constant.JavaScript.INJECTED_NAME, LifeServicePlatform.class, this));
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wvContent.getSettings().setSupportMultipleWindows(false);
        wvContent.getSettings().setLoadWithOverviewMode(true);
        wvContent.getSettings().setDatabaseEnabled(true);
        wvContent.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvContent.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        wvContent.getSettings().setUserAgentString(wvContent.getSettings().getUserAgentString() + Regex.SPACE.getRegext() + JS.UA.getContent() + Regex.SPACE.getRegext());
        wvContent.loadUrl(BundleUtil.getInstance().getStringData(this, Temp.URL.getContent()));
    }

    @Override
    protected void setListener() {

    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void OnLeftIconEvent() {
        if (wvContent != null && wvContent.canGoBack()) {
            wvContent.goBack();
        } else {
            onFinish("OnLeftIconEvent");
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void receivedTitle(WebView view, String title) {
        initializeToolbar(R.color.color_383857, true, R.mipmap.icon_back1, this, android.R.color.white, title);
    }

    @Override
    public void onBackPressed() {
        if (wvContent != null && wvContent.canGoBack()) {
            wvContent.goBack();
        } else {
            onFinish("onBackPressed");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wvContent != null) {
            wvContent.clearHistory();
            wvContent.clearCache(true);
            wvContent.destroy();
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
            case Constant.RequestCode.DIALOG_PROMPT_TOKEN_ERROR:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_TOKEN_ERROR");
                startLoginActivity(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
                // 重新执行getVersion
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    presenter.checkPermission(this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        showPermissionPromptDialog();
    }

    @Override
    public void startMainActivity(int tab) {
        
    }
}
