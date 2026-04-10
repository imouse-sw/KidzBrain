package com.kidzbrain.juegos.chicles; // Ajusta la ruta de tu paquete

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

import com.kidzbrain.juegos.multiplosdivisores.MultiplosDMusica;
import com.kidzbrain.login.R;

public class Chicles extends AppCompatActivity {

    // Variables de la interfaz
    private TextView txtPuntuacion, txtPregunta;
    private FrameLayout contenedorChicles;
    private ImageView imgManivela, imgMonedaAnimacion;
    private List<PointF> posicionesOcupadas = new ArrayList<>();

    // Variables lógicas del juego
    private boolean esModoDificil;
    private int puntos = 0;
    private android.content.SharedPreferences prefs;
    private boolean animacionEnCurso = false;
    private ImageButton btnBack;
    private androidx.appcompat.widget.AppCompatButton btnOp1, btnOp2, btnOp3;
    ImageView imgBrainBotVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicles);

        esModoDificil = getIntent().getBooleanExtra("esModoDificil", false);

        txtPuntuacion = findViewById(R.id.txtPuntuacion);
        txtPregunta = findViewById(R.id.txtPregunta);
        contenedorChicles = findViewById(R.id.contenedorChicles);
        imgManivela = findViewById(R.id.imgManivela);
        imgMonedaAnimacion = findViewById(R.id.imgMonedaAnimacion);
        imgBrainBotVendor = findViewById(R.id.imgBrainBotVendor);


        // Enlazamos los botones
        btnOp1 = findViewById(R.id.btnOpcion1);
        btnOp2 = findViewById(R.id.btnOpcion2);
        btnOp3 = findViewById(R.id.btnOpcion3);

        // --- MANEJO DE SHAREDPREFERENCES ---
        prefs = getSharedPreferences("KidzBrainPrefs", MODE_PRIVATE);
        // Cargamos los puntos guardados (si no hay, empieza en 0)
        puntos = prefs.getInt("puntosChicles", 0);
        txtPuntuacion.setText("Puntos: " + puntos);

        prepararNuevaPregunta();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ObjectAnimator idleBrainBot = ObjectAnimator.ofFloat(imgBrainBotVendor, "translationY", 0f, -15f);
        idleBrainBot.setDuration(1200); // Tarda 1.2 segundos en subir
        idleBrainBot.setRepeatMode(ObjectAnimator.REVERSE); // Baja suavemente
        idleBrainBot.setRepeatCount(ObjectAnimator.INFINITE); // Se repite por siempre
        idleBrainBot.start();

        hacerBotonApachurrable(btnOp1);
        hacerBotonApachurrable(btnOp2);
        hacerBotonApachurrable(btnOp3);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hacerBotonApachurrable(View boton) {
        boton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    // Se encoge al 90% de su tamaño súper rápido
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    // Regresa a su tamaño original al soltarlo con un pequeño rebote
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150)
                            .setInterpolator(new android.view.animation.OvershootInterpolator()).start();
                    break;
            }
            return false; // IMPORTANTE: Devolver false para que el OnClickListener normal siga funcionando
        });
    }



    private void entregarPremio(int colorGanador) {
        // 1. Preparamos la Moneda (La hacemos visible primero)
        imgMonedaAnimacion.setVisibility(View.VISIBLE);
        imgMonedaAnimacion.setTranslationY(0f); // Aseguramos que empiece en su lugar original

        // Sonido de moneda
        MediaPlayer mpMoneda = MediaPlayer.create(this, R.raw.aud_moneda);
        mpMoneda.start();
        mpMoneda.setOnCompletionListener(MediaPlayer::release);

        // Animación moneda: Baja 80 píxeles simulando entrar a la ranura
        ObjectAnimator animMoneda = ObjectAnimator.ofFloat(imgMonedaAnimacion, "translationY", 0f, 80f);
        animMoneda.setDuration(400);

        // 2. Preparamos la Manivela
        // Animación manivela: Gira 360 grados
        ObjectAnimator animManivela = ObjectAnimator.ofFloat(imgManivela, "rotation", 0f, 360f);
        animManivela.setDuration(600);

        // 3. Preparamos el Chicle Premio
        ImageView chiclePremio = new ImageView(this);
        chiclePremio.setImageResource(R.drawable.px_chicle);

        // ¡Magia! Pintamos tu chicle blanco del color ganador
        chiclePremio.setColorFilter(colorGanador, PorterDuff.Mode.MULTIPLY);

        // Tamaño del chicle (unos 80x80 píxeles para que se vea bien al salir)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(90, 90);
        // Lo centramos horizontalmente en el contenedor
        params.gravity = android.view.Gravity.CENTER_HORIZONTAL;
        chiclePremio.setLayoutParams(params);

        // Lo ponemos invisible al inicio para que no aparezca de golpe antes de tiempo
        chiclePremio.setVisibility(View.INVISIBLE);
        contenedorChicles.addView(chiclePremio);

        // Animación chicle: Cae desde arriba (-100f) hasta el fondo del contenedor (200f)
        ObjectAnimator animChicle = ObjectAnimator.ofFloat(chiclePremio, "translationY", -100f, 500f);
        animChicle.setDuration(1000);
        animChicle.setInterpolator(new BounceInterpolator()); // ¡El rebote físico!

        // 4. ¡Ensamblamos la secuencia de la película!
        AnimatorSet pelicula = new AnimatorSet();

        // La moneda va primero, luego la manivela
        pelicula.playSequentially(animMoneda, animManivela);

        // Listeners para sincronizar efectos visuales y de sonido en momentos exactos
        animMoneda.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Cuando la moneda termina de bajar, la ocultamos
                imgMonedaAnimacion.setVisibility(View.INVISIBLE);
            }
        });

        animManivela.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Al empezar a girar la manivela, suena el engranaje
                MediaPlayer mpManivela = MediaPlayer.create(Chicles.this, R.raw.aud_manivela);
                mpManivela.start();
                mpManivela.setOnCompletionListener(MediaPlayer::release);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Al terminar de girar, aparece el chicle, suena y empieza a caer
                chiclePremio.setVisibility(View.VISIBLE);
                animChicle.start();

                MediaPlayer mpCae = MediaPlayer.create(Chicles.this, R.raw.aud_chicle_cae);
                mpCae.start();
                mpCae.setOnCompletionListener(MediaPlayer::release);
            }
        });

        // ¡Luces, cámara, acción!
        pelicula.start();
    }

    private void prepararNuevaPregunta() {
        // Quitamos el candado para que puedan volver a presionar botones
        animacionEnCurso = false;

        imgBrainBotVendor.setImageResource(R.drawable.px_bb_vendedor);

        contenedorChicles.removeAllViews();
        posicionesOcupadas.clear();

        int chiclesRojos, chiclesAzules, chiclesVerdes;

        // Bucle mágico para evitar empates y que siempre haya un ganador claro
        do {
            chiclesRojos = (int) (Math.random() * 5) + 2;
            chiclesAzules = (int) (Math.random() * 5) + 2;
            chiclesVerdes = (int) (Math.random() * 5) + 2;
        } while (chiclesRojos == chiclesAzules || chiclesRojos == chiclesVerdes || chiclesAzules == chiclesVerdes);

        // Llenamos la esfera visualmente
        aventarChiclesAlCristal(chiclesRojos, android.graphics.Color.RED);
        aventarChiclesAlCristal(chiclesAzules, android.graphics.Color.BLUE);
        aventarChiclesAlCristal(chiclesVerdes, android.graphics.Color.GREEN);

        if (esModoDificil) {
            configurarModoDificil(chiclesRojos, chiclesAzules, chiclesVerdes);
        } else {
            configurarModoFacil(chiclesRojos, chiclesAzules, chiclesVerdes);
        }
    }

    private void configurarModoFacil(int rojos, int azules, int verdes) {
        txtPregunta.setText("¿Qué color es más probable que salga?");

        // Encontramos cuál es el mayor matemáticamente
        int maximo = Math.max(rojos, Math.max(azules, verdes));

        String textoCorrecto;
        int colorPremio;

        if (maximo == rojos) {
            textoCorrecto = "Rojo";
            colorPremio = android.graphics.Color.RED;
        } else if (maximo == azules) {
            textoCorrecto = "Azul";
            colorPremio = android.graphics.Color.BLUE;
        } else {
            textoCorrecto = "Verde";
            colorPremio = android.graphics.Color.GREEN;
        }

        // Asignamos textos fijos a los botones
        btnOp1.setText("Rojo");
        btnOp2.setText("Azul");
        btnOp3.setText("Verde");

        configurarClicsDeBotones(textoCorrecto, colorPremio);
    }

    private void configurarModoDificil(int rojos, int azules, int verdes) {
        int totalChicles = rojos + azules + verdes;

        // Elegimos un color al azar para preguntar por él (0=Rojo, 1=Azul, 2=Verde)
        int colorObjetivo = (int) (Math.random() * 3);

        String nombreColorObjetivo;
        String fraccionCorrecta;
        int colorPremio;

        if (colorObjetivo == 0) {
            nombreColorObjetivo = "rojo";
            fraccionCorrecta = rojos + "/" + totalChicles;
            colorPremio = android.graphics.Color.RED;
        } else if (colorObjetivo == 1) {
            nombreColorObjetivo = "azul";
            fraccionCorrecta = azules + "/" + totalChicles;
            colorPremio = android.graphics.Color.BLUE;
        } else {
            nombreColorObjetivo = "verde";
            fraccionCorrecta = verdes + "/" + totalChicles;
            colorPremio = android.graphics.Color.GREEN;
        }

        txtPregunta.setText("¿Probabilidad de sacar un chicle " + nombreColorObjetivo + "?");

        // En este modo, usamos las fracciones de los 3 colores como opciones (una será la correcta)
        btnOp1.setText(rojos + "/" + totalChicles);
        btnOp2.setText(azules + "/" + totalChicles);
        btnOp3.setText(verdes + "/" + totalChicles);

        configurarClicsDeBotones(fraccionCorrecta, colorPremio);
    }

    private void configurarClicsDeBotones(String respuestaCorrecta, int colorParaPremio) {
        View.OnClickListener listenerGlobal = v -> {
            if (animacionEnCurso) return; // Si ya están dando el premio, ignoramos más clics

            androidx.appcompat.widget.AppCompatButton btnPresionado = (androidx.appcompat.widget.AppCompatButton) v;
            String textoBoton = btnPresionado.getText().toString();

            animacionEnCurso = true; // Bloqueamos la pantalla

            if (textoBoton.equals(respuestaCorrecta)) {
                // ¡Acierto!
                puntos += 15;
                txtPuntuacion.setText("Puntos: " + puntos);
                guardarPuntos();

                // Disparamos la película que armamos en el paso anterior
                entregarPremio(colorParaPremio);

                MediaPlayer.create(this, R.raw.aud_correcto).start();
                MediaPlayer.create(this, R.raw.aud_voz_correcto).start();
                imgBrainBotVendor.setImageResource(R.drawable.px_bb_vendedor_feliz);

                // Preparamos la siguiente pregunta después de 2.5 segundos (lo que tarda la animación)
                new android.os.Handler().postDelayed(this::prepararNuevaPregunta, 2500);

            } else {
                // ¡Fallo!
                puntos -= 5;
                if (puntos < 0) puntos = 0; // Evitamos puntos negativos
                txtPuntuacion.setText("Puntos: " + puntos);
                guardarPuntos();

                // --- EFECTO TERREMOTO ---
                // Movemos el contenedor de la máquina de izquierda a derecha rápidamente
                View ensambleMaquina = findViewById(R.id.ensambleMaquina); // Asegúrate de tener este ID en tu XML
                ObjectAnimator shake = ObjectAnimator.ofFloat(ensambleMaquina, "translationX", 0f, 20f, -20f, 15f, -15f, 6f, -6f, 0f);
                shake.setDuration(400); // Todo el temblor pasa en menos de medio segundo
                shake.start();

                // Aquí podrías reproducir el audio_incorrecto
                MediaPlayer.create(this, R.raw.aud_incorrecto).start();
                MediaPlayer.create(this, R.raw.aud_voz_incorrecto).start();
                imgBrainBotVendor.setImageResource(R.drawable.px_bb_vendedor_triste);

                // Cambiamos rápido de pregunta para que no se aburran (1 segundo)
                new android.os.Handler().postDelayed(this::prepararNuevaPregunta, 2500);
            }
        };

        btnOp1.setOnClickListener(listenerGlobal);
        btnOp2.setOnClickListener(listenerGlobal);
        btnOp3.setOnClickListener(listenerGlobal);
    }

    private void guardarPuntos() {
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("puntosChicles", puntos);
        editor.apply();
    }

    private void aventarChiclesAlCristal(int cantidad, int colorChicle) {
        float densidad = getResources().getDisplayMetrics().density;
        int anchoCristal = (int) (150 * densidad);
        int altoCristal = (int) (130 * densidad);
        int tamanoChicle = (int) (35 * densidad);  // Los hice un pelín más pequeños para que quepan mejor

        for (int i = 0; i < cantidad; i++) {
            ImageView chicleEstatico = new ImageView(this);
            chicleEstatico.setImageResource(R.drawable.px_chicle);
            chicleEstatico.setColorFilter(colorChicle, android.graphics.PorterDuff.Mode.MULTIPLY);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tamanoChicle, tamanoChicle);
            chicleEstatico.setLayoutParams(params);

            float posX = 0;
            float posY = 0;
            boolean posicionValida = false;
            int intentos = 0;

            // Intentamos buscar un lugar libre (máximo 50 intentos para que no se trabe el juego)
            while (!posicionValida && intentos < 50) {
                posX = (float) (Math.random() * (anchoCristal - tamanoChicle));
                posY = (float) (Math.random() * (altoCristal - tamanoChicle));

                posicionValida = true; // Asumimos que es válida hasta que se demuestre lo contrario

                // Revisamos contra todos los chicles que ya pusimos
                for (PointF pos : posicionesOcupadas) {
                    // Usamos el Teorema de Pitágoras para medir la distancia real entre chicles
                    double distancia = Math.hypot((posX - pos.x), (posY - pos.y));

                    // Si están más cerca que el tamaño de un chicle (multiplicado por 0.8 para
                    // dejar que se "toquen" o empalmen un poquitito visualmente), la rechazamos.
                    if (distancia < (tamanoChicle * 0.8)) {
                        posicionValida = false;
                        break; // Dejamos de buscar y volvemos a tirar los dados
                    }
                }
                intentos++;
            }

            // Guardamos la posición ganadora en la memoria para el siguiente chicle
            posicionesOcupadas.add(new PointF(posX, posY));

            chicleEstatico.setX(posX);
            chicleEstatico.setY(posY);
            chicleEstatico.setRotation((float) (Math.random() * 360));

            contenedorChicles.addView(chicleEstatico);
        }
    }
}