package com.example.fraginteractivos;

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

import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioContarRanitas extends PlantillaFragmentoInteractivo implements View.OnClickListener {
    private final Integer respuestaCorrectaNumero = 15;
    String textoIntroducido;
    Button boton;
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec2_int1_ranitas, container, false);

        boton = vistita.findViewById(R.id.boton_ranitas);
        editText = vistita.findViewById(R.id.editadito_ranitas);

        boton.setOnClickListener(this);

        return vistita;
    }

    @Override
    public void onClick(View view) {
        comprobarRespuesta();
    }

    @Override
    protected void comprobarRespuesta() {
        ActividadLeccion1 actividad = null;
        if(getActivity() instanceof ActividadLeccion1) {
            actividad = (ActividadLeccion1) getActivity();
        }

        textoIntroducido = editText
                .getText()
                .toString()
                .toLowerCase()
                .trim();

        if(textoIntroducido.isEmpty()) {
            Toast.makeText(getContext(), "¡Introduce una respuesta!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Integer numeroIntroducido = Integer.parseInt(textoIntroducido);

                if(numeroIntroducido.equals(respuestaCorrectaNumero)) {
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
                    Toast.makeText(getContext(), "¡Ups! Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (NumberFormatException e) {
                if(textoIntroducido.equals("quince")) {
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
                    Toast.makeText(getContext(), "¡Ups! Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                }
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
