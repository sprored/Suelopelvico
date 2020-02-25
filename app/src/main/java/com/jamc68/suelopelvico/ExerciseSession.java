package com.jamc68.suelopelvico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ExerciseSession extends AppCompatActivity {
    // Fields
    private static int totalMinutes = 10;
    private static int turnCounter = 0;
    Handler handler = new Handler();

    // States
    private static final int RELAXATION = 0;
    private static final int CONTRACTION = 1;

    // Timer constants
    private static final int RELAXATION_SECONDS = 12;
    private static final int CONTRACTION_SECONDS = 6;
    private static final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);

    // Getters and Setters
    public static int getTotalMinutes() {
        return totalMinutes;
    }

    public static void setTotalMinutes(int totalMinutes) {
        ExerciseSession.totalMinutes = totalMinutes;
    }

    public static int getTurnCounter() {
        return turnCounter;
    }

    public static void setTurnCounter(int turnCounter) {
        ExerciseSession.turnCounter = turnCounter;
    }

    public static int getRELAXATION() {
        return RELAXATION;
    }

    public static int getCONTRACTION() {
        return CONTRACTION;
    }

    public static int getRELAXATION_SECONDS() {
        return RELAXATION_SECONDS;
    }

    public static int getCONTRACTION_SECONDS() {
        return CONTRACTION_SECONDS;
    }

    public static int getTOTAL_TURNS() {
        return TOTAL_TURNS;
    }


    int state = RELAXATION;

    // Start runnableCode
    private Runnable runnableCode = new Runnable() {
        private MediaPlayer mediaPlayer;

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            if ((state == RELAXATION) && (turnCounter < TOTAL_TURNS)) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.contract);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = CONTRACTION;
                turnCounter = turnCounter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setTextColor(getResources().getColor(R.color.secondaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + turnCounter + " / " + TOTAL_TURNS);
                handler.postDelayed(runnableCode, CONTRACTION_SECONDS * 1000);

            } else if ((state == CONTRACTION) && (turnCounter < TOTAL_TURNS)) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.relax);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = RELAXATION;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableCode, RELAXATION_SECONDS * 1000);

            } else {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                textViewMain.setText(R.string.end_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= 21) {
                finishAndRemoveTask();
            } else {
                finish();
                System.exit(0);
            }
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        handler.postDelayed(runnableCode, 1000);
    }
}
