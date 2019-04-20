package com.joeljt.day_15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LockPatternView view = findViewById(R.id.lock_view);
        view.setOnLockPatternFinishedListener(new OnLockPatternFinishedListener() {
            @Override
            public void onLockPatternFinished(@NotNull String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
