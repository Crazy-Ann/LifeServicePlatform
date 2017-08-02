package com.service.customer.base.sticky.listener;


import android.support.v7.widget.RecyclerView;

public interface OnViewBinderListener {

    void bind(RecyclerView.ViewHolder viewHolder, Object t, int position, boolean checkable);

    RecyclerView.ViewHolder getViewHolder();
}
