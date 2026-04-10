package com.kidzbrain.lecciones.matematicas.quintosexto;

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

import com.kidzbrain.fraginteractivos.EjercicioDados1;
import com.kidzbrain.fraginteractivos.EjercicioProbabilidad1;
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

public class ActividadLeccion3_4M extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar barraDeProgreso;
    private Button botonSiguiente;
    private ImageButton botonSalir;

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

        nivelActual = getIntent().getIntExtra("nivel", 3);
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
        builder.setPositiveButton("Si", (dialogInterface, i) -> finish());
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void construirLeccion() {
        listaDePasos = new ArrayList<>();

        // Teoría
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria1_matematicas, R.raw.mate3_4_1));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria2_matematicas, R.raw.mate3_4_2));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria3_matematicas, R.raw.mate3_4_3));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria4_matematicas, R.raw.mate3_4_4));
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria5_matematicas, R.raw.mate3_4_5));

        // Ejercicios
        listaDePasos.add(new EjercicioProbabilidad1());
        listaDePasos.add(new EjercicioDados1());

        // Fin
        listaDePasos.add(PlantillaFragmentoTeoria.getInstance(R.layout.fragment_lec3_4_teoria6_fin_matematicas, R.raw.mate3_4_6));
    }

    private void mostrarPaso(int indice) {
        Fragment fragmentoAMostrar = listaDePasos.get(indice);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorcito_fragmentito, fragmentoAMostrar).commit();

        float progreso = ((float) (indice + 1) / listaDePasos.size()) * 100;
        barraDeProgreso.setProgress((int) progreso);

        IPasoLeccion paso = (IPasoLeccion) fragmentoAMostrar;
        if (paso.esInteractivo()) {
            botonSiguiente.setEnabled(false);
            paso.setOyentePasoCompletado(() -> botonSiguiente.setEnabled(true));
        } else {
            botonSiguiente.setEnabled(true);
        }
    }

    private void avanzarAlSiguientePaso() {
        if (pasoActual < listaDePasos.size() - 1) {
            pasoActual++;
            mostrarPaso(pasoActual);
        } else {
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

    private void guardarProgresoEnServidor(int idUsuario, int idLeccion) {
        ApiService api = RetrofitClient.getApiService();
        ProgresoDto progreso = new ProgresoDto(idUsuario, idLeccion, 1, 100);

        api.guardarProgreso(progreso).enqueue(new Callback<ProgresoDto>() {
            @Override
            public void onResponse(Call<ProgresoDto> call, Response<ProgresoDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadLeccion3_4M.this, "¡Lección Completada y Guardada!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActividadLeccion3_4M.this, "Se guardó localmente (Error Servidor)", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<ProgresoDto> call, Throwable t) {
                Toast.makeText(ActividadLeccion3_4M.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
