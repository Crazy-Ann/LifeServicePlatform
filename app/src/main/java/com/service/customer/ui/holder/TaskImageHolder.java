package com.service.customer.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.service.customer.R;
import com.service.customer.base.sticky.holder.BaseHolder;
import com.service.customer.components.utils.ViewUtil;

public class TaskImageHolder extends BaseHolder {

    public ImageView ivTaskImage;

    public TaskImageHolder(View itemView) {
        super(itemView);
        ivTaskImage = ViewUtil.getInstance().findView(itemView, R.id.ivTaskImage);
    }
}
