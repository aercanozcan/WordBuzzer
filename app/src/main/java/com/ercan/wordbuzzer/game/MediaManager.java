package com.ercan.wordbuzzer.game;

import android.content.Context;
import android.media.MediaPlayer;

import com.ercan.wordbuzzer.R;

/**
 * Created by ercanozcan on 21/02/16.
 */
public class MediaManager {

    private static MediaManager instance;

    MediaPlayer mainTheme;
    MediaPlayer effects;
    Context context;

    private MediaManager(Context context) {
        this.context = context;
    }

    public static MediaManager getInstance(Context context) {
        if (instance == null)
            instance = new MediaManager(context);
        return instance;
    }

    /**
     * plays a main theme music
     */
    public void playMainTheme() {

        if (mainTheme == null) {
            mainTheme = MediaPlayer.create(context.getApplicationContext(), R.raw.main_theme_song);
        }

        try {
            if (mainTheme.isPlaying()) {//it is already playing
                return;
            }
        } catch (IllegalStateException e) {//in this case this is not playing and not initialized
            mainTheme = MediaPlayer.create(context.getApplicationContext(), R.raw.main_theme_song);//we initialized it here
        }
        mainTheme.setLooping(true);
        mainTheme.start();

    }

    public void stopMainTheme() {
        if (!mainTheme.isPlaying()) {//it is not playing
            return;
        }
        mainTheme.stop();
        mainTheme.release();
    }

    /**
     * wrong answer feedback sound
     */
    public void playBuzzer() {
        effects = MediaPlayer.create(context, R.raw.buzzer);
        effects.start();
    }

    /**
     * correct answer feedback sound
     */
    public void playCorrect() {
        effects = MediaPlayer.create(context, R.raw.correct);
        effects.start();
    }

    /**
     * game end feedback sound
     */
    public void playWin() {
        effects = MediaPlayer.create(context, R.raw.win_sound);
        effects.start();
    }


}
