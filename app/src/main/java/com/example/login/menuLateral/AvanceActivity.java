package com.example.login.menuLateral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView; // Importante para el botón
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spring.ApiService;
import com.example.login.R;
import com.example.spring.RetrofitClient;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvanceActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private TextView tvNivelActual, tvPuntosTotales;
    private ProgressBar pbNivelGeneral;
    private LinearProgressIndicator pbMatematicas, pbCiencias;

    private static final int MINUTOS_PARA_SUBIR_NIVEL = 30;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avance);

        inicializarVistas();
        configurarBotones();
        simularTiempoDeJuego();
        cargarNivelYBarraDeProgreso();

        // Inicializar Retrofit
        apiService = RetrofitClient.getApiService();
        obtenerDatosDelServidor();
    }

    private void inicializarVistas() {
        btnMenu = findViewById(R.id.btnMenu);
        tvNivelActual = findViewById(R.id.tv_nivel_actual);
        tvPuntosTotales = findViewById(R.id.tv_puntos_totales);
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
        // Asumimos que el ID de usuario está guardado en SharedPreferences
        SharedPreferences userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int idUsuario = userPrefs.getInt("userId", 1); // Usamos 1 como default

        // Obtener puntaje de Matemáticas (ID 1)
        obtenerPuntajeMateria(idUsuario, 1, pbMatematicas);

        // Obtener puntaje de Ciencias (ID 2)
        obtenerPuntajeMateria(idUsuario, 2, pbCiencias);
    }

    private void obtenerPuntajeMateria(int idUsuario, int idMateria, LinearProgressIndicator progressBar) {
        apiService.getPuntuacionPorMateria(idUsuario, idMateria).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int puntuacion = response.body();
                    // Asumimos un máximo de 1000 puntos por materia para la barra de progreso
                    progressBar.setProgress((puntuacion * 100) / 1000);
                } else {
                    Log.e("API_ERROR", "Error al obtener puntaje para materia " + idMateria + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(AvanceActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Fallo en la llamada a la API", t);
            }
        });
    }
}
