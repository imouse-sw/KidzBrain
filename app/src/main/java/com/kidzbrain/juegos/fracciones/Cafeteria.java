package com.kidzbrain.juegos.fracciones;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.juegos.pizzangulos.PizzaMusica;
import com.kidzbrain.login.R; // Asegúrate de que este import sea correcto

public class Cafeteria extends AppCompatActivity {
    TextView txtFraccion, txtMensajeChef, txtInstruccion;
    ImageView imgChef, imgCliente;
    GridLayout glBandeja;
    Button bServir;

    // Variables existentes...
    int numeradorObj, denominadorObj;

    int cantidadHuecosBandeja;
    int objetivoHuecosLlenos;
    int porcionesServidas = 0;
    int comidaActualId;
    int[] menuImagenes = {
            R.drawable.res_pizza,
            R.drawable.res_fresas_con_crema,
            R.drawable.res_pure,
            R.drawable.res_pure,
            R.drawable.res_arroz_con_leche
    };

    String[] menuNombres = {
            "pizza",
            "fresas",
            "puré",
            "papas",
            "arroz"
    };

    SoundPool soundPool;
    int sfxSplat, sfxCorrecto, sfxError, sfxCampana, sfxVozBien, sfxVozMal, vozInicio;
    boolean sonidosListos = false;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cafeteria);

        txtFraccion = findViewById(R.id.txtFraccion);
        txtMensajeChef = findViewById(R.id.txtMensajeChef);
        txtInstruccion = findViewById(R.id.txtInstruccion);
        imgChef = findViewById(R.id.img_chef);
        imgCliente = findViewById(R.id.img_cliente);
        glBandeja = findViewById(R.id.glBandeja);
        bServir = findViewById(R.id.bServir);

        CafeteriaMusica.reproduce(this);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        soundPool = builder.build();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        sfxSplat = soundPool.load(this, R.raw.sfx_splat, 1);
        vozInicio = soundPool.load(this, R.raw.aud_cafe_voz_inicio, 1);
        sfxCorrecto = soundPool.load(this, R.raw.yupi, 1);
        sfxError = soundPool.load(this, R.raw.bowomp, 1);
        sfxCampana = soundPool.load(this, R.raw.aud_campana, 1);
        sfxVozBien = soundPool.load(this, R.raw.aud_muybien, 1);
        sfxVozMal = soundPool.load(this, R.raw.aud_muymal, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                if (sampleId == vozInicio) {
                    soundPool.play(vozInicio, 1, 1, 0, 0, 1);
                }
            }
        });

        bServir.setOnClickListener(v -> verificarPedido());

        nuevoCliente();
    }

    private void nuevoCliente() {
        porcionesServidas = 0;
        bServir.setEnabled(true);
        imgChef.setImageResource(R.drawable.px_chefbot_normal);
        imgCliente.setImageResource(R.drawable.px_cliente);
        txtMensajeChef.setText("¡Nuevo pedido!");

        int indiceRandom = (int)(Math.random() * menuImagenes.length);
        comidaActualId = menuImagenes[indiceRandom];
        txtInstruccion.setText("Quiero " + menuNombres[indiceRandom] + ":");

        int denomVisual = (int)(Math.random() * 5 ) + 2;
        int numVisual = (int)(Math.random() * (denomVisual - 1)) + 1;

        txtFraccion.setText(numVisual + "/" + denomVisual);

        int multiplicador = 1;
        int hayEquivalencia = (int)(Math.random() * 2) + 1;
        if (hayEquivalencia == 1) {
            multiplicador = (int)(Math.random() * 3) + 2;
        }

        cantidadHuecosBandeja = denomVisual * multiplicador;
        objetivoHuecosLlenos = numVisual * multiplicador;

        generarBandeja(cantidadHuecosBandeja);
    }

    private void generarBandeja(int cantidadHuecos) {
        glBandeja.removeAllViews();

        for (int i = 0; i < cantidadHuecos; i++) {
            ImageView casilla = new ImageView(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = 160;   // Ancho en pixeles
            params.height = 160;  // Alto en pixeles

            params.setMargins(10, 10, 10, 10);

            casilla.setLayoutParams(params);


            casilla.setImageResource(R.drawable.px_casillabandeja_vacia);
            casilla.setTag("VACIO");
            casilla.setScaleType(ImageView.ScaleType.FIT_CENTER);

            casilla.setOnClickListener(v -> {
                ImageView img = (ImageView) v;
                String estado = (String) img.getTag();

                if (estado.equals("VACIO")) {
                    img.setImageResource(comidaActualId);
                    img.setTag("LLENO");
                    porcionesServidas++;
                    if (sonidosListos) soundPool.play(sfxSplat, 1, 1, 0, 0, 1);
                }
                else {
                    img.setImageResource(R.drawable.px_casillabandeja_vacia);
                    img.setTag("VACIO");
                    porcionesServidas--;
                }
            });

            // Agregamos al Grid
            glBandeja.addView(casilla);
        }
    }

    private void verificarPedido() {
        if (porcionesServidas == objetivoHuecosLlenos) {
            txtMensajeChef.setText("¡Excelente servicio!");

            imgChef.setImageResource(R.drawable.px_chefbot); // El feliz
            imgCliente.setImageResource(R.drawable.px_cliente_habla); // El feliz

            soundPool.play(sfxCorrecto, 1, 1, 0, 0, 1);
            soundPool.play(sfxVozBien, 1, 1, 0, 0, 1);
            bServir.setEnabled(false);

            new Handler(Looper.getMainLooper()).postDelayed(this::nuevoCliente, 2500);

        } else {
            txtMensajeChef.setText("Ups... pedían " + objetivoHuecosLlenos + "/" + cantidadHuecosBandeja + " porciones.");

            imgChef.setImageResource(R.drawable.px_chefbot_triste);
            imgCliente.setImageResource(R.drawable.px_cliente_triste);

            soundPool.play(sfxError, 1, 1, 0, 0, 1);
            soundPool.play(sfxVozMal, 1, 1, 0, 0, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (isFinishing()) {
            CafeteriaMusica.libera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CafeteriaMusica.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CafeteriaMusica.reproduce(this);
    }
}