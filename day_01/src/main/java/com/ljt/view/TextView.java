package com.ljt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/07/23, 07:16
 * For：自定义 TextView
 */

public class TextView extends View {

    private String mText;
    private int mTextSize = 15;
    private int mTextColor;

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

        mTextColor = typedArray.getColor(R.styleable.JoeTextView_JoeTextColor, Color.BLACK);
//        mTextSize = typedArray.getDimensionPixelSize()

        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // mode: AT_MOST, EXACTLY, UNSECIFIED
        // AT_MOST 一般对应 wrap_content, EXACTLY 对应具体的数值, unspecified 一般不使用，系统控件才会用到


    }
}
