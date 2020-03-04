package com.jamc68.suelopelvico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Fields for audio management
    private MediaPlayer mediaPlayer;
    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mediaPlayer.release();
        }
    };
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                mediaPlayer.release();
            }
        }
    };


    //fields for exercise time management
    private static int totalMinutes = 10;
    private static final int RELAXATION_SECONDS = 12;
    private static final int CONTRACTION_SECONDS = 6;
    private boolean exerciseRunning = false;

    // Fields for runnable code
    private static int turnCounter = 0;
    private static final boolean isRelaxed = true;
    private static final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);

    Handler handler = new Handler();

    // Start runnableCode
    boolean state = isRelaxed;
    Runnable runnableExerciseSession = new Runnable() {

        @Override
        public void run() {
            // Change screen
            TextView textViewMain = (TextView) findViewById(R.id.main_display);
            TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);

            // Starting Contraction
            if ((state == isRelaxed) && (turnCounter < TOTAL_TURNS)) {
                // Request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the MediaPlayer for the audio resource associated
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.contract);

                    // Start the audio file
                    mediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }

                state = !isRelaxed;
                turnCounter = turnCounter + 1;
                textViewMain.setText(R.string.contraction_message);
                textViewMain.setTextColor(getResources().getColor(R.color.secondaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.contractionColor));
                textViewSecondary.setText(
                        getString(R.string.secondary_message, turnCounter, TOTAL_TURNS));

                handler.postDelayed(runnableExerciseSession, CONTRACTION_SECONDS * 1000);

                // Starting Rest
            } else if ((state == !isRelaxed) && (turnCounter < TOTAL_TURNS)) {
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                state = isRelaxed;
                textViewMain.setText(R.string.relaxation_message);
                textViewMain.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textViewMain.setBackgroundColor(getResources().getColor(R.color.relaxColor));

                handler.postDelayed(runnableExerciseSession, RELAXATION_SECONDS * 1000);

                // Final step
            } else {
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.final_sound);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                textViewMain.setText(R.string.end_message);
                textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
            }
        }
    };
    // End of runnable


    public void startCounter(View view) {
        exerciseRunning = true;
        handler.postDelayed(runnableExerciseSession, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        //TODO: Save turnCounter and date. Restore:
        // If same day -> form saved pause;
        // else -> from shared prefs
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // To keep the screen from sleeping
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView textViewSecondary = (TextView) findViewById(R.id.secondary_display);
        textViewSecondary.setText(getString(R.string.secundary_intro_message, totalMinutes));

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        ////////// Find the View that shows the pause message
        TextView pauseApp = (TextView) findViewById(R.id.button_pause);
        // Set a click listener on that View
        pauseApp.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                if (exerciseRunning) {
                    handler.removeCallbacks(runnableExerciseSession);
                    TextView textViewMain = (TextView) findViewById(R.id.main_display);
                    textViewMain.setText(getText(R.string.pause_message).toString());
                    textViewMain.setTextColor(getResources().getColor(R.color.primaryTextColor));
                    textViewMain.setBackgroundColor(getResources().getColor(R.color.introColor));
                    exerciseRunning = false;
                    onPause();
                }
            }
        }); ///////////// END OF pauseApp.setOnClickListener


        ///////// Find the View that shows the exit message
        TextView exitApp = (TextView) findViewById(R.id.button_exit);
        // Set a click listener on that View
        exitApp.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnableExerciseSession);
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAndRemoveTask();
                } else {
                    finish();
                    System.exit(0);
                }
            }
        }); ///////////// END OF exitApp.setOnClickListener

    }

}

