package com.ljt.day_22;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by JoeLjt on 2019/4/16.
 * Email: lijiateng1219@gmail.com
 * Description: 使用代码创建布局
 *
 * 1. 头部用来存放 tab；
 * 2. 下面一个 FrameLayout 用来存放阴影（View）+ 菜单内容布局
 *
 */

public class ListDataScreenView extends LinearLayout{

    private LinearLayout mMenuTabLayout;
    private FrameLayout mMenuMiddleLayout;
    private View mShadowView;
    private FrameLayout mMenuContainerView;
    private String mShadowViewBgColor = "#999999";

    public ListDataScreenView(Context context) {
        this(context, null);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();

    }

    private void initLayout() {

        // 1. 创建头部用来存放 tab
        mMenuTabLayout = new LinearLayout(getContext());
        mMenuTabLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabLayout);

        // 2. 创建内容布局
        mMenuMiddleLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        mMenuMiddleLayout.setLayoutParams(params);
        addView(mMenuMiddleLayout);

        // 创建阴影布局，直接添加进 frameLayout 中，默认就是 MATCH_PARENT
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(Color.parseColor(mShadowViewBgColor));
        mMenuMiddleLayout.addView(mShadowView);

        // 创建内容布局
        mMenuContainerView = new FrameLayout(getContext());

        mMenuMiddleLayout.addView(mMenuContainerView);


    }

}
