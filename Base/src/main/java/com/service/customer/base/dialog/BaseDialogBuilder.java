package com.service.customer.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.service.customer.base.constant.Constant;
import com.service.customer.base.constant.Temp;

public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

    private String tag = Constant.View.CUSTOM_DIALOG;
    private int requestCode = Constant.RequestCode.DIALOG;
    protected final FragmentManager fragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;
    private Fragment targetFragment;
    private boolean isCancelable = true;
    private boolean isCancelableOnTouchOutside = true;
    private boolean isUseDarkTheme = false;
    private boolean isUseLightTheme = false;

    public BaseDialogBuilder(FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        this.fragmentManager = fragmentManager;
        mClass = clazz;
    }

    protected abstract T self();

    protected abstract Bundle prepareArguments();

    public T setCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            this.isCancelable = cancelable;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        this.targetFragment = fragment;
        this.requestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return self();
    }

    public T setTag(String tag) {
        this.tag = tag;
        return self();
    }

    public T useDarkTheme() {
        this.isUseDarkTheme = true;
        return self();
    }

    public T useLightTheme() {
        this.isUseLightTheme = true;
        return self();
    }

    private BaseDialogFragment create(Context ctx) {
        final Bundle bundle = prepareArguments();
        final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(ctx, mClass.getName(), bundle);
        bundle.putBoolean(Temp.CANCELABLE_ON_TOUCH_OUTSIDE.getContent(), isCancelableOnTouchOutside);
        bundle.putBoolean(Temp.USE_DARK_THEME.getContent(), isUseDarkTheme);
        bundle.putBoolean(Temp.USE_LIGHT_THEME.getContent(), isUseLightTheme);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, requestCode);
        } else {
            bundle.putInt(Temp.REQUEST_CODE.getContent(), requestCode);
        }
        fragment.setCancelable(isCancelable);
        return fragment;
    }

    public DialogFragment show(Context ctx) {
        BaseDialogFragment fragment = create(ctx);
        fragment.show(fragmentManager, tag);
        return fragment;
    }

    public void dismiss(Context ctx) {
        create(ctx).dismiss();
    }

    public DialogFragment showAllowingStateLoss(Context ctx) {
        BaseDialogFragment fragment = create(ctx);
        fragment.showAllowingStateLoss(fragmentManager, tag);
        return fragment;
    }
}
