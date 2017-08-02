package com.service.customer.components.widget.lock.view;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.service.customer.components.constant.Constant;


public class NodeDrawable extends Drawable {

    protected float mArrowTipRad;
    protected float mArrowBaseRad;
    protected float mArrowHalfBase;

    protected ShapeDrawable mCircles[];

    protected Paint mPaint;
    protected Path mPath;
    protected float mAngle;

    protected Point mPoint;
    protected float mDiameter;
    protected int mState;
    protected int mColor;

    public NodeDrawable(float diameter, Point point) {
        mCircles = new ShapeDrawable[Constant.Lock.CIRCLE_COUNT];

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mAngle = Float.NaN;

        mPoint = point;
        mDiameter = diameter;
        mState = Constant.Lock.STATE_UNSELECTED;

        setColor(Constant.Lock.DEFAULT_STATE_COLORS[Constant.Lock.STATE_CUSTOM]);

        buildShapes(diameter, mPoint);
    }

    @Override
    public void draw(Canvas canvas) {
        for (int ii = 0; ii < Constant.Lock.CIRCLE_COUNT; ii++) {
            mCircles[Constant.Lock.CIRCLE_ORDER[ii]].draw(canvas);
        }
        if (!Float.isNaN(mAngle)) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void buildShapes(float outerDiameter, Point center) {
        for (int ii = 0; ii < Constant.Lock.CIRCLE_COUNT; ii++) {
            mCircles[ii] = new ShapeDrawable(new OvalShape());
            Paint circlePaint = mCircles[ii].getPaint();
            circlePaint.setColor(Constant.Lock.DEFAULT_CIRCLE_COLORS[ii]);
            circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

            float diameter = outerDiameter * Constant.Lock.CIRCLE_RATIOS[ii];
            // int offset = (int) (diameter / 2.0f);
            int offset = (int) (diameter / 1.5f);

            mCircles[ii].setBounds(center.x - offset, center.y - offset,
                                   center.x + offset, center.y + offset);
        }

        float middleDiameter = outerDiameter
                * Constant.Lock.CIRCLE_RATIOS[Constant.Lock.CIRCLE_MIDDLE];

        mArrowTipRad = middleDiameter / 2.0f * 0.9f;
        mArrowBaseRad = middleDiameter / 2.0f * 0.6f;
        mArrowHalfBase = middleDiameter / 2.0f * 0.3f;
    }

    public void setNodeState(int state) {
        int color = mColor;
        if (state != Constant.Lock.STATE_CUSTOM) {
            color = Constant.Lock.DEFAULT_STATE_COLORS[state];
        }
        mCircles[Constant.Lock.CIRCLE_OUTER].getPaint().setColor(color);
        mPaint.setColor(color);
        if (state == Constant.Lock.STATE_UNSELECTED) {
            setAngle(Float.NaN);
        }
        mState = state;
    }

    public int getNodeState() {
        return mState;
    }

    public void setAngle(float angle) {
        if (!Float.isNaN(angle)) {
            float tipX = mPoint.x - ((float) Math.cos(angle)) * mArrowTipRad;
            float tipY = mPoint.y - ((float) Math.sin(angle)) * mArrowTipRad;

            float baseCenterX = mPoint.x - ((float) Math.cos(angle))
                    * mArrowBaseRad;
            float baseCenterY = mPoint.y - ((float) Math.sin(angle))
                    * mArrowBaseRad;

            // first base vertex of arrow
            float baseVertAX = baseCenterX - mArrowHalfBase
                    * ((float) Math.cos(angle + Math.PI / 2));
            float baseVertAY = baseCenterY - mArrowHalfBase
                    * ((float) Math.sin(angle + Math.PI / 2));
            // second base vertex of arrow
            float baseVertBX = baseCenterX - mArrowHalfBase
                    * ((float) Math.cos(angle - Math.PI / 2));
            float baseVertBY = baseCenterY - mArrowHalfBase
                    * ((float) Math.sin(angle - Math.PI / 2));

            Path arrow = new Path();
            arrow.moveTo(tipX, tipY);
            arrow.lineTo(baseVertAX, baseVertAY);
            arrow.lineTo(baseVertBX, baseVertBY);
            arrow.lineTo(tipX, tipY);

            mPath = arrow;
        }
        mAngle = angle;
    }

    public float getAngle() {
        return mAngle;
    }

    public Point getPoint() {
        return mPoint;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    @Override
    public int getOpacity() {
        return mCircles[Constant.Lock.CIRCLE_OUTER].getOpacity();
    }

    @Override
    public void setAlpha(int alpha) {
        for (int ii = 0; ii < Constant.Lock.CIRCLE_COUNT; ii++) {
            mCircles[ii].setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        for (int ii = 0; ii < Constant.Lock.CIRCLE_COUNT; ii++) {
            mCircles[ii].setColorFilter(cf);
        }
    }
}
