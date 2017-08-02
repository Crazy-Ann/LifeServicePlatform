package com.service.customer.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.customer.R;
import com.service.customer.base.sticky.holder.BaseHolder;
import com.service.customer.components.utils.ViewUtil;

public class ServiceHolder extends BaseHolder {

    public ImageView ivService;
    public TextView tvName;

    public ServiceHolder(View itemView) {
        super(itemView);
        ivService = ViewUtil.getInstance().findView(itemView, R.id.ivService);
        tvName = ViewUtil.getInstance().findView(itemView, R.id.tvName);
    }
}
