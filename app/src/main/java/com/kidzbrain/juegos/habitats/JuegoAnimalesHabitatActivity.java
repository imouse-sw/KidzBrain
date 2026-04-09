package com.kidzbrain.juegos.habitats;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoAnimalesHabitatActivity extends AppCompatActivity {

    private TextView tvFase, tvPregunta, tvAnimal, tvPuntaje;
    private ImageView ivAnimal, ivVida1, ivVida2, ivVida3;
    private androidx.constraintlayout.widget.ConstraintLayout opcion1, opcion2, opcion3;
    private TextView tvOpcion1, tvOpcion2, tvOpcion3;
    private ImageButton btnBrainBot, btnBack;

    private RepositorioAnimales repositorioAnimales;
    private Animal animalActual;

    private int faseActual = 1;
    private int puntaje = 0;
    private int vidas = 3;

    private final Handler handler = new Handler();

    private final String ayudaJuego = "Recuerda. Primero debes elegir el hábitat correcto del animal. Después debes seleccionar qué come.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_animales_habitat);

        iniciarVistas();
        repositorioAnimales = new RepositorioAnimales();

        btnBack.setOnClickListener(v -> finish());

        btnBrainBot.setOnClickListener(v ->
                BrainBotDialogHelper.mostrarDialogo(this, ayudaJuego, R.raw.brainbot_ayuda_juego)
        );

        opcion1.setOnClickListener(v -> validarRespuesta(tvOpcion1.getText().toString()));
        opcion2.setOnClickListener(v -> validarRespuesta(tvOpcion2.getText().toString()));
        opcion3.setOnClickListener(v -> validarRespuesta(tvOpcion3.getText().toString()));

        actualizarMarcadores();
        nuevaRonda();
    }

    private void iniciarVistas() {
        tvFase = findViewById(R.id.tvFase);
        tvPregunta = findViewById(R.id.tvPregunta);
        tvAnimal = findViewById(R.id.tvAnimal);
        tvPuntaje = findViewById(R.id.tvPuntaje);

        ivAnimal = findViewById(R.id.ivAnimal);
        ivVida1 = findViewById(R.id.ivVida1);
        ivVida2 = findViewById(R.id.ivVida2);
        ivVida3 = findViewById(R.id.ivVida3);

        opcion1 = findViewById(R.id.opcion1);
        opcion2 = findViewById(R.id.opcion2);
        opcion3 = findViewById(R.id.opcion3);

        tvOpcion1 = findViewById(R.id.tvOpcion1);
        tvOpcion2 = findViewById(R.id.tvOpcion2);
        tvOpcion3 = findViewById(R.id.tvOpcion3);

        btnBrainBot = findViewById(R.id.btnBrainBot);
        btnBack = findViewById(R.id.btnBack);
    }

    private void nuevaRonda() {
        if (vidas <= 0) {
            mostrarResultadoFinal();
            return;
        }

        animalActual = repositorioAnimales.obtenerSiguienteAnimal();
        faseActual = 1;

        tvFase.setText("Fase 1 de 2");
        tvPregunta.setText("¿Dónde vive este animal?");
        tvAnimal.setText(animalActual.getNombre());
        ivAnimal.setImageResource(animalActual.getImagenResId());

        cargarOpciones(animalActual.getOpcionesHabitat());
    }

    private void pasarAFaseComida() {
        faseActual = 2;
        tvFase.setText("Fase 2 de 2");
        tvPregunta.setText("¿Qué come este animal?");
        cargarOpciones(animalActual.getOpcionesComida());
    }

    private void cargarOpciones(String[] opciones) {
        List<String> lista = new ArrayList<>();
        Collections.addAll(lista, opciones);
        Collections.shuffle(lista);

        tvOpcion1.setText(lista.get(0));
        tvOpcion2.setText(lista.get(1));
        tvOpcion3.setText(lista.get(2));
    }

    private void validarRespuesta(String respuesta) {
        if (faseActual == 1) {
            if (respuesta.equalsIgnoreCase(animalActual.getHabitatCorrecto())) {
                puntaje += 10;
                actualizarMarcadores();
                Toast.makeText(this, "Correcto. Ahora elige qué come.", Toast.LENGTH_SHORT).show();
                pasarAFaseComida();
            } else {
                vidas--;
                actualizarMarcadores();
                Toast.makeText(this, "Ese no es su hábitat.", Toast.LENGTH_SHORT).show();

                if (vidas <= 0) {
                    mostrarResultadoFinal();
                } else {
                    handler.postDelayed(this::nuevaRonda, 700);
                }
            }
        } else {
            if (respuesta.equalsIgnoreCase(animalActual.getComidaCorrecta())) {
                puntaje += 15;
                Toast.makeText(this, "Muy bien. Completaste la ronda.", Toast.LENGTH_SHORT).show();
            } else {
                vidas--;
                Toast.makeText(this, "Esa no es su comida.", Toast.LENGTH_SHORT).show();
            }

            actualizarMarcadores();

            if (vidas <= 0) {
                mostrarResultadoFinal();
            } else {
                handler.postDelayed(this::nuevaRonda, 800);
            }
        }
    }

    private void actualizarMarcadores() {
        tvPuntaje.setText(String.valueOf(puntaje));

        ivVida1.setImageResource(vidas >= 1 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
        ivVida2.setImageResource(vidas >= 2 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
        ivVida3.setImageResource(vidas >= 3 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
    }

    private void mostrarResultadoFinal() {
        new AlertDialog.Builder(this)
                .setTitle("Juego terminado")
                .setMessage("Tu puntaje final fue: " + puntaje)
                .setCancelable(false)
                .setPositiveButton("Jugar otra vez", (dialog, which) -> reiniciarJuego())
                .setNegativeButton("Salir", (dialog, which) -> finish())
                .show();
    }

    private void reiniciarJuego() {
        puntaje = 0;
        vidas = 3;
        repositorioAnimales = new RepositorioAnimales();
        actualizarMarcadores();
        nuevaRonda();
    }

    @Override
    protected void onDestroy() {
        BrainBotDialogHelper.detenerAudio();
        super.onDestroy();
    }
}