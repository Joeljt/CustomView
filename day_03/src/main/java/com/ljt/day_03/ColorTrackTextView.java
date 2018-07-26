package com.ljt.day_03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lijiateng on 2018/7/26.
 */

public class ColorTrackTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mOriginPaint, mChangePaint;

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
        paint.setDither(true); // 防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }


}
