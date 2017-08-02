package com.service.customer.components.widget.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.service.customer.components.R;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.constant.ProgressBarState;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.widget.progressbar.listener.OnProgressUpdateListener;

public class DownloadProgressBar extends View {

    private Paint mCirclePaint;
    private Paint mDrawingPaint;
    private Paint mProgressPaint;
    private Paint mProgressBackgroundPaint;

    private float mRadius;
    private float mStrokeWidth;
    private float mLineWidth;
    private float mLengthFix;
    private float mArrowLineToDotAnimatedValue;
    private float mArrowLineToHorizontalLineAnimatedValue;
    private float mDotToProgressAnimatedValue;
    private float mCurrentGlobalProgressValue;
    private float mSuccessValue;
    private float mExpandCollapseValue;
    private float mErrorValue;
    private float mOvershootValue;

    private float mCenterX;
    private float mCenterY;
    private float mPaddingX;
    private float mPaddingY;

    private int mCircleBackgroundColor;
    private int mDrawingColor;
    private int mProgressBackgroundColor;
    private int mProgressColor;
    private int mProgressDuration;
    private int mResultDuration;

    private AnimatorSet mArrowToLineAnimatorSet;
    private AnimatorSet mProgressAnimationSet;

    private OvershootInterpolator mOvershootInterpolator;

    private ValueAnimator mDotToProgressAnimation;
    private ValueAnimator mProgressAnimation;
    private ValueAnimator mSuccessAnimation;
    private ValueAnimator mExpandAnimation;
    private ValueAnimator mCollapseAnimation;
    private ValueAnimator mErrorAnimation;
    private ValueAnimator mArrowLineToDot;
    private ValueAnimator mArrowLineToHorizontalLine;
    private ValueAnimator mManualProgressAnimation;

    private RectF mCircleBounds;
    private RectF mProgressBackgroundBounds = new RectF();
    private RectF mProgressBounds = new RectF();

    private OnProgressUpdateListener mOnProgressUpdateListener;
    private AnimatorSet mManualProgressAnimationSet;
    private float mFromArc = 0;
    private float mToArc = 0;
    private float mCurrentGlobalManualProgressValue;

    private ProgressBarState mProgressBarState;
    private ProgressBarState mResultProgressBarState;
    private ProgressBarState mWhichProgress;

    public DownloadProgressBar(Context context) {
        super(context);
    }

