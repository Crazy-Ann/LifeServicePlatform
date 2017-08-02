package com.service.customer.base.sticky.binder;

import android.support.v7.widget.RecyclerView;

import com.service.customer.base.sticky.listener.OnViewBinderListener;

public abstract class BaseViewBinder implements OnViewBinderListener {

    @Override
    public abstract void bind(RecyclerView.ViewHolder viewHolder, Object o, int position, boolean checkable);

    @Override
    public abstract RecyclerView.ViewHolder getViewHolder();
}
