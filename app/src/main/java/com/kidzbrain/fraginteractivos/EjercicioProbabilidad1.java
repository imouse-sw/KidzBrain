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

public class EjercicioProbabilidad1 extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lec3_4_int1_probabilidad, container, false);

        Button btn57 = view.findViewById(R.id.btn_opcion_5_7);
        Button btn27 = view.findViewById(R.id.btn_opcion_2_7);

        btn57.setOnClickListener(this);
        btn27.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_opcion_2_7) {
            // Respuesta Correcta: 2 de 7 son verdes
            reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Excelente! 2 casos favorables entre 7 totales.", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Casi! Recuerda que buscamos las canicas VERDES.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}
