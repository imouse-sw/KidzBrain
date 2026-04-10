package com.kidzbrain.juegos.comida;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kidzbrain.login.R;
import com.kidzbrain.juegos.habitats.BrainBotDialogHelper;

public class MenuPedidosSaludables extends AppCompatActivity {

    private final String instrucciones = "Hola, soy BrainBot. En este juego llegarán clientes con pedidos saludables. Lee con atención lo que quieren, elige los alimentos correctos y entrégalos. Si aciertas, ganarás puntos. Si te equivocas, perderás una vida.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_pedidos_saludables);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ConstraintLayout btnInstrucciones = findViewById(R.id.btnInstrucciones);
        ConstraintLayout btnJugar = findViewById(R.id.btnJugar);

        btnBack.setOnClickListener(v -> finish());

        btnInstrucciones.setOnClickListener(v ->
                BrainBotDialogHelper.mostrarDialogo(this, instrucciones, R.raw.brainbot_intro_pedidos)
        );

        btnJugar.setOnClickListener(v ->
                startActivity(new Intent(this, JuegoPedidosSaludablesActivity.class))
        );
    }

    @Override
    protected void onDestroy() {
        BrainBotDialogHelper.detenerAudio();
        super.onDestroy();
    }
}
