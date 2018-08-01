package com.ljt.day_06;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/08/01, 07:46
 * For：
 */

public class LetterSlideBar extends View {

    private Paint mLetterPaint, mFocusedLetterPaint;
    private int letterColor, letterFocusedColor;

    public LetterSlideBar(Context context) {
        this(context, null);
    }

    public LetterSlideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSlideBar);

        letterColor = typedArray.getColor(R.styleable.LetterSlideBar_letter_color, Color.BLACK);
        letterFocusedColor = typedArray.getColor(R.styleable.LetterSlideBar_letter_focused_color, Color.RED);

        typedArray.recycle();

        mLetterPaint = new Paint();
        mLetterPaint.setAntiAlias(true);
        mLetterPaint.setColor(letterColor);
        mLetterPaint.setTextSize(12);

        mFocusedLetterPaint = new Paint();
        mFocusedLetterPaint.setAntiAlias(true);
        mFocusedLetterPaint.setColor(letterFocusedColor);
        mFocusedLetterPaint.setTextSize(12);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 高度可以直接获取
        // 宽度需要进行适当的计算：getPaddingLeft + 一个字母的宽度 + getPaddingRight

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = (int) (getPaddingLeft() + mLetterPaint.measureText("A") + getPaddingRight());

        setMeasuredDimension(width, height);

    }
}
