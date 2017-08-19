package com.service.customer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.ActivityUtil;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.FragmentUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.ui.contract.MainContract;
import com.service.customer.ui.contract.implement.ActivityViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.fragment.HomePageFragment;
import com.service.customer.ui.fragment.MineFragment;
import com.service.customer.ui.fragment.TaskManagementFragment;
import com.service.customer.ui.presenter.MainPresenter;

import java.util.List;

public class MainActivity extends ActivityViewImplement<MainContract.Presenter> implements MainContract.View, TabLayout.OnTabSelectedListener, View.OnLayoutChangeListener {

    private TabLayout tlMenu;
    private FragmentUtil fragmentUtil;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        tlMenu = ViewUtil.getInstance().findView(this, R.id.tlMenu);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mainPresenter = new MainPresenter(this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mainPresenter.checkPermission(this,this);
        }
        mainPresenter.initialize();
        setBasePresenterImplement(mainPresenter);
        getSavedInstanceState(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mainPresenter.checkPermission(this,this);
        }

        tlMenu.setTabMode(TabLayout.MODE_FIXED);
        tlMenu.setBackgroundColor(Color.WHITE);
        tlMenu.setSelectedTabIndicatorHeight(0);
        tlMenu.addTab(tlMenu.newTab().setCustomView(getLayoutInflater().inflate(R.layout.tab_home_page, null)));
        tlMenu.addTab(tlMenu.newTab().setCustomView(getLayoutInflater().inflate(R.layout.tab_task_management, null)));
        tlMenu.addTab(tlMenu.newTab().setCustomView(getLayoutInflater().inflate(R.layout.tab_mine, null)));
        fragmentUtil = new FragmentUtil(getSupportFragmentManager(), R.id.tlContent);
//        fragmentUtil.addItem(new FragmentUtil.OperationInfo(this, getString(R.string.home_page), HomePageFragment2.class));
        fragmentUtil.addItem(new FragmentUtil.OperationInfo(this, getString(R.string.home_page), HomePageFragment.class));
        fragmentUtil.addItem(new FragmentUtil.OperationInfo(this, getString(R.string.task_management), TaskManagementFragment.class));
        fragmentUtil.addItem(new FragmentUtil.OperationInfo(this, getString(R.string.mine), MineFragment.class));

        switch (BundleUtil.getInstance().getIntData(this, Temp.TAB.getContent())) {
            case Constant.Tab.HOME_PAGE:
                fragmentUtil.show(getString(R.string.home_page), true);
                tlMenu.getTabAt(Constant.Tab.HOME_PAGE).select();
                break;
            case Constant.Tab.TASK_MANAGEMENT:
                fragmentUtil.show(getString(R.string.task_management), true);
                tlMenu.getTabAt(Constant.Tab.TASK_MANAGEMENT).select();
                break;
            case Constant.Tab.MINE:
                fragmentUtil.show(getString(R.string.mine), true);
                tlMenu.getTabAt(Constant.Tab.MINE).select();
                break;
        }
    }

    @Override
    protected void setListener() {
        tlMenu.addOnTabSelectedListener(this);
        tlMenu.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
            case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mainPresenter.checkPermission(this,this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.quit_prompt))
                .setPositiveButtonText(this, R.string.confirm)
                .setNegativeButtonText(this, R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_QUIT)
                .show(this);
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
            case Constant.RequestCode.DIALOG_PROMPT_QUIT:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_QUIT");
                BaseApplication.getInstance().releaseInstance();
                ActivityUtil.removeAll();
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
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_NET_WORK_ERROR");
                break;
            case Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SET_PERMISSION");
                refusePermissionSetting();
                break;
            case Constant.RequestCode.DIALOG_PROMPT_QUIT:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_QUIT");
                break;
            default:
                break;
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LogUtil.getInstance().print("onTabSelected," + tab.getPosition());
        switch (tab.getPosition()) {
            case Constant.Tab.HOME_PAGE:
                fragmentUtil.show(getString(R.string.home_page), true);
                break;
            case Constant.Tab.TASK_MANAGEMENT:
                fragmentUtil.show(getString(R.string.task_management), true);
                break;
            case Constant.Tab.MINE:
                fragmentUtil.show(getString(R.string.mine), true);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        LogUtil.getInstance().print("onTabUnselected," + tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        LogUtil.getInstance().print("onTabReselected," + tab.getPosition());
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        LogUtil.getInstance().print("onLayoutChange");
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
