package com.kidzbrain.fraginteractivos;

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

import com.kidzbrain.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioContarCamiones extends PlantillaFragmentoInteractivo implements View.OnClickListener {
    private final int respuestaCorrecta = 4;
    private int seleccionada = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec1_int1_camiones, container, false);

        Button boton1 = vistita.findViewById(R.id.boton_opcion_camiones1);
        Button boton2 = vistita.findViewById(R.id.boton_opcion_camiones2);
        Button boton3 = vistita.findViewById(R.id.boton_opcion_camiones3);
        Button boton4 = vistita.findViewById(R.id.boton_opcion_camiones4);

        boton1.setOnClickListener(this);
        boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);
        boton4.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View view) {
        Button botonPresionado = (Button)view;
        seleccionada = Integer.parseInt(botonPresionado.getText().toString());
        comprobarRespuesta();
    }

    @Override
    protected void comprobarRespuesta() {
        ActividadLeccion1M actividad = null;
        if(getActivity() instanceof ActividadLeccion1M) {
            actividad = (ActividadLeccion1M) getActivity();
        }

        if(seleccionada == respuestaCorrecta) {
            if(actividad!=null) {
                reproducirSonido("sonido_correcto");
            }
            Toast.makeText(getContext(), "¡Muy bien!", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        }
        else if(seleccionada!=-1) {
            if (actividad != null) {
                reproducirSonido("sonido_incorrecto");
            }
            Toast.makeText(getContext(), "¡Ups! Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}
