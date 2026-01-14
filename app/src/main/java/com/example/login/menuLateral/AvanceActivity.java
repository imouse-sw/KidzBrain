package com.example.login.menuLateral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Importante para el botón
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class AvanceActivity extends AppCompatActivity {

    // 1. Declaramos el botón del menú
    private ImageView btnMenu;

    private TextView tvNivelActual, tvPuntosTotales;
    private ProgressBar pbNivelGeneral;
    private LinearProgressIndicator pbMatematicas, pbCiencias;

    private static final int MINUTOS_PARA_SUBIR_NIVEL = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avance);

        inicializarVistas();
        configurarBotones(); // Nuevo método para la lógica de botones
        simularTiempoDeJuego();
        cargarNivelYBarraDeProgreso();
        obtenerDatosDelServidor();
    }

    private void inicializarVistas() {
        // Enlazamos el botón del XML
        btnMenu = findViewById(R.id.btnMenu);

        tvNivelActual = findViewById(R.id.tv_nivel_actual);
        tvPuntosTotales = findViewById(R.id.tv_puntos_totales);
        pbNivelGeneral = findViewById(R.id.pb_nivel_general);
        pbMatematicas = findViewById(R.id.pb_matematicas);
        pbCiencias = findViewById(R.id.pb_ciencias);
    }

    // -------------------------------------------------------------------------
    // LÓGICA DEL BOTÓN DE MENÚ
    // -------------------------------------------------------------------------
    private void configurarBotones() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPCIÓN A: Regresar al Inicio (Lo recomendado)
                // Como esta pantalla no tiene el código de la Sidebar (DrawerLayout),
                // lo mejor es que este botón te regrese a la pantalla principal.
                finish();

                // Efecto visual de transición (opcional, para que se vea suave)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void simularTiempoDeJuego() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", Context.MODE_PRIVATE);
        int minutosJugados = prefs.getInt("minutos_totales", 0);
        minutosJugados += 15;
        prefs.edit().putInt("minutos_totales", minutosJugados).apply();
    }

    private void cargarNivelYBarraDeProgreso() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", Context.MODE_PRIVATE);
        int minutosTotales = prefs.getInt("minutos_totales", 0);

        int nivelActual = (minutosTotales / MINUTOS_PARA_SUBIR_NIVEL) + 1;
        int minutosEnNivelActual = minutosTotales % MINUTOS_PARA_SUBIR_NIVEL;
        int porcentajeBarra = (minutosEnNivelActual * 100) / MINUTOS_PARA_SUBIR_NIVEL;

        pbNivelGeneral.setProgress(porcentajeBarra);

        String tituloRango;
        if (nivelActual < 3) tituloRango = "Explorador Novato";
        else if (nivelActual < 5) tituloRango = "Estudiante Curioso";
        else if (nivelActual < 10) tituloRango = "¡Genio!";
        else tituloRango = "Maestro de KidzBrain";

        tvNivelActual.setText("Nivel " + nivelActual + ": " + tituloRango);
        tvPuntosTotales.setText(minutosTotales + " Minutos jugados en total");
    }

    private void obtenerDatosDelServidor() {
        // TODO: Aquí irá tu código de Retrofit/SpringBoot
    }
}