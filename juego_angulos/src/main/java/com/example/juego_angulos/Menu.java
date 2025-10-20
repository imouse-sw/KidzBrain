package com.example.juego_angulos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    Button facil, normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_angulos);

        facil = findViewById(R.id.bPractica);
        normal = findViewById(R.id.bNormal);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        Musica.reproduce(this);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            int id = v.getId();

            Intent intentito = new Intent(this, MainActivity.class);

            if (id == R.id.bPractica) {
                intentito.putExtra("Dificultad", "Práctica");
            }
            else if (id == R.id.bNormal) {
                intentito.putExtra("Dificultad", "Normal");
            }

            startActivity(intentito);
        }
    }

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
}