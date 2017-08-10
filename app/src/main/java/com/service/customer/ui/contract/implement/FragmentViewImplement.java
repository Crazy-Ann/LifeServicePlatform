package com.service.customer.ui.contract.implement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.service.customer.R;
import com.service.customer.base.fragment.BaseFragment;
import com.service.customer.base.view.BaseView;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.ui.activity.LoginActivity;
import com.service.customer.ui.dialog.ProgressDialog;
import com.service.customer.ui.dialog.PromptDialog;

public abstract class FragmentViewImplement<T> extends BaseFragment implements BaseView<T> {

    private DialogFragment dialogFragment;
    private BasePresenterImplement basePresenterImplement;

    public void setLoginPresenter(@NonNull T loginPresenter) { }

    public BasePresenterImplement getBasePresenterImplement() {
        return basePresenterImplement;
    }

    public void setBasePresenterImplement(BasePresenterImplement basePresenterImplement) {
        this.basePresenterImplement = basePresenterImplement;
    }

    @Override
    public void showNetWorkPromptDialog() {
        LogUtil.getInstance().print("FragmentViewImplement-Dialog");
        PromptDialog.createBuilder(getActivity().getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(R.string.net_work_error_prompt))
                .setPositiveButtonText(getActivity(), R.string.setting)
                .setNegativeButtonText(getActivity(), R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK)
                .show(getActivity());
    }

    @Override
    public void showPermissionPromptDialog() {

    }

    @Override
    public void showLoadingPromptDialog(int resoutId, int requestCode) {
        if (dialogFragment != null) {
            ViewUtil.getInstance().hideDialog(dialogFragment);
        }
        dialogFragment = ProgressDialog.createBuilder(getActivity().getSupportFragmentManager())
                .setPrompt(getString(resoutId))
                .setCancelableOnTouchOutside(false)
                .setCancelable(false)
                .setRequestCode(requestCode)
                .showAllowingStateLoss(getActivity().getApplicationContext());
    }

    @Override
    public void hideLoadingPromptDialog() {
        ViewUtil.getInstance().hideDialog(dialogFragment);
    }

    @Override
    public void showPromptDialog(int resoutId, int requestCode) {
        PromptDialog.createBuilder(getActivity().getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(getString(resoutId))
                .setPositiveButtonText(getActivity(), R.string.dialog_known)
                .setCancelable(true)
                .setCancelableOnTouchOutside(true)
                .setRequestCode(requestCode)
                .show(getActivity());
    }

    @Override
    public void showPromptDialog(String prompt, int requestCode) {
        PromptDialog.createBuilder(getActivity().getSupportFragmentManager())
                .setTitle(getString(R.string.dialog_prompt))
                .setPrompt(prompt)
                .setPositiveButtonText(getActivity(), R.string.dialog_known)
                .setCancelable(true)
                .setCancelableOnTouchOutside(false)
                .setRequestCode(requestCode)
                .show(getActivity());
    }


    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {
    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void startLoginActivity() {
        startActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        onFinish("startLoginActivity");
    }

    @Override
    public void startPermissionSettingActivity() {

    }

    @Override
    public void refusePermissionSetting() {

    }
}
