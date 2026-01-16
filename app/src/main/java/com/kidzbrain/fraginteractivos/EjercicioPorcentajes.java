package com.kidzbrain.fraginteractivos;

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

import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioPorcentajes extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int[] imagenes = {
            R.drawable.diez_cuadros,  // 10 cuadritos coloreados
            R.drawable.cincuenta_cuadros,  // 50 cuadritos coloreados
            R.drawable.setentacinco_cuadros,  // 75 cuadritos coloreados
            R.drawable.cien_cuadros  // Toda la cuadrícula coloreada
    };

    // Respuestas correctas vinculadas a los Tags después de actualizar textos
    private int[] respuestasCorrectas = {0, 1, 2, 2}; // El tag correcto para cada pregunta

    private int indiceActual = 0;
    private ImageView imgPorc;
    private TextView txtProgreso;
    private Button btn0, btn1, btn2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lec8_int_op1, container, false);

        imgPorc = vista.findViewById(R.id.img_ejercicio_porc);
        txtProgreso = vista.findViewById(R.id.txt_progreso_porc);
        btn0 = vista.findViewById(R.id.btn_p_op0);
        btn1 = vista.findViewById(R.id.btn_p_op1);
        btn2 = vista.findViewById(R.id.btn_p_op2);

        btn0.setTag(0); btn1.setTag(1); btn2.setTag(2);
        btn0.setOnClickListener(this); btn1.setOnClickListener(this); btn2.setOnClickListener(this);

        cargarPregunta();
        return vista;
    }

    private void cargarPregunta() {
        imgPorc.setImageResource(imagenes[indiceActual]);
        txtProgreso.setText("Pregunta " + (indiceActual + 1) + " de " + imagenes.length);

        // Cambiamos las opciones según la pregunta para que sea dinámico
        if (indiceActual == 0) { // 10%
            btn0.setText("10%"); btn1.setText("20%"); btn2.setText("5%");
        } else if (indiceActual == 1) { // 50%
            btn0.setText("25%"); btn1.setText("50%"); btn2.setText("10%");
        } else if (indiceActual == 2) { // 75%
            btn0.setText("50%"); btn1.setText("100%"); btn2.setText("75%");
        } else if (indiceActual == 3) { // 100%
            btn0.setText("0%"); btn1.setText("50%"); btn2.setText("100%");
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();
        if (seleccion == respuestasCorrectas[indiceActual]) {
            reproducirSonido("sonido_correcto");
            indiceActual++;
            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Bien hecho! Siguiente nivel.", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                Toast.makeText(getContext(), "¡Eres un experto en Porcentajes!", Toast.LENGTH_LONG).show();
                comprobarRespuesta();
            }
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Ups! Recuerda que cada cuadrito es 1%.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void comprobarRespuesta() { notificarPasoCompletado(); }

    @Override protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}