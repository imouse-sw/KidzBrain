package com.example.juego_cambio;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int precio, pago, totalEntregado;
    TextView precioView, pagoView, totalView, cambioView;
    Button confirmar, volver, deshacer;
    ImageButton m1, m2, m5, m10, b20, b50, b100, b200;
    SoundPool soundPool;
    int correctoIdFx, incorrectoIdFx, correctoIdVoz, incorrectoIdVoz;

    private ArrayList<Integer> historialDeCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_cambio);

        precioView = findViewById(R.id.precio);
        pagoView = findViewById(R.id.pago);
        totalView = findViewById(R.id.total);
        cambioView = findViewById(R.id.cambio);
        m1 = findViewById(R.id.moneda1);
        m2 = findViewById(R.id.moneda2);
        m5 = findViewById(R.id.moneda5);
        m10 = findViewById(R.id.moneda10);
        b20 = findViewById(R.id.billete20);
        b50 = findViewById(R.id.billete50);
        b100 = findViewById(R.id.billete100);
        b200 = findViewById(R.id.billete200);
        confirmar = findViewById(R.id.confirmar);
        volver = findViewById(R.id.volver);
        deshacer = findViewById(R.id.bDeshacer);

        m1.setOnClickListener(this);
        m2.setOnClickListener(this);
        m5.setOnClickListener(this);
        m10.setOnClickListener(this);
        b20.setOnClickListener(this);
        b50.setOnClickListener(this);
        b100.setOnClickListener(this);
        b200.setOnClickListener(this);
        confirmar.setOnClickListener(this);
        volver.setOnClickListener(this);
        deshacer.setOnClickListener(this);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        soundPool = builder.build();

        correctoIdFx = soundPool.load(this, R.raw.yupi, 1);
        incorrectoIdFx = soundPool.load(this, R.raw.bowomp, 1);
        correctoIdVoz = soundPool.load(this, R.raw.correcto2, 2);
        incorrectoIdVoz = soundPool.load(this, R.raw.incorrecto, 2);

        historialDeCambio = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        String pasadito = extras.getString("Dificultad");

        if(pasadito.equals("Fácil")) {
            cambioView.setVisibility(View.VISIBLE);
        }
        else {
            cambioView.setVisibility(View.INVISIBLE);
        }

        instrucciones();

        nuevaTransaccion();
    }

    private void instrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bienvenido!");
        builder.setIcon(R.drawable.bb_px_normal_habla);
        builder.setMessage(
                "Lee con atención los datos que se te dan; no te preocupes, ¡la mejor forma de entender es intentarlo! 🤖🤗" +
                "\n\n" +
                "Recuerda que no es necesario que calcules mentalmente, puedes utilizar papel y lápiz. ¡Diviértete!");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog cuadrito = builder.create();
        cuadrito.show();
    }

    private void nuevaTransaccion() {
        historialDeCambio.clear();

        precio = (int)(Math.random()*220)+15;
        do{
            pago = (int)(Math.random()*320)+15;
        }while(pago<precio);

        totalEntregado = 0;
        cambioView.setText("Cambio a entregar: $"+(pago-precio));
        actualizarLabels();
    }

    private void actualizarLabels() {
        precioView.setText("Precio del producto: $"+precio);
        pagoView.setText("El cliente pagó: $"+pago);
        totalView.setText("Cambio entregado: $"+totalEntregado);
    }

    @Override
    public void onClick(View v) {

        if(v instanceof ImageButton) {
            int valor = Integer.parseInt(v.getTag().toString());

            historialDeCambio.add(valor);

            totalEntregado += valor;
            actualizarLabels();
        }
        else if(v instanceof Button) {
            String cadenita = ((Button)v).getText().toString();

            if(cadenita.equals("Deshacer última acción")) {
                if(historialDeCambio.isEmpty()) {
                    Toast.makeText(this, "No hay nada que deshacer. 🤷", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer ultimoValor = historialDeCambio.get(historialDeCambio.size()-1);

                    totalEntregado -= ultimoValor;
                    historialDeCambio.remove(historialDeCambio.size()-1);
                    actualizarLabels();
                }
            }
            else if(cadenita.equals("Confirmar respuesta")) {
                int cambio = pago - precio;

                if (totalEntregado == cambio) {
                    Toast.makeText(this,
                                    "¡Correcto! ✅",
                                    Toast.LENGTH_SHORT)
                            .show();

                    soundPool.play(correctoIdFx, 1, 1, 0, 0, 1);
                    soundPool.play(correctoIdVoz, 1, 1, 1, 0, 1);
                }
                else if (totalEntregado < cambio) {
                    int faltante = cambio - totalEntregado;
                    Toast.makeText(this,
                                    "❌ Faltan $" + faltante,
                                    Toast.LENGTH_SHORT)
                            .show();

                    soundPool.play(incorrectoIdFx, 1, 1, 0, 0, 1);
                    soundPool.play(incorrectoIdVoz, 1, 1, 1, 0, 1);
                }
                else {
                    int sobrante = totalEntregado - cambio;

                    Toast.makeText(this,
                                    "❌ Te pasaste por $" + sobrante,
                                    Toast.LENGTH_SHORT)
                            .show();

                    soundPool.play(incorrectoIdFx, 1, 1, 0, 0, 1);
                    soundPool.play(incorrectoIdVoz, 1, 1, 1, 0, 1);
                }

                nuevaTransaccion();
            }
            else if(cadenita.equals("Volver al menú de dificultad")) {
                finish();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Musiquita.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Musiquita.puchalePlay(this);
    }

}