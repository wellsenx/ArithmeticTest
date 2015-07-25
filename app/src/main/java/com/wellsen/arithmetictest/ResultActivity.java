package com.wellsen.arithmetictest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

    private int solvedCount;
    private int totalCount;
    private int score;
    private int highScore;
    private int accuracy;
    private int difficultyLevel;
    private int multiplier;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String SHARED_PREFERENCES_NAME = "com.wellsen.arithmetictest";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String SOLVED_KEY = "solved";
    private static final String TOTAL_KEY = "total";
    private static final String DIFFICULTY_KEY = "difficulty_level";

    private static final int DIFFICULTY_EASY = 10;
    private static final int DIFFICULTY_MEDIUM = 20;
    private static final int DIFFICULTY_HARD = 40;
    private static final int DIFFICULTY_DEFAULT = DIFFICULTY_MEDIUM;

    private static final int MULTIPLIER_EASY = 5;
    private static final int MULTIPLIER_MEDIUM = 10;
    private static final int MULTIPLIER_HARD = 20;

    private TextView scoreTextView;
    private TextView solvedTextView;
    private TextView totalTextView;
    private TextView accuracyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        solvedCount = intent.getIntExtra(SOLVED_KEY, 0);
        totalCount = intent.getIntExtra(TOTAL_KEY, 0);
        difficultyLevel = intent.getIntExtra(DIFFICULTY_KEY, DIFFICULTY_DEFAULT);

        multiplier = setMultiplier(difficultyLevel);

        accuracy = totalCount != 0 ? 100 * solvedCount / totalCount : 0;
        score = multiplier * accuracy * solvedCount / 10;

        prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        highScore = prefs.getInt(HIGH_SCORE_KEY, 0);

        if (score > highScore) {
            Toast.makeText(getApplicationContext(),
                    R.string.got_high_score_message,
                    Toast.LENGTH_LONG).show();

            editor = getSharedPreferences(SHARED_PREFERENCES_NAME,
                    MODE_PRIVATE)
                    .edit();
            editor.putInt(HIGH_SCORE_KEY, score);
            editor.commit();
        }

        initializeViews();
        setViews();

    }

    private int setMultiplier(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return MULTIPLIER_EASY;
            case DIFFICULTY_MEDIUM:
                return MULTIPLIER_MEDIUM;
            case DIFFICULTY_HARD:
                return MULTIPLIER_HARD;
            default:
                return MULTIPLIER_MEDIUM;
        }
    }

    private void initializeViews() {
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        solvedTextView = (TextView) findViewById(R.id.solvedTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
    }

    private void setViews() {
        scoreTextView.setText(Integer.toString(score));
        solvedTextView.setText(Integer.toString(solvedCount));
        totalTextView.setText(Integer.toString(totalCount));
        accuracyTextView.setText(Integer.toString(accuracy) + " %");
    }
}
