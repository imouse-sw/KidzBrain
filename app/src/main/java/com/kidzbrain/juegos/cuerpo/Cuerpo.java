package com.kidzbrain.juegos.cuerpo;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kidzbrain.login.R;

import java.util.HashMap;
import java.util.Map;

public class Cuerpo extends AppCompatActivity implements View.OnClickListener {

    ImageButton bsalir;
    ImageView fboca, fbrazo, fcabello, fcejas, fcodo, fcuello, fmano, fnariz, fojos, foreja, fpie, fpierna, frodilla, ftorso;
    ImageView rboca, rbrazo, rcabello, rcejas, rcodo, rcuello, rmano, rnariz, rojos, roreja, rpie, rpierna, rrodilla, rtorso;
    private int puntos = 0;
    Map<String, String> correctMatches;
    private MediaPlayer mediaPlayer;

    private Drawable recuadroOriginalDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuerpo);

        mostrarVentanaEmergente();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_cuerpo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        correctMatches = new HashMap<>();
        correctMatches.put("imagenBoca", "recuadroBoca");
        correctMatches.put("imagenBrazo", "recuadroBrazo");
        correctMatches.put("imagenCabello", "recuadroCabello");
        correctMatches.put("imagenCejas", "recuadroCejas");
        correctMatches.put("imagenCodo", "recuadroCodo");
        correctMatches.put("imagenCuello", "recuadroCuello");
        correctMatches.put("imagenMano", "recuadroMano");
        correctMatches.put("imagenNariz", "recuadroNariz");
        correctMatches.put("imagenOjos", "recuadroOjos");
        correctMatches.put("imagenOreja", "recuadroOreja");
        correctMatches.put("imagenPie", "recuadroPie");
        correctMatches.put("imagenPierna", "recuadroPierna");
        correctMatches.put("imagenRodilla", "recuadroRodilla");
        correctMatches.put("imagenTorso", "recuadroTorso");

        fboca = findViewById(R.id.imagenBoca);
        fbrazo = findViewById(R.id.imagenBrazo);
        fcabello = findViewById(R.id.imagenCabello);
        fcejas = findViewById(R.id.imagenCejas);
        fcodo = findViewById(R.id.imagenCodo);
        fcuello = findViewById(R.id.imagenCuello);
        fmano = findViewById(R.id.imagenMano);
        fnariz = findViewById(R.id.imagenNariz);
        fojos = findViewById(R.id.imagenOjos);
        foreja = findViewById(R.id.imagenOreja);
        fpie = findViewById(R.id.imagenPie);
        fpierna = findViewById(R.id.imagenPierna);
        frodilla = findViewById(R.id.imagenRodilla);
        ftorso = findViewById(R.id.imagenTorso);

        rboca = findViewById(R.id.recuadroBoca);
        rbrazo = findViewById(R.id.recuadroBrazo);
        rcabello = findViewById(R.id.recuadroCabello);
        rcejas = findViewById(R.id.recuadroCejas);
        rcodo = findViewById(R.id.recuadroCodo);
        rcuello = findViewById(R.id.recuadroCuello);
        rmano = findViewById(R.id.recuadroMano);
        rnariz = findViewById(R.id.recuadroNariz);
        rojos = findViewById(R.id.recuadroOjos);
        roreja = findViewById(R.id.recuadroOreja);
        rpie = findViewById(R.id.recuadroPie);
        rpierna = findViewById(R.id.recuadroPierna);
        rrodilla = findViewById(R.id.recuadroRodilla);
        rtorso = findViewById(R.id.recuadroTorso);

        recuadroOriginalDrawable = rboca.getDrawable();

        bsalir = findViewById(R.id.btnSalir);
        bsalir.setOnClickListener(this);

        View.OnTouchListener touchListener = new MyTouchListener();
        fboca.setOnTouchListener(touchListener);
        fbrazo.setOnTouchListener(touchListener);
        fcabello.setOnTouchListener(touchListener);
        fcejas.setOnTouchListener(touchListener);
        fcodo.setOnTouchListener(touchListener);
        fcuello.setOnTouchListener(touchListener);
        fmano.setOnTouchListener(touchListener);
        fnariz.setOnTouchListener(touchListener);
        fojos.setOnTouchListener(touchListener);
        foreja.setOnTouchListener(touchListener);
        fpie.setOnTouchListener(touchListener);
        fpierna.setOnTouchListener(touchListener);
        frodilla.setOnTouchListener(touchListener);
        ftorso.setOnTouchListener(touchListener);

        View.OnDragListener dragListener = new MyDragListener();
        rboca.setOnDragListener(dragListener);
        rbrazo.setOnDragListener(dragListener);
        rcabello.setOnDragListener(dragListener);
        rcejas.setOnDragListener(dragListener);
        rcodo.setOnDragListener(dragListener);
        rcuello.setOnDragListener(dragListener);
        rmano.setOnDragListener(dragListener);
        rnariz.setOnDragListener(dragListener);
        rojos.setOnDragListener(dragListener);
        roreja.setOnDragListener(dragListener);
        rpie.setOnDragListener(dragListener);
        rpierna.setOnDragListener(dragListener);
        rrodilla.setOnDragListener(dragListener);
        rtorso.setOnDragListener(dragListener);
    }



    private void mostrarVentanaEmergente() {
        mediaPlayer = MediaPlayer.create(this, R.raw.audiocuerpo);
        mediaPlayer.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Bienvenido al juego!");
        builder.setMessage("Arrastra las partes del cuerpo a los recuadros correctos para ganar puntos. ¡Mucha suerte!");
        builder.setIcon(R.drawable.bb_feliz);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                Toast.makeText(Cuerpo.this, "¡El juego ha comenzado!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, android.view.MotionEvent motionEvent) {
            if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView dropZone = (ImageView) v;
            ImageView draggedView = (ImageView) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    dropZone.clearColorFilter();
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    dropZone.setColorFilter(Color.parseColor("#80FFEB3B"), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    dropZone.clearColorFilter();
                    break;
                case DragEvent.ACTION_DROP:
                    dropZone.clearColorFilter();
                    if (isCorrectDrop(draggedView, dropZone)) {
                        ViewGroup owner = (ViewGroup) draggedView.getParent();
                        owner.removeView(draggedView);
                        dropZone.setImageDrawable(draggedView.getDrawable());
                        dropZone.setTag("filled");
                        puntos++;
                        Toast.makeText(Cuerpo.this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
                        mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.correcto);
                        mediaPlayer.start();
                        if (puntos == 14) {
                            mostrarAlertaVictoria();
                            mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.ff);
                            mediaPlayer.start();
                        }
                    } else {
                        draggedView.setVisibility(View.VISIBLE);
                        Toast.makeText(Cuerpo.this, "Incorrecto. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                        mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.incorrecto);
                        mediaPlayer.start();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        draggedView.setVisibility(View.VISIBLE);
                    }
                    dropZone.clearColorFilter();
                    break;
            }
            return true;
        }
    }

    private void mostrarAlertaVictoria() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Felicidades!");
        builder.setMessage("¡Has completado el juego! Ganaste 🏆");
        builder.setCancelable(false);

        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isCorrectDrop(View draggedView, View dropZone) {
        if (dropZone.getTag() != null && dropZone.getTag().equals("filled")) {
            return false;
        }

        String draggedIdName = getResources().getResourceEntryName(draggedView.getId());
        String dropZoneIdName = getResources().getResourceEntryName(dropZone.getId());
        String correctDropZoneId = correctMatches.get(draggedIdName);

        if (correctDropZoneId == null) {
            return false;
        }

        return correctDropZoneId.equals(dropZoneIdName);
    }
}
