package com.ljt.view.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ljt.view.R;

/**
 * Author: ljt@yonyou.com
 * Date&Time: 2018/08/05, 11:51
 * For：
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mStrings;

    public MyAdapter( Context mContext, String[] strings) {
        this.mContext = mContext;
        this.mStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return mStrings[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_test, null);

        MyTextView textView = convertView.findViewById(R.id.tv_test);
        textView.setText(mStrings[position]);

        return convertView;
    }
}
