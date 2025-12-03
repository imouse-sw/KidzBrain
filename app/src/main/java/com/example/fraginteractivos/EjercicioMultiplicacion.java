package com.example.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lecciones.matematicas.matematicas.primerosegundo.ActividadLeccion1M;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioMultiplicacion extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int respuestaCorrecta = 20;
    private int seleccionada = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec4_int1_op1, container, false);

        Button boton1 = vistita.findViewById(R.id.btnOpcion1);
        Button boton2 = vistita.findViewById(R.id.btnCorrecto);
        Button boton3 = vistita.findViewById(R.id.btnOpcion2);

        boton1.setOnClickListener(this);
        boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View view) {
        Button botonPresionado = (Button) view;
        try {
            seleccionada = Integer.parseInt(botonPresionado.getText().toString());
            comprobarRespuesta();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        ActividadLeccion1M actividad = null;
        if (getActivity() instanceof ActividadLeccion1M) {
            actividad = (ActividadLeccion1M) getActivity();
        }

        if (seleccionada == respuestaCorrecta) {
            if (actividad != null) {
                reproducirSonido("sonido_correcto");
            }
            Toast.makeText(getContext(), "¡Excelente! 5 filas x 4 galletas = 20", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        }
        else if (seleccionada != -1) {

            if (actividad != null) {
                reproducirSonido("sonido_incorrecto");
            }
            Toast.makeText(getContext(), "¡Casi! Cuenta las filas de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}