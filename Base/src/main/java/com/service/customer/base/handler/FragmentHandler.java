package com.service.customer.base.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.SoftReference;

public abstract class FragmentHandler<T extends Fragment> extends Handler {

    private final SoftReference<T> mFragments;

    public FragmentHandler(T fragments) {
        mFragments = new SoftReference<>(fragments);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T fragments = mFragments.get();
        if (fragments == null) {
            return;
        }
        handleMessage(fragments, msg);
    }

    protected abstract void handleMessage(T fragments, Message msg);
}
