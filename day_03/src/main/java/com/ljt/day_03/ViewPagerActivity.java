package com.ljt.day_03;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiateng on 2018/7/26.
 */

public class ViewPagerActivity extends AppCompatActivity {

    private String[] items = {"直播", "推荐", "视频", "图片", "段子", "精华"};
    private LinearLayout mIndicatorContainer;
    private List<ColorTrackTextView> mIndicators;
    private ViewPager mViewPager;

    private int currPosition = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager);

        mIndicators = new ArrayList<>();
        mIndicatorContainer = findViewById(R.id.indicator_view);
        mViewPager = findViewById(R.id.view_pager);

        initIndicator();
        initViewPager();

        // 默认选中推荐
        mViewPager.setCurrentItem(1);

    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // position -> 当前的位置
                // positionOffset -> 滚动的 0-1 百分比

                ColorTrackTextView left = mIndicators.get(position);
                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                left.setCurrProgress(1 - positionOffset);

                try {
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrProgress(positionOffset);
                } catch (Exception e) {

                }

            }

            @Override
            public void onPageSelected(int position) {

                mIndicators.get(currPosition).setTextSize(18);

                ColorTrackTextView colorTrackTextView = mIndicators.get(position);
                colorTrackTextView.setTextSize(19);

                currPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initIndicator() {
        for (int i = 0; i < items.length; i++) {

            // 动态添加颜色跟踪的 TextView
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.weight = 1;
            ColorTrackTextView view = new ColorTrackTextView(this);
            view.setTextSize(18);

            view.setChangeColor(Color.RED);
            view.setText(items[i]);
            view.setLayoutParams(params);
//            view.setBackgroundColor(Color.parseColor("#00FF00"));

            // 加入容器布局
            mIndicatorContainer.addView(view);

            // 加入集合
            mIndicators.add(view);

        }
    }

}
