package com.service.customer.ui.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.sticky.binder.BaseViewBinder;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.net.entity.ServiceInfo;
import com.service.customer.ui.holder.ServiceHolder;

public class ServiceBinder extends BaseViewBinder {

    private Context context;
    private RecyclerView recyclerView;

    public ServiceBinder(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int position, boolean checkable) {
        ServiceHolder holder = (ServiceHolder) viewHolder;
        ServiceInfo info = (ServiceInfo) o;
        GlideUtil.getInstance().with(context, info.getIconUrl(), context.getResources().getDrawable(R.mipmap.icon_default), context.getResources().getDrawable(R.mipmap.icon_default), DiskCacheStrategy.NONE, holder.ivService);
        holder.tvName.setText(info.getName());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new ServiceHolder(LayoutInflater.from(context).inflate(R.layout.item_service, recyclerView, false));
    }
}
