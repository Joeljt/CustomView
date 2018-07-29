package com.ljt.day_04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lijiateng on 2018/7/29.
 */

public class CircleProgressView extends View {

    private Paint mOuterPaint, mInnerPaint, mTextPaint;

    private int mOuterCircleColor;
    private int mInnerCircleColor;
    private int mProgressTextColor;
    private int mProgressTextSize;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mOuterCircleColor = typedArray.getColor(R.styleable.CircleProgressView_outer_circle_color, Color.BLUE);
        mInnerCircleColor = typedArray.getColor(R.styleable.CircleProgressView_inner_circle_color, Color.RED);
        mProgressTextColor = typedArray.getColor(R.styleable.CircleProgressView_progress_text_color, Color.RED);
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_progress_text_size, 15);
        typedArray.recycle();

        // 绘制外层整体进度圆形画笔

        // 绘制进度圆形画笔

        // 绘制文字画笔


    }

}
