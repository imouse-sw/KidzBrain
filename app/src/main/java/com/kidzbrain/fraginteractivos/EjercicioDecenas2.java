package com.kidzbrain.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kidzbrain.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioDecenas2 extends PlantillaFragmentoInteractivo implements View.OnClickListener {
    Integer d1, d2, u1, u2;
    int d1c = 4, u1c = 2, d2c = 3, u2c = 0;
    Button boton;
    EditText etD1, etD2, etU1, etU2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec3_int1_dec2, container, false);

        boton = vistita.findViewById(R.id.btn_comprobar);
        etD1 = vistita.findViewById(R.id.edit_decenas);
        etD2 = vistita.findViewById(R.id.edit_decenas2);
        etU1 = vistita.findViewById(R.id.edit_unidades);
        etU2 = vistita.findViewById(R.id.edit_unidades2);

        boton.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View view) {
        comprobarRespuesta();
    }

    @Override
    protected void comprobarRespuesta() {
        ActividadLeccion1M actividad = null;
        if(getActivity() instanceof ActividadLeccion1M) {
            actividad = (ActividadLeccion1M) getActivity();
        }

        String d1Txt = etD1.getText().toString().trim();
        String d2Txt = etD2.getText().toString().trim();
        String u1Txt = etU1.getText().toString().trim();
        String u2Txt = etU2.getText().toString().trim();

        if(d1Txt.isEmpty() || d2Txt.isEmpty() || u1Txt.isEmpty() || u2Txt.isEmpty()) {
            Toast.makeText(getContext(), "¡Llena todos los espacios vacíos!", Toast.LENGTH_SHORT).show();
        }
        else {
            d1 = Integer.parseInt(d1Txt);
            d2 = Integer.parseInt(d2Txt);
            u1 = Integer.parseInt(u1Txt);
            u2 = Integer.parseInt(u2Txt);

            if(d1 == d1c && d2 == d2c && u1 == u1c && u2 == u2c) {
                if(actividad!=null) {
                    reproducirSonido("sonido_correcto");
                    reproducirSonido("voz_correcto");
                }
                Toast.makeText(getContext(), "¡Muy bien!", Toast.LENGTH_SHORT).show();
                notificarPasoCompletado();
            }
            else {
                if (actividad != null) {
                    reproducirSonido("sonido_incorrecto");
                    reproducirSonido("voz_incorrecto");
                }
                Toast.makeText(getContext(), "¡Revisa muy bien! Una de estas tiene truco.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
        mapaSonidos.put("voz_correcto", soundPool.load(context, R.raw.aud_voz_correcto, 1));
        mapaSonidos.put("voz_incorrecto", soundPool.load(context, R.raw.aud_voz_incorrecto, 1));
    }
}
