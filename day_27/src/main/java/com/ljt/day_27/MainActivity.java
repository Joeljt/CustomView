package com.ljt.day_27;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ljt.day_27.loading.LoadingView;
import com.ljt.day_27.parallax.MyPagerAdapter;
import com.ljt.day_27.parallax.ParallaxViewPager;

public class MainActivity extends AppCompatActivity {

    private ParallaxViewPager mVp;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVp = findViewById(R.id.parallax_vp);
        mLoadingView = findViewById(R.id.loading_view);
//        mLoadingView.setVisibility(View.GONE);

        // view 层不做过多的工作，只设置布局 id 集合
        mVp.setLayoutIds(getSupportFragmentManager(),
                new int[]{R.layout.fragment_page_first, R.layout.fragment_page_second, R.layout.fragment_page_third});

        mVp.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingView.disappear();
            }
        }, 2000);

    }



}
