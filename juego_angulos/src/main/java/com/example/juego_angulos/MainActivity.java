package com.example.juego_angulos;

import android.annotation.SuppressLint;
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

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    PizzaView pizzaView;
    SeekBar barrita;
    Button cortar;
    TextView resultado, pedido, puntuacionTxt, generando, anguloTxt;
    ImageView chef, cliente;
    Handler handler;
    SoundPool soundPool;

    int anguloPedido, anguloActual, puntuacion = 0;
    int correctoIdFx, perfectoIdFx, perfectoIdVoz, incorrectoIdFx, correctoIdVoz, incorrectoIdVoz, campanaId, cocinandoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_angulos);

        handler = new Handler(Looper.getMainLooper());

        pizzaView = findViewById(R.id.pizza_view);
        barrita = findViewById(R.id.sbAngulo);
        cortar = findViewById(R.id.bCortar);
        resultado = findViewById(R.id.txtResultado);
        pedido = findViewById(R.id.txtAngulo);
        chef = findViewById(R.id.img_chef);
        cliente = findViewById(R.id.img_cliente);
        puntuacionTxt = findViewById(R.id.puntuacion);
        generando = findViewById(R.id.txtGenerando);
        anguloTxt = findViewById(R.id.txtAnguloActual);

        barrita.setOnSeekBarChangeListener(this);
        cortar.setOnClickListener(this);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        soundPool = builder.build();

        pizzaView.setImagenPizza(R.drawable.px_pizza);

        Bundle extras = getIntent().getExtras();
        String pasadito = extras.getString("Dificultad");
        if(pasadito.equals("Práctica")) {
            anguloTxt.setVisibility(View.VISIBLE);
        }
        else {
            anguloTxt.setVisibility(View.INVISIBLE);
        }

        correctoIdFx = soundPool.load(this, R.raw.yupi, 1);
        incorrectoIdFx = soundPool.load(this, R.raw.bowomp, 1);
        correctoIdVoz = soundPool.load(this, R.raw.aud_muybien, 1);
        incorrectoIdVoz = soundPool.load(this, R.raw.aud_muymal, 1);
        campanaId = soundPool.load(this, R.raw.aud_campana, 1);
        cocinandoId = soundPool.load(this, R.raw.aud_cookin, 1);
        perfectoIdFx = soundPool.load(this, R.raw.aud_perfecto, 1);
        perfectoIdVoz = soundPool.load(this, R.raw.aud_perfecto_voz, 1);

        nuevoPedido();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        pizzaView.setAngulo(progress);
        anguloTxt.setText("Ángulo: "+progress+"°");
        anguloActual = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bCortar) {
            int diferencia = anguloActual - anguloPedido;

            if(diferencia == 0) {
                resultado.setText("¡Un corte exacto! Perfecto. +5 puntos.");

                chef.setImageResource(R.drawable.px_chefbot);
                cliente.setImageResource(R.drawable.px_cliente_habla);

                actualizarPuntuacion(true, 5);
                soundPool.play(perfectoIdFx, 1, 1, 0, 0, 1);
                soundPool.play(perfectoIdVoz, 1, 1, 0, 0, 1);
            }
            else if(diferencia > 0 && diferencia < 10) {
                resultado.setText("¡Muy bien! Solo te pasaste por "+diferencia+" grados." +
                        "\nTu ángulo: "+anguloActual+"°. +1 punto.");

                chef.setImageResource(R.drawable.px_chefbot);
                cliente.setImageResource(R.drawable.px_cliente_habla);

                actualizarPuntuacion(true, 1);
                soundPool.play(correctoIdFx, 1, 1, 0, 0, 1);
                soundPool.play(correctoIdVoz, 1, 1, 0, 0, 1);
            }
            else if(diferencia < 0 && diferencia > -10){
                resultado.setText("¡Muy bien! Solo faltaron "+(anguloPedido-anguloActual)+" grados." +
                        "\nTu ángulo: "+anguloActual+"°. +1 punto.");

                chef.setImageResource(R.drawable.px_chefbot);
                cliente.setImageResource(R.drawable.px_cliente_habla);

                actualizarPuntuacion(true, 1);
                soundPool.play(correctoIdFx, 1, 1, 0, 0, 1);
                soundPool.play(correctoIdVoz, 1, 1, 0, 0, 1);
            }
            else if(diferencia >= 10) {
                resultado.setText("Te pasaste por "+Math.abs(diferencia)+" grados." +
                        "\nTu ángulo: "+anguloActual+"°");

                chef.setImageResource(R.drawable.px_chefbot_triste);
                cliente.setImageResource(R.drawable.px_cliente_triste);

                actualizarPuntuacion(false, 0);
                soundPool.play(incorrectoIdFx, 1, 1, 0, 0, 1);
                soundPool.play(incorrectoIdVoz, 1, 1, 0, 0, 1);
            }
            else if(diferencia <= -10) {
                resultado.setText("Te faltaron "+(anguloPedido-anguloActual)+" grados." +
                        "\nTu ángulo: "+anguloActual+"°");

                chef.setImageResource(R.drawable.px_chefbot_triste);
                cliente.setImageResource(R.drawable.px_cliente_triste);

                actualizarPuntuacion(false, 0);
                soundPool.play(incorrectoIdFx, 1, 1, 0, 0, 1);
                soundPool.play(incorrectoIdVoz, 1, 1, 0, 0, 1);
            }

            generando.setVisibility(View.VISIBLE);
            pizzaView.setImagenPizza(R.drawable.px_pizza_previo);
            pizzaView.setMostrarLineas(false);
            soundPool.play(cocinandoId, 0.7f, 0.7f, 0, 0, 1);

            handler.postDelayed(this::nuevoPedido, 4000);
        }
    }

    private void actualizarPuntuacion(boolean salioBien, int puntos) {
        if(salioBien) {
            puntuacion += puntos;
            puntuacionTxt.setText("Puntuación: "+puntuacion);
        }
    }

    private void nuevoPedido() {
        anguloPedido = (int)(Math.random()*359) + 1;

        pedido.setText("El cliente ha pedido "+anguloPedido+"° de pizza.");
        resultado.setText("Esperando el corte...");
        generando.setVisibility(View.INVISIBLE);
        chef.setImageResource(R.drawable.px_chefbot_normal);
        cliente.setImageResource(R.drawable.px_cliente);
        pizzaView.setImagenPizza(R.drawable.px_pizza);
        pizzaView.setMostrarLineas(true);

        soundPool.play(campanaId, 1, 1, 0, 0, 1);

        barrita.setProgress(0);
        pizzaView.setAngulo(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Musica.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Musica.reproduce(this);
    }
}