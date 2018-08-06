package com.ljt.view.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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


//        Log.e("LJT", "之前 " + String.format("widthMode is %d, heightMode is %d; width is %d, height is %d",
//                widthMode, heightMode, width, height));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);


//        Log.e("LJT","之后 " +  String.format("widthMode is %d, heightMode is %d; width is %d, height is %d",
//                widthMode, heightMode, width, height));
    }

    @Override
    protected void onDraw(Canvas canvas){
//        final String s = "Hello. I'm some text!";
//
//        Paint p = new Paint();
//        Rect bounds = new Rect();
//        p.setTextSize(60);
//
//        p.getTextBounds(s, 0, s.length(), bounds);
//        float mt = p.measureText(s);
//        int bw = bounds.width();
//
//        Log.i("LCG", String.format(
//                "measureText %f, getTextBounds %d (%s)",
//                mt,
//                bw, bounds.toShortString())
//        );
//        bounds.offset(0, -bounds.top);
//        p.setStyle(Paint.Style.STROKE);
//        canvas.drawColor(0xff000080);
//        p.setColor(0xffff0000);
//        canvas.drawRect(bounds, p);
//        p.setColor(0xff00ff00);
//        canvas.drawText(s, 0, bounds.bottom, p);

        final String someText = "Hello. I'm some text!";

        Paint mPaint = new Paint();

        // .measureText()
        //        float measuredWidth = mPaint.measureText(someText);

        // .getTextBounds()
        Rect mBounds = new Rect();
        mPaint.getTextBounds(someText, 0, someText.length(), mBounds);
//        int measuredWidth = mBounds.width();
//        int measuredHeight = mBounds.height();

        Log.d("Test", String.format(
                "Text is '%s', measureText %f, getTextBounds %d",
                someText,
                mPaint.measureText(someText),
                mBounds.width())
        );

    }

}
