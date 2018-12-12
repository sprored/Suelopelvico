package com.example.sprocket.suelopelvico;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int RELAXATION = 0;
    final int RELAXATION_SECONDS = 12;
    final int CONTRACTION = 1;
    final int CONTRACTION_SECONDS = 6;
    final int TOTAL_MINUTES = 10;
    final int TOTAL_TURNS = (TOTAL_MINUTES * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);

    Handler handler = new Handler();
    int state = 0;
    int counter = 0;
    private Runnable runnableCode = new Runnable() {

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
            MediaPlayer mpContraction = MediaPlayer.create(getApplicationContext(), R.raw.contract);
            MediaPlayer mpRelaxation = MediaPlayer.create(getApplicationContext(), R.raw.relax);
            MediaPlayer mpFinal = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);

            if ((state == RELAXATION) && (counter < TOTAL_TURNS)) {
                mpContraction.start();
                state = CONTRACTION;
                counter = counter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
                handler.postDelayed(runnableCode, CONTRACTION_SECONDS * 1000);

            } else if ((state == CONTRACTION) && (counter < TOTAL_TURNS)) {
                mpRelaxation.start();
                state = RELAXATION;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableCode, RELAXATION_SECONDS * 1000);

            } else {
                mpFinal.start();
                textViewMain.setText(R.string.end_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
                mpContraction.release();
                mpContraction = null;
                mpRelaxation.release();
                mpRelaxation = null;
                mpFinal = null;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
        textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);

    }


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

}
