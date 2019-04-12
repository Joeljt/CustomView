package com.ljt.day_27.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ljt.day_27.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoeLjt on 2019/4/11.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class ParallaxViewPager extends ViewPager {

    private List<ParallaxFragment> mFragments;

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFragments = new ArrayList<>();
    }

    /**
     * 布局 id
     *
     * @param layoutIds
     */
    public void setLayoutIds(FragmentManager fm, int[] layoutIds) {

        // 1. 声明 Fragment
        for (int layoutId : layoutIds) {

            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ParallaxFragment.LAYOUT_ID_KEY, layoutId);
            fragment.setArguments(bundle);

            mFragments.add(fragment);
        }

        // 2. 设置 adapter
        setAdapter(new MyPagerAdapter(fm, mFragments));

        // 3. 设置 ViewPager 滚动的监听
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // position, 当前位置；positionOffset, 滚动的百分比，0-1；positionOffsetPixels，滚动的距离，0-屏幕距离

                // 同时设置左右两侧的 fragment，左边退出，右边进入
                ParallaxFragment outFragment = mFragments.get(position);


                List<View> outFragmentParallaxViews = outFragment.getParallaxViews();
                for (View view : outFragmentParallaxViews) {
                    ParallaxTag parallaxTag = (ParallaxTag) view.getTag(R.id.parallax_tag);
                    view.setTranslationX(( -positionOffsetPixels) * parallaxTag.getxOut());
                    view.setTranslationY(( -positionOffsetPixels) * parallaxTag.getyOut());
                }

                try {
                    ParallaxFragment inFragment = mFragments.get(position + 1);
                    outFragmentParallaxViews = inFragment.getParallaxViews();
                    for (View view : outFragmentParallaxViews) {
                        ParallaxTag parallaxTag = (ParallaxTag) view.getTag(R.id.parallax_tag);
                        view.setTranslationX((getMeasuredWidth() - positionOffsetPixels) * parallaxTag.getxIn());
                        view.setTranslationY((getMeasuredWidth() - positionOffsetPixels) * parallaxTag.getyIn());
                    }
                } catch (Exception e) {}

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
