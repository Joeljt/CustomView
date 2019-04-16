package com.ljt.day_22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListDataScreenView view = findViewById(R.id.screen_view);
        view.setMenuAdapter(new MyMenuAdapter(this));


    }

    public void test(View view) {
        Toast.makeText(MainActivity.this, "MainActivity", Toast.LENGTH_LONG).show();
    }

}
