package com.kidzbrain.juegos.cambio;

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

import com.kidzbrain.login.R;

public class MenuCambio extends AppCompatActivity implements View.OnClickListener {
    Button facil, normal;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_cambio);

        facil = findViewById(R.id.bFacil);
        normal = findViewById(R.id.bNormal);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.instrucciones_cambio);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        CambioMusica.puchalePlay(this);

        mediaPlayer.start();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            String cadenita = ((Button)v).getText().toString();

            Intent intentito = new Intent(this, Cambio.class);
            intentito.putExtra("Dificultad", cadenita);

            startActivity(intentito);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CambioMusica.pausa();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CambioMusica.puchalePlay(this);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            CambioMusica.libera();
        }
    }
}