package com.kidzbrain.juegos.multiplosdivisores;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView; // NUEVO: Importar ImageView
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.juegos.multiplosdivisores.GeneradorNivel;
import com.kidzbrain.juegos.pizzangulos.PizzaMusica;
import com.kidzbrain.login.R;
import java.util.List;

public class MultiplosDivisores extends AppCompatActivity {

    private FrameLayout contenedorGlobos;
    private TextView txtMision, txtPuntuacion, txtFeedback;
    private ImageView imgPayaso; // NUEVO: Declarar el ImageView del payaso

    private boolean esModoDificil;
    private int puntos = 0;

    // Variables del nivel actual
    private int numeroObjetivo;
    private boolean buscandoMultiplos;
    private Runnable creadorGlobos;
    private int globosGolpeados = 0;
    private List<Integer> opcionesActuales;
    private int indiceOpcion = 0;

    private Handler handlerGlobos = new Handler();
    private GeneradorNivel generador = new GeneradorNivel();

    // NUEVO: Un Runnable que se encarga exclusivamente de regresar al payaso a su estado normal
    private Runnable revertirPayaso = () -> imgPayaso.setImageResource(R.drawable.bb_px_payaso);
    private TextView txtContador;
    private View panelTutorial;
    private ImageButton btnBack;
    private int ultimaPosX = -1000; // La iniciamos lejos para que el primer globo no tenga problemas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplos_d);

        esModoDificil = getIntent().getBooleanExtra("esModoDificil", false);

        contenedorGlobos = findViewById(R.id.contenedorGlobos);
        txtMision = findViewById(R.id.txtMision);
        txtPuntuacion = findViewById(R.id.txtPuntuacion);
        txtFeedback = findViewById(R.id.txtFeedback);
        btnBack = findViewById(R.id.btnBack);

        // NUEVO: Enlazar el ImageView
        imgPayaso = findViewById(R.id.img_payaso);

        btnBack.setOnClickListener(v -> finish());

        // 2. Enlaza los nuevos elementos
        txtContador = findViewById(R.id.txtContador);
        panelTutorial = findViewById(R.id.panelTutorial);
        Button btnEntendido = findViewById(R.id.btnEntendido);

        btnEntendido.setOnClickListener(v -> {
            panelTutorial.setVisibility(View.GONE); // Ocultamos la tarjeta
            prepararNuevoNivel(); // Ahora sí, ¡arranca el juego!
            iniciarLluviaDeGlobos();
        });

