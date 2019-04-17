package com.ljt.day_23;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by JoeLjt on 2019/4/17.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class CircleView extends View {

    private Paint mPaint;
    private int mPaintColor;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public void exchangeColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.e("LJT", "CircleView onMeasure()");

        int radius = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(radius, radius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        int radius = dip2px(5); // radius 不能大于 cx/cy，否则会把 circle 画成 square

        Log.e("LJT", String.format("cx is %d, cy is %d, radius is %d", cx, cy, radius));

        canvas.drawCircle(cx, cy, cx, mPaint);
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

}
