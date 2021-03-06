package com.service.customer.components.widget.sticky.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;

public class GridLayoutDividerItemDecoration extends RecyclerView.ItemDecoration {

    private SparseArray<ItemDecorationProps> decorationPropss; // itemType -> prop

    public GridLayoutDividerItemDecoration(SparseArray<ItemDecorationProps> decorationPropss) {
        this.decorationPropss = decorationPropss;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        int itemType = adapter.getItemViewType(position);

        ItemDecorationProps props;
        if (decorationPropss != null) {
            props = decorationPropss.get(itemType);
        } else {
            return;
        }
        if (props == null) {
            return;
        }
        int spanIndex = 0;
        int spanSize = 1;
        int spanCount = 1;
        int orientation = OrientationHelper.VERTICAL;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            spanIndex = lp.getSpanIndex();
            spanSize = lp.getSpanSize();
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            spanCount = layoutManager.getSpanCount(); // Assume that there're spanCount items in this row/column.
            orientation = layoutManager.getOrientation();
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            spanIndex = lp.getSpanIndex();
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            spanCount = layoutManager.getSpanCount(); // Assume that there're spanCount items in this row/column.
            spanSize = lp.isFullSpan() ? spanCount : 1;
            orientation = layoutManager.getOrientation();
        }

        boolean isFirstRowOrColumn, isLastRowOrColumn;
        int prePos = position > 0 ? position - 1 : -1;
        int nextPos = position < adapter.getItemCount() - 1 ? position + 1 : -1;
        // Last position on the last row 上一行的最后一个位置
        int preRowPos = position > spanIndex ? position - (1 + spanIndex) : -1;
        // First position on the next row 下一行的第一个位置
        int nextRowPos = position < adapter.getItemCount() - (spanCount - spanIndex) ? position + (spanCount - spanIndex) : -1;
        isFirstRowOrColumn = position == 0 || prePos == -1 || itemType != adapter.getItemViewType(prePos) ||
                preRowPos == -1 || itemType != adapter.getItemViewType(preRowPos);
        isLastRowOrColumn = position == adapter.getItemCount() - 1 || nextPos == -1 || itemType != adapter.getItemViewType(nextPos) ||
                nextRowPos == -1 || itemType != adapter.getItemViewType(nextRowPos);

        int left = 0, top = 0, right = 0, bottom = 0;

        if (orientation == GridLayoutManager.VERTICAL) {
            if (props.hasVerticalEdge()) {
                left = props.getVerticalSpace() * (spanCount - spanIndex) / spanCount;
                right = props.getVerticalSpace() * (spanIndex + (spanSize - 1) + 1) / spanCount;
            } else {
                left = props.getVerticalSpace() * spanIndex / spanCount;
                right = props.getVerticalSpace() * (spanCount - (spanIndex + spanSize - 1) - 1) / spanCount;
            }
            if (isFirstRowOrColumn) { // First row
                if (props.hasHorizontalEdge()) {
                    top = props.getHorizontalSpace();
                }
            }
            if (isLastRowOrColumn) { // Last row
                if (props.hasHorizontalEdge()) {
                    bottom = props.getHorizontalSpace();
                }
            } else {
                bottom = props.getHorizontalSpace();
            }
        } else {
            if (props.hasHorizontalEdge()) {
                top = props.getHorizontalSpace() * (spanCount - spanIndex) / spanCount;
                bottom = props.getHorizontalSpace() * (spanIndex + (spanSize - 1) + 1) / spanCount;
            } else {
                top = props.getHorizontalSpace() * spanIndex / spanCount;
                bottom = props.getHorizontalSpace() * (spanCount - (spanIndex + spanSize - 1) - 1) / spanCount;
            }
            if (isFirstRowOrColumn) { // First column
                if (props.hasVerticalEdge()) {
                    left = props.getVerticalSpace();
                }
            }
            if (isLastRowOrColumn) { // Last column
                if (props.hasVerticalEdge()) {
                    right = props.getVerticalSpace();
                }
            } else {
                right = props.getVerticalSpace();
            }
        }
        outRect.set(left, top, right, bottom);
    }

    public static class ItemDecorationProps {
        private int verticalSpace;
        private int horizontalSpace;
        private boolean hasVerticalEdge;
        private boolean hasHorizontalEdge;

        public ItemDecorationProps(int horizontalSpace, int verticalSpace, boolean hasHorizontalEdge, boolean hasVerticalEdge) {
            this.verticalSpace = verticalSpace;
            this.horizontalSpace = horizontalSpace;
            this.hasHorizontalEdge = hasHorizontalEdge;
            this.hasVerticalEdge = hasVerticalEdge;
        }

        public int getHorizontalSpace() {
            return this.horizontalSpace;
        }

        public int getVerticalSpace() {
            return this.verticalSpace;
        }

        public boolean hasVerticalEdge() {
            return hasVerticalEdge;
        }

        public boolean hasHorizontalEdge() {
            return hasHorizontalEdge;
        }
    }
}
