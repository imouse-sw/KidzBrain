package com.kidzbrain.login.menuLateral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kidzbrain.utilidades.KidzBrainApp;
import com.kidzbrain.login.R;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvanceActivity extends AppCompatActivity {

    private ImageView btnMenu;

    private TextView tvNivelActual, tvPuntosTotales;
    private TextView tvRacha;
    private TextView tvPorcentajeMatematicas, tvPorcentajeCiencias;

    private ProgressBar pbNivelGeneral;
    private LinearProgressIndicator pbMatematicas, pbCiencias;
    private static final int MINUTOS_PARA_SUBIR_NIVEL = 30;
    private ApiService apiService;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnableTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avance);

        inicializarVistas();
        configurarBotones();

        mostrarRacha();
        cargarNivelYBarraDeProgreso();

        apiService = RetrofitClient.getApiService();
        obtenerDatosDelServidor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarNivelYBarraDeProgreso();
        mostrarRacha();
        iniciarContadorSesion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detenerContadorSesion();
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
    // TIEMPO EN VIVO SOLO VISUAL
    // -------------------------
    private void iniciarContadorSesion() {
        if (runnableTiempo != null) return;

        runnableTiempo = new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);
                int minutosGuardados = prefs.getInt("minutos_totales", 0);

                long tiempoInicioGlobal = KidzBrainApp.getTiempoInicioApp();
                int minutosGlobalesActuales = 0;
                int segundosGlobalesRestantes = 0;

                if (KidzBrainApp.estaAppActiva() && tiempoInicioGlobal > 0) {
                    long segundosGlobales = (SystemClock.elapsedRealtime() - tiempoInicioGlobal) / 1000;
                    minutosGlobalesActuales = (int) (segundosGlobales / 60);
                    segundosGlobalesRestantes = (int) (segundosGlobales % 60);
                }

                int minutosMostrados = minutosGuardados + minutosGlobalesActuales;

                tvPuntosTotales.setText(
                        minutosMostrados + " Minutos jugados\n" +
                                "Sesión actual: " + minutosGlobalesActuales + " min " + segundosGlobalesRestantes + " s"
                );

                actualizarNivelEnVivo(minutosMostrados);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableTiempo);
    }

    private void detenerContadorSesion() {
        if (runnableTiempo != null) {
            handler.removeCallbacks(runnableTiempo);
            runnableTiempo = null;
        }
    }

    private void cargarNivelYBarraDeProgreso() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);
        int minutosGuardados = prefs.getInt("minutos_totales", 0);

        long tiempoInicioGlobal = KidzBrainApp.getTiempoInicioApp();
        int minutosGlobalesActuales = 0;

        if (KidzBrainApp.estaAppActiva() && tiempoInicioGlobal > 0) {
            long segundosGlobales = (SystemClock.elapsedRealtime() - tiempoInicioGlobal) / 1000;
            minutosGlobalesActuales = (int) (segundosGlobales / 60);
        }

        int minutosTotales = minutosGuardados + minutosGlobalesActuales;
        actualizarNivelEnVivo(minutosTotales);
        tvPuntosTotales.setText(minutosTotales + " Minutos jugados");
    }

    private void actualizarNivelEnVivo(int minutosTotales) {
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
    }

    // -------------------------
    // RACHA SOLO VISUAL
    // -------------------------
    private void mostrarRacha() {
        SharedPreferences prefs = getSharedPreferences("KidzBrainStats", MODE_PRIVATE);
        int racha = prefs.getInt("racha", 1);
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