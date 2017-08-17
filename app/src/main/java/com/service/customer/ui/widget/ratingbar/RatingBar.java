package com.service.customer.ui.widget.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.service.customer.R;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.ui.widget.ratingbar.listeneer.OnRatingChangeListener;

public class RatingBar extends LinearLayout {

    private int count;
    private int selectedCount;
    private int stateDrawable;
    private float size;
    private float divider;
    private boolean editable;
    private boolean differentSize;

    private OnRatingChangeListener onRatingChangeListener;

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        count = typedArray.getInt(R.styleable.RatingBar_rb_count, 5);
        selectedCount = typedArray.getInt(R.styleable.RatingBar_rb_selectedCount, 0);
        editable = typedArray.getBoolean(R.styleable.RatingBar_rb_editable, false);
        differentSize = typedArray.getBoolean(R.styleable.RatingBar_rb_differentSize, false);
        size = typedArray.getDimension(R.styleable.RatingBar_rb_size, ViewUtil.getInstance().dp2px(context, 0));
        divider = typedArray.getDimension(R.styleable.RatingBar_rb_divider, ViewUtil.getInstance().dp2px(context, 0));
        stateDrawable = typedArray.getResourceId(R.styleable.RatingBar_rb_stateDrawable, -1);
        initializeView();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        initializeView();
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        if (selectedCount > count) {
            return;
        }
        this.selectedCount = selectedCount;
        initializeView();
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    private void initializeView() {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            CheckBox checkBox = new CheckBox(getContext());
            LayoutParams layoutParams;
            if (size == 0) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams((int) size, (int) size);
            }
            if (differentSize && count % 2 != 0) {
                int index = i;
                if (index > count / 2) {
                    index = count - 1 - index;
                }
                float scale = (index + 1) / (float) (count / 2 + 1);
                layoutParams.width = (int) (layoutParams.width * scale);
                layoutParams.height = layoutParams.width;
            }
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            if (i != 0 && i != count - 1) {
                layoutParams.leftMargin = (int) divider;
                layoutParams.rightMargin = (int) divider;
            } else if (i == 0) {
                layoutParams.rightMargin = (int) divider;
            } else if (i == count - 1) {
                layoutParams.leftMargin = (int) divider;
            }
            addView(checkBox, layoutParams);
            checkBox.setButtonDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            if (stateDrawable == -1) {
                stateDrawable = R.drawable.selector_rating_star;
            }
            checkBox.setBackgroundResource(stateDrawable);
            if (i + 1 <= selectedCount) {
                checkBox.setChecked(true);
            }
            checkBox.setEnabled(editable);
            final int position = i;
            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    selectedCount = position + 1;
                    for (int i = 0; i < count; i++) {
                        CheckBox checkBox = (CheckBox) getChildAt(i);
                        if (i <= position) {
                            checkBox.setChecked(true);
                        } else if (i > position) {
                            checkBox.setChecked(false);
                        }
                    }
                    if (onRatingChangeListener != null) {
                        onRatingChangeListener.onRatingChange(selectedCount);
                    }
                }
            });
        }
    }
}
