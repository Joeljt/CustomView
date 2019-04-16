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
 * 3. 利用 adapter 来实现，仿照 ListView 实现
 *
 */

public class ListDataScreenView extends LinearLayout{

    private LinearLayout mMenuTabLayout;
    private FrameLayout mMenuMiddleLayout;
    private View mShadowView;
    private FrameLayout mMenuContainerView;
    private String mShadowViewBgColor = "#999999";

    private int mMenuContainerHeight;

    private BaseMenuAdapter mMenuAdapter;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        mMenuContainerHeight = (int) (height * 0.75);
        ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
        layoutParams.height = mMenuContainerHeight;
        mMenuContainerView.setLayoutParams(layoutParams);
        mMenuContainerView.setTranslationY(-mMenuContainerHeight);

    }

    public void setMenuAdapter(BaseMenuAdapter adapter) {
        this.mMenuAdapter = adapter;
        initDataFromAdapter(adapter);
    }

    private void initDataFromAdapter(BaseMenuAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {

            // tab view
            View tabView = adapter.getTabView(i, mMenuTabLayout);
            LayoutParams params =
                    new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            tabView.setLayoutParams(params);
            mMenuTabLayout.addView(tabView);

            // content view
            View menuView = adapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);

        }
    }

    private void initLayout() {

        setOrientation(VERTICAL);

        // 1. 创建头部用来存放 tab
        mMenuTabLayout = new LinearLayout(getContext());
        mMenuTabLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabLayout);

        // 2. 创建内容布局
        mMenuMiddleLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        mMenuMiddleLayout.setLayoutParams(params);

        addView(mMenuMiddleLayout);

        // 创建阴影布局，直接添加进 frameLayout 中，默认就是 MATCH_PARENT
        // 开始不展示遮罩层，等点击条目时才展示
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(Color.parseColor(mShadowViewBgColor));
        mShadowView.setAlpha(0f);
//        mShadowView.setVisibility(GONE);
        mMenuMiddleLayout.addView(mShadowView);

        // 3. 创建内容布局
        mMenuContainerView = new FrameLayout(getContext());
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleLayout.addView(mMenuContainerView);

    }

}
