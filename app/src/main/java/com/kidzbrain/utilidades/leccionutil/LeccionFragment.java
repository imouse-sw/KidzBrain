package com.kidzbrain.utilidades.leccionutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidzbrain.login.R;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.LeccionResponseDto;

// IMPORTS DE TUS ACTIVITIES (Asegúrate de tener todos aquí)
import com.kidzbrain.lecciones.matematicas.primerosegundo.*;
import com.kidzbrain.lecciones.matematicas.tercerocuarto.*;
import com.kidzbrain.lecciones.matematicas.quintosexto.*;
import com.kidzbrain.lecciones.ciencias.primerosegundo.*;
import com.kidzbrain.lecciones.ciencias.tercerocuarto.*;
import com.kidzbrain.lecciones.ciencias.quintosexto.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeccionFragment extends Fragment {

    private static final String ARG_NIVEL_TIPO = "nivel_tipo";
    private int nivelTipo; // 1 (Básico), 2 (Intermedio), 3 (Avanzado) -> Equivalente a idGrado
    private RecyclerView recyclerView;

    // ID TEMPORAL (Deberías sacarlo del Login real)
    private int idUsuarioActual;

    public static LeccionFragment newInstance(int nivelTipo) {
        LeccionFragment fragment = new LeccionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NIVEL_TIPO, nivelTipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nivelTipo = getArguments().getInt(ARG_NIVEL_TIPO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leccion_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // El segundo parámetro (-1) es lo que devuelve si NO encuentra nada (ej. error de sesión)
        idUsuarioActual = preferences.getInt("userId", -1);

        if (idUsuarioActual == -1) {
            // Manejo de error: Si no hay ID, tal vez mandarlo al Login de nuevo
            Toast.makeText(getContext(), "Error de sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = view.findViewById(R.id.recycler_view_lecciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. Obtener datos básicos
        String materiaNombre = getActivity().getIntent().getStringExtra("materia");
        int idMateria = "matematicas".equalsIgnoreCase(materiaNombre) ? 1 : 2; // Mat=1, Cie=2
        int idGrado = nivelTipo; // El tab 1 es Grado 1, etc.

        // 2. INICIAR LA CARGA DESDE EL SERVIDOR
        cargarDatosDeServidor(idMateria, idGrado, materiaNombre);
    }

    private void cargarDatosDeServidor(int idMateria, int idGrado, String materiaNombre) {
        ApiService api = RetrofitClient.getApiService(getContext());

        // PASO A: Preguntar ¿En qué nivel voy? (Para los candados)
        api.getSiguienteLeccion(idUsuarioActual, idMateria, idGrado).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                // Si el server responde bien, usamos ese número. Si no, por defecto el 1.
                int ordenDesbloqueado = (response.isSuccessful() && response.body() != null)
                        ? response.body() : 1;

                // PASO B: Ahora sí, ¡Dame los títulos de la base de datos!
                traerTextosDeBD(idMateria, idGrado, materiaNombre, ordenDesbloqueado);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Si falla internet, asumimos nivel 1 y tratamos de cargar textos
                traerTextosDeBD(idMateria, idGrado, materiaNombre, 1);
            }
        });
    }

    private void traerTextosDeBD(int idMateria, int idGrado, String materiaNombre, int ordenDesbloqueado) {
        ApiService api = RetrofitClient.getApiService(getContext());

        api.getLeccionesPorGrado(idMateria, idGrado).enqueue(new Callback<List<LeccionResponseDto>>() {
            @Override
            public void onResponse(Call<List<LeccionResponseDto>> call, Response<List<LeccionResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LeccionResponseDto> datosDeLaBD = response.body();

                    // AQUÍ OCURRE LA MAGIA: Convertimos JSON -> Objetos Visuales
                    List<Leccion> listaParaMostrar = mapearDatos(datosDeLaBD, materiaNombre, idGrado);

                    // Llenamos el Recycler
                    LeccionAdapter adapter = new LeccionAdapter(getContext(), listaParaMostrar, materiaNombre, ordenDesbloqueado);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<LeccionResponseDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar lecciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- ESTA FUNCIÓN UNE TUS TEXTOS DE BD CON TUS ACTIVITIES DE ANDROID ---
    private List<Leccion> mapearDatos(List<LeccionResponseDto> datosBD, String materia, int grado) {
        List<Leccion> listaFinal = new ArrayList<>();

        // Definimos el fondo
        int bg = "matematicas".equalsIgnoreCase(materia)
                ? R.drawable.bg_card_matematicas
                : R.drawable.bg_card_ciencias;

        for (LeccionResponseDto dato : datosBD) {
            // Buscamos qué Activity le toca según el ORDEN que viene de la BD
            Class<?> actividad = obtenerActivity(materia, grado, dato.getOrden());

            if (actividad != null) {
                // Creamos la lección usando TÍTULO y DESCRIPCIÓN de MySQL
                listaFinal.add(new Leccion(
                        dato.getLeccionId(),
                        dato.getTitulo(),       // Texto de BD
                        dato.getDescripcion(),  // Texto de BD
                        bg,
                        dato.getOrden(),
                        actividad
                ));
            }
        }
        return listaFinal;
    }

    // Un switch gigante para saber qué abrir
    private Class<?> obtenerActivity(String materia, int grado, int orden) {
        if ("matematicas".equalsIgnoreCase(materia)) {
            if (grado == 1) { // Básico
                switch (orden) {
                    case 1: return ActividadLeccion1_1M.class;
                    case 2: return ActividadLeccion1_2M.class;
                    case 3: return ActividadLeccion1_3M.class;
                    case 4: return ActividadLeccion1_4M.class;
                }
            } else if (grado == 2) { // Intermedio
                switch (orden) {
                    case 1: return ActividadLeccion2_1M.class; // Orden 1 de BD -> Lección 4 de Java
                    case 2: return ActividadLeccion2_2M.class;
                    case 3: return ActividadLeccion2_3M.class;
                    case 4: return ActividadLeccion2_4M.class;
                }
            } else if (grado == 3) { // Avanzado
                switch (orden) {
                    case 1: return ActividadLeccion3_1M.class;
                    case 2: return ActividadLeccion3_2M.class;
                    case 3: return ActividadLeccion3_3M.class;
                    case 4: return ActividadLeccion3_4M.class;
                }
            }
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            if (grado == 1) {
                switch (orden) {
                    case 1: return ActividadLeccion1C.class;
                    case 2: return ActividadLeccion1_2C.class;
                    case 3: return ActividadLeccion1_3C.class;
                    case 4: return ActividadLeccion1_4C.class;
                }
            } else if (grado == 2) {
                switch (orden) {
                    case 1: return ActividadLeccion2_1C.class;
                    case 2: return ActividadLeccion2_2C.class;
                    case 3: return ActividadLeccion2_3C.class;
                    case 4: return ActividadLeccion2_4C.class;
                }
            } else if (grado == 3) {
                switch (orden) {
                    case 1: return ActividadLeccion3_1C.class;
                    case 2: return ActividadLeccion3_2C.class;
                    case 3: return ActividadLeccion3_3C.class;
                    case 4: return ActividadLeccion3_4C.class;
                }
            }
        }
        return null; // Si no hay activity asignada, no se muestra
    }
}