package com.ljt.day_03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lijiateng on 2018/8/7.
 */

public class ColorTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mOriginPaint, mChangePaint;

    public ColorTextView(Context context) {
        this(context, null);
    }

    public ColorTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor = typedArray.getColor(R.styleable.ColorTrackTextView_origin_color, Color.BLACK);
        int changeColor = typedArray.getColor(R.styleable.ColorTrackTextView_change_color, Color.RED);

        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        typedArray.recycle();

    }

    private Paint getPaintByColor(int changeColor) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(changeColor);
        paint.setDither(true); // 防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        cutCanvas(canvas, 0, getWidth()/2, mChangePaint);

        cutCanvas(canvas, getWidth() / 2, getWidth(), mOriginPaint);

    }

    private void cutCanvas(Canvas canvas, int start, int end, Paint paint) {
        canvas.save();

        canvas.clipRect(new Rect(start, 0, end, getHeight()));

        String text = getText().toString();
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        int dx = -textBounds.left + getWidth() / 2 - textBounds.width() / 2;
        int dy = -textBounds.top + getHeight() / 2 - textBounds.height() / 2;

        canvas.drawText(text, dx, dy , paint);


        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();

        // 获取起始位置
        int x = getWidth() / 2 - textBounds.width() / 2;
        int y = getHeight() / 2 + (metrics.bottom - metrics.top) / 2 - metrics.bottom;

        canvas.restore();
    }
}
