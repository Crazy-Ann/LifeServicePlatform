package com.service.customer.components.widget.lock.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.service.customer.components.constant.Constant;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.MessageUtil;
import com.service.customer.components.utils.StringUtil;
import com.service.customer.components.utils.ThreadPoolUtil;
import com.service.customer.components.widget.lock.listener.OnPointSelectedListener;
import com.service.customer.components.widget.lock.listener.implement.NoneHighLightMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LockPatternView extends View {

    private int mLengthPx;
    private int mCellLength;
    protected NodeDrawable[][] mDrawables;
    private Paint mPaint;

    private OnPointSelectedListener mOnPointSelectedListener;

    private boolean isDisplay;
    private boolean isExtension;
    private boolean isTactileFeedback;
    private boolean isOnce;
    private boolean isTouchEnable;

    private Point mTouchPoint;
    private Point mTouchCell;
    private int mTouchThresHold;

    private List<Point> mPoints;
    private Set<Point> mPointsPool;

    private Handler mHandler;
    private Vibrator mVibrator;

    // private Matrix mMatrix;

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mLengthPx = Constant.Lock.DEFAULT_LENGTH_PX;
        mDrawables = new NodeDrawable[0][0];
        mPaint = new Paint();
        mPaint.setColor(Constant.Lock.EDGE_COLOR);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mOnPointSelectedListener = new NoneHighLightMode();

        isDisplay = false;
        isExtension = false;

        mTouchPoint = new Point(-1, -1);
        mTouchCell = new Point(-1, -1);

        mVibrator = (Vibrator) getContext().getSystemService(
                Context.VIBRATOR_SERVICE);

        // mMatrix = new Matrix();
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    protected void clearMark(List<Point> pattern) {
        for (Point e : pattern) {
            mDrawables[e.x][e.y].setNodeState(Constant.Lock.STATE_UNSELECTED);
        }
    }

    private void loadResult(List<Point> result, OnPointSelectedListener listener) {
        for (int ii = 0; ii < result.size(); ii++) {
            Point e = result.get(ii);
            NodeDrawable node = mDrawables[e.x][e.y];
            node.setNodeState(listener.setOnPointSelectedListener(node, ii,
                    result.size(), e.x, e.y, Constant.Lock.DEFAULT_LENGTH_NODES));
            if (ii < result.size() - 1) {
                Point f = result.get(ii + 1);
                Point centerE = mDrawables[e.x][e.y].getPoint();
                Point centerF = mDrawables[f.x][f.y].getPoint();
                mDrawables[e.x][e.y].setAngle((float) Math.atan2(centerE.y
                        - centerF.y, centerE.x - centerF.x));
            }
        }
    }

    private void appendPattern(List<Point> result, Point node) {
        NodeDrawable nodeDraw = mDrawables[node.x][node.y];
        nodeDraw.setNodeState(Constant.Lock.STATE_SELECTED);
        if (result.size() > 0) {
            Point tailNode = result.get(result.size() - 1);
            NodeDrawable tailDraw = mDrawables[tailNode.x][tailNode.y];
            Point tailCenter = tailDraw.getPoint();
            Point nodeCenter = nodeDraw.getPoint();
            tailDraw.setAngle((float) Math.atan2(tailCenter.y - nodeCenter.y,
                    tailCenter.x - nodeCenter.x));
        }
        result.add(node);
    }

    private void handleResult() {
        isDisplay = true;
        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                loadResult(mPoints, mOnPointSelectedListener);
                if (isOnce) {
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Lock.PRACTICE_RESULT_ONCE, getResult(mPoints)));
                } else {
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Lock.PRACTICE_RESULT_TWICE, getResult(mPoints)));
                }
            }
        });
    }

    private String getResult(List<Point> points) {
        List<String> results = new ArrayList<>();
        for (Point point : points) {
//             results.add(point.toString());
            String[] indexs = point.toString().split(Regex.COMMA.getRegext());
            results.add(String.valueOf(Integer.parseInt(indexs[0]) + (Integer.parseInt(indexs[1]) * 2 + Integer.parseInt(indexs[1]))));
            LogUtil.getInstance().print(results);
        }
        return StringUtil.append(false, results);
    }

    public void resetOldResult() {
        clearMark(mPoints);
        mPoints.clear();
        mPointsPool.clear();
        isDisplay = false;
    }

    @SuppressLint("DrawAllocation")
    // @Override
    // protected void onDraw(Canvas canvas) {
    // drawView(canvas);
    // canvas.save();
    // mMatrix.preScale(Constant.Lock.WIDTH_ZOOM_SCALE, Constant.Lock.HEIGHT_ZOOM_SCALE);
    // canvas.setDensity(DensityUtil.getDensityDpi(getContext()));
    // mMatrix.postTranslate(canvas.getWidth()
    // * Constant.Lock.WIDTH_TRANSLATE_SCALE, canvas.getHeight()
    // * Constant.Lock.HEIGHT_TRANSLATE_SCALE);
    // canvas.setMatrix(mMatrix);
    // drawView(canvas);
    // canvas.restore();
    // mMatrix.reset();
    // }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        // draw pattern edges first
        Point edgeStart, edgeEnd;
        Iterator<Point> iterator = new CenterIterator(mPoints.iterator());

        if (iterator.hasNext()) {
            edgeStart = iterator.next();
            while (iterator.hasNext()) {
                edgeEnd = iterator.next();
                canvas.drawLine(edgeStart.x, edgeStart.y, edgeEnd.x, edgeEnd.y,
                        mPaint);
                edgeStart = edgeEnd;
            }
            if (isExtension) {
                canvas.drawLine(edgeStart.x, edgeStart.y, mTouchPoint.x,
                        mTouchPoint.y, mPaint);
            }
        }

        // then draw nodes
        for (int y = 0; y < Constant.Lock.DEFAULT_LENGTH_NODES; y++) {
            for (int x = 0; x < Constant.Lock.DEFAULT_LENGTH_NODES; x++) {
                mDrawables[x][y].draw(canvas);
            }
        }
    }

    // private int getMeasuredInfo(int measureSpec) {
    // int result = 0;
    // int size = MeasureSpec.getSize(measureSpec);
    // switch (MeasureSpec.getMode(measureSpec)) {
    // case MeasureSpec.UNSPECIFIED:
    // result = Constant.Lock.DEFAULT_LENGTH_PX;
    // break;
    // case MeasureSpec.AT_MOST:
    // result = Math.max(size, Constant.Lock.DEFAULT_LENGTH_PX);
    // break;
    // case MeasureSpec.EXACTLY:
    // default:
    // result = size;
    // }
    // return result;
    // }

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // setMeasuredDimension(getMeasuredInfo(widthMeasureSpec),
    // getMeasuredInfo(heightMeasureSpec));
    // }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int length;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.UNSPECIFIED
                && hMode == MeasureSpec.UNSPECIFIED) {
            length = Constant.Lock.DEFAULT_LENGTH_PX;
            setMeasuredDimension(length, length);
        } else if (wMode == MeasureSpec.UNSPECIFIED) {
            length = height;
        } else if (hMode == MeasureSpec.UNSPECIFIED) {
            length = width;
        } else {
            length = Math.min(width, height);
        }

        setMeasuredDimension(length, length);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchEnable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isExtension = true;
                    if (isDisplay) {
                        resetOldResult();
                    }
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();
                    float y = event.getY();
                    mTouchPoint.x = (int) x;
                    mTouchPoint.y = (int) y;
                    mTouchCell.x = (int) x / mCellLength;
                    mTouchCell.y = (int) y / mCellLength;
                    if (mTouchCell.x < 0
                            || mTouchCell.x >= Constant.Lock.DEFAULT_LENGTH_NODES
                            || mTouchCell.y < 0
                            || mTouchCell.y >= Constant.Lock.DEFAULT_LENGTH_NODES) {
                        break;
                    }
                    Point nearestCenter = mDrawables[mTouchCell.x][mTouchCell.y]
                            .getPoint();
                    int dist = (int) Math.sqrt(Math.pow(x - nearestCenter.x, 2)
                            + Math.pow(y - nearestCenter.y, 2));
                    if (dist < mTouchThresHold && !mPointsPool.contains(mTouchCell)) {
                        if (isTactileFeedback) {
                            mVibrator.vibrate(Constant.Lock.TACTILE_FEEDBACK_DURATION);
                        }
                        Point newPoint = new Point(mTouchCell);
                        if (mPoints.size() > 0) {
                            Point tail = mPoints.get(mPoints.size() - 1);
                            Point delta = new Point(newPoint.x - tail.x, newPoint.y
                                    - tail.y);
                            int gcd = Math.abs(PatternGenerator.computeGcd(delta.x,
                                    delta.y));
                            if (gcd > 1) {
                                for (int ii = 1; ii < gcd; ii++) {
                                    Point inside = new Point(tail.x + delta.x / gcd
                                            * ii, tail.y + delta.y / gcd * ii);
                                    if (!mPointsPool.contains(inside)) {
                                        appendPattern(mPoints, inside);
                                        mPointsPool.add(inside);
                                    }
                                }
                            }
                        }

                        appendPattern(mPoints, newPoint);
                        mPointsPool.add(newPoint);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isExtension = false;
                    handleResult();
                    break;
                default:
                    return super.onTouchEvent(event);
            }
            invalidate();
        }
        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mLengthPx = Math.min(w, h);
        mCellLength = mLengthPx / Constant.Lock.DEFAULT_LENGTH_NODES;
        mDrawables = new NodeDrawable[Constant.Lock.DEFAULT_LENGTH_NODES][Constant.Lock.DEFAULT_LENGTH_NODES];
        // float nodeDiameter = ((float) mCellLength) *
        // Constant.Lock.CELL_NODE_RATIO;
        // mPaint.setStrokeWidth(nodeDiameter * Constant.Lock.NODE_EDGE_RATIO);
        float diameter = (float) (mCellLength * Constant.Lock.CELL_NODE_RATIO / 1.25);
        // mPaint.setStrokeWidth(diameter * Constant.Lock.NODE_EDGE_RATIO / 2);
        mPaint.setStrokeWidth(5);
        mTouchThresHold = (int) (diameter / 2);

        for (int y = 0; y < Constant.Lock.DEFAULT_LENGTH_NODES; y++) {
            for (int x = 0; x < Constant.Lock.DEFAULT_LENGTH_NODES; x++) {
                Point center = new Point(x * mCellLength + mCellLength / 2, y
                        * mCellLength + mCellLength / 2);
                mDrawables[x][y] = new NodeDrawable(diameter, center);
            }
        }

        if (!isOnce) {
            loadResult(mPoints, mOnPointSelectedListener);
        }
    }

    public OnPointSelectedListener getListener() {
        return mOnPointSelectedListener;
    }

    public void setResultMode(boolean isOnce) {
        isDisplay = false;
        this.isOnce = isOnce;
        mPoints = new ArrayList<>();
        mPointsPool = new HashSet<>();
    }

    public void setTactileFeedbackEnabled(boolean enabled) {
        isTactileFeedback = enabled;
    }

    public void setTouchEnabled(boolean enabled) {
        isTouchEnable = enabled;
    }

    public boolean getTactileFeedbackEnabled() {
        return isTactileFeedback;
    }

    private class CenterIterator implements Iterator<Point> {
        private Iterator<Point> mIterator;

        public CenterIterator(Iterator<Point> iterator) {
            mIterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return mIterator.hasNext();
        }

        @Override
        public Point next() {
            Point node = mIterator.next();
            return mDrawables[node.x][node.y].getPoint();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
