package com.example.user.quizproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvMainTitle;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvMainTitle = (TextView) findViewById(R.id.mainTitle);
        mBtnStart = (Button) findViewById(R.id.btnStart);

        Typeface tf = ResourcesCompat.getFont(this, R.font.font2);
        mTvMainTitle.setTypeface(tf);
        mBtnStart.setTypeface(tf);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
            }
        });
    }
}