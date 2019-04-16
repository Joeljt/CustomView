package com.ljt.day_21;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by JoeLjt on 2019/4/15.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class LoadingView extends LinearLayout {

    private ShapeView mShapeView;
    private View mShadow;

    private int mTranslationDistance;
    private int mScaleDistance;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_ui_loading_view, this);

        mShapeView = findViewById(R.id.shape_view);
        mShadow = findViewById(R.id.v_shadow);

        MarginLayoutParams params = (MarginLayoutParams) mShapeView.getLayoutParams();
        mTranslationDistance = params.bottomMargin - 5;

        initFallAnimator();

    }

    private void initFallAnimator() {

        // 下落动画
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationDistance);
        translationYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShapeView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        // 阴影配合放大动画
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadow, "scaleX", 0.3f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationYAnimator, scaleAnimator);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();

        // 下落完成后上抛
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                initUpAnimator();
            }

        });

    }

    /**
     * 上抛动画
     */
    private void initUpAnimator() {

        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0);
        translationYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShapeView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        // 阴影配合缩小动画
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadow, "scaleX", 1.0f, 0.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationYAnimator, scaleAnimator);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());


        // 下落完成后上抛
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                initFallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                initRotationAnimator();
            }
        });

        animatorSet.start();

    }

    private void initRotationAnimator() {

        mShapeView.exchange();
        ShapeView.Shape mCurrShape = mShapeView.getCurrShape();

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
        switch (mCurrShape) {
            case CIRCLE:
            case SQUARE:
                // 圆形和正方形旋转 360 度
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
            case TRIANGLE:
                // 三角形旋转 120 度
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 120);
                break;
        }

        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setDuration(150);
        rotationAnimator.start();

    }


}
