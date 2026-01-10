package com.example.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioMultFracciones extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int[] imagenes = {
            R.drawable.multsex, // Imagen 1: Muestra 1/2 de 1/3
            R.drawable.multoct  // Imagen 2: Muestra 1/2 de 1/4
    };

    // Respuestas correctas vinculadas a los Tags:
    // Pregunta 1: 1/6 es Tag 0.
    // Pregunta 2: 1/8 es Tag 1.
    private final int[] respuestasCorrectas = {0, 1};

    private int indiceActual = 0;
    private ImageView imgMult;
    private TextView txtProgreso;
    private TextView txtTitulo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lec7_int_op2, container, false);

        imgMult = vista.findViewById(R.id.img_ejercicio_mult);
        txtProgreso = vista.findViewById(R.id.txt_progreso_mult);
        txtTitulo = vista.findViewById(R.id.txt_titulo_mult);

        Button btn0 = vista.findViewById(R.id.btn_m_op0);
        Button btn1 = vista.findViewById(R.id.btn_m_op1);
        Button btn2 = vista.findViewById(R.id.btn_m_op2);

        // Tags según el texto del botón: Tag 0=1/6, Tag 1=1/8, Tag 2=1/10
        btn0.setTag(0);
        btn1.setTag(1);
        btn2.setTag(2);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        cargarPregunta();

        return vista;
    }

    private void cargarPregunta() {
        imgMult.setImageResource(imagenes[indiceActual]);
        txtProgreso.setText("Pregunta " + (indiceActual + 1) + " de " + imagenes.length);

        if(indiceActual == 0) {
            txtTitulo.setText("¿Cuánto es la mitad de 1/3?");
        } else {
            txtTitulo.setText("¿Cuánto es la mitad de 1/4?");
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();

        if (seleccion == respuestasCorrectas[indiceActual]) {
            reproducirSonido("sonido_correcto");
            indiceActual++;

            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Bien pensado! Vamos por la otra.", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                Toast.makeText(getContext(), "¡Excelente! Entiendes las partes de las partes.", Toast.LENGTH_LONG).show();
                comprobarRespuesta();
            }
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Casi! Cuenta en cuántas partes iguales se dividiría el total.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        notificarPasoCompletado();
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}