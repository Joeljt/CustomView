package com.ljt.day_22;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

    private static int ANIMATION_DURATION = 300;

    private LinearLayout mMenuTabLayout;
    private FrameLayout mMenuMiddleLayout;
    private View mShadowView;
    private FrameLayout mMenuContainerView;
    private int mShadowViewBgColor = 0x88888888;

    private int mMenuContainerHeight;

    // 当前打开的菜单栏位置信息，未打开就是 -1
    private int mCurrPosition = -1;

    private boolean isExecutingAnimator;
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

        // 设置 View 为 Gone 后，会触发重绘，会导致页面位置出现问题
        if (mMenuContainerHeight != 0) {
            return;
        }

        int height = MeasureSpec.getSize(heightMeasureSpec);
        mMenuContainerHeight = (int) (height * 0.75);
        ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
        layoutParams.height = mMenuContainerHeight;
        mMenuContainerView.setLayoutParams(layoutParams);
        mMenuContainerView.setTranslationY(-mMenuContainerHeight);

    }

    public void setMenuAdapter(BaseMenuAdapter adapter) {
        mMenuAdapter = adapter;
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

            // 为每个 tabView 设置点击事件
            onTabViewClicked(i, tabView);

            // content view
            View menuView = adapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);

        }
    }

    /**
     * 为每个 tabView 设置点击事件
     *
     * @param position
     * @param tabView
     */
    private void onTabViewClicked(final int position, final View tabView) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 没有菜单栏打开，点击后打开对应的菜单栏
                if (mCurrPosition == -1) {
                    openMenu(position, tabView);
                    return;
                }

                // 当前点击的就是已经打开的菜单页，那就关闭菜单
                if (mCurrPosition == position) {
                    closeMenu();
                }

                // 否则就直接切换页面即可
                if (mCurrPosition != position) {
                    // 把当前正显示的页面隐藏
                    View currOpenedMenu = mMenuContainerView.getChildAt(mCurrPosition);
                    currOpenedMenu.setVisibility(GONE);
                    mMenuAdapter.onMenuClosed(mMenuTabLayout.getChildAt(mCurrPosition));

                    View targetMenu = mMenuContainerView.getChildAt(position);
                    targetMenu.setVisibility(VISIBLE);
                    mMenuAdapter.onMenuOpened(mMenuTabLayout.getChildAt(position));

                    mCurrPosition = position;
                }

            }
        });
    }

    private void closeMenu() {

        if (isExecutingAnimator) {
            return;
        }

        ValueAnimator translationAnimator =
                ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);

        ValueAnimator alphaAnimator =
                ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                // 收起菜单后设置文字信息隐藏，否则会重叠
                View view = mMenuContainerView.getChildAt(mCurrPosition);
                view.setVisibility(GONE);
                mCurrPosition = -1;
                mShadowView.setVisibility(GONE); // 必须设置为 Gone，否则无法操作 contentView
                isExecutingAnimator = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isExecutingAnimator = true;
                mMenuAdapter.onMenuClosed(mMenuTabLayout.getChildAt(mCurrPosition));
            }

        });
        animatorSet.start();

    }

    /**
     * 打开菜单动画
     *
     * 菜单内容位移动画 + 背景遮罩透明度变化，一起执行
     *
     * @param position
     * @param tabView
     */
    private void openMenu(int position, final View tabView) {

        if (isExecutingAnimator) {
            return;
        }

        // 展示当前页的内容
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);

        ValueAnimator translationAnimator =
                ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);

        mShadowView.setVisibility(VISIBLE);
        ValueAnimator alphaAnimator =
                ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isExecutingAnimator = true;
                mMenuAdapter.onMenuOpened(tabView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExecutingAnimator = false;
            }

        });
        animatorSet.start();

        mCurrPosition = position;

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
        mShadowView.setBackgroundColor(mShadowViewBgColor);
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrPosition != -1) closeMenu();
            }
        });
        mMenuMiddleLayout.addView(mShadowView);

        // 3. 创建内容布局
        mMenuContainerView = new FrameLayout(getContext());
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleLayout.addView(mMenuContainerView);

    }

}
