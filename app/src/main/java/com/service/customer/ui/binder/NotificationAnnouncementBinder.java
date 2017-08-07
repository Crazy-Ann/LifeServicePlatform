package com.service.customer.ui.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.service.customer.R;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.net.entity.NotificationAnnouncementInfo;
import com.service.customer.ui.holder.NotificationAnnouncementHolder;

public class NotificationAnnouncementBinder extends BaseViewBinder {

    private Context context;
    private RecyclerView recyclerView;

    public NotificationAnnouncementBinder(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int position, boolean checkable) {
        NotificationAnnouncementHolder notificationAnnouncementHolder = (NotificationAnnouncementHolder) viewHolder;
        NotificationAnnouncementInfo notificationAnnouncementInfo = (NotificationAnnouncementInfo) o;
        notificationAnnouncementHolder.tvTtile.setText(notificationAnnouncementInfo.getTitle());
        notificationAnnouncementHolder.tvDescreption.setText(notificationAnnouncementInfo.getDescreption());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new NotificationAnnouncementHolder(LayoutInflater.from(context).inflate(R.layout.item_notification_announcement, recyclerView, false));
    }
}
