package com.kidzbrain.juegos.habitats;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kidzbrain.login.R;

public class BrainBotDialogHelper {

    private static MediaPlayer mediaPlayer;

    public static void mostrarDialogo(Context context, String texto, int audioResId) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_brainbot_instrucciones, null);

        TextView tvTexto = view.findViewById(R.id.tvTextoBrainBot);
        ConstraintLayout btnEscuchar = view.findViewById(R.id.btnEscucharDialogo);
        ConstraintLayout btnCerrar = view.findViewById(R.id.btnCerrarDialogo);

        tvTexto.setText(texto);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        btnEscuchar.setOnClickListener(v -> reproducirAudio(context, audioResId));

        btnCerrar.setOnClickListener(v -> {
            detenerAudio();
            dialog.dismiss();
        });

        dialog.show();
        reproducirAudio(context, audioResId);
    }

    private static void reproducirAudio(Context context, int audioResId) {
        detenerAudio();
        mediaPlayer = MediaPlayer.create(context, audioResId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> detenerAudio());
        }
    }

    public static void detenerAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}