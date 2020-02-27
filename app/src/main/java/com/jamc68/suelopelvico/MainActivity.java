package com.jamc68.suelopelvico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int totalMinutes = 10;
    private static final int RELAXATION_SECONDS = 12;
    private static final int CONTRACTION_SECONDS = 6;

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences setupSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
    }

    // Fields for runnable code
    private static int turnCounter = 0;
    private static final boolean isRelaxed = true;
    private static final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);
    Handler handler = new Handler();

    // Start runnableCode
    boolean state = isRelaxed;
    Runnable runnableExerciseSession = new Runnable() {
        private MediaPlayer mediaPlayer;

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            if ((state == isRelaxed) && (turnCounter < TOTAL_TURNS)) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.contract);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = !isRelaxed;
                turnCounter = turnCounter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setTextColor(getResources().getColor(R.color.secondaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText(
                        getString(R.string.secondary_message, turnCounter, TOTAL_TURNS));
                handler.postDelayed(runnableExerciseSession, CONTRACTION_SECONDS * 1000);

            } else if ((state == !isRelaxed) && (turnCounter < TOTAL_TURNS)) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.relax);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = isRelaxed;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableExerciseSession, RELAXATION_SECONDS * 1000);

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


    public void startCounter(View view) {
        handler.postDelayed(runnableExerciseSession, 1000);
    }

    public void pauseCounter(View view) {
        handler.removeCallbacks(runnableExerciseSession);
    }

    public void exitApp(View view) {
        handler.removeCallbacks(runnableExerciseSession);
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, MenuActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_help:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
        textViewSecondary.setText(getString(R.string.secundary_intro_message,totalMinutes ));
    }
}

