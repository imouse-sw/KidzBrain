package com.example.lecciones.ciencias.tercerocuarto;

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

import com.example.fraginteractivos.EjercicioMateria;
import com.example.fraginteractivos.lec1ciencias_ejerpartes;
import com.example.login.R;
import com.example.utilidades.leccionutil.IPasoLeccion;
import com.example.utilidades.leccionutil.PlantillaFragmentoTeoria;

import java.util.ArrayList;
import java.util.List;

public class ActividadLeccion2_1C  extends AppCompatActivity implements View.OnClickListener
{
    // las vistas de la lección
    private ProgressBar barraDeProgreso;
    private Button botonSiguiente;
    private ImageButton botonSalir;

    // para la lógica
    private List<Fragment> listaDePasos;
    private int pasoActual = 0;

    // para sonidos
    // etc etc etc

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
                R.layout.fragment_lec2_1ciencias_teoria1,
                R.raw.lec_ciencias4));

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec2_1ciencias_teoria2,
                R.raw.lec_ciencias4_2));

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec2_1ciencias_teoria3,
                R.raw.lec_ciencias4_3));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec2_1ciencias_teoria4,
                R.raw.lec_ciencias4_4));

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec2_1ciencias_teoria5,
                R.raw.lec_ciencias4_5));

        listaDePasos.add(new EjercicioMateria());

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(
                R.layout.fragment_lec2_1ciencias_teoria6_fin,
                R.raw.lec_ciencias4_6));



        /* asi va a estar este pedo

        practicamente en cada actividad de las lecciones va a haber el miiiismo código pero va a cambiar
        este bloque de construirLeccion():

        vamo' a meter toooodoos los fragmentos que vamos a utilizar para cada lección en la lista.

        por ejemplo, metemos una instancia de teoría

        AQUÍ INSTANCIO DIRECTAMENTE LA PLANTILLA PORQUE LO ÚNICO QUE CAMBIA ES EL LAYOUT, NO LA CLASE. LA CLASE
        YA ESTÁ "TERMINADA", NO NECESITA LÓGICA PORQUE LO ÚNICO QUE VA A HACER EL NIÑO ES ESCUCHAR O LEER LA INFORMACIÓN
        Y PUCHARLE SIGUIENTE.

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_contar_teoria_1));

        luego metemos igual una instancia de una actividad interactiva, la que sea

        AQUÍ SÍ SE INSTANCIA UN EJERCICIO APARTE PORQUE ESE EJERCICIO VA A HEREDAR DE LA CLASE "PlantillaFragmentoInteractivo",
        SE VA A ESCRIBIR SU LÓGICA Y SE HACE EL getInstance() DENTRO DE ESE EJERCICIO CONCRETO.

        listaDePasos.add(new EjercicioContarManzanas().getInstance()); (esto es nomás un ejemplo jeje)

        luego más teoría
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_contar_teoria_2));

        etc etc etc depende lo que necesitemos en cada lección :P
         */
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
                /*
                esto se vuelve a habilitar el botoncito cuando el fragmento llama a
                notificarPasoCompletado(), que es un métodoque se tiene que implementar
                en cada fragmento interactivo que hereda de PlantillaFragmentoInteractivo, o sea es aparte
                y los requerimientos que el programa tiene que cumplir para que este metodose active
                varía en función de los ejercicios que tengamos en cada lección.
                */
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
            // si sí es el último paso, la lección terminó y ya yupi bien hecho niño te ganaste una verguiada
            Toast.makeText(this, "¡Lección Completada!", Toast.LENGTH_SHORT).show();
            /*
            si neta me dan ganas de chambear, aquí le pongo que se desbloquee el juego relacionado a la lección o
            grupo de lecciones y ese tipo de cosas xdddd spoiler muy probablemente no
             */
            SharedPreferences prefs = getSharedPreferences("Progreso_ciencias", MODE_PRIVATE);
            prefs.edit().putInt("nivelDesbloqueado", 2).apply();
            finish(); // cierra la actividad de la lección
        }
    }
}
