package com.example.sprocket.suelopelvico;

import android.media.AudioManager;
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
    final int RELAXATION_SECONDS = 12;
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

            /* En el constructor DEPRECATED de SoundPool hay que indicar tres parámetros.
            El primero corresponde al máximo de reproducciones simultáneas.
            El segundo es el tipo de stream de audio (normalmente STREAM_MUSIC).
            El tercero es la calidad de reproducción, aunque actualmente no se implementa.
             */
            SoundPool soundPool;
            final int idContract, idRelax, idFinal;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                soundPool = new SoundPool.Builder()
                        .setMaxStreams(10)
                        .build();
            } else {
                soundPool = new SoundPool(5,AudioManager.STREAM_MUSIC,1);
            }


            idContract = soundPool.load(getApplicationContext(),R.raw.contract,1);
            idRelax = soundPool.load(getApplicationContext(),R.raw.relax,1);
            idFinal = soundPool.load(getApplicationContext(),R.raw.final_sound,1);

            if ((state == RELAXATION) && (counter < TOTAL_TURNS)) {
                /*El método play() permite reproducir una pista. Hay que indicarle
                el identificador de pista;
                el volumen para el canal izquierdo y derecho (0.0 a 1.0);
                La prioridad;
                El número de repeticiones (-1= siempre, 0=solo una vez, 1=repetir una vez, …  )
                y el ratio de reproducción, con el que podremos modificar la velocidad o pitch
                (1.0 reproducción normal, rango: 0.5 a 2.0)
                 */
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId,
                                               int status) {
                        soundPool.play(idContract, 20, 20, 1, 0, 1f);
                    }
                });

                state = CONTRACTION;
                counter = counter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText((String) getText(R.string.secondary_message) + " " + counter + " / " + TOTAL_TURNS);
                handler.postDelayed(runnableCode, CONTRACTION_SECONDS * 1100);

            } else if ((state == CONTRACTION) && (counter < TOTAL_TURNS)) {
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId,
                                               int status) {
                        soundPool.play(idRelax, 20, 20, 1, 0, 1f);
                    }
                });

                state = RELAXATION;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));
                handler.postDelayed(runnableCode, RELAXATION_SECONDS * 1000);

            } else {
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId,
                                               int status) {
                        soundPool.play(idFinal, 20, 20, 1, 0, 1f);
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
