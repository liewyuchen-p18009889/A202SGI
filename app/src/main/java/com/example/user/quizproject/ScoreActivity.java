package com.example.user.quizproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView mTvScoreMsg, mTvScoreTitle, mTvScore;
    private Button mBtnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mTvScoreMsg = (TextView) findViewById(R.id.tvScoreMsg);
        mTvScoreTitle = (TextView) findViewById(R.id.tvScoreTitle);
        mTvScore = (TextView) findViewById(R.id.tvScore);
        mBtnDone = (Button) findViewById(R.id.btnDone);

        Typeface tf = ResourcesCompat.getFont(this, R.font.font2);
        mTvScoreMsg.setTypeface(tf);
        mTvScoreTitle.setTypeface(tf);
        mTvScore.setTypeface(tf);
        mBtnDone.setTypeface(tf);

        String strScore = getIntent().getStringExtra("SCORE");
        int correctCount = getIntent().getIntExtra("correctCount", 1);

        mTvScore.setText(strScore);

        if(correctCount>=3 && correctCount<5){
            mTvScoreMsg.setText("You passed this quiz!");
        }else if(correctCount<3){
            mTvScoreMsg.setText("You failed this quiz!");
        }else if(correctCount==5){
            mTvScoreMsg.setText("Congratulations!");
        }

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                ScoreActivity.this.startActivity(intent);
                ScoreActivity.this.finish();
            }
        });
    }
}