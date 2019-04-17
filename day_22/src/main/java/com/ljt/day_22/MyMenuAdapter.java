package com.ljt.day_22;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JoeLjt on 2019/4/16.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class MyMenuAdapter extends BaseMenuAdapter {

    private String[] mTabTitles = {"类型", "价格", "规格", "更多"};

    private Context mContext;
    private LayoutInflater mInflater;

    public MyMenuAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) mInflater.inflate(R.layout.ui_list_data_screen_tab, parent, false);
        tabView.setText(mTabTitles[position]);
        tabView.setTextColor(Color.BLACK);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        TextView menuView = (TextView) mInflater.inflate(R.layout.ui_list_data_screen_menu, parent, false);
        menuView.setText(mTabTitles[position]);

        // 真实场景下，选择完某个筛选条件以后，菜单要关闭
        // ---> 考虑使用观察者模式实现，参考 ListView
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        return menuView;
    }

    @Override
    public void onMenuOpened(View tabView) {
        TextView textView = (TextView) tabView;
        textView.setTextColor(Color.RED);
    }

    @Override
    public void onMenuClosed(View tabView) {
        TextView textView = (TextView) tabView;
        textView.setTextColor(Color.BLACK);
    }

}
