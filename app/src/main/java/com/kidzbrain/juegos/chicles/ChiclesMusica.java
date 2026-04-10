package com.kidzbrain.juegos.chicles;

import android.content.Context;
import android.media.MediaPlayer;

import com.kidzbrain.login.R;

public class ChiclesMusica {
    private static MediaPlayer cancionAReproducir;

    public static void reproduce(Context context) {
        if(cancionAReproducir == null) {
            cancionAReproducir = MediaPlayer.create(context.getApplicationContext(), R.raw.bg_candy_shop); // Cambia "bg_musica2" por el nombre de tu archivo
            cancionAReproducir.setVolume(1, 1);
            cancionAReproducir.setLooping(true);
        }
        if(!cancionAReproducir.isPlaying()) {
            cancionAReproducir.start();
        }
    }

    public static void pausa() {
        if(cancionAReproducir != null && cancionAReproducir.isPlaying()) {
            cancionAReproducir.pause();
        }
    }

    public static void libera() {
        if(cancionAReproducir != null) {
            cancionAReproducir.release();
            cancionAReproducir = null;
        }
    }
}
