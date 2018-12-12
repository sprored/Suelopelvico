package com.example.sprocket.suelopelvico;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int RELAXATION = 0;
    final int RELAXATION_SECONDS = 6;
    final int CONTRACTION = 1;
    final int CONTRACTION_SECONDS = 6;
    final int TOTAL_MINUTES = 10;
    final int TOTAL_TURNS = (TOTAL_MINUTES * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);
    SoundPool soundPool;

    Handler handler = new Handler();
    int state = 0;
    int counter = 0;
    private Runnable runnableCode = new Runnable() {

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            /* En el constructor de SoundPool hay que indicar tres parámetros.
            El primero corresponde al máximo de reproducciones simultáneas.
            El segundo es el tipo de stream de audio (normalmente STREAM_MUSIC).
            El tercero es la calidad de reproducción, aunque actualmente no se implementa.
             */
            int idContract, idRelax, idFinal;
            soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
            idContract = soundPool.load(getApplicationContext(),R.raw.contract,0);
            idRelax = soundPool.load(getApplicationContext(),R.raw.relax,0);
            idFinal = soundPool.load(getApplicationContext(),R.raw.final_sound,0);

            MediaPlayer mpContraction = MediaPlayer.create(getApplicationContext(), R.raw.contract);
            MediaPlayer mpRelaxation = MediaPlayer.create(getApplicationContext(), R.raw.relax);

            if ((state == RELAXATION) && (counter < TOTAL_TURNS)) {
                /*El método play() permite reproducir una pista. Hay que indicarle
                el identificador de pista;
                el volumen para el canal izquierdo y derecho (0.0 a 1.0);
                La prioridad;
                El número de repeticiones (-1= siempre, 0=solo una vez, 1=repetir una vez, …  )
                y el ratio de reproducción, con el que podremos modificar la velocidad o pitch
                (1.0 reproducción normal, rango: 0.5 a 2.0)

                 */
                soundPool.play(idContract,1,1,1,0,1);
                state = CONTRACTION;
                counter = counter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
                handler.postDelayed(runnableCode, CONTRACTION_SECONDS * 1100);

            } else if ((state == CONTRACTION) && (counter < TOTAL_TURNS)) {
                if (mpRelaxation.isPlaying()) {
                    mpRelaxation.release();
                    mpRelaxation = MediaPlayer.create(getApplicationContext(), R.raw.relax);
                }
                mpRelaxation.start();
                state = RELAXATION;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableCode, RELAXATION_SECONDS * 1000);

            } else {
                MediaPlayer mpFinal = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);
                mpFinal.start();
                textViewMain.setText(R.string.end_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
                mpContraction.release();
                mpRelaxation.release();
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
