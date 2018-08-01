package com.ljt.day_06;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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

    // 定义26个字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

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

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制 26 个英文字母
        for (String letter : mLetters) {

            // x 坐标的位置 -> (控件的宽度 - 字母宽度) 的一半
            int x = (int) (getWidth() / 2 - mLetterPaint.measureText(letter) / 2);

            // 基线的位置，需要根据字母数量和当前位置来不断计算得到
            // 当前字母的基线 = 当前字母之前的所有字母的高度 + 当前字母的基线
//            (getHeight() / mLetters.length) * ;
            int y = 0;

            canvas.drawText(letter, x, y, mLetterPaint);

        }

    }
}
