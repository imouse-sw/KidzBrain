package com.kidzbrain.juegos.tresR;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TresR extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout layoutJuegoReciclaje;
    private ImageView contenedorOrganica, contenedorInorganicaRec, contenedorInorganicaNoRec;

    private Handler handler;
    private Runnable runnableSpawn;
    private int puntos = 0;
    private int errores = 0;
    private final int MAX_ERRORES = 3;
    private Random random;

    private TextView tvPuntuacion;
    private TextView tvErrores;

    ImageButton bsalir;
    private MediaPlayer mediaPlayer;

    private int[] basurasIDs = {
            R.drawable.manzana, R.drawable.platano, R.drawable.hojas, R.drawable.hueso, // Orgánico
            R.drawable.botellaa, R.drawable.botellav, R.drawable.lata, R.drawable.lata, // Inorgánico Reciclable
            R.drawable.cafe, R.drawable.foco, R.drawable.panal, R.drawable.papitas // Inorgánico No Reciclable
    };

    private Map<Integer, Integer> contenedorCorrecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tresr);

        bsalir = findViewById(R.id.salir);
        bsalir.setOnClickListener(this);

        layoutJuegoReciclaje = findViewById(R.id.layoutJuegoReciclaje);
        contenedorOrganica = findViewById(R.id.ContenedorOrganica);
        contenedorInorganicaRec = findViewById(R.id.ContenedorInorganicaRec);
        contenedorInorganicaNoRec = findViewById(R.id.ContenedorInorganicaNoRec);

        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        tvErrores = findViewById(R.id.tvErrores);

        actualizarPuntuacionUI();
        actualizarErroresUI();

        random = new Random();

        contenedorCorrecto = new HashMap<>();
        contenedorCorrecto.put(R.drawable.manzana, R.id.ContenedorOrganica);
        contenedorCorrecto.put(R.drawable.platano, R.id.ContenedorOrganica);
        contenedorCorrecto.put(R.drawable.hojas, R.id.ContenedorOrganica);
        contenedorCorrecto.put(R.drawable.hueso, R.id.ContenedorOrganica);

        contenedorCorrecto.put(R.drawable.botellaa, R.id.ContenedorInorganicaRec);
        contenedorCorrecto.put(R.drawable.botellav, R.id.ContenedorInorganicaRec);
        contenedorCorrecto.put(R.drawable.papel, R.id.ContenedorInorganicaRec);
        contenedorCorrecto.put(R.drawable.lata, R.id.ContenedorInorganicaRec);

        contenedorCorrecto.put(R.drawable.cafe, R.id.ContenedorInorganicaNoRec);
        contenedorCorrecto.put(R.drawable.foco, R.id.ContenedorInorganicaNoRec);
        contenedorCorrecto.put(R.drawable.panal, R.id.ContenedorInorganicaNoRec);
        contenedorCorrecto.put(R.drawable.papitas, R.id.ContenedorInorganicaNoRec);

        View.OnDragListener dragListener = new MyDragListener();
        contenedorOrganica.setOnDragListener(dragListener);
        contenedorInorganicaRec.setOnDragListener(dragListener);
        contenedorInorganicaNoRec.setOnDragListener(dragListener);

        mostrarVentanaEmergente();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnableSpawn);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null && errores < MAX_ERRORES) {
            handler.post(runnableSpawn);
        }
    }

    private void playSound(int soundResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        });
    }

    private void iniciarJuego() {
        handler = new Handler();
        runnableSpawn = new Runnable() {
            @Override
            public void run() {
                crearImagenCaida();
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnableSpawn);
    }

    private void mostrarVentanaEmergente() {
        playSound(R.raw.audioreciclaje);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Hola, bienvenido al juego!");
        builder.setMessage("Arrastra los distintos tipos de basura a sus contenedores correspondientes para ganar puntos," +
                "¡pero cuidado, no puedes equivocarte mas de 3 veces o perderás,! ¡Mucha suerte!");

        builder.setIcon(R.drawable.brainbotf);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                Toast.makeText(TresR.this, "¡El juego ha comenzado!", Toast.LENGTH_SHORT).show();
                iniciarJuego();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void crearImagenCaida() {
        ImageView basuraView = new ImageView(this);

        int basuraID = basurasIDs[random.nextInt(basurasIDs.length)];
        basuraView.setImageResource(basuraID);
        basuraView.setTag(basuraID);

        int tamanoBasura = (int) (200 * getResources().getDisplayMetrics().density);
        int anchoPantalla = layoutJuegoReciclaje.getWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tamanoBasura, tamanoBasura);
        params.leftMargin = random.nextInt(anchoPantalla - tamanoBasura);
        params.topMargin = 0;

        layoutJuegoReciclaje.addView(basuraView, params);

        basuraView.setOnTouchListener(new MyTouchListener());

        ObjectAnimator caidaAnimator = ObjectAnimator.ofFloat(basuraView, "translationY", 0f, layoutJuegoReciclaje.getHeight() + 100);
        caidaAnimator.setDuration(5000);
        caidaAnimator.setInterpolator(new AccelerateInterpolator());

        caidaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (basuraView.getParent() != null) {
                    layoutJuegoReciclaje.removeView(basuraView);
                    manejarError();
                }
            }
        });
        caidaAnimator.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.salir) {
            finish();
        }
    }

    private class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, android.view.MotionEvent motionEvent) {
            if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                ObjectAnimator animator = (ObjectAnimator) view.getTag(R.id.animator_tag);
                if (animator != null) {
                    animator.cancel();
                }

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
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    dropZone.setColorFilter(Color.parseColor("#80FFEB3B"), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    dropZone.clearColorFilter();
                    break;
                case DragEvent.ACTION_DROP:
                    dropZone.clearColorFilter();
                    int draggedImageID = (int) draggedView.getTag();
                    int dropZoneID = dropZone.getId();

                    if (isCorrectDrop(draggedImageID, dropZoneID)) {
                        ViewGroup owner = (ViewGroup) draggedView.getParent();
                        if (owner != null) {
                            owner.removeView(draggedView);
                        }
                        puntos++;
                        actualizarPuntuacionUI();
                        Toast.makeText(TresR.this, "¡Correcto!", Toast.LENGTH_SHORT).show();
                        playSound(R.raw.correcto);

                        if (puntos == 20) {
                            mostrarAlertaVictoria();
                            playSound(R.raw.ff);
                        }
                    } else {
                        draggedView.setVisibility(View.VISIBLE);
                        manejarError();
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

    private boolean isCorrectDrop(int draggedImageID, int dropZoneID) {
        Integer correctDropZoneID = contenedorCorrecto.get(draggedImageID);
        return correctDropZoneID != null && correctDropZoneID == dropZoneID;
    }

    private void actualizarPuntuacionUI() {
        tvPuntuacion.setText("Puntos: " + puntos);
    }

    private void actualizarErroresUI() {
        tvErrores.setText("Errores: " + errores + "/" + MAX_ERRORES);
    }

    private void manejarError() {
        errores++;
        actualizarErroresUI();
        if (errores >= MAX_ERRORES) {
            finalizarJuego();
        } else {
            Toast.makeText(TresR.this, "Incorrecto. Te quedan " + (MAX_ERRORES - errores) + " errores.", Toast.LENGTH_SHORT).show();
            playSound(R.raw.incorrecto);
        }
    }

    private void finalizarJuego() {
        handler.removeCallbacks(runnableSpawn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Juego Terminado");
        builder.setMessage("Tu puntuación final es: " + puntos + ".");
        builder.setCancelable(false);

        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reiniciarJuego();
            }
        });

        builder.show();
    }

    private void reiniciarJuego() {
        puntos = 0;
        errores = 0;
        actualizarPuntuacionUI();
        actualizarErroresUI();

        for (int i = layoutJuegoReciclaje.getChildCount() - 1; i >= 0; i--) {
            View child = layoutJuegoReciclaje.getChildAt(i);
            if (child instanceof ImageView && child.getTag() != null) {
                layoutJuegoReciclaje.removeViewAt(i);
            }
        }
        iniciarJuego();
    }

    private void mostrarAlertaVictoria() {
        handler.removeCallbacks(runnableSpawn);
        playSound(R.raw.ff);

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

        builder.setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reiniciarJuego();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
