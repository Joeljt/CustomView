package com.ljt.day_04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lijiateng on 2018/7/29.
 */

public class CircleProgressView extends View {

    private Paint mOuterPaint, mInnerPaint, mTextPaint;

    private int mStrokeWidth;
    private int mOuterCircleColor;
    private int mInnerCircleColor;
    private int mProgressTextColor;
    private int mProgressTextSize;

    private float mCurrProgress = 0.0f;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_progress_stroke_width, 15);
        mOuterCircleColor = typedArray.getColor(R.styleable.CircleProgressView_outer_circle_color, Color.BLUE);
        mInnerCircleColor = typedArray.getColor(R.styleable.CircleProgressView_inner_circle_color, Color.RED);
        mProgressTextColor = typedArray.getColor(R.styleable.CircleProgressView_progress_text_color, Color.RED);
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_progress_text_size, 15);
        typedArray.recycle();

        // 绘制外层整体进度圆形画笔
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterCircleColor);
        mOuterPaint.setStrokeWidth(mStrokeWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        // 绘制进度圆形画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerCircleColor);
        mInnerPaint.setStrokeWidth(mStrokeWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        // 绘制文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size = widthSize < heightSize ? widthSize : heightSize;

        // 保证是一个正方形
        setMeasuredDimension(size, size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. 画外层大圆

        // 设置外切矩形
        RectF rectF = new RectF(mStrokeWidth/2, mStrokeWidth/2, getWidth() - mStrokeWidth/2, getHeight()-mStrokeWidth/2);
        canvas.drawArc(rectF, 0, 360, false, mOuterPaint);

        // 2. 画进度圆形
        canvas.drawArc(rectF, 0, mCurrProgress * 360, false, mInnerPaint);

        // 3. 绘制进度文字

        // 获取文字信息
        String currProgressText = String.valueOf((int) (mCurrProgress * 100)) + "%";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(currProgressText, 0, currProgressText.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();


        // 确定起始坐标
        int x = getWidth() / 2 - bounds.width() / 2;
        int y = getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;

        canvas.drawText(currProgressText, x, y, mTextPaint);

    }

    public void setProgress(float progress) {
        this.mCurrProgress = progress;
        invalidate();
    }

}
