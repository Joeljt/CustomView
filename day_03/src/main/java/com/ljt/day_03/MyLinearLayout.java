package com.ljt.day_03;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lijiateng on 2018/12/4.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.e("LJT", "MyLinearLayout: " + MeasureSpec.getMode(widthMeasureSpec) + ", " + MeasureSpec.getSize(widthMeasureSpec));
//        Log.e("LJT", "MyLinearLayout: " + MeasureSpec.getMode(heightMeasureSpec) + ", " + MeasureSpec.getSize(heightMeasureSpec));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int spec = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int childDimension = 0; // 子布局 LayoutParams 的值

        int sizeWithoutPadding = Math.max(0, size - 0);

        int returnMode = 0;
        int returnSize = 0;

        switch (spec) {
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    // 父布局固定大小，不管是限定死的宽高还是 match_parent 导致的窗口大小
                    // 子布局自己声明了明确的宽高信息，那就让他保持该有的样子，mode 自然也就是 EXACTLY
                    returnSize = childDimension;
                    returnMode = MeasureSpec.EXACTLY;
                } else if (size == LayoutParams.MATCH_PARENT) {
                    // 子布局想和父布局一样，但是父布局限定死了，那就和父布局一样就行
                    returnSize = sizeWithoutPadding;
                    returnMode = MeasureSpec.EXACTLY;
                } else if (size == LayoutParams.WRAP_CONTENT) {
                    // 子布局要包裹内容，那就让它尽量大就可以，但是 size 不能超过父布局
                    returnSize = size;
                    returnMode = MeasureSpec.AT_MOST;
                }
                break;

            case MeasureSpec.AT_MOST:
                // AT_MOST 说明父布局是包裹内容，自己也不知道自己多大
                if (childDimension >= 0) {
                    // 子布局知道自己多大，那就让它那么大；而且已经知道自己多大了，那就是 EXACTLY
                    returnSize = childDimension;
                    returnMode = MeasureSpec.EXACTLY;
                } else if (size == LayoutParams.MATCH_PARENT) {
                    // 子布局想和父布局一样大，那就一样大
                    returnSize = size;
                    returnMode = MeasureSpec.AT_MOST;
                } else if (size == LayoutParams.WRAP_CONTENT) {
                    // 子布局要包裹内容，但是父布局并不知道自己多大，那就尽量大就好
                    returnSize = size;
                    returnMode = MeasureSpec.AT_MOST;
                }
                break;
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        r = 1000;
        b = 1000;

        super.onLayout(changed, l, t, r, b);


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.e("LJT", "MyLinearLayout onDraw: " + canvas.getWidth());
    }
}
