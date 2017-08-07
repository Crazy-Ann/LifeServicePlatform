package com.service.customer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.service.customer.base.sticky.adapter.FixedStickyHeaderAdapter;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.net.entity.TaskImageInfo;
import com.service.customer.ui.holder.TaskImageHolder;

public class TaskImageAdapter extends FixedStickyHeaderAdapter<TaskImageInfo, TaskImageHolder> {

    public TaskImageAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(ctx, binder, groupable);
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {

    }
}
