package com.ljt.day_03;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lijiateng on 2018/7/26.
 */

public class ItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, null);

        TextView textView = view.findViewById(R.id.text);
        Bundle arguments = getArguments();
        textView.setText(arguments.getString("title"));

        return view;
    }

    public static ItemFragment newInstance(String item) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", item);
        fragment.setArguments(bundle);
        return fragment;
    }

}
