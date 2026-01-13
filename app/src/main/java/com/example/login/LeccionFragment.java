package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_2C;
import com.example.lecciones.ciencias.primerosegundo.ActividadLeccion1_3C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_1C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_2C;
import com.example.lecciones.ciencias.quintosexto.ActividadLeccion3_3C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_1C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_2C;
import com.example.lecciones.ciencias.tercerocuarto.ActividadLeccion2_3C;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion7M;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion8M;
import com.example.lecciones.matematicas.quintosexto.ActividadLeccion9M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion4M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion5M;
import com.example.lecciones.matematicas.tercerocuarto.ActividadLeccion6M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion2M;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion3M;

import java.util.ArrayList;
import java.util.List;

public class LeccionFragment extends Fragment {

    private static final String ARG_NIVEL_TIPO = "nivel_tipo"; // 1: Básico, 2: Intermedio, 3: Avanzado
    private int nivelTipo;

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

        String materia = getActivity().getIntent().getStringExtra("materia");
        SharedPreferences prefs = getActivity().getSharedPreferences("Progreso_" + materia, Context.MODE_PRIVATE);
        int nivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lecciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Leccion> lecciones = getLeccionesPorNivel(materia, nivelTipo);

        LeccionAdapter adapter = new LeccionAdapter(getContext(), lecciones, materia, nivelDesbloqueado);
        recyclerView.setAdapter(adapter);
    }

    private List<Leccion> getLeccionesPorNivel(String materia, int nivelTipo) {
        List<Leccion> lecciones = new ArrayList<>();
        if ("matematicas".equalsIgnoreCase(materia)) {
            int background = R.drawable.bg_card_matematicas;
            if (nivelTipo == 1) { // Básico
                lecciones.add(new Leccion("Lección 1", "Contando y Sumando", background, 1, ActividadLeccion1M.class));
                lecciones.add(new Leccion("Lección 2", "Sumas y Restas", background, 2, ActividadLeccion2M.class));
                lecciones.add(new Leccion("Lección 3", "Decenas", background, 3, ActividadLeccion3M.class));
            } else if (nivelTipo == 2) { // Intermedio
                lecciones.add(new Leccion("Lección 4", "Operaciones", background, 4, ActividadLeccion4M.class));
                lecciones.add(new Leccion("Lección 5", "Área y Perímetro", background, 5, ActividadLeccion5M.class));
                lecciones.add(new Leccion("Lección 6", "Ángulos", background, 6, ActividadLeccion6M.class));
            } else { // Avanzado
                lecciones.add(new Leccion("Lección 7", "Fracciones y Decimales", background, 7, ActividadLeccion7M.class));
                lecciones.add(new Leccion("Lección 8", "Porcentajes", background, 8, ActividadLeccion8M.class));
                lecciones.add(new Leccion("Lección 9", "Volumen", background, 9, ActividadLeccion9M.class));
            }
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            int background = R.drawable.bg_card_ciencias;
            if (nivelTipo == 1) { // Básico
                lecciones.add(new Leccion("Lección 1", "Las partes del cuerpo", background, 1, ActividadLeccion1C.class));
                lecciones.add(new Leccion("Lección 2", "Los estados de la materia", background, 2, ActividadLeccion1_2C.class));
                lecciones.add(new Leccion("Lección 3", "Las 3 R", background, 3, ActividadLeccion1_3C.class));
            } else if (nivelTipo == 2) { // Intermedio
                lecciones.add(new Leccion("Lección 4", "La materia", background, 4, ActividadLeccion2_1C.class));
                lecciones.add(new Leccion("Lección 5", "El sistema solar", background, 5, ActividadLeccion2_2C.class));
                lecciones.add(new Leccion("Lección 6", "Ecosistemas", background, 6, ActividadLeccion2_3C.class));
            } else { // Avanzado
                lecciones.add(new Leccion("Lección 7", "La materia", background, 7, ActividadLeccion3_1C.class));
                lecciones.add(new Leccion("Lección 8", "Energía", background, 8, ActividadLeccion3_2C.class));
                lecciones.add(new Leccion("Lección 9", "Biología", background, 9, ActividadLeccion3_3C.class));
            }
        }
        return lecciones;
    }
}
