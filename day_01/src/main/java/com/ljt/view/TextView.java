package com.ljt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/07/23, 07:16
 * For：自定义 TextView
 *
 * 继承自 LinearLayout 时，除非设置 background 属性，否则无法显示
 * 原因：ViewGroup 默认不会执行 onDraw() 方法
 *
 * 解决方法：1.重写 dispatchDraw()
 *          2. 给一个默认的透明背景
 *          3. 设置焦点等方式，改变 flag 的值
 */

public class TextView extends View {

    private String mText;
    private int mTextSize = 15;
    private int mTextColor = Color.BLACK;

    private Paint mPaint;

    /**
     * 在代码中初始化时使用
     * @param context
     */
    public TextView(Context context) {
        this(context, null);
    }

    /**
     * 在布局中声明时调用此构造方法
     * @param context
     * @param attrs 系统属性通过该属性传入
     */
    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 在布局文件中声明，并且使用了 style 属性时，调用此构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoeTextView);

        mText = typedArray.getString(R.styleable.JoeTextView_JoeText);
        mTextColor = typedArray.getColor(R.styleable.JoeTextView_JoeTextColor, mTextColor);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.JoeTextView_JoeTextSize, sp2px(mTextSize));

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        setBackgroundColor(Color.parseColor("#ffffffff"));

//        setWillNotDraw(true);

    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // mode: AT_MOST, EXACTLY, UNSECIFIED
        // AT_MOST 一般对应 wrap_content, EXACTLY 对应具体的数值, unspecified 一般不使用，系统控件才会用到

        // 1. 获取控件的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 2. 获取控件的宽度，并根据测量模式进行测量
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {

            // 使用画笔对 mText 进行测量，并将测量结果存入 rect 对象中
            Rect rect = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), rect);
            widthSize = rect.width() + getPaddingLeft() + getPaddingRight();

        }

        // 3. 获取控件的高度，并根据测量模式进行测量
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {

            // 使用画笔对 mText 进行测量，并将测量结果存入 rect 对象中
            Rect rect = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), rect);
            heightSize = rect.height() + getPaddingTop() + getPaddingBottom();

        }

        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 canvas 来画文本内容
        // text, x, y, paint
        // X 开始的位置，y 是基线

//        Paint.FontMetrics metrics = mPaint.getFontMetrics();

//        int dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
//        int baseLine = getHeight()/2 + dy;

//        int textHeight = (metrics.descent - metrics.ascent + metrics.leading);
//
//        int centerY = textHeight / 2;
//
//        int dy1 = centerY - metrics.descent;


//        (metrics.descent - metrics.ascent) / 2 + ((metrics.descent - metrics.ascent) / 2 - metrics.descent);


//        Log.e("ljt", metrics.toString());
//        Log.e("ljt", "" + getHeight());
//        Log.e("ljt", "" + baseLine);
//        Log.e("ljt", "" + dy);
//
//        Log.e("ljt", " =============================== ");
//
//
//        Log.e("ljt", "" + textHeight);
//        Log.e("ljt", "" + centerY);
//        Log.e("ljt", "" + dy1);

//        Paint.FontMetrics metrics = mPaint.getFontMetrics();
//        float dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
//        float baseLine = getHeight() / 2 + dy;

        Paint.FontMetricsInt metrics = mPaint.getFontMetricsInt();
        int dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        int baseLine = getHeight()/2 + dy;


        Log.e("ljt", metrics.toString());

        canvas.drawText(mText, getPaddingLeft(), baseLine, mPaint);

//        canvas.drawText(mText, getPaddingLeft(), -metrics.ascent, mPaint);


    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//
//
//        Paint.FontMetricsInt metrics = mPaint.getFontMetricsInt();
//        int dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
//        int baseLine = getHeight()/2 + dy;
//
//
//        Log.e("ljt", metrics.toString());
//
//        canvas.drawText(mText, getPaddingLeft(), baseLine, mPaint);
//    }


}
