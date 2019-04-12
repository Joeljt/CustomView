package com.ljt.day_27.loading;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.ljt.day_27.R;

/**
 * Created by JoeLjt on 2019/4/12.
 * Email: lijiateng1219@gmail.com
 * Description: 加载动画
 */

public class LoadingView extends View {

    // 动画时长
    private static final int ROTATION_ANIMATION_TIME = 2000;

    // 当前大圆旋转过的角度
    private float mRotatedAngle;

    // 大圆半径 - 屏幕宽度的 1/4
    private int mBigCircleRadius;
    private int mSmallCircleRadius;
    private float mCurrBigCircleRadius;

    // 画笔
    private Paint mPaint;

    // 颜色列表
    private int[] mColorArray;

    private int mCenterX, mCenterY;

    private LoadingState mLoadingState;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initParams(getContext());
    }

    private void initParams(Context context) {

        // 获取颜色列表
        mColorArray = context.getResources().getIntArray(R.array.splash_circle_colors);

        if (mColorArray.length == 0) {
            return;
        }

        // 获取大圆的半径
        mBigCircleRadius = getMeasuredWidth() / 4;
        mSmallCircleRadius = mBigCircleRadius / 7;

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLoadingState == null) {
            mLoadingState = new RotationStateImpl();
        }

        mLoadingState.draw(canvas);

    }


    public void disappear() {
        // 关闭旋转动画
        if (mLoadingState instanceof RotationStateImpl) {
            RotationStateImpl rotationStateImpl = (RotationStateImpl) mLoadingState;
            rotationStateImpl.cancel();
        }

        // 开始缩放动画
        mLoadingState = new MergeStateImpl();

    }

    public class RotationStateImpl extends LoadingState {

        private ValueAnimator mValueAnimator;

        public RotationStateImpl() {
            setRotateAnimation();
        }

        /**
         * 简单分析一下自定义 View 的样式
         * <p>
         * 1. 整体围成一个大圆，不断旋转，大圆位于屏幕正中，直径为屏幕宽度的 1/2；
         * 2. 六个小圆均匀分布在大圆上，每个扇形的角度相同，也就是 60°
         * 3. 大圆的半径是已知的，小圆的圆心也是已知的，角度也是已知的，只需要使用三角函数，求得每个小圆圆心的坐标值
         * <p>
         * 考虑使用属性动画实现，从 0 变化到 360，过程中不断重绘
         *
         * @param canvas
         */
        @Override
        public void draw(Canvas canvas) {
            // 考虑使用属性动画实现，从 0 变化到 360，过程中不断重绘
            canvas.drawColor(Color.WHITE);

            // 得到每个扇形的角度
            double percentAngle = Math.PI * 2 / mColorArray.length;

            for (int i = 0; i < mColorArray.length; i++) {

                mPaint.setColor(mColorArray[i]);

                double currAngle = percentAngle * i + mRotatedAngle;

                // x轴直角边 = 半径 * cos(角度)
                float cx = mCenterX + (float) (mBigCircleRadius * Math.cos(currAngle));

                // y轴直角边 = 半径 * sin(角度)
                float cy = mCenterY + (float) (mBigCircleRadius * Math.sin(currAngle));

                canvas.drawCircle(cx, cy, mSmallCircleRadius, mPaint);

            }
        }

        private void setRotateAnimation() {
            mValueAnimator = ObjectAnimator.ofFloat(0, (float) Math.PI * 2);
            mValueAnimator.setRepeatCount(-1);
            mValueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRotatedAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

        private void cancel() {
            mValueAnimator.cancel();
        }

    }

    public class MergeStateImpl extends LoadingState {

        private ValueAnimator mValueAnimator;

        public MergeStateImpl() {
            setMergeAnimation();
        }

        /**
         * 简单分析一下缩放动画的过程
         * <p>
         * 1. 几个小圆从圆周上缩放至圆心位置；
         *
         * @param canvas
         */
        @Override
        public void draw(Canvas canvas) {
            // 考虑使用属性动画实现，从 0 变化到 360，过程中不断重绘
            canvas.drawColor(Color.WHITE);

            // 得到每个扇形的角度
            double percentAngle = Math.PI * 2 / mColorArray.length;

            for (int i = 0; i < mColorArray.length; i++) {

                mPaint.setColor(mColorArray[i]);

                double currAngle = percentAngle * i + mRotatedAngle;

                // x轴直角边 = 半径 * cos(角度)
                float cx = mCenterX + (float) (mCurrBigCircleRadius * Math.cos(currAngle));

                // y轴直角边 = 半径 * sin(角度)
                float cy = mCenterY + (float) (mCurrBigCircleRadius * Math.sin(currAngle));

                canvas.drawCircle(cx, cy, mSmallCircleRadius, mPaint);

            }
        }

        private void setMergeAnimation() {
            mValueAnimator = ObjectAnimator.ofFloat(mBigCircleRadius, 0);
            mValueAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mValueAnimator.setInterpolator(new AnticipateInterpolator(5f));
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrBigCircleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    cancel();
                    mLoadingState = new ExtendStateImpl();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

            });
            mValueAnimator.start();
        }

        private void cancel() {
            mValueAnimator.cancel();
        }

    }

    public class ExtendStateImpl extends LoadingState {

        private ValueAnimator mValueAnimator;
        private int mExtendRadius;

        public ExtendStateImpl() {
            double mHalfXPow = Math.pow(mCenterX, 2);
            double mHalfYPow = Math.pow(mCenterY, 2);
            mExtendRadius = (int) Math.sqrt(mHalfXPow + mHalfYPow);
            setMergeAnimation();
        }

        /**
         * 简单分析一下缩放动画的过程
         * <p>
         * 1. 从屏幕正中心开始扩散，半径不断放大，直到整个屏幕都是当前圆的内切矩形
         * <p>
         * 也就是说，圆的半径是屏幕对角线的一半: Math.sqr(centerX * centerX + centerY * centerY)
         *
         * @param canvas
         */
        @Override
        public void draw(Canvas canvas) {

            float strokeWidth = mExtendRadius - mCurrBigCircleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);

            float radius = strokeWidth / 2 + mCurrBigCircleRadius;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);

        }

        private void setMergeAnimation() {

            mValueAnimator = ObjectAnimator.ofFloat(0, mExtendRadius);
            mValueAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrBigCircleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

        private void cancel() {
            mValueAnimator.cancel();
        }

    }


}