        prepararNuevoNivel();
        iniciarLluviaDeGlobos();
    }


    private void prepararNuevoNivel() {
        if (esModoDificil) {
            numeroObjetivo = (int) (Math.random() * 8) + 6;
        } else {
            numeroObjetivo = (int) (Math.random() * 4) + 2;
        }

        buscandoMultiplos = Math.random() > 0.5;

        if (buscandoMultiplos) {
            txtMision.setText("¡Atrapa los múltiplos de " + numeroObjetivo + "!");
        } else {
            txtMision.setText("¡Atrapa los divisores de " + numeroObjetivo + "!");
        }

        opcionesActuales = generador.generarOpciones(numeroObjetivo, buscandoMultiplos);
        indiceOpcion = 0;
        globosGolpeados = 0; // RESET: Empezamos cuenta de golpes de cero

        if (txtContador != null) {
            // Inicializamos el texto con el total (ej: 5)
            txtContador.setText("Cambio de misión en: " + opcionesActuales.size() + " globos");
        }
    }

    private void iniciarLluviaDeGlobos() {
        creadorGlobos = new Runnable() {
            @Override
            public void run() {
                // Si llegamos al final de la lista de 5 números pero no los han golpeado,
                // reiniciamos el índice para que sigan saliendo los mismos números en bucle.
                if (indiceOpcion >= opcionesActuales.size()) {
                    indiceOpcion = 0;
                }

                int numeroParaGlobo = opcionesActuales.get(indiceOpcion);
                crearGloboGrafico(numeroParaGlobo);
                indiceOpcion++;

                int tiempoRespawn = esModoDificil ? 800 : 2500;
                handlerGlobos.postDelayed(this, tiempoRespawn);
            }
        };
        handlerGlobos.post(creadorGlobos);
    }

    private void crearGloboGrafico(int numeroGlobo) {
        Button globo = new Button(this);
        globo.setText(String.valueOf(numeroGlobo));
        globo.setBackgroundResource(R.drawable.px_globo);

        int tamañoGlobo = 250;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tamañoGlobo, tamañoGlobo);
        globo.setLayoutParams(params);

        globo.setTextSize(32f);

        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int altoPantalla = getResources().getDisplayMetrics().heightPixels;

        int posX;
        do {
            posX = (int) (Math.random() * (anchoPantalla - tamañoGlobo));
            // Repite el cálculo SI la distancia entre el nuevo globo y el anterior es menor al tamaño de un globo
        } while (Math.abs(posX - ultimaPosX) < tamañoGlobo);

        // Guardamos esta posición en la memoria para el SIGUIENTE globo
        ultimaPosX = posX;
        // -----------------------------------------

        globo.setX(posX);
        globo.setY(altoPantalla + 100f);

        // Agregar al layout
        contenedorGlobos.addView(globo);

        ObjectAnimator animacion = ObjectAnimator.ofFloat(globo, "translationY", altoPantalla + 100f, -300f);

        int velocidadVuelo = esModoDificil ? 4000 : 6500;
        animacion.setDuration(velocidadVuelo);
        animacion.start();

        globo.setOnClickListener(v -> {

            // 1. Reproducir el sonido de pinchar el globo (suena siempre)
            MediaPlayer mpGolpe = MediaPlayer.create(v.getContext(), R.raw.aud_golpe);
            mpGolpe.start();
            mpGolpe.setOnCompletionListener(MediaPlayer::release); // Libera la memoria al terminar

            boolean esCorrecto = false;

            // Revisamos las matemáticas
            if (buscandoMultiplos) {
                esCorrecto = (numeroGlobo % numeroObjetivo == 0);
            } else {
                esCorrecto = (numeroObjetivo % numeroGlobo == 0);
            }

            // Cancelamos cualquier reseteo de cara anterior
            handlerGlobos.removeCallbacks(revertirPayaso);

            if (esCorrecto) {
                puntos += 10;
                txtFeedback.setText("¡Bien!");
                txtFeedback.setTextColor(Color.GREEN);
                imgPayaso.setImageResource(R.drawable.bb_px_payaso_feliz);

                // --- SONIDOS DE ACIERTO ---
                MediaPlayer mpCorrecto = MediaPlayer.create(v.getContext(), R.raw.aud_correcto);
                MediaPlayer mpVozCorrecto = MediaPlayer.create(v.getContext(), R.raw.aud_voz_correcto);
                mpCorrecto.start();
                mpVozCorrecto.start();
                mpCorrecto.setOnCompletionListener(MediaPlayer::release);
                mpVozCorrecto.setOnCompletionListener(MediaPlayer::release);

            } else {
                puntos -= 5;
                txtFeedback.setText("¡Auch!");
                txtFeedback.setTextColor(Color.RED);
                imgPayaso.setImageResource(R.drawable.bb_px_payaso_triste);

                // --- SONIDOS DE ERROR ---
                MediaPlayer mpIncorrecto = MediaPlayer.create(v.getContext(), R.raw.aud_incorrecto);
                MediaPlayer mpVozIncorrecto = MediaPlayer.create(v.getContext(), R.raw.aud_voz_incorrecto);
                mpIncorrecto.start();
                mpVozIncorrecto.start();
                mpIncorrecto.setOnCompletionListener(MediaPlayer::release);
                mpVozIncorrecto.setOnCompletionListener(MediaPlayer::release);
            }

            globosGolpeados++;
            int restantes = opcionesActuales.size() - globosGolpeados;

            if (restantes > 0) {
                txtContador.setText("Cambio de misión en: " + restantes + " globos");
            } else {
                // Solo cuando golpean el último globo de la lista, cambiamos de misión
                prepararNuevoNivel();
            }

            // Actualizamos la pantalla
            txtPuntuacion.setText("Puntos: " + puntos);

            // Programamos que regrese a la normalidad en 1 segundo
            handlerGlobos.postDelayed(revertirPayaso, 1000);

            // Detenemos su vuelo y lo borramos de la pantalla
            animacion.cancel();
            contenedorGlobos.removeView(globo);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handlerGlobos != null) {
            if (creadorGlobos != null) handlerGlobos.removeCallbacks(creadorGlobos);
            handlerGlobos.removeCallbacks(revertirPayaso); // NUEVO: Limpiamos este también por seguridad
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handlerGlobos != null && creadorGlobos != null) {
            handlerGlobos.post(creadorGlobos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handlerGlobos != null) {
            handlerGlobos.removeCallbacksAndMessages(null);
        }
    }
}