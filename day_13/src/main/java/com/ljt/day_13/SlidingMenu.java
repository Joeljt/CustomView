package com.ljt.day_13;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import java.lang.reflect.Type;

/**
 * Created by JoeLjt on 2019/5/13.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class SlidingMenu extends HorizontalScrollView {

    private int mMenuWidth;

    private View mMenuView;
    private View mContentView;

    private boolean mMenuOpened;

    private GestureDetector mDetector;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);

        float menuRightMargin = typedArray.getDimension(R.styleable.SlidingMenu_menuRightMargin, dip2px(50));
        mMenuWidth = (int) (getScreenWidth(context) - menuRightMargin);

        typedArray.recycle();

        // 初始化 GestureDetector
        mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                // 只要有快速滑动，就会回调这个方法
                // 快速向左滑动时，velocityX 为负；向右滑动为正

                Log.e("LJT", String.format("velocityX is %s, velocityY is %s", velocityX, velocityY));

                if (mMenuOpened) {
                    if (velocityX > 0) {
                        closeMenu();
                    }
                } else {
                    if (velocityX < 0) {
                        openMenu();
                    }
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                getResources().getDisplayMetrics());
    }

    // 1. 宽高不对，整个乱套了 -> 指定宽高

    // 布局加载完成后会调用这个方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ViewGroup linearLayout = (ViewGroup) getChildAt(0);

        // 1.菜单页的宽度是屏幕宽度减去一小部分距离
        mMenuView = linearLayout.getChildAt(0);
        ViewGroup.LayoutParams menuParam = mMenuView.getLayoutParams();
        menuParam.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParam);

        // 2. 内容页的宽度是屏幕的宽度
        mContentView = linearLayout.getChildAt(1);
        ViewGroup.LayoutParams contentParam = mContentView.getLayoutParams();
        contentParam.width = getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParam);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 开始是隐藏的
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mDetector.onTouchEvent(ev)) {
            return true;
        }

        // 手指抬起是二选一，要么打开菜单，要么关闭菜单
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getScrollX() < mMenuWidth / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }
                // 确保 super.onTouchEvent(ev) 不执行，否则会重复调用 mScroller 的方法
                return true;
        }

        return super.onTouchEvent(ev);
    }

    // 处理滚动过程中左右两侧的缩放效果以及透明度变化
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // 计算一个梯度值
        float scale = 1.0f * l / mMenuWidth; // l 从 0 到 mMenuWidth, scale 从 1 到 0
        // 右边的缩放值从 0.7 到 1
        float rightScale = (float) (0.7 + 0.3 * scale);

        // 默认以当前 View 的中心点缩放，重新设置中心点为左边中间位置
        mContentView.setPivotX(0);
        mContentView.setPivotY(mContentView.getMeasuredHeight() / 2);
        mContentView.setScaleX(rightScale);
        mContentView.setScaleY(rightScale);

        // 设置菜单缩放以及透明度
        float leftScale = 0.7f + (1 - scale) * 0.3f;
        mMenuView.setScaleX(leftScale);
        mMenuView.setScaleY(leftScale);

    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuOpened = false;
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        mMenuOpened = true;
    }

}
