package com.kidzbrain.juegos.pizzangulos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

public class MenuPizza extends AppCompatActivity implements View.OnClickListener {
    Button facil, normal;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_pizza);

        facil = findViewById(R.id.bPractica);
        normal = findViewById(R.id.bNormal);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        PizzaMusica.reproduce(this);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            int id = v.getId();

            Intent intentito = new Intent(this, Pizza.class);

            if (id == R.id.bPractica) {
                intentito.putExtra("Dificultad", "Jugar en Práctica");
            }
            else if (id == R.id.bNormal) {
                intentito.putExtra("Dificultad", "Jugar en Normal");
            }

            startActivity(intentito);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PizzaMusica.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PizzaMusica.reproduce(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            PizzaMusica.libera();
        }
    }
}