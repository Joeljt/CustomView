package com.ljt.day_16;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.RelativeLayout;

/**
 * Created by JoeLjt on 2019/5/13.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class SlidingMenu extends HorizontalScrollView {

    private int mMenuWidth;

    private View mMenuView;
    private View mContentView;
    private View mShadowView;

    private boolean mMenuOpened;

    private boolean mIsIntercept;

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
                    if (velocityX < 0) {
                        closeMenu();
                        return true;
                    }
                } else {
                    if (velocityX > 0) {
                        openMenu();
                        return true;
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

        linearLayout.removeView(mContentView);

        RelativeLayout containerLayout = new RelativeLayout(getContext());
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(Color.parseColor("#55000000"));
        mShadowView.setAlpha(0.0f);

        containerLayout.addView(mContentView);
        containerLayout.addView(mShadowView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.width = getScreenWidth(getContext());
        containerLayout.setLayoutParams(layoutParams);
        linearLayout.addView(containerLayout);

//        ViewGroup.LayoutParams contentParam = mContentView.getLayoutParams();
//        contentParam.width = getScreenWidth(getContext());
//        containerLayout.setLayoutParams(contentParam);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 开始是隐藏的
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;

        // 菜单打开状态下，点击右侧关闭菜单；同时不能响应任何点击事件
        if (mMenuOpened && ev.getX() > mMenuWidth) {
            closeMenu();
            mIsIntercept = true;
            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mIsIntercept) {
            return true;
        }

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

        float scale = 1.0f *  l / mMenuWidth;
        float resultScale =  (1-scale);
        mShadowView.setAlpha(resultScale);

        mMenuView.setTranslationX(0.6f*l);

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
