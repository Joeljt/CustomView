package com.ljt.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTv = findViewById(R.id.tv_test);

        Log.e("LJT", "onCreate ---- > " + mTv.getHeight());

        mTv.post(new Runnable() {
            @Override
            public void run() {
                Log.e("LJT", "post ---- > " + mTv.getHeight());
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LJT", "onResume ---- > " + mTv.getHeight());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("LJT", "onAttachedToWindow ---- > " + mTv.getHeight());
    }



}
