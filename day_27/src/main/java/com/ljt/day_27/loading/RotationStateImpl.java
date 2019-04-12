//package com.ljt.day_27.loading;
//
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//
///**
// * Created by JoeLjt on 2019/4/12.
// * Email: lijiateng1219@gmail.com
// * Description:
// */
//
//public class RotationStateImpl extends LoadingState {
//
//    private View mView;
//    private Paint mPaint;
//    private float mRotatedAngle;
//    private ValueAnimator mValueAnimator;
//
//    public RotationStateImpl(View view, Paint paint, int[] colorArray, int centerX, int) {
//        this.mView = view;
//        this.mPaint = paint;
//        setRotateAnimation();
//    }
//
//    /**
//     * 简单分析一下自定义 View 的样式
//     * <p>
//     * 1. 整体围成一个大圆，不断旋转，大圆位于屏幕正中，直径为屏幕宽度的 1/2；
//     * 2. 六个小圆均匀分布在大圆上，每个扇形的角度相同，也就是 60°
//     * 3. 大圆的半径是已知的，小圆的圆心也是已知的，角度也是已知的，只需要使用三角函数，求得每个小圆圆心的坐标值
//     * <p>
//     * 考虑使用属性动画实现，从 0 变化到 360，过程中不断重绘
//     *
//     * @param canvas
//     */
//    @Override
//    public void draw(Canvas canvas) {
//
//        // 考虑使用属性动画实现，从 0 变化到 360，过程中不断重绘
//        canvas.drawColor(Color.WHITE);
//
//        // 得到每个扇形的角度
//        double percentAngle = Math.PI * 2 / mColorArray.length;
//
//        for (int i = 0; i < mColorArray.length; i++) {
//
//            mPaint.setColor(mColorArray[i]);
//
//            double currAngle = percentAngle * i + mRotatedAngle;
//
//            // x轴直角边 = 半径 * cos(角度)
//            float cx = mCenterX + (float) (mBigCircleRadius * Math.cos(currAngle));
//
//            // y轴直角边 = 半径 * sin(角度)
//            float cy = mCenterY + (float) (mBigCircleRadius * Math.sin(currAngle));
//
//            canvas.drawCircle(cx, cy, mSmallCircleRadius, mPaint);
//
//        }
//    }
//
//    private void setRotateAnimation() {
//        mValueAnimator = ObjectAnimator.ofFloat(0, (float) Math.PI * 2);
//        mValueAnimator.setRepeatCount(-1);
//        mValueAnimator.setDuration(ROTATION_ANIMATION_TIME);
//        mValueAnimator.setInterpolator(new LinearInterpolator());
//        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mRotatedAngle = (float) animation.getAnimatedValue();
//                mView.invalidate();
//            }
//        });
//        mValueAnimator.start();
//    }
//
//    private void cancel() {
//        mValueAnimator.cancel();
//    }
//}
