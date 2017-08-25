package com.service.customer.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.service.customer.R;
import com.service.customer.base.sticky.holder.BaseHolder;
import com.service.customer.components.utils.ViewUtil;

public class NotificationAnnouncementHolder extends BaseHolder {

    public TextView tvTtile;
    public TextView tvDescreption;

    public NotificationAnnouncementHolder(View itemView) {
        super(itemView);
        tvTtile = ViewUtil.getInstance().findView(itemView, R.id.tvTtile);
        tvDescreption = ViewUtil.getInstance().findView(itemView, R.id.tvAddress);
    }
}
