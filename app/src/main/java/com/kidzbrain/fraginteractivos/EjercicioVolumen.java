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

public class EjercicioVolumen extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private final int[] imagenes = {
            R.drawable.vol_conteo1,   // Prisma de 8 cubos (2x2x2)
            R.drawable.vol_conteo2,   // Prisma de 12 cubos (3x2x2)
            R.drawable.vol_formula1,  // Caja con etiquetas: 5, 2, 3
            R.drawable.vol_conteo3,     // Cubo de 3x3x3
            R.drawable.vol_piscina     // Piscina: Base 10m2, Altura 2m
    };

    // Tags correctos para cada pregunta
    private int[] respuestasCorrectas = {1, 0, 2, 1, 0};

    private int indiceActual = 0;
    private ImageView imgVol;
    private TextView txtProgreso;
    private Button btn0, btn1, btn2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lec9_int_op1, container, false);

        imgVol = vista.findViewById(R.id.img_ejercicio_vol);
        txtProgreso = vista.findViewById(R.id.txt_progreso_vol);
        btn0 = vista.findViewById(R.id.btn_v_op0);
        btn1 = vista.findViewById(R.id.btn_v_op1);
        btn2 = vista.findViewById(R.id.btn_v_op2);

        btn0.setTag(0); btn1.setTag(1); btn2.setTag(2);
        btn0.setOnClickListener(this); btn1.setOnClickListener(this); btn2.setOnClickListener(this);

        cargarPregunta();
        return vista;
    }

    private void cargarPregunta() {
        imgVol.setImageResource(imagenes[indiceActual]);
        txtProgreso.setText("Pregunta " + (indiceActual + 1) + " de " + imagenes.length);

        if (indiceActual == 0) { // 2x2x2 = 8
            btn0.setText("6u³"); btn1.setText("8u³"); btn2.setText("4u³");
        } else if (indiceActual == 1) { // 3x2x2 = 12
            btn0.setText("12u³"); btn1.setText("7u³"); btn2.setText("10u³");
        } else if (indiceActual == 2) { // 5x2x3 = 30
            btn0.setText("10u³"); btn1.setText("15u³"); btn2.setText("30u³");
        } else if (indiceActual == 3) { // 3x3x3 = 27
            btn0.setText("9u³"); btn1.setText("27u³"); btn2.setText("12u³");
        } else if (indiceActual == 4) { // 10 * 2 = 20
            btn0.setText("20m³"); btn1.setText("12m³"); btn2.setText("40m³");
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();
        if (seleccion == respuestasCorrectas[indiceActual]) {
            reproducirSonido("sonido_correcto");
            indiceActual++;
            if (indiceActual < imagenes.length) {
                Toast.makeText(getContext(), "¡Excelente constructor!", Toast.LENGTH_SHORT).show();
                cargarPregunta();
            } else {
                Toast.makeText(getContext(), "¡Nivel Experto en 3D alcanzado!", Toast.LENGTH_LONG).show();
                comprobarRespuesta();
            }
        } else {
            reproducirSonido("sonido_incorrecto");
            Toast.makeText(getContext(), "Multiplica: Largo x Ancho x Alto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void comprobarRespuesta() { notificarPasoCompletado(); }

    @Override protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
    }
}