    public DownloadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DownloadProgressBar, 0, 0);
        try {
            mRadius = array.getDimension(R.styleable.DownloadProgressBar_circleRadius, 0);
            mStrokeWidth = array.getDimension(R.styleable.DownloadProgressBar_strokeWidth, 0);
            mLineWidth = array.getDimension(R.styleable.DownloadProgressBar_lineWidth, 0);
            mLengthFix = (float) (mLineWidth / (2 * Math.sqrt(2)));
            mProgressDuration = array.getInteger(R.styleable.DownloadProgressBar_progressDuration, Constant.ProgressBar.DEFAULT_PROGRESS_DURATION);
            mResultDuration = array.getInteger(R.styleable.DownloadProgressBar_resultDuration, Constant.ProgressBar.DEFAULT_RESULT_DURATION);
            mProgressBackgroundColor = array.getColor(R.styleable.DownloadProgressBar_progressBackgroundColor, 0);
            mDrawingColor = array.getColor(R.styleable.DownloadProgressBar_drawingColor, 0);
            mProgressColor = array.getColor(R.styleable.DownloadProgressBar_progressColor, 0);
            mCircleBackgroundColor = array.getColor(R.styleable.DownloadProgressBar_circleBackgroundColor, 0);
            mOvershootValue = array.getFloat(R.styleable.DownloadProgressBar_overshootValue, Constant.ProgressBar.DEFAULT_OVERSHOOT_VALUE);
        } finally {
            array.recycle();
        }

        mCirclePaint = new Paint();
        mCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mCircleBackgroundColor);
        mCirclePaint.setStrokeWidth(mStrokeWidth);

        mDrawingPaint = new Paint();
        mDrawingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDrawingPaint.setStyle(Paint.Style.STROKE);
        mDrawingPaint.setColor(mDrawingColor);
        mDrawingPaint.setStrokeWidth(mLineWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);

        mProgressBackgroundPaint = new Paint();
        mProgressBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        mProgressBackgroundPaint.setStyle(Paint.Style.FILL);

        mProgressBarState = ProgressBarState.IDLE;
        setupAnimations();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        mPaddingX = w / 2f - mRadius;
        mPaddingY = h / 2f - mRadius;

        mCircleBounds = new RectF();
        mCircleBounds.top = mPaddingY;
        mCircleBounds.left = mPaddingX;
        mCircleBounds.bottom = h / 2f + mRadius;
        mCircleBounds.right = w / 2f + mRadius;
    }

    private void setupAnimations() {
        mOvershootInterpolator = new OvershootInterpolator(mOvershootValue);
        mArrowLineToDot = ValueAnimator.ofFloat(0, mRadius / 4);
        mArrowLineToDot.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArrowLineToDotAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mArrowLineToDot.setDuration(200);
        mArrowLineToDot.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                mProgressBarState = ProgressBarState.ANIMATING_LINE_TO_DOT;
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onAnimationStarted();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mArrowLineToDot.setInterpolator(new AccelerateInterpolator());

        mArrowLineToHorizontalLine = ValueAnimator.ofFloat(0, mRadius / 2);
        mArrowLineToHorizontalLine.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArrowLineToHorizontalLineAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mArrowLineToHorizontalLine.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mArrowLineToHorizontalLine.setDuration(600);
        mArrowLineToHorizontalLine.setStartDelay(400);
        mArrowLineToHorizontalLine.setInterpolator(mOvershootInterpolator);

        mDotToProgressAnimation = ValueAnimator.ofFloat(0, mRadius);
        mDotToProgressAnimation.setDuration(600);
        mDotToProgressAnimation.setStartDelay(600);
        mDotToProgressAnimation.setInterpolator(mOvershootInterpolator);
        mDotToProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mDotToProgressAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mDotToProgressAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mWhichProgress == ProgressBarState.ANIMATING_PROGRESS)
                    mProgressAnimationSet.start();
                else if (mWhichProgress == ProgressBarState.ANIMATING_MANUAL_PROGRESS)
                    mManualProgressAnimationSet.start();

                mProgressBarState = mWhichProgress;

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mArrowToLineAnimatorSet = new AnimatorSet();
        mArrowToLineAnimatorSet.playTogether(mArrowLineToDot, mArrowLineToHorizontalLine, mDotToProgressAnimation);

        mProgressAnimation = ValueAnimator.ofFloat(0, 360f);
        mProgressAnimation.setStartDelay(500);
        mProgressAnimation.setInterpolator(new LinearInterpolator());
        mProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentGlobalProgressValue = (float) valueAnimator.getAnimatedValue();
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onProgressUpdate(mCurrentGlobalProgressValue);
                }
                invalidate();
            }
        });
        mProgressAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                mDotToProgressAnimatedValue = 0;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mProgressAnimation.setDuration(mProgressDuration);

        mManualProgressAnimation = ValueAnimator.ofFloat(mFromArc, mToArc);
        mManualProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentGlobalManualProgressValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mManualProgressAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onManualProgressStarted();
                }
                mDotToProgressAnimatedValue = 0;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onManualProgressEnded();
                }
                if (mToArc > 359) {
                    mCollapseAnimation.start();
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        mExpandAnimation = ValueAnimator.ofFloat(0, mRadius / 6);
        mExpandAnimation.setDuration(300);
        mExpandAnimation.setInterpolator(new DecelerateInterpolator());
        mExpandAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mExpandCollapseValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mCollapseAnimation = ValueAnimator.ofFloat(mRadius / 6, mStrokeWidth / 2);
        mCollapseAnimation.setDuration(300);
        mCollapseAnimation.setStartDelay(300);
        mCollapseAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mProgressBarState == ProgressBarState.ANIMATING_MANUAL_PROGRESS) {
                    if (mResultProgressBarState == ProgressBarState.ANIMATING_FAILED) {
                        mErrorAnimation.start();
                    } else if (mResultProgressBarState == ProgressBarState.ANIMATING_SUCCESS) {
                        mSuccessAnimation.start();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mCollapseAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mCollapseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mExpandCollapseValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mManualProgressAnimationSet = new AnimatorSet();
        mManualProgressAnimationSet.playSequentially(mExpandAnimation, mManualProgressAnimation);

        mProgressAnimationSet = new AnimatorSet();
        mProgressAnimationSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mResultProgressBarState == ProgressBarState.ANIMATING_FAILED) {
                    mErrorAnimation.start();
                } else if (mResultProgressBarState == ProgressBarState.ANIMATING_SUCCESS) {
                    mSuccessAnimation.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mProgressAnimationSet.playSequentially(mExpandAnimation, mProgressAnimation, mCollapseAnimation);

        mErrorAnimation = ValueAnimator.ofFloat(0, mRadius / 4);
        mErrorAnimation.setDuration(600);
        mErrorAnimation.setStartDelay(500);
        mErrorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mErrorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mErrorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mErrorAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                mProgressBarState = ProgressBarState.ANIMATING_FAILED;
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onAnimationFailed();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnProgressUpdateListener != null) {
                            mOnProgressUpdateListener.onAnimationEnded();
                        }
                        mProgressBarState = ProgressBarState.IDLE;
                        resetValues();
                        invalidate();
                    }
                }, mResultDuration);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mSuccessAnimation = ValueAnimator.ofFloat(0, mRadius / 4);
        mSuccessAnimation.setDuration(600);
        mSuccessAnimation.setStartDelay(500);
        mSuccessAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mSuccessAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSuccessValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mSuccessAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                mProgressBarState = ProgressBarState.ANIMATING_SUCCESS;
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.onAnimationSuccess();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnProgressUpdateListener != null) {
                            mOnProgressUpdateListener.onAnimationEnded();
                        }
                        mProgressBarState = ProgressBarState.IDLE;
                        resetValues();
                        invalidate();
                    }
                }, mResultDuration);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void resetValues() {
        mArrowLineToDotAnimatedValue = 0;
        mArrowLineToHorizontalLineAnimatedValue = 0;
        mCurrentGlobalProgressValue = 0;
        mCurrentGlobalManualProgressValue = 0;
        mManualProgressAnimation.setFloatValues(0, 0);
        mToArc = 0;
        mFromArc = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint);
        switch (mProgressBarState) {
            case IDLE:
                canvas.drawLine(mCenterX, mCenterY - mRadius / 2, mCenterX, mCenterY + mRadius / 2, mDrawingPaint);
                canvas.drawLine(mCenterX - mRadius / 2, mCenterY, mCenterX + mLengthFix, mCenterY + mRadius / 2 + mLengthFix, mDrawingPaint);
                canvas.drawLine(mCenterX - mLengthFix, mCenterY + mRadius / 2 + mLengthFix, mCenterX + mRadius / 2, mCenterY, mDrawingPaint);
                break;
            case ANIMATING_LINE_TO_DOT:
                if (!mDotToProgressAnimation.isRunning()) {
                    canvas.drawLine(
                            mCenterX,
                            mCenterY - mRadius / 2 + mArrowLineToDotAnimatedValue * 2 - mStrokeWidth / 2,
                            mCenterX,
                            mCenterY + mRadius / 2 - mArrowLineToDotAnimatedValue * 2 + mStrokeWidth / 2,
                            mDrawingPaint
                    );
                }
                canvas.drawLine(
                        mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2,
                        mCenterY,
                        mCenterX + mLengthFix,
                        mCenterY + mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue + mLengthFix,
                        mDrawingPaint
                );
                canvas.drawLine(
                        mCenterX - mLengthFix,
                        mCenterY + mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue + mLengthFix,
                        mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2,
                        mCenterY,
                        mDrawingPaint
                );
                break;
            case ANIMATING_PROGRESS:
                float progress = ((mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2) - (mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2)) / 360f;

                mDrawingPaint.setStrokeWidth(mStrokeWidth);
                canvas.drawArc(mCircleBounds, -90, mCurrentGlobalProgressValue, false, mDrawingPaint);

                mProgressBackgroundBounds.left = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBackgroundBounds.top = mCenterY - mExpandCollapseValue;
                mProgressBackgroundBounds.right = mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBackgroundBounds.bottom = mCenterY + mExpandCollapseValue;
                canvas.drawRoundRect(mProgressBackgroundBounds, 45, 45, mProgressBackgroundPaint);

                mProgressBounds.left = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBounds.top = mCenterY - mExpandCollapseValue;
                mProgressBounds.right = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2 + progress * mCurrentGlobalProgressValue;
                mProgressBounds.bottom = mCenterY + mExpandCollapseValue;
                canvas.drawRoundRect(mProgressBounds, 45, 45, mProgressPaint);
                break;
            case ANIMATING_MANUAL_PROGRESS:
                float manualProgress = ((mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2) - (mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2)) / 360f;

                mDrawingPaint.setStrokeWidth(mStrokeWidth);
                canvas.drawArc(mCircleBounds, -90, mCurrentGlobalManualProgressValue, false, mDrawingPaint);

                mProgressBackgroundBounds.left = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBackgroundBounds.top = mCenterY - mExpandCollapseValue;
                mProgressBackgroundBounds.right = mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBackgroundBounds.bottom = mCenterY + mExpandCollapseValue;
                canvas.drawRoundRect(mProgressBackgroundBounds, 45, 45, mProgressBackgroundPaint);

                mProgressBounds.left = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2;
                mProgressBounds.top = mCenterY - mExpandCollapseValue;
                mProgressBounds.right = mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2 + manualProgress * mCurrentGlobalManualProgressValue;
                mProgressBounds.bottom = mCenterY + mExpandCollapseValue;
                canvas.drawRoundRect(mProgressBounds, 45, 45, mProgressPaint);
                break;
            case ANIMATING_SUCCESS:
                mDrawingPaint.setStrokeWidth(mLineWidth);
                canvas.drawArc(mCircleBounds, 0, 360, false, mDrawingPaint);
                canvas.drawLine(
                        mCenterX - mRadius / 2 + mSuccessValue * 2 - mSuccessValue / (float) Math.sqrt(2f) / 2,
                        mCenterY + mSuccessValue,
                        mCenterX + mSuccessValue * 2 - mSuccessValue / (float) Math.sqrt(2f) / 2,
                        mCenterY - mSuccessValue,
                        mDrawingPaint
                );
                canvas.drawLine(
                        mCenterX - mSuccessValue - 2 * mSuccessValue / (float) Math.sqrt(2f) / 2,
                        mCenterY,
                        mCenterX + mRadius / 2 - mSuccessValue * 2 - mSuccessValue / (float) Math.sqrt(2f) / 2,
                        mCenterY + mSuccessValue,
                        mDrawingPaint
                );
                break;
            case ANIMATING_FAILED:
                mDrawingPaint.setStrokeWidth(mLineWidth);
                canvas.drawArc(mCircleBounds, 0, 360, false, mDrawingPaint);

                canvas.drawLine(
                        mCenterX - mRadius / 2 - mRadius / 4 + mErrorValue * 2,
                        mCenterY + mErrorValue,
                        mCenterX + mErrorValue,
                        mCenterY - mErrorValue,
                        mDrawingPaint
                );
                canvas.drawLine(
                        mCenterX - mErrorValue,
                        mCenterY - mErrorValue,
                        mCenterX + mRadius / 2 + mRadius / 4 - mErrorValue * 2,
                        mCenterY + mErrorValue,
                        mDrawingPaint
                );
                break;
        }
        if (mDotToProgressAnimatedValue > 0) {
            canvas.drawCircle(
                    mCenterX,
                    mCenterY - mDotToProgressAnimatedValue,
                    mStrokeWidth / 2,
                    mDrawingPaint
            );
        }

        if (mDotToProgressAnimation.isRunning() && !mArrowLineToHorizontalLine.isRunning()) {
            canvas.drawLine(
                    mCenterX - mRadius / 2 - mArrowLineToHorizontalLineAnimatedValue / 2,
                    mCenterY,
                    mCenterX + mRadius / 2 + mArrowLineToHorizontalLineAnimatedValue / 2,
                    mCenterY,
                    mDrawingPaint
            );
        }
    }

    public void onSuccess() {
        mResultProgressBarState = ProgressBarState.ANIMATING_SUCCESS;
        mWhichProgress = ProgressBarState.ANIMATING_PROGRESS;
        mArrowToLineAnimatorSet.start();
        invalidate();
    }

    public void onFailed() {
        mWhichProgress = ProgressBarState.ANIMATING_PROGRESS;
        mResultProgressBarState = ProgressBarState.ANIMATING_FAILED;
        mArrowToLineAnimatorSet.start();
        invalidate();
    }

    public void onManualProgressAnimation() {
        mWhichProgress = ProgressBarState.ANIMATING_MANUAL_PROGRESS;
        mResultProgressBarState = ProgressBarState.ANIMATING_SUCCESS;
        mArrowToLineAnimatorSet.start();
        invalidate();
    }

    public void abortDownload() {
        if (mExpandAnimation.isRunning() || mProgressAnimation.isRunning()) {
            mProgressAnimationSet.cancel();
            mCollapseAnimation.start();
            invalidate();
        }
    }

    public void setErrorResultProgressBarState() {
        if (mSuccessAnimation.isRunning() || mErrorAnimation.isRunning())
            return;
        mResultProgressBarState = ProgressBarState.ANIMATING_FAILED;
    }

    public void setSuccessResultProgressBarState() {
        if (mSuccessAnimation.isRunning() || mErrorAnimation.isRunning())
            return;
        mResultProgressBarState = ProgressBarState.ANIMATING_SUCCESS;
    }

    public void setProgress(float value) {
        LogUtil.getInstance().print(value);
        if (value < 1 || value > 100) {
            return;
        }
        mToArc = value * 3.6f;
        mManualProgressAnimation.setFloatValues(mFromArc, mToArc);
        mManualProgressAnimation.start();
        mFromArc = mToArc;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superProgressBarState = super.onSaveInstanceState();
        SavedProgressBarState savedProgressBarState = new SavedProgressBarState(superProgressBarState);
        savedProgressBarState.mProgressBarState = mProgressBarState;
        savedProgressBarState.mCurrentPlayTime = getCurrentPlayTimeByProgressBarState(mProgressBarState);
        return savedProgressBarState;
    }

    private void setCurrentPlayTimeByProgressBarStateAndPlay(long[] tab, ProgressBarState mProgressBarState) {
        switch (mProgressBarState) {
            case ANIMATING_LINE_TO_DOT:
                mArrowToLineAnimatorSet.start();
                for (int i = 0; i < mArrowToLineAnimatorSet.getChildAnimations().size(); i++) {
                    ((ValueAnimator) mArrowToLineAnimatorSet.getChildAnimations().get(i)).setCurrentPlayTime(tab[i]);
                }
                break;
            case ANIMATING_PROGRESS:
                mProgressAnimationSet.start();
                for (int i = 0; i < mProgressAnimationSet.getChildAnimations().size(); i++) {
                    ((ValueAnimator) mProgressAnimationSet.getChildAnimations().get(i)).setCurrentPlayTime(tab[i]);
                }
                break;
            case ANIMATING_FAILED:
                mErrorAnimation.start();
                mErrorAnimation.setCurrentPlayTime(tab[0]);
                break;
            case ANIMATING_SUCCESS:
                mSuccessAnimation.start();
                mSuccessAnimation.setCurrentPlayTime(tab[0]);
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedProgressBarState) {
            SavedProgressBarState savedProgressBarState = (SavedProgressBarState) state;
            mProgressBarState = savedProgressBarState.mProgressBarState;
            super.onRestoreInstanceState(savedProgressBarState.getSuperState());
            if (mProgressBarState != ProgressBarState.IDLE) {
                continueAnimation(mProgressBarState, savedProgressBarState.mCurrentPlayTime);
            }
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private void continueAnimation(ProgressBarState mProgressBarState, long[] mmCurrentPlayTime) {
        setCurrentPlayTimeByProgressBarStateAndPlay(mmCurrentPlayTime, mProgressBarState);
    }

    private long[] getCurrentPlayTimeByProgressBarState(ProgressBarState mProgressBarState) {
        long[] tab = new long[3];
        switch (mProgressBarState) {
            case ANIMATING_LINE_TO_DOT:
                for (int i = 0; i < mArrowToLineAnimatorSet.getChildAnimations().size(); i++) {
                    tab[i] = ((ValueAnimator) mArrowToLineAnimatorSet.getChildAnimations().get(i)).getCurrentPlayTime();
                }
                mArrowToLineAnimatorSet.cancel();
                break;
            case ANIMATING_PROGRESS:
                for (int i = 0; i < mProgressAnimationSet.getChildAnimations().size(); i++) {
                    tab[i] = ((ValueAnimator) mProgressAnimationSet.getChildAnimations().get(i)).getCurrentPlayTime();
                }
                mProgressAnimationSet.cancel();
                break;
            case ANIMATING_FAILED:
                tab[0] = mErrorAnimation.getCurrentPlayTime();
                mErrorAnimation.cancel();
                break;
            case ANIMATING_SUCCESS:
                tab[0] = mSuccessAnimation.getCurrentPlayTime();
                mSuccessAnimation.cancel();
                break;
        }
        return tab;
    }

    static class SavedProgressBarState extends BaseSavedState {

        private boolean isFlashing;
        private boolean isConfigurationChanged;
        private long[] mCurrentPlayTime;
        private ProgressBarState mProgressBarState;

        public SavedProgressBarState(Parcel source) {
            super(source);
            isFlashing = source.readInt() == 1;
            isConfigurationChanged = source.readInt() == 1;
            mCurrentPlayTime = source.createLongArray();
        }

        public SavedProgressBarState(Parcelable superProgressBarState) {
            super(superProgressBarState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isFlashing ? 1 : 0);
            dest.writeInt(isConfigurationChanged ? 1 : 0);
            dest.writeLongArray(mCurrentPlayTime);

        }

        public static final Creator<SavedProgressBarState> CREATOR = new Creator<SavedProgressBarState>() {

            @Override
            public SavedProgressBarState createFromParcel(Parcel source) {
                return new SavedProgressBarState(source);
            }

            @Override
            public SavedProgressBarState[] newArray(int size) {
                return new SavedProgressBarState[size];
            }
        };
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener onProgressUpdateListener) {
        this.mOnProgressUpdateListener = onProgressUpdateListener;
    }
}
