package com.ljt.day_27.parallax;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by JoeLjt on 2019/4/11.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<ParallaxFragment> mFragments;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(FragmentManager fm, List<ParallaxFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
