package com.service.customer.components.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.service.customer.components.R;
import com.service.customer.components.constant.Constant;

public class SnackBarUtil {

    private Snackbar mSnackbar;

    private static SnackBarUtil mSnackbarUtil;

    private SnackBarUtil() {
        // cannot be instantiated
    }

    public static synchronized SnackBarUtil getInstance() {
        if (mSnackbarUtil == null) {
            mSnackbarUtil = new SnackBarUtil();
        }
        return mSnackbarUtil;
    }

    public static void releaseInstance() {
        if (mSnackbarUtil != null) {
            mSnackbarUtil = null;
        }
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length) {
        showSnackBar(activity, message, length, null, null, Constant.View.DEFAULT_SIZE, Constant.View.DEFAULT_COLOR);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, float textSize) {
        showSnackBar(activity, message, length, null, null, textSize, Constant.View.DEFAULT_COLOR);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, int color) {
        showSnackBar(activity, message, length, null, null, Constant.View.DEFAULT_SIZE, color);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, float textSize, int color) {
        showSnackBar(activity, message, length, null, null, textSize, color);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message1, int length, CharSequence message2, View.OnClickListener listener) {
        showSnackBar(activity, message1, length, message2, listener, Constant.View.DEFAULT_SIZE, Constant.View.DEFAULT_COLOR);
    }


    public void showSnackBar(FragmentActivity activity, CharSequence message1, int length, CharSequence message2, View.OnClickListener listener, float textSize, int color) {
        if (activity != null) {
            if (mSnackbar == null) {
                mSnackbar = Snackbar.make(activity.getWindow().getDecorView(), message1, length);
            } else {
                mSnackbar.setText(message1);
            }

            if (listener != null) {
                if (!TextUtils.isEmpty(message2)) {
                    mSnackbar.setAction(message2, listener);
                }
            }

            if (textSize != Constant.View.DEFAULT_SIZE) {
                ((TextView) ViewUtil.getInstance().findView(mSnackbar.getView(), R.id.snackbar_text)).setTextSize(textSize);
            }
            if (color != Constant.View.DEFAULT_COLOR) {
                ((TextView) ViewUtil.getInstance().findView(mSnackbar.getView(), R.id.snackbar_text)).setTextColor(color);
            }
            mSnackbar.show();
        }
    }

    public void hideSnackBar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    public boolean isShown() {
        return mSnackbar != null && mSnackbar.isShownOrQueued();
    }
}
