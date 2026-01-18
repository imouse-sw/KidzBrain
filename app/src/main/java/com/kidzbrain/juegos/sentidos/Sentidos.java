package com.kidzbrain.juegos.sentidos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sentidos extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        finish();
    }

    private enum Sentido {
        VISTA, OIDO, GUSTO, OLFATO, TACTO
    }

    private static class Pregunta {
        final int imagenResId;
        final Sentido sentidoCorrecto;

        Pregunta(int imagenResId, Sentido sentidoCorrecto) {
            this.imagenResId = imagenResId;
            this.sentidoCorrecto = sentidoCorrecto;
        }
    }

    private TextView txtPuntos;
    private ImageView imagenPregunta;
    private ImageButton btnVista, btnOido, btnGusto, btnOlfato, btnTacto, btnsali;

    private List<Pregunta> listaPreguntas;
    private int preguntaActualIndex = 0;
    private int puntos = 0;
    private MediaPlayer playerInstrucciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentidos);

        txtPuntos = findViewById(R.id.txtPuntos);
        imagenPregunta = findViewById(R.id.imagenPregunta);
        btnVista = findViewById(R.id.btnVista);
        btnOido = findViewById(R.id.btnOido);
        btnGusto = findViewById(R.id.btnGusto);
        btnOlfato = findViewById(R.id.btnOlfato);
        btnTacto = findViewById(R.id.btnTacto);
        btnsali = findViewById(R.id.btnSalir4);


        btnVista.setOnClickListener(v -> verificarRespuesta(Sentido.VISTA));
        btnOido.setOnClickListener(v -> verificarRespuesta(Sentido.OIDO));
        btnGusto.setOnClickListener(v -> verificarRespuesta(Sentido.GUSTO));
        btnOlfato.setOnClickListener(v -> verificarRespuesta(Sentido.OLFATO));
        btnTacto.setOnClickListener(v -> verificarRespuesta(Sentido.TACTO));
        btnsali.setOnClickListener(this);

        mostrarDialogoInstrucciones();
    }

    private void mostrarDialogoInstrucciones() {

        playerInstrucciones = MediaPlayer.create(this, R.raw.audiosentidos);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("¡Instrucciones! 🧠")
                .setMessage("Mira la imagen que aparece en pantalla y selecciona el botón del sentido que crees que le corresponde. ¡Suerte!")
                .setPositiveButton("¡A Jugar!", null)
                .setNeutralButton("Escuchar 🎧", null)
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            positiveButton.setOnClickListener(v -> {
                stopInstructionAudio();
                dialog.dismiss();
                prepararCuestionario();
            });

            neutralButton.setOnClickListener(v -> {
                if (playerInstrucciones != null) {
                    if (playerInstrucciones.isPlaying()) {
                        playerInstrucciones.pause();
                        playerInstrucciones.seekTo(0);
                    } else {
                        playerInstrucciones.start();
                    }
                }
            });
        });

        dialog.show();
    }

    private void stopInstructionAudio() {
        if (playerInstrucciones != null) {
            if (playerInstrucciones.isPlaying()) {
                playerInstrucciones.stop();
            }
            playerInstrucciones.release();
            playerInstrucciones = null;
        }
    }


    private void playSound(int resId) {
        MediaPlayer player = MediaPlayer.create(this, resId);
        player.setOnCompletionListener(mp -> mp.release());
        player.start();
    }


    private void verificarRespuesta(Sentido sentidoSeleccionado) {
        Sentido respuestaCorrecta = listaPreguntas.get(preguntaActualIndex).sentidoCorrecto;

        if (sentidoSeleccionado == respuestaCorrecta) {
            puntos++;
            Toast.makeText(this, "¡Correcto! 👍", Toast.LENGTH_SHORT).show();
            playSound(R.raw.correcto);
        } else {
            Toast.makeText(this, "Incorrecto... 👎", Toast.LENGTH_SHORT).show();
            playSound(R.raw.incorrecto);
        }

        preguntaActualIndex++;
        mostrarSiguientePregunta();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInstructionAudio();
    }

    private void prepararCuestionario() {
        puntos = 0;
        preguntaActualIndex = 0;

        listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Pregunta(R.drawable.binoculares, Sentido.VISTA));
        listaPreguntas.add(new Pregunta(R.drawable.libro, Sentido.VISTA));
        listaPreguntas.add(new Pregunta(R.drawable.paisaje, Sentido.VISTA));
        listaPreguntas.add(new Pregunta(R.drawable.audifono, Sentido.OIDO));
        listaPreguntas.add(new Pregunta(R.drawable.bocina, Sentido.OIDO));
        listaPreguntas.add(new Pregunta(R.drawable.campana, Sentido.OIDO));
        listaPreguntas.add(new Pregunta(R.drawable.helado, Sentido.GUSTO));
        listaPreguntas.add(new Pregunta(R.drawable.paleta, Sentido.GUSTO));
        listaPreguntas.add(new Pregunta(R.drawable.pizza, Sentido.GUSTO));
        listaPreguntas.add(new Pregunta(R.drawable.flor, Sentido.OLFATO));
        listaPreguntas.add(new Pregunta(R.drawable.perfume, Sentido.OLFATO));
        listaPreguntas.add(new Pregunta(R.drawable.popo, Sentido.OLFATO));
        listaPreguntas.add(new Pregunta(R.drawable.abrazo, Sentido.TACTO));
        listaPreguntas.add(new Pregunta(R.drawable.arena, Sentido.TACTO));
        listaPreguntas.add(new Pregunta(R.drawable.pasto, Sentido.TACTO));

        Collections.shuffle(listaPreguntas);

        mostrarSiguientePregunta();
    }

    private void mostrarSiguientePregunta() {
        if (preguntaActualIndex < listaPreguntas.size()) {
            Pregunta preguntaActual = listaPreguntas.get(preguntaActualIndex);
            imagenPregunta.setImageResource(preguntaActual.imagenResId);
            txtPuntos.setText("Puntos: " + puntos);
        } else {
            mostrarResultadoFinal();
        }
    }

    private void mostrarResultadoFinal() {
        new AlertDialog.Builder(this)
                .setTitle("🎉 ¡Fin del juego! 🎉")
                .setMessage("Tu puntuación final es: " + puntos + " / " + listaPreguntas.size())
                .setPositiveButton("Jugar de nuevo", (dialog, which) -> {
                    prepararCuestionario();
                })
                .setNegativeButton("Salir", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
