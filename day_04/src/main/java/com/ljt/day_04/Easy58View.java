package com.ljt.day_04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lijiateng on 2018/7/29.
 */

public class Easy58View extends View {

    private Paint mPaint;
    private RectF rectF;
    private Path mPath;

    private int mStrokeWidth = 15;
    private int mSquareColor;
    private int mTriangleColor;
    private int mCircleColor;

    private Shape mCurrShape = Shape.CIRCLE;

    public Easy58View(Context context) {
        this(context, null);
    }

    public Easy58View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Easy58View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Easy58View);
        mSquareColor = typedArray.getDimensionPixelSize(R.styleable.Easy58View_square_color, Color.BLUE);
        mTriangleColor = typedArray.getColor(R.styleable.Easy58View_triangle_color, Color.BLUE);
        mCircleColor = typedArray.getColor(R.styleable.Easy58View_circle_color, Color.GREEN);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokeWidth);

        mPath = new Path();
        rectF = new RectF(mStrokeWidth/2,mStrokeWidth/2,getWidth()-mStrokeWidth/2, getHeight()-mStrokeWidth/2);

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

        switch (mCurrShape) {
            case CIRCLE:
                drawCircle(canvas);
                break;
            case SQUARE:
                drawSquare(canvas);
                break;
            case TRIANGLE:
                drawTriangle(canvas);
                break;
        }

    }

    private void drawSquare(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rectF, mPaint);
    }

    private void drawTriangle(Canvas canvas) {
        mPath.moveTo(getWidth() / 2, 0);
        mPath.lineTo(0, (float) Math.sqrt(getWidth()*getWidth() - getWidth()/2*getWidth()/2));
        mPath.lineTo(getWidth(), (float) Math.sqrt(getWidth()*getWidth() - getWidth()/2*getWidth()/2));
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.RED);
        canvas.drawCircle(getWidth()/2, getHeight()/2, rectF.width()/2, mPaint);
    }

    /**
     * 循环更新图形绘制
     */
    public void exchange() {
        switch (mCurrShape) {
            case CIRCLE:
                mCurrShape = Shape.SQUARE;
                break;
            case SQUARE:
                mCurrShape = Shape.TRIANGLE;
                break;
            case TRIANGLE:
                mCurrShape = Shape.CIRCLE;
                break;
        }
        // 重绘
        invalidate();
    }

    public enum Shape{
        CIRCLE, SQUARE, TRIANGLE
    }

}
