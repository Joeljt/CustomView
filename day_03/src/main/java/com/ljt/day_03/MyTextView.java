package com.ljt.day_03;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by lijiateng on 2018/12/4.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.e("LJT", "MyTextView: " + MeasureSpec.getMode(widthMeasureSpec) + ", " + MeasureSpec.getSize(widthMeasureSpec));
//        Log.e("LJT", "MyTextView: " + MeasureSpec.getMode(heightMeasureSpec) + ", " + MeasureSpec.getSize(heightMeasureSpec));


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("LJT", "onDraw: " + canvas.getWidth());
    }
}
