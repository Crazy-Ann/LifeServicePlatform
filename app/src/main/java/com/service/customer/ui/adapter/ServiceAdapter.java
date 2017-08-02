package com.service.customer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.service.customer.base.sticky.adapter.FixedStickyHeaderAdapter;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.net.entity.ServiceInfo;
import com.service.customer.ui.holder.ServiceHolder;

public class ServiceAdapter extends FixedStickyHeaderAdapter<ServiceInfo, ServiceHolder> {

    public ServiceAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(ctx, binder, groupable);
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {

    }
}
