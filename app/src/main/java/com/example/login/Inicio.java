package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout; // Importante: Nuevo layout
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.login.menuLateral.AjustesActivity;
import com.example.login.menuLateral.AvanceActivity;
import com.example.login.menuLateral.PerfilActivity;
import com.example.utilidades.Animaciones;
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // Vistas principales
    private ConstraintLayout btnMatematicas, btnCiencias; // Ahora son ConstraintLayout
    private View btnMenuContainer; // Usamos 'View' genérico para el contenedor del menú
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    // Elementos visuales para animar
    private ImageView ivTituloSeccion;
    private TextView tvSubtitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // 1. Vincular Vistas (IDs actualizados según tu XML)
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Botón Menú: Vinculamos el CONTENEDOR (FrameLayout en XML) para mejor tacto
        btnMenuContainer = findViewById(R.id.btnMenuContainer);

        // Botones de Materias
        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnCiencias = findViewById(R.id.btnCiencias);

        // Títulos
        ivTituloSeccion = findViewById(R.id.ivTituloSeccion);
        tvSubtitulo = findViewById(R.id.tvSubtitulo);

        // 2. Configurar Listeners
        navigationView.setNavigationItemSelectedListener(this);
        btnMenuContainer.setOnClickListener(this);
        btnMatematicas.setOnClickListener(this);
        btnCiencias.setOnClickListener(this);

        // 3. SECUENCIA DE ANIMACIONES (Cascada)
        // Si tu clase 'Animaciones' usa alpha/translation, esto funcionará perfecto.

        // A) El botón de menú entra suavemente
        btnMenuContainer.setAlpha(0f);
        btnMenuContainer.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(200)
                .start();


        // 4. Manejo del botón "Atrás" del celular
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    // Si estamos en Inicio y damos atrás, salimos de la app o minimizamos
                    setEnabled(false);
                    onBackPressed();
                }
            }
        });
    }

    // --- CLICKS EN PANTALLA ---
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Ahora comparamos con el ID del contenedor del menú
        if (id == R.id.btnMenuContainer) {
            drawerLayout.openDrawer(navigationView);
        }
        else if (id == R.id.btnMatematicas) {
            abrirMapa("matematicas");
        }
        else if (id == R.id.btnCiencias) {
            abrirMapa("ciencias");
        }
    }

    // Método auxiliar para no repetir código al abrir actividades
    private void abrirMapa(String materia) {
        Intent intent = new Intent(this, mapa_niveles.class);
        intent.putExtra("materia", materia);
        startActivity(intent);
        // Transición suave entre actividades
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // --- MENÚ LATERAL (DRAWER) ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
        } else if (id == R.id.nav_avance) {
            startActivity(new Intent(this, AvanceActivity.class));
        } else if (id == R.id.nav_ajustes) {
            startActivity(new Intent(this, AjustesActivity.class));
        } else if (id == R.id.nav_cerrar_sesion) {
            Toast.makeText(this, "Hasta pronto...", Toast.LENGTH_SHORT).show();
            // Opcional: Volver al Login
            // finish();
        }

        drawerLayout.closeDrawer(navigationView);
        return true;
    }
}