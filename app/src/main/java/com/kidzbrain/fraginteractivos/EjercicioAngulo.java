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

import com.kidzbrain.lecciones.matematicas.tercerocuarto.ActividadLeccion2_3M;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioAngulo extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int[] imagenes = {
            R.drawable.ventana,  // Imagen 1
            R.drawable.picsa2,    // Imagen 2
            R.drawable.reloj,   // Imagen 3
            R.drawable.tirejas,  // Imagen 4
            R.drawable.recto  // Imagen 5
    };

    private final int[] respuestasCorrectas = {
            1, // Ventana = Recto
            0, // Pizza = Agudo
            2, // Reloj abierto = Obtuso
            0, // Tijeras cerradas = Agudo
            1  // Escuadra = Recto
    };

    private int indiceActual = 0;
    private ImageView imgFigura;
    private TextView txtProgreso;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec6_int_op1, container, false);

        imgFigura = vistita.findViewById(R.id.img_figura_angulo);
        txtProgreso = vistita.findViewById(R.id.txt_progreso_angulos);

        Button btnAgudo = vistita.findViewById(R.id.btn_agudo);
        Button btnRecto = vistita.findViewById(R.id.btn_recto);
        Button btnObtuso = vistita.findViewById(R.id.btn_obtuso);

        // Asignamos tags numéricos para identificar qué botón es cuál
        btnAgudo.setTag(0);
        btnRecto.setTag(1);
        btnObtuso.setTag(2);

        btnAgudo.setOnClickListener(this);
        btnRecto.setOnClickListener(this);
        btnObtuso.setOnClickListener(this);

        // Cargar la primera pregunta
        cargarPregunta();

        return vistita;
    }

    private void cargarPregunta() {
        // Actualizar imagen
        imgFigura.setImageResource(imagenes[indiceActual]);

        // Actualizar texto de progreso
        String progreso = "Figura " + (indiceActual + 1) + " de " + imagenes.length;
        txtProgreso.setText(progreso);
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();
        verificarRespuesta(seleccion);
    }

    private void verificarRespuesta(int seleccion) {
        ActividadLeccion2_3M actividad = null;
        if(getActivity() instanceof ActividadLeccion2_3M) {
            actividad = (ActividadLeccion2_3M) getActivity();
        }

        if (seleccion == respuestasCorrectas[indiceActual]) {
            // CORRECTO
            if(actividad != null) reproducirSonido("sonido_correcto");

            // Avanzar al siguiente
            indiceActual++;

            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Bien hecho!", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                // TERMINÓ EL JUEGO
                Toast.makeText(getContext(), "¡Excelente! Identificaste todos.", Toast.LENGTH_SHORT).show();
                comprobarRespuesta(); // Finalizar lección
            }

        } else {
            // INCORRECTO
            if(actividad != null) reproducirSonido("sonido_incorrecto");
            // Mensaje específico solicitado
            Toast.makeText(getContext(), "¡Casi! Observa la abertura del ángulo y vuelve a intentar.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        // Notificar a la actividad padre que el ejercicio completo terminó
        notificarPasoCompletado();
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}