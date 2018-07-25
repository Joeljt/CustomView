package com.ljt.day_02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by lijiateng on 2018/7/25.
 *
 * 自定义 View 的基本流程：
 *
 * 1. 分析效果；
 * 2. 确定自定义属性，编写 attrs.xml
 * 3. 在布局中使用
 * 4. 在自定义 View 中获取自定义属性
 * 5. onMeasure()
 * 6. onDraw() 画外圆弧，内圆弧，文字
 * 7. 其它效果实现
 *
 */

public class QQStepView extends View {

    private int mOuterColor;
    private int mInnerColor;
    private int mBorderWidth;
    private int mTextSize;
    private int mTextColor;

    private Paint mPaint;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);

        mOuterColor = typedArray.getColor(R.styleable.QQStepView_OuterColor, Color.BLUE);
        mInnerColor = typedArray.getColor(R.styleable.QQStepView_InnerColor, Color.RED);
        mBorderWidth = typedArray.getDimensionPixelOffset(R.styleable.QQStepView_BorderWidth, px2sp(15));
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.QQStepView_OuterColor, px2sp(15));
        mTextColor = typedArray.getColor(R.styleable.QQStepView_OuterColor, Color.RED);

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mOuterColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    private int px2sp(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

}
