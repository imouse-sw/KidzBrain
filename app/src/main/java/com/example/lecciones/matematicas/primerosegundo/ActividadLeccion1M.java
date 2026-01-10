package com.example.lecciones.matematicas.primerosegundo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fraginteractivos.EjercicioContarCamiones;
import com.example.login.R;
import com.example.utilidades.leccionutil.IPasoLeccion;
import com.example.utilidades.leccionutil.PlantillaFragmentoTeoria;

import java.util.ArrayList;
import java.util.List;

public class ActividadLeccion1M extends AppCompatActivity implements View.OnClickListener {
    // las vistas de la lección
    private ProgressBar barraDeProgreso;
    private Button botonSiguiente;
    private ImageButton botonSalir;

    // para la lógica
    private List<Fragment> listaDePasos;
    private int pasoActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_leccion);

        barraDeProgreso = findViewById(R.id.barra_progreso);
        botonSiguiente = findViewById(R.id.boton_siguiente);
        botonSalir = findViewById(R.id.boton_salir);

        construirLeccion();
        mostrarPaso(pasoActual);

        botonSiguiente.setOnClickListener(this);
        botonSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.boton_siguiente) {
            avanzarAlSiguientePaso();
        }
        else if(id == R.id.boton_salir) {
            mensajeSalir();
        }
    }

    private void mensajeSalir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Saliendo...");
        builder.setIcon(R.drawable.ic_exit);
        builder.setMessage("¿Seguro que quieres salir?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.show();
    }

    private void construirLeccion() {
        listaDePasos = new ArrayList<>();

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec1_teoria1,
                R.raw.teoria1voz));

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec1_teoria2,
                R.raw.teoria2voz));

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec1_teoria3,
                R.raw.teoria3voz));

        listaDePasos.add(new EjercicioContarCamiones());

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec1_teoria4_fin,
                R.raw.teoria4voz));
    }

    /**
     * carga los fragmentos de las pantallitas en el contenedor (el framelayout)
     * @param indice es el número del paso a mostrar
     */
    private void mostrarPaso(int indice) {
        // obtiene el fragmento a partir de la listita que hicimos en el construirLeccion()
        Fragment fragmentoAMostrar = listaDePasos.get(indice);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorcito_fragmentito, fragmentoAMostrar).commit();

        // actualiza la barra de progreso según el índice de paso actual, esta formulita me la dió gema (gracias gema)
        float progreso = ((float) (indice + 1) / listaDePasos.size()) * 100;
        barraDeProgreso.setProgress((int) progreso);

        // habla con el fragmento a través de la interfaz de IPasoLeccion
        IPasoLeccion paso = (IPasoLeccion) fragmentoAMostrar;

        if (paso.esInteractivo()) {
            // si es un ejercicio, se inhabilita el botoncito de "siguiente"
            botonSiguiente.setEnabled(false);

            // y le pasamos un oyente para que nos avise cuando el usuario termine el ejercicio.
            paso.setOyentePasoCompletado(() -> {
                botonSiguiente.setEnabled(true);
            });

        } else {
            // si es teoría, el botón "siguiente" está habilitado
            botonSiguiente.setEnabled(true);
        }
    }

    // lógica del botoncito de siguiente
    private void avanzarAlSiguientePaso() {
        if (pasoActual < listaDePasos.size() - 1) {
            // si no es el último paso, avanza
            pasoActual++;
            mostrarPaso(pasoActual);
        } else {
            // Último paso completado
            Toast.makeText(this, "¡Lección Completada!", Toast.LENGTH_SHORT).show();

            // Guardar progreso para desbloquear nivel 2
            SharedPreferences prefs = getSharedPreferences("Progreso_matematicas", MODE_PRIVATE);
            prefs.edit().putInt("nivelDesbloqueado", 2).apply();

            finish(); // vuelve al mapa
        }
    }

}



