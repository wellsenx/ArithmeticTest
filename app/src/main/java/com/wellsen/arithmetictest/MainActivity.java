package com.wellsen.arithmetictest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private int highScore;
    private TextView highScoreTextView;
    private TextView difficultyTextView;
    private int difficultyLevel;

    private static final String SHARED_PREFERENCES_NAME = "com.wellsen.arithmetictest";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String DIFFICULTY_KEY = "difficulty_level";

    private static final int DIFFICULTY_EASY = 10;
    private static final int DIFFICULTY_MEDIUM = 20;
    private static final int DIFFICULTY_HARD = 40;
    private static final int DIFFICULTY_DEFAULT = DIFFICULTY_MEDIUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        initializeViews();
        showHighScore();
        showDifficulty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.clear_high_score:
                clearHighScoreAlert();
                return true;
            case R.id.set_difficulty:
                setDifficultyDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startTest(View v) {
        Intent intent = new Intent(MainActivity.this,
                QuestionActivity.class);
        intent.putExtra(DIFFICULTY_KEY, difficultyLevel);
        startActivity(intent);
    }

    private void initializeViews() {
        highScoreTextView = (TextView) findViewById(R.id.highScoreTextView);
        difficultyTextView = (TextView) findViewById(R.id.difficultyTextView);
    }

    private void showHighScore() {

        highScore = prefs.getInt(HIGH_SCORE_KEY, 0);
        highScoreTextView.setText(Integer.toString(highScore));
    }

    private void showDifficulty() {
        difficultyLevel = prefs.getInt(DIFFICULTY_KEY, DIFFICULTY_DEFAULT);
        switch (difficultyLevel) {
            case DIFFICULTY_EASY:
                difficultyTextView.setText(R.string.easy);
                break;
            case DIFFICULTY_MEDIUM:
                difficultyTextView.setText(R.string.medium);
                break;
            case DIFFICULTY_HARD:
                difficultyTextView.setText(R.string.hard);
                break;
            default:
                difficultyTextView.setText(R.string.medium);
                break;
        }

    }

    private void clearHighScoreAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.clear_high_score)
                .setMessage(R.string.clear_high_score_message)
                .setPositiveButton(R.string.yes_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearHighScore();
                    }
                })
                .setNegativeButton(R.string.no_string, null)
                .show();
    }

    private void clearHighScore() {
        prefs.edit()
                .remove(HIGH_SCORE_KEY)
                .commit();

        Toast.makeText(getApplicationContext(),
                R.string.high_score_cleared_message,
                Toast.LENGTH_LONG).show();

        showHighScore();
    }

    private void setDifficultyDialog() {
        final CharSequence[] items = {
                getString(R.string.easy),
                getString(R.string.medium),
                getString(R.string.hard) };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.set_difficulty);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        setDifficulty(DIFFICULTY_EASY);
                        break;
                    case 1:
                        setDifficulty(DIFFICULTY_MEDIUM);
                        break;
                    case 2:
                        setDifficulty(DIFFICULTY_HARD);
                        break;
                    default:
                        setDifficulty(DIFFICULTY_DEFAULT);
                        break;
                }
            }
        });
        builder.setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, difficulty already set
            }
        });

        AlertDialog difficultyDialog;
        difficultyDialog = builder.create();
        difficultyDialog.show();
    }

    private void setDifficulty(int difficulty) {
        difficultyLevel = difficulty;

        editor = getSharedPreferences(SHARED_PREFERENCES_NAME,
                MODE_PRIVATE)
                .edit();
        editor.putInt(DIFFICULTY_KEY, difficultyLevel);
        editor.commit();

        showDifficulty();
    }
}
