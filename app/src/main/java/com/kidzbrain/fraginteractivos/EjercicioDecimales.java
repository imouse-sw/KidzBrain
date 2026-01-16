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

public class EjercicioDecimales extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int[] imagenes = {
            R.drawable.compra1, // Imagen para $10.50 + $3.25
            R.drawable.compra2 // Imagen para $20.00 - $5.50
    };

    // Pregunta 1: Respuesta $13.75 (Tag 2)
    // Pregunta 2: Respuesta $14.50 (Vamos a reusar el Tag 0 para la respuesta de la pregunta 2)
    private final int[] respuestasCorrectas = {2, 0};

    private int indiceActual = 0;
    private ImageView imgDecimal;
    private TextView txtPregunta;
    private Button btn0, btn1, btn2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lec7_int_op3, container, false);

        imgDecimal = vista.findViewById(R.id.img_dinero_decimal);
        txtPregunta = vista.findViewById(R.id.txt_pregunta_decimal);

        btn0 = vista.findViewById(R.id.btn_d_op0);
        btn1 = vista.findViewById(R.id.btn_d_op1);
        btn2 = vista.findViewById(R.id.btn_d_op2);

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
        imgDecimal.setImageResource(imagenes[indiceActual]);

        if (indiceActual == 0) {
            txtPregunta.setText("¿Cuánto es $10.50 + $3.25?");
            btn0.setText("$15.50");
            btn1.setText("$13.25");
            btn2.setText("$13.75");
        } else {
            txtPregunta.setText("Si pagas $20.00 y gastas $5.50\n¿Cuánto te sobra?");
            btn0.setText("$14.50"); // Correcta para la pregunta 2
            btn1.setText("$15.50");
            btn2.setText("$14.00");
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();

        if (seleccion == respuestasCorrectas[indiceActual]) {
            reproducirSonido("sonido_correcto");
            indiceActual++;

            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Bien calculado! Vamos por la siguiente.", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                Toast.makeText(getContext(), "¡Excelente! Eres muy bueno con el dinero.", Toast.LENGTH_LONG).show();
                comprobarRespuesta();
            }
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "¡Casi! Revisa bien la alineación de los centavos.", Toast.LENGTH_SHORT).show();
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