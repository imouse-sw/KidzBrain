package com.kidzbrain.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

import java.util.ArrayList;
import java.util.List;

public class EjercicioMateria extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private class PreguntaEstado {
        int imagenResId;
        String[] opciones;
        int respuestaCorrecta;

        public PreguntaEstado(int imagenResId, String[] opciones, int respuestaCorrecta) {
            this.imagenResId = imagenResId;
            this.opciones = opciones;
            this.respuestaCorrecta = respuestaCorrecta;
        }
    }

    private List<PreguntaEstado> listaDePreguntas;
    private int preguntaActualIndex = 0;

    private int respuestaCorrecta;
    private int seleccionada = -1;

    private ImageView imagenPregunta;
    private TextView textoPregunta;
    private Button btnOpcion1, btnOpcion2, btnOpcion3;
    private Button btnSiguiente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lec2ciencias_ejermateria, container, false);

        imagenPregunta = vista.findViewById(R.id.imagen_ejercicio_materia);
        textoPregunta = vista.findViewById(R.id.texto_pregunta_materia);
        btnSiguiente = vista.findViewById(R.id.boton_siguiente_materia);

        btnOpcion1 = vista.findViewById(R.id.boton_opcion_materia1);
        btnOpcion2 = vista.findViewById(R.id.boton_opcion_materia2);
        btnOpcion3 = vista.findViewById(R.id.boton_opcion_materia3);

        btnOpcion1.setOnClickListener(this);
        btnOpcion2.setOnClickListener(this);
        btnOpcion3.setOnClickListener(this);

        btnSiguiente.setOnClickListener(v -> avanzarSiguientePregunta());

        inicializarPreguntas();
        cargarPregunta(preguntaActualIndex);

        return vista;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.boton_opcion_materia1) {
            seleccionada = 0;
        } else if (id == R.id.boton_opcion_materia2) {
            seleccionada = 1;
        } else if (id == R.id.boton_opcion_materia3) {
            seleccionada = 2;
        }

        comprobarRespuesta();
    }

    @Override
    protected void comprobarRespuesta() {
        habilitarOpciones(false);
        btnSiguiente.setVisibility(View.VISIBLE);

        if (seleccionada == respuestaCorrecta) {
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else if (seleccionada != -1) {
            Toast.makeText(getContext(), "No es correcto, observa bien la imagen.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
    }

    private void inicializarPreguntas() {
        listaDePreguntas = new ArrayList<>();

        listaDePreguntas.add(new PreguntaEstado(
                R.drawable.cubo,          // sólido
                new String[]{"Sólido", "Líquido", "Gas"},
                0
        ));

        listaDePreguntas.add(new PreguntaEstado(
                R.drawable.sopa,      // líquido
                new String[]{"Sólido", "Líquido", "Gas"},
                1
        ));

        listaDePreguntas.add(new PreguntaEstado(
                R.drawable.dioxido,          // gas
                new String[]{"Líquido", "Gas", "Sólido"},
                1
        ));
    }

    private void cargarPregunta(int index) {
        if (index >= listaDePreguntas.size()) {
            terminarCuestionario();
            return;
        }

        PreguntaEstado pregunta = listaDePreguntas.get(index);

        this.respuestaCorrecta = pregunta.respuestaCorrecta;

        imagenPregunta.setImageResource(pregunta.imagenResId);
        textoPregunta.setText("¿Qué estado de la materia ves aquí?");
        btnOpcion1.setText(pregunta.opciones[0]);
        btnOpcion2.setText(pregunta.opciones[1]);
        btnOpcion3.setText(pregunta.opciones[2]);

        btnSiguiente.setVisibility(View.GONE);
        habilitarOpciones(true);
        seleccionada = -1;
    }

    private void avanzarSiguientePregunta() {
        preguntaActualIndex++;
        cargarPregunta(preguntaActualIndex);
    }

    private void habilitarOpciones(boolean habilitar) {
        btnOpcion1.setEnabled(habilitar);
        btnOpcion2.setEnabled(habilitar);
        btnOpcion3.setEnabled(habilitar);
    }

    private void terminarCuestionario() {
        notificarPasoCompletado();

        textoPregunta.setText("¡Muy bien! Terminaste el ejercicio.");
        imagenPregunta.setVisibility(View.GONE);
        btnOpcion1.setVisibility(View.GONE);
        btnOpcion2.setVisibility(View.GONE);
        btnOpcion3.setVisibility(View.GONE);

        btnSiguiente.setText("Volver a empezar");
        btnSiguiente.setVisibility(View.VISIBLE);

        btnSiguiente.setOnClickListener(v -> {
            preguntaActualIndex = 0;

            imagenPregunta.setVisibility(View.VISIBLE);
            btnOpcion1.setVisibility(View.VISIBLE);
            btnOpcion2.setVisibility(View.VISIBLE);
            btnOpcion3.setVisibility(View.VISIBLE);

            btnSiguiente.setText("Siguiente");
            btnSiguiente.setOnClickListener(v2 -> avanzarSiguientePregunta());

            cargarPregunta(preguntaActualIndex);
        });
    }
}
