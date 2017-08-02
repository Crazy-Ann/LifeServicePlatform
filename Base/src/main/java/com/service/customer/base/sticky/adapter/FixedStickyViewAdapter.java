package com.service.customer.base.sticky.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.service.customer.base.sticky.binder.BaseViewBinder;

import java.util.ArrayList;
import java.util.List;

public abstract class FixedStickyViewAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {


    public static final int TYPE_HEADER_VIEW = 0x5001;
    public static final int TYPE_CONTENT_VIEW = 0x5003;
    public static final int TYPE_FOOTER_VIEW = 0x5004;
    public static final int NOTIFY_TIP_UNAVAILABLE = -1;
    protected int mNotifyTip;

    protected ArrayList<FixedStickyView> mHeaderViews = new ArrayList<>();
    protected ArrayList<FixedStickyView> mFooterViews = new ArrayList<>();

    private SparseArray<FixedViewHoldGenerator> mGenerators = new SparseArray<>();
    private BaseViewBinder mViewBinder;

    protected List<T> mItems = new ArrayList<>();
    protected OnItemClickListener mItemClickListener;
    protected OnErrorClickListener mErrorClickListener;

    public FixedStickyViewAdapter(BaseViewBinder binder) {
        this.mViewBinder = binder;
        mNotifyTip = NOTIFY_TIP_UNAVAILABLE;
    }

    public FixedStickyViewAdapter setData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mItems.clear();
            mItems.addAll(datas);
            notifyDataSetChanged();
        }
        return this;
    }

    public void addItems(Activity activity, List<T> items) {
        mItems.addAll(items);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void clear() {
        mItems.clear();
        mHeaderViews.clear();
        mFooterViews.clear();
        mGenerators.clear();
        notifyDataSetChanged();
    }

    public void bindDataToHeaderOrFooter(int id, Object object, int viewType) {
        List<FixedStickyView> views = null;
        if (viewType == TYPE_HEADER_VIEW) {
            views = mHeaderViews;
        } else if (viewType == TYPE_FOOTER_VIEW) {
            views = mFooterViews;
        }
        if (views != null && views.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                FixedStickyView view = views.get(i);
                if (view.mId == id) {
                    view.mObject = object;
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void addFooterView(int id, int layoutId, int viewType, int fixedStickyViewType, FixedViewHoldGenerator generator) {
        final FixedStickyView info = new FixedStickyView();
        info.mId = id;
        info.mObject = null;
        info.mLayoutId = layoutId;
        info.mViewType = viewType;
        info.mFixedStickyViewType = fixedStickyViewType;
        if (mGenerators.get(fixedStickyViewType) == null) {
            mGenerators.append(fixedStickyViewType, generator);
        }
        mFooterViews.add(info);
        notifyDataSetChanged();
    }

    public boolean hasFooterView(int id) {
        for (int i = 0; i < mFooterViews.size(); i++) {
            if (mFooterViews.get(i).mId == id) {
                return true;
            }
        }
        return false;
    }

    public Object getItem(int position) {
        if (position < mHeaderViews.size()) {
            return mHeaderViews.get(position);
        }
        if (position >= mHeaderViews.size() && position < mHeaderViews.size() + mItems.size()) {
            return mItems.get(position - mHeaderViews.size());
        }
        return mFooterViews.get(position - mHeaderViews.size() - mItems.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CONTENT_VIEW:
                return mViewBinder.getViewHolder();
            default:
                return mGenerators.get(viewType).generate();
        }
    }

    public void setNotifyTip(int notifyTip) {
        this.mNotifyTip = notifyTip;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object obj = getItem(position);
        if (obj instanceof FixedStickyView) {
            onBindHeaderOrFooter(holder, obj);
        } else {
            mViewBinder.bind(holder, obj, position, mNotifyTip == position);
        }
        final int itemPosition = position;
        if (holder.getItemViewType() == TYPE_CONTENT_VIEW) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        FixedStickyView view;
        if (position < mHeaderViews.size()) {
            view = mHeaderViews.get(position);
            return view.mFixedStickyViewType;
        } else if ((position >= mHeaderViews.size() && position < mHeaderViews.size() + mItems.size())) {
            return TYPE_CONTENT_VIEW;
        } else {
            view = mFooterViews.get(position - mHeaderViews.size() - mItems.size());
            return view.mFixedStickyViewType;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size() + mHeaderViews.size() + mFooterViews.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnErrorClickListener {
        void onErrorClick();
    }

    public void setOnErrorClickListener(OnErrorClickListener listener) {
        this.mErrorClickListener = listener;
    }

    protected abstract void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object);

    public static class FixedStickyView {
        public int mId;
        public int mViewType;
        public int mFixedStickyViewType;
        public int mLayoutId;
        public Object mObject;
    }

    public static class FixedViewHoldGenerator {
        public RecyclerView.ViewHolder generate() {
            return null;
        }
    }
}
