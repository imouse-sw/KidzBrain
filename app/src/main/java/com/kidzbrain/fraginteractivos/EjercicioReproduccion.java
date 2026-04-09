package com.kidzbrain.fraginteractivos;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EjercicioReproduccion extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    // Clase para estructurar las preguntas del banco de datos
    private class FaseBiologica {
        int imagen;
        String desc;
        String[] opciones;
        int correcta;

        FaseBiologica(int img, String d, String[] ops, int c) {
            this.imagen = img;
            this.desc = d;
            this.opciones = ops;
            this.correcta = c;
        }
    }

    private List<FaseBiologica> bancoDatos;
    private int indiceActual = 0;
    private int puntos = 0;

    private ImageView imgPrincipal;
    private TextView txtProgreso, txtPuntos, txtDesc;
    private LinearLayout layoutControles, layoutFinal;
    private Button[] botones = new Button[3];

    // MediaPlayer para instrucciones largas
    private MediaPlayer mediaPlayerInstruccion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ejercicio_reproduccion, container, false);

        // Enlace de componentes UI
        imgPrincipal = v.findViewById(R.id.img_repro_pregunta);
        txtProgreso = v.findViewById(R.id.txt_progreso_repro);
        txtPuntos = v.findViewById(R.id.txt_puntos_repro);
        txtDesc = v.findViewById(R.id.txt_descripcion_repro);
        layoutControles = v.findViewById(R.id.layout_controles_repro);
        layoutFinal = v.findViewById(R.id.layout_final_repro);

        botones[0] = v.findViewById(R.id.btn_repro_op0);
        botones[1] = v.findViewById(R.id.btn_repro_op1);
        botones[2] = v.findViewById(R.id.btn_repro_op2);

        for (int i = 0; i < 3; i++) {
            botones[i].setTag(i);
            botones[i].setOnClickListener(this);
        }

        v.findViewById(R.id.btn_repro_reiniciar).setOnClickListener(view -> reiniciarSimulacion());

        inicializarBanco();

        // Ventana de instrucciones
        mostrarVentanaInstruccion(
                "Módulo de Simulación: Reproducción",
                "Observa cada imagen y analiza qué proceso, estructura o tipo de reproducción representa.\n" +
                        "Después, selecciona la opción correcta para continuar.\n" +
                        "¡Demuestra cuánto sabes sobre la reproducción en plantas y animales!"
        );

        cargarFase();
        return v;
    }

    private void inicializarBanco() {
        bancoDatos = new ArrayList<>();
        bancoDatos.add(new FaseBiologica(R.drawable.img_polen, "¿Qué estructura facilita la fecundación en plantas?", new String[]{"Estambre", "Polen", "Raíz"}, 1));
        bancoDatos.add(new FaseBiologica(R.drawable.img_semilla, "Unidad de dispersión que contiene el embrión:", new String[]{"Semilla", "Fruto", "Tallo"}, 0));
        bancoDatos.add(new FaseBiologica(R.drawable.img_mamifero, "Tipo de reproducción con desarrollo embrionario interno:", new String[]{"Ovípara", "Vivípara", "Asexual"}, 1));
        bancoDatos.add(new FaseBiologica(R.drawable.img_oviparo, "Organismos que nacen de un huevo:", new String[]{"Mamíferos", "Ovíparos", "Vivíparos"}, 1));
        bancoDatos.add(new FaseBiologica(R.drawable.img_germinacion, "Fase donde la semilla rompe su latencia:", new String[]{"Floración", "Polinización", "Germinación"}, 2));
        bancoDatos.add(new FaseBiologica(R.drawable.img_herencia, "Transmisión de rasgos de padres a hijos:", new String[]{"Metamorfosis", "Herencia Genética", "Fotosíntesis"}, 1));
        bancoDatos.add(new FaseBiologica(R.drawable.img_metamorfosis, "Cambio drástico en el desarrollo de algunos insectos:", new String[]{"Metamorfosis", "Crecimiento Lineal", "Gestación"}, 0));
        bancoDatos.add(new FaseBiologica(R.drawable.img_fruto, "Estructura que protege a las semillas en las angiospermas:", new String[]{"Hoja", "Sépalo", "Fruto"}, 2));

        Collections.shuffle(bancoDatos);
    }

    private void cargarFase() {
        if (indiceActual < bancoDatos.size()) {
            FaseBiologica actual = bancoDatos.get(indiceActual);
            imgPrincipal.setImageResource(actual.imagen);
            txtDesc.setText(actual.desc);
            txtProgreso.setText("ANÁLISIS " + (indiceActual + 1) + "/" + bancoDatos.size());

            for (int i = 0; i < 3; i++) {
                botones[i].setText(actual.opciones[i]);
            }
        } else {
            layoutControles.setVisibility(View.GONE);
            layoutFinal.setVisibility(View.VISIBLE);
            txtDesc.setText("Puntaje Final: " + puntos + " / 800");
            notificarPasoCompletado();
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();

        if (seleccion == bancoDatos.get(indiceActual).correcta) {
            reproducirSonido("sonido_ok");
            puntos += 100;
            txtPuntos.setText("PUNTOS: " + puntos);
            indiceActual++;
            cargarFase();
        } else {
            reproducirSonido("sonido_error");
            Toast.makeText(getContext(), "Error en el análisis. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            puntos = Math.max(0, puntos - 20);
            txtPuntos.setText("PUNTOS: " + puntos);
        }
    }

    private void reiniciarSimulacion() {
        indiceActual = 0;
        puntos = 0;
        txtPuntos.setText("PUNTOS: 0");
        layoutControles.setVisibility(View.VISIBLE);
        layoutFinal.setVisibility(View.GONE);
        inicializarBanco();
        cargarFase();
    }

    private void mostrarVentanaInstruccion(String titulo, String mensaje) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setCancelable(false);

        builder.setPositiveButton("INICIAR", (d, w) -> {
            detenerAudioInstruccion();
            d.dismiss();
        });

        builder.setNeutralButton("🔊 Escuchar", null);

        android.app.AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnBocina = dialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL);
            btnBocina.setOnClickListener(v -> {
                reproducirAudioInstruccion();
                Toast.makeText(getContext(), "Reproduciendo instrucciones...", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show();
    }

    private void reproducirAudioInstruccion() {
        detenerAudioInstruccion();

        mediaPlayerInstruccion = MediaPlayer.create(getContext(), R.raw.instruccion_repro_adv);
        if (mediaPlayerInstruccion != null) {
            mediaPlayerInstruccion.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayerInstruccion = null;
            });
            mediaPlayerInstruccion.start();
        }
    }

    private void detenerAudioInstruccion() {
        if (mediaPlayerInstruccion != null) {
            if (mediaPlayerInstruccion.isPlaying()) {
                mediaPlayerInstruccion.stop();
            }
            mediaPlayerInstruccion.release();
            mediaPlayerInstruccion = null;
        }
    }

    @Override
    protected void comprobarRespuesta() {
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool sp) {
        mapaSonidos.put("sonido_ok", sp.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_error", sp.load(context, R.raw.aud_incorrecto, 1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detenerAudioInstruccion();
    }
}