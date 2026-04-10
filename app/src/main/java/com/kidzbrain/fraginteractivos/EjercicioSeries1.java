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

public class EjercicioSeries1 extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lec1_4_int2_series, container, false);

        Button btn9 = view.findViewById(R.id.btn_opcion_9);
        Button btn10 = view.findViewById(R.id.btn_opcion_10);

        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_opcion_10) {
            // Respuesta Correcta: 10 (2, 4, 6, 8, 10)
            reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Increíble! Eres un experto.", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Ups! Suma 2 al número 8.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        // Lógica en onClick
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}
