package com.snowcoin.snowcoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;


public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.t1, null);
        View v2 = View.inflate(this, R.layout.t2, null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);

    }
}