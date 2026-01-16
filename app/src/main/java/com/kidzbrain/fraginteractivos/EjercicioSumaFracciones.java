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

public class EjercicioSumaFracciones extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    // Lista de imágenes para las preguntas
    private final int[] imagenes = {
            R.drawable.ejerciciofrac, // Pregunta 1: 1/4 + 2/4
            R.drawable.ejerciciofrac2 // Pregunta 2: 3/4 - 1/4 (Sugerida)
    };

    // Respuestas correctas vinculadas a los Tags (0, 1, 2)
    // Para la primera pregunta, 3/4 es la opción correcta (Tag 1)
    private final int[] respuestasCorrectas = {
            1, // Tag 1 = "3/4"
            0  // Tag 0 = "2/4" (Resultado de 3/4 - 1/4)
    };

    private int indiceActual = 0;
    private ImageView imgEjercicio;
    private TextView txtProgreso;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout que me pasaste
        View vista = inflater.inflate(R.layout.fragment_lec7_int_op1, container, false);

        // Referencias del XML
        imgEjercicio = vista.findViewById(R.id.img_ejercicio_suma);
        txtProgreso = vista.findViewById(R.id.txt_progreso_suma);

        Button btn0 = vista.findViewById(R.id.btn_s_op0);
        Button btn1 = vista.findViewById(R.id.btn_s_op1);
        Button btn2 = vista.findViewById(R.id.btn_s_op2);

        // Asignamos los Tags según las opciones del XML
        btn0.setTag(0); // Representa "2/4"
        btn1.setTag(1); // Representa "3/4"
        btn2.setTag(2); // Representa "4/4"

        // Listeners
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        cargarPregunta();

        return vista;
    }

    private void cargarPregunta() {
        // Cambiar imagen
        imgEjercicio.setImageResource(imagenes[indiceActual]);

        // Actualizar el título de progreso
        String textoProgreso = "Pregunta " + (indiceActual + 1) + " de " + imagenes.length;
        txtProgreso.setText(textoProgreso);
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();
        verificarRespuesta(seleccion);
    }

    private void verificarRespuesta(int seleccion) {
        if (seleccion == respuestasCorrectas[indiceActual]) {
            // ACIERTO
            reproducirSonido("sonido_correcto");
            indiceActual++;

            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Muy bien! Sigue así.", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                // FIN DEL EJERCICIO
                Toast.makeText(getContext(), "¡Dominas las sumas de fracciones!", Toast.LENGTH_LONG).show();
                comprobarRespuesta();
            }
        } else {
            // ERROR
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Casi! Si el denominador es igual, solo suma lo de arriba.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        // Notifica que esta parte de la lección terminó
        notificarPasoCompletado();
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}