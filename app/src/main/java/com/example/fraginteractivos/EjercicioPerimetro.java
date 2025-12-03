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

import com.example.lecciones.matematicas.matematicas.tercerocuarto.ActividadLeccion4M;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioPerimetro extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int respuestaCorrecta = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec5_int_op1, container, false);

        Button btnCorrecto = vistita.findViewById(R.id.btn_opcion_correcta);
        Button btnMal1 = vistita.findViewById(R.id.btn_opcion_incorrecta1);
        Button btnMal2 = vistita.findViewById(R.id.btn_opcion_incorrecta2);

        btnCorrecto.setTag(20);
        btnMal1.setTag(10);
        btnMal2.setTag(24);

        btnCorrecto.setOnClickListener(this);
        btnMal1.setOnClickListener(this);
        btnMal2.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View v) {
        int valorSeleccionado = (int) v.getTag();
        validar(valorSeleccionado);
    }

    private void validar(int seleccion) {
        ActividadLeccion4M actividad = null;
        if(getActivity() instanceof ActividadLeccion4M) {
            actividad = (ActividadLeccion4M) getActivity();
        }

        if (seleccion == respuestaCorrecta) {
            if(actividad != null) reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Correcto! El perímetro es 20 m.", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            if(actividad != null) reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Casi! Revisa todos los lados y suma de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {}

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}