package com.ljt.view.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/08/05, 12:00
 * For：
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);


        Log.e("LJT", "之前 " + String.format("widthMode is %d, heightMode is %d; width is %d, height is %d",
                widthMode, heightMode, width, height));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);


        Log.e("LJT","之后 " +  String.format("widthMode is %d, heightMode is %d; width is %d, height is %d",
                widthMode, heightMode, width, height));
    }



}
