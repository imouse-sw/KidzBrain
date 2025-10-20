package com.example.juego_cambio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuEleccion extends AppCompatActivity implements View.OnClickListener {
    Button facil, normal;
    ImageButton audio;
    AudioManager audioManager;
    private int ultimoVolumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_eleccion);

        facil = findViewById(R.id.bFacil);
        normal = findViewById(R.id.bNormal);
        audio = findViewById(R.id.bAudio);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);
        audio.setOnClickListener(this);

        //mediaPlayer = MediaPlayer.create(this, R.raw.instrucciones_cambio);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Musiquita.puchalePlay(this);

        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        ultimoVolumen = currentVolume;

        if (currentVolume == 0) {
            audio.setImageResource(R.drawable.px_no_audio);
        } else {
            audio.setImageResource(R.drawable.px_audio);
        }

        //mediaPlayer.start();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            String cadenita = ((Button)v).getText().toString();

            Intent intentito = new Intent(this, MainActivity.class);
            intentito.putExtra("Dificultad", cadenita);

            startActivity(intentito);
        }
        else if (v instanceof ImageButton) {
            int volumenActual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            if (volumenActual == 0) {
                int volumen = ultimoVolumen;

                if(volumen == 0) {
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    volumen = maxVolume / 8;
                }

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumen, 0);
                audio.setImageResource(R.drawable.px_audio);
            }
            else {
                ultimoVolumen = volumenActual;

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                audio.setImageResource(R.drawable.px_no_audio);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Musiquita.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Musiquita.puchalePlay(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            Musiquita.libera();
        }
    }
}