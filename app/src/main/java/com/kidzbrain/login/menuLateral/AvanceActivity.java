package com.kidzbrain.login.menuLateral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kidzbrain.login.R;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvanceActivity extends AppCompatActivity {

    private ImageView btnMenu;

    // Nivel general
    private TextView tvNivelActual, tvPuntosTotales;

    // Racha
    private TextView tvRacha;

    // Porcentaje por materia
    private TextView tvPorcentajeMatematicas, tvPorcentajeCiencias;

    private ProgressBar pbNivelGeneral;
    private LinearProgressIndicator pbMatematicas, pbCiencias;

    private static final int MINUTOS_PARA_SUBIR_NIVEL = 30;

    private ApiService apiService;

    // Tiempo de sesión
    private long tiempoInicioSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avance);

        inicializarVistas();
        configurarBotones();

        tiempoInicioSesion = SystemClock.elapsedRealtime();

        actualizarRacha();
        cargarNivelYBarraDeProgreso();

        apiService = RetrofitClient.getApiService();
        obtenerDatosDelServidor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        guardarTiempoDeSesion();
    }

    private void inicializarVistas() {
        btnMenu = findViewById(R.id.btnMenu);

        tvNivelActual = findViewById(R.id.tv_nivel_actual);
        tvPuntosTotales = findViewById(R.id.tv_puntos_totales);

        tvRacha = findViewById(R.id.tv_racha);

        tvPorcentajeMatematicas = findViewById(R.id.tv_porcentaje_matematicas);
        tvPorcentajeCiencias = findViewById(R.id.tv_porcentaje_ciencias);

        pbNivelGeneral = findViewById(R.id.pb_nivel_general);
        pbMatematicas = findViewById(R.id.pb_matematicas);
        pbCiencias = findViewById(R.id.pb_ciencias);
    }

    private void configurarBotones() {
        btnMenu.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    // -------------------------
    // TIEMPO DE SESIÓN
    // -------------------------
    private void guardarTiempoDeSesion() {
        long tiempoFin = SystemClock.elapsedRealtime();
        long segundosSesion = (tiempoFin - tiempoInicioSesion) / 1000;

        int minutosSesion = (int) (segundosSesion / 60);
        if (minutosSesion <= 0) return;

        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);
        int minutosTotales = prefs.getInt("minutos_totales", 0);
        minutosTotales += minutosSesion;

        prefs.edit().putInt("minutos_totales", minutosTotales).apply();

        Log.d("TIEMPO", "Minutos sesión: " + minutosSesion);
    }

    private void cargarNivelYBarraDeProgreso() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);
        int minutosTotales = prefs.getInt("minutos_totales", 0);

        int nivelActual = (minutosTotales / MINUTOS_PARA_SUBIR_NIVEL) + 1;
        int minutosEnNivel = minutosTotales % MINUTOS_PARA_SUBIR_NIVEL;
        int porcentaje = (minutosEnNivel * 100) / MINUTOS_PARA_SUBIR_NIVEL;

        pbNivelGeneral.setProgress(porcentaje);

        String rango;
        if (nivelActual < 3) rango = "Explorador Novato";
        else if (nivelActual < 5) rango = "Estudiante Curioso";
        else if (nivelActual < 10) rango = "¡Genio!";
        else rango = "Maestro de KidzBrain";

        tvNivelActual.setText("Nivel " + nivelActual + ": " + rango);
        tvPuntosTotales.setText(minutosTotales + " Minutos jugados");
    }

    // -------------------------
    // RACHA DIARIA
    // -------------------------
    private void actualizarRacha() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);

        long hoy = System.currentTimeMillis();
        long ultimoDia = prefs.getLong("ultimo_dia", 0);
        int racha = prefs.getInt("racha", 0);

        long UN_DIA = 24 * 60 * 60 * 1000;

        if (ultimoDia == 0) {
            racha = 1;
        } else {
            long diff = hoy - ultimoDia;
            if (diff < UN_DIA) {
                // mismo día, no cambia
            } else if (diff < UN_DIA * 2) {
                racha++;
            } else {
                racha = 1;
            }
        }

        prefs.edit()
                .putLong("ultimo_dia", hoy)
                .putInt("racha", racha)
                .apply();

        tvRacha.setText(racha + " Días");
    }

    // -------------------------
    // PROGRESO POR MATERIA
    // -------------------------
    private void obtenerDatosDelServidor() {
        SharedPreferences userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int idUsuario = userPrefs.getInt("userId", 1);

        obtenerPuntajeMateria(idUsuario, 1, pbMatematicas, tvPorcentajeMatematicas);
        obtenerPuntajeMateria(idUsuario, 2, pbCiencias, tvPorcentajeCiencias);
    }

    private void obtenerPuntajeMateria(
            int idUsuario,
            int idMateria,
            LinearProgressIndicator progressBar,
            TextView tvPorcentaje
    ) {

        Log.d("PUNTOS", "API -> Usuario: " + idUsuario + " Materia: " + idMateria);

        apiService.getPuntuacionPorMateria(idUsuario, idMateria)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            int puntos = response.body();

                            int progreso = Math.min((puntos * 100) / 1000, 100);
                            progressBar.setProgress(progreso);
                            tvPorcentaje.setText(progreso + "%");

                            Log.d("PUNTOS", "Materia " + idMateria + ": " + progreso + "%");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("PUNTOS", "Error de conexión", t);
                    }
                });
    }
}
