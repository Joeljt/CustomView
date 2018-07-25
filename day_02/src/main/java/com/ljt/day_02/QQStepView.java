package com.ljt.day_02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by lijiateng on 2018/7/25.
 * <p>
 * 自定义 View 的基本流程：
 * <p>
 * 1. 分析效果；
 * 2. 确定自定义属性，编写 attrs.xml
 * 3. 在布局中使用
 * 4. 在自定义 View 中获取自定义属性
 * 5. onMeasure()
 * 6. onDraw() 画外圆弧，内圆弧，文字
 * 7. 其它效果实现
 */

public class QQStepView extends View {

    private int mOuterColor;
    private int mInnerColor;
    private int mBorderWidth;
    private int mTextSize;
    private int mTextColor;

    private Paint mOutPaint, mInnerPaint, mTextPaint;

    // 最大步数
    private int mMaxStep = 0;
    private int mCurrentStep = 0;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);

        mOuterColor = typedArray.getColor(R.styleable.QQStepView_outer_color, Color.BLUE);
        mInnerColor = typedArray.getColor(R.styleable.QQStepView_inner_color, Color.RED);
        mBorderWidth = (int) typedArray.getDimension(R.styleable.QQStepView_border_width, 15);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.QQStepView_text_size, px2sp(15));
        mTextColor = typedArray.getColor(R.styleable.QQStepView_text_color, Color.RED);

        typedArray.recycle();

        // 外层圆环画笔
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStyle(Paint.Style.STROKE);

        // 进度圆弧画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        // 文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 确保是个正方形，如果宽高不一致时，使用较小的一个

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width < height ? width : height, width < height ? width : height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas.drawArc(rectF, 135, 270, false, mOutPaint);
        // 画圆弧的方法，接收五个参数
        // 1. RectF：外切矩形的坐标，左上顶点一个，右下顶点一个，这里需要考虑到圆弧画笔的宽度，因此需要进行「除以2」的操作
        // 2. startAngle：开始角度，x 轴方向为 0°，顺时针为正向
        // 3. sweepAngle: 圆弧需要划过的角度，用来最终确认扇面大小
        // 4. useCenter: 配合画笔的 style 属性，来决定最终画出的是一个扇面，还是一个没有圆心缺口的饼图
        // 5. paint: 画笔

        // 绘制大圆弧
        RectF rectF = new RectF(mBorderWidth / 2, mBorderWidth / 2, getWidth() - mBorderWidth / 2, getHeight() - mBorderWidth / 2);
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);

        // 绘制进度圆弧，进度圆弧是调用者从外部传入的，不是写死的
        if (mCurrentStep == 0) return;
        float sweepAngle = (float) mCurrentStep / mMaxStep;
        canvas.drawArc(rectF, 135,  sweepAngle*270, false, mInnerPaint);

        // 对文字进行测量
        String text = String.valueOf(mCurrentStep);
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);

        // 获取绘制 x 坐标
        int dx = getWidth() / 2 - textBounds.width()/2;

        // 获取基线 y 坐标
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        // 绘制文字
        canvas.drawText(text, dx, baseLine, mTextPaint);

    }

    private int px2sp(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public synchronized void setMaxStep(int maxStep) {
        this.mMaxStep = maxStep;
    }

    public synchronized void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        invalidate();
    }

}
