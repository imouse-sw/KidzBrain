package com.example.fraginteractivos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

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
        ActividadLeccion1 actividad = null;
        if(getActivity() instanceof ActividadLeccion1) {
            actividad = (ActividadLeccion1) getActivity();
        }

        if(seleccionada == respuestaCorrecta) {
            if(actividad!=null) {
                actividad.reproducirSonido(R.raw.aud_correcto);
            }
            notificarPasoCompletado();
        }
        else if(seleccionada!=-1) {
            if (actividad != null) {
                actividad.reproducirSonido(R.raw.aud_incorrecto);
            }
            Toast.makeText(getContext(), "¡Ups! Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }
}
