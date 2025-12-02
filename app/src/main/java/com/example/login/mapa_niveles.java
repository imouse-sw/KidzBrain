package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_2C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_3C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_1C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_2C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_3C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_1C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_2C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_3C;
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

        // Recuperar la materia enviada desde la pantalla de inicio
        materia = getIntent().getStringExtra("materia");

        // Referencias a los botones
        btnNivel1 = findViewById(R.id.btnNivel1);
        btnNivel2 = findViewById(R.id.btnNivel2);
        btnNivel3 = findViewById(R.id.btnNivel3);

        // Inicializar SharedPreferences para guardar el progreso
        prefs = getSharedPreferences("Progreso_" + materia, MODE_PRIVATE);

        // Configurar el fondo según la materia
        actualizarFondo();

        // Actualizar botones según progreso guardado
        actualizarNiveles();

        // Asignar listeners a los botones de niveles
        btnNivel1.setOnClickListener(v -> abrirLeccion(1));
        btnNivel2.setOnClickListener(v -> abrirLeccion(2));
        btnNivel3.setOnClickListener(v -> abrirLeccion(3));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Siempre actualizar botones y fondo al volver al mapa
        actualizarNiveles();
        actualizarFondo();
    }

    /**
     * Actualiza la habilitación de los botones según el progreso guardado.
     */
    private void actualizarNiveles() {
        int nivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1);

        // Nivel 1 siempre habilitado
        btnNivel1.setEnabled(true);

        // Nivel 2 disponible si se desbloqueó
        btnNivel2.setEnabled(nivelDesbloqueado >= 2);

        // Nivel 3 disponible si se desbloqueó
        btnNivel3.setEnabled(nivelDesbloqueado >= 3);
    }

    /**
     * Actualiza el fondo según la materia
     */
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

    /**
     * Abre la lección correspondiente al nivel seleccionado.
     */
    private void abrirLeccion(int nivel) {
        Intent intent;

        if ("matematicas".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1:
                    intent = new Intent(this, ActividadLeccion1M.class);
                    break;
                case 2:
                    intent = new Intent(this, ActividadLeccion2M.class);
                    break;
                case 3:
                    intent = new Intent(this, ActividadLeccion3M.class);
                    break;
                default:
                    return;
            }
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1:
                    intent = new Intent(this, ActividadLeccion3_1C.class);
                    break;
                case 2:
                 intent = new Intent(this, ActividadLeccion3_2C.class);
                 break;
                case 3:
                 intent = new Intent(this, ActividadLeccion3_3C.class);
                 break;
                default:
                    return;
            }
        } else {
            // Materia no reconocida
            return;
        }

        // Pasar datos
        intent.putExtra("materia", materia);
        intent.putExtra("nivel", nivel);
        startActivity(intent);
    }
}
