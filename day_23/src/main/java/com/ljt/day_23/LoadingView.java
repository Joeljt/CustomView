package com.ljt.day_23;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

/**
 * Created by JoeLjt on 2019/4/17.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class LoadingView extends RelativeLayout {

    private CircleView mLeftView, mMiddleView, mRightView;

    private int mMoveDistance;

    private boolean isStopped;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
        startAnimation();
    }

    /**
     * 使用 post 方法，将方法放到 onResume 之后去执行
     */
    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                goOutAnimator();
            }
        });
    }

    private void goOutAnimator() {

        if (isStopped) return;

        Log.e("LJT", "goOutAnimator");

        ObjectAnimator leftAnimator
                = ObjectAnimator.ofFloat(mLeftView, "translationX", 0, -mMoveDistance);
        ObjectAnimator rightAnimator
                = ObjectAnimator.ofFloat(mRightView, "translationX", 0, mMoveDistance);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftAnimator, rightAnimator);
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                backInAnimator();
            }
        });
        animatorSet.start();
    }

    private void backInAnimator() {

        if (isStopped) return;

        Log.e("LJT", "backInAnimator");

        ObjectAnimator leftAnimator
                = ObjectAnimator.ofFloat(mLeftView, "translationX", -mMoveDistance, 0);
        ObjectAnimator rightAnimator
                = ObjectAnimator.ofFloat(mRightView, "translationX", mMoveDistance, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftAnimator, rightAnimator);
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 交换颜色，左给中，中给右，右给左
                int mLeftColor = mLeftView.getPaintColor();
                int mMiddleColor = mMiddleView.getPaintColor();
                int mRightColor = mRightView.getPaintColor();

                mLeftView.exchangeColor(mRightColor);
                mMiddleView.exchangeColor(mLeftColor);
                mRightView.exchangeColor(mMiddleColor);

                goOutAnimator();

            }
        });
        animatorSet.start();
    }

    private void initLayout(Context context) {
        mLeftView = getCircleView(context);
        mLeftView.exchangeColor(Color.GREEN);

        mMiddleView = getCircleView(context);
        mMiddleView.exchangeColor(Color.BLACK);

        mRightView = getCircleView(context);
        mRightView.exchangeColor(Color.BLUE);

        addView(mLeftView);
        addView(mRightView);
        addView(mMiddleView);

        mMoveDistance = dip2px(20);

    }


    private CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        LayoutParams layoutParams = new LayoutParams(dip2px(10), dip2px(10));
        layoutParams.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(layoutParams);
        return circleView;
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    @Override
    public void setVisibility(int visibility) {

        // 不再调用系统的测量、摆放等源码
        super.setVisibility(View.GONE);

        // 停止动画
        mLeftView.clearAnimation();
        mMiddleView.clearAnimation();
        mRightView.clearAnimation();

        // 从父布局移除自己
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }

        // 移除自己的所有布局
        removeAllViews();

        isStopped = true;

    }

    public void dismiss() {
        setVisibility(View.GONE);
    }

}
