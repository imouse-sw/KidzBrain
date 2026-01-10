package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

// Importaciones de tus lecciones...
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_2C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_3C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_1C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_2C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_3C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_1C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_2C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_3C;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion7M;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion8M;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion9M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion4M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion5M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion6M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion2M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion3M;

public class mapa_niveles extends AppCompatActivity {

    private Button btnNivel1, btnNivel2, btnNivel3, btnNivel4, btnNivel5, btnNivel6, btnNivel7, btnNivel8, btnNivel9;
    private ImageView btnBack;
    private TextView tvTituloMateria;
    private ConstraintLayout rootLayout;

    private SharedPreferences prefs;
    private String materia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_niveles);

        // 1. Recibir datos
        materia = getIntent().getStringExtra("materia");

        // 2. Vincular Vistas
        rootLayout = findViewById(R.id.rootLayout);
        tvTituloMateria = findViewById(R.id.tvTituloMateria);
        btnBack = findViewById(R.id.btnBack);

        btnNivel1 = findViewById(R.id.btnNivel1);
        btnNivel2 = findViewById(R.id.btnNivel2);
        btnNivel3 = findViewById(R.id.btnNivel3);
        btnNivel4 = findViewById(R.id.btnNivel4);
        btnNivel5 = findViewById(R.id.btnNivel5);
        btnNivel6 = findViewById(R.id.btnNivel6);
        btnNivel7 = findViewById(R.id.btnNivel7);
        btnNivel8 = findViewById(R.id.btnNivel8);
        btnNivel9 = findViewById(R.id.btnNivel9);

        // 3. Configurar Diseño (Colores y Título)
        personalizarInterfaz();

        // 4. Lógica de Niveles
        prefs = getSharedPreferences("Progreso_" + materia, MODE_PRIVATE);
        actualizarNiveles();

        // Scroll al inicio (Arriba)
        ScrollView scrollView = findViewById(R.id.scrollNiveles);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));

        // 5. Listeners
        btnBack.setOnClickListener(v -> finish()); // Regresar al Inicio

        btnNivel1.setOnClickListener(v -> abrirLeccion(1));
        btnNivel2.setOnClickListener(v -> abrirLeccion(2));
        btnNivel3.setOnClickListener(v -> abrirLeccion(3));
        btnNivel4.setOnClickListener(v -> abrirLeccion(4));
        btnNivel5.setOnClickListener(v -> abrirLeccion(5));
        btnNivel6.setOnClickListener(v -> abrirLeccion(6));
        btnNivel7.setOnClickListener(v -> abrirLeccion(7));
        btnNivel8.setOnClickListener(v -> abrirLeccion(8));
        btnNivel9.setOnClickListener(v -> abrirLeccion(9));
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarNiveles();
    }

    private void personalizarInterfaz() {
        if ("matematicas".equalsIgnoreCase(materia)) {
            tvTituloMateria.setText("Matemáticas");
            rootLayout.setBackgroundColor(Color.parseColor("#E1F5FE")); // Azul cielo
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            tvTituloMateria.setText("Ciencias");
            rootLayout.setBackgroundColor(Color.parseColor("#E0F2F1")); // Verde menta
        } else {
            tvTituloMateria.setText("Aventura");
        }
    }

    private void actualizarNiveles() {
        int nivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1);

        // Helper para configurar cada botón visual y lógicamente
        configurarBoton(btnNivel1, 1, nivelDesbloqueado);
        configurarBoton(btnNivel2, 2, nivelDesbloqueado);
        configurarBoton(btnNivel3, 3, nivelDesbloqueado);
        configurarBoton(btnNivel4, 4, nivelDesbloqueado);
        configurarBoton(btnNivel5, 5, nivelDesbloqueado);
        configurarBoton(btnNivel6, 6, nivelDesbloqueado);
        configurarBoton(btnNivel7, 7, nivelDesbloqueado);
        configurarBoton(btnNivel8, 8, nivelDesbloqueado);
        configurarBoton(btnNivel9, 9, nivelDesbloqueado);
    }

    private void configurarBoton(Button btn, int nivelBoton, int nivelUsuario) {
        if (nivelUsuario >= nivelBoton) {
            // DESBLOQUEADO
            btn.setEnabled(true);
            btn.setBackgroundResource(R.drawable.bg_nivel_activo); // Amarillo brillante
            btn.setTextColor(Color.WHITE);
            btn.setAlpha(1.0f);
        } else {
            // BLOQUEADO
            btn.setEnabled(false);
            btn.setBackgroundResource(R.drawable.bg_nivel_bloqueado); // Gris
            btn.setTextColor(Color.parseColor("#90A4AE"));
            btn.setAlpha(0.7f);
        }
    }

    private void abrirLeccion(int nivel) {
        Intent intent = null;

        if ("matematicas".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1: intent = new Intent(this, ActividadLeccion1M.class); break;
                case 2: intent = new Intent(this, ActividadLeccion2M.class); break;
                case 3: intent = new Intent(this, ActividadLeccion3M.class); break;
                case 4: intent = new Intent(this, ActividadLeccion4M.class); break;
                case 5: intent = new Intent(this, ActividadLeccion5M.class); break;
                case 6: intent = new Intent(this, ActividadLeccion6M.class); break;
                case 7: intent = new Intent(this, ActividadLeccion7M.class); break;
                case 8: intent = new Intent(this, ActividadLeccion8M.class); break;
                case 9: intent = new Intent(this, ActividadLeccion9M.class); break;
            }
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            switch (nivel) {
                case 1: intent = new Intent(this, ActividadLeccion1C.class); break;
                case 2: intent = new Intent(this, ActividadLeccion1_2C.class); break;
                case 3: intent = new Intent(this, ActividadLeccion1_3C.class); break;
                case 4: intent = new Intent(this, ActividadLeccion2_1C.class); break;
                case 5: intent = new Intent(this, ActividadLeccion2_2C.class); break;
                case 6: intent = new Intent(this, ActividadLeccion2_3C.class); break;
                case 7: intent = new Intent(this, ActividadLeccion3_1C.class); break;
                case 8: intent = new Intent(this, ActividadLeccion3_2C.class); break;
                case 9: intent = new Intent(this, ActividadLeccion3_3C.class); break;
            }
        }

        if (intent != null) {
            intent.putExtra("materia", materia);
            intent.putExtra("nivel", nivel);
            startActivity(intent);
        }
    }
}
