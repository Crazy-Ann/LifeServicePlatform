package com.service.customer.components.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 消息提示工具
 *
 * @author yjt
 */
public class ToastUtil {

    private Toast mToast;

    private static ToastUtil mToastUtil;

    private ToastUtil() {
        // cannot be instantiated
    }

    public static synchronized ToastUtil getInstance() {
        if (mToastUtil == null) {
            mToastUtil = new ToastUtil();
        }
        return mToastUtil;
    }

    public static void releaseInstance() {
        if (mToastUtil != null) {
            mToastUtil = null;
        }
    }

    public void showToast(Context mContext, CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, message, duration);
        } else {
            mToast.setText(message);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showToast(Context mContext, int resId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, resId, duration);
        } else {
            mToast.setText(resId);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
