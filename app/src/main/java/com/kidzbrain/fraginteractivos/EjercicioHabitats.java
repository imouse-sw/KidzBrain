package com.kidzbrain.fraginteractivos;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.util.ArrayList;
import java.util.List;

public class EjercicioHabitats extends PlantillaFragmentoInteractivo {
    private class AnimalInfo {
        int imgRes;
        int targetId;
        String explicacion;

        public AnimalInfo(int imgRes, int targetId, String explicacion) {
            this.imgRes = imgRes;
            this.targetId = targetId;
            this.explicacion = explicacion;
        }
    }

    private List<AnimalInfo> animales;
    private int currentIdx = 0;
    private ImageView imgAnimal;
    private TextView txtExplicacion;
    private Button btnNext;

    private SoundPool soundPool;
    private int soundOk, soundError;
    private MediaPlayer mediaPlayerInstrucciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ejercicio_habitats, container, false);

        imgAnimal = v.findViewById(R.id.imagen_animal);
        txtExplicacion = v.findViewById(R.id.texto_explicacion);
        btnNext = v.findViewById(R.id.btn_siguiente_animal);

        setupDragAndDrop(v);
        initData();
        loadAnimal();

        btnNext.setOnClickListener(view -> {
            currentIdx++;
            loadAnimal();
        });

        mostrarVentanaInstrucciones();

        return v;
    }

    private void mostrarVentanaInstrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¡Hola, pequeño explorador!");
        builder.setMessage("Hoy vamos a jugar con los animales y sus hábitats.\n" +
                "Observa muy bien a cada animal y arrástralo al lugar donde vive.\n" +
                "Recuerda: cada uno tiene un hogar especial, como el mar, el bosque, la madriguera o la selva.\n" +
                "¡Vamos a descubrir dónde pertenece cada uno!!");

        builder.setPositiveButton("¡Empezar!", (dialog, id) -> {
            detenerAudioInstrucciones();
            dialog.dismiss();
        });

        builder.setNeutralButton("Escuchar audio", null);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            reproducirAudioInstrucciones();
        });
    }

    private void reproducirAudioInstrucciones() {
        if (mediaPlayerInstrucciones == null) {
            mediaPlayerInstrucciones = MediaPlayer.create(getContext(), R.raw.instrucciones_habitats);
        }
        if (!mediaPlayerInstrucciones.isPlaying()) {
            mediaPlayerInstrucciones.start();
        }
    }

    private void detenerAudioInstrucciones() {
        if (mediaPlayerInstrucciones != null) {
            if (mediaPlayerInstrucciones.isPlaying()) mediaPlayerInstrucciones.stop();
            mediaPlayerInstrucciones.release();
            mediaPlayerInstrucciones = null;
        }
    }

    @Override
    protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        this.soundPool = soundPool;
        soundOk = soundPool.load(context, R.raw.aud_correcto, 1);
        soundError = soundPool.load(context, R.raw.aud_incorrecto, 1);
    }

    private void initData() {
        animales = new ArrayList<>();
        animales.add(new AnimalInfo(R.drawable.delfin, R.id.habitat_agua, "El delfín vive en el mar, donde puede nadar y buscar alimento."));
        animales.add(new AnimalInfo(R.drawable.lobo, R.id.habitat_arbol, "El lobo vive en el bosque, donde encuentra refugio y comida."));
        animales.add(new AnimalInfo(R.drawable.tejon, R.id.habitat_tierra, "El tejón vive en madrigueras, donde puede descansar."));
        animales.add(new AnimalInfo(R.drawable.mono, R.id.habitat_selva, "El mono vive en la selva, donde encuentra árboles y refugio."));
    }

    private void loadAnimal() {
        if (currentIdx < animales.size()) {
            AnimalInfo info = animales.get(currentIdx);
            imgAnimal.setImageResource(info.imgRes);
            imgAnimal.setVisibility(View.VISIBLE);
            imgAnimal.setAlpha(1.0f);
            txtExplicacion.setText("¿A dónde pertenece este animal?");
            btnNext.setVisibility(View.GONE);
        } else {
            finishGame();
        }
    }

    private void setupDragAndDrop(View root) {
        imgAnimal.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadow, v, 0);
                v.setAlpha(0.5f);
                return true;
            }
            return false;
        });

        View.OnDragListener listener = (v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                checkResult(v.getId());
            } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                imgAnimal.setAlpha(1.0f);
            }
            return true;
        };

        root.findViewById(R.id.habitat_agua).setOnDragListener(listener);
        root.findViewById(R.id.habitat_arbol).setOnDragListener(listener);
        root.findViewById(R.id.habitat_tierra).setOnDragListener(listener);
        root.findViewById(R.id.habitat_selva).setOnDragListener(listener);
    }

    private void checkResult(int droppedId) {
        if (droppedId == animales.get(currentIdx).targetId) {
            // Acierto
            if (soundPool != null) soundPool.play(soundOk, 1, 1, 0, 0, 1);
            Toast.makeText(getContext(), "¡Excelente!", Toast.LENGTH_SHORT).show();

            txtExplicacion.setText(animales.get(currentIdx).explicacion);
            imgAnimal.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        } else {
            // Error
            if (soundPool != null) soundPool.play(soundError, 1, 1, 0, 0, 1);
            Toast.makeText(getContext(), "No es su hábitat, ¡observa bien!", Toast.LENGTH_SHORT).show();
        }
    }

    private void finishGame() {
        txtExplicacion.setText("¡Felicidades! Has cuidado muy bien de la naturaleza.");
        imgAnimal.setVisibility(View.GONE);
        btnNext.setText("Reiniciar");
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setOnClickListener(v -> {
            currentIdx = 0;
            loadAnimal();
            mostrarVentanaInstrucciones();
        });
        notificarPasoCompletado();
    }

    @Override protected void comprobarRespuesta() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        detenerAudioInstrucciones();
    }
}