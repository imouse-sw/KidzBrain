package com.kidzbrain.juegos.comida;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kidzbrain.juegos.habitats.BrainBotDialogHelper;
import com.kidzbrain.login.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JuegoPedidosSaludablesActivity extends AppCompatActivity {

    private TextView tvPedido, tvPuntaje, tvBandeja;
    private ImageView ivCliente, ivVida1, ivVida2, ivVida3;

    private ConstraintLayout cardAlim1, cardAlim2, cardAlim3, cardAlim4, cardAlim5, cardAlim6, cardAlim7, cardAlim8;
    private ImageView ivAlim1, ivAlim2, ivAlim3, ivAlim4, ivAlim5, ivAlim6, ivAlim7, ivAlim8;
    private TextView tvAlim1, tvAlim2, tvAlim3, tvAlim4, tvAlim5, tvAlim6, tvAlim7, tvAlim8;

    private ConstraintLayout btnEntregar, btnLimpiar;
    private ImageButton btnBack, btnBrainBot;

    private final List<Alimento> opcionesActuales = new ArrayList<>();
    private final List<Alimento> seleccionados = new ArrayList<>();

    private RepositorioPedidosSaludables repositorio;
    private ClientePedido pedidoActual;

    private int puntaje = 0;
    private int vidas = 3;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego_pedidos_saludables);

        iniciarVistas();
        repositorio = new RepositorioPedidosSaludables();

        btnBack.setOnClickListener(v -> finish());

        btnBrainBot.setOnClickListener(v ->
                BrainBotDialogHelper.mostrarDialogo(
                        this,
                        "Lee bien lo que pide el cliente, elige los alimentos correctos y luego presiona entregar pedido.",
                        R.raw.brainbot_ayuda_pedidos
                )
        );

        btnLimpiar.setOnClickListener(v -> {
            seleccionados.clear();
            actualizarBandeja();
        });

        btnEntregar.setOnClickListener(v -> entregarPedido());

        actualizarMarcadores();
        nuevaRonda();
    }

    private void iniciarVistas() {
        tvPedido = findViewById(R.id.tvPedido);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        tvBandeja = findViewById(R.id.tvBandeja);

        ivCliente = findViewById(R.id.ivCliente);
        ivVida1 = findViewById(R.id.ivVida1);
        ivVida2 = findViewById(R.id.ivVida2);
        ivVida3 = findViewById(R.id.ivVida3);

        cardAlim1 = findViewById(R.id.cardAlim1);
        cardAlim2 = findViewById(R.id.cardAlim2);
        cardAlim3 = findViewById(R.id.cardAlim3);
        cardAlim4 = findViewById(R.id.cardAlim4);
        cardAlim5 = findViewById(R.id.cardAlim5);
        cardAlim6 = findViewById(R.id.cardAlim6);
        cardAlim7 = findViewById(R.id.cardAlim7);
        cardAlim8 = findViewById(R.id.cardAlim8);

        ivAlim1 = findViewById(R.id.ivAlim1);
        ivAlim2 = findViewById(R.id.ivAlim2);
        ivAlim3 = findViewById(R.id.ivAlim3);
        ivAlim4 = findViewById(R.id.ivAlim4);
        ivAlim5 = findViewById(R.id.ivAlim5);
        ivAlim6 = findViewById(R.id.ivAlim6);
        ivAlim7 = findViewById(R.id.ivAlim7);
        ivAlim8 = findViewById(R.id.ivAlim8);

        tvAlim1 = findViewById(R.id.tvAlim1);
        tvAlim2 = findViewById(R.id.tvAlim2);
        tvAlim3 = findViewById(R.id.tvAlim3);
        tvAlim4 = findViewById(R.id.tvAlim4);
        tvAlim5 = findViewById(R.id.tvAlim5);
        tvAlim6 = findViewById(R.id.tvAlim6);
        tvAlim7 = findViewById(R.id.tvAlim7);
        tvAlim8 = findViewById(R.id.tvAlim8);

        btnEntregar = findViewById(R.id.btnEntregar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        btnBack = findViewById(R.id.btnBack);
        btnBrainBot = findViewById(R.id.btnBrainBot);
    }

    private void nuevaRonda() {
        if (vidas <= 0) {
            mostrarResultadoFinal();
            return;
        }

        seleccionados.clear();
        actualizarBandeja();

        pedidoActual = repositorio.obtenerSiguientePedido();
        opcionesActuales.clear();
        opcionesActuales.addAll(repositorio.obtenerOpcionesParaPedido(pedidoActual));

        tvPedido.setText(pedidoActual.getTextoPedido());
        ivCliente.setImageResource(pedidoActual.getImagenClienteResId());

        cargarAlimentosEnPantalla();
    }

    private void cargarAlimentosEnPantalla() {
        configurarCard(cardAlim1, ivAlim1, tvAlim1, opcionesActuales.get(0));
        configurarCard(cardAlim2, ivAlim2, tvAlim2, opcionesActuales.get(1));
        configurarCard(cardAlim3, ivAlim3, tvAlim3, opcionesActuales.get(2));
        configurarCard(cardAlim4, ivAlim4, tvAlim4, opcionesActuales.get(3));
        configurarCard(cardAlim5, ivAlim5, tvAlim5, opcionesActuales.get(4));
        configurarCard(cardAlim6, ivAlim6, tvAlim6, opcionesActuales.get(5));
        configurarCard(cardAlim7, ivAlim7, tvAlim7, opcionesActuales.get(6));
        configurarCard(cardAlim8, ivAlim8, tvAlim8, opcionesActuales.get(7));
    }

    private void configurarCard(ConstraintLayout card, ImageView imagen, TextView texto, Alimento alimento) {
        imagen.setImageResource(alimento.getImagenResId());
        texto.setText(alimento.getNombre());

        card.setOnClickListener(v -> {
            if (seleccionados.contains(alimento)) {
                seleccionados.remove(alimento);
            } else {
                if (seleccionados.size() < pedidoActual.getCantidadMaxima()) {
                    seleccionados.add(alimento);
                } else {
                    Toast.makeText(this,
                            "Solo puedes elegir " + pedidoActual.getCantidadMaxima() + " alimento(s).",
                            Toast.LENGTH_SHORT).show();
                }
            }
            actualizarBandeja();
        });
    }

    private void actualizarBandeja() {
        if (seleccionados.isEmpty()) {
            tvBandeja.setText("Bandeja vacía");
            return;
        }

        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < seleccionados.size(); i++) {
            texto.append("• ").append(seleccionados.get(i).getNombre());
            if (i < seleccionados.size() - 1) texto.append("\n");
        }
        tvBandeja.setText(texto.toString());
    }

    private void entregarPedido() {
        if (seleccionados.size() < pedidoActual.getCantidadMinima()) {
            Toast.makeText(this, "Faltan alimentos en la bandeja.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean correcto = validarPedido();

        if (correcto) {
            puntaje += 20;
            Toast.makeText(this, "Pedido correcto.", Toast.LENGTH_SHORT).show();
            BrainBotDialogHelper.mostrarDialogo(this, "Muy bien. Ese pedido está correcto.", R.raw.brainbot_correcto);
        } else {
            vidas--;
            Toast.makeText(this, "Ese pedido no cumple lo solicitado.", Toast.LENGTH_SHORT).show();
            BrainBotDialogHelper.mostrarDialogo(this, "Ese pedido no está bien. Intenta prestar más atención.", R.raw.brainbot_error);
        }

        actualizarMarcadores();
        handler.postDelayed(this::nuevaRonda, 900);
    }

    private boolean validarPedido() {
        Set<String> etiquetasCumplidas = new HashSet<>();

        for (Alimento alimento : seleccionados) {
            for (String etiqueta : alimento.getEtiquetas()) {
                etiquetasCumplidas.add(etiqueta);
            }
        }

        for (String obligatoria : pedidoActual.getEtiquetasObligatorias()) {
            if (!etiquetasCumplidas.contains(obligatoria)) {
                return false;
            }
        }

        for (Alimento alimento : seleccionados) {
            for (String prohibida : pedidoActual.getEtiquetasProhibidas()) {
                if (alimento.tieneEtiqueta(prohibida)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void actualizarMarcadores() {
        tvPuntaje.setText(String.valueOf(puntaje));

        ivVida1.setImageResource(vidas >= 1 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
        ivVida2.setImageResource(vidas >= 2 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
        ivVida3.setImageResource(vidas >= 3 ? R.drawable.ic_corazon_lleno : R.drawable.ic_corazon_vacio);
    }

    private void mostrarResultadoFinal() {
        new AlertDialog.Builder(this)
                .setTitle("Juego terminado")
                .setMessage("Tu puntaje final fue: " + puntaje)
                .setCancelable(false)
                .setPositiveButton("Jugar otra vez", (dialog, which) -> reiniciarJuego())
                .setNegativeButton("Salir", (dialog, which) -> finish())
                .show();
    }

    private void reiniciarJuego() {
        puntaje = 0;
        vidas = 3;
        repositorio = new RepositorioPedidosSaludables();
        actualizarMarcadores();
        nuevaRonda();
    }

    @Override
    protected void onDestroy() {
        BrainBotDialogHelper.detenerAudio();
        super.onDestroy();
    }
}