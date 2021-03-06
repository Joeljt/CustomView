package com.ljt.day_22;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JoeLjt on 2019/4/16.
 * Email: lijiateng1219@gmail.com
 *
 * Description: 菜单栏的通用 adapter
 *
 */

public abstract class BaseMenuAdapter {

    private MenuObserver mMenuObserver;

    public void registerMenuObserver(MenuObserver mMenuObserver) {
        this.mMenuObserver = mMenuObserver;
    }

    public void unRegisterMenuObserver(MenuObserver mMenuObserver) {
        this.mMenuObserver = null;
    }

    public void closeMenu() {
        if (mMenuObserver != null) {
            mMenuObserver.onMenuItemSelected();
        }
    }

    /**
     * 获取总共多少条
     * @return
     */
    public abstract int getCount();

    /**
     * 获取 tab
     * @param position
     * @param parent
     * @return
     */
    public abstract View getTabView(int position, ViewGroup parent);

    /**
     * 获取当前菜单的内容
     * @param position
     * @param parent
     * @return
     */
    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 菜单打开的回调
     *
     * @param tabView
     */
    public abstract void onMenuOpened(View tabView);

    /**
     * 菜单关闭的回调
     *
     * @param tabView
     */
    public abstract void onMenuClosed(View tabView);

}
