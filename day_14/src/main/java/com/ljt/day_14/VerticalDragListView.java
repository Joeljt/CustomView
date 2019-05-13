package com.ljt.day_14;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by JoeLjt on 2019/5/13.
 * Email: lijiateng1219@gmail.com
 * Description:
 * <p>
 * 1. 后面不能拖动
 * 2. 列表只能垂直拖动
 * 3. 垂直拖动的范围只能是后面菜单 View 的高度
 * 4. 手指松开的结果要么是展开，要么是隐藏
 */

public class VerticalDragListView extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private View mDragTargetView;
    private int mDragDistance;

    // 手指按下的位置
    private float mDownY = 0;

    // 菜单栏是否完全打开
    private boolean isMenuFullyOpen;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) {
            throw new RuntimeException();
        }

        mDragTargetView = getChildAt(1);
        mDragTargetView.post(new Runnable() {
            @Override
            public void run() {
                mDragDistance = getChildAt(0).getMeasuredHeight();
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        // 背景菜单完整展示时，完全拦截掉 ListView 的事件
        if (isMenuFullyOpen) {
            return true;
        }

        // 当处于列表顶部，并仍然向上滑动时，拦截 ListView 的事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                // 事件的拦截发生在 MOVE 事件
                // MOVE return true 后，调用自己的 onTouchEvent ，传递给 ViewDragHelper，得不到 DOWN 事件
                // 让 ViewDragHelper 获取到完整的 DOWN 事件
                mDragHelper.processTouchEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                float mMoveY = event.getY();
                if (mMoveY - mDownY > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 指定某个子 View 是否可以拖动，就是 child
            return child == mDragTargetView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            if (top < 0) {
                top = 0;
            }

            if (top >= mDragDistance) {
                top = mDragDistance;
            }

            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int top = releasedChild.getTop();

            if (top <= mDragDistance / 2) {
                mDragHelper.settleCapturedViewAt(0, 0);
                isMenuFullyOpen = false;
            } else {
                mDragHelper.settleCapturedViewAt(0, mDragDistance);
                isMenuFullyOpen = true;
            }

            invalidate();

        }

    };

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mDragTargetView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragTargetView, -1);
        }
        return ViewCompat.canScrollVertically(mDragTargetView, -1);
    }

}
