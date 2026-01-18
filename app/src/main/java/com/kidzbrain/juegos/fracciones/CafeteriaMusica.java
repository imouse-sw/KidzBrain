package com.kidzbrain.juegos.fracciones;

import android.content.Context;
import android.media.MediaPlayer;

import com.kidzbrain.login.R;

public class CafeteriaMusica {
    private static MediaPlayer cancionAReproducir;

    public static void reproduce(Context context) {
        if(cancionAReproducir == null) {
            cancionAReproducir = MediaPlayer.create(context.getApplicationContext(), R.raw.bg_cafe_musica); // Cambia "bg_musica2" por el nombre de tu archivo
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
    /*

    En la clase de tu actividad principal de tu aplicación,
    agrega la siguiente línea:

    Musica.reproduce();

    Y agrega los siguientes métodos al final:

    @Override
    protected void onPause() {
        super.onPause();
        Musica.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Musica.reproduce(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            Musica.libera();
        }
    }

    Adicionalmente, Musica.pausa() y Musica.reproduce() los puedes añadir en cualquier punto de tu código
    para pausar o reproducir a tu gusto.

     */
}
