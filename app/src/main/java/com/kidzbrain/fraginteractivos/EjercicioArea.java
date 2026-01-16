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

import com.kidzbrain.lecciones.matematicas.tercerocuarto.ActividadLeccion4M;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioArea extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int respuestaCorrecta = 21;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec5_int_op2, container, false);

        Button btnCorrecto = vistita.findViewById(R.id.btn_area_correcta);
        Button btnOp1 = vistita.findViewById(R.id.btn_area_op1);
        Button btnOp2 = vistita.findViewById(R.id.btn_area_op2);

        btnCorrecto.setTag(21);
        btnOp1.setTag(20);
        btnOp2.setTag(10);

        btnCorrecto.setOnClickListener(this);
        btnOp1.setOnClickListener(this);
        btnOp2.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View v) {
        int valor = (int) v.getTag();

        ActividadLeccion4M actividad = null;
        if(getActivity() instanceof ActividadLeccion4M) {
            actividad = (ActividadLeccion4M) getActivity();
        }

        if (valor == respuestaCorrecta) {
            if(actividad != null) reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Excelente! 7 x 3 = 21 m²", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            if(actividad != null) reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "Recuerda el truco: Multiplica Largo x Ancho.", Toast.LENGTH_SHORT).show();
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