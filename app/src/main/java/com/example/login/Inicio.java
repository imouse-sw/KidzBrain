package com.example.login;

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
            Toast.makeText(this, "Abrir Matemáticas", Toast.LENGTH_SHORT).show();
        } else if (idsito == R.id.btnCiencias) {
            Toast.makeText(this, "Abrir Ciencias Naturales", Toast.LENGTH_SHORT).show();
        } else if (idsito == R.id.btnEspanol) {
            Toast.makeText(this, "Abrir Español", Toast.LENGTH_SHORT).show();
        }
    }


    //Lo mismo que en los botones, pero en este caso para el menu lateral.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Perfil seleccionado", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_avance) {
            Toast.makeText(this, "Avance seleccionado", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_ajustes) {
            Toast.makeText(this, "Ajustes seleccionado", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_cerrar_sesion) {
            Toast.makeText(this, "Cerrar sesión seleccionado", Toast.LENGTH_SHORT).show();
        }
        // Cierra el menú después de seleccionar una opción
        drawerLayout.closeDrawer(navigationView);
        return true; // Indica que el evento fue manejado
    }
}
