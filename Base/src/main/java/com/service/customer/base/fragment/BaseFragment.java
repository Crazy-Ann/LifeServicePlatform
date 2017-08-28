package com.service.customer.base.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.base.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.toolbar.listener.OnLeftIconEventListener;
import com.service.customer.base.toolbar.listener.OnRightIconEventListener;
import com.service.customer.base.toolbar.listener.OnRightTextEventListener;
import com.service.customer.base.toolbar.listener.OnTitleEventListener;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    protected Toolbar inToolbar;
    protected ImageView ivLeftIconEvent;
    protected TextView tvTitle;
    protected TextView tvRightTextEvent;
    protected ImageView ivRightIconEvent;
    protected ImageView ivTitleRightIcon;

    /**
     * 只有中间tittle，tittle是否可以点击需要设置
     */
    protected void initializeToolbar(int toolbarColorId, int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener) {
        initializeToolbar(toolbarColorId, false, Constant.View.DEFAULT_RESOURCE, null,
                          titleColorId, isTitleClickable, title, onTitleEventListener, Constant.View.DEFAULT_RESOURCE, null);
    }

    /**
     * 有左侧icon，tittle不可点击
     */

    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener, int titleColorId, String title) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                          true, titleColorId, false, title, null,
                          false, Constant.View.DEFAULT_RESOURCE, null, null,
                          false, Constant.View.DEFAULT_RESOURCE, null);
    }

    /**
     * 有左侧icon，右侧文字的tittle，tittle是否可以点击需要设置
     */
    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener
            , int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener
            , int rightTextColorId, String rightText, OnRightTextEventListener onRightTextEventListener) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                          true, titleColorId, isTitleClickable, title, onTitleEventListener,
                          true, rightTextColorId, rightText, onRightTextEventListener,
                          false, Constant.View.DEFAULT_RESOURCE, null);
    }

    /**
     * 有左侧icon，右侧icon的tittle，tittle是否可以点击需要设置
     */
    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener
            , int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener
            , int rightIconId, final OnRightIconEventListener onRightIconEventListener) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                          true, titleColorId, isTitleClickable, title, onTitleEventListener,
                          false, Constant.View.DEFAULT_RESOURCE, null, null,
                          true, rightIconId, onRightIconEventListener);
    }

    @SuppressLint("NewApi")
    private void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, final OnLeftIconEventListener onLeftIconEventListener
            , boolean isShowTitle, int titleColorId, boolean isTitleClickable, String title, final OnTitleEventListener onTitleEventListener
            , boolean hasRightTextEvent, int rightTextColorId, String rightText, final OnRightTextEventListener onRightTextEventListener
            , boolean hasRightIconEvent, int rightIconId, final OnRightIconEventListener onRightIconEventListener) {
        if (inToolbar != null) {
            if (toolbarColorId != Constant.View.DEFAULT_RESOURCE) {
                inToolbar.setBackgroundColor(getResources().getColor(toolbarColorId));
            }
            if (hasLeftIconEvent) {
                if (ivLeftIconEvent == null) {
                    ivLeftIconEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.ivLeftIconEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onLeftIconEventListener.OnLeftIconEvent();
                        }
                    });
                }
                if (leftIconId != Constant.View.DEFAULT_RESOURCE) {
                    GlideUtil.getInstance().with(getActivity(), leftIconId, null, null, DiskCacheStrategy.NONE, ivLeftIconEvent);
                    ViewUtil.getInstance().setViewVisible(ivLeftIconEvent);
                }
            } else {
                if (ivLeftIconEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(ivLeftIconEvent);
                }
            }
            if (isShowTitle) {
                if (isTitleClickable) {
                    if (tvTitle == null) {
                        tvTitle = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.tvTitle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onTitleEventListener.onTitletEvent();
                            }
                        });
                    }
                } else {
                    if (tvTitle == null) {
                        tvTitle = ViewUtil.getInstance().findView(inToolbar, R.id.tvTitle);
                    }
                }
                tvTitle.setText(title);
                if (titleColorId != Constant.View.DEFAULT_RESOURCE) {
                    tvTitle.setTextColor(getResources().getColor(titleColorId));
                }
                ViewUtil.getInstance().setViewVisible(tvTitle);
            } else {
                if (tvTitle != null) {
                    ViewUtil.getInstance().setViewInvisible(tvTitle);
                    tvTitle.setText(null);
                }
            }
            if (hasRightTextEvent) {
                if (tvRightTextEvent == null) {
                    tvRightTextEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.tvRightTextEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRightTextEventListener.OnRightTextEvent();
                        }
                    });
                }
                ViewUtil.getInstance().setViewVisible(tvRightTextEvent);
                tvRightTextEvent.setText(rightText);
                if (rightTextColorId != Constant.View.DEFAULT_RESOURCE) {
                    tvRightTextEvent.setTextColor(getResources().getColor(rightTextColorId));
                }
            } else {
                if (tvRightTextEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(tvRightTextEvent);
                    tvRightTextEvent.setText(null);
                }
            }
            if (hasRightIconEvent) {
                if (ivRightIconEvent == null) {
                    ivRightIconEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.ivRightIconEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRightIconEventListener.OnRightIconEvent();
                        }
                    });
                }
                if (leftIconId != Constant.View.DEFAULT_RESOURCE) {
                    GlideUtil.getInstance().with(getActivity(), rightIconId, null, null, DiskCacheStrategy.NONE, ivRightIconEvent);
                    ViewUtil.getInstance().setViewVisible(ivRightIconEvent);
                }
            } else {
                if (ivRightIconEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(ivRightIconEvent);
                }
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onAttach() invoked!!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onCreate() invoked!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onCreateView() invoked!!");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onViewCreated() invoked!!");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onActivityCreated() invoked!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onStart() invoked!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onResume() invoked!!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setSavedInstanceState(outState);
        super.onSaveInstanceState(outState);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onSaveInstanceState() invoked!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onPause() invoked!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onStop() invoked!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onDestroyView() invoked!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onDestroy() invoked!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onDetach() invoked!!");
        try {
            Field field = Fragment.class.getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onHiddenChanged() invoked!!--" + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " setUserVisibleHint() invoked!!--" + isVisibleToUser);
    }

    protected abstract void findViewById();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    public abstract boolean onBackPressed();

    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    protected void startActivity(String action) {
        startActivity(action, null);
    }

    protected void startActivity(Class<?> cls, int flag) {
        startActivity(cls, flag, null);
    }

    protected void startActivity(Class<?> cls, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    protected void startActivityForResult(String action, int requestCode) {
        startActivityForResult(action, requestCode, null);
    }

    protected void startActivityForResult(String action, int requestCode, Bundle bundle) {
        startActivityForResult(action, null, null, requestCode, bundle);
    }

    protected void startActivityForResult(String action, Uri uri, int requestCode) {
        startActivityForResult(action, uri, null, requestCode, null);
    }

    protected void startActivityForResult(String action, String type, int requestCode, Bundle bundle) {
        startActivityForResult(action, null, type, requestCode, bundle);
    }

    protected void startActivityForResultWithParcelable(String action, int flags, HashMap<String, Parcelable> map, int requestCode) {
        startActivityForResultWithParcelable(action, null, flags, null, map, requestCode);
    }

    protected void startActivityForResultWithParcelable(String action, Uri uri, int flags, String type, HashMap<String, Parcelable> map, int requestCode) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        if (flags != Constant.FileProvider.DEFAULT_FLAG) {
            intent.addFlags(flags);
        }
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        if (uri != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(uri, type);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResultWithSerializable(String action, HashMap<String, Serializable> map, int requestCode) {
        startActivityForResultWithSerializable(action, null, null, map, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResultWithSerializable(String action, Uri uri, String type, HashMap<String, Serializable> map, int requestCode) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        if (uri != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(uri, type);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(String action, Uri uri, String type, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        if (uri != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(uri, type);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onFinish(String message) {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        LogUtil.getInstance().print("onFinish is called: " + message);
    }
}
