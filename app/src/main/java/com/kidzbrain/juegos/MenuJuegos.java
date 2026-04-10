package com.kidzbrain.juegos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.kidzbrain.juegos.canicas.Canicas;
import com.kidzbrain.juegos.chicles.Chicles;
import com.kidzbrain.juegos.chicles.MenuChicles;
import com.kidzbrain.juegos.comida.MenuPedidosSaludables;
import com.kidzbrain.juegos.cuerpo.Cuerpo;
import com.kidzbrain.juegos.fracciones.Cafeteria;
import com.kidzbrain.juegos.multiplosdivisores.MenuMultiplosD;
import com.kidzbrain.juegos.multiplosdivisores.MultiplosDivisores;
import com.kidzbrain.juegos.habitats.MenuAnimalesHabitat;
import com.kidzbrain.juegos.pizzangulos.MenuPizza;
import com.kidzbrain.juegos.sentidos.Sentidos;
import com.kidzbrain.juegos.tanque.MenuTanque;
import com.kidzbrain.juegos.tresR.TresR;
import com.kidzbrain.login.R;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.JuegoResponseDto;
import com.kidzbrain.spring.Juego;
import com.kidzbrain.juegos.cambio.MenuCambio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuJuegos extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_juegos);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBackContainer).setOnClickListener(v -> finish());

        viewPager = findViewById(R.id.viewPagerJuegos);
        configurarEfectoCarrusel(); // Agregamos animaciones bonitas

        cargarJuegosDelServidor();
    }

    private void cargarJuegosDelServidor() {
        ApiService api = RetrofitClient.getApiService();
        api.getAllJuegos().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<JuegoResponseDto>> call, Response<List<JuegoResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Juego> juegosListos = mapearJuegos(response.body());
                    JuegoAdapter adapter = new JuegoAdapter(MenuJuegos.this, juegosListos);
                    viewPager.setAdapter(adapter);
                } else {
                    Toast.makeText(MenuJuegos.this, "No se encontraron juegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JuegoResponseDto>> call, Throwable t) {
                Toast.makeText(MenuJuegos.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Juego> mapearJuegos(List<JuegoResponseDto> dtos) {
        List<Juego> lista = new ArrayList<>();

        for (JuegoResponseDto dto : dtos) {
            Log.d("DEBUG_JUEGOS", "Recibido Juego: '" + dto.getNombreJuego() + "' | ID: " + dto.getJuegoId());

            Class<?> actividad = obtenerActivity(dto.getJuegoId());

            if (actividad == null) {
                Log.d("DEBUG_JUEGOS", "❌ No se encontró actividad para: " + dto.getNombreJuego());
            } else {
                Log.d("DEBUG_JUEGOS", "✅ Actividad encontrada: " + actividad.getSimpleName());
            }

            int icono = obtenerIcono(dto.getNombreJuego());

            lista.add(new Juego(
                    dto.getJuegoId(),
                    dto.getNombreJuego(),
                    dto.getDescripcion(),
                    icono,
                    actividad
            ));
        }
        return lista;
    }

    private Class<?> obtenerActivity(int id) {
        if (id == 1) {
            return Canicas.class;
        }
        if (id == 2) {
            return MenuCambio.class;
        }
        if (id == 3) {
            return MenuPizza.class;
        }
        if (id == 4) {
            return MenuTanque.class;
        }
        if (id == 5) {
            return Cafeteria.class;
        }
        if (id == 6) {
            return Cuerpo.class;
        }
        if (id == 7) {
            return Sentidos.class;
        }
        if (id == 8) {
            return TresR.class;
        }

        return null;
    }

    private int obtenerIcono(String nombre) {
        if (nombre != null && nombre.contains("Tienda")) {
            return R.drawable.ic_tienda;
        }
        else if (nombre != null && nombre.contains("Pizz")) {
            return R.drawable.ic_pipsha;
        }
        else if (nombre != null && nombre.contains("Adic")) {
            return R.drawable.ic_canicas;
        }
        else if (nombre != null && nombre.contains("Const")) {
            return R.drawable.ic_agua;
        }
        else if (nombre != null && nombre.contains("Cafetería")) {
            return R.drawable.ic_cafe;
        }
        else if (nombre != null && nombre.contains("cuerpo")) {
            return R.drawable.ic_cuerpo2;
        }
        else if (nombre != null && nombre.contains("¿Qué sientes?")) {
            return R.drawable.ic_sentidos;
        }
        else if (nombre != null && nombre.contains("Las 3R")) {
            return R.drawable.ic_basura;
        }
        return R.drawable.ic_games;
    }

    private void configurarEfectoCarrusel() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Efecto Zoom en el centro
        });

        viewPager.setPageTransformer(transformer);
    }
}