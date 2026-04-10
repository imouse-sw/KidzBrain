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

import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioPatrones1 extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lec1_4_int1_patrones, container, false);

        Button btnRojo = view.findViewById(R.id.btn_opcion_rojo);
        Button btnAzul = view.findViewById(R.id.btn_opcion_azul);

        btnRojo.setOnClickListener(this);
        btnAzul.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_opcion_azul) {
            // Respuesta Correcta: Rojo
            reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Excelente detective!", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Ups! Mira bien el patrón.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        // Implementado directamente en onClick para este caso simple
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}
