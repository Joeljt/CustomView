package com.ljt.day_05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/07/30, 22:27
 * For：
 */

public class RatingBar extends View {

    private Bitmap mFocusedDrawable, mUnfocusedDrawable;

    private int mGradeLevel;

    private int mCurrGrade;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int focusedResourceId = typedArray.getResourceId(R.styleable.RatingBar_rating_bar_focused, 0);
        int unfocusedResourceId = typedArray.getResourceId(R.styleable.RatingBar_rating_bar_unfocused, 0);
        mGradeLevel = typedArray.getInt(R.styleable.RatingBar_rating_bar_grade_level, 0);
        
        // 强制校验
        if(focusedResourceId == 0 || unfocusedResourceId == 0 || mGradeLevel == 0) {
            throw new RuntimeException("Please checkout your xml!");
        }

        typedArray.recycle();

        // 获取星星的 Bitmap 对象
        mFocusedDrawable = BitmapFactory.decodeResource(getResources(), focusedResourceId);
        mUnfocusedDrawable = BitmapFactory.decodeResource(getResources(), unfocusedResourceId);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 宽应该是 一个星星的宽度 * 星星的数量；高应该是 星星的高度 即可
        int width = mFocusedDrawable.getWidth() * mGradeLevel;
        int height = mFocusedDrawable.getHeight();

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mGradeLevel; i++) {

            int x = mFocusedDrawable.getWidth() * i;

            if (mCurrGrade > i) {
                // 假设分数为 2 分，0 和 1 需要绘制 mFocusedDrawable，>1 的部分绘制 mUnfocusedDrawable
                canvas.drawBitmap(mFocusedDrawable, x, 0, null);
            } else {
                canvas.drawBitmap(mUnfocusedDrawable, x, 0, null);
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                // 修正 x 的值，有可能为负值
                float eventX = event.getX();
                if(eventX < 0) {
                    eventX = 0;
                }

                // 获取触摸位置的 x 坐标，与单个星星的宽度做除法，得到分数值
                // 除完以后需要有个 +1 的矫正操作
                int currGrade = (int) (eventX / mFocusedDrawable.getWidth() + 1);

                // 屏蔽非法值
                if(currGrade < 0) {
                    currGrade = 1;
                }else if(currGrade > mGradeLevel) {
                    currGrade = mGradeLevel;
                }

                // 分数如果相同，则不再进行重绘，避免 onDraw()方法的多次调用
                if(mCurrGrade == currGrade){
                    return true;
                }

                // 更新变量
                mCurrGrade = currGrade;

                // 更新当前分数后重新绘制页面
                invalidate();

        }

        // 自己消费触摸事件
        return true;
    }
}
