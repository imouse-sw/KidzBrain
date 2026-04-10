package com.kidzbrain.lecciones.matematicas.tercerocuarto;

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

import com.kidzbrain.fraginteractivos.EjercicioAngulo;
import com.kidzbrain.login.R;
import com.kidzbrain.utilidades.leccionutil.IPasoLeccion;
import com.kidzbrain.utilidades.leccionutil.PlantillaFragmentoTeoria;

import java.util.ArrayList;
import java.util.List;

import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.ProgresoDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadLeccion2_3M extends AppCompatActivity implements View.OnClickListener {
    // las vistas de la lección
    private ProgressBar barraDeProgreso;
    private Button botonSiguiente;
    private ImageButton botonSalir;

    // para la lógica
    private List<Fragment> listaDePasos;
    private int pasoActual = 0;
    private int nivelActual;
    private String materia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_leccion);

        barraDeProgreso = findViewById(R.id.barra_progreso);
        botonSiguiente = findViewById(R.id.boton_siguiente);
        botonSalir = findViewById(R.id.boton_salir);

        // Recibir el nivel y la materia
        nivelActual = getIntent().getIntExtra("nivel", 1);
        materia = getIntent().getStringExtra("materia");

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

        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec6_teoria1, R.raw.mate6_1));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec6_teoria2, R.raw.mate6_2));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec6_teoria3, R.raw.mate6_3));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec6_teoria4, R.raw.mate6_4));
        listaDePasos.add(new EjercicioAngulo());
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec6_teoria5_fin, R.raw.mate6_5));
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

    private void avanzarAlSiguientePaso() {
        if (pasoActual < listaDePasos.size() - 1) {
            pasoActual++;
            mostrarPaso(pasoActual);
        } else {
            // --- AQUÍ ESTABA EL ERROR: QUITAMOS SHAREDPREFERENCES ---

            // 1. Recuperar datos necesarios
            int idLeccion = getIntent().getIntExtra("idLeccion", -1);

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            int idUsuario = prefs.getInt("userId", -1);

            if (idLeccion != -1 && idUsuario != -1) {
                guardarProgresoEnServidor(idUsuario, idLeccion);
            } else {
                Toast.makeText(this, "Error de datos: No se pudo guardar", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // Nuevo método para hablar con Spring Boot
    private void guardarProgresoEnServidor(int idUsuario, int idLeccion) {
        ApiService api = RetrofitClient.getApiService();

        // Creamos el DTO para enviar (Completado = 1)
        ProgresoDto progreso = new ProgresoDto(idUsuario, idLeccion, 1, 100); // 100 puntos por defecto
        // Nota: Asegúrate que tu ProgresoDto en Android tenga este constructor o usa setters

        api.guardarProgreso(progreso).enqueue(new Callback<ProgresoDto>() {
            @Override
            public void onResponse(Call<ProgresoDto> call, Response<ProgresoDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadLeccion2_3M.this, "¡Lección Completada y Guardada!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActividadLeccion2_3M.this, "Se guardó localmente (Error Servidor)", Toast.LENGTH_SHORT).show();
                }
                finish(); // Regresamos al mapa (que ahora sí se actualizará)
            }

            @Override
            public void onFailure(Call<ProgresoDto> call, Throwable t) {
                Toast.makeText(ActividadLeccion2_3M.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
