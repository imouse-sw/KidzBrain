package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.login.menuLateral.AjustesActivity;
import com.example.login.menuLateral.AvanceActivity;
import com.example.login.menuLateral.PerfilActivity;
import com.example.utilidades.Animaciones;
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout btnMatematicas, btnCiencias;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Referencias a los elementos del layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnMatematicas.setOnClickListener(this);

        btnCiencias = findViewById(R.id.btnCiencias);
        btnCiencias.setOnClickListener(this);

        // Animaciones para que aparezcan los botones
        Animaciones.mostrarConAnimacion(btnMatematicas, 300);
        Animaciones.mostrarConAnimacion(btnCiencias, 900);

        // Control del botón "atrás" con el menú lateral
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    setEnabled(false);
                    onBackPressed();
                }
            }
        });
    }

    // --- ACCIONES DE LOS BOTONES DE LA PANTALLA PRINCIPAL ---
    @Override
    public void onClick(View view) {
        int idsito = view.getId();

        if (idsito == R.id.btnMenu) {
            drawerLayout.openDrawer(navigationView);
        }
        // 👉 Botón de Matemáticas: abre el mapa de niveles
        else if (idsito == R.id.btnMatematicas) {
            Intent intent = new Intent(this, mapa_niveles.class);
            intent.putExtra("materia", "matematicas");
            startActivity(intent);
        }
        // 👉 Botón de Ciencias: abre el mapa de niveles
        else if (idsito == R.id.btnCiencias) {
            Intent intent = new Intent(this, mapa_niveles.class);
            intent.putExtra("materia", "ciencias");
            startActivity(intent);
        }
    }

    // --- OPCIONES DEL MENÚ LATERAL ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_avance) {
            Intent intent = new Intent(this, AvanceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ajustes) {
            Intent intent = new Intent(this, AjustesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cerrar_sesion) {
            Toast.makeText(this, "Cerrar sesión seleccionado", Toast.LENGTH_SHORT).show();
        }

        // Cierra el menú después de seleccionar una opción
        drawerLayout.closeDrawer(navigationView);
        return true;
    }
}
