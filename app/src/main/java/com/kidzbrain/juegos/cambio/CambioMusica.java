package com.kidzbrain.juegos.cambio;

import android.content.Context;
import android.media.MediaPlayer;

import com.kidzbrain.login.R;

public class CambioMusica {
    private static MediaPlayer cancioncita;

    public static void puchalePlay(Context context) {
        if(cancioncita == null) {
            cancioncita = MediaPlayer.create(context.getApplicationContext(), R.raw.cp);
            cancioncita.setLooping(true);
        }
        if(!cancioncita.isPlaying()) {
            cancioncita.start();
        }
    }

    public static void pausa() {
        if(cancioncita != null && cancioncita.isPlaying()) {
            cancioncita.pause();
        }
    }

    public static void libera() {
        if(cancioncita != null) {
            cancioncita.release();
            cancioncita = null;
        }
    }
}
