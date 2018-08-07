package com.ljt.day_06;

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
import android.view.MotionEvent;
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
    private int itemHeight;

    private int mCurrPos;

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
        mLetterPaint.setTextSize(px2sp(12));

        mFocusedLetterPaint = new Paint();
        mFocusedLetterPaint.setAntiAlias(true);
        mFocusedLetterPaint.setColor(letterFocusedColor);
        mFocusedLetterPaint.setTextSize(px2sp(12));

    }

    private float px2sp(int px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 高度可以直接获取
        // 宽度需要进行适当的计算：getPaddingLeft + 一个字母的宽度 + getPaddingRight

        int height = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
        int width = (int) (getPaddingLeft() + mLetterPaint.measureText("A") + getPaddingRight());

        setMeasuredDimension(width, height);

        // 总高度除以字母总数，得到单个字母的高度
        itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制 26 个英文字母
        for (int i = 0; i < mLetters.length; i++) {

            // x 坐标的位置 -> (控件的宽度 - 字母宽度) 的一半
            int x = (int) (getWidth() / 2 - mLetterPaint.measureText(mLetters[i]) / 2);

            // 基线的位置，需要根据字母数量和当前位置来不断计算得到
            // 当前字母的基线 = 当前字母之前的所有字母的高度 + 当前字母的基线

            Paint.FontMetricsInt fontMetricsInt = mLetterPaint.getFontMetricsInt();
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            int y = i * itemHeight + itemHeight / 2 + dy;
//            canvas.drawText(mLetters[i], x, y, mLetterPaint);

//            Rect bounds = new Rect();
//            mLetterPaint.getTextBounds(mLetters[i], 0, mLetters[i].length(), bounds);
//
//            x = getWidth() / 2 - bounds.width() / 2 - bounds.left;
//            y = i * itemHeight - bounds.top + getPaddingTop();
//
//            Log.e("LJT", String.format("first y is %d, second y is %d, itemHeight is %d",
//                    i * itemHeight + itemHeight / 2 + dy,
//                    i * itemHeight - bounds.top,
//                    itemHeight));

//            canvas.drawText(mLetters[i], x, y, mLetterPaint);

            // 使用不同的画笔，完成界面的更新
            if (mCurrPos == i) {
                canvas.drawText(mLetters[i], x, y, mFocusedLetterPaint);
            } else {
                canvas.drawText(mLetters[i], x, y, mLetterPaint);
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                // 触摸的位置除以单个字母的高度，就可以得到对应的字母位置
                float eventY = event.getY();

                // 屏蔽非法值
                if (eventY < 0) {
                    eventY = 0;
                } else if (eventY > getHeight()) {
                    eventY = getHeight();
                }

                int currPosition = (int) (eventY / itemHeight);

                if (mListener != null) {
                    mListener.onTouched(mLetters[currPosition]);
                }

                // 减少 onDraw() 方法的调用
                if (mCurrPos == currPosition) return true;

                mCurrPos = currPosition;

                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.fingerUp();
                        }
                    }
                }, 100);
                break;
        }

        return true;
    }

    public interface OnTouchedListener {
        void onTouched(CharSequence letter);

        void fingerUp();
    }

    private OnTouchedListener mListener;

    public void setOnTouchedListener(OnTouchedListener l) {
        this.mListener = l;
    }

}
