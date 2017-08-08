package com.service.customer.ui.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.net.entity.TaskImageInfo;
import com.service.customer.ui.holder.TaskImageHolder;

public class TaskImageBinder extends BaseViewBinder {

    private Context context;
    private RecyclerView recyclerView;

    public TaskImageBinder(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int position, boolean checkable) {
        TaskImageHolder taskImageHolder = (TaskImageHolder) viewHolder;
        TaskImageInfo taskImageInfo = (TaskImageInfo) o;
        GlideUtil.getInstance().with(context, taskImageInfo.getFile() != null ? taskImageInfo.getFile() : taskImageInfo.getResourceId(), context.getResources().getDrawable(R.mipmap.icon_default), context.getResources().getDrawable(R.mipmap.icon_default), DiskCacheStrategy.NONE, taskImageHolder.ivTaskImage);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new TaskImageHolder(LayoutInflater.from(context).inflate(R.layout.item_task_image, recyclerView, false));
    }
}
