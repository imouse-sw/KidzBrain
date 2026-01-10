package com.example.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion4M;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioFraccion extends PlantillaFragmentoInteractivo {

    private final int objetivoSeleccion = 6;

    private ImageView[] slices = new ImageView[8];
    private boolean[] isSelected = new boolean[8];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec4_int1_op3, container, false);
        int[] ids = {
                R.id.slice1, R.id.slice2, R.id.slice3, R.id.slice4,
                R.id.slice5, R.id.slice6, R.id.slice7, R.id.slice8
        };

        for (int i = 0; i < 8; i++) {
            slices[i] = vistita.findViewById(ids[i]);

            final int index = i;

            slices[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSlice(index);
                }
            });
        }

        Button btnComprobar = vistita.findViewById(R.id.btn_comprobar_pizza);
        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarRespuesta();
            }
        });

        return vistita;
    }

    private void toggleSlice(int index) {
        isSelected[index] = !isSelected[index];

        if (isSelected[index]) {
            slices[index].setAlpha(1.0f);
        } else {
            slices[index].setAlpha(0.5f);
        }
    }

    @Override
    protected void comprobarRespuesta() {
        int totalSeleccionadas = 0;
        for (boolean b : isSelected) {
            if (b) totalSeleccionadas++;
        }

        ActividadLeccion4M actividad = null;
        if(getActivity() instanceof ActividadLeccion4M) {
            actividad = (ActividadLeccion4M) getActivity();
        }

        if (totalSeleccionadas == objetivoSeleccion) {
            if(actividad != null) {
                reproducirSonido("sonido_correcto");
            }
            Toast.makeText(getContext(), "¡Excelente! Has seleccionado 6/8.", Toast.LENGTH_SHORT).show();
            notificarPasoCompletado();

        } else {
            if(actividad != null) {
                reproducirSonido("sonido_incorrecto");
            }
            String mensaje = "Tienes " + totalSeleccionadas + " pedazos. Necesitamos 6.";
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}