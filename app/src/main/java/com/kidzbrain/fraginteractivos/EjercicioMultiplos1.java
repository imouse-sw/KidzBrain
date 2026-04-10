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

public class EjercicioMultiplos1 extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lec2_4_int1_multiplos, container, false);

        Button btn7 = view.findViewById(R.id.btn_opcion_7);
        Button btn9 = view.findViewById(R.id.btn_opcion_9);

        btn7.setOnClickListener(this);
        btn9.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_opcion_9) {
            // Respuesta Correcta: 9 (es múltiplo de 3)
            reproducirSonido("sonido_correcto");
            Toast.makeText(getContext(), "¡Bien hecho! 3 x 3 = 9", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Ups! El 7 no está en la tabla del 3.", Toast.LENGTH_SHORT).show();
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
