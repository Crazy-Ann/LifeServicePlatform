package com.service.customer.components.widget.lock.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.service.customer.components.R;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.utils.ViewUtil;

public class LockPatternThumbnailView extends View {

    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private Drawable mDrawable1;
    private Drawable mDrawable2;

    private String mLockParameter;

    public LockPatternThumbnailView(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public LockPatternThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(Constant.Lock.NODE_EDGE_RATIO / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mDrawable1 = getResources().getDrawableForDensity(
                R.mipmap.icon_point1,
                ViewUtil.getInstance().getDensityDpi(context));
        mDrawable2 = getResources().getDrawableForDensity(
                R.mipmap.icon_point2,
                ViewUtil.getInstance().getDensityDpi(context));
        if (mDrawable2 != null) {
            mWidth = mDrawable2.getIntrinsicWidth();
            mHeight = mDrawable2.getIntrinsicHeight();
            mDrawable1.setBounds(0, 0, mWidth, mHeight);
            mDrawable2.setBounds(0, 0, mWidth, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable1 == null || mDrawable2 == null) {
            return;
        }
        for (int i = 0; i < Constant.Lock.CIRCLE_COUNT; i++) {
            for (int j = 0; j < Constant.Lock.CIRCLE_COUNT; j++) {
                mPaint.setColor(-16777216);
                canvas.save();
                canvas.translate(j * mHeight + j * mHeight, i * mWidth + i * mWidth);
                if (!TextUtils.isEmpty(mLockParameter)) {
                    if (mLockParameter.indexOf(String.valueOf(Constant.Lock.CIRCLE_COUNT * i + (j))) == -1) {
                        mDrawable1.draw(canvas);
                    } else {
                        mDrawable2.draw(canvas);
                    }
                } else {
                    mDrawable1.draw(canvas);
                }
                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawable2 != null) {
            setMeasuredDimension(Constant.Lock.CIRCLE_COUNT * mHeight + mHeight
                    * (Constant.Lock.CIRCLE_COUNT - 1), Constant.Lock.CIRCLE_COUNT
                                         * mWidth + mWidth * (Constant.Lock.CIRCLE_COUNT - 1));
        }
    }

    public void setLockParameter(String str) {
        this.mLockParameter = str;
        invalidate();
    }
}
