package com.kidzbrain.juegos.habitats;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

public class MenuAnimalesHabitat extends AppCompatActivity {

    private final String instrucciones = "Hola, soy BrainBot. En este juego primero debes elegir dónde vive el animal. Después debes seleccionar qué come. Si aciertas, ganarás puntos. Si fallas, perderás una vida. Tienes tres vidas. Presiona jugar para comenzar.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_animales_habitat);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ConstraintLayout btnInstrucciones = findViewById(R.id.btnInstrucciones);
        ConstraintLayout btnJugar = findViewById(R.id.btnJugar);

        btnBack.setOnClickListener(v -> finish());

        btnInstrucciones.setOnClickListener(v ->
                BrainBotDialogHelper.mostrarDialogo(this, instrucciones, R.raw.brainbot_intro_animales)
        );

        btnJugar.setOnClickListener(v -> {
            Intent intent = new Intent(this, JuegoAnimalesHabitatActivity.class);
            startActivity(intent);
        });

        BrainBotDialogHelper.mostrarDialogo(this, instrucciones, R.raw.brainbot_intro_animales);
    }

    @Override
    protected void onDestroy() {
        BrainBotDialogHelper.detenerAudio();
        super.onDestroy();
    }
}