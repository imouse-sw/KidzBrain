package com.example.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Asegúrate de importar la actividad correcta
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion4M;
import com.example.login.R;
import com.example.utilidades.leccionutil.PlantillaFragmentoInteractivo;

public class EjercicioDivision extends PlantillaFragmentoInteractivo {

    // NUEVO TOTAL: 24 galletas
    private final int totalGalletas = 24;
    private final int limitePorNino = 8; // 24 entre 3 = 8

    private int galletasRepartidas = 0;
    private int[] cuentaAmigos = {0, 0, 0};

    private TextView[] txtAmigosViews = new TextView[3];
    private TextView txtGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistita = inflater.inflate(R.layout.fragment_lec4_int1_op2, container, false);

        txtGlobal = vistita.findViewById(R.id.txt_contador_global);

        txtAmigosViews[0] = vistita.findViewById(R.id.txt_cuenta_amigo1);
        txtAmigosViews[1] = vistita.findViewById(R.id.txt_cuenta_amigo2);
        txtAmigosViews[2] = vistita.findViewById(R.id.txt_cuenta_amigo3);

        Button btn1 = vistita.findViewById(R.id.btn_dar_1);
        Button btn2 = vistita.findViewById(R.id.btn_dar_2);
        Button btn3 = vistita.findViewById(R.id.btn_dar_3);

        btn1.setOnClickListener(v -> darGalleta(0));
        btn2.setOnClickListener(v -> darGalleta(1));
        btn3.setOnClickListener(v -> darGalleta(2));

        return vistita;
    }

    private void darGalleta(int indiceAmigo) {
        if (galletasRepartidas >= totalGalletas) {
            Toast.makeText(getContext(), "¡Ya no hay más galletas!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cuentaAmigos[indiceAmigo] >= limitePorNino) {
            Toast.makeText(getContext(), "Este amigo ya tiene sus 8 galletas.", Toast.LENGTH_SHORT).show();
            return;
        }

        galletasRepartidas++;
        cuentaAmigos[indiceAmigo]++;


        txtGlobal.setText("Repartidas: " + galletasRepartidas + " / " + totalGalletas);
        txtAmigosViews[indiceAmigo].setText(String.valueOf(cuentaAmigos[indiceAmigo]));


        if (galletasRepartidas == totalGalletas) {
            comprobarRespuesta();
        }
    }

    @Override
    protected void comprobarRespuesta() {
        if (getActivity() instanceof ActividadLeccion4M) {
            reproducirSonido("sonido_correcto");
        }

        Toast.makeText(getContext(), "¡Excelente! 24 ÷ 3 = 8", Toast.LENGTH_LONG).show();
        notificarPasoCompletado();
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
    }
}