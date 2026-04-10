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

public class EjercicioDados1 extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lec3_4_int2_dados, container, false);

        Button btn16 = view.findViewById(R.id.btn_opcion_1_6);
        Button btn66 = view.findViewById(R.id.btn_opcion_6_6);

        btn16.setOnClickListener(this);
        btn66.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_opcion_1_6) {
            // Respuesta Correcta: 1/6
            reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Exacto! Solo hay un '6' entre las 6 caras.", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Ups! 6/6 significaría que todas las caras tienen un 6.", Toast.LENGTH_SHORT).show();
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
