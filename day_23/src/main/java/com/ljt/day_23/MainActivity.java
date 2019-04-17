package com.ljt.day_23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoadingView view = findViewById(R.id.ll_view);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.dismiss();
            }
        }, 1000);

    }
}
