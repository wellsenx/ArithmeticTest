package com.wellsen.arithmetictest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wellsen.arithmetictest.model.RandomEquation;

public class QuestionActivity extends Activity {

    private TextView solvedTextView;
    private TextView timeRemainingTextView;
    private TextView questionTextView;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    //private Button answerButton5;

    private final static long TIMER_VALUE = 60000; // 1 minute
    private static final String SOLVED_KEY = "solved";
    private static final String TOTAL_KEY = "total";
    private static final String TIMER_KEY = "timer";
    private static final String EQUATION_KEY = "equation";
    private static final String ANSWER_KEY = "answer";
    private static final String ANSWERS_KEY = "answers";
    private static final String DIFFICULTY_KEY = "difficulty_level";

    private static final int DIFFICULTY_EASY = 10;
    private static final int DIFFICULTY_MEDIUM = 20;
    private static final int DIFFICULTY_HARD = 40;
    private static final int DIFFICULTY_DEFAULT = DIFFICULTY_MEDIUM;

    private long timeRemaining = TIMER_VALUE;
    private int solvedCount = 0;
    private int totalCount = 1;
    private RandomEquation randomEquation;
    private String equation;
    private int answer;
    private int[] answers;

    private int difficultyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        difficultyLevel = intent.getIntExtra(DIFFICULTY_KEY, DIFFICULTY_DEFAULT);

        initializeViews();

        if (savedInstanceState != null) {
            solvedCount = savedInstanceState.getInt(SOLVED_KEY);
            totalCount = savedInstanceState.getInt(TOTAL_KEY);
            timeRemaining = savedInstanceState.getLong(TIMER_KEY);
            equation = savedInstanceState.getString(EQUATION_KEY);
            answer = savedInstanceState.getInt(ANSWER_KEY);
            answers = savedInstanceState.getIntArray(ANSWERS_KEY);
            difficultyLevel = savedInstanceState.getInt(DIFFICULTY_KEY);
        } else {
            createNewEquation(difficultyLevel);
        }

        setViews();

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                if (((Button) v).getText() == Integer.toString(answer)) {
                    ++solvedCount;
                }

                solvedTextView.setText(solvedCount + " / " + totalCount);
                ++totalCount;
                createNewEquation(difficultyLevel);
            }
        };

        answerButton1.setOnClickListener(listener);
        answerButton2.setOnClickListener(listener);
        answerButton3.setOnClickListener(listener);
        answerButton4.setOnClickListener(listener);
        //answerButton5.setOnClickListener(listener);

        new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingTextView.setText(Long.toString(millisUntilFinished / 1000));
                timeRemaining = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionActivity.this,
                        ResultActivity.class);
                intent.putExtra(SOLVED_KEY, solvedCount);
                intent.putExtra(TOTAL_KEY, --totalCount); // current total not counted
                intent.putExtra(DIFFICULTY_KEY, difficultyLevel);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void createNewEquation(int difficulty) {
        randomEquation = new RandomEquation(difficulty);
        equation = randomEquation.equation;
        answer = randomEquation.answer;
        answers = randomEquation.answers;
        setViews();
    }

    private void initializeViews() {
        solvedTextView = (TextView) findViewById(R.id.questionSolved);
        timeRemainingTextView = (TextView) findViewById(R.id.timeRemaining);
        questionTextView = (TextView) findViewById(R.id.questionEquation);
        answerButton1 = (Button) findViewById(R.id.answerButton1);
        answerButton2 = (Button) findViewById(R.id.answerButton2);
        answerButton3 = (Button) findViewById(R.id.answerButton3);
        answerButton4 = (Button) findViewById(R.id.answerButton4);
        //answerButton5 = (Button) findViewById(R.id.answerButton5);
    }

    private void setViews() {
        setTitle("Question " + totalCount);
        solvedTextView.setText(solvedCount + " / " + totalCount);
        questionTextView.setText(equation);
        answerButton1.setText(Integer.toString(answers[0]));
        answerButton2.setText(Integer.toString(answers[1]));
        answerButton3.setText(Integer.toString(answers[2]));
        answerButton4.setText(Integer.toString(answers[3]));
        //answerButton5.setText(Integer.toString(answers[4]));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SOLVED_KEY, solvedCount);
        outState.putInt(TOTAL_KEY, totalCount);
        outState.putLong(TIMER_KEY, timeRemaining);
        outState.putString(EQUATION_KEY, equation);
        outState.putInt(ANSWER_KEY, answer);
        outState.putIntArray(ANSWERS_KEY, answers);
        outState.putInt(DIFFICULTY_KEY, difficultyLevel);
    }
}
