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

    // Start runableCode
    private Runnable runnableCode = new Runnable() {
        private MediaPlayer mediaPlayer;

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            if ((state == RELAXATION) && (counter < TOTAL_TURNS)) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.contract);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                state = CONTRACTION;
                counter = counter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
                handler.postDelayed(runnableCode, CONTRACTION_SECONDS * 1000);

            } else if ((state == CONTRACTION) && (counter < TOTAL_TURNS)) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
        textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);

    }

    private Runnable runnableCodeContract = new Runnable() {
//        private MediaPlayer mpContract;

        @Override
        public void run() {
            // Change screen
        }};

    private Runnable runnableCodeRelax = new Runnable() {
//        private MediaPlayer mpRelax;

        @Override
        public void run() {
            // Change screen
        }};

    public void startCounter(View view) {
        handler.postDelayed(runnableCode, 1000);
        for (int i = 0; i > TOTAL_TURNS -1; i++) {

            /**
             *   textViewMain.setText(R.string.contraction_message);
             *   textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
             *   textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
             *   handler.postDelayed(runnableCodeRelax, CONTRACTION_SECONDS * 1000);
             *
             *   textViewMain.setText(R.string.relaxation_message);
             *   textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
             *   handler.postDelayed(runnableCodeContract, RELAXATION_SECONDS * 1000);
             */
        }
        /**
         *   textViewMain.setText(R.string.contraction_message);
         *   textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
         *   textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
         *   handler.postDelayed(runnableCodeRelax, CONTRACTION_SECONDS * 1000);

         *  mpFinal = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);
         *  mpFinal.start();
         *  textViewMain.setText(R.string.end_message);
         *  textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
         */
    }

    public void exitApp(View view) {
        if (Build.VERSION.SDK_INT >= 21)
            finishAndRemoveTask();
        else
            finish();
        System.exit(0);
    }

}
