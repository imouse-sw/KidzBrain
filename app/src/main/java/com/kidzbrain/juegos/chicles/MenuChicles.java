package com.kidzbrain.juegos.chicles; // Ajusta a tu paquete correcto

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R; // Ajusta si el R.layout viene de otra ruta

public class MenuChicles extends AppCompatActivity implements View.OnClickListener {

    private Button facil, normal;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_chicles);

        facil = findViewById(R.id.bPractica);
        normal = findViewById(R.id.bNormal);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        MediaPlayer mpVozInstrucciones = MediaPlayer.create(this, R.raw.aud_voz_chicles);
        mpVozInstrucciones.start();
        mpVozInstrucciones.setOnCompletionListener(MediaPlayer::release);

        ChiclesMusica.reproduce(this); // Descomenta cuando tengas tu clase de música
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            int id = v.getId();

            // Apuntamos a la actividad principal del juego de la máquina
            Intent intentito = new Intent(this, Chicles.class);

            if (id == R.id.bPractica) {
                // Modo Aprendiz (Preguntas lógicas)
                intentito.putExtra("esModoDificil", false);
            }
            else if (id == R.id.bNormal) {
                // Modo Experto (Fracciones)
                intentito.putExtra("esModoDificil", true);
            }

            startActivity(intentito);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            ChiclesMusica.libera();
        }
    }
}