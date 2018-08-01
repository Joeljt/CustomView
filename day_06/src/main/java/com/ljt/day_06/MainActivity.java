package com.ljt.day_06;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private LetterSlideBar mLsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView)findViewById(R.id.tv_test);
        mLsb = (LetterSlideBar) findViewById(R.id.lsb);

        mLsb.setOnTouchedListener(new LetterSlideBar.OnTouchedListener() {
            @Override
            public void onTouched(CharSequence letter) {
                mTv.setText(letter);
                mTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void fingerUp() {

                mTv.setVisibility(View.GONE);
            }
        });

    }
}
