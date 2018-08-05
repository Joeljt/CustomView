package com.ljt.view.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/08/05, 11:47
 * For：
 */

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.makeMeasureSpec(200 >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, height);
    }
}
