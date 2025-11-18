package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_2C;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion2M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion3M;

public class mapa_niveles extends AppCompatActivity {

    private Button btnNivel1, btnNivel2, btnNivel3;
    private SharedPreferences prefs;
    private String materia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_niveles);

        materia = getIntent().getStringExtra("materia");

        btnNivel1 = findViewById(R.id.btnNivel1);
        btnNivel2 = findViewById(R.id.btnNivel2);
        btnNivel3 = findViewById(R.id.btnNivel3);

        prefs = getSharedPreferences("Progreso_" + materia, MODE_PRIVATE);

        actualizarFondo();
        actualizarNiveles();

        // ✅ Hacer que el scroll inicie ARRIBA
        ScrollView scrollView = findViewById(R.id.scrollNiveles);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));

        btnNivel1.setOnClickListener(v -> abrirLeccion(1));
        btnNivel2.setOnClickListener(v -> abrirLeccion(2));
        btnNivel3.setOnClickListener(v -> abrirLeccion(3));
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarNiveles();
        actualizarFondo();
    }

    private void actualizarNiveles() {
        int nivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1);

        btnNivel1.setEnabled(true);
        btnNivel2.setEnabled(nivelDesbloqueado >= 2);
        btnNivel3.setEnabled(nivelDesbloqueado >= 3);
    }

    private void actualizarFondo() {
        RelativeLayout layout = findViewById(R.id.layoutMapa);

        if ("matematicas".equalsIgnoreCase(materia)) {
            layout.setBackgroundResource(R.drawable.mapa_matematicas);
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            layout.setBackgroundResource(R.drawable.mapa_ciencias);
        } else {
            layout.setBackgroundResource(R.drawable.mapa_fondo_default);
        }
    }

    private void abrirLeccion(int nivel) {
        Intent intent;

        if ("matematicas".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1:
                    intent = new Intent(this, ActividadLeccion1M.class); break;
                case 2:
                    intent = new Intent(this, ActividadLeccion2M.class); break;
                case 3:
                    intent = new Intent(this, ActividadLeccion3M.class); break;
                default:
                    return;
            }
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1:
                    intent = new Intent(this, ActividadLeccion1C.class); break;
                case 2:
                    intent = new Intent(this, ActividadLeccion1_2C.class); break;
                default:
                    return;
            }
        } else {
            return;
        }

        intent.putExtra("materia", materia);
        intent.putExtra("nivel", nivel);
        startActivity(intent);
    }
}
