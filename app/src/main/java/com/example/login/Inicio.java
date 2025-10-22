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

import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion1;
import com.example.lecciones.matematicas.primerosegundo.ActividadLeccion2;
import com.example.login.menuLateral.AjustesActivity;
import com.example.login.menuLateral.AvanceActivity;
import com.example.login.menuLateral.PerfilActivity;
import com.example.utilidades.Animaciones;
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout btnMatematicas, btnEspanol, btnCiencias;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnMatematicas.setOnClickListener(this);

        btnEspanol = findViewById(R.id.btnEspanol);
        btnEspanol.setOnClickListener(this);

        btnCiencias = findViewById(R.id.btnCiencias);
        btnCiencias.setOnClickListener(this);

        // eto es pa que los botoncitos salgan retasaditos
        Animaciones.mostrarConAnimacion(btnMatematicas, 300);
        Animaciones.mostrarConAnimacion(btnEspanol, 600);
        Animaciones.mostrarConAnimacion(btnCiencias, 900);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    setEnabled(false); // Deshabilita este callback para que el sistema maneje el botón atrás
                    onBackPressed();
                }
            }
        });
    }


    // Para que darle click a los botones hagan su funcion.
    @Override
    public void onClick(View view) {
        int idsito = view.getId();
        if (idsito == R.id.btnMenu) {
            drawerLayout.openDrawer(navigationView);
        } else if (idsito == R.id.btnMatematicas) {
            Intent intent = new Intent(this, ActividadLeccion1.class);
            startActivity(intent);
        } else if (idsito == R.id.btnCiencias) {
            Toast.makeText(this, "Abrir Ciencias Naturales", Toast.LENGTH_SHORT).show();
        } else if (idsito == R.id.btnEspanol) {
            Intent intent = new Intent(this, ActividadLeccion2.class);
            startActivity(intent);
        }
    }


    //Lo mismo que en los botones, pero en este caso para el menu lateral.
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
        return true; // Indica que el evento fue manejado
    }
}
