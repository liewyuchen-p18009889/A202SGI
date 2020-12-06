package com.example.user.quizproject;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTvQuestion, mTvQuestionNum, mTvTimer;
    private Button mBtnOption1, mBtnOption2, mBtnOption3, mBtnOption4;
    private List<Question> mQuestionList;
    private int questionNum, score;
    private CountDownTimer cdt;
    private int setNum;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mTvQuestion = (TextView) findViewById(R.id.tvQuestion);
        mTvQuestionNum = (TextView) findViewById(R.id.tvQuestionNum);
        mTvTimer = (TextView) findViewById(R.id.tvTimer);

        mBtnOption1 = (Button) findViewById(R.id.btnOption1);
        mBtnOption2 = (Button) findViewById(R.id.btnOption2);
        mBtnOption3 = (Button) findViewById(R.id.btnOption3);
        mBtnOption4 = (Button) findViewById(R.id.btnOption4);

        Typeface tf = ResourcesCompat.getFont(this, R.font.font2);
        mTvQuestion.setTypeface(tf);
        mTvQuestionNum.setTypeface(tf);
        mTvTimer.setTypeface(tf);
        mBtnOption1.setTypeface(tf);
        mBtnOption2.setTypeface(tf);
        mBtnOption3.setTypeface(tf);
        mBtnOption4.setTypeface(tf);

        mBtnOption1.setOnClickListener(this);
        mBtnOption2.setOnClickListener(this);
        mBtnOption3.setOnClickListener(this);
        mBtnOption4.setOnClickListener(this);

        setNum = getIntent().getIntExtra("SET_NO", 1);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("sets").child(String.valueOf(setNum)).child("questions");

        getQuestionsList();

        score = 0;
    }

    private void getQuestionsList(){
        mQuestionList = new ArrayList<>();

        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    Question q = childSnapshot.getValue(Question.class);
                    mQuestionList.add(new Question(q.getQuestion(), q.getOptionA(), q.getOptionB(),
                            q.getOptionC(), q.getOptionD(), q.getCorrectAns()));
                }
                setQuestion();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QuestionActivity.this, error.toString(), Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    private void setQuestion(){
        mTvTimer.setText(String.valueOf(10));

        mTvQuestion.setText(mQuestionList.get(0).getQuestion());
        mBtnOption1.setText(mQuestionList.get(0).getOptionA());
        mBtnOption2.setText(mQuestionList.get(0).getOptionB());
        mBtnOption3.setText(mQuestionList.get(0).getOptionC());
        mBtnOption4.setText(mQuestionList.get(0).getOptionD());

        mTvTimer.setText(String.valueOf(1) + "/" + String.valueOf(mQuestionList.size()));

        startTimer();

        questionNum = 0;
    }

    private void startTimer(){
        cdt = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };

        cdt.start();
    }

    @Override
    public void onClick(View v) {
        int option=0;

        switch(v.getId()){
            case R.id.btnOption1:
                option = 1;
                break;

            case R.id.btnOption2:
                option = 2;
                break;

            case R.id.btnOption3:
                option = 3;
                break;

            case R.id.btnOption4:
                option = 4;
                break;

                default:
        }

        cdt.cancel();
        checkAnswer(option, v);
    }

    private void checkAnswer(int option, View view){
        ColorStateList green = ColorStateList.valueOf(Color.parseColor("#47e679"));
        ColorStateList red = ColorStateList.valueOf(Color.parseColor("#e64747"));

        if(option == mQuestionList.get(questionNum).getCorrectAns()){
            //answer correctly
            ((Button)view).setBackgroundTintList(green);
            score++;
        }else {
            //answer wrongly
            ((Button)view).setBackgroundTintList(red);

            switch (mQuestionList.get(questionNum).getCorrectAns()){
                case 1:
                    mBtnOption1.setBackgroundTintList(green);
                    break;

                case 2:
                    mBtnOption2.setBackgroundTintList(green);
                    break;

                case 3:
                    mBtnOption3.setBackgroundTintList(green);
                    break;

                case 4:
                    mBtnOption4.setBackgroundTintList(green);
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 2000);
    }

    private void changeQuestion(){
        if(questionNum < mQuestionList.size()-1){
            questionNum++;

            displayAnim(mTvQuestion, 0, 0);
            displayAnim(mBtnOption1, 0, 1);
            displayAnim(mBtnOption2, 0, 2);
            displayAnim(mBtnOption3, 0, 3);
            displayAnim(mBtnOption4, 0, 4);

            mTvQuestionNum.setText(String.valueOf(questionNum+1) + " / "
                    + String.valueOf(mQuestionList.size()));
            mTvTimer.setText(String.valueOf(10));
            startTimer();

        }else{
            //proceed to score activity
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("correctCount", score);
            intent.putExtra("SCORE", String.valueOf(score) + " / "
                    + String.valueOf(mQuestionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void displayAnim(final View view, final int value, final int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
        .setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(value==0){
                    switch (viewNum){
                        case 0:
                            ((TextView)view).setText(mQuestionList.get(questionNum).getQuestion());
                            break;

                        case 1:
                            ((Button)view).setText(mQuestionList.get(questionNum).getOptionA());
                            break;

                        case 2:
                            ((Button)view).setText(mQuestionList.get(questionNum).getOptionB());
                            break;

                        case 3:
                            ((Button)view).setText(mQuestionList.get(questionNum).getOptionC());
                            break;

                        case 4:
                            ((Button)view).setText(mQuestionList.get(questionNum).getOptionD());
                            break;
                    }

                    if(viewNum != 0){
                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffb545")));
                    }

                    displayAnim(view, 1, viewNum);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        cdt.cancel();
    }
}
