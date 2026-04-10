package com.kidzbrain.fraginteractivos;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoInteractivo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EjercicioNutricion extends PlantillaFragmentoInteractivo implements View.OnClickListener {

    private class ItemNutricion {
        int imagen;
        String nombre;
        int tipo;

        ItemNutricion(int img, String nom, int t) {
            this.imagen = img;
            this.nombre = nom;
            this.tipo = t;
        }
    }

    private List<ItemNutricion> listaPreguntas;
    private int indiceActual = 0;

    private ImageView imgAlimento;
    private TextView txtProgreso, txtNombre;
    private LinearLayout layoutBotones, layoutFinal;
    private Button btnEnergia, btnCrecer, btnProteger, btnRepetir;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ejercicio_nutricion, container, false);

        imgAlimento = vista.findViewById(R.id.img_alimento_pregunta);
        txtProgreso = vista.findViewById(R.id.txt_progreso_nutricion);
        txtNombre = vista.findViewById(R.id.txt_nombre_alimento);
        layoutBotones = vista.findViewById(R.id.layout_botones_nutri);
        layoutFinal = vista.findViewById(R.id.layout_final_nutri);

        btnEnergia = vista.findViewById(R.id.btn_nutri_energia);
        btnCrecer = vista.findViewById(R.id.btn_nutri_crecer);
        btnProteger = vista.findViewById(R.id.btn_nutri_proteger);
        btnRepetir = vista.findViewById(R.id.btn_repetir_nutri);

        btnEnergia.setTag(0);
        btnCrecer.setTag(1);
        btnProteger.setTag(2);

        btnEnergia.setOnClickListener(this);
        btnCrecer.setOnClickListener(this);
        btnProteger.setOnClickListener(this);
        btnRepetir.setOnClickListener(v -> reiniciarEjercicio());

        prepararPreguntas();

        mostrarVentanaInstruccion("¡Superpoderes Alimenticios!",
                "Mira con atención cada alimento y toca el botón correcto según su superpoder.\n" +
                        "Algunos alimentos nos dan energía para jugar y movernos, otros nos ayudan a crecer fuertes, y otros nos ayudan a proteger nuestro cuerpo y mantenernos sanos.\n" +
                        "¡Observa bien y elige la mejor respuesta!",
                "aud_instruccion_nutricion");

        cargarPregunta();
        return vista;
    }

    private void prepararPreguntas() {
        listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_pan, "EL PAN", 0));
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_huevo, "EL HUEVO", 1));
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_manzana, "LA MANZANA", 2));
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_arroz, "EL ARROZ", 0));
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_pescado, "EL PESCADO", 1));
        listaPreguntas.add(new ItemNutricion(R.drawable.alimento_brocoli, "EL BRÓCOLI", 2));

        Collections.shuffle(listaPreguntas);
    }

    private void cargarPregunta() {
        if (indiceActual < listaPreguntas.size()) {
            ItemNutricion actual = listaPreguntas.get(indiceActual);
            imgAlimento.setImageResource(actual.imagen);
            txtNombre.setText(actual.nombre);
            txtProgreso.setText("Alimento " + (indiceActual + 1) + " de " + listaPreguntas.size());

            layoutBotones.setVisibility(View.VISIBLE);
            layoutFinal.setVisibility(View.GONE);
        } else {
            txtNombre.setText("¡LOGRADO!");
            txtProgreso.setText("6 de 6 completados");
            imgAlimento.setImageResource(R.drawable.esnupi2);
            layoutBotones.setVisibility(View.GONE);
            layoutFinal.setVisibility(View.VISIBLE);

            notificarPasoCompletado();
        }
    }

    @Override
    public void onClick(View v) {
        int seleccion = (int) v.getTag();
        if (seleccion == listaPreguntas.get(indiceActual).tipo) {
            reproducirSonido("sonido_correcto");
            indiceActual++;
            cargarPregunta();
        } else {
            reproducirSonido("sonido_incorrecto");
            darPista();
        }
    }

    private void darPista() {
        int correct = listaPreguntas.get(indiceActual).tipo;
        String pista = (correct == 0) ? "Gasolina para correr." : (correct == 1) ? "Para músculos fuertes." : "Es un escudo de vitaminas.";
        Toast.makeText(getContext(), pista, Toast.LENGTH_SHORT).show();
    }

    private void reiniciarEjercicio() {
        indiceActual = 0;
        prepararPreguntas();
        cargarPregunta();
    }

    @Override protected void comprobarRespuesta() {}

    @Override protected void cargarSonidosEspecificos(Context context, SoundPool soundPool) {
        mapaSonidos.put("sonido_correcto", soundPool.load(context, R.raw.aud_correcto, 1));
        mapaSonidos.put("sonido_incorrecto", soundPool.load(context, R.raw.aud_incorrecto, 1));
        mapaSonidos.put("aud_instruccion_nutricion", soundPool.load(context, R.raw.instrucciones_nutri, 1));
    }

    private void mostrarVentanaInstruccion(String titulo, String mensaje, String nombreSonido) {
        reproducirSonido(nombreSonido);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setCancelable(false);

        builder.setNeutralButton("🔊 Oír instrucciones", (dialog, which) -> {
        });

        builder.setPositiveButton("¡ENTENDIDO!", (dialog, which) -> {
            dialog.dismiss();
        });

        android.app.AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnAudio = dialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL);
            btnAudio.setOnClickListener(v -> {
                reproducirSonido(nombreSonido);
                Toast.makeText(getContext(), "Reproduciendo instrucción...", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show();
    }
}