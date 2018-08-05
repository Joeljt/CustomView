package com.ljt.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ljt.view.test.MyAdapter;
import com.ljt.view.test.MyListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        MyListView listView = findViewById(R.id.lv_test);

        String[] arr = {"123", "234", "3424"};

        listView.setAdapter(new MyAdapter(this, arr));

    }
}
