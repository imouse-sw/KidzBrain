package com.kidzbrain.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kidzbrain.juegos.MenuJuegos;
import com.kidzbrain.login.menuLateral.AjustesActivity;
import com.kidzbrain.login.menuLateral.AvanceActivity;
import com.kidzbrain.login.menuLateral.PerfilActivity;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.dto.AccesoDto;
import com.kidzbrain.utilidades.chatbot.ChatBotBottomSheet;
import com.kidzbrain.utilidades.leccionutil.mapa_niveles;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inicio extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ConstraintLayout btnMatematicas, btnCiencias, btnJuegos;
    private TextView tvNombreUsuario;
    private ApiService apiService;
    private ImageButton btnChatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnCiencias = findViewById(R.id.btnCiencias);
        btnJuegos = findViewById(R.id.btnJuegos);
        tvNombreUsuario = findViewById(R.id.tv_nombre_usuario);
        ImageButton btnChatbot = findViewById(R.id.btnChatbot);

        apiService = com.kidzbrain.spring.RetrofitClient.getApiService(this);

        // --- ANIMACIONES ---
        aplicarAnimaciones();

        cargarDatosUsuario();

        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        btnChatbot.setOnClickListener(v -> {
            ChatBotBottomSheet chat = new ChatBotBottomSheet();
            chat.show(getSupportFragmentManager(), "chatbot");
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(Inicio.this, PerfilActivity.class));
            } else if (id == R.id.nav_avance) {
                startActivity(new Intent(Inicio.this, AvanceActivity.class));
            } else if (id == R.id.nav_ajustes) {
                startActivity(new Intent(Inicio.this, AjustesActivity.class));
            } else if (id == R.id.nav_cerrar_sesion) {
                cerrarSesion();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        btnMatematicas.setOnClickListener(v -> abrirMapaNiveles("matematicas"));
        btnCiencias.setOnClickListener(v -> abrirMapaNiveles("ciencias"));
        btnJuegos.setOnClickListener(v -> abrirMapaJuegos());
    }

    private void aplicarAnimaciones() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Vistas a animar
        FrameLayout btnMenuContainer = findViewById(R.id.btnMenuContainer);
        TextView tvNombreUsuario = findViewById(R.id.tv_nombre_usuario);
        ImageView ivTituloSeccion = findViewById(R.id.ivTituloSeccion);
        TextView tvSubtitulo = findViewById(R.id.tvSubtitulo);
        ConstraintLayout btnMatematicas = findViewById(R.id.btnMatematicas);
        ConstraintLayout btnCiencias = findViewById(R.id.btnCiencias);

        // Aplicar animaciones
        btnMenuContainer.startAnimation(fadeIn);
        tvNombreUsuario.startAnimation(fadeIn);
        ivTituloSeccion.startAnimation(fadeIn);
        tvSubtitulo.startAnimation(fadeIn);
        btnMatematicas.startAnimation(slideFromLeft);
        btnCiencias.startAnimation(slideFromRight);
        btnJuegos.startAnimation(slideFromLeft);
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String nombre = prefs.getString("userName", "Invitado");

        // El TextView original que tenías en la pantalla principal
        tvNombreUsuario.setText("Hola, " + nombre);

        // NUEVO: Encontrar el NavigationView y su Cabecera
        NavigationView navigationView = findViewById(R.id.navigation_view);
        android.view.View headerView = navigationView.getHeaderView(0);

        // Buscar el TextView dentro del nuevo XML de la cabecera
        TextView tvHeaderNombre = headerView.findViewById(R.id.tvHeaderNombreUsuario);
        tvHeaderNombre.setText("Hola, " + nombre);
    }

    private void abrirMapaJuegos() {
        Intent intent = new Intent(Inicio.this, MenuJuegos.class);
        startActivity(intent);
    }

    private void abrirMapaNiveles(String materia) {
        Intent intent = new Intent(Inicio.this, mapa_niveles.class);
        intent.putExtra("materia", materia);
        startActivity(intent);
    }

    private void cerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        int usuarioId = prefs.getInt("userId", -1);

        if(usuarioId != -1) {
            AccesoDto logout = new AccesoDto(usuarioId, "LOGOUT");
            apiService.registrarAcceso(logout).enqueue(new Callback<AccesoDto>() {
                @Override
                public void onResponse(Call<AccesoDto> call, Response<AccesoDto> response) {
                    limpiarYSalir(prefs);
                }
                @Override
                public void onFailure(Call<AccesoDto> call, Throwable t) {
                    limpiarYSalir(prefs);
                }
            });
        }
        else {
            limpiarYSalir(prefs);
        }
    }

    private void limpiarYSalir(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(Inicio.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
