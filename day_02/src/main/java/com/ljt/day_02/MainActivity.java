package com.ljt.day_02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Log.e("LJT", "handleMessage()");
//        }
//    };
    View view;

    static final ThreadLocal<String> sRunQueues = new ThreadLocal<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final QQStepView view = findViewById(R.id.step_view);
//
//        view.setMaxStep(4000);

        // 属性动画
//        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
//        valueAnimator.setDuration(1500);
//        valueAnimator.setInterpolator(new OvershootInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float currStep = (float) valueAnimator.getAnimatedValue();
//                view.setCurrentStep((int) currStep);
//            }
//        });
//        valueAnimator.start();

//        String a = "lijiateng";
////        sRunQueues.set(a);
//        Log.e("LJT", sRunQueues.get().toString());

        final ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();

        // 等待 Add 到父布局中
        view = new View(this) {
            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);

//                Log.e("LJT", "onLayout()");

            }
        };

//
//            @Override
//            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//                Log.e("LJT", "onMeasure()");
//            }
//
//            @Override
//            protected void onDraw(Canvas canvas) {
//                super.onDraw(canvas);
//                Log.e("LJT", "onDraw()");
//            }
//        };
//
//        // 根据源码来看，mAttachInfo 还为空，这里会交给 Handler 来处理
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("LJT", "mHandler.post ---- > " + view.getHeight());
//            }
//        });
//
//        // 根据源码来看，mAttachInfo 还为空，这里会交给 Handler 来处理
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("LJT", "view.post ---- > " + view.getHeight());
//            }
//        });
//
//        new Thread(){
//            @Override
//            public void run() {
//
//                Log.e("LJT", "new Thread run()");
//
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("LJT", "new Thread view.post ---- > " + view.getHeight());
//                    }
//                });
//            }
//        }.start();
//
        viewGroup.addView(view);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.e("LJT", "onResume() ---> " + view.getHeight());
//
//        new Thread() {
//            @Override
//            public void run() {
//
//                Log.e("LJT", "onResume new Thread run()");
//
//
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("LJT", "onResume view.post ---- > " + view.getHeight());
//                    }
//                });
//            }
//        }.start();



    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

//        Log.e("LJT", "onAttachedToWindow() ---> " + view.getHeight());
//
//        new Thread(){
//            @Override
//            public void run() {
//
//                Log.e("LJT", "onAttachedToWindow new Thread run()");
//
//
//
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("LJT", "onAttachedToWindow view.post ---- > " + view.getHeight());
//                    }
//                });
//            }
//        }.start();


    }
}
