package com.kidzbrain.juegos.tanque;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R; // Asegúrate que este import sea correcto para tu proyecto

public class Tanque extends AppCompatActivity {

    // Vistas
    ImageView imgBrainbot, aguaView;
    TextView txtObjetivo, txtMensaje, txtCalculoActual, lblAlto, lblAncho, lblProf;
    SeekBar sbAlto, sbAncho, sbProf;
    Button bConstruir;

    // Lógica del juego
    int valAlto = 1, valAncho = 1, valProf = 1; // Valores mínimos iniciales (1m)
    int targetVolumen;
    boolean esModoFacil;

    // Sonidos (Opcional, reutiliza los de Pizza)
    SoundPool soundPool;
    int instruccionesVoz, correctoIdFx, incorrectoIdFx, correctoIdVoz, incorrectoIdVoz, aguaId, incorrectoAguaIdFx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tanque);

        // 1. Vincular Vistas
        imgBrainbot = findViewById(R.id.img_brainbot);
        aguaView = findViewById(R.id.agua_view);
        txtObjetivo = findViewById(R.id.txtObjetivo);
        txtMensaje = findViewById(R.id.txtMensaje);
        txtCalculoActual = findViewById(R.id.txtCalculoActual);

        lblAlto = findViewById(R.id.lblAlto);
        lblAncho = findViewById(R.id.lblAncho);
        lblProf = findViewById(R.id.lblProf);

        sbAlto = findViewById(R.id.sbAlto);
        sbAncho = findViewById(R.id.sbAncho);
        sbProf = findViewById(R.id.sbProf);

        bConstruir = findViewById(R.id.bConstruir);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(6);
        soundPool = builder.build();

        aguaView.post(() -> {
            aguaView.setPivotY(aguaView.getHeight());
            aguaView.setPivotX(aguaView.getWidth() / 2f);
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String dif = extras.getString("Dificultad", "Jugar en Modo Normal");
            esModoFacil = dif.equals("Jugar en Modo Fácil");
        }
        setupListeners();

        nuevoNivel();

        instruccionesVoz = soundPool.load(this, R.raw.aud_tanque_voz_instrucciones, 1);
        correctoIdFx = soundPool.load(this, R.raw.yupi, 1);
        incorrectoIdFx = soundPool.load(this, R.raw.bowomp, 1);
        incorrectoAguaIdFx = soundPool.load(this, R.raw.aud_voz_awa, 1);
        correctoIdVoz = soundPool.load(this, R.raw.aud_muybien, 1);
        incorrectoIdVoz = soundPool.load(this, R.raw.aud_muymal, 1);
        aguaId = soundPool.load(this, R.raw.sfx_agua, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                if (sampleId == instruccionesVoz) {
                    soundPool.play(instruccionesVoz, 1, 1, 0, 0, 1);
                }
            }
        });
    }

    private void setupListeners() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // +1 porque la barra va de 0 a 8, pero queremos valores de 1 a 9 m
                int valorReal = progress + 1;

                if (seekBar.getId() == R.id.sbAlto) valAlto = valorReal;
                if (seekBar.getId() == R.id.sbAncho) valAncho = valorReal;
                if (seekBar.getId() == R.id.sbProf) valProf = valorReal;

                actualizarVisuales();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        sbAlto.setOnSeekBarChangeListener(listener);
        sbAncho.setOnSeekBarChangeListener(listener);
        sbProf.setOnSeekBarChangeListener(listener);

        bConstruir.setOnClickListener(v -> verificarVictoria());
    }

    private void actualizarVisuales() {
        // Texto de feedback
        lblAlto.setText("Alto: " + valAlto + "m");
        lblAncho.setText("Ancho: " + valAncho + "m");
        lblProf.setText("Prof.: " + valProf + "m");

        int volumenActual = valAlto * valAncho * valProf;
        txtCalculoActual.setText(valAlto + " x " + valAncho + " x " + valProf + " = " + volumenActual + " m³");

        // Magia Visual: Escalar el agua
        // Dividimos entre 9f porque es el máximo valor posible para normalizarlo (0.1 a 1.0)
        float escalaY = valAlto / 9f;
        float escalaX = valAncho / 9f;

        // La profundidad la simulamos con opacidad (alpha)
        // Más profundo = Más oscuro/sólido (Alpha 1.0). Menos profundo = Más transparente (Alpha 0.4)
        float alpha = 0.3f + (valProf / 9f * 0.7f);

        aguaView.animate()
                .scaleY(escalaY)
                .scaleX(escalaX)
                .alpha(alpha)
                .setDuration(100) // Animación suave
                .start();
    }

    private void nuevoNivel() {
        int tAlto = 0, tAncho = 0, tProf = 0;

        // Resetear visuales
        imgBrainbot.setImageResource(R.drawable.px_bb_constructor);
        txtMensaje.setText("Calcula las medidas...");
        bConstruir.setEnabled(true);
        bConstruir.setText("¡CONSTRUIR!");

        // Generar problema matemático
        // Usamos números pequeños (1-5) para evitar volúmenes gigantes
        tAlto = (int)(Math.random() * 7) + 1;
        tAncho = (int)(Math.random() * 6) + 1;
        tProf = (int)(Math.random() * 9) + 1;

        targetVolumen = tAlto * tAncho * tProf;

        txtObjetivo.setText("Meta: " + targetVolumen + " m³");

        // Resetear barras a 1
        sbAlto.setProgress(0);
        sbAncho.setProgress(0);
        sbProf.setProgress(0);

        // Habilitar todas las barras primero
        sbAlto.setEnabled(true);
        sbAncho.setEnabled(true);
        sbProf.setEnabled(true);

        if (esModoFacil) {
            sbAncho.setProgress(tAncho - 1);
            sbAncho.setEnabled(false); // Bloqueado

            sbProf.setProgress(tProf - 1);
            sbProf.setEnabled(false); // Bloqueado

            txtMensaje.setText("La base ya está lista (" + tAncho + "x" + tProf + "). Ajusta la altura.");
        }

        // Forzar actualización visual inicial
        valAlto = 1;
        valAncho = esModoFacil ? tAncho : 1;
        valProf = esModoFacil ? tProf : 1;
        actualizarVisuales();
    }

    private void verificarVictoria() {
        int volumenActual = valAlto * valAncho * valProf;

        if (volumenActual == targetVolumen) {
            // GANÓ
            imgBrainbot.setImageResource(R.drawable.px_bb_constructor_oa); // Ojos Abiertos / Feliz
            txtMensaje.setText("¡EXCELENTE TRABAJO!");
            soundPool.play(correctoIdFx, 1, 1, 0, 0, 1);
            soundPool.play(correctoIdVoz, 1, 1, 0, 0, 1);

            bConstruir.setEnabled(false);

            new Handler(Looper.getMainLooper()).postDelayed(this::nuevoNivel, 2500);

        } else {
            // PERDIÓ (Feedback visual)
            if (volumenActual > targetVolumen) {
                // Se desbordó
                imgBrainbot.setImageResource(R.drawable.px_bb_constructor_mojado);
                txtMensaje.setText("¡CUIDADO! Se desbordó el tanque.");
                soundPool.play(incorrectoAguaIdFx, 1, 1, 0, 0, 1);
                soundPool.play(aguaId, 1, 1, 0, 0, 1);
            } else {
                // Falta agua
                imgBrainbot.setImageResource(R.drawable.px_bb_constructor); // O un Brainbot triste si tienes
                txtMensaje.setText("Aún falta llenar más...");
                soundPool.play(incorrectoIdVoz, 1, 1, 0 ,0, 1);
                soundPool.play(incorrectoIdFx, 1, 1, 0, 0, 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}