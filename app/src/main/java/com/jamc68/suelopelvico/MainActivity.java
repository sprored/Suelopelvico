package com.jamc68.suelopelvico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    // Fields
    int totalMinutes = 10;
    int turnCounter = 0;

    // States
    final int RELAXATION = 0;
    final int CONTRACTION = 1;

    // Timer constants
    final int RELAXATION_SECONDS = 12;
    final int CONTRACTION_SECONDS = 6;
    final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);



    Handler handler = new Handler();
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


    public void startCounter(View view) {
        handler.postDelayed(runnableCode, 1000);
    }


    public void exitApp(View view) {
        if (Build.VERSION.SDK_INT >= 21)
            finishAndRemoveTask();
        else
            finish();
        System.exit(0);
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
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
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

        ExerciseSession session = new ExerciseSession();

        TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
        textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + turnCounter + " / " + TOTAL_TURNS);

    }
}