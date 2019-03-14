package com.ljt.day_27;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by lijiateng on 2019/3/14.
 */

public class StatusBarUtil {

    /**
     * 为状态栏设置颜色
     * 1. 5.0 以后有专门的方法设置
     * 2. 4.4-5.0 将 activity status bar 设为透明，然后在 DecorView 加个指定颜色的 View 实现
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0 以上
            activity.getWindow().setStatusBarColor(color);
        }

        // 4.4
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // 让 statusBar 透明，然后给 DecorView add 一个指定颜色的、与 statusBar 同高的 View
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
            view.setBackgroundColor(color);
            decorView.addView(view);

            // 获取到自己的布局，然后设置 fitsSystemWindow，或者给自己的布局设置 paddingTop
            ViewGroup contentView = decorView.findViewById(android.R.id.content);
            View rootView = contentView.getChildAt(0);
            rootView.setFitsSystemWindows(true);

        }
    }

    /**
     * 使用 Resources 获取状态栏的高度，非反射
     *
     * @param activity
     * @return
     */
    private static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int statusBarHeightId = resources
                .getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

    /**
     * 设置 activity 的状态栏透明
     *
     * @param activity
     */
    public static void setActivityTranslucent(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
