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

import com.kidzbrain.lecciones.ciencias.primerosegundo.ActividadLeccion1C;
import com.kidzbrain.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

import java.util.ArrayList;
import java.util.List;

public class lec1ciencias_ejerpartes extends PlantillaFragmentoInteractivo implements View.OnClickListener {
    private class PreguntaCuerpo {
        int imagenResId;
        String[] opciones;
        int respuestaCorrecta;

        public PreguntaCuerpo(int imagenResId, String[] opciones, int respuestaCorrecta) {
            this.imagenResId = imagenResId;
            this.opciones = opciones;
            this.respuestaCorrecta = respuestaCorrecta;
        }
    }

    private List<PreguntaCuerpo> listaDePreguntas;
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
        View vistita = inflater.inflate(R.layout.fragment_lec1ciencias_ejerpartes, container, false);

        imagenPregunta = vistita.findViewById(R.id.imagen_ejercicio_cuerpo);
        textoPregunta = vistita.findViewById(R.id.texto_pregunta_cuerpo);
        btnSiguiente = vistita.findViewById(R.id.boton_siguiente_pregunta);

        btnOpcion1 = vistita.findViewById(R.id.boton_opcion_cuerpo1);
        btnOpcion2 = vistita.findViewById(R.id.boton_opcion_cuerpo2);
        btnOpcion3 = vistita.findViewById(R.id.boton_opcion_cuerpo3);

        btnOpcion1.setOnClickListener(this);
        btnOpcion2.setOnClickListener(this);
        btnOpcion3.setOnClickListener(this);

        btnSiguiente.setOnClickListener(v -> avanzarSiguientePregunta());

        inicializarPreguntas();
        cargarPregunta(preguntaActualIndex);

        return vistita;
    }

    /**
     * Este método se llama CADA vez que uno de los botones de opción
     * (a los que les pusimos setOnClickListener(this)) es presionado.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.boton_opcion_cuerpo1) {
            seleccionada = 0;
        } else if (id == R.id.boton_opcion_cuerpo2) {
            seleccionada = 1;
        } else if (id == R.id.boton_opcion_cuerpo3) {
            seleccionada = 2;
        }

        comprobarRespuesta();
    }


    @Override
    protected void comprobarRespuesta() {
        ActividadLeccion1C actividad = null;
        if(getActivity() instanceof ActividadLeccion1M) {
            actividad = (ActividadLeccion1C) getActivity();
        }

        habilitarOpciones(false);
        btnSiguiente.setVisibility(View.VISIBLE);

        if(seleccionada == respuestaCorrecta) {
            if(actividad != null) {
            }
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();

        } else if(seleccionada != -1) {
            if (actividad != null) {
            }
            Toast.makeText(getContext(), "¡Ups! Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {

    }


    private void inicializarPreguntas() {
        listaDePreguntas = new ArrayList<>();
        listaDePreguntas.add(new PreguntaCuerpo(
                R.drawable.bracillo,
                new String[]{"Brazo", "Boca", "Ojo", "Pie"},
                0
        ));
        listaDePreguntas.add(new PreguntaCuerpo(
                R.drawable.bokilla,
                new String[]{"Mano", "Ojo", "Boca", "Nariz"},
                2
        ));
        listaDePreguntas.add(new PreguntaCuerpo(
                R.drawable.ojillos,
                new String[]{"Pie", "Ojo", "Brazo", "Oreja"},
                1
        ));
    }

    private void cargarPregunta(int index) {
        if (index >= listaDePreguntas.size()) {
            terminarCuestionario();
            return;
        }

        PreguntaCuerpo pregunta = listaDePreguntas.get(index);

        this.respuestaCorrecta = pregunta.respuestaCorrecta;

        imagenPregunta.setImageResource(pregunta.imagenResId);
        textoPregunta.setText("¿Qué parte del cuerpo es esta?");
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

        textoPregunta.setText("¡Felicidades, has terminado!");
        imagenPregunta.setVisibility(View.GONE);
        btnOpcion1.setVisibility(View.GONE);
        btnOpcion2.setVisibility(View.GONE);
        btnOpcion3.setVisibility(View.GONE);

        btnSiguiente.setText("Volver a empezar");
        btnSiguiente.setOnClickListener(v -> {
            preguntaActualIndex = 0;
            cargarPregunta(preguntaActualIndex);

            imagenPregunta.setVisibility(View.VISIBLE);
            btnOpcion1.setVisibility(View.VISIBLE);
            btnOpcion2.setVisibility(View.VISIBLE);
            btnOpcion3.setVisibility(View.VISIBLE);

            btnSiguiente.setText("Siguiente Pregunta");
            btnSiguiente.setOnClickListener(v2 -> avanzarSiguientePregunta());
        });
    }
}