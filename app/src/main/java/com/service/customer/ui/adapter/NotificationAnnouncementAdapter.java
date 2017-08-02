package com.service.customer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.service.customer.base.sticky.adapter.FixedStickyHeaderAdapter;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.net.entity.NotificationAnnouncementInfo;
import com.service.customer.ui.holder.NotificationAnnouncementHolder;

/**
 * Created by yjt on 2017/8/1.
 */

public class NotificationAnnouncementAdapter extends FixedStickyHeaderAdapter<NotificationAnnouncementInfo, NotificationAnnouncementHolder> {

    public NotificationAnnouncementAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(ctx, binder, groupable);
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {

    }
}
