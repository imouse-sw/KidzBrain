package com.kidzbrain.juegos.tanque;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

public class MenuTanque extends AppCompatActivity implements View.OnClickListener {

    Button facil, normal;
    SoundPool soundPool;
    int saludoVoz;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_tanque);

        facil = findViewById(R.id.bPractica);
        normal = findViewById(R.id.bNormal);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        soundPool = builder.build();

        saludoVoz = soundPool.load(this, R.raw.aud_tanque_voz_inicio, 1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                if (sampleId == saludoVoz) {
                    soundPool.play(saludoVoz, 1, 1, 0, 0, 1);
                }
            }
        });

        TanqueMusica.reproduce(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bPractica || v.getId() == R.id.bNormal) {

            Intent intentito = new Intent(this, Tanque.class);

            if (v.getId() == R.id.bPractica) {
                intentito.putExtra("Dificultad", "Jugar en Modo Fácil");
            }
            else if (v.getId() == R.id.bNormal) {
                intentito.putExtra("Dificultad", "Jugar en Modo Normal");
            }

            startActivity(intentito);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            soundPool.release();
            TanqueMusica.libera();
        }
    }
}