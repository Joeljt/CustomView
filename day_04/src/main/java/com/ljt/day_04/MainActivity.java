package com.ljt.day_04;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Easy58View cpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cpView = (Easy58View)findViewById(R.id.cp_view);

    }

    public void test(View view) {

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
//                cpView.setProgress(value);
            }
        });
        valueAnimator.setDuration(1500);
        valueAnimator.start();

    }
}
