package com.jamc68.suelopelvico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class ExerciseSession extends AppCompatActivity {
    // Fields
    private static int turnCounter = 0;
    Handler handler = new Handler();

    // Start runnableCode
    boolean state = ExerciseData.isRELAXED();
    private Runnable runnableExerciseSession = new Runnable() {
        private MediaPlayer mediaPlayer;
        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            if ((state == ExerciseData.isRELAXED()) && (turnCounter < ExerciseData.getTOTAL_TURNS())) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.contract);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = ExerciseData.isCONTRACTED();
                turnCounter = turnCounter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setTextColor(getResources().getColor(R.color.secondaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + turnCounter + " / " + ExerciseData.getTOTAL_TURNS());
                handler.postDelayed(runnableExerciseSession, ExerciseData.getCONTRACTION_SECONDS() * 1000);

            } else if ((state == ExerciseData.isCONTRACTED()) && (turnCounter < ExerciseData.getTOTAL_TURNS())) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.relax);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = ExerciseData.isRELAXED();
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableExerciseSession, ExerciseData.getRELAXATION_SECONDS() * 1000);

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
            NavUtils.navigateUpFromSameTask(this);
        }
        handler.removeCallbacks(runnableExerciseSession);
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        handler.postDelayed(runnableExerciseSession, 1000);
    }
}
