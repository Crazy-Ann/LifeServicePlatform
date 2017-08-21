package com.service.customer.components.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;

public class FragmentUtil {

    private FragmentManager fragmentManager;
    private HashMap<String, OperationInfo> items = new HashMap<String, OperationInfo>();
    private OperationInfo operationInfo;
    private int resource;
    private int[] animations = new int[2];

    public FragmentUtil(FragmentManager fragmentManager, int resource) {
        this.fragmentManager = fragmentManager;
        this.resource = resource;
        items.clear();
        animations[0] = android.R.anim.fade_in;
        animations[1] = android.R.anim.fade_out;
    }

    public void addItem(OperationInfo info) {
        items.put(info.getTag(), info);
    }

    public void getItem(OperationInfo info) {
        items.get(info.getTag());
    }

    public boolean isShowing(String tag) {
        return operationInfo.tag.equals(tag);
    }

    public Fragment show(String tag, boolean hasAnimate, boolean isRefresh) {
        return show(items.get(tag), hasAnimate, isRefresh);
    }

    public Fragment show(int id, boolean hasAnimate, boolean isRefresh) {
        return show(items.get(String.valueOf(id)), hasAnimate, isRefresh);
    }

    public Fragment show(String tag, Bundle args, boolean hasAnimate, boolean isRefresh) {
        OperationInfo info = items.get(tag);
        info.bundle = args;
        return show(info, hasAnimate, isRefresh);
    }

    private Fragment show(OperationInfo operationInfo, boolean hasAnimate, boolean isRefresh) {
        FragmentTransaction transaction = fragmentManager.beginTransaction().disallowAddToBackStack();
        if (hasAnimate) {
            transaction.setCustomAnimations(animations[0], animations[1]);
        }
        if (this.operationInfo != operationInfo) {
            if (this.operationInfo != null && this.operationInfo.fragment != null) {
                transaction.hide(this.operationInfo.fragment);
            }
            this.operationInfo = operationInfo;
            if (this.operationInfo != null) {
                if (this.operationInfo.fragment == null) {
                    this.operationInfo.fragment = Fragment.instantiate(this.operationInfo.context, this.operationInfo.clazz.getName(), this.operationInfo.bundle);
                    if (operationInfo.bundle != null) {
                        this.operationInfo.fragment.setArguments(operationInfo.bundle);
                    }
                    transaction.add(resource, this.operationInfo.fragment, this.operationInfo.tag);
                } else {
                    transaction.show(this.operationInfo.fragment);
                }
            }
        } else {
            if(isRefresh){
                transaction.remove(this.operationInfo.fragment);
                this.operationInfo.fragment = Fragment.instantiate(this.operationInfo.context, this.operationInfo.clazz.getName(), this.operationInfo.bundle);
                if (operationInfo.bundle != null) {
                    this.operationInfo.fragment.setArguments(operationInfo.bundle);
                }
                transaction.add(resource, this.operationInfo.fragment, this.operationInfo.tag);
            }else{
                LogUtil.getInstance().print(operationInfo.fragment + " has displayed");
            }
        }
        transaction.commitAllowingStateLoss();
        if (this.operationInfo == null) {
            return null;
        } else {
            return this.operationInfo.fragment;
        }
    }

    public static class OperationInfo {
        protected Context context;
        protected String tag;
        protected Class<?> clazz;
        protected Bundle bundle;
        protected Fragment fragment;

        public OperationInfo(Context context, String tag, Class<?> cls) {
            this(context, tag, cls, null);
        }

        public OperationInfo(Context context, int viewId, Class<?> cls) {
            this(context, viewId, cls, null);
        }

        public OperationInfo(Context context, String tag, Class<?> cls, Bundle args) {
            this.context = context;
            this.tag = tag;
            this.clazz = cls;
            this.bundle = args;
        }

        public OperationInfo(Context context, int viewId, Class<?> cls, Bundle args) {
            this.context = context;
            this.tag = String.valueOf(viewId);
            this.clazz = cls;
            this.bundle = args;
        }

        public String getTag() {
            return tag;
        }

        @Override
        public String toString() {
            return "OperationInfo{" +
                    "context:" + context + '\'' +
                    ", tag:" + tag + '\'' +
                    ", clazz:" + clazz + '\'' +
                    ", bundle:" + bundle + '\'' +
                    ", fragment:" + fragment + '\'' +
                    '}';
        }
    }
}
