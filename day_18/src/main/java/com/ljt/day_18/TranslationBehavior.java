package com.ljt.day_18;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by lijiateng on 2019/3/18.
 */

public class TranslationBehavior extends FloatingActionButton.Behavior {

    private boolean isOut = false;

    public TranslationBehavior() {
    }

    public TranslationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed        屏幕跟随手指触摸滚动的距离 - 垂直方向
     * @param dxUnconsumed
     * @param dyUnconsumed      屏幕滚动到底，但是手指还在滑动的距离 - 垂直方向
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        Log.e("ljt", String.format("dxConsumed is %d, dyConsumed is %d, dxUnconsumed is %d, dyUnconsumed is %d", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed));

        if (dyConsumed > 0) {
            // dyConsumed > 0 说明手指在向下滑动，target 应该滑出屏幕
            if (!isOut) {

                LinearLayout llBottom = coordinatorLayout.findViewById(R.id.bottom_tab_layout);
                int bottomHeight = llBottom.getMeasuredHeight();
                llBottom.animate().translationY(bottomHeight).setDuration(300).start();

                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                int translationY = layoutParams.bottomMargin + child.getMeasuredHeight();
                child.animate().translationY(translationY).setDuration(300).start();
                isOut = true;
            }

        } else {
            // dyConsumed < 0 说明手指在向上滑动，target 应该滑入屏幕
            if (isOut) {

                LinearLayout llBottom = coordinatorLayout.findViewById(R.id.bottom_tab_layout);
                llBottom.animate().translationY(0).setDuration(300).start();

                child.animate().translationY(0).setDuration(300).start();
                isOut = false;

            }

        }

    }
}
