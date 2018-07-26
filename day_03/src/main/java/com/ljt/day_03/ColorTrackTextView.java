package com.ljt.day_03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by lijiateng on 2018/7/26.
 *
 * 主要技术点：
 *  利用 canvas.clipRect() 方法，不断地去改变切割点，
 *  然后用两个不同颜色的画笔对两侧进行绘制，从而实现一个文本两种颜色的效果
 *
 */

public class ColorTrackTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mOriginPaint, mChangePaint;

    private float mProgress;

    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    public enum Direction{
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint(context, attrs);

    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor = typedArray.getColor(R.styleable.ColorTrackTextView_origin_color, getTextColors().getDefaultColor());
        int changeColor = typedArray.getColor(R.styleable.ColorTrackTextView_change_color, getTextColors().getDefaultColor());

        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        typedArray.recycle();

    }

    private Paint getPaintByColor(int changeColor) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(changeColor);
//        paint.setDither(true); // 防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 不使用 super.onDraw()，因为要自己进行绘制操作

        // 根据进度把中间值算出来
        int middle = (int) (mProgress * getWidth());

        if (mDirection == Direction.LEFT_TO_RIGHT) {

            // 左边是红色，右边是黑色

            // 绘制变色的区域
            drawText(canvas, mChangePaint, 0, middle);

            // 绘制不变色区域
            drawText(canvas, mOriginPaint, middle, getWidth());

        } else {

            // 左边是黑色，右边是红色

            // 绘制变色区域
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());

            // 绘制不变色的区域
            drawText(canvas, mOriginPaint, 0, getWidth() - middle);

        }

    }

    private void drawText(Canvas canvas, Paint paint, int start, int end) {

        // 保存当前画布状态
        canvas.save();

        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);

        // 获取文字的基本宽高信息
        String text = getText().toString();
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();

        // 获取起始位置
        int x = getWidth() / 2 - textBounds.width() / 2;
        int y = getHeight() / 2 + (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        canvas.drawText(text, x, y, paint);

        // 清空画布属性，方便接下来绘制变色的部分
        canvas.restore();

    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setCurrProgress(float currProgress) {
        this.mProgress = currProgress;
        invalidate();
    }

}
