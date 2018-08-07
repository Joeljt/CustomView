package com.ljt.day_03;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ColorTrackTextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tvTest = findViewById(R.id.tv_test);

    }

    public void leftToRight(View view) {
        tvTest.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
        final ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currProgress = (float) valueAnimator.getAnimatedValue();
                tvTest.setCurrProgress(currProgress);
            }
        });
        animator.start();
    }

    public void rightToLeft(View view) {
        tvTest.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
        final ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currProgress = (float) valueAnimator.getAnimatedValue();
                tvTest.setCurrProgress(currProgress);
            }
        });
        animator.start();
    }


}
